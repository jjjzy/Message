package com.jjjzy.messaging;

import com.jjjzy.messaging.Models.Conversation;
import com.jjjzy.messaging.dao.ConversationDAO;
import com.jjjzy.messaging.dao.ConversationUsersDAO;
import com.jjjzy.messaging.dao.UserDAO;
import com.jjjzy.messaging.service.ConversationService;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConversationServiceTest {
    @InjectMocks
    UserService userService;

    @InjectMocks
    ConversationService conversationService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private ConversationDAO conversationDAO;

    @Mock
    private ConversationUsersDAO conversationUsersDAO;

    @Test
    public void testStartConversation_happyCase(){
        Conversation tempConversation = new Conversation();
        tempConversation.setTitle("123");
        tempConversation.setNotice("123");
        tempConversation.setCreateTime(new Date());
        tempConversation.setId(1);

        this.conversationDAO.createConversation(tempConversation);
    }
}
