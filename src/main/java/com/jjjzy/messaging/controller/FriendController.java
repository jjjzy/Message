package com.jjjzy.messaging.controller;

import com.jjjzy.messaging.enums.Status;
import com.jjjzy.messaging.exceptions.MessageServiceException;
import com.jjjzy.messaging.models.FriendInvitation;
import com.jjjzy.messaging.models.User;
import com.jjjzy.messaging.responses.BaseResponse;
import com.jjjzy.messaging.responses.GetFriendInvitationsResponse;
import com.jjjzy.messaging.responses.GetFriendsResponse;
import com.jjjzy.messaging.responses.InviteFriendResponse;
import com.jjjzy.messaging.annotations.NeedLoginTokenAuthentication;
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
    public BaseResponse acceptFriendInvitation(User user, @RequestParam int invitationId) throws MessageServiceException{
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
