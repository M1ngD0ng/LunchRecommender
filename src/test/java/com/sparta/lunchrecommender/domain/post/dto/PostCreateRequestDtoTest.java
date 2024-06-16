package com.sparta.lunchrecommender.domain.post.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostCreateRequestDtoTest {
    @Test
    @DisplayName("PostCreateRequestDto 생성 테스트")
    void test1(){
        String content = "테스트 내용입니다.";
        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto(content);
        assertEquals(content, postCreateRequestDto.getContent());
    }

}