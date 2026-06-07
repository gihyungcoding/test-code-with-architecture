package com.example.demo.controller;

import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.PostResponse;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PostUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_특정_게시글의_정보를_전달_받을_수_있다() throws Exception {
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("hello world"))
                .andExpect(jsonPath("$.writer.id").value(1))
                .andExpect(jsonPath("$.writer.email").value("gihyung.coding@gmail.com"))
                .andExpect(jsonPath("$.writer.nickname").value("gihyunglee"));
    }

    @Test
    void 사용자는_존재하지_않는_게시글의_식별자로_api를_호출할_경우_404_응답을_받는다() throws Exception {
        mockMvc.perform(get("/api/posts/123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Posts에서 ID 123를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_게시글을_수정할_수_있다() throws Exception {
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("hello world 2")
                .build();

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("hello world 2"))
                .andExpect(jsonPath("$.writer.id").value(1))
                .andExpect(jsonPath("$.writer.email").value("gihyung.coding@gmail.com"))
                .andExpect(jsonPath("$.writer.nickname").value("gihyunglee"));
    }
}
