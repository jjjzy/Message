package com.jjjzy.messaging.unitTest;

import com.jjjzy.messaging.enums.MessageType;
import com.jjjzy.messaging.exceptions.MessageServiceException;
import com.jjjzy.messaging.models.Conversation;
import com.jjjzy.messaging.models.User;
import com.jjjzy.messaging.dao.*;
import com.jjjzy.messaging.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageServiceUnitTest {
    @InjectMocks
    MessageService messageService;

    @Mock
    private FriendDAO friendDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private MessageDAO messageDAO;

    @Mock
    private ConversationDAO conversationDAO;

    @Mock
    private ConversationUsersDAO conversationUsersDAO;

    @Test
    public void testSendMessage_happyCase() throws MessageServiceException {
        when(this.conversationDAO.getConversationById(1)).thenReturn(new Conversation());
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        when(this.conversationUsersDAO.getUserIDfromByConversationId(1)).thenReturn(ids);
        this.messageService.sendMessage(1, 0, 1, MessageType.TEXT, "test content");
    }

    @Test
    public void testSendMessage_toConversationNotExist_throwMessageServiceException() throws MessageServiceException {
        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.messageService.sendMessage(1, 0, 1, MessageType.TEXT, "test content"));
        assertEquals("The conversation doesn't exist.", messageServiceException.getMessage());
    }

    @Test
    public void testSendMessage_toUserNotExist_throwMessageServiceException() throws MessageServiceException {
        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.messageService.sendMessage(1, 1, 0, MessageType.TEXT, "test content"));
        assertEquals("User Id provided is invalid.", messageServiceException.getMessage());
    }

    @Test
    public void testSendMessage_sendUserConversationAtSameTime_throwMessageServiceException() throws MessageServiceException {
        when(this.conversationDAO.getConversationById(1)).thenReturn(new Conversation());
        when(this.userDAO.findUserByUserId(1)).thenReturn(new User());
        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.messageService.sendMessage(1, 1, 1, MessageType.TEXT, "test content"));
        assertEquals("Please send message to either user or conversation", messageServiceException.getMessage());
    }

}
