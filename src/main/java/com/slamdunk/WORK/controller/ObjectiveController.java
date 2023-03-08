package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.dto.request.ObjectiveRequest;
import com.slamdunk.WORK.security.UserDetailsImpl;
import com.slamdunk.WORK.service.ObjectiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ObjectiveController {
    private final ObjectiveService objectiveService;

    //목표 생성
    @PostMapping("api/objective")
    public ResponseEntity<?> registerObjective(
            @RequestBody ObjectiveRequest objectiveRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return objectiveService.registerObjective(objectiveRequest, userDetails);
    }

    //목표 전체 조회
    @GetMapping("api/objective")
    public ResponseEntity<?> allObjective(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return objectiveService.allObjective(userDetails);
    }

    //목표 상세 조회
    @GetMapping("api/objective/detail/{objective_id}")
    public ResponseEntity<?> detailObjective(
            @PathVariable("objective_id") Long objectiveId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return objectiveService.detailObjective(objectiveId, userDetails);
    }
}
