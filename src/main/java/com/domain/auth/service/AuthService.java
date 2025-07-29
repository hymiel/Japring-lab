package com.domain.auth.service;

import com.domain.auth.dto.LoginRequestDto;
import com.domain.user.entity.User;
import com.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void logout(String token, HttpServletResponse response) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 서버측 로그아웃: 블랙리스트 처리 혹은 토큰 무효화 구현 (선택사항)

        response.setHeader("Authorization", "");
        response.setHeader("Set-Cookie", "Authorization=; Max-Age=0; Path=/; HttpOnly");
    }
}
