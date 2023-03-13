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

    //회원-핵심결과 중간테이블 생성
    @Transactional
    public void registerUserToDo(ToDo toDo, KeyResult keyResult, Objective objective, UserDetailsImpl userDetails) {
        Optional<Objective> objectiveCheck = objectiveRepository.findById(objective.getId());
        Optional<KeyResult> keyResultCheck = keyResultRepository.findById(keyResult.getId());
        Optional<ToDo> toDoCheck = toDoRepository.findById(toDo.getId());

        if (objectiveCheck.isPresent() && keyResultCheck.isPresent() && toDoCheck.isPresent()) {
            UserToDo userToDo = new UserToDo(userDetails.getUser(), objectiveCheck.get(), keyResultCheck.get(), toDo);
            userToDoRepository.save(userToDo);
        }
    }

    //회원-투두 전체 조회
    public List<Long> allToDo(UserDetailsImpl userDetails) {
        List<UserToDo> userToDoList = userToDoRepository.findAllByUserId(userDetails.getUser().getId());

        List<Long> toDoId = new ArrayList<>();
        for (int i = 0; i < userToDoList.size(); i++) {
            toDoId.add(userToDoList.get(i).getToDo().getId());
        }

        return toDoId;

    }

    //회원-투두 상세 조회
    public boolean checkMyToDo(Long toDoId, UserDetailsImpl userDetails) {
        Optional<UserToDo> checkDate = userToDoRepository.findByToDoIdAndUserId(toDoId, userDetails.getUser().getId());
        return checkDate.isPresent();
    }


}