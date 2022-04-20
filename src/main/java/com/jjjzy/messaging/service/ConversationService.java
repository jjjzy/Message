package com.jjjzy.messaging.service;

import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.Conversation;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.dao.ConversationDAO;
import com.jjjzy.messaging.dao.ConversationUsersDAO;
import com.jjjzy.messaging.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConversationService {
    @Autowired
    private ConversationDAO conversationDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ConversationUsersDAO conversationUsersDAO;

    public void startConversation(int fromUserId, String title, String notice, List<Integer> toUserId) throws MessageServiceException{
        Conversation conversation = new Conversation();
        conversation.setNotice(notice);
        conversation.setTitle(title);
        conversation.setCreateTime(new Date());

        this.conversationDAO.createConversation(conversation);

        System.out.println(toUserId);

        this.conversationUsersDAO.createConversationUser(conversation.getId(), fromUserId);
        for(int i : toUserId){
            User tempUser = this.userDAO.findUserByUserId(i);
            if(tempUser != null){
                this.conversationUsersDAO.createConversationUser(conversation.getId(), i);
            }
            else{
                throw new MessageServiceException(Status.USER_NOT_EXISTS);
            }
        }
    }

    public void updateConversation(int conversationId, String title, String notice) {
        this.conversationDAO.updateConversation(conversationId, title, notice);
    }

    public void inviteUserToConversation(int conversationId, List<Integer> userIds) throws MessageServiceException{
        for(int i : userIds){
            User user = this.userDAO.findUserByUserId(i);
            if(user != null){
                this.conversationUsersDAO.inviteUserToConversation(conversationId, i);
            }
            else{
                throw new MessageServiceException(Status.USER_NOT_EXISTS);
            }
        }

    }

    public List<Conversation> getAllConversations(int userId){
        List<Conversation> conversations = new ArrayList<Conversation>();
        for(int i : this.conversationUsersDAO.getConversationIdById(userId)){
            conversations.add(this.conversationDAO.getConversationById(i));
        }

        return conversations;
    }
//
//    public List<Integer> getConversationIdById(int id) {
//        return this.conversationUsersDAO.getConversationIdById(id);
//    }
//
//    public List<Conversation> getConversationById(List<Integer> conversationIds){
//        List<Conversation> conversations = new ArrayList<Conversation>();
//        for(int i : conversationIds){
////            for(Conversation j : this.conversationDAO.getConversationById(i))
//            conversations.add(this.conversationDAO.getConversationById(i));
//        }
//
//        return conversations;
//    }

    public void removeUserFromConversation(int conversationId, List<Integer> userIds) throws MessageServiceException{
        for(int i : userIds){
            User user = this.userDAO.findUserByUserId(i);
            if(user != null){
                this.conversationUsersDAO.removeUserFromConversation(i);
            }
            else{
                throw new MessageServiceException(Status.USER_NOT_EXISTS);
            }
        }
    }

    public void removeConversation(int conversationId) throws MessageServiceException{
        Conversation conversation = this.conversationDAO.getConversationById(conversationId);
        if(conversation == null){
            throw new MessageServiceException(Status.CONVERSATION_DOES_NOT_EXIST);
        }

        this.conversationDAO.removeConversation(conversationId);
        this.conversationUsersDAO.removeConversation(conversationId);

    }
}
