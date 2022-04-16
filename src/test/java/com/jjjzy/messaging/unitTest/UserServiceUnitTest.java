package com.jjjzy.messaging.unitTest;


import com.jjjzy.messaging.Enums.Gender;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Models.UserValidationCode;
import com.jjjzy.messaging.dao.UserDAO;
import com.jjjzy.messaging.dao.UserValidationCodeDAO;
import com.jjjzy.messaging.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static com.jjjzy.messaging.Utils.PasswordUtils.md5;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @InjectMocks
    UserService userService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserValidationCodeDAO userValidationCodeDAO;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void testRegister_happyCase() throws Exception {

        when(userDAO.findUserByUsername("steph")).thenReturn(null);

        this.userService.registerUser("steph",
                "123",
                "nouseage999@gmail.com",
                "nick",
                Gender.MALE,
                "address");
    }


    @Test
    public void testRegister_usernameExists_throwsMessageServiceException() throws Exception {
        when(userDAO.findUserByUsername("steph")).thenReturn(new User());

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.userService.registerUser("steph",
                        "123",
                        "fjj@gmail.com",
                        "nick",
                        Gender.MALE,
                        "address"));
        assertEquals("Username exists.", messageServiceException.getMessage());
    }

    @Test
    public void testActivate_happyCase() throws Exception {
        User tempUser = new User();
        tempUser.setId(9);
        when(userDAO.findUserByUsername("steph")).thenReturn(tempUser);


        UserValidationCode tempUserVali = new UserValidationCode();
        tempUserVali.setValidationCode("123456");
        when(userValidationCodeDAO.findUserValidationCodeByUserId(9)).thenReturn(tempUserVali);

        this.userService.activateUser("steph", "123456");
    }

    @Test
    public void testActivate_userDoesNotExist_throwsMessageServiceException() throws Exception {
        when(userDAO.findUserByUsername("steph")).thenReturn(null);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.userService.activateUser("steph", "123"));
        assertEquals("User not exists.", messageServiceException.getMessage());
    }

    @Test
    public void testActivate_wrongValidationCode_throwsMessageServiceException() throws Exception {
        User tempUser = new User();
        tempUser.setUsername("Steph");
        tempUser.setPassword(md5("1234"));
        tempUser.setId(123);
        when(userDAO.findUserByUsername("steph")).thenReturn(tempUser);

        UserValidationCode tempCode = new UserValidationCode();
        tempCode.setUserId(123);
        tempCode.setValidationCode("123456");

        when(userValidationCodeDAO.findUserValidationCodeByUserId(123)).thenReturn(tempCode);

//
//        when(userDAO.findUserByUsername("Stephkk")).thenReturn(tempUser);
//
//
//        when(userDAO.findUserByUsername("steph")).thenReturn(null);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.userService.activateUser("steph", "123"));
        assertEquals("Wrong validation code", messageServiceException.getMessage());
    }

    @Test
    public void testLogin_happyCase() throws Exception{
        User tempUser = new User();
        tempUser.setUsername("Stephkk");
        tempUser.setPassword(md5("1234"));
        when(userDAO.findUserByUsername("Stephkk")).thenReturn(tempUser);

        this.userService.userLogin(tempUser);
    }

    @Test
    public void testLogin_userDoesNotExist_throwsMessageServiceException() throws Exception {
        when(userDAO.findUserByUsername("steph")).thenReturn(null);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.userService.userLogin(new User()));
        assertEquals("User not exists.", messageServiceException.getMessage());
    }

    @Test
    public void testLogin_wrongPassword_throwsMessageServiceException() throws Exception {
        User tempUser = new User();
        tempUser.setUsername("Steph");
        tempUser.setPassword(md5("1234"));
        when(userDAO.findUserByUsername("steph")).thenReturn(tempUser);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.userService.userLogin(tempUser));
        assertEquals("Password is wrong, try again", messageServiceException.getMessage());
    }

    @Test
    public void testLogout_happyCase() throws Exception {
        this.userService.userLogout("7");
    }

    @Test
    public void testVerifyLoginToken(){
        this.userService.verifyLoginToken("123");
    }
}
