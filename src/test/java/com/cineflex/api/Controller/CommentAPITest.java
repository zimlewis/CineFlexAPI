package com.cineflex.api.Controller;

import com.cineflex.api.controller.CommentAPI;
import com.cineflex.api.model.Account;
import com.cineflex.api.model.Comment;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.CommentService;
import com.cineflex.api.service.JsonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentAPITest {

    @Test
    void testPostComment_Success() {
        JsonService jsonService = mock(JsonService.class);
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        CommentService commentService = mock(CommentService.class);

        CommentAPI controller = new CommentAPI(jsonService, authenticationService, commentService);

        UUID episodeId = UUID.randomUUID();
        String content = "Great episode!";
        UUID userId = UUID.randomUUID();
        Account mockUser = Account.builder().id(userId).build();

        Comment inputComment = Comment.builder()
                .content(content)
                .episode(episodeId)
                .account(userId)
                .build();

        Comment returnedComment = Comment.builder()
                .id(UUID.randomUUID())
                .content(content)
                .episode(episodeId)
                .account(userId)
                .build();

        // Dữ liệu JSON giả
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("content", content);

        // Setup behavior cho mock
        when(authenticationService.getAccount()).thenReturn(mockUser);
        when(jsonService.getOrNull(json, "content", String.class)).thenReturn(content);
        when(commentService.addComment(any(Comment.class))).thenReturn(returnedComment);

        // Gọi method
        ResponseEntity<Comment> response = controller.postACOmment(json, episodeId.toString());

        // Kiểm tra kết quả
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(content, response.getBody().getContent());
    }

    @Test
    void testPostComment_Unauthorized() {
        JsonService jsonService = mock(JsonService.class);
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        CommentService commentService = mock(CommentService.class);

        CommentAPI controller = new CommentAPI(jsonService, authenticationService, commentService);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("content", "Test comment");

        when(authenticationService.getAccount()).thenReturn(null);

        ResponseEntity<Comment> response = controller.postACOmment(json, UUID.randomUUID().toString());

        assertEquals(401, response.getStatusCodeValue());
    }
}
