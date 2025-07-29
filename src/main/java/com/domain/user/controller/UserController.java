package com.domain.user.controller;

import com.domain.user.dto.SignupRequestDto;
import com.domain.user.dto.UpdateUserRequestDto;
import com.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<String> update(@PathVariable String username, @RequestBody UpdateUserRequestDto updateUserRequestDto){
        userService.updateUser(username, updateUserRequestDto);
        return ResponseEntity.ok("유저정보 수정완료");
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> delete(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok("유저 삭제 완료");
    }
}
