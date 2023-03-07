package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.dto.request.UserRequest;
import com.slamdunk.WORK.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("api/user/signup")
    public ResponseEntity<?> userSignup(@RequestBody UserRequest userRequest) {
        return userService.userSignup(userRequest);
    }

//    //이메일 중복 확인
//    @PostMapping("api/user/email")
//    public ResponseEntity<?> duplicateCheckEmail(@RequestBody UserRequest userRequest) {
//        return userService.duplicateCheckEmail(userRequest);
//    }
//
//    //로그인
//    @PostMapping("api/user/login")
//    public ResponseEntity<?>
}
