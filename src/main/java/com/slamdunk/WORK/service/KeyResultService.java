package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.request.KeyResultRequest;
import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.repository.KeyResultRepository;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeyResultService {
    private final KeyResultRepository keyResultRepository;
    private final ObjectiveRepository objectiveRepository;
    private final UserKeyResultService userKeyResultService;

    //핵심결과 생성
    @Transactional
    public ResponseEntity<?> registerKeyResult(Long objectiveId, KeyResultRequest keyResultRequest, UserDetailsImpl userDetails) {
        Optional<Objective> objectiveCheck = objectiveRepository.findById(objectiveId);
        if (objectiveCheck.isEmpty()) {
            return new ResponseEntity<>("목표가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        } else {
            for (int i = 0; i< keyResultRequest.getKeyResultDate().size(); i++) {
                KeyResult newKeyResult = new KeyResult(
                        objectiveCheck.get(),
                        keyResultRequest.getKeyResultDate().get(i));
                keyResultRepository.save(newKeyResult);

                userKeyResultService.registerUserKeyResult(newKeyResult, objectiveCheck.get(), userDetails);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
