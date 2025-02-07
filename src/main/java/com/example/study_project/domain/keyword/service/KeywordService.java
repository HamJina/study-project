package com.example.study_project.domain.keyword.service;

import com.example.study_project.domain.keyword.entity.Keyword;
import com.example.study_project.domain.keyword.repository.KeywordRepository;
import com.example.study_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public void createLatestKeyword(User user, String content) {
        Keyword keyword = new Keyword();
        keyword.setUser(user);
        keyword.setContent(content);
        keyword.setSearchedDate(LocalDateTime.now());

        keywordRepository.save(keyword);
    }
}
