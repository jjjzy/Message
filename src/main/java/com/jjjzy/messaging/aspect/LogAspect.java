package com.jjjzy.messaging.aspect;


import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.jjjzy.messaging.Request.StartConversationRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
@Order(1)
public class LogAspect {
    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private AmazonCloudWatch amazonCloudWatch;

//    RateLimiter rateLimiter = RateLimiter.create(0.1);

    @Around("execution(* com.jjjzy.messaging.controller.*Controller.*(..))")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        boolean isSuccessful = true;

        System.out.println("start");

        try {
            return proceedingJoinPoint.proceed();
        } catch (Exception exception) {
            isSuccessful = false;
            throw exception;
        } finally {
//            rateLimiter.acquire();
            double elapsedMs = System.currentTimeMillis() - startTime;
            logger.info("Executed {}.{}, elapsed: {} ms, isSuccessful: {}. ",
                    proceedingJoinPoint.getTarget().getClass().getName(),
                    proceedingJoinPoint.getSignature().getName(),
                    elapsedMs,
                    isSuccessful);
            Date now = new Date();

            List<Dimension> dimensions = List.of(new Dimension()
                    .withName("API")
                    .withValue(proceedingJoinPoint.getTarget().getClass().getName() + "." + proceedingJoinPoint.getSignature().getName()));
            MetricDatum timeDatum = new MetricDatum()
                    .withMetricName("Time")
                    .withUnit(StandardUnit.Milliseconds)
                    .withValue(elapsedMs)
                    .withTimestamp(now)
                    .withDimensions(dimensions);

            MetricDatum countDatum = new MetricDatum()
                    .withMetricName("Count")
                    .withUnit(StandardUnit.Count)
                    .withValue(1.0)
                    .withTimestamp(now)
                    .withDimensions(dimensions);

            MetricDatum errorDatum = new MetricDatum()
                    .withMetricName("Error")
                    .withUnit(StandardUnit.Count)
                    .withValue(isSuccessful ? 0.0 : 1.0)
                    .withTimestamp(now)
                    .withDimensions(dimensions);


            this.amazonCloudWatch.putMetricData(new PutMetricDataRequest()
                    .withNamespace("messaging")
                    .withMetricData(timeDatum, countDatum, errorDatum));
        }
    }
}
