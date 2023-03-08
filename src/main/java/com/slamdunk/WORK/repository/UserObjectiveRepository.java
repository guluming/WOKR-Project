package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.UserObjective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserObjectiveRepository extends JpaRepository<UserObjective, Long> {
    List<UserObjective> findAllByUserId(Long userId);
}
