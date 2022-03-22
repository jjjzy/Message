package com.jjjzy.messaging.Exceptions;

import com.jjjzy.messaging.Enums.Status;

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
