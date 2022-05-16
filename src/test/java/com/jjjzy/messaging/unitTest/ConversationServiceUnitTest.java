package com.jjjzy.messaging.unitTest;

import com.jjjzy.messaging.exceptions.MessageServiceException;
import com.jjjzy.messaging.models.Conversation;
import com.jjjzy.messaging.models.User;
import com.jjjzy.messaging.dao.ConversationDAO;
import com.jjjzy.messaging.dao.ConversationUsersDAO;
import com.jjjzy.messaging.dao.UserDAO;
import com.jjjzy.messaging.service.ConversationService;
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
public class ConversationServiceUnitTest {
    @InjectMocks
    ConversationService conversationService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private ConversationDAO conversationDAO;

    @Mock
    private ConversationUsersDAO conversationUsersDAO;

    //TODO
    //Test fails if i didnt put credential in resources in test. however user service tests work.
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

    @Test
    public void testUpdateConversation_happyCase(){
        this.conversationService.updateConversation(1, "123", "123");
    }

    @Test
    public void testInviteUserToConversation_happyCase() throws Exception{
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(2);
        temp.add(3);

        User tempUser = new User();
        tempUser.setId(2);
        when(userDAO.findUserByUserId(2)).thenReturn(tempUser);

        User tempUser2 = new User();
        tempUser.setId(3);
        when(userDAO.findUserByUserId(3)).thenReturn(tempUser2);


        this.conversationService.inviteUserToConversation(1, temp);
    }

    @Test
    public void testInviteUserToConversation_inviteNullUser_throwsMessageServiceException() throws Exception{
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(2);
        temp.add(3);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.conversationService.inviteUserToConversation(1, temp));

        assertEquals(messageServiceException.getMessage(), "User not exists.");
    }

    @Test
    public void testRemoveUserFromConversation_happyCase() throws Exception{
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(2);
        temp.add(3);

        User tempUser = new User();
        tempUser.setId(2);
        when(userDAO.findUserByUserId(2)).thenReturn(tempUser);

        User tempUser2 = new User();
        tempUser.setId(3);
        when(userDAO.findUserByUserId(3)).thenReturn(tempUser2);

        this.conversationService.removeUserFromConversation(1, temp);
    }

    @Test
    public void testRemoveUserFromConversation_inviteNullUser_throwsMessageServiceException() throws Exception{
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(2);
        temp.add(3);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.conversationService.removeUserFromConversation(1, temp));

        assertEquals(messageServiceException.getMessage(), "User not exists.");
    }


    @Test
    public void testRemoveConversation_happyCase() throws Exception{
        Conversation tempConversation = new Conversation();
        tempConversation.setId(1);

        when(this.conversationDAO.getConversationById(1)).thenReturn(tempConversation);

        this.conversationService.removeConversation(1);
    }

    @Test
    public void testRemoveConversation_conversationNotFound_throwsMessageServiceException() throws Exception{
        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.conversationService.removeConversation(1));

        assertEquals(messageServiceException.getMessage(), "The conversation doesn't exist.");
    }

    @Test
    public void testGetAllConversations_happyCase(){
        List<Conversation> temp = new ArrayList<Conversation>();

        Conversation tempConversation = new Conversation();
        tempConversation.setId(1);

        temp.add(tempConversation);

        List<Integer> temp1 = new ArrayList<Integer>();
        temp1.add(1);
        temp1.add(2);


        when(this.conversationUsersDAO.getConversationIdById(1)).thenReturn(temp1);


        when(this.conversationDAO.getConversationById(1)).thenReturn(new Conversation());
        when(this.conversationDAO.getConversationById(2)).thenReturn(new Conversation());

        this.conversationService.getAllConversations(1);
    }
}
