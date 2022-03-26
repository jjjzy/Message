package com.jjjzy.messaging.controller;



import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Request.InviteUserToConversationRequest;
import com.jjjzy.messaging.Request.RemoveUserFromConversationRequest;
import com.jjjzy.messaging.Request.StartConversationRequest;
import com.jjjzy.messaging.Request.UpdateConversationRequest;
import com.jjjzy.messaging.Response.*;
import com.jjjzy.messaging.annotation.NeedLoginTokenAuthentication;
import com.jjjzy.messaging.service.ConversationService;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    //TODO
    //multiple same users converation check

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ConversationService conversationService;

//    @PostMapping("/start")
//    @NeedAuthentication
//    public StartConversationResponse startConversation(@RequestBody StartConversationRequest startConversationRequest) throws MessageServiceException {
//        User user = this.userService.verifyLoginToken(startConversationRequest.getLoginToken());
//        if(user == null){
//            throw new MessageServiceException(Status.USER_NOT_EXISTS);
//        }
//
//
//        this.conversationService.startConversation(user.getId(),
//                startConversationRequest.getTitle(),
//                startConversationRequest.getNotice(),
//                startConversationRequest.getToUserIds());
//
//        return new StartConversationResponse(Status.OK);
//    }

    @PostMapping("/start")
    @NeedLoginTokenAuthentication
    public StartConversationResponse startConversation(User user, @RequestBody StartConversationRequest startConversationRequest) throws MessageServiceException {
        this.conversationService.startConversation(user.getId(),
                startConversationRequest.getTitle(),
                startConversationRequest.getNotice(),
                startConversationRequest.getToUserIds());

        return new StartConversationResponse(Status.OK);
    }

    @GetMapping("/get")
    @NeedLoginTokenAuthentication
    public GetAllConversationsResponse GetAllConversations(User user) throws MessageServiceException{
        return new GetAllConversationsResponse(Status.OK, this.conversationService.getAllConversations(user.getId()));
    }

    @PostMapping("/update")
    @NeedLoginTokenAuthentication
    public UpdateConversationResponse updateConversationResponse(User user, @RequestBody UpdateConversationRequest updateConversationRequest) throws MessageServiceException {
        this.conversationService.updateConversation(updateConversationRequest.getConversationId(), updateConversationRequest.getTitle(), updateConversationRequest.getNotice());

        return new UpdateConversationResponse(Status.OK);
    }

    @PostMapping("/invite")
    @NeedLoginTokenAuthentication
    public InviteUserToConversationResponse inviteUserToConversationResponse(User user, @RequestBody InviteUserToConversationRequest inviteUserToConversationRequest) throws MessageServiceException{
        this.conversationService.inviteUserToConversation(inviteUserToConversationRequest.getConversationId(),
                inviteUserToConversationRequest.getUserIds());

        return new InviteUserToConversationResponse(Status.OK);
    }

    @PostMapping("/remove")
    @NeedLoginTokenAuthentication
    public RemoveUserFromConversationResponse removeUserFromConversationResponse(User user, @RequestBody RemoveUserFromConversationRequest removeUserFromConversationRequest) throws MessageServiceException{
        this.conversationService.removeUserFromConversation(removeUserFromConversationRequest.getConversationId(),
                removeUserFromConversationRequest.getUserIds());

        return new RemoveUserFromConversationResponse(Status.OK);
    }

    @PostMapping("delete")
    @NeedLoginTokenAuthentication
    public DeleteConversationResponse deleteConversationResponse(User user, @RequestParam int conversationId) throws MessageServiceException{
        this.conversationService.removeConversation(conversationId);

        return new DeleteConversationResponse(Status.OK);
    }
}
