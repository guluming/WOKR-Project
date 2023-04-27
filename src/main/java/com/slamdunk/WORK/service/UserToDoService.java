package com.slamdunk.WORK.service;

import com.slamdunk.WORK.entity.*;
import com.slamdunk.WORK.repository.KeyResultRepository;
import com.slamdunk.WORK.repository.ObjectiveRepository;
import com.slamdunk.WORK.repository.ToDoRepository;
import com.slamdunk.WORK.repository.UserToDoRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class UserToDoService {
    private final ToDoRepository toDoRepository;
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final UserToDoRepository userToDoRepository;

    //회원-투두 중간테이블 생성
    @Transactional
    public void registerUserToDo(UserDetailsImpl userDetails, Objective objective, KeyResult keyResult, ToDo toDo) {
        if (objective == null && keyResult == null) {
            Optional<ToDo> toDoCheck = toDoRepository.findById(toDo.getId());

            if (toDoCheck.isPresent()) {
                UserToDo userToDo = new UserToDo(userDetails.getUser(), null, null, toDoCheck.get(), userDetails.getUser().getTeam());
                userToDoRepository.save(userToDo);
            }
        } else if (objective != null && keyResult != null) {
            Optional<Objective> objectiveCheck = objectiveRepository.findByIdAndDeleteStateFalse(objective.getId());
            Optional<KeyResult> keyResultCheck = keyResultRepository.findByIdAndDeleteStateFalse(keyResult.getId());
            Optional<ToDo> toDoCheck = toDoRepository.findById(toDo.getId());

            if (objectiveCheck.isPresent() && keyResultCheck.isPresent() && toDoCheck.isPresent()) {
                UserToDo userToDo = new UserToDo(userDetails.getUser(), objectiveCheck.get(), keyResultCheck.get(), toDoCheck.get(), userDetails.getUser().getTeam());
                userToDoRepository.save(userToDo);
            }
        }
    }

    //회원-투두 전체 조회
    public List<Long> allToDo(Long userId) {
        List<UserToDo> userToDoList = userToDoRepository.findAllByUserId(userId);

        List<Long> toDoId = new ArrayList<>();
        for (UserToDo userToDo : userToDoList) {
            Optional<ToDo> toDoDeleteCheck = toDoRepository.findByIdAndDeleteStateFalse(userToDo.getToDo().getId());
            if (toDoDeleteCheck.isPresent()) {
                toDoId.add(userToDo.getToDo().getId());
            }
        }
        return toDoId;
    }

    //회원-투두 상세 조회
    public boolean checkMyToDo(Long toDoId, UserDetailsImpl userDetails) {
        Optional<UserToDo> checkDate = userToDoRepository.findByToDoIdAndUserId(toDoId, userDetails.getUser().getId());
        return checkDate.isPresent();
    }

    //회원-할일 목표Id로 중간테이블 전체 조회
    public List<ToDo> checkToDoOfObjective(Long objectiveId, Long userId) {
        List<UserToDo> checkToDoOfObjectiveList = userToDoRepository.findAllByObjectiveIdAndUserId(objectiveId, userId);

        List<ToDo> ToDoOfObjectiveList = new ArrayList<>();
        if (!checkToDoOfObjectiveList.isEmpty()) {
            for (int i = 0; i < checkToDoOfObjectiveList.size(); i++) {
                ToDoOfObjectiveList.add(checkToDoOfObjectiveList.get(i).getToDo());
            }
        }

        return ToDoOfObjectiveList;
    }

    //회원-할일 핵심결과Id로 중간테이블 전체 조회
    public List<ToDo> checkToDoOfKeyResult(Long keyResultId, Long userId) {
        List<UserToDo> checkToDoOfKeyResultList = userToDoRepository.findAllByKeyResultIdAndUserId(keyResultId, userId);

        List<ToDo> ToDoOfKeyResultList = new ArrayList<>();
        if (!checkToDoOfKeyResultList.isEmpty()) {
            for (int i = 0; i < checkToDoOfKeyResultList.size(); i++) {
                ToDoOfKeyResultList.add(checkToDoOfKeyResultList.get(i).getToDo());
            }
        }

        return ToDoOfKeyResultList;
    }

    //회원-투두 로그인 유저 투두 확인
    public boolean checkUserMyToDo(Long toDoId, User user) {
        Optional<UserToDo> checkDate = userToDoRepository.findByToDoIdAndUserId(toDoId, user.getId());
        return checkDate.isPresent();
    }

    //회원-투두 생성 갯수 확인
    public int createToDoCount(Long userId) {
        List<UserToDo> userToDoList = userToDoRepository.findAllByUserIdAndCompletionFalseAndProgress(userId, LocalDate.now());
        return userToDoList.size();
    }

    //회원-투두 가장 첫번째 종료일 조회
    public UserToDo findFirstEndDate(User user) {
        List<UserToDo> userToDo = userToDoRepository.findByUserIdAndFirstEndDate(user.getId(), LocalDate.now());
        if (!userToDo.isEmpty()) {
            return userToDo.get(0);
        } else {
            return null;
        }
    }

    //회원-투두 가장 마지막 종료일 조회
    public UserToDo findLastEndDate(User user) {
        List<UserToDo> userToDo = userToDoRepository.findByUserIdAndLastEndDate(user.getId(), LocalDate.now());
        if (!userToDo.isEmpty()) {
            return userToDo.get(0);
        } else {
            return null;
        }
    }
}