package com.jjjzy.messaging.exceptions;

import com.jjjzy.messaging.enums.Status;

public class MessageServiceException extends Exception{
    private Status status;

    public MessageServiceException(Status status) {
        super(status.getMessage());
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
