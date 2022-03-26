package com.jjjzy.messaging.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.Message;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Request.GetMessageRequest;
import com.jjjzy.messaging.Request.SendMessageRequest;
import com.jjjzy.messaging.Response.GetMessageResponse;
import com.jjjzy.messaging.Response.SendMessageResponse;
import com.jjjzy.messaging.annotation.NeedLoginTokenAuthentication;
import com.jjjzy.messaging.service.ConversationService;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.MessageService;
import com.jjjzy.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    @Autowired
    private AmazonS3 amazonS3;

    @PostMapping("/send")
    @NeedLoginTokenAuthentication
    public SendMessageResponse sendMessage(User user, @RequestBody SendMessageRequest sendMessageRequest) throws MessageServiceException{
        this.messageService.sendMessage(user.getId(),
                sendMessageRequest.getToUserId(),
                sendMessageRequest.getToConversationId(),
                sendMessageRequest.getMessageType(),
                sendMessageRequest.getContent());

        return new SendMessageResponse(Status.OK);
    }

    @PostMapping("/upload")
    @NeedLoginTokenAuthentication
    public void uploadFile(User user, @RequestParam("messageId") Integer messageId, @RequestParam("file") MultipartFile file) throws MessageServiceException, IOException {
//        System.out.println(file);

//        file.getInputStream();
//
//        File f = new File("src/main/resources/targetFile.tmp");
//
//        try (OutputStream os = new FileOutputStream(f)) {
//            os.write(file.getBytes());
//        }

        this.amazonS3.putObject("messaging-jjjzy", "hey1", file.getInputStream(), new ObjectMetadata());
    }

    @GetMapping("/get")
    @NeedLoginTokenAuthentication
    public GetMessageResponse getMessage(User user, @RequestBody GetMessageRequest getMessageRequest) throws MessageServiceException{
        return new GetMessageResponse(this.messageService.getMessage(user.getId(), getMessageRequest.getToUserId(), getMessageRequest.getToConversationId()));
    }
}
