package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.UserKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserKeyResultRepository extends JpaRepository<UserKeyResult, Long> {
    List<UserKeyResult> findAllByTeam(String team);
    Optional<UserKeyResult> findByKeyResultIdAndUserId(Long KeyResultId, Long userId);
}
