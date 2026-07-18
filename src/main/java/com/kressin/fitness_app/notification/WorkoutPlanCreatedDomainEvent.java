package com.kressin.fitness_app.notification;

import java.time.ZonedDateTime;
import java.util.UUID;

public record WorkoutPlanCreatedDomainEvent(Long workoutPlanId, UUID messageId, ZonedDateTime scheduledAt) {
}
