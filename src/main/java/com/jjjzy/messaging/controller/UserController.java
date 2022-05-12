package com.jjjzy.messaging.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Request.RegisterUserRequest;
import com.jjjzy.messaging.Response.*;
import com.jjjzy.messaging.annotation.NeedLoginTokenAuthentication;
import com.jjjzy.messaging.annotation.NeedUsernamePasswordAuthentication;
import com.jjjzy.messaging.service.UserService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Date;

import static com.jjjzy.messaging.Utils.utils.generateToken;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    Bucket bucket = Bucket4j.builder()
            .addLimit(Bandwidth.classic(200, Refill.intervally(200, Duration.ofMinutes(1))))
            .build();

    @PostMapping("/register")
    public RegisterUserResponse register(@RequestBody RegisterUserRequest registerUserRequest) throws MessageServiceException {
        if (bucket.tryConsume(1)) {
            if (!registerUserRequest.getPassword().equals(registerUserRequest.getRepeatPassword())) {
                throw new MessageServiceException(
                        Status.PASSWORDS_NOT_MATCH);
            }

            this.userService.registerUser(registerUserRequest.getUsername(),
                    registerUserRequest.getPassword(),
                    registerUserRequest.getEmail(),
                    registerUserRequest.getNickname(),
                    registerUserRequest.getGender(),
                    registerUserRequest.getAddress());

            return new RegisterUserResponse(Status.OK);
        }
        return new RegisterUserResponse(Status.TOO_MANY_REQUEST);
    }

    @GetMapping("/activate")
    public ActivateUserResponse activateUser(@RequestParam String username, @RequestParam String validationCode) throws MessageServiceException {
        this.userService.activateUser(username, validationCode);
        return new ActivateUserResponse(Status.OK);
    }

    @GetMapping("/sendEmail")
    @NeedUsernamePasswordAuthentication
    public BaseResponse sendValidationCodeEmail(User user){
        this.userService.updateValidationCode(user.getId(), generateToken(), new Date());
        this.userService.sendValidationCodeEmail(user.getEmail(), user.getUsername(), this.userService.getValidationCode(user.getId()).getValidationCode());
        return new BaseResponse(Status.OK);
    }

    @PostMapping("/login")
    @NeedUsernamePasswordAuthentication
    public UserLoginResponse userLogin(User user) throws MessageServiceException {
        this.userService.userLogin(user);
        return new UserLoginResponse(Status.OK);
    }

    @PostMapping("/logout")
    @NeedLoginTokenAuthentication
    public UserLogoutResponse userLogout(User user) throws MessageServiceException {
        this.userService.userLogout(user.getUsername());
        return new UserLogoutResponse(Status.OK);
    }
}
//./mvnw spring-boot:run
//messaging_password_2022
