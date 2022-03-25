package com.jjjzy.messaging.controller;


import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.Message;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Request.GetMessageRequest;
import com.jjjzy.messaging.Request.SendMessageRequest;
import com.jjjzy.messaging.Response.GetMessageResponse;
import com.jjjzy.messaging.Response.SendMessageResponse;
import com.jjjzy.messaging.service.ConversationService;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.MessageService;
import com.jjjzy.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public SendMessageResponse sendMessage(@RequestBody SendMessageRequest sendMessageRequest) throws MessageServiceException{
        User user = this.userService.verifyLoginToken(sendMessageRequest.getLoginToken());
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        this.messageService.sendMessage(user.getId(),
                sendMessageRequest.getToUserId(),
                sendMessageRequest.getToConversationId(),
                sendMessageRequest.getMessageType(),
                sendMessageRequest.getContent());

        return new SendMessageResponse(Status.OK);
    }

    @GetMapping("/get")
    public GetMessageResponse getMessage(@RequestBody GetMessageRequest getMessageRequest) throws MessageServiceException{
        User user = this.userService.verifyLoginToken(getMessageRequest.getLoginToken());
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }
        System.out.println(this.messageService.getMessage(user.getId(), getMessageRequest.getToUserId(), getMessageRequest.getToConversationId()));
        return new GetMessageResponse(this.messageService.getMessage(user.getId(), getMessageRequest.getToUserId(), getMessageRequest.getToConversationId()));

    }
}
