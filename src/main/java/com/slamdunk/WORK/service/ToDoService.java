package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.dto.response.ToDoDetailResponse;
import com.slamdunk.WORK.dto.response.ToDoResponse;
import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.ToDo;
import com.slamdunk.WORK.repository.KeyResultRepository;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.repository.ToDoRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final ToDoRepository toDoRepository;
    private final UserToDoService userToDoService;

    //투두 전체 조회
    public ResponseEntity<?> getAllToDos(UserDetailsImpl userDetails) {
        List<Long> todoId = userToDoService.allToDo(userDetails);
        List<ToDoResponse> toDoResponseList = new ArrayList<>();
        for (int i=0; i<todoId.size(); i++) {
            Optional<ToDo> toDo = toDoRepository.findById(todoId.get(i));
            if (toDo.isPresent()) {
                ToDoResponse toDoResponse = ToDoResponse.builder()
                        .myToDo(userToDoService.checkMyToDo(toDo.get().getId(), userDetails))
                        .toDoId(toDo.get().getId())
                        .toDo(toDo.get().getToDo())
                        .memo(toDo.get().getMemo())
                        .startDate(toDo.get().getStartDate())
                        .endDate(toDo.get().getEndDate())
                        .priority(toDo.get().getPriority())
                       // .display(toDo.get().isDisplay())
                        .build();
                toDoResponseList.add(toDoResponse);
            }
        }
        return new ResponseEntity<>(toDoResponseList, HttpStatus.OK);

    }

    //투두 상세 조회
    public ResponseEntity<?> detailToDo(Long toDoId, UserDetailsImpl userDetails) {
        Optional<ToDo> toDo = toDoRepository.findById(toDoId);

        if (toDo.isPresent()) {
            ToDoDetailResponse toDoDetailResponse = ToDoDetailResponse.builder()
                    .myToDo(userToDoService.checkMyToDo(toDoId, userDetails))
                    .toDoId(toDo.get().getId())
                    .toDo(toDo.get().getToDo())
                    .memo(toDo.get().getMemo())
                    .startDate(toDo.get().getStartDate())
                    .endDate(toDo.get().getEndDate())
                    .priority(toDo.get().getPriority())
                    .build();
            return new ResponseEntity<>(toDoDetailResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("존재하지 않는 투두입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //투두 생성
//    @Transactional
//    public ResponseEntity<?> createToDo(Long objectiveId, Long keyResultId, ToDoRequest toDoRequest, UserDetailsImpl userDetails) {
//        ToDo toDo = new ToDo(toDoRequest);
//        if (objectiveId != null) {
//            Optional<Objective> objectiveCheck = objectiveRepository.findById(objectiveId);
//            objectiveCheck.ifPresent(objective -> toDo.setObjective(objective));
//        }
//        if (keyResultId != null) {
//            Optional<KeyResult> keyResultCheck = keyResultRepository.findById(keyResultId);
//            keyResultCheck.ifPresent(keyResult -> {
//                toDo.setKeyResult(keyResult);
//                userToDoService.registerUserToDo(toDo, keyResult, userDetails);
//            });
//        }
//        toDoRepository.save(toDo);
//        userToDoService.registerUserToDo(toDo, null, userDetails);
//
//        ToDoResponse toDoResponse = ToDoResponse.builder()
//                .myToDo(userToDoService.checkMyToDo(toDo.getId(), userDetails))
//                .toDoId(toDo.getId())
//                .toDo(toDo.getToDo())
//                .memo(toDo.getMemo())
//                .startDate(toDo.getStartDate())
//                .endDate(toDo.getEndDate())
//                .priority(toDo.getPriority())
//                //.display(toDo.isDisplay())
//                .build();
//        return new ResponseEntity<>(toDoResponse, HttpStatus.CREATED);
//    }

    @Transactional
    public ResponseEntity<?>createToDo(ToDoRequest toDoRequest, UserDetailsImpl userDetails) {
        KeyResult keyResultCheck = keyResultRepository.findById(toDoRequest.getKeyResultId()).orElse(null);
        if (keyResultCheck != null) {
            KeyResult keyResult = keyResultCheck;
            ToDo toDo = new ToDo(toDoRequest);
            toDo.setKeyResult(keyResult);
            toDoRepository.save(toDo);
            userToDoService.registerUserToDo(toDo, keyResult, userDetails);

            ToDoResponse toDoResponse = ToDoResponse.builder()
                    .myToDo(userToDoService.checkMyToDo(toDo.getId(), userDetails))
                    .toDoId(toDo.getId())
                    .toDo(toDo.getToDo())
                    .memo(toDo.getMemo())
                    .startDate(toDo.getStartDate())
                    .endDate(toDo.getEndDate())
                    .priority(toDo.getPriority())
//                    .display(toDo.isDisplay())
                    .build();
            return new ResponseEntity<>(toDoResponse, HttpStatus.CREATED);
        } else {
            ToDo toDo = new ToDo(toDoRequest);
            toDoRepository.save(toDo);
            userToDoService.registerUserToDo(toDo, null, userDetails);

            ToDoResponse toDoResponse = ToDoResponse.builder()
                    .myToDo(userToDoService.checkMyToDo(toDo.getId(), userDetails))
                    .toDoId(toDo.getId())
                    .toDo(toDo.getToDo())
                    .memo(toDo.getMemo())
                    .startDate(toDo.getStartDate())
                    .endDate(toDo.getEndDate())
                    .priority(toDo.getPriority())
//                    .display(toDo.isDisplay())
                    .build();
            return new ResponseEntity<>(toDoResponse, HttpStatus.CREATED);
        }
    }



    //투두 수정
    public void updateToDo(Long todo_id, UserDetailsImpl userDetails, ToDoRequest toDoRequest) {
        Optional<ToDo> toDoOptional = toDoRepository.findById(todo_id);
        if (toDoOptional.isPresent()) {
            ToDo existingToDo = toDoOptional.get();
            if (existingToDo.getId().equals(userDetails.getUser().getId())) {
                ToDo updatedToDo = ToDo.builder()
                        .id(existingToDo.getId())
                        .toDo(toDoRequest.getToDo())
                        .memo(toDoRequest.getMemo())
                        .startDate(toDoRequest.getStartDate())
                        .endDate(toDoRequest.getEndDate())
                        .priority(toDoRequest.getPriority())
//                        .display(toDoRequest.isDisplay())
                        .build();
                toDoRepository.save(updatedToDo);
            }
        }
    }
        //투두 완료 상태
        public void updateCompletion (Long todo_id, UserDetailsImpl userDetails, ToDoRequest toDoRequest) {
            Optional<ToDo> toDoOptional = toDoRepository.findById(todo_id);
            if (toDoOptional.isPresent()) {
                ToDo donetoDo = toDoOptional.get();
                if (donetoDo.getId().equals(userDetails.getUser().getId())) {
                    donetoDo.setCompletion(toDoRequest.isCompletion());
                    toDoRepository.save(donetoDo);

                }
            }
        }
}


//    public void deleteToDoById (Long todo_id, UserDetailsImpl userDetails){
//        toDoRepository.deleteById(todo_id);
//    }
//



