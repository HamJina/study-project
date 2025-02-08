package com.example.study_project.domain.apply.service;

import com.example.study_project.domain.apply.dto.request.ApplyDTO;
import com.example.study_project.domain.apply.dto.response.ApplyResponseDTO;
import com.example.study_project.domain.apply.entity.Apply;
import com.example.study_project.domain.apply.repository.ApplyRepository;
import com.example.study_project.domain.enums.ApplyStatus;
import com.example.study_project.domain.posts.entity.Post;
import com.example.study_project.domain.posts.repository.PostRepository;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.global.error.exception.CustomException;
import com.example.study_project.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final PostRepository postRepository;

    public ApplyResponseDTO applyPost(ApplyDTO applyDTO, User currentUser, Long postId) {
        Apply apply = new Apply();
        Post findPost = postRepository.findById(postId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_EXIST_POST);
        });

        if(findPost.getWriter() == currentUser) {
            throw new CustomException(ErrorCode.NOT_ELIGIBLE);
        }

        Apply findApply = applyRepository.findByPostIdAndUserId(postId, currentUser.getId());
        if(findApply != null) {
            throw new CustomException(ErrorCode.ALREADY_APPLY);
        }

        apply.setPost(findPost);
        apply.setUser(currentUser);
        apply.setMessage(applyDTO.getMessage());
        apply.setStatus(ApplyStatus.WAITING);

        applyRepository.save(apply);

        return ApplyResponseDTO.createToDTO(apply);
    }

    public List<ApplyResponseDTO> myApplyList(User currentUser) {
        List<Apply> applyList = applyRepository.findByUserId(currentUser.getId());

        return applyList.stream()
                .map(ApplyResponseDTO::createToDTO)
                .collect(Collectors.toList());
    }
}
