package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
    List<KeyResult> findAllByObjectiveId(Long ObjectiveId);
    @Query("SELECT k FROM KeyResult k WHERE k.id = :keyResultId AND k.deleteState = false")
    Optional<KeyResult> findByIdAndDeleteStateFalse(@Param("keyResultId")Long keyResultId);
}
