package com.jjjzy.messaging.controller;

import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.FriendInvitation;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Response.BaseResponse;
import com.jjjzy.messaging.Response.GetFriendInvitationsResponse;
import com.jjjzy.messaging.Response.GetFriendsResponse;
import com.jjjzy.messaging.Response.InviteFriendResponse;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.UserService;
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

    @PostMapping("/invitations/invite")
    public InviteFriendResponse inviteFriend(@RequestParam String loginToken,
                                             @RequestParam String toUsername,
                                             @RequestParam String message) throws MessageServiceException{
        User user = this.userService.verifyLoginToken(loginToken);
        int fromUserId = user.getId();

        User toUser = this.userService.verifyUsername(toUsername);
        int toUserId = toUser.getId();

        this.friendService.sendInvitation(fromUserId, toUserId, message);

        return new InviteFriendResponse(Status.OK);
    }

    @GetMapping("/invitations")
    public GetFriendInvitationsResponse getPendingFriendInvitations(@RequestParam String loginToken) throws MessageServiceException {
        User user = this.userService.verifyLoginToken(loginToken);
        List<FriendInvitation> friendInvitations = this.friendService.getPendingFriendInvitations(user);
        return new GetFriendInvitationsResponse(friendInvitations);
    }

    @PostMapping("/invitations/accept")
    public BaseResponse acceptFriendInvitation(@RequestParam String loginToken,
                                               @RequestParam int invitationId) {
        User user = this.userService.verifyLoginToken(loginToken);
        this.friendService.acceptInvitation(invitationId);
        return new BaseResponse(Status.OK);
    }

    @GetMapping("/invitations/friends")
    public GetFriendsResponse getFriends(@RequestParam String loginToken) throws MessageServiceException {
        User user = this.userService.verifyLoginToken(loginToken);

        List<User> friends = this.friendService.getFriends(user);

        return new GetFriendsResponse(friends);
    }
}
