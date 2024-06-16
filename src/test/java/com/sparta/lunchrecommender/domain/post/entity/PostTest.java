package com.sparta.lunchrecommender.domain.post.entity;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PostTest {
    @Test
    @DisplayName("Post - 생성자 테스트")
    void test1(){
        //given
        String content = "테스트 내용입니다.";
        PostCreateRequestDto requestDto = new PostCreateRequestDto(content);

        //when
        Post post = new Post(requestDto);

        //then
        assertEquals(content, post.getContent());
    }

    @Test
    @DisplayName("Post - update 메서드 테스트")
    void test2(){
        //given
        String oldContent= "수정 전 내용입니다.";
        String newContent = "수정 후 내용입니다.";

        PostCreateRequestDto createRequestDto = new PostCreateRequestDto(oldContent);
        Post post = new Post(createRequestDto);

        PostUpdateRequestDto updateRequestDto = new PostUpdateRequestDto(newContent);

        //when
        post.update(updateRequestDto);

        //then
        assertEquals(newContent, post.getContent());
    }
}