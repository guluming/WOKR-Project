package com.slamdunk.WORK.repository;

import com.slamdunk.WORK.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
