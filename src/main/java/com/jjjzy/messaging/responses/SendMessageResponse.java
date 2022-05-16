package com.jjjzy.messaging.responses;

import com.jjjzy.messaging.enums.Status;

public class SendMessageResponse extends BaseResponse {
    public SendMessageResponse(Status status) {
        super(status);
    }
}
