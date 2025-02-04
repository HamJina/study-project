package com.example.study_project.domain.user.service;

import com.example.study_project.domain.user.dto.request.JoinDTO;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.domain.user.repository.UserRepository;
import com.example.study_project.global.error.exception.CustomException;
import com.example.study_project.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    //비밀번호 암호화
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinUser(JoinDTO joinDTO) {

        //DTO에서 username과 password추출
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        //username이 이미 존재하는 이름인지 판단(아이디 중복확인)
        Boolean isExist = userRepository.existsByUsername(username);

        //이미 존재하는 이름이면 회원가입 진행x
        if(isExist) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_ID);
        }

        //존재하지 않는 이름이면 새로운 User엔티티 생성
        User user = new User();

        user.setName(joinDTO.getName());
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhoneNumber(joinDTO.getPhoneNumber());
        user.setEmail(joinDTO.getEmail());

        //user객체 저장
        userRepository.save(user);
    }

    public boolean ExistIDCheck(String username) {
        return userRepository.existsByUsername(username);
    }
}
