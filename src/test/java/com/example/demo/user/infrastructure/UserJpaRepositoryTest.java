package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link UserJpaRepository} 테스트 코드
 * @author gihyung.lee
 * @since 2026-06-03
 */
@Sql("/sql/user-repository-test-data.sql")
@DataJpaTest
@TestPropertySource("classpath:test-application.properties")
public class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1L, UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_유저_데이터가_없으면_Optional_empty_를_내려준다() {
        // given
        // when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1L, UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("gihyung.coding@gmail.com", UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_유저_데이터가_없으면_Optional_empty_를_내려준다() {
        // given
        // when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("gihyung.coding2@gmail.com", UserStatus.ACTIVE);

        // then
        assertThat(result.isEmpty()).isTrue();
    }
}
