package com.jjjzy.messaging.aspect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.dao.UserDAO;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Aspect
@Component
@Order(0)
public class LoginTokenAuthenticationAspect {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDAO userDAO;


    @Around("execution(* com.jjjzy.messaging.controller.*Controller.*(..)) && @annotation(com.jjjzy.messaging.annotation.NeedLoginTokenAuthentication)")
    public Object authenticate(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = mapper.convertValue(body, new TypeReference<Map<String, Object>>() {});
//        String loginToken = map.get("loginToken").toString();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String loginToken = request.getHeader("loginToken");

        System.out.println(loginToken);
        //TODO
        //TOKEN EXPIRE DATE
        //AOP TO CHECK TIME?


        User user = userService.verifyLoginToken(loginToken);
        if (user == null || TimeUnit.MINUTES.convert((new Date()).getTime() - user.getLastLoginTime().getTime(), TimeUnit.MILLISECONDS) > 90) {
            throw new MessageServiceException(Status.LOGINTOKEN_AUTHENTICATION_FAILED);
        }
        try {
            var args = proceedingJoinPoint.getArgs();
            args[0] = user;
            return proceedingJoinPoint.proceed(args);
        } finally {
        }
    }
}
