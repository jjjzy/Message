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

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Random;

import static com.jjjzy.messaging.Utils.utils.generateToken;
import static com.jjjzy.messaging.Utils.utils.md5;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserValidationCodeDAO userValidationCodeDAO;

    @Autowired
    private JavaMailSender javaMailSender;

    public void registerUser(String username, String password, String email, String nickname,
                             Gender gender, String address) throws MessageServiceException {
        if (this.userDAO.findUserByUsername(username) != null) {
            throw new MessageServiceException(Status.USERNAME_EXISTS);
        }
        User user = new User();
        user.setUsername(username);

        String encryptedPassword = md5(password);
        user.setPassword(encryptedPassword);

        user.setEmail(email);
        user.setNickname(nickname);
        user.setGender(gender);
        user.setAddress(address);
        user.setRegistrationTime(new Date());
        int idd = this.userDAO.insertUser(user);

        UserValidationCode userValidationCode = createValidationCode(user.getId());

        this.userValidationCodeDAO.insertUserValidationCode(userValidationCode);

        sendValidationCodeEmail(email, username, userValidationCode.getValidationCode());
    }

    public UserValidationCode createValidationCode(int userId){
        UserValidationCode userValidationCode = new UserValidationCode();

        userValidationCode.setValidationCode(generateToken());
        userValidationCode.setUserId(userId);
        userValidationCode.setCreateTime(new Date());
        return userValidationCode;
    }

    public void updateValidationCode(int userId, String validationCode, Date createTime){
        this.userValidationCodeDAO.updateValidationCode(validationCode, createTime, userId);
    }

    public void sendValidationCodeEmail(String email, String username, String validationCode){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(username + ", thanks for registering for messaging");
        msg.setText("Hello \n here is your verification code \n " + validationCode + "\n you can also use this link \n" +
                "http://localhost:8080/users/activate?username=" + username + "&validationCode=" + validationCode);

        javaMailSender.send(msg);
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

        long diffInMillies = Math.abs(new Date().getTime() - userValidationCode.getCreateTime().getTime());

        if(diffInMillies >= 3 * 60 * 1000){
            throw new MessageServiceException(Status.VALIDATION_CODE_EXPIRED);
        }

        this.userDAO.setUserValid(user.getId());

        this.userValidationCodeDAO.deleteUserValidationCode(user.getId());
    }

    public void userLogin(User user) throws MessageServiceException{
        if(user.isValid() == false){
            throw new MessageServiceException(Status.USER_NOT_ACTIVATED);
        }
        Date loginTime = new Date();
        this.userDAO.setUserLastLoginTime(loginTime, user.getUsername());

        String loginToken = generateToken();

        this.userDAO.setUserLoginToken(loginToken, user.getUsername());
    }

    public User verifyLoginToken(String loginToken){
        User user = this.userDAO.findUserByLoginToken(loginToken);
        return user;
    }

    public User verifyUsername(String username){
        User user = this.userDAO.findUserByUsername(username);
        return user;
    }

    public UserValidationCode getValidationCode(int userId){
        return this.userValidationCodeDAO.findUserValidationCodeByUserId(userId);
    }

    public void userLogout(String username) throws MessageServiceException{
        this.userDAO.setUserLoginToken(null, username);
    }
}
