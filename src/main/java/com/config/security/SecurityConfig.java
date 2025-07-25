package com.config;

import com.domain.auth.filter.JwtAuthenticationFilter;
import com.domain.auth.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService; // 커스텀 서비스 주입 (로그인 시)

    /**
     * 보안 필터 체인 설정
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // JWT 인증 필터 생성 (생성자에 JwtUtil과 ObjectMapper 주입)
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, objectMapper);

        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // CORS 기본 설정 활성화
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 기반 인증 → 세션 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // 로그인, 회원가입 등 공개 API
                        .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll() // 공개 조회 API 예시
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                )
                .authenticationProvider(authenticationProvider()) // 사용자 인증 제공자 등록
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 인증 필터 등록
                .build();
    }

    /**
     * 비밀번호 암호화 방식
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 인증에 사용될 사용자 정보 로드 및 패스워드 체크 제공자
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // 유저 로딩 서비스
        provider.setPasswordEncoder(passwordEncoder());     // 비밀번호 비교용 인코더
        return provider;
    }

    /**
     * 로그인 시 AuthenticationManager가 사용됨 (스프링 내부 제공)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
