package com.example.demo.user.service.port;

public interface MailSender {
    void send(String email, String subject, String content);
}
