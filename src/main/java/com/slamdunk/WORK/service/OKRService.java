package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.response.OKRResponse;
import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.UserKeyResult;
import com.slamdunk.WORK.entity.UserObjective;
import com.slamdunk.WORK.repository.KeyResultRepository;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.repository.UserKeyResultRepository;
import com.slamdunk.WORK.repository.UserObjectiveRepository;
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
    private final UserObjectiveRepository userObjectiveRepository;
    private final UserKeyResultRepository userKeyResultRepository;
    private final UserObjectiveService userObjectiveService;
    private final UserKeyResultService userKeyResultService;

    //목표-핵심결과 전체 조회
    public ResponseEntity<?> allObjectiveKeyResult(UserDetailsImpl userDetails) {
        List<UserObjective> checkCreateObjective = userObjectiveRepository.findAllByUserId(userDetails.getUser().getId());

        List<OKRResponse> okrResponseList = new ArrayList<>();
        if (checkCreateObjective.isEmpty()) {
            return new ResponseEntity<>(okrResponseList, HttpStatus.OK);
        } else {
            for (int i=0; i<checkCreateObjective.size(); i++) {
                List<OKRResponse.keyResult> okrKeyResultResponseList = new ArrayList<>();
                List<KeyResult> checkCreateKeyResult = keyResultRepository.findAllByObjectiveId(checkCreateObjective.get(i).getObjective().getId());
                if (!checkCreateKeyResult.isEmpty()) {
                    for (int k=0; k<checkCreateKeyResult.size(); k++) {
                        OKRResponse.keyResult okrKeyResultResponse = OKRResponse.keyResult.builder()
                                .myKeyResult(userKeyResultService.checkMyKeyResult(checkCreateKeyResult.get(k).getId(), userDetails))
                                .keyResultId(checkCreateKeyResult.get(k).getId())
                                .keyResult(checkCreateKeyResult.get(k).getKeyResult())
                                .progress(checkCreateKeyResult.get(k).getProgress())
                                .emotion(checkCreateKeyResult.get(k).getEmoticon())
                                .build();

                        okrKeyResultResponseList.add(okrKeyResultResponse);
                    }
                }

                Optional<Objective> checkObjective = objectiveRepository.findById(checkCreateObjective.get(i).getObjective().getId());
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
