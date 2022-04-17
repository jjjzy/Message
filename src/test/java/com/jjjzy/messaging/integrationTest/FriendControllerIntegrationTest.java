package com.jjjzy.messaging.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjjzy.messaging.Enums.FriendInvitationStatus;
import com.jjjzy.messaging.Models.FriendInvitation;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.dao.TestFriendDAO;
import com.jjjzy.messaging.dao.TestUserDAO;
import com.jjjzy.messaging.dao.TestUserValidationCodeDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.jjjzy.messaging.Utils.PasswordUtils.md5;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FriendControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUserDAO testUserDAO;

    @Autowired
    private TestFriendDAO testFriendDAO;

    @Autowired
    private TestUserValidationCodeDAO testUserValidationCodeDAO;

    @Autowired
    private ObjectMapper objectMapper;

//    @BeforeEach
//    @Order(0)
//    public void cleanUp1() throws Exception {
//        testUserDAO.deleteAllUsers();
//        testUserDAO.deleteAllUserValidationCode();
//    }

    @BeforeEach
    public void setUp() throws Exception {
        testUserDAO.deleteAllUsers();
        testUserDAO.deleteAllUserValidationCode();
        testFriendDAO.deleteAllInvitations();

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
    }

    @Test
    public void testInviteFriend_happyCase() throws Exception {
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");


        String loginToken = user1.getLoginToken();
        this.mockMvc.perform(post("/friends/send")
                        .contentType("application/json")
                        .header("loginToken", loginToken)
                        .param("toUserId", Integer.toString(user2.getId()))
                        .param("message", "hey, this is a test"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));


        FriendInvitation friendInvitation = this.testFriendDAO.getInvitationByIdAndStatus(user1.getId(), FriendInvitationStatus.PENDING).get(0);
        assertEquals(friendInvitation.getFromUserId(), user1.getId());
        assertEquals(friendInvitation.getToUserId(), user2.getId());
        assertEquals(friendInvitation.getMessage(), "hey, this is a test");
        assertEquals(friendInvitation.getId(), 1);
    }

    @Test
    public void testGetPendingFriends_happyCase() throws Exception {
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");

        String loginToken = user1.getLoginToken();
        this.mockMvc.perform(post("/friends/send")
                        .contentType("application/json")
                        .header("loginToken", loginToken)
                        .param("toUserId", Integer.toString(user2.getId()))
                        .param("message", "hey, this is a test"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        MvcResult result = this.mockMvc.perform(get("/friends/getPending")
                        .contentType("application/json")
                        .header("loginToken", loginToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")))
                .andExpect(jsonPath("$.invitations[0].fromUserId", is(1)))
                .andExpect(jsonPath("$.invitations[0].toUserId", is(2)))
                .andExpect(jsonPath("$.invitations[0].message", is("hey, this is a test")))
                .andExpect(jsonPath("$.invitations[0].status", is("PENDING")))
                .andReturn();


        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        System.out.println(json.getString("invitations"));
//        System.out.println(json.getString(""));
//        assertEquals(array.getJSONObject(2).getString("fromUserId"), 1);
//        assertEquals(array.getJSONObject(2).getString("toUserId"), 2);
//        assertEquals(array.getJSONObject(2).getString("status"), "PENDING");
//        assertEquals(array.getJSONObject(2).getString("message"), "hey, this is a test");
    }

    @Test
    public void testAcceptingFriendInvitations_happyCase() throws Exception {
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");

        String loginToken = user1.getLoginToken();
        this.mockMvc.perform(post("/friends/send")
                        .contentType("application/json")
                        .header("loginToken", loginToken)
                        .param("toUserId", Integer.toString(user2.getId()))
                        .param("message", "hey, this is a test"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        MvcResult result = this.mockMvc.perform(get("/friends/getPending")
                        .contentType("application/json")
                        .header("loginToken", loginToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")))
                .andExpect(jsonPath("$.invitations[0].fromUserId", is(1)))
                .andExpect(jsonPath("$.invitations[0].toUserId", is(2)))
                .andExpect(jsonPath("$.invitations[0].message", is("hey, this is a test")))
                .andExpect(jsonPath("$.invitations[0].status", is("PENDING")))
                .andReturn();

        loginToken = user2.getLoginToken();
        this.mockMvc.perform(post("/friends/accept")
                        .contentType("application/json")
                        .param("invitationId", Integer.toString(1))
                        .header("loginToken", loginToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));
    }


    @Test
    public void testGetFriends_happyCase() throws Exception {
        User user1 = this.testUserDAO.findUserByUsername("george1");
        User user2 = this.testUserDAO.findUserByUsername("george2");

        String loginToken = user1.getLoginToken();
        this.mockMvc.perform(post("/friends/send")
                        .contentType("application/json")
                        .header("loginToken", loginToken)
                        .param("toUserId", Integer.toString(user2.getId()))
                        .param("message", "hey, this is a test"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        loginToken = user2.getLoginToken();
        this.mockMvc.perform(post("/friends/accept")
                        .contentType("application/json")
                        .param("invitationId", Integer.toString(1))
                        .header("loginToken", loginToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        loginToken = user1.getLoginToken();
        this.mockMvc.perform(get("/friends/getFriends")
                        .contentType("application/json")
                        .header("loginToken", loginToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")))
                .andExpect(jsonPath("$.friends[0].username", is("george2")))
                .andExpect(jsonPath("$.friends[0].password", is(md5("123"))))
                .andExpect(jsonPath("$.friends[0].email", is("nouseage999@gmail.com")));
    }
}
