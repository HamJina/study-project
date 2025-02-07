package com.example.study_project.domain.posts.repository;

import com.example.study_project.domain.posts.entity.Post;
import com.example.study_project.domain.posts.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;


public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Slice<Post> findPostsByPage(Long lastPostId, Pageable pageable) {
        QPost post = QPost.post;

        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(lastPostId == null ? null : post.id.lt(lastPostId)) // lastPostId보다 작은 데이터 가져오기
                .orderBy(post.id.desc()) // 최신순 정렬
                .limit(pageable.getPageSize() + 1) // 다음 페이지 여부 확인을 위해 pageSize + 1개 조회
                .fetch();

        boolean hasNext = posts.size() > pageable.getPageSize();

        return new SliceImpl<>(hasNext ? posts.subList(0, pageable.getPageSize()) : posts, pageable, hasNext);
    }

    @Override
    public List<Post> findLatestRecruitmentPosts(int size) {
        QPost post = QPost.post;

        return queryFactory
                .selectFrom(post)
                .where(post.recruited.isTrue()) // 모집중인 글만 필터링
                .orderBy(post.createdDate.desc()) // 최신순 정렬
                .limit(size) // 요청한 개수만큼 제한
                .fetch();
    }

    @Override
    public List<Post> findHotRecruitmentPosts(int size) {
        QPost post = QPost.post;
        return queryFactory
                .selectFrom(post)
                .where(post.recruited.isTrue())
                .orderBy(post.hits.desc(), post.createdDate.desc())
                .limit(size)
                .fetch();
    }
}
