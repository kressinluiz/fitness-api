package com.kressin.fitness_app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kressin.fitness_app.entity.WorkoutPlan;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    @Query("""
            SELECT wp
            FROM WorkoutPlan wp
            JOIN wp.workout w
            WHERE LOWER(w.name)
            LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<WorkoutPlan> search(
            String search,
            Pageable pageable);
}
