package com.security.controller;

import com.security.domain.User;
import com.security.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apredoiu on 4/20/15.
 */

@RestController
public class UserController {

    @Autowired
    @Qualifier("users")
    Users users;

    @RequestMapping(value = "/user/all", method = RequestMethod.GET)
    public List<User> userAll(){
        return users.getUsers();
    }

}
