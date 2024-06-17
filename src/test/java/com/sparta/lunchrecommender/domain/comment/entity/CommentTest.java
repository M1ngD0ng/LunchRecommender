package com.sparta.lunchrecommender.domain.comment.entity;

import com.sparta.lunchrecommender.domain.comment.dto.CommentRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    Post post;
    User user;

    @BeforeEach
    void setUp() {
        String contentPost = "게시물 내용";
        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto(contentPost);
        this.post = new Post(postCreateRequestDto);

        String loginId = "alswjd9999";
        String password = "Qwerty123?";
        String email = "alswjd@sparta.com";
        String name = "alswjd";
        String nickname = "leemj";
        String intro = "hello";
        UserStatus userStatus = UserStatus.ACTIVE;
        this.user = new User(loginId, password, name, nickname, email, intro, userStatus);
    }

    @Test
    @DisplayName("생성자 테스트")
    void test1(){
        // given
        String contentComment="댓글 내용";
        CommentRequestDto commentRequestDto = new CommentRequestDto(contentComment);

        // when
        Comment comment = new Comment(commentRequestDto, post, user);

        // then
        assertEquals(contentComment, comment.getContent());
        assertEquals(post.getPostId(), comment.getPost().getPostId());
        assertEquals(user.getUserId(), comment.getUser().getUserId());
        assertEquals(0L, comment.getLikeCount());
    }

    @Test
    @DisplayName("update 메서드 테스트")
    void test2(){
        // given
        String contentComment="댓글 내용";
        CommentRequestDto commentRequestDto = new CommentRequestDto(contentComment);
        Comment comment = new Comment(commentRequestDto, post, user);

        String newContentComment = "수정된 댓글";
        CommentRequestDto commentUpdateRequestDto = new CommentRequestDto(newContentComment);

        // when
        comment.update(commentUpdateRequestDto);

        assertEquals(comment.getContent(), newContentComment);


    }
}