package com.example.study_project.domain.user.service;

import com.example.study_project.domain.user.dto.CustomUserDetails;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //DB에서 조회
        User userData = userRepository.findByUsername(username);

        if(userData != null) {
            //존재하는 회원이면 UserDetails에 담아서 return하면 AuthenticationManager가 검증함
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
