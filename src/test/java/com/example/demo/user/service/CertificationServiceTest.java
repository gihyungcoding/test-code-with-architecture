package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CertificationServiceTest {

    @Test
    void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {
        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);
        // when
        certificationService.send("gihyung.coding@gmail.com", 1L, "aaaaaa-aaaa-aaaa-aaaa-aaaaaa");
        // then
        assertThat(fakeMailSender.email).isEqualTo("gihyung.coding@gmail.com");
        assertThat(fakeMailSender.subject).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaaa-aaaa-aaaa-aaaa-aaaaaa");
    }
}
