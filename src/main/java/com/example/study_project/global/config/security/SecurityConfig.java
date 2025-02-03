package com.example.study_project.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //스프링부트에서 관리되기 위함
@EnableWebSecurity //시큐리티를 위한 config
public class SecurityConfig {

    //비밀번호를 해시로 암호화 시켜서 검증
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //사용자가 정의한 로그인 방식을 설정
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable(jwt)
        http
                .csrf((auth) -> auth.disable());
        //From 로그인 방식 disable(jwt)
        http
                .formLogin((auth) -> auth.disable());
        //http basic 인증 방식 disable(jwt)
        http
                .httpBasic((auth) -> auth.disable());
        //특정 경로에 대해 어떤 권한을 가져야 하는지 (인가작업)
        http
                .authorizeHttpRequests((auth) -> auth
                        //해당 경로에 대해서는 모든 권한 허용
                        .requestMatchers("/users/login", "/", "/users/join", "/users/idcheck").permitAll()
                        //다른 요청에 대해서는 로그인한 사용자만 접근할 수 있다.
                        .anyRequest().authenticated());
        //jwt방식에서는 세션을 stateless상태로 관리 (중요!!!!)
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
