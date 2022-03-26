package com.jjjzy.messaging.unitTest;

import com.jjjzy.messaging.Enums.FriendInvitationStatus;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.FriendInvitation;
import com.jjjzy.messaging.Models.Message;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.controller.MessageServiceExceptionHandler;
import com.jjjzy.messaging.dao.FriendDAO;
import com.jjjzy.messaging.dao.UserDAO;
import com.jjjzy.messaging.service.FriendService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FriendServiceUnitTest {
    @InjectMocks
    FriendService friendService;

    @Mock
    private FriendDAO friendDAO;

    @Mock
    private UserDAO userDAO;

    @Test
    public void testSendInvitation_happyCase() throws Exception {
        when(this.userDAO.findUserByUserId(1)).thenReturn(new User());

        this.friendService.sendInvitation(2, 1, "123");
    }

    @Test
    public void testSendInvitation_toUserIdInvalid_throwsMessageServiceException() throws Exception{
        when(this.userDAO.findUserByUserId(1)).thenReturn(null);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.friendService.sendInvitation(2, 1, "123"));

        assertEquals(messageServiceException.getMessage(), "User Id provided is invalid.");
    }

    @Test
    public void testAcceptInvitation_happyCase() throws Exception{
        when(this.friendDAO.getInvitationById(1)).thenReturn(new FriendInvitation());

        this.friendService.acceptInvitation(1);
    }

    @Test
    public void testAcceptInvitation_invitationIdDoesNotExist_throwsMessageServiceException() throws Exception{
        when(this.friendDAO.getInvitationById(1)).thenReturn(null);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.friendService.acceptInvitation(1)
        );

        assertEquals(messageServiceException.getMessage(), "Target friend invitation doesn't exist.");
    }

    @Test
    public void testGetFriends_IAmReceiverOfRequest_friendIsFromUserId(){
        List<FriendInvitation> invitations = new ArrayList<FriendInvitation>();
        FriendInvitation temp = new FriendInvitation();
        temp.setFromUserId(1);
        temp.setToUserId(2);
        invitations.add(temp);

        when(this.friendDAO.getInvitationByIdAndStatus(2, FriendInvitationStatus.ACCEPT)).thenReturn(invitations);

        this.friendService.getFriends(2);
    }

    @Test
    public void testGetFriends_IAmSenderOfRequest_friendIsToUserId(){
        List<FriendInvitation> invitations = new ArrayList<FriendInvitation>();
        FriendInvitation temp = new FriendInvitation();
        temp.setFromUserId(1);
        temp.setToUserId(2);
        invitations.add(temp);

        when(this.friendDAO.getInvitationByIdAndStatus(1, FriendInvitationStatus.ACCEPT)).thenReturn(invitations);

        this.friendService.getFriends(1);
    }
    @Test
    public void testGetPendingFriendInvitations() throws Exception {
        this.friendService.getPendingFriendInvitations(1);
    }
}
