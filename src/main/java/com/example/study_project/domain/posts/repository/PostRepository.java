package com.example.study_project.domain.posts.repository;


import com.example.study_project.domain.posts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findByWriterIdAndRecruitedTrue(Long userId);

    @Query("SELECT COUNT(p) FROM Post p")
    long countAllPosts();
}
