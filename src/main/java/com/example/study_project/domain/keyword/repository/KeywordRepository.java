package com.example.study_project.domain.keyword.repository;

import com.example.study_project.domain.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findByUserIdOrderBySearchedDateDesc(Long userId);
}
