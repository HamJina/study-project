package com.example.study_project.domain.posts.repository;

import com.example.study_project.domain.posts.entity.Post;
import com.example.study_project.domain.posts.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.example.study_project.domain.posts.entity.QPost.post;


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
}
