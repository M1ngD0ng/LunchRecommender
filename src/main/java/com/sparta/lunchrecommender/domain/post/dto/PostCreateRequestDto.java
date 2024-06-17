package com.sparta.lunchrecommender.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public class PostCreateRequestDto {
    private String content;

    @JsonCreator
    public PostCreateRequestDto(String content) {
        this.content=content;
    }
}
