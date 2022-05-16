package com.jjjzy.messaging.unitTest;


import com.jjjzy.messaging.enums.Gender;
import com.jjjzy.messaging.exceptions.MessageServiceException;
import com.jjjzy.messaging.models.User;
import com.jjjzy.messaging.models.UserValidationCode;
import com.jjjzy.messaging.dao.UserDAO;
import com.jjjzy.messaging.dao.UserValidationCodeDAO;
import com.jjjzy.messaging.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.jjjzy.messaging.utils.utils.generateToken;
import static com.jjjzy.messaging.utils.utils.md5;
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


        UserValidationCode tempUserValidationCode = new UserValidationCode();
        tempUserValidationCode.setCreateTime(new Date());
        tempUserValidationCode.setValidationCode("123456");
        when(userValidationCodeDAO.findUserValidationCodeByUserId(9)).thenReturn(tempUserValidationCode);

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

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.userService.activateUser("steph", "123"));
        assertEquals("Wrong validation code", messageServiceException.getMessage());
    }

    @Test
    public void testActivate_waitedTooLong_throwsMessageServiceException() throws Exception{
        User tempUser = new User();
        tempUser.setUsername("steph");
        tempUser.setPassword(md5("1234"));
        tempUser.setId(123);
        when(userDAO.findUserByUsername("steph")).thenReturn(tempUser);


        UserValidationCode tempCode = new UserValidationCode();
        tempCode.setUserId(123);
        tempCode.setValidationCode("123456");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.ENGLISH);
        String dateInString = "22-01-2015 10:15:55 AM";
        Date date = formatter.parse(dateInString);
        tempCode.setCreateTime(date);

        when(userValidationCodeDAO.findUserValidationCodeByUserId(123)).thenReturn(tempCode);

        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.userService.activateUser("steph", "123456"));
        assertEquals("Validation code is expired, please get email again.", messageServiceException.getMessage());
    }

    @Test
    public void testLogin_happyCase() throws Exception{
        User tempUser = new User();
        tempUser.setUsername("Stephkk");
        tempUser.setPassword(md5("1234"));
        tempUser.setValid(true);

        this.userService.userLogin(tempUser);
    }

    @Test
    public void testLogin_notActivated_throwsMessageServiceException() throws Exception {
        MessageServiceException messageServiceException = assertThrows(
                MessageServiceException.class,
                () -> this.userService.userLogin(new User()));
        assertEquals("You are not validated, please activate first.", messageServiceException.getMessage());
    }

    @Test
    public void testLogout_happyCase() throws Exception {
        this.userService.userLogout("7");
    }

    @Test
    public void testVerifyLoginToken(){
        this.userService.verifyLoginToken("123");
    }

    @Test
    public void testUpdateValidationCode() throws Exception {
        this.userService.updateValidationCode(123, generateToken(), new Date());
    }

    @Test
    public void testGetValidationCode(){
        this.userService.getValidationCode(123);
    }
}
