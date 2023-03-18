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
    private final KeyResultRepository keyResultRepository;
    private final UserToDoRepository userToDoRepository;

    //회원-투두 중간테이블 생성
    @Transactional
    public void registerUserToDo(ToDo toDo,KeyResult keyResult, UserDetailsImpl userDetails) {
        Optional<ToDo> toDoCheck = toDoRepository.findById(toDo.getId());

        if (keyResult !=null && toDoCheck.isPresent()) {
            UserToDo userToDo = new UserToDo(userDetails.getUser(), null, toDo);
            userToDoRepository.save(userToDo);
        } else if (toDoCheck.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 투두입니다.");
        } else {
            UserToDo userToDo = new UserToDo(userDetails.getUser(), toDo);
            userToDoRepository.save(userToDo);
        }
    }

    //회원-투두 전체 조회
    public List<Long> allToDo(UserDetailsImpl userDetails) {
        List<UserToDo> userToDoList = userToDoRepository.findAllByUserId(userDetails.getUser().getId());

        List<Long> toDoId = new ArrayList<>();
        for (UserToDo userToDo : userToDoList) {
            toDoId.add(userToDo.getToDo().getId());
        }

        return toDoId;

    }

    //회원-투두 상세 조회
    public boolean checkMyToDo(Long toDoId, UserDetailsImpl userDetails) {
        Optional<UserToDo> checkDate = userToDoRepository.findByToDoIdAndUserId(toDoId, userDetails.getUser().getId());
        return checkDate.isPresent();
    }



}