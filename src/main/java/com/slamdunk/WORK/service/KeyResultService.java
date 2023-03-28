package com.slamdunk.WORK.service;

import com.slamdunk.WORK.Editor.KeyResultEditor;
import com.slamdunk.WORK.Editor.ToDoEditor;
import com.slamdunk.WORK.dto.request.EmoticonRequest;
import com.slamdunk.WORK.dto.request.KeyResultEditRequest;
import com.slamdunk.WORK.dto.request.KeyResultRequest;
import com.slamdunk.WORK.dto.request.ProgressRequest;
import com.slamdunk.WORK.dto.response.KeyResultDetailResponse;
import com.slamdunk.WORK.dto.response.KeyResultResponse;
import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.ToDo;
import com.slamdunk.WORK.entity.UserKeyResult;
import com.slamdunk.WORK.repository.KeyResultRepository;
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
public class KeyResultService {
    private final KeyResultRepository keyResultRepository;
    private final ObjectiveRepository objectiveRepository;
    private final UserKeyResultService userKeyResultService;
    private final UserToDoService userToDoService;

    //핵심결과 생성
    @Transactional
    public ResponseEntity<?> registerKeyResult(Long objectiveId, KeyResultRequest keyResultRequest, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            Optional<Objective> objectiveCheck = objectiveRepository.findByIdAndDeleteStateFalse(objectiveId);
            if (objectiveCheck.isEmpty()) {
                return new ResponseEntity<>("목표가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
            } else {
                List<UserKeyResult> checkKeyResultCount = userKeyResultService.allKeyResultOfObjective(objectiveId, userDetails);
                if (checkKeyResultCount.size() < 3) {
                    KeyResult newKeyResult = new KeyResult(objectiveCheck.get(), keyResultRequest);
                    keyResultRepository.save(newKeyResult);

                    userKeyResultService.registerUserKeyResult(newKeyResult, objectiveCheck.get(), userDetails);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("핵심결과가 최대치 입니다.", HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            return new ResponseEntity<>("팀장만 생성 가능합니다.", HttpStatus.FORBIDDEN);
        }
    }

    //핵심결과 전체 조회
    public ResponseEntity<?> allKeyResult(UserDetailsImpl userDetails) {
        List<Long> keyResultId = userKeyResultService.allKeyResult(userDetails);

        List<KeyResultResponse> keyResultResponseList = new ArrayList<>();
        for (int i = 0; i < keyResultId.size(); i++) {
            Optional<KeyResult> keyResult = keyResultRepository.findByIdAndDeleteStateFalse(keyResultId.get(i));
            if (keyResult.isPresent()) {
                KeyResultResponse keyResultResponse = KeyResultResponse.builder()
                        .myKeyResult(userKeyResultService.checkMyKeyResult(keyResult.get().getId(), userDetails))
                        .keyResultId(keyResult.get().getId())
                        .krNumber(keyResult.get().getKrNumber())
                        .keyResult(keyResult.get().getKeyResult())
                        .progress(keyResult.get().getProgress())
                        .emotion(keyResult.get().getEmoticon())
                        .build();

                keyResultResponseList.add(keyResultResponse);
            }
        }

        return new ResponseEntity<>(keyResultResponseList, HttpStatus.OK);
    }

    //핵심결과 상세 조회
    public ResponseEntity<?> detailKeyResult(Long keyResultId, UserDetailsImpl userDetails) {
        Optional<KeyResult> keyResult = keyResultRepository.findByIdAndDeleteStateFalse(keyResultId);
        if (keyResult.isPresent()) {
            KeyResultDetailResponse keyResultDetailResponse = KeyResultDetailResponse.builder()
                    .myKeyResult(userKeyResultService.checkMyKeyResult(keyResultId, userDetails))
                    .keyResultId(keyResult.get().getId())
                    .krNumber(keyResult.get().getKrNumber())
                    .keyResult(keyResult.get().getKeyResult())
                    .build();

            return new ResponseEntity<>(keyResultDetailResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("존재하지 않는 핵심결과입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //핵심결과 진척도 수정
    @Transactional
    public ResponseEntity<String> keyResultProgressEdit(Long keyResultId, UserDetailsImpl userDetails, ProgressRequest progressRequest) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            Optional<KeyResult> keyResultProgressEdit = keyResultRepository.findByKeyResultIdAndTeam(keyResultId, userDetails.getUser().getTeam());
            if (keyResultProgressEdit.isPresent()) {
                KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = keyResultProgressEdit.get().KeyResultToEditor();

                if (progressRequest.getProgress() > 0) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .progress(progressRequest.getProgress())
                            .build();
                    keyResultProgressEdit.get().KeyResultEdit(keyResultEditor);
                    return new ResponseEntity<>("진척도를 수정 했습니다.", HttpStatus.OK);
                } else if (progressRequest.getProgress() == 0) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .progress(0)
                            .build();
                    keyResultProgressEdit.get().KeyResultEdit(keyResultEditor);
                    return new ResponseEntity<>("진척도가 초기화 되었습니다.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("입력된 진척도가 없습니다.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("존재하지 않는 핵심결과이거나, 해당 핵심결과의 소속팀이 아닙니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("수정할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    //핵심결과 자신감 수정
    @Transactional
    public ResponseEntity<String> keyResultEmoticonEdit(Long keyResultId, UserDetailsImpl userDetails, EmoticonRequest emoticonRequest) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            Optional<KeyResult> keyResultEmoticonEdit = keyResultRepository.findByKeyResultIdAndTeam(keyResultId, userDetails.getUser().getTeam());
            if (keyResultEmoticonEdit.isPresent()) {
                KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = keyResultEmoticonEdit.get().KeyResultToEditor();

                if (emoticonRequest.getEmoticon() > 0) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .emoticon(emoticonRequest.getEmoticon())
                            .build();
                    keyResultEmoticonEdit.get().KeyResultEdit(keyResultEditor);
                    return new ResponseEntity<>("자신감을 수정 했습니다.", HttpStatus.OK);
                } else if (emoticonRequest.getEmoticon() == 0) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .emoticon(0)
                            .build();
                    keyResultEmoticonEdit.get().KeyResultEdit(keyResultEditor);
                    return new ResponseEntity<>("자신감이 초기화 되었습니다.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("입력된 자신감이 없습니다.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("존재하지 않는 핵심결과이거나, 해당 핵심결과의 소속팀이 아닙니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("수정할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    //핵심결과 수정
    @Transactional
    public ResponseEntity<?> keyResultEdit(Long keyResultId, UserDetailsImpl userDetails, KeyResultEditRequest keyResultEditRequest) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            Optional<KeyResult> keyResultEdit = keyResultRepository.findByKeyResultIdAndTeam(keyResultId, userDetails.getUser().getTeam());
            if (keyResultEdit.isPresent()) {
                KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = keyResultEdit.get().KeyResultToEditor();

                if (keyResultEditRequest.getKeyResult() != null && !keyResultEditRequest.getKeyResult().equals("")) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .keyResult(keyResultEditRequest.getKeyResult())
                            .build();
                    keyResultEdit.get().KeyResultEdit(keyResultEditor);
                    return new ResponseEntity<>("핵심결과가 수정 되었습니다.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("입력된 핵심결과 없습니다.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("존재하지 않는 핵심결과이거나, 해당 핵심결과의 소속팀이 아닙니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("수정할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    //핵심결과 삭제
    @Transactional
    public ResponseEntity<?> keyResultDelete(Long keyResultId, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            List<ToDo> ToDoOfKeyResultList = userToDoService.checkToDoOfKeyResult(keyResultId, userDetails.getUser().getId());
            Optional<KeyResult> deleteKeyResult = keyResultRepository.findByKeyResultIdAndTeam(keyResultId, userDetails.getUser().getTeam());
            if (deleteKeyResult.isPresent() && !ToDoOfKeyResultList.isEmpty()) {
                for (int i = 0; i < ToDoOfKeyResultList.size(); i++) {
                    if (!ToDoOfKeyResultList.get(i).isDeleteState()) {
                        ToDoEditor.ToDoEditorBuilder toDoEditorBuilder = ToDoOfKeyResultList.get(i).ToDoToEditor();
                        ToDoEditor toDoEditor = toDoEditorBuilder
                                .deleteState(true)
                                .build();
                        ToDoOfKeyResultList.get(i).ToDoEdit(toDoEditor);
                    }
                }

                if (!deleteKeyResult.get().isDeleteState()) {
                    KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = deleteKeyResult.get().KeyResultToEditor();
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .deleteState(true)
                            .build();
                    deleteKeyResult.get().KeyResultEdit(keyResultEditor);
                }

                return new ResponseEntity<>("핵심결과, 할일이 모두 삭제 되었습니다.", HttpStatus.OK);
            } else if (deleteKeyResult.isPresent()) {
                if (!deleteKeyResult.get().isDeleteState()) {
                    KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = deleteKeyResult.get().KeyResultToEditor();
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .deleteState(true)
                            .build();
                    deleteKeyResult.get().KeyResultEdit(keyResultEditor);
                }

                return new ResponseEntity<>("핵심결과가 삭제 되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("존재하지 않는 핵심결과이거나, 해당 핵심결과의 소속팀이 아닙니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
}
