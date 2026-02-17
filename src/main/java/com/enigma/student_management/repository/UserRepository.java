package com.enigma.student_management.repository;

import com.enigma.student_management.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Method Query for login
    Optional<User> findByUsername(String username);

    // Method query for registration validate
    boolean existsByUsername(String username);
}
