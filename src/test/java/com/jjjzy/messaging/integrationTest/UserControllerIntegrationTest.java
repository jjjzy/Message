package com.jjjzy.messaging.integrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjjzy.messaging.models.User;
import com.jjjzy.messaging.dao.TestUserDAO;
import com.jjjzy.messaging.dao.TestUserValidationCodeDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.jjjzy.messaging.utils.utils.md5;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUserDAO testUserDAO;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUserValidationCodeDAO testUserValidationCodeDAO;

    @BeforeEach
    public void cleanUp1() throws Exception {
        testUserDAO.deleteAllUsers();
        testUserDAO.deleteAllUserValidationCode();
    }

    @Test
    @Order(1)
    public void testRegister_happyCase() throws Exception {
        String requestBody = "{\"username\": \"george2\", \"password\": \"123\", \"repeatPassword\": \"123\", \"email\": \"nouseage999@gmail.com\"}";

        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        User user = this.testUserDAO.findUserByUsername("george2");
        assertEquals(user.getPassword(), md5("123"));
        assertEquals(user.getEmail(), "nouseage999@gmail.com");
        assertEquals(user.getAddress(), null);
        assertEquals(user.getId(), 1);
    }

    @Test
    @Order(2)
    public void testActivate_happyCase() throws Exception{
        String requestBody = "{\"username\": \"george\", \"password\": \"123\", \"repeatPassword\": \"123\", \"email\": \"nouseage999@gmail.com\"}";
        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        String validationCode = this.testUserValidationCodeDAO.findUserValidationCodeByUserId(this.testUserDAO.findUserByUsername("george").getId()).getValidationCode();
        this.mockMvc.perform(get("/users/activate")
                        .contentType("application/json")
                        .param("username", "george")
                        .param("validationCode", validationCode))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        User user = this.testUserDAO.findUserByUsername("george");

        assertEquals(user.isValid(), true);
    }

    @Test
    @Order(3)
    public void testLogin_happyCase() throws Exception{
        String requestBody = "{\"username\": \"george\", \"password\": \"123\", \"repeatPassword\": \"123\", \"email\": \"nouseage999@gmail.com\"}";
        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        String validationCode = this.testUserValidationCodeDAO.findUserValidationCodeByUserId(this.testUserDAO.findUserByUsername("george").getId()).getValidationCode();
        this.mockMvc.perform(get("/users/activate")
                        .contentType("application/json")
                        .param("username", "george")
                        .param("validationCode", validationCode))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        this.mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .param("username", "george")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        User user = this.testUserDAO.findUserByUsername("george");
        assert(user.getLoginToken() != null);
        assert(user.getLastLoginTime() != null);
    }

    @Test
    @Order(4)
    public void testRegister_passwordDoNotMatch_throwsMessageServiceException() throws Exception {
        String requestBody = "{\"username\": \"george2\", \"password\": \"123\", \"repeatPassword\": \"1233\", \"email\": \"nouseage999@gmail.com\"}";

        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(2000)))
                .andExpect(jsonPath("$.message", is("Passwords don't match")));
    }

    @Test
    @Order(5)
    public void testSendEmail_happyCase() throws Exception {
        String requestBody = "{\"username\": \"george\", \"password\": \"123\", \"repeatPassword\": \"123\", \"email\": \"nouseage999@gmail.com\"}";
        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        this.mockMvc.perform(get("/users/sendEmail")
                        .contentType("application/json")
                .param("username", "george")
                .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));
    }

    @Test
    @Order(6)
    public void testLogout_happyCase() throws Exception {
        String requestBody = "{\"username\": \"george\", \"password\": \"123\", \"repeatPassword\": \"123\", \"email\": \"nouseage999@gmail.com\"}";
        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        String validationCode = this.testUserValidationCodeDAO.findUserValidationCodeByUserId(this.testUserDAO.findUserByUsername("george").getId()).getValidationCode();
        this.mockMvc.perform(get("/users/activate")
                        .contentType("application/json")
                        .param("username", "george")
                        .param("validationCode", validationCode))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        this.mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .param("username", "george")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        String loginToken = this.testUserDAO.findUserByUsername("george").getLoginToken();
        this.mockMvc.perform(post("/users/logout")
                        .contentType("application/json")
                        .header("loginToken", loginToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));

        User user = this.testUserDAO.findUserByUsername("george");
        assert(user.getLoginToken() == null);
    }
}
