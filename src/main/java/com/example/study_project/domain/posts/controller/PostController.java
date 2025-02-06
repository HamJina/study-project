package com.example.study_project.domain.posts.controller;

import com.example.study_project.domain.posts.dto.request.PostDTO;
import com.example.study_project.domain.posts.dto.response.PostResponseDTO;
import com.example.study_project.domain.posts.service.PostService;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    //모집글 작성
    @PostMapping("/create")
    public ResponseEntity createPost(@RequestBody PostDTO postDTO) {
        User currentUser = getCurrentUser();
        PostResponseDTO post = postService.createPost(postDTO, currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("post", post);

        return ResponseEntity.ok().body(responseData);
    }

    //모집글 상세조회
    @GetMapping("/{postId}")
    public ResponseEntity detailPost(@PathVariable long postId) {
        PostResponseDTO post = postService.detailPost(postId);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("post", post);

        return ResponseEntity.ok().body(responseData);
    }



    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserId(authentication.getName());
    }
}
