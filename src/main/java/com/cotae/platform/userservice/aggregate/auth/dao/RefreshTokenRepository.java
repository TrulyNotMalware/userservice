package com.cotae.platform.userservice.aggregate.auth.dao;

import com.cotae.platform.userservice.aggregate.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
}
