package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class UserTest {

    @Test
    public void User_는_UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate dto = UserCreate.builder()
                .email("gihyung.coding4@gmail.com")
                .nickname("gihyung.coding4")
                .address("Seoul")
                .build();
        // when
        User user = User.from(dto, new TestUuidHolder("aaaaaa-aaaa-aaaa-aaaa-aaaaaa"));
        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("gihyung.coding4@gmail.com");
        assertThat(user.getNickname()).isEqualTo("gihyung.coding4");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaa");
    }

    @Test
    public void User_는_UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1L)
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .build();
        UserUpdate dto = UserUpdate.builder()
                .nickname("gihyung.coding1_2")
                .address("Seoul2")
                .build();

        // when
        user = user.update(dto);
        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("gihyung.coding@gmail.com");
        assertThat(user.getNickname()).isEqualTo("gihyung.coding1_2");
        assertThat(user.getAddress()).isEqualTo("Seoul2");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaa");

    }

    @Test
    public void User_는_로그인_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1L)
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .build();
        // when
        user = user.login(new TestClockHolder(2L));
        // then
        assertThat(user.getLastLoginAt()).isEqualTo(2L);
    }

    @Test
    public void User_는_인증코드로_계정을_활성화_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(1L)
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .build();
        // when
        user = user.certification("aaaaaa-aaaa-aaaa-aaaa-aaaaaa");
        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void User_는_잘못된_인증코드로_계정을_활성화_하려하면_에러를_던진다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(1L)
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .build();
        // when
        // then
        assertThatThrownBy(() -> user.certification("aaaaaa-aaaa-aaaa-aaaa-aaaaaad"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);

    }
}
