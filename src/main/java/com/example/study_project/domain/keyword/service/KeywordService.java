package com.example.study_project.domain.keyword.service;

import com.example.study_project.domain.keyword.dto.KeywordDTO;
import com.example.study_project.domain.keyword.entity.Keyword;
import com.example.study_project.domain.keyword.repository.KeywordRepository;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.global.error.exception.CustomException;
import com.example.study_project.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<KeywordDTO> getLatestKeyword(User currentUser) {
        List<Keyword> latestKeyword = keywordRepository.findByUserIdOrderBySearchedDateDesc(currentUser.getId());

        //검색어가 존재하지 않을때 예외 처리
        if(latestKeyword.isEmpty()) {
            throw new CustomException(ErrorCode.KEYWORD_IS_EMPTY);
        }

         //entity -> DTO
        return latestKeyword.stream()
                .map((keyword -> new KeywordDTO(keyword.getId(), keyword.getContent(), keyword.getSearchedDate())))
                .collect(Collectors.toList());
    }
}
