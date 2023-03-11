package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.dto.request.KeyResultRequest;
import com.slamdunk.WORK.security.UserDetailsImpl;
import com.slamdunk.WORK.service.KeyResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KeyResultController {
    private final KeyResultService keyResultService;

    //핵심결과 생성
    @PostMapping("api/{objective_id}/keyresult")
    public ResponseEntity<?> registerKeyResult(
            @PathVariable("objective_id") Long objectiveId,
            @RequestBody KeyResultRequest keyResultRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return keyResultService.registerKeyResult(objectiveId, keyResultRequest, userDetails);
    }
}
