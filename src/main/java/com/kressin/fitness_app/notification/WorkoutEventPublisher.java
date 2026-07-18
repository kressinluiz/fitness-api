package com.kressin.fitness_app.notification;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class WorkoutEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public WorkoutEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishWorkoutScheduled(WorkoutScheduledEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY_WORKOUT_SCHEDULED,
                event);
    }
}
