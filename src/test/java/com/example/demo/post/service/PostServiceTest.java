package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/post-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})
public class PostServiceTest {

    @Autowired
    private PostService postService;
    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getPostById은_단일_게시물_정보를_찾아올_수_있다() {
        // given
        long id = 1L;

        // when
        PostEntity entity = postService.getPostById(id);

        // then
        assertThat(entity.getId()).isEqualTo(1L);
    }

    @Test
    void getPostById은_유효하지_않은_게시물_식별자로_조회_시_오류를_반환한다() {
        // given
        long id = 2L;

        // when
        // then
        assertThatThrownBy(() -> {
            PostEntity entity = postService.getPostById(id);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void postCreateDto_를_이용하여_게시물_데이터를_생성할_수_있음() {
        // given
        PostCreate dto = PostCreate.builder()
                .content("hello world")
                .writerId(1L)
                .build();

        // when
        PostEntity entity = postService.create(dto);

        // then
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getContent()).isEqualTo("hello world");
        // TODO 생성일 검증 필요
    }

    @Test
    void postUpdateDto_를_이용하여_게시물_데이터를_수정할_수_있음() {
        // given
        PostUpdate dto = PostUpdate.builder()
                .content("hello world 2")
                .build();
        // when
        PostEntity entity = postService.update(1, dto);

        // then
        assertThat(entity.getContent()).isEqualTo("hello world 2");
        // TODO 수정일 검증 필요
    }
}
