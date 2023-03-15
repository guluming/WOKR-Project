package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
    List<KeyResult> findAllByObjectiveId(Long ObjectiveId);
}
