package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.request.ObjectiveRequest;
import com.slamdunk.WORK.dto.request.ProgressRequest;
import com.slamdunk.WORK.dto.response.ObjectiveDetailResponse;
import com.slamdunk.WORK.dto.response.ObjectiveResponse;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.repository.ObjectiveRepository;
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
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final UserObjectiveService userObjectiveService;

    //목표 생성
    @Transactional
    public ResponseEntity<?> registerObjective(ObjectiveRequest objectiveRequest, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            Optional<Objective> objectiveCheck = objectiveRepository.findByObjective(objectiveRequest.getObjective());
            if (objectiveCheck.isPresent()) {
                return new ResponseEntity<>("오브젝트 이름이 중복됩니다.", HttpStatus.BAD_REQUEST);
            } else {
                Objective newObjective = new Objective(objectiveRequest);
                objectiveRepository.save(newObjective);

                userObjectiveService.registerUserObjective(newObjective, userDetails);

                ObjectiveResponse objectiveResponse = new ObjectiveResponse(
                        newObjective.getId(),
                        newObjective.getObjective(),
                        newObjective.getStartDate(),
                        newObjective.getEndDate(),
                        newObjective.getColor(),
                        newObjective.getProgress()
                );

                return new ResponseEntity<>(objectiveResponse, HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>("팀장만 생성 가능합니다." ,HttpStatus.FORBIDDEN);
        }
    }

    //목표 전체 조회
    public ResponseEntity<?> allObjective(UserDetailsImpl userDetails) {
        List<Long> objectiveId = userObjectiveService.allObjective(userDetails);

        List<ObjectiveResponse> objectiveResponseList = new ArrayList<>();
        for (int i=0; i<objectiveId.size(); i++) {
            Optional<Objective> objective = objectiveRepository.findById(objectiveId.get(i));
            if (objective.isPresent()) {
                ObjectiveResponse objectiveResponse = new ObjectiveResponse(
                        objective.get().getId(),
                        objective.get().getObjective(),
                        objective.get().getStartDate(),
                        objective.get().getEndDate(),
                        objective.get().getColor(),
                        objective.get().getProgress()
                );
                objectiveResponseList.add(objectiveResponse);
            }
        }

        return new ResponseEntity<>(objectiveResponseList, HttpStatus.OK);
    }

    //목표 상세 조회
    public ResponseEntity<?> detailObjective(Long objectiveId, UserDetailsImpl userDetails) {
        Optional<Objective> objective = objectiveRepository.findById(objectiveId);

        if (objective.isPresent()) {
            ObjectiveDetailResponse objectiveDetailResponse = new ObjectiveDetailResponse(
                    userObjectiveService.checkMyObjective(objectiveId, userDetails),
                    objective.get().getId(),
                    objective.get().getObjective(),
                    objective.get().getStartDate(),
                    objective.get().getEndDate(),
                    objective.get().getColor()
            );

            return new ResponseEntity<>(objectiveDetailResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("존재하지 않는 목표입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //목표 진척도 수정
    public ResponseEntity<?> objectiveProgressEdit(Long objectiveId, UserDetailsImpl userDetails, ProgressRequest progressRequest) {
        Optional<Objective> objective = objectiveRepository.findById(objectiveId);

        if (objective.isPresent()) {
            if (userObjectiveService.checkMyObjective(objectiveId, userDetails)) {
                objective.get().objectiveProgressUpdate(progressRequest.getProgress());
                objectiveRepository.save(objective.get());

                return new ResponseEntity<>("진척도를 수정 했습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("수정할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("존재하지 않는 목표입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
