package com.example.demo.repository;

import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link PostRepository} 테스트 코드
 * @author gihyung.lee
 * @since 2026-06-07
 */
@Sql("/sql/post-repository-test-data.sql")
@DataJpaTest
@TestPropertySource("classpath:test-application.properties")
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void findById_로_게시글_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<PostEntity> result = postRepository.findById(1L);

        // then
        assertThat(result.isPresent()).isTrue();
    }
}
