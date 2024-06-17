package com.sparta.lunchrecommender.domain.post.service;

import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostResponseDto;
import com.sparta.lunchrecommender.domain.post.dto.PostUpdateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.user.entity.User;
import com.sparta.lunchrecommender.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Page<PostResponseDto> getPosts(int page, String sortBy, LocalDateTime startDate, LocalDateTime endDate) {
        int size = 10; // 페이지수는 10개로 고정
        Sort.Direction direction = Sort.Direction.DESC; // 항상 desc로 조회
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> postList;

        if(startDate != null && endDate != null){
            postList = postRepository.findAllByCreatedAtBetween(startDate, endDate, pageable);
        } else {
            postList = postRepository.findAll(pageable);
        }
        return postList.map(post -> new PostResponseDto(post, post.getUser()));
    }

    public PostResponseDto createPost(PostCreateRequestDto requestDto, User user) {

        Post post = new Post(requestDto);

        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost, user);
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User user) {

        Post post = findPostById(postId);

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("현재 게시물 작성자만 수정이 가능합니다.");
        }

        post.update(requestDto);

        return new PostResponseDto(post, user);
    }

    public void deletePost(Long postId, User user) {

        Post post = findPostById(postId);

        if (!post.getUser().getUserId().equals(user.getUserId())) {

            throw new IllegalArgumentException("현재 게시물 작성자만 삭제가 가능합니다.");
        }

        postRepository.delete(post);

    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물이 존재하지 않습니다."));
    }
}
