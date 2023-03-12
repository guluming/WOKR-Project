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

import java.util.Optional;
@RequiredArgsConstructor
@Service
public class UserToDoService {
    private final ToDoRepository toDoRepository;
    private final KeyResultRepository keyResultRepository;
    private final ObjectiveRepository objectiveRepository;
    private  final ToDoService toDoService;
    private final UserToDoRepository userToDoRepository;


    @Transactional
    public void createUserToDo(ToDo ToDo, UserDetailsImpl userDetails) {
    }


    public boolean checkMyToDo(Long toDoId, UserDetailsImpl userDetails) {
        Optional<UserToDo> checkDate = userToDoRepository.findByKeyResultIdAndUserId(toDoId, userDetails.getUser().getId());
        return checkDate.isPresent();
    }
    }
