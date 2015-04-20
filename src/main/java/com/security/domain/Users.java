package com.security.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apredoiu on 4/20/15.
 */
//@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix="users")
@Component("users")
public class Users {

    @JsonDeserialize(as=ArrayList.class, contentAs=User.class)
    private List<User> users = new ArrayList<>();

    @Override
    public String toString(){
        String rez;
        rez = "nr of users:" + users.size();
        for(User user: users) {
            rez += user.toString();
        }
        return rez;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
