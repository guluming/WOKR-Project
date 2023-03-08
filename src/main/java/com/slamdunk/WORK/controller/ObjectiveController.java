package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.dto.request.ObjectiveRequest;
import com.slamdunk.WORK.security.UserDetailsImpl;
import com.slamdunk.WORK.service.ObjectiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
