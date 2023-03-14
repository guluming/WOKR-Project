package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.security.UserDetailsImpl;
import com.slamdunk.WORK.service.OKRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OKRController {
    private final OKRService okrService;

    //목표-핵심결과 전체 조회
    @GetMapping("api/okr")
    public ResponseEntity<?> allObjectiveKeyResult(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return okrService.allObjectiveKeyResult(userDetails);
    }
}
