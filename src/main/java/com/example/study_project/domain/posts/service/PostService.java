package com.example.study_project.domain.posts.service;

import com.example.study_project.domain.keyword.service.KeywordService;
import com.example.study_project.domain.posts.dto.request.PostDTO;
import com.example.study_project.domain.posts.dto.response.PostResponseDTO;
import com.example.study_project.domain.posts.entity.Post;
import com.example.study_project.domain.posts.repository.PostRepository;
import com.example.study_project.domain.posts.repository.PostRepositoryCustom;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.global.error.exception.CustomException;
import com.example.study_project.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final KeywordService keywordService;

    public PostResponseDTO createPost(PostDTO postDTO, User currentUser) {
        Post post = new Post();

        //작성자 설정
        post.setWriter(currentUser);
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setTags(postDTO.getTags());
        post.setRecruited(true);
        post.setHits(0);
        post.setFiled(postDTO.getFiled());
        post.setRecruitedPeopleNum(0);
        post.setTotalPeopleNum(postDTO.getTotalPeopleNum());

        postRepository.save(post);

        return PostResponseDTO.createToDTO(post);
    }

    //@Cacheable(value = "postCache", key = "#postId", cacheManager = "boardCacheManager")
    public PostResponseDTO detailPost(long postId) {
        //id를 이용해서 모집글을 조회한다.
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_POST));
        //조회된 모집글은 조회수가 증가한다.
        findPost.increaseHits();
        //entity -> dto
        return PostResponseDTO.createToDTO(findPost);
    }

    public Slice<PostResponseDTO> getPostList(Long lastPostId, Pageable pageable) {
        Slice<Post> postSlice = postRepository.findPostsByPage(lastPostId, pageable);

        // Entity -> DTO 변환
        return postSlice.map(PostResponseDTO::createToDTO);
    }

    public List<PostResponseDTO> getNewPost(int size) {
        List<Post> posts = postRepository.findLatestRecruitmentPosts(size);
        // Entity -> DTO 변환
        return posts.stream()
                .map(PostResponseDTO::createToDTO)
                .collect(Collectors.toList());
    }

    public List<PostResponseDTO> getHotPost(int size) {
        List<Post> hotRecruitmentPosts = postRepository.findHotRecruitmentPosts(size);

        return hotRecruitmentPosts.stream()
                .map(PostResponseDTO::createToDTO)
                .collect(Collectors.toList());
    }

    public Slice<PostResponseDTO> getKeywordPosts(Long lastPostId, Pageable pageable, String keyword, User currentUser) {
        //검색 결과 list반환
        Slice<Post> keywordPosts = postRepository.findKeywordPostsByPage(lastPostId, pageable, keyword);

        //최신 검색어에 추가(첫 검색시)
        if(lastPostId == null) {
            keywordService.createLatestKeyword(currentUser, keyword);
        }

        return keywordPosts.map(PostResponseDTO::createToDTO);
    }
}
