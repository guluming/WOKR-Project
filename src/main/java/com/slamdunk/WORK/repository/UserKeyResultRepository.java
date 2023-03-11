package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.UserKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserKeyResultRepository extends JpaRepository<UserKeyResult, Long> {
    List<UserKeyResult> findAllByUserId(Long userId);
}
