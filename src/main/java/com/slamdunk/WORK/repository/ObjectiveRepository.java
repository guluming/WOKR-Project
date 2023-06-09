package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.Objective;
import com.slamdunk.WORK.entity.UserObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    @Query("SELECT o FROM Objective o inner join UserObjective uo on o.id = uo.objective.id WHERE o.id = :ObjectiveId AND uo.team = :team AND o.deleteState = false ")
    Optional<Objective> findByObjectiveIdAndTeam(@Param("ObjectiveId") Long ObjectiveId, @Param("team") String team);
    @Query("SELECT o FROM Objective o WHERE o.id = :objectiveId AND o.deleteState = false")
    Optional<Objective> findByIdAndDeleteStateFalse(@Param("objectiveId")Long objectiveId);
}
