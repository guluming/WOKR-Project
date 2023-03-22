package com.slamdunk.WORK.service;

import com.slamdunk.WORK.Editor.KeyResultEditor;
import com.slamdunk.WORK.dto.request.EmoticonRequest;
import com.slamdunk.WORK.dto.request.KeyResultEditRequest;
import com.slamdunk.WORK.dto.request.KeyResultRequest;
import com.slamdunk.WORK.dto.request.ProgressRequest;
import com.slamdunk.WORK.dto.response.KeyResultDetailResponse;
import com.slamdunk.WORK.dto.response.KeyResultResponse;
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

    //핵심결과 생성
    @Transactional
    public ResponseEntity<?> registerKeyResult(Long objectiveId, KeyResultRequest keyResultRequest, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getTeamPosition().equals("팀장")) {
            Optional<Objective> objectiveCheck = objectiveRepository.findByIdAndDeleteStateFalse(objectiveId);
            if (objectiveCheck.isEmpty()) {
                return new ResponseEntity<>("목표가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
            } else {
                KeyResult newKeyResult = new KeyResult(objectiveCheck.get(), keyResultRequest);
                keyResultRepository.save(newKeyResult);

                userKeyResultService.registerUserKeyResult(newKeyResult, objectiveCheck.get(), userDetails);
                return new ResponseEntity<>(HttpStatus.CREATED);
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
        Optional<KeyResult> keyResult = keyResultRepository.findByIdAndDeleteStateFalse(keyResultId);

        if (keyResult.isPresent()) {
            if (userKeyResultService.checkMyKeyResult(keyResultId, userDetails)) {
                KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = keyResult.get().KeyResultToEditor();

                if (progressRequest.getProgress() > 0) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .progress(progressRequest.getProgress())
                            .build();
                    keyResult.get().KeyResultEdit(keyResultEditor);
                } else if (progressRequest.getProgress() == 0) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .progress(0)
                            .build();
                    keyResult.get().KeyResultEdit(keyResultEditor);
                    return new ResponseEntity<>("진척도가 초기화 되었습니다.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("입력된 진척도가 없습니다.", HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("진척도를 수정 했습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("수정할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("존재하지 않는 핵심결과입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //핵심결과 자신감 수정
    @Transactional
    public ResponseEntity<String> keyResultEmoticonEdit(Long keyResultId, UserDetailsImpl userDetails, EmoticonRequest emoticonRequest) {
        Optional<KeyResult> keyResult = keyResultRepository.findByIdAndDeleteStateFalse(keyResultId);

        if (keyResult.isPresent()) {
            if (userKeyResultService.checkMyKeyResult(keyResultId, userDetails)) {
                KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = keyResult.get().KeyResultToEditor();

                if (emoticonRequest.getEmoticon() > 0) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .emoticon(emoticonRequest.getEmoticon())
                            .build();
                    keyResult.get().KeyResultEdit(keyResultEditor);
                } else if (emoticonRequest.getEmoticon() == 0) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .emoticon(0)
                            .build();
                    keyResult.get().KeyResultEdit(keyResultEditor);
                    return new ResponseEntity<>("자신감이 초기화 되었습니다.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("입력된 자신감이 없습니다.", HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("자신감을 수정 했습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("수정할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("존재하지 않는 핵심결과입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //핵심결과 수정
    @Transactional
    public ResponseEntity<?> keyResultEdit(Long keyResultId, UserDetailsImpl userDetails, KeyResultEditRequest keyResultEditRequest) {
        if (userKeyResultService.checkMyKeyResult(keyResultId, userDetails)) {
            Optional<KeyResult> editKeyResult = keyResultRepository.findByIdAndDeleteStateFalse(keyResultId);
            if (editKeyResult.isPresent()) {
                KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = editKeyResult.get().KeyResultToEditor();
                if (keyResultEditRequest.getKeyResult() != null) {
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .keyResult(keyResultEditRequest.getKeyResult())
                            .build();
                    editKeyResult.get().KeyResultEdit(keyResultEditor);
                }

                return new ResponseEntity<>("핵심결과가 수정 되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("존재하지 않는 핵심결과입니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    //핵심결과 삭제
    @Transactional
    public ResponseEntity<?> keyResultDelete(Long keyResultId, UserDetailsImpl userDetails) {
        if (userKeyResultService.checkMyKeyResult(keyResultId, userDetails)) {
            Optional<KeyResult> deleteKeyResult = keyResultRepository.findById(keyResultId);
            if (deleteKeyResult.isPresent()) {
                if (!deleteKeyResult.get().isDeleteState()) {
                    KeyResultEditor.KeyResultEditorBuilder keyResultEditorBuilder = deleteKeyResult.get().KeyResultToEditor();
                    KeyResultEditor keyResultEditor = keyResultEditorBuilder
                            .deleteState(true)
                            .build();
                    deleteKeyResult.get().KeyResultEdit(keyResultEditor);

                    return new ResponseEntity<>("핵심결과가 삭제 되었습니다.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("이미 삭제된 핵심결과입니다.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("존재하지 않는 핵심결과입니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
}
