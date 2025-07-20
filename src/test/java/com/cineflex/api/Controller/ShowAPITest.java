package com.cineflex.api.Controller;

import com.cineflex.api.controller.ShowAPI;
import com.cineflex.api.model.Genre;
import com.cineflex.api.model.Season;
import com.cineflex.api.model.Show;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.JwtService;
import com.cineflex.api.service.ShowService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShowAPI.class)
@Import(ShowAPITest.MockedServiceConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class ShowAPITest {

    @TestConfiguration
    static class MockedServiceConfig {
        @Bean
        public ShowService showService() {
            return Mockito.mock(ShowService.class);
        }

        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }

        @Bean
        public JsonService jsonService() {
            return new JsonService();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShowService showService;

    @Test
    void testGetShowById_success() throws Exception {
        UUID showId = UUID.randomUUID();
        Show show = Show.builder()
                .id(showId)
                .title("Test Show")
                .build();

        when(showService.findShowById(showId)).thenReturn(show);

        mockMvc.perform(get("/api/shows/{id}", showId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Show"));
    }

    @Test
    void testGetShowById_notFound() throws Exception {
        UUID showId = UUID.randomUUID();

        when(showService.findShowById(showId)).thenReturn(null);

        mockMvc.perform(get("/api/shows/{id}", showId))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetAllShows_success() throws Exception {
        when(showService.findAllShows()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/shows"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetAllShows_withGenresFilter_success() throws Exception {
        when(showService.findShowByGenre(String.valueOf(List.of("Action", "Drama"))))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/shows")
                        .param("genres", "Action")
                        .param("genres", "Drama"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAddShow_success() throws Exception {
        UUID showId = UUID.randomUUID();
        Show saved = Show.builder()
                .id(showId)
                .title("New Show")
                .build();

        when(showService.addShow(any(Show.class))).thenReturn(saved);

        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "New Show",
                                  "description": "Desc",
                                  "releaseDate": "2025-01-01",
                                  "thumbnail": "img.png",
                                  "onGoing": true,
                                  "isSeries": true,
                                  "ageRating": "16+"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Show"));
    }

    @Test
    void testUpdateShow_success() throws Exception {
        UUID showId = UUID.randomUUID();
        Show updated = Show.builder()
                .id(showId)
                .title("Updated Show")
                .build();

        when(showService.updateShow(any(Show.class))).thenReturn(updated);

        mockMvc.perform(put("/api/shows/{id}", showId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated Show",
                                  "description": "Updated",
                                  "releaseDate": "2025-02-02",
                                  "thumbnail": "updated.png",
                                  "onGoing": false,
                                  "isSeries": false,
                                  "ageRating": "18+"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Show"));
    }

    @Test
    void testDeleteShow_success() throws Exception {
        UUID showId = UUID.randomUUID();
        Mockito.doNothing().when(showService).deleteShow(showId);

        mockMvc.perform(delete("/api/shows/{id}", showId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSeasonsOfShow_success() throws Exception {
        UUID showId = UUID.randomUUID();

        when(showService.findSeaonsByShows(showId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/shows/{id}/seasons", showId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAddSeasonToShow_success() throws Exception {
        UUID seasonId = UUID.randomUUID();
        UUID showId = UUID.randomUUID();

        Season season = Season.builder()
                .id(seasonId)
                .title("Season 1")
                .build();

        when(showService.addSeason(any())).thenReturn(season);

        mockMvc.perform(post("/api/shows/{id}/seasons", showId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Season 1",
                                    "description": "First season",
                                    "releaseDate": "2025-01-01"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Season 1"));
    }

    @Test
    void testGetGenresOfShow_success() throws Exception {
        UUID showId = UUID.randomUUID();

        when(showService.getShowGenres(showId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/shows/{id}/genres", showId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAddGenresToShow_success() throws Exception {
        UUID showId = UUID.randomUUID();
        UUID genreId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        Genre genre = Genre.builder()
                .id(genreId)
                .name("Action")
                .build();

        when(showService.addGenresToShow(eq(showId), any()))
                .thenReturn(List.of(genre));

        mockMvc.perform(post("/api/shows/{id}/genres", showId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "genres": ["550e8400-e29b-41d4-a716-446655440000"]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].name").value("Action"));
    }
}
