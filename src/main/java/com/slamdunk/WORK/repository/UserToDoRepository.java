package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.UserToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserToDoRepository extends JpaRepository<UserToDo, Long> {
        List<UserToDo> findAllByUserId(Long userId);
        Optional<UserToDo> findByToDoIdAndUserId(Long ToDoId, Long userId);
        List<UserToDo> findAllByKeyResult(KeyResult keyResult);
        Optional<UserToDo> findByToDoId(Long ToDoId);
        @Query("select ut from UserToDo ut inner join ToDo t on ut.toDo.id = t.id where ut.user.id = :userId AND t.completion = false AND t.endDate >= :today order by t.endDate")
        List<UserToDo> findAllByUserIdAndCompletionFalseAndProgress(@Param("userId") Long userId, @Param("today")LocalDate today);
        @Query("select ut from UserToDo ut inner join ToDo t on ut.toDo.id = t.id where ut.user.id = :userId AND t.completion = true AND t.endDate >= :today order by t.endDate")
        List<UserToDo> findAllByUserIdAndCompletionTrueAndCompletion(@Param("userId") Long userId, @Param("today")LocalDate today);
        @Query("select ut from UserToDo ut inner join ToDo t on ut.toDo.id = t.id where ut.user.id = :userId AND t.endDate >= :today order by t.endDate desc")
        List<UserToDo> findByUserIdAndLastEndDate(@Param("userId") Long userId, @Param("today")LocalDate today);
}

