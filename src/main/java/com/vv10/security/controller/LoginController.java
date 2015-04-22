package com.vv10.security.controller;

import com.vv10.security.domain.LoginResponse;
import com.vv10.security.domain.LoginStatus;
import com.vv10.security.domain.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.String;
import java.util.Optional;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    @Qualifier("users")
    Users users;
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public LoginResponse getLoginPage(@RequestParam String user, @RequestParam String passwd) {
        LoginResponse response = new LoginResponse();
        response.setStatus(LoginStatus.OK);
        response.setKey("session_key");
        response.setMessage("welcome !");
        return response;
    }

}