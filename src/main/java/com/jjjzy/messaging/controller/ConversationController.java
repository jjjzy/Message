package com.jjjzy.messaging.controller;



import com.jjjzy.messaging.enums.Status;
import com.jjjzy.messaging.exceptions.MessageServiceException;
import com.jjjzy.messaging.models.User;
import com.jjjzy.messaging.requests.InviteUserToConversationRequest;
import com.jjjzy.messaging.requests.RemoveUserFromConversationRequest;
import com.jjjzy.messaging.requests.StartConversationRequest;
import com.jjjzy.messaging.requests.UpdateConversationRequest;
import com.jjjzy.messaging.responses.*;
import com.jjjzy.messaging.annotations.NeedLoginTokenAuthentication;
import com.jjjzy.messaging.service.ConversationService;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
