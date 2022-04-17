package com.jjjzy.messaging.Request;

import java.util.List;

public class StartConversationRequest {
    String title;
    String notice;
    List<Integer> toUserIds;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<Integer> getToUserIds() {
        return toUserIds;
    }

    public void setToUserIds(List<Integer> toUserIds) {
        this.toUserIds = toUserIds;
    }
}
