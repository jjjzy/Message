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

    public List<FriendInvitation> getPendingFriendInvitations(User user) throws MessageServiceException {
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        List<FriendInvitation> pendingInvitation = this.friendDao.listPendingFriendInvitations(user.getId(), FriendInvitationStatus.PENDING);

        return pendingInvitation;
    }

    public void sendInvitation(int fromUserId, int toUserId, String message) {
        FriendInvitation friendInvitation = new FriendInvitation();

        friendInvitation.setSendTime(new Date());
        friendInvitation.setFromUserId(fromUserId);
        friendInvitation.setToUserId(toUserId);
        friendInvitation.setMessage(message);
        friendInvitation.setStatus(FriendInvitationStatus.PENDING);

        this.friendDao.insertInvitation(friendInvitation);
    }

    public void acceptInvitation(int invitationID){
        this.friendDao.updateInvitationStatus(invitationID, FriendInvitationStatus.ACCEPT);
    }

    public List<User> getFriends(User user) throws MessageServiceException {
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        List<FriendInvitation> acceptedInvitation = this.friendDao.listPendingFriendInvitations(user.getId(), FriendInvitationStatus.ACCEPT);

        List<User> friends = new ArrayList<User>();

        int thisUserId = user.getId();

        for (int i = 0; i < acceptedInvitation.size(); i++) {
            FriendInvitation curInvitation= acceptedInvitation.get(i);
            int from = curInvitation.getFromUserId();
            int to = curInvitation.getToUserId();
            if(from == thisUserId){
                friends.add(this.userDAO.findUserByUserId(curInvitation.getToUserId()));
            }
            else{
                friends.add(this.userDAO.findUserByUserId(curInvitation.getFromUserId()));
            }
        }

        for (int i = 0; i < acceptedInvitation.size(); i++){
            System.out.println(friends.get(i).getUsername());
        }

        return friends;
    }
}
