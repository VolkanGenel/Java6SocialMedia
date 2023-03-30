package com.volkan.controller;

import static com.volkan.constants.ApiUrls.*;

import com.volkan.dto.request.CreateNewPostRequestDto;
import com.volkan.repository.entity.Post;
import com.volkan.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(POST)
public class PostController {
    private final PostService postService;
    @PostMapping(CREATE)
    public ResponseEntity<Post> createPost(@RequestBody CreateNewPostRequestDto dto) {
        return ResponseEntity.ok(postService.createPost(dto));
    }
    @GetMapping(FINDALL)
    public ResponseEntity<List<Post>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }
}
