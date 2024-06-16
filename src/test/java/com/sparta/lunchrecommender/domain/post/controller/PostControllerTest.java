package com.sparta.lunchrecommender.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostResponseDto;
import com.sparta.lunchrecommender.domain.post.dto.PostUpdateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.post.service.PostService;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.controller.UserController;
import com.sparta.lunchrecommender.domain.user.entity.User;
import com.sparta.lunchrecommender.domain.user.repository.UserRepository;
import com.sparta.lunchrecommender.domain.user.service.UserService;
import com.sparta.lunchrecommender.global.config.WebSecurityConfig;
import com.sparta.lunchrecommender.global.security.UserDetailsImpl;
import com.sparta.lunchrecommender.mvc.MockSpringSecurityFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {UserController.class, PostController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class PostControllerTest {
    @Autowired
    private MockMvc mvc;

    Principal mockPrincipal; // 가짜 인증 객체

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String loginId = "alswjd9999";
        String password = "Qwerty123?";
        String email = "alswjd@sparta.com";
        String name = "alswjd";
        String nickname = "leemj";
        String intro = "hello";
        UserStatus userStatus = UserStatus.ACTIVE;
        User user = new User(loginId, password, name, nickname, email, intro, userStatus);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(user);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("PostController - getPosts 테스트")
    void test1() throws Exception {
        String startDate = "20240619";
        String endDate = "20240620";

        MultiValueMap<String, String> listRequestForm = new LinkedMultiValueMap<>();
        listRequestForm.add("page", "0");
        listRequestForm.add("sortBy", "createdAt");
        listRequestForm.add("startDate", startDate);
        listRequestForm.add("endDate", endDate);
        PostCreateRequestDto requestDto = new PostCreateRequestDto("게시물 내용");
        Post post = new Post(requestDto);

        User user = new User(
                "alswjd1111",
                "Qwerty123?",
                "alswjd22",
                "lee",
                "alswjd@naver.com",
                "소개", UserStatus.ACTIVE);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        Page<PostResponseDto> posts = new PageImpl<>(Collections.singletonList(new PostResponseDto(post, user)));
        given(postService.getPosts(
                eq(0),
                eq("createdAt"),
                eq(LocalDate.parse(startDate, formatter).atStartOfDay()),
                eq(LocalDate.parse(endDate, formatter).atTime(23, 59, 59))))
                .willReturn(posts);

        // when - then
        mvc.perform(get("/api/posts/getList")
                        .params(listRequestForm))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PostController - createPost 테스트")
    void test2() throws Exception {
        this.mockUserSetup();
        String content = "새 게시물 내용";
        PostCreateRequestDto requestDto = new PostCreateRequestDto(content);

        String requestBody = objectMapper.writeValueAsString(requestDto);

        mvc.perform(post("/api/post")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("PostController - updatePost 테스트")
    void test3() throws Exception {
        this.mockUserSetup();
        String oldContent = "기존 게시물 내용";
        PostCreateRequestDto requestDto = new PostCreateRequestDto(oldContent);
        Post post = new Post(requestDto);
        post.setPostId(100L);

        String newContent = "수정된 게시물 내용";
        PostUpdateRequestDto updateRequestDto = new PostUpdateRequestDto(newContent);

        String requestBody = objectMapper.writeValueAsString(updateRequestDto);

        mvc.perform(patch("/api/post/{post_Id}",post.getPostId()) // 여기에 어떤 ID가 들어가도 테스트 통과되는 이유 이해안됨
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("PostController - deletePost 테스트")
    void test4() throws Exception {
        this.mockUserSetup();
        mvc.perform(delete("/api/post/{post_Id}",1L)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

}