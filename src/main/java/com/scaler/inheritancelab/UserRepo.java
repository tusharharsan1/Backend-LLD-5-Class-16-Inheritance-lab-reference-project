package com.scaler.inheritancelab;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, UUID> {
}
