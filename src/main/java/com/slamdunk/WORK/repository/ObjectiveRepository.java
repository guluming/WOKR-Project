package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    Optional<Objective> findByObjective(String objective);
    @Query("SELECT o FROM Objective o WHERE o.id = :objectiveId AND o.deleteState = false")
    Optional<Objective> findByIdAndDeleteStateFalse(@Param("objectiveId")Long objectiveId);
}
