package com.enigma.student_management.repository;

import com.enigma.student_management.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    // Check course by name
    boolean existsByName(String name);

    // Find course by name
    Optional<Course> findByName(String name);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ENROLLED'")
    long countEnrolledStudents(@Param("courseId") UUID courseId);
}
