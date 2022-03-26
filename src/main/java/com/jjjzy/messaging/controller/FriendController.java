package com.jjjzy.messaging.controller;

import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.FriendInvitation;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Response.BaseResponse;
import com.jjjzy.messaging.Response.GetFriendInvitationsResponse;
import com.jjjzy.messaging.Response.GetFriendsResponse;
import com.jjjzy.messaging.Response.InviteFriendResponse;
import com.jjjzy.messaging.annotation.NeedLoginTokenAuthentication;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.UserService;
import jdk.jfr.Unsigned;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {
    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @PostMapping("/send")
    @NeedLoginTokenAuthentication
    public InviteFriendResponse inviteFriend(User user,
                                             @RequestParam int toUserId,
                                             @RequestParam String message) throws MessageServiceException{
        this.friendService.sendInvitation(user.getId(), toUserId, message);

        return new InviteFriendResponse(Status.OK);
    }

    @GetMapping("/getPending")
    @NeedLoginTokenAuthentication
    public GetFriendInvitationsResponse getPendingFriendInvitations(User user) throws MessageServiceException {
        List<FriendInvitation> friendInvitations = this.friendService.getPendingFriendInvitations(user.getId());
        return new GetFriendInvitationsResponse(friendInvitations);
    }

    @PostMapping("/accept")
    @NeedLoginTokenAuthentication
    public BaseResponse acceptFriendInvitation(@RequestParam int invitationId) throws MessageServiceException{
        this.friendService.acceptInvitation(invitationId);
        return new BaseResponse(Status.OK);
    }

    @GetMapping("/getFriends")
    @NeedLoginTokenAuthentication
    public GetFriendsResponse getFriends(User user) throws MessageServiceException {
        List<User> friends = this.friendService.getFriends(user.getId());

        return new GetFriendsResponse(friends);
    }
}
