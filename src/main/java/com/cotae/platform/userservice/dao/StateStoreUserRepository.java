package com.cotae.platform.userservice.dao;

import com.cotae.platform.userservice.aggregate.StateStoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateStoreUserRepository extends JpaRepository<StateStoreUser, Long> {
}
