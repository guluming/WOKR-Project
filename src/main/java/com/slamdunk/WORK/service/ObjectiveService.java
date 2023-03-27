package com.slamdunk.WORK.service;

import com.slamdunk.WORK.Editor.KeyResultEditor;
import com.slamdunk.WORK.Editor.ObjectiveEditor;
import com.slamdunk.WORK.Editor.ToDoEditor;
import com.slamdunk.WORK.dto.request.ObjectiveEditRequest;
import com.slamdunk.WORK.dto.request.ObjectiveRequest;
import com.slamdunk.WORK.dto.request.ProgressRequest;
import com.slamdunk.WORK.dto.response.ObjectiveDetailResponse;
import com.slamdunk.WORK.dto.response.ObjectiveResponse;
import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.ToDo;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final UserObjectiveService userObjectiveService;
    private final UserKeyResultService userKeyResultService;
    private final UserToDoService userToDoService;

    //목표 생성
    @Transactional
    public ResponseEntity<?> registerObjective(ObjectiveRequest objectiveRequest, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            List<Long> objectiveIdList = userObjectiveService.allObjective(userDetails);
            if (objectiveIdList.size() < 4) {
                Objective newObjective = new Objective(objectiveRequest);
                objectiveRepository.save(newObjective);

                userObjectiveService.registerUserObjective(newObjective, userDetails);

                ObjectiveResponse objectiveResponse = ObjectiveResponse.builder()
                        .myObjective(userObjectiveService.checkMyObjective(newObjective.getId(), userDetails))
                        .objectiveId(newObjective.getId())
                        .objective(newObjective.getObjective())
                        .startdate(newObjective.getStartDate())
                        .enddate(newObjective.getEndDate())
                        .color(newObjective.getColor())
                        .progress(newObjective.getProgress())
                        .build();

                return new ResponseEntity<>(objectiveResponse, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("생성된 목표가 최대치 입니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("팀장만 생성 가능합니다.", HttpStatus.FORBIDDEN);
        }
    }

    //목표 전체 조회
    public ResponseEntity<?> allObjective(UserDetailsImpl userDetails) {
        List<Long> objectiveIdList = userObjectiveService.allObjective(userDetails);

        List<ObjectiveResponse> objectiveResponseList = new ArrayList<>();
        for (int i = 0; i < objectiveIdList.size(); i++) {
            Optional<Objective> objective = objectiveRepository.findByIdAndDeleteStateFalse(objectiveIdList.get(i));
            if (objective.isPresent()) {
                ObjectiveResponse objectiveResponse = ObjectiveResponse.builder()
                        .myObjective(userObjectiveService.checkMyObjective(objective.get().getId(), userDetails))
                        .objectiveId(objective.get().getId())
                        .objective(objective.get().getObjective())
                        .startdate(objective.get().getStartDate())
                        .enddate(objective.get().getEndDate())
                        .color(objective.get().getColor())
                        .progress(objective.get().getProgress())
                        .build();

                objectiveResponseList.add(objectiveResponse);
            }
        }

        return new ResponseEntity<>(objectiveResponseList, HttpStatus.OK);
    }

    //목표 상세 조회
    public ResponseEntity<?> detailObjective(Long objectiveId, UserDetailsImpl userDetails) {
        Optional<Objective> objective = objectiveRepository.findByIdAndDeleteStateFalse(objectiveId);

        if (objective.isPresent()) {
            ObjectiveDetailResponse objectiveDetailResponse = ObjectiveDetailResponse.builder()
                    .myObjective(userObjectiveService.checkMyObjective(objectiveId, userDetails))
                    .objectiveId(objective.get().getId())
                    .objective(objective.get().getObjective())
                    .startdate(objective.get().getStartDate())
                    .enddate(objective.get().getEndDate())
                    .color(objective.get().getColor())
                    .build();

            return new ResponseEntity<>(objectiveDetailResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("존재하지 않는 목표입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //목표 진척도 수정
    @Transactional
    public ResponseEntity<String> objectiveProgressEdit(Long objectiveId, UserDetailsImpl userDetails, ProgressRequest progressRequest) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            Optional<Objective> objectiveProgressEdit = objectiveRepository.findByObjectiveIdAndTeam(objectiveId, userDetails.getUser().getTeam());
            if (objectiveProgressEdit.isPresent()) {
                ObjectiveEditor.ObjectiveEditorBuilder objectiveEditorBuilder = objectiveProgressEdit.get().ObjectiveToEditor();

                if (progressRequest.getProgress() > 0) {
                    ObjectiveEditor objectiveEditor = objectiveEditorBuilder
                            .progress(progressRequest.getProgress())
                            .build();
                    objectiveProgressEdit.get().ObjectiveEdit(objectiveEditor);
                } else if (progressRequest.getProgress() == 0) {
                    ObjectiveEditor objectiveEditor = objectiveEditorBuilder
                            .progress(0)
                            .build();
                    objectiveProgressEdit.get().ObjectiveEdit(objectiveEditor);
                    return new ResponseEntity<>("진척도가 초기화 되었습니다.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("입력된 진척도가 없습니다.", HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("진척도를 수정 했습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("존재하지 않는 목표이거나, 해당 목표의 소속팀이 아닙니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("수정할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    //목표 수정
    @Transactional
    public ResponseEntity<String> objectiveEdit(Long objectiveId, UserDetailsImpl userDetails, ObjectiveEditRequest objectiveEditRequest) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            Optional<Objective> editObjective = objectiveRepository.findByObjectiveIdAndTeam(objectiveId, userDetails.getUser().getTeam());
            if (editObjective.isPresent()) {
                ObjectiveEditor.ObjectiveEditorBuilder objectiveEditorBuilder = editObjective.get().ObjectiveToEditor();

                if (objectiveEditRequest.getObjective() != null && !objectiveEditRequest.getObjective().equals("")) {
                    ObjectiveEditor objectiveEditor = objectiveEditorBuilder
                            .objective(objectiveEditRequest.getObjective())
                            .build();
                    editObjective.get().ObjectiveEdit(objectiveEditor);
                }
                if (objectiveEditRequest.getStartdate() != null && !objectiveEditRequest.getStartdate().equals("")) {
                    ObjectiveEditor objectiveEditor = objectiveEditorBuilder
                            .startdate(objectiveEditRequest.getStartdate())
                            .build();
                    editObjective.get().ObjectiveEdit(objectiveEditor);
                }
                if (objectiveEditRequest.getEnddate() != null && !objectiveEditRequest.getEnddate().equals("")) {
                    ObjectiveEditor objectiveEditor = objectiveEditorBuilder
                            .enddate(objectiveEditRequest.getEnddate())
                            .build();
                    editObjective.get().ObjectiveEdit(objectiveEditor);
                }
                if (objectiveEditRequest.getColor() != null && !objectiveEditRequest.getColor().equals("")) {
                    ObjectiveEditor objectiveEditor = objectiveEditorBuilder
                            .color(objectiveEditRequest.getColor())
                            .build();
                    editObjective.get().ObjectiveEdit(objectiveEditor);
                }
                return new ResponseEntity<>("목표가 수정 되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("존재하지 않는 목표이거나, 해당 목표의 소속팀이 아닙니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    //목표 삭제
    @Transactional
    public ResponseEntity<String> objectiveDelete(Long objectiveId, UserDetailsImpl userDetails) {
        if (userObjectiveService.checkMyObjective(objectiveId, userDetails)) {
            List<KeyResult> KeyResultOfObjectiveList = userKeyResultService.checkKeyResultOfObjective(objectiveId, userDetails.getUser().getId());
            List<ToDo> ToDoOfObjectiveList = userToDoService.checkToDoOfObjective(objectiveId, userDetails.getUser().getId());
            Optional<Objective> deleteObjective = objectiveRepository.findById(objectiveId);
            if (deleteObjective.isPresent() && !KeyResultOfObjectiveList.isEmpty() && !ToDoOfObjectiveList.isEmpty()) {
                for (int i = 0; i < KeyResultOfObjectiveList.size(); i++) {
                    if (!KeyResultOfObjectiveList.get(i).isDeleteState()) {
                        KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = KeyResultOfObjectiveList.get(i).KeyResultToEditor();
                        KeyResultEditor keyResultEditor = keyResultEditorBuilder
                                .deleteState(true)
                                .build();
                        KeyResultOfObjectiveList.get(i).KeyResultEdit(keyResultEditor);
                    }
                }

                for (int i = 0; i < ToDoOfObjectiveList.size(); i++) {
                    if (!ToDoOfObjectiveList.get(i).isDeleteState()) {
                        ToDoEditor.ToDoEditorBuilder toDoEditorBuilder = ToDoOfObjectiveList.get(i).ToDoToEditor();
                        ToDoEditor toDoEditor = toDoEditorBuilder
                                .deleteState(true)
                                .build();
                        ToDoOfObjectiveList.get(i).ToDoEdit(toDoEditor);
                    }
                }

                if (!deleteObjective.get().isDeleteState()) {
                    ObjectiveEditor.ObjectiveEditorBuilder objectiveEditorBuilder = deleteObjective.get().ObjectiveToEditor();
                    ObjectiveEditor objectiveEditor = objectiveEditorBuilder
                            .deleteState(true)
                            .build();
                    deleteObjective.get().ObjectiveEdit(objectiveEditor);
                }

                return new ResponseEntity<>("목표, 핵심결과, 할일이 모두 삭제 되었습니다.", HttpStatus.OK);
            } else if (deleteObjective.isPresent() && !KeyResultOfObjectiveList.isEmpty()) {
                for (int i = 0; i < KeyResultOfObjectiveList.size(); i++) {
                    if (!KeyResultOfObjectiveList.get(i).isDeleteState()) {
                        KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = KeyResultOfObjectiveList.get(i).KeyResultToEditor();
                        KeyResultEditor keyResultEditor = keyResultEditorBuilder
                                .deleteState(true)
                                .build();
                        KeyResultOfObjectiveList.get(i).KeyResultEdit(keyResultEditor);
                    }
                }

                if (!deleteObjective.get().isDeleteState()) {
                    ObjectiveEditor.ObjectiveEditorBuilder objectiveEditorBuilder = deleteObjective.get().ObjectiveToEditor();
                    ObjectiveEditor objectiveEditor = objectiveEditorBuilder
                            .deleteState(true)
                            .build();
                    deleteObjective.get().ObjectiveEdit(objectiveEditor);
                }

                return new ResponseEntity<>("목표, 핵심결과가 모두 삭제 되었습니다.", HttpStatus.OK);
            } else if (deleteObjective.isPresent()) {
                if (!deleteObjective.get().isDeleteState()) {
                    ObjectiveEditor.ObjectiveEditorBuilder objectiveEditorBuilder = deleteObjective.get().ObjectiveToEditor();
                    ObjectiveEditor objectiveEditor = objectiveEditorBuilder
                            .deleteState(true)
                            .build();
                    deleteObjective.get().ObjectiveEdit(objectiveEditor);
                }

                return new ResponseEntity<>("목표가 삭제 되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("존재하지 않는 목표입니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
}
