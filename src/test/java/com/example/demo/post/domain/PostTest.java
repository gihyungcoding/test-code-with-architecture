package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    public void Post_는_PostCreate_객체로_생성할_수_있다(){
        // given
        PostCreate dto = PostCreate.builder()
                .content("hello world")
                .writerId(1L)
                .build();
        User user = User.builder()
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .build();
        
        // when
        Post post = Post.from(user, dto, new TestClockHolder(1L));
        // then
        assertThat(post.getId()).isNull();
        assertThat(post.getContent()).isEqualTo("hello world");
        assertThat(post.getCreatedAt()).isEqualTo(1L);
        assertThat(post.getWriter().getEmail()).isEqualTo("gihyung.coding@gmail.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("gihyunglee");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaa");
    }

    @Test
    public void Post_는_PostUpdate_객체로_본문과_수정일을_수정할_수_있다(){

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

        PostUpdate dto = PostUpdate.builder()
                .content("hello world 2")
                .build();


        // when
        post = post.update(dto, new TestClockHolder(2L));

        // then
        assertThat(post.getId()).isEqualTo(1L);
        assertThat(post.getContent()).isEqualTo("hello world 2");
        assertThat(post.getModifiedAt()).isEqualTo(2L);
    }
}
