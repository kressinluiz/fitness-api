package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.kressin.fitness_app.dto.WorkoutResponse;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@Transactional
public class WorkoutControllerTest extends AbstractIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long exerciseId;
    private Long createdWorkoutID;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(post("/exercises")
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

        exerciseId = createdExercise.id();

        MvcResult workoutResult = mockMvc.perform(post("/workouts")
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

        createdWorkoutID = createdWorkout.id();
    }

    @Test
    void shouldCreateWorkout() throws Exception {
        mockMvc.perform(post("/workouts")
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
                .andExpect(jsonPath("$.name")
                        .value("Nome do Workout"));
    }

    @Test
    void shouldThrowWithBlankName() throws Exception {
        mockMvc.perform(post("/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"",
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
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowWithNullDescription() throws Exception {
        mockMvc.perform(post("/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Nome do Workout",
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
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetWorkout() throws Exception {
        mockMvc.perform(get("/workouts/" + createdWorkoutID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Nome do Workout"));
    }

    @Test
    void shouldThrowWithGetInvalidID() throws Exception {
        Long invalidID = createdWorkoutID + 1;
        mockMvc.perform(get("/workouts/" + invalidID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteWorkout() throws Exception {
        mockMvc.perform(delete("/workouts/" + createdWorkoutID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldThrowWithDeleteInvalidID() throws Exception {
        Long invalidID = createdWorkoutID + 1;
        mockMvc.perform(delete("/workouts/" + invalidID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateWorkout() throws Exception {
        mockMvc.perform(patch("/workouts/" + createdWorkoutID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Nome do Workout Atualizado"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Nome do Workout Atualizado"));
    }

    @Test
    void shouldThrowWithUpdateInvalidName() throws Exception {
        mockMvc.perform(patch("/workouts/" + createdWorkoutID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":""
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowWithUpdateInvalidWorkout() throws Exception {
        Long invalidID = createdWorkoutID + 1;

        mockMvc.perform(patch("/workouts/" + invalidID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name":"Nome Novo"
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateExercisePlansOrder() throws Exception {
        MvcResult workoutResult = mockMvc.perform(post("/workouts")
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
                                    },
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
                                    },
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
                        """.formatted(exerciseId, exerciseId, exerciseId)))
                .andExpect(status().isCreated())
                .andReturn();

        WorkoutResponse createdWorkout = objectMapper.readValue(
                workoutResult.getResponse().getContentAsString(),
                WorkoutResponse.class);

        for (int i = 0; i < createdWorkout.exercisePlans().size(); i++) {
            assertEquals(i + 1, createdWorkout.exercisePlans().get(i).position());
        }

        Long workoutId = createdWorkout.id();
        workoutResult = mockMvc.perform(patch("/workouts/" + workoutId + "/exercise-plans/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "exercisePlanIds":
                                [
                                    %d,
                                    %d,
                                    %d
                                ]
                            }
                        """.formatted(createdWorkout.exercisePlans().get(2).id(),
                        createdWorkout.exercisePlans().get(1).id(),
                        createdWorkout.exercisePlans().get(0).id())))
                .andExpect(status().isOk())
                .andReturn();

        WorkoutResponse updatedWorkout = objectMapper.readValue(
                workoutResult.getResponse().getContentAsString(),
                WorkoutResponse.class);
        for (int i = 0; i < updatedWorkout.exercisePlans().size(); i++) {
            assertEquals(updatedWorkout.exercisePlans().size() - i,
                    updatedWorkout.exercisePlans().get(i).position());
        }
    }
}
