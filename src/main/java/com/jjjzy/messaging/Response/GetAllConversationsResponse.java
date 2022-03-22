package com.jjjzy.messaging.Response;

import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Models.Conversation;

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
