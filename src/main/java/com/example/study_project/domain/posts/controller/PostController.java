package com.example.study_project.domain.posts.controller;

import com.example.study_project.domain.enums.PostStatus;
import com.example.study_project.domain.posts.dto.request.PostDTO;
import com.example.study_project.domain.posts.dto.response.PostResponseDTO;
import com.example.study_project.domain.posts.service.PostService;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    //모집글 수정
    @PutMapping("/{postId}")
    public ResponseEntity updatePost(@RequestBody PostDTO postDTO, @PathVariable Long postId) {
        User currentUser = getCurrentUser();
        PostResponseDTO postResponse = postService.updatePost(postDTO, currentUser, postId);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("post", postResponse);

        return ResponseEntity.ok().body(responseData);

    }

    //모집글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId) {
        User currentUser = getCurrentUser();
        postService.deletePost(postId, currentUser);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "모집글이 삭제되었습니다.");

        return ResponseEntity.ok().body(responseData);
    }

    //모집글 상세조회
    @GetMapping("/{postId}")
    public ResponseEntity detailPost(@PathVariable long postId) {
        User currentUser = getCurrentUser();
        PostResponseDTO post = postService.detailPost(postId);

        //자신이 해당 모집글의 지원자인지 작성자인지 확인하는 코드
        PostStatus postStatus = checkStatus(postId, currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("post", post);
        responseData.put("postStatus", postStatus);

        return ResponseEntity.ok().body(responseData);
    }

    //모집글 전체 목록 조회(페이징 구현)
    @GetMapping("/list")
    public ResponseEntity getPostList( @RequestParam(required = false) Long lastPostId,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize);
        Slice<PostResponseDTO> postList = postService.getPostList(lastPostId, pageable);

        return ResponseEntity.ok().body(postList);
    }

    //New 모집글 조회(최신 5개만)
    @GetMapping("/new")
    public ResponseEntity getNewPost(@RequestParam(defaultValue = "5") int size) {
        List<PostResponseDTO> latestPosts = postService.getNewPost(size);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("latestPosts", latestPosts);

        return ResponseEntity.ok().body(responseData);
    }

    //Hot 모집글 목록 조회
    @GetMapping("/hot")
    public ResponseEntity getHotPost(@RequestParam(defaultValue = "5") int size) {
        List<PostResponseDTO> hotPosts = postService.getHotPost(size);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("hotPosts", hotPosts);

        return ResponseEntity.ok().body(responseData);
    }

    //모집글 검색(제목을 기준으로 검색)
    @GetMapping("/search/keyword")
    public ResponseEntity getKeywordPosts(@RequestParam(required = false) Long lastPostId,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(defaultValue = "") String keyword) {
        Pageable pageable = PageRequest.of(0, pageSize);
        User currentUser = getCurrentUser();
        Slice<PostResponseDTO> keywordPosts = postService.getKeywordPosts(lastPostId, pageable, keyword, currentUser);

        return ResponseEntity.ok().body(keywordPosts);
    }

    //모집글 스크랩 설정
    @PostMapping("/scrap/{postId}")
    public ResponseEntity scrapPost(@PathVariable Long postId) {
        User currentUser = getCurrentUser();
        postService.scrapPost(postId, currentUser);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "스크랩이 설정되었습니다.");

        return ResponseEntity.ok().body(responseData);
    }

    //스크랩 모집글 목록 조회
    @GetMapping("/scrap/list")
    public ResponseEntity scrapPostList() {
        User currentUser = getCurrentUser();
        List<PostResponseDTO> scrapList = postService.scrapPostList(currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("scrapList", scrapList);

        return ResponseEntity.ok().body(responseData);

    }

    //내가 모집중인 모집글 목록(recruited = true, writer = me)
    @GetMapping("/recruiting")
    public ResponseEntity recruitingPost() {
        User currentUser = getCurrentUser();
        List<PostResponseDTO> postResponse = postService.recruitingPost(currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("postResponse", postResponse);

        return ResponseEntity.ok().body(responseData);
    }

    private PostStatus checkStatus(Long postId, User currentUser) {
        return postService.checkStatus(postId, currentUser);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserId(authentication.getName());
    }
}
