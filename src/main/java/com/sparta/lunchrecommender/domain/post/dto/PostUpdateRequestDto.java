package com.sparta.lunchrecommender.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private String content;

    @JsonCreator
    public PostUpdateRequestDto(String content) {
        this.content=content;
    }
}
