package com.kressin.fitness_app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.kressin.fitness_app.dto.ExerciseResponse;
import com.kressin.fitness_app.dto.WorkoutPlanResponse;
import com.kressin.fitness_app.dto.WorkoutResponse;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@Transactional
public class WorkoutPlanControllerTest extends AbstractIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Long createdWorkoutPlanID;
    WorkoutPlanResponse createdWorkoutPlan;
    Long workoutId;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Nome do Exercício",
                                "description":"Descrição do Exercício",
                                "category":"Categoria do Exercício",
                                "muscleGroup":"Grupo do Exercício"
                            }
                        """))
                .andExpect(status().isCreated())
                .andReturn();

        ExerciseResponse createdExercise = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ExerciseResponse.class);

        Long exerciseId = createdExercise.id();

        MvcResult workoutResult = mockMvc.perform(post("/api/v1/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Nome do Workout",
                                "description":"Descrição do Workout",
                                "exercisePlans":[
                                    {
                                        "exerciseId":%d,
                                        "sets": [
                                            {
                                                "reps":10,
                                                "weight":50
                                            },
                                            {
                                                "reps":8,
                                                "weight":60
                                            },
                                            {
                                                "reps":6,
                                                "weight":70
                                            }
                                        ]
                                    }
                                ]
                            }
                        """.formatted(exerciseId)))
                .andExpect(status().isCreated())
                .andReturn();

        WorkoutResponse createdWorkout = objectMapper.readValue(
                workoutResult.getResponse().getContentAsString(),
                WorkoutResponse.class);

        workoutId = createdWorkout.id();

        MvcResult workoutPlanResult = mockMvc.perform(post("/api/v1/planner")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Nome do Workout Plan",
                                "workoutId":%d,
                                "workoutDate":
                                {
                                    "scheduleType": "RECURRING",
                                    "scheduleEntries":
                                    [
                                        {
                                            "weekDay":1,
                                            "dateTime":"2026-07-11T13:48:54.365Z"
                                        },
                                        {
                                            "weekDay":2,
                                            "dateTime":"2026-07-11T13:48:54.365Z"
                                        },
                                        {
                                            "weekDay":3,
                                            "dateTime":"2026-07-11T13:48:54.365Z"
                                        }
                                    ]
                                }
                            }
                        """.formatted(workoutId)))
                .andExpect(status().isCreated())
                .andReturn();

        createdWorkoutPlan = objectMapper.readValue(
                workoutPlanResult.getResponse().getContentAsString(),
                WorkoutPlanResponse.class);

        createdWorkoutPlanID = createdWorkoutPlan.id();
    }

    @Test
    void shouldCreateWorkoutPlan() throws Exception {
        mockMvc.perform(post("/api/v1/planner")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "workoutId":%d,
                                "workoutDate":
                                {
                                    "scheduleType": "RECURRING",
                                    "scheduleEntries":
                                    [
                                        {
                                            "weekDay":1,
                                            "dateTime":"2026-07-11T13:48:54.365Z"
                                        },
                                        {
                                            "weekDay":2,
                                            "dateTime":"2026-07-11T13:48:54.365Z"
                                        },
                                        {
                                            "weekDay":3,
                                            "dateTime":"2026-07-11T13:48:54.365Z"
                                        }
                                    ]
                                }
                            }
                        """.formatted(workoutId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.workout.name")
                        .value("Nome do Workout"));
    }

    @Test
    void shouldThrowWithInvalidWorkoutID() throws Exception {
        Long invalidID = workoutId + 1;
        mockMvc.perform(post("/api/v1/planner")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "workoutId":%d,
                                "workoutDate":
                                {
                                    "scheduleType": "RECURRING",
                                    "scheduleEntries":
                                    [
                                        {
                                            "weekDay":1,
                                            "dateTime":"2026-07-11T13:48:54.365Z"
                                        },
                                        {
                                            "weekDay":2,
                                            "dateTime":"2026-07-11T13:48:54.365Z"
                                        },
                                        {
                                            "weekDay":3,
                                            "dateTime":"2026-07-11T13:48:54.365Z"
                                        }
                                    ]
                                }
                            }
                        """.formatted(invalidID)));
    }

    @Test
    void shouldThrowWithNullWorkoutDate() throws Exception {
        mockMvc.perform(post("/api/v1/planner")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "workoutId":%d
                            }
                        """.formatted(workoutId)));
    }

    @Test
    void shouldGetWorkout() throws Exception {
        mockMvc.perform(get("/api/v1/planner/" + createdWorkoutPlanID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workout.name")
                        .value("Nome do Workout"));
    }

    @Test
    void shouldThrowWithGetInvalidID() throws Exception {
        Long invalidID = createdWorkoutPlanID + 1;
        mockMvc.perform(get("/api/v1/planner/" + invalidID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteWorkout() throws Exception {
        mockMvc.perform(delete("/api/v1/planner/" + createdWorkoutPlanID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldThrowWithDeleteInvalidID() throws Exception {
        Long invalidID = createdWorkoutPlanID + 1;
        mockMvc.perform(delete("/api/v1/planner/" + invalidID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateWorkoutPlan() throws Exception {
        mockMvc.perform(patch("/api/v1/planner/" + createdWorkoutPlanID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "workoutDate":
                                {
                                    "id":%d,
                                    "scheduleType": "SPECIFIC_DATES"
                                }
                            }
                        """.formatted(createdWorkoutPlan.workoutDate().id())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutDate.scheduleType")
                        .value("SPECIFIC_DATES"));
    }

    @Test
    void shouldThrowWithUpdateInvalidWorkoutID() throws Exception {
        Long randomID = createdWorkoutPlanID + 1;
        mockMvc.perform(patch("/api/v1/planner/" + createdWorkoutPlanID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "workoutId":%d
                        }
                        """.formatted(randomID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowWithUpdateInvalidWorkoutPlan() throws Exception {
        Long invalidID = createdWorkoutPlanID + 1;

        mockMvc.perform(patch("/api/v1/planner/" + invalidID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "workoutId": 5
                            }
                        """))
                .andExpect(status().isNotFound());
    }

}
