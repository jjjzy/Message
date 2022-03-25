package com.jjjzy.messaging.controller;

import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Response.BaseResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MessageServiceExceptionHandler {
    @ExceptionHandler(MessageServiceException.class)
    @ResponseBody
    protected BaseResponse handleServiceException(MessageServiceException ex) {
        BaseResponse baseResponse = new BaseResponse(ex.getStatus());
        return baseResponse;
    }
}
