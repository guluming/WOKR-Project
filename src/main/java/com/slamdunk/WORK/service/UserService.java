package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.request.UserRequest;
import com.slamdunk.WORK.entity.User;
import com.slamdunk.WORK.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public ResponseEntity<?> userSignup(UserRequest userRequest) {
        Optional<User> userEmailCheck = userRepository.findByEmail(userRequest.getEmail());
        if (userEmailCheck.isPresent()) {
            return new ResponseEntity<>("중복된 이메일입니다.", HttpStatus.BAD_REQUEST);
        } else {
            User newUser = new User(userRequest);
            newUser.encryptPassword(passwordEncoder);
            userRepository.save(newUser);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
