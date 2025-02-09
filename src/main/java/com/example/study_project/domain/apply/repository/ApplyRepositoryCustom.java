package com.example.study_project.domain.apply.repository;

import com.example.study_project.domain.apply.entity.Apply;

import java.util.List;

public interface ApplyRepositoryCustom {

    List<Apply> findByPostIdAndWaiting(Long postId);
}
