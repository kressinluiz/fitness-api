package com.kressin.fitness_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kressin.fitness_app.entity.ExercisePlan;

public interface ExercisePlanRepository extends JpaRepository<ExercisePlan, Long> {
    @Query("""
                select max(ep.position)
                from ExercisePlan ep
                where ep.workout.id = :workoutId
            """)
    Optional<Integer> findMaxPosition(Long workoutId);
}
