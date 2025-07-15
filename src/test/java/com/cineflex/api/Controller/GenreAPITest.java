package com.cineflex.api.Controller;

import com.cineflex.api.controller.GenreAPI;
import com.cineflex.api.model.Genre;
import com.cineflex.api.service.GenreService;
import com.cineflex.api.service.JsonService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GenreAPITest {

    private MockMvc mockMvc;
    private GenreService genreService;
    private JsonService jsonService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        genreService = mock(GenreService.class);
        jsonService = mock(JsonService.class);

        GenreAPI controller = new GenreAPI(jsonService, genreService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetGenreByIdSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        Genre genre = Genre.builder()
                .id(id)
                .name("Action")
                .build();

        when(genreService.getGenre(id)).thenReturn(genre);

        mockMvc.perform(get("/api/genres/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Action"));
    }

    @Test
    public void testGetGenreByIdFailure() throws Exception {
        UUID id = UUID.randomUUID();

        when(genreService.getGenre(id))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Genre not found"));

        mockMvc.perform(get("/api/genres/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddGenreSuccess() throws Exception {
        String name = "Comedy";

        ObjectNode jsonBody = objectMapper.createObjectNode();
        jsonBody.put("name", name);

        Genre inputGenre = Genre.builder().name(name).build();
        Genre returnedGenre = Genre.builder()
                .id(UUID.randomUUID())
                .name(name)
                .build();

        when(jsonService.getOrNull(any(JsonNode.class), eq("name"), eq(String.class))).thenReturn(name);
        when(genreService.addGenre(any(Genre.class))).thenReturn(returnedGenre);

        mockMvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Comedy"));
    }

    @Test
    public void testAddGenreMissingName() throws Exception {
        ObjectNode jsonBody = objectMapper.createObjectNode(); // No "name"

        when(jsonService.getOrNull(any(JsonNode.class), eq("name"), eq(String.class))).thenReturn(null);

        mockMvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody.toString()))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void testAddGenreInternalError() throws Exception {
        ObjectNode jsonBody = objectMapper.createObjectNode();
        jsonBody.put("name", "Drama");

        when(jsonService.getOrNull(any(JsonNode.class), eq("name"), eq(String.class))).thenReturn("Drama");
        when(genreService.addGenre(any(Genre.class)))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Database error"));

        mockMvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody.toString()))
                .andExpect(status().isInternalServerError());
    }
}
