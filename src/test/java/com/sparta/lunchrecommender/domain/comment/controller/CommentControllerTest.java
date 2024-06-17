package com.sparta.lunchrecommender.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lunchrecommender.domain.comment.dto.CommentRequestDto;
import com.sparta.lunchrecommender.domain.comment.dto.CommentResponseDto;
import com.sparta.lunchrecommender.domain.comment.entity.Comment;
import com.sparta.lunchrecommender.domain.comment.service.CommentService;
import com.sparta.lunchrecommender.domain.post.controller.PostController;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {UserController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class CommentControllerTest {
    @Autowired
    private MockMvc mvc;

    Principal mockPrincipal; // 가짜 인증 객체

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    Post post;
    User user;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();
        // Mock 테스트 유져 생성
        String loginId = "alswjd9999";
        String password = "Qwerty123?";
        String email = "alswjd@sparta.com";
        String name = "alswjd";
        String nickname = "leemj";
        String intro = "hello";
        UserStatus userStatus = UserStatus.ACTIVE;
        this.user = new User(loginId, password, name, nickname, email, intro, userStatus);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(user);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());

        PostCreateRequestDto requestDto = new PostCreateRequestDto("게시물 내용");
        this.post = new Post(requestDto);
        post.setPostId(100L);

    }

    @Test
    @DisplayName("addComment 테스트")
    void addComment() throws Exception {
        //given
        String content = "새 댓글";
        CommentRequestDto requestDto = new CommentRequestDto(content);

        String requestBody = objectMapper.writeValueAsString(requestDto);

        mvc.perform(post("/api/post/{post_id}/comment", post.getPostId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("updateComment 테스트")
    void updateComment() throws Exception {
        //given
        String content = "댓글";
        CommentRequestDto requestDto = new CommentRequestDto(content);
        Comment comment = new Comment(requestDto, post, user);
        comment.setCommentId(100L);
        CommentResponseDto responseDto = new CommentResponseDto(comment);

        String requestBody = objectMapper.writeValueAsString(requestDto);
        given(commentService.updateComment(eq(post.getPostId()), eq(comment.getCommentId()), eq(requestDto),eq(user))).willReturn(responseDto);

        mvc.perform(patch("/api/post/{post_id}/comment/{comment_id}", post.getPostId(), comment.getCommentId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("findCommentById 테스트")
    void findCommentById() throws Exception{
        String content = "댓글";
        CommentRequestDto requestDto = new CommentRequestDto(content);
        Comment comment = new Comment(requestDto, post, user);
        comment.setCommentId(100L);
        CommentResponseDto responseDto = new CommentResponseDto(comment);

        given(commentService.findCommentById(eq(post.getPostId()), eq(100L))).willReturn(responseDto);

        mvc.perform(get("/api/post/{post_id}/comment/{comment_id}", post.getPostId(), 100L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("findCommentAll 테스트")
    void findCommentAll() throws Exception{
        Comment comment1 = new Comment(new CommentRequestDto("댓글1"), post, user);
        Comment comment2 = new Comment(new CommentRequestDto("댓글2"), post, user);
        comment1.setCommentId(100L);
        comment2.setCommentId(200L);
        CommentResponseDto responseDto1 = new CommentResponseDto(comment1);
        CommentResponseDto responseDto2 = new CommentResponseDto(comment2);

        given(commentService.findCommentAll(post.getPostId())).willReturn(
                List.of(responseDto1, responseDto2));

        mvc.perform(get("/api/post/{post_id}/comment/getList", post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("deleteComment 테스트")
    void deleteComment() throws Exception{
        doNothing().when(commentService).deleteComment(eq(post.getPostId()),eq(100L),eq(user));

        mvc.perform(delete("/api/post/{post_id}/comment/{comment_id}", post.getPostId(),100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk());

    }
}