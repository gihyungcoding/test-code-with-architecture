package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseTest {
    @Test
    public void UserResponse_는_User_객체로_생성할_수_있다() {
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
        UserResponse userResponse = UserResponse.from(user);
        // then
        assertThat(userResponse.getId()).isEqualTo(1L);
        assertThat(userResponse.getEmail()).isEqualTo("gihyung.coding@gmail.com");
        assertThat(userResponse.getNickname()).isEqualTo("gihyunglee");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(1L);
    }
}
