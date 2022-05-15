package com.jjjzy.messaging.config;

import com.jjjzy.messaging.Models.Message;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.service.MessageService;
import com.jjjzy.messaging.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {
    private static final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Server connection opened");
        sessions.add(session);

//        TextMessage message = new TextMessage("one-time message from server");
//        logger.info("Server sends: {}", message);
//        session.sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Server connection closed: {}", status);
        sessions.remove(session);
    }

    @Scheduled(fixedRate = 1000)
    void sendPeriodicMessages() throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
//                logger.info("Server sends: {}", broadcast);
//                session.sendMessage(new TextMessage(broadcast));
                String loginToken = session.getAttributes().get("loginToken").toString();

                User user = this.userService.verifyLoginToken(loginToken);

                List<Message> recentMessage = this.messageService.getAllUnreadMessage(user.getId(), "UNREAD");
//                System.out.println(recentMessage);
                for(Message message : recentMessage){
                    session.sendMessage(new TextMessage(message.getContent()));
                }

            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        super.handleTextMessage(session, message);

//        String request = message.getPayload();
//        logger.info("Server received: {}", request);
//
//        String response = String.format("response from server to '%s'", HtmlUtils.htmlEscape(request));
//        logger.info("Server sends: {}", response);
//        session.sendMessage(new TextMessage("hello"));

//        List<Message> recentMessage = this.messageService.getAllMessage(2);
//        System.out.println(recentMessage);
//        System.out.println(recentMessage.get(1).getContent());
//
//        for(WebSocketSession curSession : sessions){
//            curSession.sendMessage(new TextMessage("hello"));
//        }



//        sessions.forEach(webSocketSession -> {
//            try {
//
//                webSocketSession.sendMessage(message);
//
//            } catch (IOException e) {
//
//                logger.error("Error occurred.", e);
//
//            }
//
//        });
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.info("Server transport error: {}", exception.getMessage());
    }

    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");
    }
}
