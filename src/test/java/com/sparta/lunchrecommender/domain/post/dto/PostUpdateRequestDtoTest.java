package com.sparta.lunchrecommender.domain.post.dto;

import com.sparta.lunchrecommender.domain.post.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostUpdateRequestDtoTest {
    @Test
    @DisplayName("PostUpdateRequestDto 생성 테스트")
    void test1() {
        //given
        String content="테스트 내용입니다.";
        PostUpdateRequestDto postUpdateRequestDto = new PostUpdateRequestDto(content);

        assertEquals(content, postUpdateRequestDto.getContent());
    }

}