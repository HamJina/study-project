package com.example.study_project.domain.posts.repository;

import com.example.study_project.domain.posts.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostRepositoryCustom {

    Slice<Post> findPostsByPage(Long lastPostId, Pageable pageable);
}
