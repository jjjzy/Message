package com.jjjzy.messaging.responses;

import com.jjjzy.messaging.enums.Status;

public class BaseResponse {
    private int code;
    private String message;
    public BaseResponse(Status status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
