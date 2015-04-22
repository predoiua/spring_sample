package com.vv10.security.domain;

/**
 * Created by predoiua on 4/22/15.
 */
public class LoginResponse {
    private LoginStatus status;
    private String key;
    private String message;

    public LoginResponse(){
    }

    public LoginStatus getStatus() {
        return status;
    }

    public void setStatus(LoginStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
