package com.jjjzy.messaging.controller;

//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.Message;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Request.GetMessageRequest;
import com.jjjzy.messaging.Request.SendMessageRequest;
import com.jjjzy.messaging.Response.GetFileResponse;
import com.jjjzy.messaging.Response.GetMessageResponse;
import com.jjjzy.messaging.Response.SendMessageResponse;
import com.jjjzy.messaging.Response.UploadFileResponse;
import com.jjjzy.messaging.annotation.NeedLoginTokenAuthentication;
import com.jjjzy.messaging.service.ConversationService;
import com.jjjzy.messaging.service.FriendService;
import com.jjjzy.messaging.service.MessageService;
import com.jjjzy.messaging.service.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

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

//    @Autowired
//    private AmazonS3 amazonS3;

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
    public UploadFileResponse uploadFile(User user, @RequestParam("messageId") Integer messageId, @RequestParam("file") MultipartFile file) throws MessageServiceException, IOException {
        this.messageService.sendFile(messageId, file);
        return new UploadFileResponse(Status.OK);
    }

    @GetMapping("/get")
    @NeedLoginTokenAuthentication
    public GetMessageResponse getMessage(User user, @RequestParam(required = true, defaultValue = "0") Integer toUserId, @RequestParam(required = true, defaultValue = "0") Integer toConversationId, @RequestParam String startDate, @RequestParam String endDate) throws MessageServiceException{
        return new GetMessageResponse(this.messageService.getMessage(user.getId(), toUserId, toConversationId, startDate, endDate));
    }

    @GetMapping("/getFile")
    @NeedLoginTokenAuthentication
    public GetFileResponse getFile(User user, @RequestParam Integer messageId) throws IOException {
        this.messageService.getFile(messageId);
        return new GetFileResponse(Status.OK, this.messageService.getFile(messageId));
    }

}
