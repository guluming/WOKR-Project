package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
    @Query("SELECT k FROM KeyResult k inner join UserKeyResult ukr on k.id = ukr.keyResult.id WHERE k.id = :keyResultId AND ukr.team = :team AND k.deleteState = false")
    Optional<KeyResult> findByKeyResultIdAndTeam(@Param("keyResultId")Long keyResultId, @Param("team")String team);
    @Query("SELECT k FROM KeyResult k WHERE k.objective.id = :ObjectiveId AND k.deleteState = false")
    List<KeyResult> findAllByObjectiveId(@Param("ObjectiveId") Long ObjectiveId);
    @Query("SELECT k FROM KeyResult k WHERE k.id = :keyResultId AND k.deleteState = false")
    Optional<KeyResult> findByIdAndDeleteStateFalse(@Param("keyResultId")Long keyResultId);
}
