package com.cineflex.api.Controller;

import com.cineflex.api.controller.SeasonAPI;
import com.cineflex.api.model.Episode;
import com.cineflex.api.model.Season;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.ShowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SeasonAPITest {

    private MockMvc mockMvc;
    private ShowService showService;
    private JsonService jsonService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        showService = mock(ShowService.class);
        jsonService = mock(JsonService.class);
        SeasonAPI controller = new SeasonAPI(showService, jsonService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetSeasonByIdSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        Season season = Season.builder()
                .id(id)
                .title("Season 1")
                .build();

        when(showService.findSeasonById(id)).thenReturn(season);

        mockMvc.perform(get("/api/seasons/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Season 1"));
    }

    @Test
    public void testDeleteSeasonSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(showService).deleteSeason(id);

        mockMvc.perform(delete("/api/seasons/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSeasonSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        UUID showId = UUID.randomUUID();

        ObjectNode request = objectMapper.createObjectNode();
        request.put("title", "Updated Season");
        request.put("description", "Description");
        request.put("releaseDate", LocalDate.now().toString());
        request.put("show", showId.toString());

        when(jsonService.getOrNull(any(), eq("title"), eq(String.class))).thenReturn("Updated Season");
        when(jsonService.getOrNull(any(), eq("description"), eq(String.class))).thenReturn("Description");
        when(jsonService.getOrNull(any(), eq("show"), eq(String.class))).thenReturn(showId.toString());

        Season updated = Season.builder().id(id).title("Updated Season").description("Description").show(showId).build();
        when(showService.updateSeason(any(Season.class))).thenReturn(updated);

        mockMvc.perform(put("/api/seasons/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Season"));
    }

    @Test
    public void testAddEpisodeSuccess() throws Exception {
        UUID seasonId = UUID.randomUUID();

        ObjectNode request = objectMapper.createObjectNode();
        request.put("title", "Ep 1");
        request.put("number", "1");
        request.put("description", "First ep");
        request.put("url", "https://url");
        request.put("releaseDate", LocalDate.now().toString());
        request.put("duration", 30);
        request.put("openingStart", 0);
        request.put("openingEnd", 10);

        when(jsonService.getOrNull(any(), eq("title"), eq(String.class))).thenReturn("Ep 1");
        when(jsonService.getOrNull(any(), eq("number"), eq(String.class))).thenReturn("1");
        when(jsonService.getOrNull(any(), eq("description"), eq(String.class))).thenReturn("First ep");
        when(jsonService.getOrNull(any(), eq("url"), eq(String.class))).thenReturn("https://url");
        when(jsonService.getOrNull(any(), eq("duration"), eq(Integer.class))).thenReturn(30);
        when(jsonService.getOrNull(any(), eq("openingStart"), eq(Integer.class))).thenReturn(0);
        when(jsonService.getOrNull(any(), eq("openingEnd"), eq(Integer.class))).thenReturn(10);

        Episode episode = Episode.builder().title("Ep 1").season(seasonId).build();
        when(showService.addEpisode(any(Episode.class))).thenReturn(episode);

        mockMvc.perform(post("/api/seasons/" + seasonId + "/episodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Ep 1"));
    }

    @Test
    public void testGetAllEpisodesFromSeason() throws Exception {
        UUID seasonId = UUID.randomUUID();
        Episode e1 = Episode.builder().title("E1").build();
        Episode e2 = Episode.builder().title("E2").build();

        when(showService.findEpisodesBySeasons(seasonId)).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/api/seasons/" + seasonId + "/episodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    public void testGetSeasonById_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(showService.findSeasonById(id)).thenReturn(null); // hoặc throw NotFoundException nếu bạn dùng

        mockMvc.perform(get("/api/seasons/" + id))
                .andExpect(status().isNotFound());
    }

}
