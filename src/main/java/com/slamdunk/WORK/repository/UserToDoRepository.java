package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.KeyResult;
import com.slamdunk.WORK.entity.UserToDo;
import org.springframework.data.domain.Sort;
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
        @Query("select ut from UserToDo ut inner join Objective o on ut.objective.id = o.id where ut.objective.id = :objectiveId AND ut.user.id = :userId AND o.deleteState = false")
        List<UserToDo> findAllByObjectiveIdAndUserId(@Param("objectiveId") Long objectiveId, @Param("userId") Long userId);
        @Query("select ut from UserToDo ut inner join KeyResult kr on ut.keyResult.id = kr.id where ut.keyResult.id = :keyResultId AND ut.user.id = :userId AND kr.deleteState = false")
        List<UserToDo> findAllByKeyResultIdAndUserId(@Param("keyResultId") Long keyResultId, @Param("userId") Long userId);
        @Query("select ut from UserToDo ut inner join ToDo t on ut.toDo.id = t.id where ut.user.id = :userId AND t.deleteState = false AND t.completion = false AND t.endDate >= :today order by t.endDate")
        List<UserToDo> findAllByUserIdAndCompletionFalseAndProgress(@Param("userId") Long userId, @Param("today")LocalDate today);
        @Query("select ut from ToDo t inner join UserToDo ut on ut.toDo.id = t.id where ut.user.id = :userId AND t.deleteState = false AND t.completion = false AND t.endDate >= :targetDay AND t.startDate <= :targetDay AND t.endDate >= :today")
        List<UserToDo> findAllByUserIdAndCompletionFalseAndProgress(@Param("userId") Long userId, @Param("targetDay")LocalDate targetDay, @Param("today")LocalDate today, Sort sort);
        @Query("select ut from ToDo t inner join UserToDo ut on ut.toDo.id = t.id where ut.user.id = :userId AND t.deleteState = false AND t.completion = false AND t.endDate >= :targetDay AND t.startDate <= :targetDay AND t.endDate < :today")
        List<UserToDo> findAllByUserIdAndCompletionFalseAndExpiration(@Param("userId") Long userId, @Param("targetDay")LocalDate targetDay, @Param("today")LocalDate today, Sort sort);
        @Query("select ut from ToDo t inner join UserToDo ut on ut.toDo.id = t.id where ut.user.id = :userId AND t.deleteState = false AND t.completion = true AND t.endDate >= :targetDay AND t.startDate <= :targetDay")
        List<UserToDo> findAllByUserIdAndCompletionTrueAndCompletion(@Param("userId") Long userId, @Param("targetDay")LocalDate targetDay, Sort sort);
        @Query("select ut from UserToDo ut inner join ToDo t on ut.toDo.id = t.id where ut.user.id = :userId AND t.deleteState = false AND t.endDate >= :today order by t.endDate desc")
        List<UserToDo> findByUserIdAndLastEndDate(@Param("userId") Long userId, @Param("today")LocalDate today);
        @Query("select ut from UserToDo ut inner join ToDo t on ut.toDo.id = t.id where ut.user.id = :userId AND t.deleteState = false AND t.completion = true AND t.endDate < :today order by t.endDate")
        List<UserToDo> findByUserIdAndFirstEndDate(@Param("userId") Long userId, @Param("today")LocalDate today);
        @Query("select ut from UserToDo ut inner join ToDo t on ut.toDo.id = t.id where ut.user.id = :userId AND t.deleteState = false AND t.endDate >= :targetDay AND t.startDate <= :targetDay")
        List<UserToDo> findAllByUserIdAndCheckDate(@Param("userId") Long userId, @Param("targetDay")LocalDate targetDay);
        List<UserToDo> findAllByUserIdIn(List<Long> teamMemberIds);
}

