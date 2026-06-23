package com.example.demo.user.controller.port;

public interface UserAuthenticationService {

    void login(long id);

    void verifyEmail(long id, String certificationCode);
}
