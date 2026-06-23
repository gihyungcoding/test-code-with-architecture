package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_전달_받을_수_있다() throws Exception {

        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1L)
                .build());
        // given
        // when
        ResponseEntity<UserResponse> result = testContainer.userController
                .getUserById(1);
        // than
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("gihyung.coding@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("gihyunglee");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1L);
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api를_호출할_경우_404_응답을_받는다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1L)
                .build());

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.userController.getUserById(123);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증코드로_계정을_활성화_시킬_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(2L)
                .email("gihyung.coding2@gmail.com")
                .nickname("gihyunglee2")
                .address("Seoul")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(2L, "aaaaaa-aaaa-aaaa-aaaa-aaaaaa");

        // than
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.findById(2L).get().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_유효하지_않은_인증코드로_계정을_활성화_시킬_수_없다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(2L)
                .email("gihyung.coding2@gmail.com")
                .nickname("gihyunglee2")
                .address("Seoul")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
        // when
        assertThatThrownBy(() -> {
            testContainer.userController.verifyEmail(2L, "aaaaaa-aaaa-aaaa-aaaa-aaaaaa21");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {

        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(1L))
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1L)
                .build());

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("gihyung.coding@gmail.com");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("gihyung.coding@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("gihyunglee");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1L);
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1L)
                .build());

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("gihyunglee1_1")
                .address("Cheonan")
                .build();

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("gihyung.coding@gmail.com", userUpdate);

        // than
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("gihyung.coding@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("gihyunglee1_1");
        assertThat(result.getBody().getAddress()).isEqualTo("Cheonan");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}
