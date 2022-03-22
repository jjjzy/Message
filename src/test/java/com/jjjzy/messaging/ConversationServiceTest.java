package com.jjjzy.messaging;

import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.Conversation;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.dao.ConversationDAO;
import com.jjjzy.messaging.dao.ConversationUsersDAO;
import com.jjjzy.messaging.dao.UserDAO;
import com.jjjzy.messaging.service.ConversationService;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    public void testStartConversation_happyCase() throws Exception{
        User tempUser = new User();
        tempUser.setId(2);

        when(userDAO.findUserByUserId(2)).thenReturn(tempUser);

        List<Integer> temp = new ArrayList<Integer>();
        temp.add(2);

        this.conversationService.startConversation(1,
                "123",
                "123",
                temp
                );
    }

    @Test
    public void testStartConversation_toUserDoesNotExist_throwsMessageServiceException() throws Exception{
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(2);

        when(userDAO.findUserByUserId(2)).thenReturn(null);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.conversationService.startConversation(1,
                        "123",
                        "123",
                        temp
                ));
                assertEquals("User not exists.", messageServiceException.getMessage());


    }
}
