package com.jjjzy.messaging.Response;

import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Models.Message;

import java.util.List;

public class GetMessageResponse extends BaseResponse{

    List<Message> messages;

    public GetMessageResponse(List<Message> message) {
        super(Status.OK);
        this.messages = message;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
