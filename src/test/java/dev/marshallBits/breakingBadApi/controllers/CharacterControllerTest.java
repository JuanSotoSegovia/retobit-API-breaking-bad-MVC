package dev.marshallBits.breakingBadApi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marshallBits.breakingBadApi.dto.CreateCharacterDTO;
import dev.marshallBits.breakingBadApi.models.CharacterStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CharacterControllerTest {
    // MockMVC (Modelo Vista Controlador)
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("GET: Recibimos 10 characters en api/caracters")
    void getAllCharacters() throws Exception {
       mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    @DisplayName("POST: un nuevo character funciona correctamente en api/characters")
    void postNewCharacter() throws Exception{
        CreateCharacterDTO saul = CreateCharacterDTO
                .builder()
                .name("Saul Goodman")
                .occupation("Lawyer")
                .status(CharacterStatus.ALIVE)
                .build();

        mockMvc.perform(post("/api/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saul))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Saul Goodman"))
                .andExpect(jsonPath("$.occupation").value("Lawyer"));


    }


    //Tarea 1: GET /api/characters/{id}
    @Test
    @DisplayName("GET: Recibimos a Walter White por ID")
    void getCharacterByID() throws Exception {
        mockMvc.perform(get("/api/characters/{id}", 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Walter White"))
                .andExpect(jsonPath("$.occupation").value("Profesor de Química"))
                .andExpect(jsonPath("$.status").value("ALIVE"));
    }

    // Tarea 2: PATCH /api/characters/{id}/status
    @Test
    @DisplayName("PATCH: Cambia estado de Walter White a DEAD")
    void patchStatusCharacterById() throws Exception {
        String jsonUpdate = """
            { "status": "DEAD" }
            """;
        mockMvc.perform(patch("/api/characters/{id}/status", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdate)
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Walter White"))
                .andExpect(jsonPath("$.status").value("DEAD"));
    }

    // Tarea 3: PUT /api/characters/{id}
    @Test
    @DisplayName("PUT: Modificacion de datos Walter White")
    void putUpdateCharacterById() throws Exception {
        String jsonUpdate = """
                {
                    "name": "Nelson Mons",
                    "occupation": "Delivery",
                    "status": "DEAD"
                }
                """;
        mockMvc.perform(put("/api/characters/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdate)
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Nelson Mons"))
                .andExpect(jsonPath("$.occupation").value("Delivery"))
                .andExpect(jsonPath("$.status").value("DEAD"));
    }
}
