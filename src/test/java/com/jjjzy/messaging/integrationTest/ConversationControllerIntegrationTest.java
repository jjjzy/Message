package com.jjjzy.messaging.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjjzy.messaging.models.Conversation;
import com.jjjzy.messaging.models.User;
import com.jjjzy.messaging.dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConversationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUserDAO testUserDAO;

    @Autowired
    private TestFriendDAO testFriendDAO;

    @Autowired
    private TestUserValidationCodeDAO testUserValidationCodeDAO;

    @Autowired
    private TestConversationUsersDAO testConversationUsersDAO;

    @Autowired
    private TestConversationDAO testConversationDAO;

    @BeforeEach
    public void setUp() throws Exception {
        this.testUserDAO.deleteAllUsers();
        this.testUserDAO.deleteAllUserValidationCode();
        this.testFriendDAO.deleteAllInvitations();
        this.testConversationUsersDAO.deleteAllConversationUsers();
        this.testConversationDAO.deleteAllConversations();

        String requestBody = "{\"username\": \"george1\", \"password\": \"123\", \"repeatPassword\": \"123\", \"email\": \"nouseage999@gmail.com\"}";
        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        String validationCode = this.testUserValidationCodeDAO.findUserValidationCodeByUserId(this.testUserDAO.findUserByUsername("george1").getId()).getValidationCode();
        this.mockMvc.perform(get("/users/activate")
                        .contentType("application/json")
                        .param("username", "george1")
                        .param("validationCode", validationCode))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        this.mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .param("username", "george1")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));



        requestBody = "{\"username\": \"george2\", \"password\": \"123\", \"repeatPassword\": \"123\", \"email\": \"nouseage999@gmail.com\"}";
        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        validationCode = this.testUserValidationCodeDAO.findUserValidationCodeByUserId(this.testUserDAO.findUserByUsername("george2").getId()).getValidationCode();
        this.mockMvc.perform(get("/users/activate")
                        .contentType("application/json")
                        .param("username", "george2")
                        .param("validationCode", validationCode))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        this.mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .param("username", "george2")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));


        requestBody = "{\"username\": \"george3\", \"password\": \"123\", \"repeatPassword\": \"123\", \"email\": \"nouseage999@gmail.com\"}";
        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        validationCode = this.testUserValidationCodeDAO.findUserValidationCodeByUserId(this.testUserDAO.findUserByUsername("george3").getId()).getValidationCode();
        this.mockMvc.perform(get("/users/activate")
                        .contentType("application/json")
                        .param("username", "george3")
                        .param("validationCode", validationCode))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        this.mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .param("username", "george3")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));
    }


    @Test
    public void testStartConversation_happyCase() throws Exception {
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");
        User user3 = this.testUserDAO.findUserByUsername("george3");

        String requestBody = "{\"title\": \"our test conversation\", \"notice\": \"temp notice\", \"toUserIds\": [2,3]}";
        this.mockMvc.perform(post("/conversations/start")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user1.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        Conversation conversation = this.testConversationDAO.getConversationById(1);
        assertEquals(conversation.getNotice(), "temp notice");
        assertEquals(conversation.getTitle(), "our test conversation");

        List<Integer> conversationUsers = this.testConversationUsersDAO.getConversationUsers(1);
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(1);
        temp.add(2);
        temp.add(3);
        assertTrue(conversationUsers.size() == temp.size() && conversationUsers.containsAll(temp) && temp.containsAll(conversationUsers));
    }

    @Test
    public void testGetConversations_happyCase() throws Exception {
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");
        User user3 = this.testUserDAO.findUserByUsername("george3");

        String requestBody = "{\"title\": \"our test conversation\", \"notice\": \"temp notice\", \"toUserIds\": [2,3]}";
        this.mockMvc.perform(post("/conversations/start")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user1.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        requestBody = "{\"title\": \"our test conversation1\", \"notice\": \"temp notice1\", \"toUserIds\": [2]}";
        this.mockMvc.perform(post("/conversations/start")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user1.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        this.mockMvc.perform(get("/conversations/get")
                        .contentType("application/json")
                        .header("loginToken", user1.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")))
                .andExpect(jsonPath("$.conversations[0].title", is("our test conversation")))
                .andExpect(jsonPath("$.conversations[0].notice", is("temp notice")))
                .andExpect(jsonPath("$.conversations[1].title", is("our test conversation1")))
                .andExpect(jsonPath("$.conversations[1].notice", is("temp notice1")));


        this.mockMvc.perform(get("/conversations/get")
                        .contentType("application/json")
                        .header("loginToken", user3.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")))
                .andExpect(jsonPath("$.conversations[0].title", is("our test conversation")))
                .andExpect(jsonPath("$.conversations[0].notice", is("temp notice")));
    }

    @Test
    public void testUpdateConversation_happyCase() throws Exception{
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");
        User user3 = this.testUserDAO.findUserByUsername("george3");

        String requestBody = "{\"title\": \"our test conversation\", \"notice\": \"temp notice\", \"toUserIds\": [2,3]}";
        this.mockMvc.perform(post("/conversations/start")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user1.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        requestBody = "{\"title\": \"modified title\", \"notice\": \"modified notice\", \"conversationId\": 1}";
        this.mockMvc.perform(post("/conversations/update")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user2.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        Conversation conversation = this.testConversationDAO.getConversationById(1);
        assertEquals(conversation.getTitle(), "modified title");
        assertEquals(conversation.getNotice(), "modified notice");
    }

    @Test
    public void testInviteUserToConversation_happyCase() throws Exception{
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");
        User user3 = this.testUserDAO.findUserByUsername("george3");

        String requestBody = "{\"title\": \"our test conversation\", \"notice\": \"temp notice\", \"toUserIds\": [2]}";
        this.mockMvc.perform(post("/conversations/start")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user1.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        requestBody = "{\"conversationId\": \"1\", \"userIds\": [3]}";
        this.mockMvc.perform(post("/conversations/invite")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user2.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        List<Integer> conversationUsers = this.testConversationUsersDAO.getConversationUsers(1);
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(1);
        temp.add(2);
        temp.add(3);
        assertTrue(conversationUsers.size() == temp.size() && conversationUsers.containsAll(temp) && temp.containsAll(conversationUsers));
    }


    @Test
    public void testRemoveUserToConversation_happyCase() throws Exception{
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");
        User user3 = this.testUserDAO.findUserByUsername("george3");

        String requestBody = "{\"title\": \"our test conversation\", \"notice\": \"temp notice\", \"toUserIds\": [2,3]}";
        this.mockMvc.perform(post("/conversations/start")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user1.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        requestBody = "{\"conversationId\": \"1\", \"userIds\": [3]}";
        this.mockMvc.perform(post("/conversations/remove")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user2.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        List<Integer> conversationUsers = this.testConversationUsersDAO.getConversationUsers(1);
        List<Integer> temp = new ArrayList<Integer>();
        temp.add(1);
        temp.add(2);
        assertTrue(conversationUsers.size() == temp.size() && conversationUsers.containsAll(temp) && temp.containsAll(conversationUsers));
    }

    @Test
    public void testDeleteConversation_happyCase() throws Exception{
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");
        User user3 = this.testUserDAO.findUserByUsername("george3");

        String requestBody = "{\"title\": \"our test conversation\", \"notice\": \"temp notice\", \"toUserIds\": [2,3]}";
        this.mockMvc.perform(post("/conversations/start")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("loginToken", user1.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        this.mockMvc.perform(post("/conversations/delete")
                        .contentType("application/json")
                        .param("conversationId", Integer.toString(1))
                        .header("loginToken", user3.getLoginToken()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        assertEquals(this.testConversationDAO.getConversationById(1), null);
        assertEquals(this.testConversationUsersDAO.getConversationUsers(1), new ArrayList<Integer>());
    }
}
