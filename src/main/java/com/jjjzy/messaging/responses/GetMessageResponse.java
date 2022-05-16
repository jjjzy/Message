package com.jjjzy.messaging.responses;

import com.jjjzy.messaging.enums.Status;
import com.jjjzy.messaging.models.Message;

import java.util.List;

public class GetMessageResponse extends BaseResponse{

    List<Message> messages;

    public GetMessageResponse(List<Message> message, Status status) {
        super(status);
        this.messages = message;
    }

    public List<Message> getMessages() {
        return messages;
    }

}
