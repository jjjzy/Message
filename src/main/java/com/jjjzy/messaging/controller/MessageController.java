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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    private Logger log = LoggerFactory.getLogger(MessageController.class);

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
    public GetMessageResponse getMessage(User user, @RequestParam(defaultValue = "0") Integer toUserId, @RequestParam(defaultValue = "0") Integer toConversationId, @RequestParam String startDate, @RequestParam String endDate) throws MessageServiceException{
        return new GetMessageResponse(this.messageService.getMessage(user.getId(), toUserId, toConversationId, startDate, endDate), Status.OK);
    }

    @GetMapping(value = "/getFile", produces = "image/jpeg")
    //TODO
    @NeedLoginTokenAuthentication
    public @ResponseBody byte[] getFile(User user, @RequestParam Integer messageId) throws IOException {
        return this.messageService.getFile(messageId);
    }

//    @GetMapping("/download/{id}")
//    public ResponseEntity<byte[]> download(@PathVariable String id) throws Exception {
//        var doc = new Document("test.pdf", "application/pdf", "Base64 encoded data");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", doc.getType());
//        headers.add("Content-Disposition", "attachment; filename=\"%s\"".formatted(doc.getFilename()));
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .headers(headers)
//                .body(Base64.getDecoder().decode(doc.getData()));
//    }

    @GetMapping("/getLatest")
    @NeedLoginTokenAuthentication
    public DeferredResult<GetMessageResponse> getLatestMessage(User user, @RequestParam(defaultValue = "0") String lastSyncTime) throws MessageServiceException{

        long timeOutInMilliSec = 10000L;
        DeferredResult<GetMessageResponse> deferredResult =
                new DeferredResult<>(timeOutInMilliSec, () -> new GetMessageResponse(List.of(), Status.GET_MESSAGE_TIMEOUT));
        CompletableFuture.runAsync(() -> {
            try {
                while (true) {
                    var messages = this.messageService.getLatestMessage(user.getId(), lastSyncTime);

                    if (!messages.isEmpty()) {
                        deferredResult.setResult(new GetMessageResponse(messages, Status.OK));
                        break;
                    } else {
                        Thread.sleep(1000);
                    }
                }

            } catch (Exception exception) {
                log.warn("Failed to get latest messages: {}", exception.getMessage(), exception);
                deferredResult.setErrorResult(new MessageServiceException(Status.MESSAGE_ERROR));
            }
        });
        return deferredResult;
    }
}
