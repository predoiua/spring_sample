package com.vv10.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by apredoiu on 4/20/15.
 */
public class User {
    private String name;
    @JsonIgnore
    private String passwd;

    @Override
    public String toString(){
        return "Name :" + name + " passwd:" + passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
