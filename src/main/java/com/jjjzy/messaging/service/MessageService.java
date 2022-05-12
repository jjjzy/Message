package com.jjjzy.messaging.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.jjjzy.messaging.Enums.MessageStatus;
import com.jjjzy.messaging.Enums.MessageType;
import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.Message;
import com.jjjzy.messaging.dao.ConversationDAO;
import com.jjjzy.messaging.dao.ConversationUsersDAO;
import com.jjjzy.messaging.dao.MessageDAO;
import com.jjjzy.messaging.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private ConversationDAO conversationDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ConversationUsersDAO conversationUsersDAO;

    public void sendMessage(int fromUserId, int toUserId, int toConversationId, MessageType messageType, String content) throws MessageServiceException {
        if(toConversationId != 0 && this.conversationDAO.getConversationById(toConversationId) == null){
            throw new MessageServiceException(Status.CONVERSATION_DOES_NOT_EXIST);
        }

        if(toUserId != 0 && this.userDAO.findUserByUserId(toUserId) == null){
            throw new MessageServiceException(Status.TARGET_USER_DOES_NOT_EXIST);
        }

        if(toUserId > 0 && toConversationId > 0){
            throw new MessageServiceException(Status.CAN_ONLY_SEND_TO_USER_OR_CONVERSATION);
        }

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

    public void sendFile(Integer messageId, MultipartFile file) throws IOException {
        this.amazonS3.putObject("jjjzy-messaging-user-files", messageId.toString(), file.getInputStream(), new ObjectMetadata());
    }

    public List<Message> getMessage(int fromUserId, int toUserId, int toConversationId, String startDate, String endDate) {
        List<Message> messages = null;
        System.out.println(startDate);
        System.out.println(toConversationId);
        if(toConversationId == 0){
            messages = this.messageDAO.getUserMessages(fromUserId, toUserId, startDate, endDate);
            this.messageDAO.updateUserMessageStatus(MessageStatus.READ, fromUserId, toUserId);
        }
        else{
            messages = this.messageDAO.getConversationMessages(fromUserId, toConversationId, startDate, endDate);
            //TODO
            //how to setup status for group chat
        }

        return messages;
    }

    public byte[] getFile(Integer messageId) throws IOException {
        //TODO
        //is downloading fine?
        S3Object s3Object = this.amazonS3.getObject("jjjzy-messaging-user-files", messageId.toString());
        String contentType = this.messageDAO.getMessageById(messageId).getMessageType().toString();
        String postFix = null;
        if(contentType.equals("IMAGE")){
            postFix = "jpg";
        }
        else if(contentType.equals("VOICE")){
            postFix = "mp3";
        }
        else if(contentType.equals("VIDEO")){
            postFix = "mp4";
        }
        this.messageDAO.updateMessageStatusById(MessageStatus.READ, messageId);

        InputStream inputStream = s3Object.getObjectContent();
        return inputStream.readAllBytes();
    }


    public List<Message> getLatestMessage(int userId, String lastSyncTime) {
        List<Message> messages = this.messageDAO.getLastestMessageBySyncTime(userId, lastSyncTime);
        for(Message i : messages){
            if(i.getMessageStatus() == MessageStatus.UNREAD){
                this.messageDAO.updateMessageStatusById(MessageStatus.READ, i.getId());
            }
        }
        return messages;
    }

    public List<Message> getAllMessage(int userId){
        List<Message> messages = this.messageDAO.getAllMessage(userId);
        return messages;
    }

    public List<Message> getAllUnreadMessage(int userId, String status){
        List<Message> messages = this.messageDAO.getAllUnreadMessage(userId, "UNREAD");

        for(Message i : messages){
            if(i.getMessageStatus() == MessageStatus.UNREAD){
                this.messageDAO.updateMessageStatusById(MessageStatus.READ, i.getId());
            }
        }

        return messages;
    }
}
