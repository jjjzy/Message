package com.jjjzy.messaging.models;

import com.jjjzy.messaging.enums.MessageStatus;
import com.jjjzy.messaging.enums.MessageType;

import java.util.Date;

public class Message {
    int id;
    int fromUserId;
    int toUserId;
    int toConversationId;

    MessageType messageType;

    String content;
    Date sendTime;

    MessageStatus messageStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
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

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }
}
