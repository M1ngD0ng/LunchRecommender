package com.sparta.lunchrecommender.domain.post.dto;

import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostResponseDtoTest {
    @Test
    @DisplayName("PostResponseDto 생성 테스트")
    void test1(){
        //given
        String content = "테스트 내용입니다.";
        PostCreateRequestDto requestDto = new PostCreateRequestDto(content);
        Post post = new Post(requestDto);

        String loginId = "alswjd9999";
        String password = "Qwerty123?";
        String email = "alswjd@sparta.com";
        String name = "alswjd";
        String nickname = "leemj";
        String intro = "hello";
        UserStatus userStatus = UserStatus.ACTIVE;
        User user = new User(loginId, password, name, nickname, email, intro, userStatus);

        //when
        PostResponseDto postResponseDto = new PostResponseDto(post, user);

        //then
        assertEquals(loginId, postResponseDto.getLoginid());
        assertEquals(nickname, postResponseDto.getNickname());
        assertEquals(content, postResponseDto.getContent());
    }
}