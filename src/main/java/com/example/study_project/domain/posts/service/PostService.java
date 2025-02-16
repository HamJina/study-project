package com.example.study_project.domain.posts.service;

import com.example.study_project.domain.apply.entity.Apply;
import com.example.study_project.domain.apply.repository.ApplyRepository;
import com.example.study_project.domain.enums.PostStatus;
import com.example.study_project.domain.keyword.service.KeywordService;
import com.example.study_project.domain.posts.dto.request.PostDTO;
import com.example.study_project.domain.posts.dto.response.PostResponseDTO;
import com.example.study_project.domain.posts.entity.Post;
import com.example.study_project.domain.posts.entity.Scrap;
import com.example.study_project.domain.posts.repository.PostRepository;
import com.example.study_project.domain.posts.repository.PostRepositoryCustom;
import com.example.study_project.domain.posts.repository.ScrapRespository;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final KeywordService keywordService;
    private final ApplyRepository applyRepository;
    private final ScrapRespository scrapRespository;

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

    @Cacheable(value = "getDetailPost", key = "'detailPost:postId:' + #postId", cacheManager = "cacheManager")
    public PostResponseDTO detailPost(long postId) {
        //id를 이용해서 모집글을 조회한다.
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_POST));
        //조회된 모집글은 조회수가 증가한다.
        findPost.increaseHits();
        //entity -> dto
        PostResponseDTO responseDTO = PostResponseDTO.createToDTO(findPost);
        return responseDTO;
    }

    @Cacheable(cacheNames = "getPostList", key = "'postList:lastPostId:' + #lastPostId", cacheManager = "cacheManager")
    public List<PostResponseDTO> getPostList(Long lastPostId, Pageable pageable) {
        Slice<Post> postSlice = postRepository.findPostsByPage(lastPostId, pageable);

        // Entity -> DTO 변환
        List<PostResponseDTO> postResponseDTOList = postSlice.stream()
                .map(PostResponseDTO::createToDTO)
                .collect(Collectors.toList());

        return postResponseDTOList;
    }


    @Cacheable(cacheNames = "getNewPosts", key = "'newPosts:size:' + #size", cacheManager = "cacheManager")
    public List<PostResponseDTO> getNewPost(int size) {
        List<Post> posts = postRepository.findLatestRecruitmentPosts(size);
        // Entity -> DTO 변환
        return posts.stream()
                .map(PostResponseDTO::createToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "getHotPosts", key = "'hotPosts:size:' + #size", cacheManager = "cacheManager")
    public List<PostResponseDTO> getHotPost(int size) {
        List<Post> hotRecruitmentPosts = postRepository.findHotRecruitmentPosts(size);

        return hotRecruitmentPosts.stream()
                .map(PostResponseDTO::createToDTO)
                .collect(Collectors.toList());
    }


    public Slice<PostResponseDTO> getKeywordPosts(Long lastPostId, Pageable pageable, String keyword, User currentUser) {
        //검색 결과 list반환
        Slice<Post> keywordPosts = postRepository.findKeywordPostsByPage(lastPostId, pageable, keyword);

        if(keywordPosts.isEmpty()) {
            throw new CustomException(ErrorCode.SEARCHEDPOST_IS_NOT_EXIST);
        }

        //최신 검색어에 추가(첫 검색시)
        if(lastPostId == null) {
            keywordService.createLatestKeyword(currentUser, keyword);
        }

        return keywordPosts.map(PostResponseDTO::createToDTO);
    }

    public PostResponseDTO updatePost(PostDTO postDTO, User currentUser, Long postId) {
        //수정할 모집글 정보 불러오기
        Post findPost = postRepository.findById(postId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_EXIST_POST);
        });

        //모집글을 수정하려는 사용자가 모집글 작성자인지 확인
        if(currentUser != findPost.getWriter()) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        //수정 권한이 존재한다면
        findPost.setTitle(postDTO.getTitle());
        findPost.setContent(postDTO.getContent());
        findPost.setTags(postDTO.getTags());
        findPost.setFiled(postDTO.getFiled());
        findPost.setTotalPeopleNum(postDTO.getTotalPeopleNum());

        return PostResponseDTO.createToDTO(findPost);

    }

    public void deletePost(Long postId, User currentUser) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_EXIST_POST);
        });

        //모집글을 삭제하려는 사용자가 모집글 작성자인지 확인
        if(currentUser != findPost.getWriter()) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        postRepository.delete(findPost);
    }

    public PostStatus checkStatus(Long postId, User currentUser) {
        //작성자인지 확인
        Post findPost = postRepository.findById(postId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_EXIST_POST);
        });

        if(findPost.getWriter() == currentUser) {
            //작성자
            return PostStatus.WRITER;
        } else{
            Apply findApply = applyRepository.findByPostIdAndUserId(postId, currentUser.getId());
            if(findApply == null) {
                return PostStatus.BEFORE_APPLY;
            } else {
                return PostStatus.AFTER_APPLY;
            }
        }
    }

    public void scrapPost(Long postId, User currentUser) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_EXIST_POST);
        });

        Scrap scrap = new Scrap();

        scrap.setUser(currentUser);
        scrap.setPost(findPost);

        scrapRespository.save(scrap);
    }

    public List<PostResponseDTO> scrapPostList(User currentUser) {
        List<Scrap> findScrapList = scrapRespository.findByUserId(currentUser.getId());
        if(findScrapList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_EXIST_SCRAP);
        }

        return findScrapList.stream()
                .map(scrap -> PostResponseDTO.createToDTO(scrap.getPost()))
                .collect(Collectors.toList());
    }

    public List<PostResponseDTO> recruitingPost(User currentUser) {
        List<Post> recruitingPosts = postRepository.findByWriterIdAndRecruitedTrue(currentUser.getId());

        if (recruitingPosts.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_EXIST_RECRUITING_POST);
        }

        return recruitingPosts.stream()
                .map(PostResponseDTO::createToDTO)
                .collect(Collectors.toList());
    }
}
