package com.sparta.lunchrecommender.domain.comment.dto;

import com.sparta.lunchrecommender.domain.comment.entity.Comment;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentResponseDtoTest {

    @Test
    @DisplayName("생성자 테스트")
    void test1() {
        // given
        String contentComment="댓글 내용";
        CommentRequestDto commentRequestDto = new CommentRequestDto(contentComment);

        String contentPost = "게시물 내용";
        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto(contentPost);
        Post post = new Post(postCreateRequestDto);

        String loginId = "alswjd9999";
        String password = "Qwerty123?";
        String email = "alswjd@sparta.com";
        String name = "alswjd";
        String nickname = "leemj";
        String intro = "hello";
        UserStatus userStatus = UserStatus.ACTIVE;
        User user = new User(loginId, password, name, nickname, email, intro, userStatus);

        Comment comment = new Comment(commentRequestDto, post, user);

        // when
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        // then
        assertEquals(contentComment, commentResponseDto.getContent());
        assertEquals(post.getPostId(), commentResponseDto.getPost_id());
        assertEquals(user.getUserId(), commentResponseDto.getUser_id());
    }
}