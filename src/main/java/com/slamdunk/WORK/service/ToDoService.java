package com.slamdunk.WORK.service;

import com.slamdunk.WORK.Editor.ToDoEditor;
import com.slamdunk.WORK.dto.request.ToDoEditRequest;
import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.dto.response.ToDoDetailResponse;
import com.slamdunk.WORK.dto.response.ToDoProgressResponse;
import com.slamdunk.WORK.dto.response.ToDoResponse;
import com.slamdunk.WORK.entity.*;
import com.slamdunk.WORK.repository.*;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final UserToDoRepository userToDoRepository;
    private final ToDoRepository toDoRepository;
    private final UserToDoService userToDoService;
    private final UserRepository userRepository;

    //할일 생성
    @Transactional
    public ResponseEntity<?> createToDo(Long objectiveId, Long keyResultId, ToDoRequest toDoRequest,
                                        UserDetailsImpl userDetails) {
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
        for (int i = 0; i < todoId.size(); i++) {
            Optional<ToDo> toDo = toDoRepository.findByIdAndDeleteStateFalse(todoId.get(i));
            if (toDo.isPresent()) {
                ToDoResponse toDoResponse = ToDoResponse.builder()
                        .myToDo(userToDoService.checkMyToDo(toDo.get().getId(), userDetails))
                        .keyResultId(toDo.get().getKeyResult() != null ? toDo.get().getKeyResult().getId() : null)
                        .krNumber(toDo.get().getKeyResult() != null ? toDo.get().getKeyResult().getKrNumber() : 0)
                        .toDoId(toDo.get().getId())
                        .toDo(toDo.get().getToDo())
                        .memo(toDo.get().getMemo())
                        .startDate(toDo.get().getStartDate())
                        .startDateTime(toDo.get().getStartDateTime())
                        .endDate(toDo.get().getEndDate())
                        .endDateTime(toDo.get().getEndDateTime())
                        .fstartDate(toDo.get().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .fendDate(toDo.get().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
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
                    .krNumber(toDo.get().getKeyResult() != null ? toDo.get().getKeyResult().getKrNumber() : 0)
                    .toDoId(toDo.get().getId())
                    .toDo(toDo.get().getToDo())
                    .memo(toDo.get().getMemo())
                    .startDate(toDo.get().getStartDate())
                    .startDateTime(toDo.get().getStartDateTime())
                    .endDate(toDo.get().getEndDate())
                    .endDateTime(toDo.get().getEndDateTime())
                    .fstartDate(toDo.get().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                    .fendDate(toDo.get().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                    .priority(toDo.get().getPriority())
                    .build();
            return new ResponseEntity<>(toDoDetailResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("존재하지 않는 할일 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //투두 완료 상태
    @Transactional
    public ResponseEntity<?> updateCompletion(Long todoId, UserDetailsImpl userDetails) {
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

    //할일 삭제
    @Transactional
    public ResponseEntity<String> toDoDelete(Long todoId, UserDetailsImpl userDetails) {
        Optional<ToDo> deletedToDoCheck = toDoRepository.findByIdAndDeleteStateFalse(todoId);
        if (deletedToDoCheck.isPresent()) {
            if (userToDoService.checkMyToDo(deletedToDoCheck.get().getId(), userDetails)) {
                if (!deletedToDoCheck.get().isDeleteState()) {
                    ToDoEditor.ToDoEditorBuilder toDoEditorBuilder = deletedToDoCheck.get().ToDoToEditor();
                    ToDoEditor toDoEditor = toDoEditorBuilder
                            .deleteState(true)
                            .build();
                    deletedToDoCheck.get().ToDoEdit(toDoEditor);
                    return new ResponseEntity<>("삭제 되었습니다.", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("이미 삭제된 할일 입니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("존재하지 않는 할일 입니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    //할일 기간만료 조회
    public ResponseEntity<?> getExpirationToDo(UserDetailsImpl userDetails) {
        LocalDate today = LocalDate.now();
        List<User> teamMembers = userRepository.findAllByTeam(userDetails.getUser().getTeam());
        List<Long> teamMemberIds = teamMembers.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        List<UserToDo> teamToDoList = userToDoRepository.findAllByUserIdIn(teamMemberIds);
        List<ToDoResponse> toDoResponseExpList = new ArrayList<>();

        for (int i = 0; i < teamToDoList.size(); i++) {
            UserToDo userToDo = teamToDoList.get(i);
            ToDo toDo = userToDo.getToDo();
            if (toDo != null && toDo.getEndDate().isBefore(today) && !toDo.isCompletion()) {
                ToDoResponse toDoResponse = ToDoResponse.builder()
                        .myToDo(userToDoService.checkMyToDo(toDo.getId(), userDetails))
                        .createUser(teamToDoList.get(i).getUser().getName())
                        .keyResultId(toDo.getKeyResult() != null ? toDo.getKeyResult().getId() : null)
                        .krNumber(toDo.getKeyResult() != null ? toDo.getKeyResult().getKrNumber() : 0)
                        .toDoId(toDo.getId())
                        .toDo(toDo.getToDo())
                        .memo(toDo.getMemo())
                        .startDate(toDo.getStartDate())
                        .startDateTime(toDo.getStartDateTime())
                        .endDate(toDo.getEndDate())
                        .endDateTime(toDo.getEndDateTime())
                        .fstartDate(toDo.getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .fendDate(toDo.getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .priority(toDo.getPriority())
                        .completion(toDo.isCompletion())
                        .color(toDo.getObjective() != null ? toDo.getObjective().getColor() : null)
                        .build();
                toDoResponseExpList.add(toDoResponse);
            }
        }
        return new ResponseEntity<>(toDoResponseExpList, HttpStatus.OK);
    }


        //할일 날짜별 전체 조회
        public ResponseEntity<?> getProgressToDo (UserDetailsImpl userDetails){
            List<User> teamMemberList = userRepository.findAllByTeam(userDetails.getUser().getTeam());

            List<ToDoProgressResponse> toDoProgressResponseList = new ArrayList<>();

            List<UserToDo> progressUserToDoList = userToDoRepository.findAllByTeamAndCompletionFalseAndProgress(userDetails.getUser().getTeam(), LocalDate.now());
            List<UserToDo> completionUserToDoList = userToDoRepository.findAllByTeamAndCompletionTrueAndCompletion(userDetails.getUser().getTeam());

            LocalDate startDay = LocalDate.now();
            LocalDate lastDay = LocalDate.now();
            LocalDate moveDay = LocalDate.now();

            for (int k = 0; k < teamMemberList.size(); k++) {
                UserToDo userToDoFirstEndDate = userToDoService.findFirstEndDate(teamMemberList.get(k));
                if (userToDoFirstEndDate != null) {
                    LocalDate temp = userToDoFirstEndDate.getToDo().getEndDate();
                    if (temp.isBefore(startDay)) {
                        startDay = userToDoFirstEndDate.getToDo().getEndDate();
                        moveDay = userToDoFirstEndDate.getToDo().getEndDate();
                    }
                }

                UserToDo userToDoLastEndDate = userToDoService.findLastEndDate(teamMemberList.get(k));
                if (userToDoLastEndDate != null) {
                    LocalDate temp = userToDoLastEndDate.getToDo().getEndDate();
                    if (temp.isAfter(lastDay)) {
                        lastDay = userToDoLastEndDate.getToDo().getEndDate();
                    }
                }
            }

            while ((moveDay.isEqual(startDay) || moveDay.isAfter(startDay)) && (moveDay.isEqual(lastDay) || moveDay.isBefore(lastDay))) {
                List<ToDoProgressResponse.progressTodo> progressTodoList = new ArrayList<>();
                List<ToDoProgressResponse.completionTodo> completionTodoList = new ArrayList<>();

                for (int i = 0; i < progressUserToDoList.size(); i++) {
                    if ((progressUserToDoList.get(i).getToDo().getStartDate().isEqual(moveDay) || progressUserToDoList.get(i).getToDo().getStartDate().isBefore(moveDay))
                            && (progressUserToDoList.get(i).getToDo().getEndDate().isEqual(moveDay) || progressUserToDoList.get(i).getToDo().getEndDate().isAfter(moveDay))) {
                        ToDoProgressResponse.progressTodo progressTodo = ToDoProgressResponse.progressTodo.builder()
                                .myToDo(userToDoService.checkUserMyToDo(progressUserToDoList.get(i).getToDo().getId(), userDetails.getUser()))
                                .createUser(progressUserToDoList.get(i).getUser().getName())
                                .keyResultId(progressUserToDoList.get(i).getKeyResult() != null ? progressUserToDoList.get(i).getKeyResult().getId() : null)
                                .krNumber(progressUserToDoList.get(i).getKeyResult() != null ? progressUserToDoList.get(i).getKeyResult().getKrNumber() : 0)
                                .toDoId(progressUserToDoList.get(i).getToDo().getId())
                                .toDo(progressUserToDoList.get(i).getToDo().getToDo())
                                .memo(progressUserToDoList.get(i).getToDo().getMemo())
                                .startDate(progressUserToDoList.get(i).getToDo().getStartDate())
                                .startDateTime(progressUserToDoList.get(i).getToDo().getStartDateTime())
                                .endDate(progressUserToDoList.get(i).getToDo().getEndDate())
                                .endDateTime(progressUserToDoList.get(i).getToDo().getEndDateTime())
                                .fstartDate(progressUserToDoList.get(i).getToDo().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                                .fendDate(progressUserToDoList.get(i).getToDo().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                                .priority(progressUserToDoList.get(i).getToDo().getPriority())
                                .completion(progressUserToDoList.get(i).getToDo().isCompletion())
                                .color(progressUserToDoList.get(i).getObjective() != null ? progressUserToDoList.get(i).getObjective().getColor() : null)
                                .build();

                        progressTodoList.add(progressTodo);
                    }
                }

                for (int i = 0; i < completionUserToDoList.size(); i++) {
                    if (completionUserToDoList.get(i).getToDo().getEndDate().isEqual(moveDay)) {
                        ToDoProgressResponse.completionTodo completionTodo = ToDoProgressResponse.completionTodo.builder()
                                .myToDo(userToDoService.checkUserMyToDo(completionUserToDoList.get(i).getToDo().getId(), userDetails.getUser()))
                                .createUser(completionUserToDoList.get(i).getUser().getName())
                                .keyResultId(completionUserToDoList.get(i).getKeyResult() != null ? completionUserToDoList.get(i).getKeyResult().getId() : null)
                                .krNumber(completionUserToDoList.get(i).getKeyResult() != null ? completionUserToDoList.get(i).getKeyResult().getKrNumber() : 0)
                                .toDoId(completionUserToDoList.get(i).getToDo().getId())
                                .toDo(completionUserToDoList.get(i).getToDo().getToDo())
                                .memo(completionUserToDoList.get(i).getToDo().getMemo())
                                .startDate(completionUserToDoList.get(i).getToDo().getStartDate())
                                .startDateTime(completionUserToDoList.get(i).getToDo().getStartDateTime())
                                .endDate(completionUserToDoList.get(i).getToDo().getEndDate())
                                .endDateTime(completionUserToDoList.get(i).getToDo().getEndDateTime())
                                .fstartDate(completionUserToDoList.get(i).getToDo().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                                .fendDate(completionUserToDoList.get(i).getToDo().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                                .priority(completionUserToDoList.get(i).getToDo().getPriority())
                                .completion(completionUserToDoList.get(i).getToDo().isCompletion())
                                .color(completionUserToDoList.get(i).getObjective() != null ? completionUserToDoList.get(i).getObjective().getColor() : null)
                                .build();

                        completionTodoList.add(completionTodo);
                    }
                }

                if (!progressTodoList.isEmpty() || !completionTodoList.isEmpty()) {
                    ToDoProgressResponse toDoProgressResponse = ToDoProgressResponse.builder()
                            .targetDate(moveDay.format(DateTimeFormatter.ofPattern("MM월 dd일")))
                            .progressTodo(progressTodoList)
                            .completionTodo(completionTodoList)
                            .build();

                    toDoProgressResponseList.add(toDoProgressResponse);
                }

                moveDay = moveDay.plusDays(1);
            }

            return new ResponseEntity<>(toDoProgressResponseList, HttpStatus.OK);
        }
    }

