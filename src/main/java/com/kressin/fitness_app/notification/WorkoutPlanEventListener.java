package com.kressin.fitness_app.notification;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class WorkoutPlanEventListener {
    private final WorkoutEventPublisher rabbitPublisher;

    public WorkoutPlanEventListener(WorkoutEventPublisher rabbitPublisher) {
        this.rabbitPublisher = rabbitPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onWorkoutPlanCreated(WorkoutPlanCreatedDomainEvent event) {
        rabbitPublisher.publishWorkoutScheduled(
                new WorkoutScheduledEvent(event.workoutPlanId(), event.messageId(), event.scheduledAt()));
    }
}
