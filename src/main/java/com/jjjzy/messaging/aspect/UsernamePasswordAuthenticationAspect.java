package com.jjjzy.messaging.aspect;

import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.jjjzy.messaging.Utils.PasswordUtils.md5;

@Aspect
@Component
@Order(0)
public class UsernamePasswordAuthenticationAspect {
    @Autowired
    private UserService userService;

    @Around("execution(* com.jjjzy.messaging.controller.*Controller.*(..)) && @annotation(com.jjjzy.messaging.annotation.NeedUsernamePasswordAuthentication)")
    public Object authenticate(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.verifyUsername(username);
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }
        if(!user.getPassword().equals(md5(password))){
            throw new MessageServiceException(Status.WRONG_PASSWORD);
        }

        try {
            var args = proceedingJoinPoint.getArgs();
            args[0] = user;
            return proceedingJoinPoint.proceed(args);
        } finally {

        }
    }
}
