package com.sparta.lunchrecommender.domain.comment.service;

import com.sparta.lunchrecommender.domain.comment.dto.CommentRequestDto;
import com.sparta.lunchrecommender.domain.comment.dto.CommentResponseDto;
import com.sparta.lunchrecommender.domain.comment.entity.Comment;
import com.sparta.lunchrecommender.domain.comment.repository.CommentRepository;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.post.repository.PostRepository;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import com.sparta.lunchrecommender.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = {
        "jwt.secret.key=vasafl444ksdjgmaliekrjgpoiareptokekkgfigll39d99s73jlakjsqtpweouqpwa32sdfsasd57wa"
})
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    User user;
    Post post;
    Comment comment;

    @BeforeAll
    public void setup(@Autowired UserRepository userRepository, @Autowired Environment environment) {
        // Mock 테스트 유져 생성
        String loginId = "alswjd1111";
        String password = "Qwerty123?";
        String email = "alswjd@sparta.com";
        String name = "alswjd";
        String nickname = "leemj";
        String intro = "hello";
        UserStatus userStatus = UserStatus.ACTIVE;
        user = userRepository.save(new User(loginId, password, name, nickname, email, intro, userStatus));

        PostCreateRequestDto requestDto = new PostCreateRequestDto("내용입니다.");
        post = new Post(requestDto);
        post.setUser(user);
        postRepository.save(post);

    }
    @AfterAll
    @Disabled
    public void clearData() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("addComment 테스트")
    void addComment() {
        CommentRequestDto requestDto = new CommentRequestDto("댓글입니다.");
        CommentResponseDto responseDto = commentService.addComment(post.getPostId(), requestDto, user);

        Optional<Comment> comment1 = commentRepository.findById(responseDto.getId());
        assertTrue(comment1.isPresent());
    }
}