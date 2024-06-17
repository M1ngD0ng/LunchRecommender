package com.sparta.lunchrecommender.domain.comment.dto;

import com.sparta.lunchrecommender.domain.comment.entity.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentRequestDtoTest {
    @Test
    @DisplayName("생성자 테스트")
    void test1(){
        String content = "댓글 내용입니다.";
        CommentRequestDto commentRequestDto = new CommentRequestDto(content);

        assertEquals(content, commentRequestDto.getContent());
    }
}