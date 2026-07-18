package com.kressin.fitness_app.notification;

public final class RabbitConfig {
    public static final String EXCHANGE = "fitness.events";
    public static final String QUEUE_WORKOUT_SCHEDULED = "notification.workout-scheduled";
    public static final String ROUTING_KEY_WORKOUT_SCHEDULED = "workout.scheduled";

    private RabbitConfig() {
    }
}
