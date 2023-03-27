package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.UserObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserObjectiveRepository extends JpaRepository<UserObjective, Long> {
    @Query("SELECT uo FROM UserObjective uo inner join Objective o on uo.objective.id = o.id WHERE uo.team = :team AND o.deleteState = false order by o.endDate")
    List<UserObjective> findAllByTeam(@Param("team") String team);
    Optional<UserObjective> findByObjectiveIdAndUserId(Long ObjectiveId, Long userId);
}
