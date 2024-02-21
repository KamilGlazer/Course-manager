package com.kamilglazer.coursesusers.repository;

import com.kamilglazer.coursesusers.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    boolean existsByUsername(String username);
    UserEntity findByUsername(String username);
}
