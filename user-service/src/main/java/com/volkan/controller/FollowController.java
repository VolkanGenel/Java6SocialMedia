package com.volkan.controller;


import static com.volkan.constants.ApiUrls.*;

import com.volkan.dto.request.CreateFollowRequestDto;
import com.volkan.repository.entity.Follow;
import com.volkan.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(FOLLOW)
public class FollowController {
    private final FollowService followService;
    @PostMapping(CREATE)
    public ResponseEntity<?> creatFollow(@RequestBody CreateFollowRequestDto dto) {
        return ResponseEntity.ok(followService.createFollow(dto));
    }
    @GetMapping(FINDALL)
    public ResponseEntity<List<Follow>> findAll() {
        return ResponseEntity.ok(followService.findAll());
    }
}
