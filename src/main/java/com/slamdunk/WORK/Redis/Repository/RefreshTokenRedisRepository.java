package com.slamdunk.WORK.Redis.Repository;

import com.slamdunk.WORK.Redis.Entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {
}
