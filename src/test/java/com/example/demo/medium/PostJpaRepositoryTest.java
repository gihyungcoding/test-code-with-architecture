package com.example.demo.medium;

import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.post.infrastructure.PostJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link PostJpaRepository} 테스트 코드
 * @author gihyung.lee
 * @since 2026-06-07
 */
@Sql("/sql/post-repository-test-data.sql")
@DataJpaTest
@TestPropertySource("classpath:test-application.properties")
public class PostJpaRepositoryTest {

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Test
    void findById_로_게시글_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<PostEntity> result = postJpaRepository.findById(1L);

        // then
        assertThat(result.isPresent()).isTrue();
    }
}
