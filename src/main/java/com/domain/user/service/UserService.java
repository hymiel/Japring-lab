package com.domain.user.service;

import com.domain.user.dto.SignupRequestDto;
import com.domain.user.dto.UpdateUserRequestDto;
import com.domain.user.entity.User;
import com.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.domain.user.enums.Role;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_TOKEN = "";

    public void signup(SignupRequestDto signupRequestDto){
        if (userRepository.findByUsername(signupRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 이름입니다.");
        } else if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }

        Role role = signupRequestDto.getRole();
        if (role == Role.ADMIN && !ADMIN_TOKEN.equals(signupRequestDto.getAdminToken())) {
            throw new IllegalArgumentException("관리자 암호가 틀렸습니다.");
        }

        User user = User.builder()
                .username(signupRequestDto.getUsername())
                .password(signupRequestDto.getPassword())
                .email(signupRequestDto.getEmail())
                .role(role)
                .build();
        userRepository.save(user);
    }

    public void updateUser(String username, UpdateUserRequestDto updateUserRequestDto){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new IllegalArgumentException("사용자가 존재하지 않습니다."));
        if (updateUserRequestDto.getEmail() != null) {
            user.updateEmail(updateUserRequestDto.getEmail());
        }
        if (updateUserRequestDto.getPassword() != null) {
            user.updatePassword(passwordEncoder.encode(updateUserRequestDto.getPassword()));
        }
        userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
