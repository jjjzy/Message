package com.jjjzy.messaging.controller;



import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Request.InviteUserToConversationRequest;
import com.jjjzy.messaging.Request.RemoveUserFromConversationRequest;
import com.jjjzy.messaging.Request.StartConversationRequest;
import com.jjjzy.messaging.Request.UpdateConversationRequest;
import com.jjjzy.messaging.Response.*;
import com.jjjzy.messaging.annotation.NeedAuthentication;
import com.jjjzy.messaging.service.ConversationService;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/conversations")
public class ConversationController {
    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ConversationService conversationService;

    @PostMapping("/start")
    @NeedAuthentication
    public StartConversationResponse startConversation(@RequestBody StartConversationRequest startConversationRequest) throws MessageServiceException {
        User user = this.userService.verifyLoginToken(startConversationRequest.getLoginToken());
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }


        this.conversationService.startConversation(user.getId(),
                startConversationRequest.getTitle(),
                startConversationRequest.getNotice(),
                startConversationRequest.getToUserIds());

        return new StartConversationResponse(Status.OK);
    }

    @GetMapping("/get")
    public GetAllConversationsResponse GetAllConversations(@RequestParam String loginToken) throws MessageServiceException{
        User user = this.userService.verifyLoginToken(loginToken);
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        return new GetAllConversationsResponse(Status.OK, this.conversationService.getAllConversations(user.getId()));
    }

    @PostMapping("/update")
    public UpdateConversationResponse updateConversationResponse(@RequestBody UpdateConversationRequest updateConversationRequest) throws MessageServiceException {
        User user = this.userService.verifyLoginToken(updateConversationRequest.getLoginToken());
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        this.conversationService.updateConversation(updateConversationRequest.getConversationId(), updateConversationRequest.getTitle(), updateConversationRequest.getNotice());

        return new UpdateConversationResponse(Status.OK);
    }

    @PostMapping("/invite")
    public InviteUserToConversationResponse inviteUserToConversationResponse(@RequestBody InviteUserToConversationRequest inviteUserToConversationRequest) throws MessageServiceException{
        User user = this.userService.verifyLoginToken(inviteUserToConversationRequest.getLoginToken());
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        this.conversationService.inviteUserToConversation(inviteUserToConversationRequest.getConversationId(),
                inviteUserToConversationRequest.getUserIds());

        return new InviteUserToConversationResponse(Status.OK);
    }

    @PostMapping("/remove")
    public RemoveUserFromConversationResponse removeUserFromConversationResponse(@RequestBody RemoveUserFromConversationRequest removeUserFromConversationRequest) throws MessageServiceException{
        User user = this.userService.verifyLoginToken(removeUserFromConversationRequest.getLoginToken());
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        this.conversationService.removeUserFromConversation(removeUserFromConversationRequest.getConversationId(),
                removeUserFromConversationRequest.getUserIds());

        return new RemoveUserFromConversationResponse(Status.OK);
    }

    @PostMapping("delete")
    public DeleteConversationResponse deleteConversationResponse(@RequestParam String loginToken, @RequestParam int conversationId) throws MessageServiceException{
        User user = this.userService.verifyLoginToken(loginToken);
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        this.conversationService.removeConversation(conversationId);

        return new DeleteConversationResponse(Status.OK);
    }
}
