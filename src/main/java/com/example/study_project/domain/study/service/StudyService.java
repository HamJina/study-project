package com.example.study_project.domain.study.service;

import com.example.study_project.domain.apply.dto.response.ApplyResponseDTO;
import com.example.study_project.domain.apply.entity.Apply;
import com.example.study_project.domain.apply.repository.ApplyRepository;
import com.example.study_project.domain.apply.service.ApplyService;
import com.example.study_project.domain.enums.ApplyStatus;
import com.example.study_project.domain.posts.entity.Post;
import com.example.study_project.domain.posts.repository.PostRepository;
import com.example.study_project.domain.study.entity.Study;
import com.example.study_project.domain.study.entity.StudyPeople;
import com.example.study_project.domain.study.repository.StudyPeopleRepository;
import com.example.study_project.domain.study.repository.StudyRepository;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.global.error.exception.CustomException;
import com.example.study_project.global.error.exception.ErrorCode;
import jakarta.transaction.Transactional;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final PostRepository postRepository;
    private final ApplyRepository applyRepository;
    private final StudyPeopleRepository studyPeopleRepository;

    public void createStudy(Long postId, User currentUser) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        //해당 모집글의 작성자만 모집글 마감 가능
        if (!currentUser.equals(findPost.getWriter())) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        int size = applyRepository.findByPostIdAndStatus(postId, ApplyStatus.JOINED).size();

        Study study = new Study();
        study.setPost(findPost);
        study.setScore(0);
        study.setTotalPeopleNum(size);

        Study existingStudy = studyRepository.findByPostId(postId);
        final Study findStudy = (existingStudy != null) ? existingStudy : studyRepository.save(study);


        //JOINED한 사람들을 토대로 StudyPeople생성하기
        List<Apply> joinedList = applyRepository.findByPostIdAndStatus(postId, ApplyStatus.JOINED);
        List<StudyPeople> studyPeopleList = joinedList.stream()
                .map(apply -> new StudyPeople(findStudy, apply.getUser()))
                .collect(Collectors.toList());

        studyPeopleList.forEach(studyPeopleRepository::save);

        findPost.setRecruited(false);
        postRepository.save(findPost);
    }


    public void studyMyList(User currentUser) {

    }
}
