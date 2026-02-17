package com.enigma.student_management.repository;

import com.enigma.student_management.entities.Enrollment;
import com.enigma.student_management.entities.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    // Check double enrollment
    boolean existsByStudentIdAndCourseIdAndStatus(UUID studentId, UUID courseId, EnrollmentStatus status);

    // Get student's enrollment
    List<Enrollment> findByStudentIdOrderByCreatedAtDesc(UUID studentId);

    // Get enrollment for grade assignment
    Optional<Enrollment> findStudentIdAndCourseId(UUID studentId, UUID courseId);

    // Get enrollments by course
    List<Enrollment> findByCourseId(UUID courseId);
}
