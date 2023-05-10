package com.slamdunk.WORK.service;

import com.slamdunk.WORK.Editor.ToDoEditor;
import com.slamdunk.WORK.dto.request.TeamMemberToDoRequest;
import com.slamdunk.WORK.dto.request.ToDoEditRequest;
import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.dto.request.WeekToDoRequest;
import com.slamdunk.WORK.dto.response.*;
import com.slamdunk.WORK.entity.*;
import com.slamdunk.WORK.repository.*;
import com.slamdunk.WORK.security.UserDetailsImpl;
import com.slamdunk.WORK.sort.CreatedDateSort;
import com.slamdunk.WORK.sort.EndDateSort;
import com.slamdunk.WORK.sort.PrioritySort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final UserToDoRepository userToDoRepository;
    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;
    private final KeyResultService keyResultService;
    private final UserToDoService userToDoService;

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
        List<Long> todoId = userToDoService.allToDo(userDetails.getUser().getId());
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
                    if (deletedToDoCheck.get().getKeyResult() != null) {
                        keyResultService.keyResultProgressEdit(deletedToDoCheck.get().getKeyResult(), userDetails);
                    }

                    return new ResponseEntity<>("완료 되었습니다.", HttpStatus.OK);
                } else {
                    ToDoEditor.ToDoEditorBuilder toDoEditorBuilder = deletedToDoCheck.get().ToDoToEditor();
                    ToDoEditor toDoEditor = toDoEditorBuilder
                            .completion(false)
                            .build();
                    deletedToDoCheck.get().ToDoEdit(toDoEditor);
                    if (deletedToDoCheck.get().getKeyResult() != null) {
                        keyResultService.keyResultProgressEdit(deletedToDoCheck.get().getKeyResult(), userDetails);
                    }

                    return new ResponseEntity<>("완료가 취소 되었습니다.", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("완료 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("삭제된 할일 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //특정 핵심결과 하위 투두 전체 카운트
    public int keyResultByAllToDoCount(KeyResult targetKeyResult) {
        return toDoRepository.findAllByKeyResultIdAndDeleteStateFalse(targetKeyResult).size();
    }

    //특정 핵심결과 하위 완료된 투두 카운트
    public int keyResultByCompletionToDoCount(KeyResult targetKeyResult) {
        return toDoRepository.findAllByKeyResultIdAndDeleteStateFalseAndCompletion(targetKeyResult).size();
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

    //할일 주간 전체 목록 조회
    public ResponseEntity<?> getAllWeekToDo(UserDetailsImpl userDetails, WeekToDoRequest weekToDoRequest) {
        List<LocalDate> existToDo = new ArrayList<>();
        for (int i = 0; i < weekToDoRequest.getTeamMembers().size(); i++) {
            LocalDate startDay = weekToDoRequest.getSunday();
            while (startDay.isEqual(weekToDoRequest.getSaturday()) || startDay.isBefore(weekToDoRequest.getSaturday())) {
                List<UserToDo> checkToDo = userToDoRepository.findAllByUserIdAndCheckDate(weekToDoRequest.getTeamMembers().get(i), startDay);
                if (!checkToDo.isEmpty()) {
                    existToDo.add(startDay);
                    startDay = startDay.plusDays(1);
                } else {
                    startDay = startDay.plusDays(1);
                }
            }
        }

        existToDo = existToDo.stream().distinct().collect(Collectors.toList());
        return new ResponseEntity<>(existToDo, HttpStatus.OK);
    }

    //할일 기간만료 목록 조회
    public ResponseEntity<?> getExpirationToDo(UserDetailsImpl userDetails, TeamMemberToDoRequest teamMemberToDoRequest) {
        List<User> selectedTeamMember = userRepository.findAllById(teamMemberToDoRequest.getTeamMembers());
        List<ToDoExpirationResponse> toDoExpirationResponseList = new ArrayList<>();
        for (int k = 0; k < selectedTeamMember.size(); k++) {
            List<UserToDo> teamToDoList
                    = userToDoRepository.findAllByUserIdAndCompletionFalseAndExpiration(
                    selectedTeamMember.get(k).getId(), teamMemberToDoRequest.getTargetDate(), LocalDate.now());

            List<UserToDo> resultTeamToDoList = new ArrayList<>();
            for (int i = 0; i < teamMemberToDoRequest.getKeyResultIds().size(); i++) {
                for (int j = 0; j < teamToDoList.size(); j++) {
                    Long temp = 0L;
                    if (teamToDoList.get(j).getKeyResult() != null) {
                        temp = teamToDoList.get(j).getKeyResult().getId();
                    }

                    if (Objects.equals(teamMemberToDoRequest.getKeyResultIds().get(i), temp)) {
                        resultTeamToDoList.add(teamToDoList.get(j));
                    }
                }
            }

            List<UserToDo> resultSortTeamToDoList = sort(resultTeamToDoList, teamMemberToDoRequest);

            List<ToDoExpirationResponse.expirationTodo> expirationTodoList = new ArrayList<>();
            for (int i = 0; i < resultSortTeamToDoList.size(); i++) {
                ToDoExpirationResponse.expirationTodo expirationTodo = ToDoExpirationResponse.expirationTodo.builder()
                        .keyResultId(resultSortTeamToDoList.get(i).getToDo().getKeyResult() != null ? resultSortTeamToDoList.get(i).getToDo().getKeyResult().getId() : null)
                        .krNumber(resultSortTeamToDoList.get(i).getToDo().getKeyResult() != null ? resultSortTeamToDoList.get(i).getToDo().getKeyResult().getKrNumber() : 0)
                        .toDoId(resultSortTeamToDoList.get(i).getToDo().getId())
                        .toDo(resultSortTeamToDoList.get(i).getToDo().getToDo())
                        .memo(resultSortTeamToDoList.get(i).getToDo().getMemo())
                        .startDate(resultSortTeamToDoList.get(i).getToDo().getStartDate())
                        .startDateTime(resultSortTeamToDoList.get(i).getToDo().getStartDateTime())
                        .endDate(resultSortTeamToDoList.get(i).getToDo().getEndDate())
                        .endDateTime(resultSortTeamToDoList.get(i).getToDo().getEndDateTime())
                        .fstartDate(resultSortTeamToDoList.get(i).getToDo().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .fendDate(resultSortTeamToDoList.get(i).getToDo().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .priority(resultSortTeamToDoList.get(i).getToDo().getPriority())
                        .completion(resultSortTeamToDoList.get(i).getToDo().isCompletion())
                        .color(resultSortTeamToDoList.get(i).getToDo().getObjective() != null ? resultSortTeamToDoList.get(i).getToDo().getObjective().getColor() : null)
                        .build();
                expirationTodoList.add(expirationTodo);
            }

            if (!expirationTodoList.isEmpty()) {
                ToDoExpirationResponse toDoExpirationResponse = ToDoExpirationResponse.builder()
                        .myToDo(selectedTeamMember.get(k).getId().equals(userDetails.getUser().getId()))
                        .userId(selectedTeamMember.get(k).getId())
                        .createUser(selectedTeamMember.get(k).getName())
                        .expirationTodo(expirationTodoList)
                        .build();

                toDoExpirationResponseList.add(toDoExpirationResponse);
            }
        }

        return new ResponseEntity<>(toDoExpirationResponseList, HttpStatus.OK);
    }

    //할일 진행 목록 조회
    public ResponseEntity<?> getProgressToDo(UserDetailsImpl userDetails, TeamMemberToDoRequest teamMemberToDoRequest) {
        List<User> selectedTeamMember = userRepository.findAllById(teamMemberToDoRequest.getTeamMembers());
        List<ToDoProgressResponse> toDoProgressResponseList = new ArrayList<>();
        for (int k = 0; k < selectedTeamMember.size(); k++) {
            List<UserToDo> teamToDoList
                    = userToDoRepository.findAllByUserIdAndCompletionFalseAndProgress(
                    selectedTeamMember.get(k).getId(), teamMemberToDoRequest.getTargetDate(), LocalDate.now());

            List<UserToDo> resultTeamToDoList = new ArrayList<>();
            for (int i = 0; i < teamMemberToDoRequest.getKeyResultIds().size(); i++) {
                for (int j = 0; j < teamToDoList.size(); j++) {
                    Long temp = 0L;
                    if (teamToDoList.get(j).getKeyResult() != null) {
                        temp = teamToDoList.get(j).getKeyResult().getId();
                    }

                    if (Objects.equals(teamMemberToDoRequest.getKeyResultIds().get(i), temp)) {
                        resultTeamToDoList.add(teamToDoList.get(j));
                    }
                }
            }

            List<UserToDo> resultSortTeamToDoList = sort(resultTeamToDoList, teamMemberToDoRequest);

            List<ToDoProgressResponse.progressTodo> progressTodoList = new ArrayList<>();
            for (int i = 0; i < resultSortTeamToDoList.size(); i++) {
                ToDoProgressResponse.progressTodo progressTodo = ToDoProgressResponse.progressTodo.builder()
                        .keyResultId(resultSortTeamToDoList.get(i).getToDo().getKeyResult() != null ? resultSortTeamToDoList.get(i).getToDo().getKeyResult().getId() : null)
                        .krNumber(resultSortTeamToDoList.get(i).getToDo().getKeyResult() != null ? resultSortTeamToDoList.get(i).getToDo().getKeyResult().getKrNumber() : 0)
                        .toDoId(resultSortTeamToDoList.get(i).getToDo().getId())
                        .toDo(resultSortTeamToDoList.get(i).getToDo().getToDo())
                        .memo(resultSortTeamToDoList.get(i).getToDo().getMemo())
                        .startDate(resultSortTeamToDoList.get(i).getToDo().getStartDate())
                        .startDateTime(resultSortTeamToDoList.get(i).getToDo().getStartDateTime())
                        .endDate(resultSortTeamToDoList.get(i).getToDo().getEndDate())
                        .endDateTime(resultSortTeamToDoList.get(i).getToDo().getEndDateTime())
                        .fstartDate(resultSortTeamToDoList.get(i).getToDo().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .fendDate(resultSortTeamToDoList.get(i).getToDo().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .priority(resultSortTeamToDoList.get(i).getToDo().getPriority())
                        .completion(resultSortTeamToDoList.get(i).getToDo().isCompletion())
                        .color(resultSortTeamToDoList.get(i).getToDo().getObjective() != null ? resultSortTeamToDoList.get(i).getToDo().getObjective().getColor() : null)
                        .build();
                progressTodoList.add(progressTodo);
            }

            if (!progressTodoList.isEmpty()) {
                ToDoProgressResponse toDoProgressResponse = ToDoProgressResponse.builder()
                        .myToDo(selectedTeamMember.get(k).getId().equals(userDetails.getUser().getId()))
                        .userId(selectedTeamMember.get(k).getId())
                        .createUser(selectedTeamMember.get(k).getName())
                        .progressTodo(progressTodoList)
                        .build();

                toDoProgressResponseList.add(toDoProgressResponse);
            }
        }

        return new ResponseEntity<>(toDoProgressResponseList, HttpStatus.OK);
    }

    //할일 완료 목록 조회
    public ResponseEntity<?> getCompletionToDo(UserDetailsImpl userDetails, TeamMemberToDoRequest teamMemberToDoRequest) {
        List<User> selectedTeamMember = userRepository.findAllById(teamMemberToDoRequest.getTeamMembers());
        List<ToDoCompletionResponse> toDoCompletionResponseList = new ArrayList<>();
        for (int k = 0; k < selectedTeamMember.size(); k++) {
            List<UserToDo> teamToDoList
                    = userToDoRepository.findAllByUserIdAndCompletionTrueAndCompletion(
                    selectedTeamMember.get(k).getId(), teamMemberToDoRequest.getTargetDate());

            List<UserToDo> resultTeamToDoList = new ArrayList<>();
            for (int i = 0; i < teamMemberToDoRequest.getKeyResultIds().size(); i++) {
                for (int j = 0; j < teamToDoList.size(); j++) {
                    Long temp = 0L;
                    if (teamToDoList.get(j).getKeyResult() != null) {
                        temp = teamToDoList.get(j).getKeyResult().getId();
                    }

                    if (Objects.equals(teamMemberToDoRequest.getKeyResultIds().get(i), temp)) {
                        resultTeamToDoList.add(teamToDoList.get(j));
                    }
                }
            }

            List<UserToDo> resultSortTeamToDoList = sort(resultTeamToDoList, teamMemberToDoRequest);

            List<ToDoCompletionResponse.completionTodo> completionTodoList = new ArrayList<>();
            for (int i = 0; i < resultSortTeamToDoList.size(); i++) {
                ToDoCompletionResponse.completionTodo completionTodo = ToDoCompletionResponse.completionTodo.builder()
                        .keyResultId(resultSortTeamToDoList.get(i).getToDo().getKeyResult() != null ? resultSortTeamToDoList.get(i).getToDo().getKeyResult().getId() : null)
                        .krNumber(resultSortTeamToDoList.get(i).getToDo().getKeyResult() != null ? resultSortTeamToDoList.get(i).getToDo().getKeyResult().getKrNumber() : 0)
                        .toDoId(resultSortTeamToDoList.get(i).getToDo().getId())
                        .toDo(resultSortTeamToDoList.get(i).getToDo().getToDo())
                        .memo(resultSortTeamToDoList.get(i).getToDo().getMemo())
                        .startDate(resultSortTeamToDoList.get(i).getToDo().getStartDate())
                        .startDateTime(resultSortTeamToDoList.get(i).getToDo().getStartDateTime())
                        .endDate(resultSortTeamToDoList.get(i).getToDo().getEndDate())
                        .endDateTime(resultSortTeamToDoList.get(i).getToDo().getEndDateTime())
                        .fstartDate(resultSortTeamToDoList.get(i).getToDo().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .fendDate(resultSortTeamToDoList.get(i).getToDo().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .priority(resultSortTeamToDoList.get(i).getToDo().getPriority())
                        .completion(resultSortTeamToDoList.get(i).getToDo().isCompletion())
                        .color(resultSortTeamToDoList.get(i).getToDo().getObjective() != null ? resultSortTeamToDoList.get(i).getToDo().getObjective().getColor() : null)
                        .build();
                completionTodoList.add(completionTodo);
            }

            if (!completionTodoList.isEmpty()) {
                ToDoCompletionResponse toDoCompletionResponse = ToDoCompletionResponse.builder()
                        .myToDo(selectedTeamMember.get(k).getId().equals(userDetails.getUser().getId()))
                        .userId(selectedTeamMember.get(k).getId())
                        .createUser(selectedTeamMember.get(k).getName())
                        .completionTodo(completionTodoList)
                        .build();

                toDoCompletionResponseList.add(toDoCompletionResponse);
            }
        }

        return new ResponseEntity<>(toDoCompletionResponseList, HttpStatus.OK);
    }

    //할일 목록 정렬
    private List<UserToDo> sort(List<UserToDo> resultTeamToDoList,
                                 TeamMemberToDoRequest teamMemberToDoRequest) {
        if (teamMemberToDoRequest.getOrderby().equals("priority")) {
            if (teamMemberToDoRequest.getOrderbyrole().equals("desc")) {
                resultTeamToDoList.sort(new PrioritySort().reversed());
            } else {
                resultTeamToDoList.sort(new PrioritySort());
            }
        } else if (teamMemberToDoRequest.getOrderby().equals("createdDate")) {
            if (teamMemberToDoRequest.getOrderbyrole().equals("desc")) {
                resultTeamToDoList.sort(new CreatedDateSort().reversed());
            } else {
                resultTeamToDoList.sort(new CreatedDateSort());
            }
        } else {
            if (teamMemberToDoRequest.getOrderbyrole().equals("desc")) {
                resultTeamToDoList.sort(new EndDateSort().reversed());
            } else {
                resultTeamToDoList.sort(new EndDateSort());
            }
        }

        return resultTeamToDoList;
    }

    //할일 대시보드 조회
    public ResponseEntity<?> getDashToDo(UserDetailsImpl userDetails) {
        List<Long> todoId = userToDoService.allToDo(userDetails.getUser().getId());
        List<ToDoResponse> dashToDoResponseList = new ArrayList<>();
        for (int n = 0; n < todoId.size(); n++) {
            Optional<ToDo> toDo = toDoRepository.findByIdAndDeleteStateFalse(todoId.get(n));
            if (toDo != null && toDo.get().getEndDate().isEqual(LocalDate.now())
                    && toDo.get().isCompletion()) {
                ToDoResponse dashToDoResponse = ToDoResponse.builder()
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
                dashToDoResponseList.add(dashToDoResponse);
            }
        }
        return new ResponseEntity<>(dashToDoResponseList, HttpStatus.OK);
    }
}

