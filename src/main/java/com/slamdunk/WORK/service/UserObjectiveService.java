package com.slamdunk.WORK.service;

import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.UserObjective;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.repository.UserObjectiveRepository;
import com.slamdunk.WORK.repository.UserRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Optional<Objective> objectiveCheck = objectiveRepository.findByObjective(objective.getObjective());

        if (objectiveCheck.isPresent()) {
            UserObjective userObjective = new UserObjective(userDetails.getUser(), objectiveCheck.get());
            userObjectiveRepository.save(userObjective);
        }
    }
}
