package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCreateControllerTest {

    @Test
    void 사용자는_유저를_생성할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(new TestUuidHolder("aaaaaa-aaaa-aaaa-aaaa-aaaaaa"))
                .build();
        UserCreate dto = UserCreate.builder()
                .email("gihyung.coding@gmail.com")
                .address("Cheonan")
                .nickname("gihyungcoding")
                .build();
        //when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(dto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("gihyung.coding@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("gihyungcoding");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
    }
}