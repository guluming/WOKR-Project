package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    @Query("SELECT t FROM ToDo t WHERE t.id = :todoId AND t.deleteState = false")
    Optional<ToDo> findByIdAndDeleteStateFalse(@Param("todoId")Long todoId);
    Optional<ToDo> findByToDo(String toDo);
}