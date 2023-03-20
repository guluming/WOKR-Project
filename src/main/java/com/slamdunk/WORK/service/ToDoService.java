package com.slamdunk.WORK.service;

import com.slamdunk.WORK.Editor.ToDoEditor;
import com.slamdunk.WORK.dto.request.ToDoEditRequest;
import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.dto.response.ToDoDetailResponse;
import com.slamdunk.WORK.dto.response.ToDoResponse;
import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.ToDo;
import com.slamdunk.WORK.entity.UserToDo;
import com.slamdunk.WORK.repository.KeyResultRepository;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.repository.ToDoRepository;
import com.slamdunk.WORK.repository.UserToDoRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final UserToDoRepository userToDoRepository;
    private final ToDoRepository toDoRepository;
    private final UserToDoService userToDoService;

    //할일 생성
    @Transactional
    public ResponseEntity<?>createToDo(Long objectiveId, Long keyResultId, ToDoRequest toDoRequest, UserDetailsImpl userDetails) {
        if (objectiveId == 0 && keyResultId == 0) {
            ToDo toDo = new ToDo(toDoRequest, null, null);
            toDoRepository.save(toDo);

            userToDoService.registerUserToDo(userDetails, null, null, toDo);
            return new ResponseEntity<>("할일이 생성 되었습니다.", HttpStatus.CREATED);
        } else if (objectiveId != 0 && keyResultId != 0) {
            Optional<Objective> objectiveCheck = objectiveRepository.findByIdAndDeleteStateFalse(objectiveId);
            Optional<KeyResult> keyResultCheck = keyResultRepository.findByIdAndDeleteStateFalse(keyResultId);

            if (objectiveCheck.isPresent() && keyResultCheck.isPresent()) {
                ToDo toDo = new ToDo(toDoRequest, objectiveCheck.get(), keyResultCheck.get());
                toDoRepository.save(toDo);

                userToDoService.registerUserToDo(userDetails, objectiveCheck.get(), keyResultCheck.get(), toDo);
                return new ResponseEntity<>("할일이 생성 되었습니다.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("목표 혹은 핵심결과가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("목표 혹은 핵심결과가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //투두 전체 조회
    public ResponseEntity<?> getAllToDos(UserDetailsImpl userDetails) {
        List<Long> todoId = userToDoService.allToDo(userDetails);
        List<ToDoResponse> toDoResponseList = new ArrayList<>();
        for (int i=0; i<todoId.size(); i++) {
            Optional<ToDo> toDo = toDoRepository.findByIdAndDeleteStateFalse(todoId.get(i));
            if (toDo.isPresent()) {
                ToDoResponse toDoResponse = ToDoResponse.builder()
                        .myToDo(userToDoService.checkMyToDo(toDo.get().getId(), userDetails))
                        .toDoId(toDo.get().getId())
                        .keyResultId(toDo.get().getKeyResult() != null ? toDo.get().getKeyResult().getId() : null)
                        .toDo(toDo.get().getToDo())
                        .memo(toDo.get().getMemo())
                        .startDate(toDo.get().getStartDate())
                        .startDateTime(toDo.get().getStartDateTime())
                        .endDate(toDo.get().getEndDate())
                        .endDateTime(toDo.get().getEndDateTime())
                        .fstartDate(toDo.get().getStartDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")))
                        .fendDate(toDo.get().getEndDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")))
                        .priority(toDo.get().getPriority())
                        .completion(toDo.get().isCompletion())
                        .color(toDo.get().getObjective() != null ? toDo.get().getObjective().getColor() : null)
                        .build();
                toDoResponseList.add(toDoResponse);
            }
        }
        return new ResponseEntity<>(toDoResponseList, HttpStatus.OK);
    }

    //투두 상세 조회
    public ResponseEntity<?> detailToDo(Long toDoId, UserDetailsImpl userDetails) {
        Optional<ToDo> toDo = toDoRepository.findByIdAndDeleteStateFalse(toDoId);
        if (toDo.isPresent()) {
            ToDoDetailResponse toDoDetailResponse = ToDoDetailResponse.builder()
                    .myToDo(userToDoService.checkMyToDo(toDoId, userDetails))
                    .toDoId(toDo.get().getId())
                    .toDo(toDo.get().getToDo())
                    .memo(toDo.get().getMemo())
                    .startDate(toDo.get().getStartDate())
                    .startDateTime(toDo.get().getStartDateTime())
                    .endDate(toDo.get().getEndDate())
                    .endDateTime(toDo.get().getEndDateTime())
                    .fstartDate(toDo.get().getStartDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")))
                    .fendDate(toDo.get().getEndDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")))
                    .priority(toDo.get().getPriority())
                    .build();
            return new ResponseEntity<>(toDoDetailResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("존재하지 않는 투두입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //투두 완료 상태
    @Transactional
    public ResponseEntity<?> updateCompletion (Long todoId, UserDetailsImpl userDetails) {
        Optional<ToDo> deletedToDoCheck = toDoRepository.findByIdAndDeleteStateFalse(todoId);
        if (deletedToDoCheck.isPresent()) {
            if (userToDoService.checkMyToDo(deletedToDoCheck.get().getId(), userDetails)) {
                if (!deletedToDoCheck.get().isCompletion()) {
                    ToDoEditor.ToDoEditorBuilder toDoEditorBuilder = deletedToDoCheck.get().ToDoToEditor();
                    ToDoEditor toDoEditor = toDoEditorBuilder
                            .completion(true)
                            .build();
                    deletedToDoCheck.get().ToDoEdit(toDoEditor);
                    return new ResponseEntity<>("완료 되었습니다.", HttpStatus.OK);
                } else {
                    ToDoEditor.ToDoEditorBuilder toDoEditorBuilder = deletedToDoCheck.get().ToDoToEditor();
                    ToDoEditor toDoEditor = toDoEditorBuilder
                            .completion(false)
                            .build();
                    deletedToDoCheck.get().ToDoEdit(toDoEditor);
                    return new ResponseEntity<>("완료가 취소 되었습니다.", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("완료 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("삭제된 할일 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //투두 수정
    @Transactional
    public ResponseEntity<?> updateToDo(Long todoId, UserDetailsImpl userDetails, ToDoEditRequest toDoEditRequest) {
        Optional<ToDo> editToDo = toDoRepository.findByIdAndDeleteStateFalse(todoId);
        if (editToDo.isPresent()) {
            if (userToDoService.checkMyToDo(editToDo.get().getId(), userDetails)) {
                ToDoEditor.ToDoEditorBuilder toDoEditorBuilder = editToDo.get().ToDoToEditor();
                ToDoEditor toDoEditor = toDoEditorBuilder
                        .toDo(toDoEditRequest.getToDo())
                        .memo(toDoEditRequest.getMemo())
                        .startDate(toDoEditRequest.getStartDate())
                        .startDateTime(toDoEditRequest.getStartDateTime())
                        .endDate(toDoEditRequest.getEndDate())
                        .endDateTime(toDoEditRequest.getEndDateTime())
                        .priority(toDoEditRequest.getPriority())
                        .build();
                editToDo.get().ToDoEdit(toDoEditor);
                return new ResponseEntity<>("수정이 완료 되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("삭제된 할일 입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}


//    public void deleteToDoById (Long todo_id, UserDetailsImpl userDetails){
//        toDoRepository.deleteById(todo_id);
//    }
//



