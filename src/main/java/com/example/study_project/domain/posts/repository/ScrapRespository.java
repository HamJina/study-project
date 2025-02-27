package com.example.study_project.domain.posts.repository;

import com.example.study_project.domain.posts.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRespository extends JpaRepository<Scrap, Long> {
    List<Scrap> findByUserId(Long userId);

    Scrap findByUserIdAndPostId(Long userId, Long postId);
}
