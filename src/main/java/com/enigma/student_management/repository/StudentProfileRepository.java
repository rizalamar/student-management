package com.enigma.student_management.repository;

import com.enigma.student_management.entities.StudentProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID> {
    // Find student by userId
    Optional<StudentProfile> findByUserId(UUID userId);
    // Check email uniqueness
    boolean existsByEmail(String email);

    // Search students with pagination for Admin only
     @Query("SELECT s FROM StudentProfile s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<StudentProfile> searchStudents(@Param("keyword") String keyword, Pageable pageable);
}
