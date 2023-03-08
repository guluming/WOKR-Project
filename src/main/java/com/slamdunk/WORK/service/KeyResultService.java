package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.request.KeyResultRequest;
import com.slamdunk.WORK.repository.KeyResultRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class KeyResultService {
    private final KeyResultRepository keyResultRepository;

    @Transactional
    public ResponseEntity<?> registerKeyResult(KeyResultRequest keyResultRequest, UserDetailsImpl userDetails) {

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
