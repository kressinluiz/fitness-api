ALTER TABLE exercise_plan
ADD COLUMN position INTEGER;

ALTER TABLE exercise_plan
ALTER COLUMN position SET NOT NULL;

ALTER TABLE exercise_plan
ADD CONSTRAINT uk_workout_position
UNIQUE(workout_id, position);
