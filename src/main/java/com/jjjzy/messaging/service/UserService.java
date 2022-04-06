package com.jjjzy.messaging.service;

import com.jjjzy.messaging.dao.UserDAO;
import com.jjjzy.messaging.dao.UserValidationCodeDAO;
import com.jjjzy.messaging.Enums.Gender;
import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Models.UserValidationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

import static com.jjjzy.messaging.Utils.LoginTokenUtils.generateToken;
import static com.jjjzy.messaging.Utils.PasswordUtils.md5;


@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserValidationCodeDAO userValidationCodeDAO;

    @Autowired
    private JavaMailSender javaMailSender;

    //TODO
    //IF not activated what to do

    public void registerUser(String username, String password, String email, String nickname,
                             Gender gender, String address) throws MessageServiceException {
        if (this.userDAO.findUserByUsername(username) != null) {
            throw new MessageServiceException(Status.USERNAME_EXISTS);
        }
        User user = new User();
        user.setUsername(username);

        String encryptedPassword = md5(password);
        System.out.println(encryptedPassword);
        user.setPassword(encryptedPassword);

        user.setEmail(email);
        user.setNickname(nickname);
        user.setGender(gender);
        user.setAddress(address);
        user.setRegistrationTime(new Date());
        int idd = this.userDAO.insertUser(user);
        System.out.println(idd);

        String validationCode = generateValidationCode();

        UserValidationCode userValidationCode = new UserValidationCode();
        userValidationCode.setUserId(user.getId());
        userValidationCode.setValidationCode(validationCode);

        this.userValidationCodeDAO.insertUserValidationCode(userValidationCode);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        //TODO
        //EMAIL VALID TIME?
        //validation code valid time
        msg.setSubject("Thanks for registering for messaging");
        msg.setText("Hello \n here is your verification code \n " + validationCode + "\n you can also use this link \n" +
                "http://localhost:8080/users/activate?username=" + username + "&validationCode=" + validationCode);
        System.out.println(msg);
        javaMailSender.send(msg);
    }

    private static String generateValidationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public void activateUser(String username, String validationCode) throws MessageServiceException {
        User user = this.userDAO.findUserByUsername(username);
        if (user == null) {
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }
        UserValidationCode userValidationCode = this.userValidationCodeDAO.findUserValidationCodeByUserId(user.getId());
        if (!validationCode.equals(userValidationCode.getValidationCode())) {
            throw new MessageServiceException(Status.WRONG_VALIDATION_CODE);
        }

        System.out.println(user.getId());
        this.userValidationCodeDAO.deleteUserValidationCode(user.getId());

        this.userDAO.setUserValid(user.getId());
    }

    public void userLogin(String username, String password) throws MessageServiceException{
        User user = this.userDAO.findUserByUsername(username);
        if(user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        if(!user.getPassword().equals(md5(password))){
            throw new MessageServiceException(Status.WRONG_PASSWORD);
        }

        Date loginTime = new Date();
        this.userDAO.setUserLastLoginTime(loginTime, user.getUsername());

        String loginToken = generateToken();

        System.out.println(loginToken);
        System.out.println(user.getUsername());

        this.userDAO.setUserLoginToken(loginToken, user.getId());
    }

    public User verifyLoginToken(String loginToken){
        User user = this.userDAO.findUserByLoginToken(loginToken);
        return user;
    }

    public void userLogout(int userId) throws MessageServiceException{
        this.userDAO.setUserLoginToken("NULL", userId);
    }


}
