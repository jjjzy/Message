package com.jjjzy.messaging.requests;

import java.util.List;

public class InviteUserToConversationRequest {
    int conversationId;
    List<Integer> userIds;

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
