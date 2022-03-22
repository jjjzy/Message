package com.jjjzy.messaging.Models;

import com.jjjzy.messaging.Enums.FriendInvitationStatus;

import java.util.Date;

public class FriendInvitation {
    private int id;
    private int fromUserId;
    private int toUserId;
    private Date sendTime;
    private FriendInvitationStatus status;
    private String message;

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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public FriendInvitationStatus getStatus() {
        return status;
    }

    public void setStatus(FriendInvitationStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
