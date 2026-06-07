package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "gihyung.coding@gmail.com";

        // when
        UserEntity entity = userService.getByEmail(email);

        // then
        assertThat(entity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void getByEmail은_PENDING_또는_INACTIVE_상태인_유저를_찾아올_수_없다() {
        // given
        String pendingUserEmail = "gihyung.coding2@gmail.com";
        String inactiveUserEmail = "gihyung.coding3@gmail.com";

        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity pendingUser = userService.getByEmail(pendingUserEmail);
        }).isInstanceOf(ResourceNotFoundException.class);

        assertThatThrownBy(() -> {
            UserEntity inactiveUser = userService.getByEmail(inactiveUserEmail);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        long id = 1L;

        // when
        UserEntity entity = userService.getById(id);

        // then
        assertThat(entity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void getById은_PENDING_또는_INACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        long pendingUserId = 2L;
        long inactiveUserId = 3L;

        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity pendingUser = userService.getById(pendingUserId);
        }).isInstanceOf(ResourceNotFoundException.class);

        assertThatThrownBy(() -> {
            UserEntity inactiveUser = userService.getById(inactiveUserId);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void userCreateDto_를_이용하여_유저_데이터를_생성할_수_있음() {
        // given
        UserCreateDto dto = UserCreateDto.builder()
                .email("gihyung.coding4@gmail.com")
                .nickname("gihyung.coding4")
                .address("Seoul")
                .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        // when
        UserEntity entity = userService.create(dto);

        // then
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getStatus()).isEqualTo(UserStatus.PENDING);
        // UUID 테스트.. 추가해야함..
    }

    @Test
    void userUpdateDto_를_이용하여_유저_데이터를_수정할_수_있음() {
        // given
        UserUpdateDto dto = UserUpdateDto.builder()
                .nickname("gihyung.coding1_2")
                .address("Seoul2")
                .build();
        // when
        UserEntity entity = userService.update(1, dto);

        // then
        assertThat(entity.getNickname()).isEqualTo("gihyung.coding1_2");
        assertThat(entity.getAddress()).isEqualTo("Seoul2");
    }

    @Test
    void user_를_로그인_시키면_마지막_로그인_시간이_변경됨() {
        // given
        long id = 1;

        // when
        userService.login(1);

        // then
        UserEntity entity = userService.getById(id);
        // 시간 검증 보완해야함
        assertThat(entity.getLastLoginAt()).isGreaterThan(0L);
    }

    @Test
    void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다() {
        // given
        long id = 2;
        String certificationCode = "aaaaaa-aaaa-aaaa-aaaa-aaaaaa";

        // when
        userService.verifyEmail(id, certificationCode);

        // then
        UserEntity entity = userService.getById(id);
        // 시간 검증 보완해야함
        assertThat(entity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증코드로_ACTIVE_시킬_수_없다() {
        // given
        long id = 2;
        String certificationCode = "aaaaaa-aaaa-aaaa-aaaa-aaaaaa23";

        // when
        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(id, certificationCode);
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
