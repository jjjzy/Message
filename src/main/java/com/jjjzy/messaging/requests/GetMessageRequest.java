package com.jjjzy.messaging.requests;

public class GetMessageRequest {
    String loginToken;

    int toUserId;
    int toConversationId;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public int getToConversationId() {
        return toConversationId;
    }

    public void setToConversationId(int toConversationId) {
        this.toConversationId = toConversationId;
    }
}
