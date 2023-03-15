package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.dto.request.EmoticonRequest;
import com.slamdunk.WORK.dto.request.KeyResultRequest;
import com.slamdunk.WORK.dto.request.ProgressRequest;
import com.slamdunk.WORK.security.UserDetailsImpl;
import com.slamdunk.WORK.service.KeyResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    //핵심결과 전체 조회
    @GetMapping("api/keyresult")
    public ResponseEntity<?> allKeyResult(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return keyResultService.allKeyResult(userDetails);
    }

    //핵심결과 상세 조회
    @GetMapping("api/keyresult/detail/{keyresult_id}")
    public ResponseEntity<?> detailKeyResult(
            @PathVariable("keyresult_id") Long keyResultId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return keyResultService.detailKeyResult(keyResultId, userDetails);
    }

    //핵심결과 진척도 수정
    @PatchMapping("api/keyresult/progress/{keyresult_id}")
    public ResponseEntity<?> keyResultProgressEdit(
            @PathVariable("keyresult_id") Long keyResultId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ProgressRequest progressRequest) {
        return keyResultService.keyResultProgressEdit(keyResultId, userDetails, progressRequest);
    }

    //핵심결과 자신감 수정
    @PatchMapping("api/keyresult/emoticon/{keyresult_id}")
    public ResponseEntity<?> keyResultEmoticonEdit(
            @PathVariable("keyresult_id") Long keyResultId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody EmoticonRequest emoticonRequest) {
        return keyResultService.keyResultEmoticonEdit(keyResultId, userDetails, emoticonRequest);
    }
}
