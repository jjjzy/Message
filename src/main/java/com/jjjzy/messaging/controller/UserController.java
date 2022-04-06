package com.jjjzy.messaging.controller;

import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Models.User;
import com.jjjzy.messaging.Request.ActivateUserRequest;
import com.jjjzy.messaging.Request.RegisterUserRequest;
import com.jjjzy.messaging.Request.UserLoginRequest;
import com.jjjzy.messaging.Request.UserLogoutRequest;
import com.jjjzy.messaging.Response.ActivateUserResponse;
import com.jjjzy.messaging.Response.RegisterUserResponse;
import com.jjjzy.messaging.Response.UserLoginResponse;
import com.jjjzy.messaging.Response.UserLogoutResponse;
import com.jjjzy.messaging.annotation.NeedLoginTokenAuthentication;
import com.jjjzy.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public RegisterUserResponse register(@RequestBody RegisterUserRequest registerUserRequest) throws MessageServiceException {
        //TODO
        //maybe add aop to check login
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

    @GetMapping("/activate")
    public ActivateUserResponse activateUser(@RequestParam String username, @RequestParam String validationCode) throws MessageServiceException {
        this.userService.activateUser(username,
                validationCode);
        return new ActivateUserResponse(Status.OK);
    }

    @PostMapping("/login")
    public UserLoginResponse userLogin(@RequestBody UserLoginRequest userLoginRequest) throws MessageServiceException {
        this.userService.userLogin(userLoginRequest.getUsername(),
                userLoginRequest.getPassword());
        return new UserLoginResponse(Status.OK);
    }

    @PostMapping("/logout")
    @NeedLoginTokenAuthentication
    public UserLogoutResponse userLogout(User user) throws MessageServiceException {
        this.userService.userLogout(user.getId());
        return new UserLogoutResponse(Status.OK);
    }
}
//./mvnw spring-boot:run
//messaging_password_2022
//

//TODO
//YML TO RUN ITEGRATION TEST?