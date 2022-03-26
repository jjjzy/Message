package com.jjjzy.messaging.aspect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;
import java.util.stream.Collectors;


@Aspect
@Component
@Order(0)
public class LoginTokenAuthenticationAspect {
    @Autowired
    private UserService userService;


    @Around("execution(* com.jjjzy.messaging.controller.*Controller.*(..)) && @annotation(com.jjjzy.messaging.annotation.NeedLoginTokenAuthentication)")
    public Object authenticate(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = mapper.convertValue(body, new TypeReference<Map<String, Object>>() {});
//        String loginToken = map.get("loginToken").toString();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String loginToken = request.getHeader("loginToken");

        System.out.println(loginToken);


        User user = userService.verifyLoginToken(loginToken);
        if (user == null) {
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