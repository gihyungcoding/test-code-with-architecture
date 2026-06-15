package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostResponseTest {
    @Test
    public void PostResponse_는_Post_객체로_생성할_수_있다() {
        // given
        Post post = Post.builder()
                .id(1L)
                .content("hello world")
                .createdAt(1L)
                .modifiedAt(1L)
                .writer(User.builder()
                                .id(1L)
                                .email("gihyung.coding@gmail.com")
                                .nickname("gihyunglee")
                                .address("Seoul")
                                .status(UserStatus.ACTIVE)
                                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                                .lastLoginAt(1L)
                                .build())
                .build();
        // when
        PostResponse response = PostResponse.from(post);
        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo("hello world");
        assertThat(response.getCreatedAt()).isEqualTo(1L);
        assertThat(response.getModifiedAt()).isEqualTo(1L);
        assertThat(response.getWriter().getId()).isEqualTo(1L);
        assertThat(response.getWriter().getEmail()).isEqualTo("gihyung.coding@gmail.com");
        assertThat(response.getWriter().getNickname()).isEqualTo("gihyunglee");
        assertThat(response.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(response.getWriter().getLastLoginAt()).isEqualTo(1L);
    }
}
