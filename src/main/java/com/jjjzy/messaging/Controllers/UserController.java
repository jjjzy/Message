package com.jjjzy.messaging.Controllers;

import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Exceptions.MessageServiceException;
import com.jjjzy.messaging.Request.ActivateUserRequest;
import com.jjjzy.messaging.Request.RegisterUserRequest;
import com.jjjzy.messaging.Request.UserLoginRequest;
import com.jjjzy.messaging.Request.UserLogoutRequest;
import com.jjjzy.messaging.Response.ActivateUserResponse;
import com.jjjzy.messaging.Response.RegisterUserResponse;
import com.jjjzy.messaging.Response.UserLoginResponse;
import com.jjjzy.messaging.Response.UserLogoutResponse;
import com.jjjzy.messaging.service.UserService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public RegisterUserResponse register(@RequestBody RegisterUserRequest registerUserRequest) throws MessageServiceException {
        if (!registerUserRequest.getPassword().equals(registerUserRequest.getRepeatPassword())) {
            throw new MessageServiceException(


                    Status.PASSWORDS_NOT_MATCH);
        }
//        System.out.println(registerUserRequest.getAddress());
//        System.out.println(registerUserRequest.getPassword());
//        System.out.println(registerUserRequest.getRepeatPassword());
        this.userService.registerUser(registerUserRequest.getUsername(),
                registerUserRequest.getPassword(),
                registerUserRequest.getEmail(),
                registerUserRequest.getNickname(),
                registerUserRequest.getGender(),
                registerUserRequest.getAddress());

        return new RegisterUserResponse(Status.OK);
    }

    @PostMapping("/activate")
    public ActivateUserResponse activateUser(@RequestBody ActivateUserRequest activateUserRequest) throws MessageServiceException {
        this.userService.activateUser(activateUserRequest.getUsername(),
                activateUserRequest.getValidationCode());
        return new ActivateUserResponse(Status.OK);
    }

    @PostMapping("/login")
    public UserLoginResponse userLogin(@RequestBody UserLoginRequest userLoginRequest) throws MessageServiceException {
        System.out.println(userLoginRequest.getUsername());
        System.out.println(userLoginRequest.getPassword());
        this.userService.userLogin(userLoginRequest.getUsername(),
                userLoginRequest.getPassword());
        return new UserLoginResponse(Status.OK);
    }

    @PostMapping("/logout")
    public UserLogoutResponse userLogout(@RequestBody UserLogoutRequest userLogoutRequest) throws MessageServiceException {
        System.out.println(userLogoutRequest.getUsername());
        System.out.println(userLogoutRequest.getLoginToken());
        this.userService.userLogout(userLogoutRequest.getUsername(),
                userLogoutRequest.getLoginToken());
        return new UserLogoutResponse(Status.OK);
    }
}
//./mvnw spring-boot:run
//messaging_password_2022
