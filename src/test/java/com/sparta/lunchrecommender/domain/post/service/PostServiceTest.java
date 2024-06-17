package com.sparta.lunchrecommender.domain.post.service;

import com.sparta.lunchrecommender.domain.comment.repository.CommentRepository;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostResponseDto;
import com.sparta.lunchrecommender.domain.post.dto.PostUpdateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.post.repository.PostRepository;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import com.sparta.lunchrecommender.domain.user.repository.UserRepository;
import com.sparta.lunchrecommender.global.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.core.env.Environment;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = {
        "jwt.secret.key=vasafl444ksdjgmaliekrjgpoiareptokekkgfigll39d99s73jlakjsqtpweouqpwa32sdfsasd57wa"
})
class PostServiceTest {
    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

    User user;
    Post post;

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
        user = new User(loginId, password, name, nickname, email, intro, userStatus);
        userRepository.save(user);
    }
    @AfterAll
    @Disabled
    public void clearData() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    @Order(1)
    @DisplayName("createPost 테스트")
    void createPost(){
        //given
        String content = "내용입니다.";
        PostCreateRequestDto requestDto = new PostCreateRequestDto(content);
        PostResponseDto responseDto = postService.createPost(requestDto,user);

        this.post = postRepository.findById(responseDto.getPostid()).orElseThrow(IllegalArgumentException::new);

        //then
        assertNotNull(responseDto.getPostid());
        assertEquals(content, responseDto.getContent());
    }

    @Test
    @Order(2)
    @DisplayName("updatePost 테스트")
    void updatePost(){
        String content = "수정된 내용입니다.";
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto(content);
        PostResponseDto responseDto = postService.updatePost(post.getPostId(),requestDto,user);

        assertEquals(responseDto.getPostid(), post.getPostId());
        assertEquals(content, responseDto.getContent());
    }

    @Test
    @Order(3)
    @DisplayName("getPosts 테스트")
    void getPosts(){
        Page<PostResponseDto> responseDtos = postService.getPosts(0,"createdAt", null, null);
        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.getTotalPages());
        assertEquals(1, responseDtos.getNumberOfElements());
        assertEquals(post.getPostId(), responseDtos.getContent().get(0).getPostid());
    }

    @Test
    @Order(4)
    @DisplayName("deletePost 테스트")
    void deletePost(){
        postService.deletePost(post.getPostId(), user);

        Optional<Post> optionalPost = postRepository.findById(post.getPostId());
        assertTrue(optionalPost.isEmpty());
    }
}