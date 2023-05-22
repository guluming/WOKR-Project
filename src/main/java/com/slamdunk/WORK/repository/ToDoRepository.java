package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    @Query("SELECT t FROM ToDo t WHERE t.id = :todoId AND t.deleteState = false")
    Optional<ToDo> findByIdAndDeleteStateFalse(@Param("todoId")Long todoId);
    @Query("SELECT t FROM ToDo t WHERE t.keyResult.id = :keyResultId AND t.deleteState = false")
    List<ToDo> findAllByKeyResultIdAndDeleteStateFalse(@Param("keyResultId")Long keyResultId);
    @Query("SELECT t FROM ToDo t WHERE t.keyResult.id = :keyResultId AND t.deleteState = false AND t.completion = true")
    List<ToDo> findAllByKeyResultIdAndDeleteStateFalseAndCompletion(@Param("keyResultId")Long keyResultId);
}