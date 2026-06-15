package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyProfileResponseTest {
    @Test
    public void MyProfileResponseTest_는_User_객체로_생성할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1L)
                .build();
        // when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);
        // then
        assertThat(myProfileResponse.getId()).isEqualTo(1L);
        assertThat(myProfileResponse.getEmail()).isEqualTo("gihyung.coding@gmail.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("gihyunglee");
        assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(1L);
    }
}
