package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.response.OKRResponse;
import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.UserKeyResult;
import com.slamdunk.WORK.repository.KeyResultRepository;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.repository.UserKeyResultRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OKRService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final UserKeyResultRepository userKeyResultRepository;
    private final UserObjectiveService userObjectiveService;
    private final UserKeyResultService userKeyResultService;

    //목표-핵심결과 전체 조회
    public ResponseEntity<?> allObjectiveKeyResult(UserDetailsImpl userDetails) {
        List<UserKeyResult> allOKRList = userKeyResultRepository.findAllByUserId(userDetails.getUser().getId());

        List<OKRResponse> okrResponseList = new ArrayList<>();
        if (allOKRList.isEmpty()) {
            return new ResponseEntity<>(okrResponseList, HttpStatus.OK);
        } else {
            List<Long> useObjectiveList = new ArrayList<>();
            for (int i=0; i<allOKRList.size(); i++) {
                useObjectiveList.add(allOKRList.get(i).getObjective().getId());
            }
            useObjectiveList = useObjectiveList.stream().distinct().collect(Collectors.toList());

            for (int i=0; i<useObjectiveList.size(); i++) {
                List<UserKeyResult> OKRList = userKeyResultRepository.findAllByObjectiveId(useObjectiveList.get(i));

                List<OKRResponse.keyResult> okrKeyResultResponseList = new ArrayList<>();
                for (int k=0; k<OKRList.size(); k++) {
                    Optional<KeyResult> checkKeyResult = keyResultRepository.findById(OKRList.get(k).getKeyResult().getId());
                    if (checkKeyResult.isPresent()) {
                        OKRResponse.keyResult okrKeyResultResponse = OKRResponse.keyResult.builder()
                                .myKeyResult(userKeyResultService.checkMyKeyResult(checkKeyResult.get().getId(), userDetails))
                                .keyResultId(checkKeyResult.get().getId())
                                .keyResult(checkKeyResult.get().getKeyResult())
                                .progress(checkKeyResult.get().getProgress())
                                .emotion(checkKeyResult.get().getEmoticon())
                                .build();

                        okrKeyResultResponseList.add(okrKeyResultResponse);
                    }
                }

                Optional<Objective> checkObjective = objectiveRepository.findById(useObjectiveList.get(i));
                if (checkObjective.isPresent()) {
                    OKRResponse okrResponse = OKRResponse.builder()
                            .myObjective(userObjectiveService.checkMyObjective(checkObjective.get().getId(), userDetails))
                            .objectiveId(checkObjective.get().getId())
                            .objective(checkObjective.get().getObjective())
                            .startdate(checkObjective.get().getStartDate())
                            .enddate(checkObjective.get().getEndDate())
                            .color(checkObjective.get().getColor())
                            .progress(checkObjective.get().getProgress())
                            .keyresult(okrKeyResultResponseList)
                            .build();

                    okrResponseList.add(okrResponse);
                } else {
                    return new ResponseEntity<>("생성한 목표가 없습니다.", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(okrResponseList, HttpStatus.OK);
        }
    }
}
