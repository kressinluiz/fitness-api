package com.kressin.fitness_app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.kressin.fitness_app.dto.ExerciseResponse;

import jakarta.transaction.Transactional;

@AutoConfigureMockMvc
@Transactional
public class ExerciseControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    @Test
    void shouldCreateExercise() throws Exception {
        mockMvc.perform(post("/exercises")
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
                .andExpect(jsonPath("$.name")
                        .value("Nome do Exercício"));
    }

    @Test
    void shouldThrowWithBlankName() throws Exception {
        mockMvc.perform(post("/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"",
                                "description":"Descrição do Exercício",
                                "category":"Categoria do Exercício",
                                "muscleGroup":"Grupo do Exercício"
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowWithNullDescription() throws Exception {
        mockMvc.perform(post("/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"",
                                "category":"Categoria do Exercício",
                                "muscleGroup":"Grupo do Exercício"
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetExercise() throws Exception {
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

        Long id = createdExercise.id();

        mockMvc.perform(get("/exercises/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Nome do Exercício"));
    }

    @Test
    void shouldThrowWithGetInvalidID() throws Exception {
        Long invalidID = 333L;
        mockMvc.perform(get("/exercises/" + invalidID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteExercise() throws Exception {
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

        Long id = createdExercise.id();

        mockMvc.perform(delete("/exercises/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldThrowWithDeleteInvalidID() throws Exception {
        Long invalidID = 333L;
        mockMvc.perform(delete("/exercises/" + invalidID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateExercise() throws Exception {
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

        Long id = createdExercise.id();

        mockMvc.perform(patch("/exercises/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Nome do Exercício Atualizado"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Nome do Exercício Atualizado"));
    }

    @Test
    void shouldThrowWithUpdateInvalidName() throws Exception {
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

        Long id = createdExercise.id();

        mockMvc.perform(patch("/exercises/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":""
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowWithUpdateInvalidExercise() throws Exception {
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

        Long id = createdExercise.id() + 1;

        mockMvc.perform(patch("/exercises/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name":"Nome Novo"
                            }
                        """))
                .andExpect(status().isNotFound());
    }

}
