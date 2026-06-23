package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.*;
import com.example.demo.user.service.CertificationServiceImpl;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {
    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;

    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final UserReadService userReadService;
    public final UserAuthenticationService userAuthenticationService;

    public final PostService postService;
    public final CertificationService certificationService;

    public final UserController userController;
    public final UserCreateController userCreateController;

    public final PostController postController;
    public final PostCreateController postCreateController;

    @Builder
    public TestContainer(UuidHolder uuidHolder, ClockHolder clockHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();

        this.certificationService = CertificationServiceImpl.builder()
                .mailSender(this.mailSender)
                .build();
        this.postService = PostServiceImpl.builder()
                .userRepository(this.userRepository)
                .postRepository(this.postRepository)
                .clockHolder(clockHolder)
                .build();
        UserServiceImpl userService = UserServiceImpl.builder()
                .certificationService(this.certificationService)
                .userRepository(this.userRepository)
                .uuidHolder(uuidHolder)
                .clockHolder(clockHolder)
                .build();
        this.userCreateService = userService;
        this.userUpdateService = userService;
        this.userReadService = userService;
        this.userAuthenticationService = userService;

        this.userController = UserController.builder()
                .userReadService(this.userReadService)
                .userCreateService(this.userCreateService)
                .userUpdateService(this.userUpdateService)
                .userAuthenticationService(this.userAuthenticationService)
                .build();

        this.userCreateController = UserCreateController.builder()
                .userCreateService(this.userCreateService)
                .build();

        this.postController = PostController.builder()
                .postService(this.postService)
                .build();
        this.postCreateController = PostCreateController.builder()
                .postService(this.postService)
                .build();
    }

}
