//package com.jjjzy.messaging.aspect;
//
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.jjjzy.messaging.Request.StartConversationRequest;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Aspect
//@Component
//@Order(0)
//public class LogAspect {
//    Logger logger = LoggerFactory.getLogger(LogAspect.class);
//
//    @Around("execution(* com.jjjzy.messaging.controller.*Controller.*(..)) && args(.., @RequestBody body)")
//    public Object log(ProceedingJoinPoint proceedingJoinPoint, Object body) throws Throwable {
//
//        long startTime = System.currentTimeMillis();
//        boolean isSuccessful = true;
//
////        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
////        String loginToken1 = request.getReader().lines().collect(Collectors.joining());
////        System.out.println(loginToken1);
//
////        HttpServletRequest request1 = (ServletRequestAttributes) RequestC
//        System.out.println("body start: ");
//
//        ObjectMapper mapper = new ObjectMapper();
//
//
//        Map<String, Object> map = mapper.convertValue(body, new TypeReference<Map<String, Object>>() {});
//
//        System.out.println(map.get("loginToken"));
//
//        System.out.println("body end: ");
//
//        System.out.println("request start: ");
////        System.out.println(request.toString());
//        System.out.println("request end: ");
//
//        System.out.println("start");
//        try {
//            return proceedingJoinPoint.proceed();
//        } catch (Exception exception) {
//            isSuccessful = false;
//            throw exception;
//        } finally {
//            long elapsedMs = System.currentTimeMillis() - startTime;
//            logger.info("Executed {}.{}, elapsed: {} ms, isSuccessful: {}. ",
//                    proceedingJoinPoint.getTarget().getClass(),
//                    proceedingJoinPoint.getSignature().getName(),
//                    elapsedMs,
//                    isSuccessful);
//        }
//    }
//}
