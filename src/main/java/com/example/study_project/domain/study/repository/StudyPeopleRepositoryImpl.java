package com.example.study_project.domain.study.repository;

import com.example.study_project.domain.study.dto.StudyListResponseDTO;
import com.example.study_project.domain.study.dto.StudyPeopleListResponseDTO;
import com.example.study_project.domain.study.entity.QStudy;
import com.example.study_project.domain.study.entity.QStudyPeople;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.example.study_project.domain.study.entity.QStudyPeople.studyPeople;
import static com.example.study_project.domain.user.entity.QUser.user;

public class StudyPeopleRepositoryImpl implements StudyPeopleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public StudyPeopleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<StudyListResponseDTO> findMyStudyList(Long userId) {
        QStudyPeople studyPeople = QStudyPeople.studyPeople;
        QStudy study = QStudy.study;

        return queryFactory.select(Projections.constructor(
                        StudyListResponseDTO.class,
                        study.post.title,
                        study.post.recruitedPeopleNum,
                        study.post.totalPeopleNum))
                .from(studyPeople)
                .join(studyPeople.study, study) // 단순 join으로 변경
                .where(studyPeople.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<StudyPeopleListResponseDTO> findStudyPeopleList(Long studyId) {
        return queryFactory.select(Projections.constructor(
                StudyPeopleListResponseDTO.class,
                user.name, user.phoneNumber, user.email))
                .from(studyPeople)
                .join(studyPeople.user, user)
                .where(studyPeople.study.id.eq(studyId))
                .fetch();
    }

}
