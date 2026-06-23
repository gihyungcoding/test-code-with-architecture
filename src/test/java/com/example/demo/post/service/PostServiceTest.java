package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PostServiceTest {

    private PostServiceImpl postService;


    @BeforeEach
    public void init() {

        FakeUserRepository userRepository = new FakeUserRepository();
        User user = User.builder()
                .id(1L)
                .email("gihyung.coding@gmail.com")
                .nickname("gihyunglee")
                .address("Seoul")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();

        userRepository.save(user);

        FakePostRepository postRepository = new FakePostRepository();

        postRepository.save(Post.builder()
                        .id(1L)
                        .content("hello world")
                        .createdAt(1780000000000L)
                        .modifiedAt(0L)
                        .writer(user)
                        .build());

        this.postService = PostServiceImpl.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(new TestClockHolder(1L))
                .build();

    }

    @Test
    void getPostById은_단일_게시물_정보를_찾아올_수_있다() {
        // given
        long id = 1L;

        // when
        Post entity = postService.getPostById(id);

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
            Post entity = postService.getPostById(id);
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
        Post entity = postService.create(dto);

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
        Post entity = postService.update(1, dto);

        // then
        assertThat(entity.getContent()).isEqualTo("hello world 2");
        // TODO 수정일 검증 필요
    }
}
