package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeUserRepository userRepository = new FakeUserRepository();

        this.userService = UserServiceImpl.builder()
                .clockHolder(new TestClockHolder(1))
                .uuidHolder(new TestUuidHolder("aaaaaa-aaaa-aaaa-aaaa-aaaaaa"))
                .userRepository(userRepository)
                .certificationService(new CertificationServiceImpl(new FakeMailSender()))
                .build();

        userRepository.save(User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());
        userRepository.save(User.builder()
                .id(2L)
                .email("gihyung.coding2@gmail.com")
                .nickname("gihyunglee2")
                .address("Seoul")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
        userRepository.save(User.builder()
                .id(3L)
                .email("'gihyung.coding3@gmail.com")
                .nickname("gihyunglee3")
                .address("Seoul")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .status(UserStatus.INACTIVE)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "gihyung.coding@gmail.com";

        // when
        User user = userService.getByEmail(email);

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void getByEmail은_PENDING_또는_INACTIVE_상태인_유저를_찾아올_수_없다() {
        // given
        String pendingUserEmail = "gihyung.coding2@gmail.com";
        String inactiveUserEmail = "gihyung.coding3@gmail.com";

        // when
        // then
        assertThatThrownBy(() -> {
            User pendingUser = userService.getByEmail(pendingUserEmail);
        }).isInstanceOf(ResourceNotFoundException.class);

        assertThatThrownBy(() -> {
            User inactiveUser = userService.getByEmail(inactiveUserEmail);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        long id = 1L;

        // when
        User entity = userService.getById(id);

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
            User pendingUser = userService.getById(pendingUserId);
        }).isInstanceOf(ResourceNotFoundException.class);

        assertThatThrownBy(() -> {
            User inactiveUser = userService.getById(inactiveUserId);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void userCreateDto_를_이용하여_유저_데이터를_생성할_수_있음() {
        // given
        UserCreate dto = UserCreate.builder()
                .email("gihyung.coding4@gmail.com")
                .nickname("gihyung.coding4")
                .address("Seoul")
                .build();

        // when
        User entity = userService.create(dto);

        // then
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(entity.getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaa");
    }

    @Test
    void userUpdateDto_를_이용하여_유저_데이터를_수정할_수_있음() {
        // given
        UserUpdate dto = UserUpdate.builder()
                .nickname("gihyung.coding1_2")
                .address("Seoul2")
                .build();
        // when
        User entity = userService.update(1, dto);

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
        User entity = userService.getById(id);
        assertThat(entity.getLastLoginAt()).isEqualTo(1L);
    }

    @Test
    void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다() {
        // given
        long id = 2;
        String certificationCode = "aaaaaa-aaaa-aaaa-aaaa-aaaaaa";

        // when
        userService.verifyEmail(id, certificationCode);

        // then
        User entity = userService.getById(id);
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
