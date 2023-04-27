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
    private final UserKeyResultService userKeyResultService;
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

    //할일 주간 전체 목록 조회
    public ResponseEntity<?> getAllWeekToDo(UserDetailsImpl userDetails, WeekToDoRequest weekToDoRequest) {
        List<LocalDate> existToDo = new ArrayList<>();
        for (int i = 0; i < weekToDoRequest.getTeamMembers().size(); i++) {
            LocalDate startDay = weekToDoRequest.getSunday();
            while (startDay.isEqual(weekToDoRequest.getSaturday()) || startDay.isBefore(weekToDoRequest.getSaturday())) {
                List<UserToDo> checkToDo = userToDoRepository.findAllByUserIdAndCheckDate(weekToDoRequest.getTeamMembers().get(i), startDay);
                if (!checkToDo.isEmpty()) {
                    existToDo.add(startDay);
                    startDay.plusDays(1);
                } else {
                    startDay.plusDays(1);
                }
            }
        }

        existToDo = existToDo.stream().distinct().collect(Collectors.toList());
        return new ResponseEntity<>(existToDo, HttpStatus.OK);
    }

    //할일 기간만료 목록 조회
    public ResponseEntity<?> getExpirationToDo(UserDetailsImpl userDetails, TeamMemberToDoRequest teamMemberToDoRequest) {
        List<User> selectedTeamMember = userRepository.findAllById(teamMemberToDoRequest.getTeamMembers());
        List<Long> nonSelectedKeyResultIdList = nonSelectedKeyResultIds(userDetails, teamMemberToDoRequest.getKeyResultIds());
        Sort sort = sortBy(teamMemberToDoRequest.getOrderby(), teamMemberToDoRequest.getOrderbyrole());
        List<ToDoExpirationResponse> toDoExpirationResponseList = new ArrayList<>();
        for (int k = 0; k < selectedTeamMember.size(); k++) {
            List<UserToDo> teamToDoList
                    = userToDoRepository.findAllByUserIdAndCompletionFalseAndExpiration(
                    selectedTeamMember.get(k).getId(), teamMemberToDoRequest.getTargetDate(), LocalDate.now(), sort);

            for (int i = 0; i < nonSelectedKeyResultIdList.size(); i++) {
                for (int j = 0; j < teamToDoList.size(); j++) {
                    if (Objects.equals(nonSelectedKeyResultIdList.get(i), teamToDoList.get(j).getKeyResult().getId())) {
                        teamToDoList.remove(teamToDoList.get(j));
                    }
                }
            }

            List<ToDoExpirationResponse.expirationTodo> expirationTodoList = new ArrayList<>();
            for (int i = 0; i < teamToDoList.size(); i++) {
                ToDoExpirationResponse.expirationTodo expirationTodo = ToDoExpirationResponse.expirationTodo.builder()
                        .keyResultId(teamToDoList.get(i).getToDo().getKeyResult() != null ? teamToDoList.get(i).getToDo().getKeyResult().getId() : null)
                        .krNumber(teamToDoList.get(i).getToDo().getKeyResult() != null ? teamToDoList.get(i).getToDo().getKeyResult().getKrNumber() : 0)
                        .toDoId(teamToDoList.get(i).getToDo().getId())
                        .toDo(teamToDoList.get(i).getToDo().getToDo())
                        .memo(teamToDoList.get(i).getToDo().getMemo())
                        .startDate(teamToDoList.get(i).getToDo().getStartDate())
                        .startDateTime(teamToDoList.get(i).getToDo().getStartDateTime())
                        .endDate(teamToDoList.get(i).getToDo().getEndDate())
                        .endDateTime(teamToDoList.get(i).getToDo().getEndDateTime())
                        .fstartDate(teamToDoList.get(i).getToDo().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .fendDate(teamToDoList.get(i).getToDo().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .priority(teamToDoList.get(i).getToDo().getPriority())
                        .completion(teamToDoList.get(i).getToDo().isCompletion())
                        .color(teamToDoList.get(i).getToDo().getObjective() != null ? teamToDoList.get(i).getToDo().getObjective().getColor() : null)
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
        List<Long> nonSelectedKeyResultIdList = nonSelectedKeyResultIds(userDetails, teamMemberToDoRequest.getKeyResultIds());
        Sort sort = sortBy(teamMemberToDoRequest.getOrderby(), teamMemberToDoRequest.getOrderbyrole());
        List<ToDoProgressResponse> toDoProgressResponseList = new ArrayList<>();
        for (int k = 0; k < selectedTeamMember.size(); k++) {
            List<UserToDo> teamToDoList
                    = userToDoRepository.findAllByUserIdAndCompletionFalseAndProgress(
                    selectedTeamMember.get(k).getId(), teamMemberToDoRequest.getTargetDate(), LocalDate.now(), sort);

            for (int i = 0; i < nonSelectedKeyResultIdList.size(); i++) {
                for (int j = 0; j < teamToDoList.size(); j++) {
                    if (Objects.equals(nonSelectedKeyResultIdList.get(i), teamToDoList.get(j).getKeyResult().getId())) {
                        teamToDoList.remove(teamToDoList.get(j));
                    }
                }
            }

            List<ToDoProgressResponse.progressTodo> progressTodoList = new ArrayList<>();
            for (int i = 0; i < teamToDoList.size(); i++) {
                ToDoProgressResponse.progressTodo progressTodo = ToDoProgressResponse.progressTodo.builder()
                        .keyResultId(teamToDoList.get(i).getToDo().getKeyResult() != null ? teamToDoList.get(i).getToDo().getKeyResult().getId() : null)
                        .krNumber(teamToDoList.get(i).getToDo().getKeyResult() != null ? teamToDoList.get(i).getToDo().getKeyResult().getKrNumber() : 0)
                        .toDoId(teamToDoList.get(i).getToDo().getId())
                        .toDo(teamToDoList.get(i).getToDo().getToDo())
                        .memo(teamToDoList.get(i).getToDo().getMemo())
                        .startDate(teamToDoList.get(i).getToDo().getStartDate())
                        .startDateTime(teamToDoList.get(i).getToDo().getStartDateTime())
                        .endDate(teamToDoList.get(i).getToDo().getEndDate())
                        .endDateTime(teamToDoList.get(i).getToDo().getEndDateTime())
                        .fstartDate(teamToDoList.get(i).getToDo().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .fendDate(teamToDoList.get(i).getToDo().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .priority(teamToDoList.get(i).getToDo().getPriority())
                        .completion(teamToDoList.get(i).getToDo().isCompletion())
                        .color(teamToDoList.get(i).getToDo().getObjective() != null ? teamToDoList.get(i).getToDo().getObjective().getColor() : null)
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
        List<Long> nonSelectedKeyResultIdList = nonSelectedKeyResultIds(userDetails, teamMemberToDoRequest.getKeyResultIds());
        Sort sort = sortBy(teamMemberToDoRequest.getOrderby(), teamMemberToDoRequest.getOrderbyrole());
        List<ToDoCompletionResponse> toDoCompletionResponseList = new ArrayList<>();
        for (int k = 0; k < selectedTeamMember.size(); k++) {
            List<UserToDo> teamToDoList
                    = userToDoRepository.findAllByUserIdAndCompletionTrueAndCompletion(
                    selectedTeamMember.get(k).getId(), teamMemberToDoRequest.getTargetDate(), sort);

            for (int i = 0; i < nonSelectedKeyResultIdList.size(); i++) {
                for (int j = 0; j < teamToDoList.size(); j++) {
                    if (Objects.equals(nonSelectedKeyResultIdList.get(i), teamToDoList.get(j).getKeyResult().getId())) {
                        teamToDoList.remove(teamToDoList.get(j));
                    }
                }
            }

            List<ToDoCompletionResponse.completionTodo> completionTodoList = new ArrayList<>();
            for (int i = 0; i < teamToDoList.size(); i++) {
                ToDoCompletionResponse.completionTodo completionTodo = ToDoCompletionResponse.completionTodo.builder()
                        .keyResultId(teamToDoList.get(i).getToDo().getKeyResult() != null ? teamToDoList.get(i).getToDo().getKeyResult().getId() : null)
                        .krNumber(teamToDoList.get(i).getToDo().getKeyResult() != null ? teamToDoList.get(i).getToDo().getKeyResult().getKrNumber() : 0)
                        .toDoId(teamToDoList.get(i).getToDo().getId())
                        .toDo(teamToDoList.get(i).getToDo().getToDo())
                        .memo(teamToDoList.get(i).getToDo().getMemo())
                        .startDate(teamToDoList.get(i).getToDo().getStartDate())
                        .startDateTime(teamToDoList.get(i).getToDo().getStartDateTime())
                        .endDate(teamToDoList.get(i).getToDo().getEndDate())
                        .endDateTime(teamToDoList.get(i).getToDo().getEndDateTime())
                        .fstartDate(teamToDoList.get(i).getToDo().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .fendDate(teamToDoList.get(i).getToDo().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
                        .priority(teamToDoList.get(i).getToDo().getPriority())
                        .completion(teamToDoList.get(i).getToDo().isCompletion())
                        .color(teamToDoList.get(i).getToDo().getObjective() != null ? teamToDoList.get(i).getToDo().getObjective().getColor() : null)
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
    private Sort sortBy(String orderBy, String orderByRole) {
        if (orderByRole.equals("asc")) {
            return Sort.by(Sort.Direction.ASC, orderBy);
        } else {
            return Sort.by(Sort.Direction.DESC, orderBy);
        }
    }

    //핵심결과 목록 선택 제외
    private List<Long> nonSelectedKeyResultIds(UserDetailsImpl userDetails, List<Long> KeyResultIds) {
        List<Long> AllKeyResultIds = new ArrayList<>();
        if (KeyResultIds.size() != 0) {
            AllKeyResultIds = userKeyResultService.allKeyResult(userDetails);
            AllKeyResultIds.removeAll(KeyResultIds);
        }
        return AllKeyResultIds;
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
//    //할일 대시보드 조회 (투두 설정 날짜 전체 )
//    public ResponseEntity<?> getDashToDo(UserDetailsImpl userDetails) {
//        List<Long> todoId = userToDoService.allToDo(userDetails);
//        List<ToDoResponse> dashToDoResponseList = new ArrayList<>();
//        for (int n = 0; n < todoId.size(); n++) {
//            Optional<ToDo> toDo = toDoRepository.findByIdAndDeleteStateFalse(todoId.get(n));
//            if (toDo != null && toDo.get().getEndDate().isAfter(LocalDate.now()) &&
//                    toDo.get().getStartDate().isBefore(LocalDate.now().plusDays(1)) &&
//                    toDo.get().isCompletion()) {
//                ToDoResponse dashToDoResponse = ToDoResponse.builder()
//                        .myToDo(userToDoService.checkMyToDo(toDo.get().getId(), userDetails))
//                        .keyResultId(toDo.get().getKeyResult() != null ? toDo.get().getKeyResult().getId() : null)
//                        .krNumber(toDo.get().getKeyResult() != null ? toDo.get().getKeyResult().getKrNumber() : 0)
//                        .toDoId(toDo.get().getId())
//                        .toDo(toDo.get().getToDo())
//                        .memo(toDo.get().getMemo())
//                        .startDate(toDo.get().getStartDate())
//                        .startDateTime(toDo.get().getStartDateTime())
//                        .endDate(toDo.get().getEndDate())
//                        .endDateTime(toDo.get().getEndDateTime())
//                        .fstartDate(toDo.get().getStartDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
//                        .fendDate(toDo.get().getEndDate().format(DateTimeFormatter.ofPattern("MM월 dd일")))
//                        .priority(toDo.get().getPriority())
//                        .completion(toDo.get().isCompletion())
//                        .color(toDo.get().getObjective() != null ? toDo.get().getObjective().getColor() : null)
//                        .build();
//                dashToDoResponseList.add(dashToDoResponse);
//            }
//        }
//        return new ResponseEntity<>(dashToDoResponseList, HttpStatus.OK);
//    }
}

