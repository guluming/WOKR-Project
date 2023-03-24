package com.slamdunk.WORK.service;

import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.UserKeyResult;
import com.slamdunk.WORK.repository.KeyResultRepository;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.repository.UserKeyResultRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserKeyResultService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final UserKeyResultRepository userKeyResultRepository;

    //회원-핵심결과 중간테이블 생성
    @Transactional
    public void registerUserKeyResult(KeyResult keyResult, Objective objective, UserDetailsImpl userDetails) {
        Optional<Objective> objectiveCheck = objectiveRepository.findById(objective.getId());
        Optional<KeyResult> keyResultCheck = keyResultRepository.findById(keyResult.getId());

        if (objectiveCheck.isPresent() && keyResultCheck.isPresent()) {
            UserKeyResult userKeyResult = new UserKeyResult(userDetails.getUser(), objectiveCheck.get(), keyResult, userDetails.getUser().getTeam());
            userKeyResultRepository.save(userKeyResult);
        }
    }

    //회원-핵심결과 중간테이블 전체 조회
    public List<Long> allKeyResult(UserDetailsImpl userDetails) {
        List<UserKeyResult> userKeyResultList = userKeyResultRepository.findAllByTeam(userDetails.getUser().getTeam());

        List<Long> keyResultId = new ArrayList<>();
        for (int i =0; i<userKeyResultList.size(); i++) {
            keyResultId.add(userKeyResultList.get(i).getKeyResult().getId());
        }

        return keyResultId;
    }

    //회원-핵심결과 중간테이블 상세 조회
    public boolean checkMyKeyResult(Long keyResultId, UserDetailsImpl userDetails) {
        Optional<UserKeyResult> checkDate
                = userKeyResultRepository.findByKeyResultIdAndUserId(keyResultId,userDetails.getUser().getId());
        return checkDate.isPresent();
    }

    //회원-핵심결과 목표Id로 중간테이블 전체 조회
    public List<KeyResult> checkKeyResultOfObjective(Long objectiveId, Long userId) {
        List<UserKeyResult> checkKeyResultOfObjectiveList = userKeyResultRepository.findAllByObjectiveIdAndUserId(objectiveId, userId);

        List<KeyResult> KeyResultOfObjectiveList = new ArrayList<>();
        if (!checkKeyResultOfObjectiveList.isEmpty()) {
            for (int i = 0; i < checkKeyResultOfObjectiveList.size(); i++) {
                KeyResultOfObjectiveList.add(checkKeyResultOfObjectiveList.get(i).getKeyResult());
            }
        }

        return KeyResultOfObjectiveList;
    }
}
