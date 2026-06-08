package com.example.demo.mock;

import com.example.demo.user.service.port.MailSender;

public class FakeMailSender implements MailSender {

    public String email;
    public String subject;
    public String content;


    @Override
    public void send(String email, String subject, String content) {
        this.email = email;
        this.subject = subject;
        this.content = content;
    }
}
