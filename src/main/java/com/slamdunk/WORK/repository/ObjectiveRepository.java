package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    Optional<Objective> findByObjective(String objective);
}
