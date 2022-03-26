package com.jjjzy.messaging.integrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjjzy.messaging.dao.TestUserDAO;
import com.jjjzy.messaging.dao.TestUserValidationCodeDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
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

    @AfterAll
    public void cleanUp(){
        testUserDAO.deleteAllUsers();
        testUserDAO.deleteAllUserValidationCode();
    }

    @Test
    public void testRegister_happyCase() throws Exception {

        String requestBody = "{\"username\": \"george2\", \"password\": \"123\", \"repeatPassword\": \"123\"}";

        this.mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")))
                ;
    }

    @Test
    public void testActivate_happyCase() throws Exception{
        String vaidationCode = this.testUserValidationCodeDAO.findUserValidationCodeByUserId(this.testUserDAO.findUserByUsername("george2").getId()).getValidationCode();
        String requestBody = String.format("{\"username\": \"george2\", \"validationCode\": \"%s\"}", vaidationCode);

        this.mockMvc.perform(post("/users/activate")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));
    }

    @Test
    public void testLogin_happyCase() throws Exception{
        String requestBody = "{\"username\": \"george2\", \"password\": \"123\"}";

        this.mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code", is(1000)))
                .andExpect(jsonPath("$.message", is("Successful")));
    }


}
