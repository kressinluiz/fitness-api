package com.kressin.fitness_app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
public class DashboardControllerTest extends AbstractIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Long workoutPlanId;
    String workoutName;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult exerciseResult = mockMvc.perform(post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Exercício Dashboard",
                                "description":"Descrição",
                                "category":"Categoria",
                                "muscleGroup":"Grupo"
                            }
                        """))
                .andExpect(status().isCreated())
                .andReturn();

        ExerciseResponse createdExercise = objectMapper.readValue(
                exerciseResult.getResponse().getContentAsString(),
                ExerciseResponse.class);

        MvcResult workoutResult = mockMvc.perform(post("/api/v1/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Treino Dashboard",
                                "description":"Descrição do Treino",
                                "exercisePlans":[
                                    {
                                        "exerciseId":%d,
                                        "sets": [
                                            {
                                                "reps":10,
                                                "weight":50
                                            }
                                        ]
                                    }
                                ]
                            }
                        """.formatted(createdExercise.id())))
                .andExpect(status().isCreated())
                .andReturn();

        WorkoutResponse createdWorkout = objectMapper.readValue(
                workoutResult.getResponse().getContentAsString(),
                WorkoutResponse.class);

        workoutName = createdWorkout.name();

        String pastDate = formatDate(ZonedDateTime.now());
        String futureDate1 = formatDate(ZonedDateTime.now().plusDays(1));
        String futureDate2 = formatDate(ZonedDateTime.now().plusDays(2));
        String futureDate3 = formatDate(ZonedDateTime.now().plusDays(3));
        String futureDate4 = formatDate(ZonedDateTime.now().plusDays(4));
        String futureDate5 = formatDate(ZonedDateTime.now().plusDays(5));
        String futureDate6 = formatDate(ZonedDateTime.now().plusDays(6));

        MvcResult workoutPlanResult = mockMvc.perform(post("/api/v1/planner")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Workout Plan de Teste",
                                "workoutId":%d,
                                "workoutDate":
                                {
                                    "scheduleType": "RECURRING",
                                    "scheduleEntries":
                                    [
                                        {
                                            "weekDay":0,
                                            "dateTime":"%s"
                                        },
                                        {
                                            "weekDay":1,
                                            "dateTime":"%s"
                                        },
                                        {
                                            "weekDay":2,
                                            "dateTime":"%s"
                                        },
                                        {
                                            "weekDay":3,
                                            "dateTime":"%s"
                                        },
                                        {
                                            "weekDay":4,
                                            "dateTime":"%s"
                                        },
                                        {
                                            "weekDay":5,
                                            "dateTime":"%s"
                                        },
                                        {
                                            "weekDay":6,
                                            "dateTime":"%s"
                                        }
                                    ]
                                }
                            }
                        """.formatted(
                        createdWorkout.id(),
                        pastDate,
                        futureDate1,
                        futureDate2,
                        futureDate3,
                        futureDate4,
                        futureDate5,
                        futureDate6)))
                .andExpect(status().isCreated())
                .andReturn();

        WorkoutPlanResponse createdWorkoutPlan = objectMapper.readValue(
                workoutPlanResult.getResponse().getContentAsString(),
                WorkoutPlanResponse.class);

        workoutPlanId = createdWorkoutPlan.id();
    }

    @Test
    void shouldGetDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/summary")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exerciseCount").value(1))
                .andExpect(jsonPath("$.workoutCount").value(1))
                .andExpect(jsonPath("$.workoutPlanCount").value(1))
                .andExpect(jsonPath("$.upcomingWorkouts.length()").value(5))
                .andExpect(jsonPath("$.upcomingWorkouts[0].workoutPlanId").value(workoutPlanId))
                .andExpect(jsonPath("$.upcomingWorkouts[0].workoutName").value(workoutName))
                .andExpect(jsonPath("$.upcomingWorkouts[0].scheduleType").value("RECURRING"))
                .andExpect(jsonPath("$.upcomingWorkouts[0].weekDay").exists())
                .andExpect(jsonPath("$.upcomingWorkouts[0].dateTime").exists())
                .andExpect(jsonPath("$.upcomingWorkouts[4].dateTime").exists());
    }

    private static String formatDate(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
