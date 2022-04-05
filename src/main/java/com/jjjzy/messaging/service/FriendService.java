package com.jjjzy.messaging.service;

import com.jjjzy.messaging.Enums.FriendInvitationStatus;
import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.FriendInvitation;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.dao.FriendDAO;
import com.jjjzy.messaging.dao.UserDAO;
import com.jjjzy.messaging.dao.UserValidationCodeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class FriendService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserValidationCodeDAO userValidationCodeDAO;

    @Autowired
    private FriendDAO friendDao;

    public List<FriendInvitation> getPendingFriendInvitations(int userId) throws MessageServiceException {
        List<FriendInvitation> pendingInvitation = this.friendDao.getInvitationByIdAndStatus(userId, FriendInvitationStatus.PENDING);

        return pendingInvitation;
    }

    public void sendInvitation(int fromUserId, int toUserId, String message) throws MessageServiceException{
        //TODO
        //maybe add aop to check toUserId
        if(this.userDAO.findUserByUserId(toUserId) == null){
            throw new MessageServiceException(Status.TARGET_USER_DOES_NOT_EXIST);
        }


        FriendInvitation friendInvitation = new FriendInvitation();

        friendInvitation.setSendTime(new Date());
        friendInvitation.setFromUserId(fromUserId);
        friendInvitation.setToUserId(toUserId);
        friendInvitation.setMessage(message);
        friendInvitation.setStatus(FriendInvitationStatus.PENDING);

        this.friendDao.insertInvitation(friendInvitation);
    }

    public void acceptInvitation(int invitationID) throws MessageServiceException{
        //TODO
        //maybe add aop to check invitationId
        if(this.friendDao.getInvitationById(invitationID) == null){
            throw new MessageServiceException(Status.FRIEND_INVITATION_DOES_NOT_EXIST);
        }
        this.friendDao.updateInvitationStatus(invitationID, FriendInvitationStatus.ACCEPT);
    }

    public List<User> getFriends(int userId){
        List<FriendInvitation> acceptedInvitation = this.friendDao.getInvitationByIdAndStatus(userId, FriendInvitationStatus.ACCEPT);
        //TODO
        //what if i dont have any accepted invitations;
        List<User> friends = new ArrayList<User>();

        for (int i = 0; i < acceptedInvitation.size(); i++) {
            FriendInvitation curInvitation = acceptedInvitation.get(i);
            int from = curInvitation.getFromUserId();
            if(from == userId){
                friends.add(this.userDAO.findUserByUserId(curInvitation.getToUserId()));
            }
            else{
                friends.add(this.userDAO.findUserByUserId(curInvitation.getFromUserId()));
            }
        }
        //TODO
        //PUT DUMMY FRIEND CLASS HERE, NOT RETURNING EVERYTHING FOR A FRIEND
        return friends;
    }
}
