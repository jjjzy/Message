package com.jjjzy.messaging.service;

import com.jjjzy.messaging.Enums.MessageStatus;
import com.jjjzy.messaging.Enums.MessageType;
import com.jjjzy.messaging.Models.Message;
import com.jjjzy.messaging.dao.MessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    public void sendMessage(int fromUserId, int toUserId, int toConversationId, MessageType messageType, String content){
        Message message = new Message();
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setContent(content);
        message.setSendTime(new Date());
        message.setToConversationId(toConversationId);
        message.setMessageStatus(MessageStatus.UNREAD);
        message.setMessageType(messageType);

        this.messageDAO.insertMessage(message);
    }

    public List<Message> getMessage(int fromUserId, int toUserId, int toConversationId) {
        List<Message> messages = new ArrayList<Message>();
        if(toConversationId < 0){
            messages = this.messageDAO.getToUserMessage(fromUserId, toUserId);
        }
        else{
            messages = this.messageDAO.getToConversationMessage(fromUserId, toConversationId);
        }

        return messages;
    }
}
