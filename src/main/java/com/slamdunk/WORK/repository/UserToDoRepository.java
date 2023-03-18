package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.UserToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserToDoRepository extends JpaRepository<UserToDo, Long> {
        List<UserToDo> findAllByUserId(Long userId);
        Optional<UserToDo> findByToDoIdAndUserId(Long ToDoId, Long userId);

        }

