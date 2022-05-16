package com.jjjzy.messaging.responses;

import com.jjjzy.messaging.enums.Status;
import com.jjjzy.messaging.models.Conversation;

import java.util.List;

public class GetAllConversationsResponse extends BaseResponse{
    private List<Conversation> conversations;

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public GetAllConversationsResponse(Status status, List<Conversation> conversations) {
        super(status);
        this.conversations = conversations;
    }
}
