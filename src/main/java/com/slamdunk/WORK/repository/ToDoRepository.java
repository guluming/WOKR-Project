package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    Optional<ToDo> findByToDo(String toDo);
}