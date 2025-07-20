package com.cineflex.api.Controller;

import com.cineflex.api.controller.EpisodeAPI;
import com.cineflex.api.model.Episode;
import com.cineflex.api.model.Comment;
import com.cineflex.api.service.CommentService;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.ShowService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class EpisodeAPITest {

    private final ShowService showService = mock(ShowService.class);
    private final JsonService jsonService = new JsonService();
    private final CommentService commentService = mock(CommentService.class);

    private final EpisodeAPI episodeAPI = new EpisodeAPI(showService, jsonService, commentService);

    @Test
    public void testGetEpisodeById_success() {
        UUID episodeId = UUID.randomUUID();

        Episode mockEpisode = Episode.builder()
                .id(episodeId)
                .title("Test Episode")
                .build();

        when(showService.findEpisodeById(episodeId)).thenReturn(mockEpisode);

        ResponseEntity<Episode> response = episodeAPI.getEpisodeById(episodeId.toString());

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Episode", response.getBody().getTitle());
    }

    @Test
    public void testGetEpisodeById_notFound() {
        UUID episodeId = UUID.randomUUID();

        when(showService.findEpisodeById(episodeId)).thenReturn(null);

        ResponseEntity<Episode> response = episodeAPI.getEpisodeById(episodeId.toString());

        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    public void testUpdateEpisode_success() {
        UUID episodeId = UUID.randomUUID();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("title", "Updated Title");
        requestBody.put("number", "1");
        requestBody.put("description", "Some desc");
        requestBody.put("url", "https://abc.com");
        requestBody.put("releaseDate", LocalDate.now().toString());
        requestBody.put("duration", 20);
        requestBody.put("openingStart", 0);
        requestBody.put("openingEnd", 5);

        Episode expected = Episode.builder()
                .id(episodeId)
                .title("Updated Title")
                .build();

        when(showService.updateEpisode(any(Episode.class))).thenReturn(expected);

        ResponseEntity<Episode> response = episodeAPI.updateEpisode(episodeId.toString(), requestBody);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Updated Title", response.getBody().getTitle());
    }




    @Test
    public void testDeleteEpisode_success() {
        UUID episodeId = UUID.randomUUID();

        doNothing().when(showService).deleteEpisode(episodeId);

        ResponseEntity<?> response = episodeAPI.deleteEpisode(episodeId.toString());

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testGetCommentsByEpisode_success() {
        UUID episodeId = UUID.randomUUID();

        List<Comment> comments = List.of(
                Comment.builder().id(UUID.randomUUID()).content("Great!").build(),
                Comment.builder().id(UUID.randomUUID()).content("Love it!").build()
        );

        when(commentService.getAllCommentsFromEpisode(episodeId)).thenReturn(comments); // ✅ FIXED method name

        ResponseEntity<?> response = episodeAPI.getComments(episodeId.toString());

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List<?>);
        assertEquals(2, ((List<?>) response.getBody()).size());
    }
}
