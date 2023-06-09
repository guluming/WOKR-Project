package com.slamdunk.WORK.service;

import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.UserObjective;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.repository.UserObjectiveRepository;
import com.slamdunk.WORK.repository.UserRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserObjectiveService {
    private final UserRepository userRepository;
    private final ObjectiveRepository objectiveRepository;
    private final UserObjectiveRepository userObjectiveRepository;

    //회원-목표 중간테이블 생성
    @Transactional
    public void registerUserObjective(Objective objective, UserDetailsImpl userDetails) {
        Optional<Objective> objectiveCheck = objectiveRepository.findById(objective.getId());

        if (objectiveCheck.isPresent()) {
            UserObjective userObjective = new UserObjective(userDetails.getUser(), objectiveCheck.get(), userDetails.getUser().getTeam());
            userObjectiveRepository.save(userObjective);
        }
    }

    //회원-목표 전체 조회
    public List<Long> allObjective(UserDetailsImpl userDetails) {
        List<UserObjective> userObjectiveList = userObjectiveRepository.findAllByTeam(userDetails.getUser().getTeam());

        List<Long> objectiveIdList = new ArrayList<>();
        for (int i = 0; i < userObjectiveList.size(); i++) {
            objectiveIdList.add(userObjectiveList.get(i).getObjective().getId());
        }

        return objectiveIdList;
    }

    //회원-목표 상세 조회
    public boolean checkMyObjective(Long objectiveId, UserDetailsImpl userDetails) {
        Optional<UserObjective> checkDate = userObjectiveRepository.findByObjectiveIdAndUserId(objectiveId, userDetails.getUser().getId());
        return checkDate.isPresent();
    }
}
