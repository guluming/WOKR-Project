package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.UserObjective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserObjectiveRepository extends JpaRepository<UserObjective, Long> {
    List<UserObjective> findAllByTeam(String team);
    Optional<UserObjective> findByObjectiveIdAndUserId(Long ObjectiveId, Long userId);
}
