package com.jjjzy.messaging.requests;

import com.jjjzy.messaging.enums.MessageType;

public class SendMessageRequest {
    String loginToken;

    int toUserId;
    int toConversationId;

    MessageType messageType;
    String Content;

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

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
}
