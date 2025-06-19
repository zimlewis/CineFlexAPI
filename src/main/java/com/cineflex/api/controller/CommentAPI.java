package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Account;
import com.cineflex.api.model.Comment;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.CommentService;
import com.cineflex.api.service.JsonService;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/comments")
public class CommentAPI {

    private final JsonService jsonService;
    private final AuthenticationService authenticationService;
    private final CommentService commentService;

    public CommentAPI (
        JsonService jsonService,
        AuthenticationService authenticationService,
        CommentService commentService
    ) {
        this.jsonService = jsonService;
        this.authenticationService = authenticationService;
        this.commentService = commentService;
    }
    
    @PostMapping("/{id}")
    public ResponseEntity<Comment> postACOmment(@RequestBody JsonNode jsonNode, @PathVariable String id) {
        try {
            Account user = authenticationService.getAccount();

            if (user == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.UNAUTHORIZED, 
                    "The client did not logged in"
                )).build();
            }

            Comment comment = Comment.builder()
                .content(jsonService.getOrNull(jsonNode, "content", String.class))
                .episode(UUID.fromString(id))
                .account(user.getId())
                .build();
            
            Comment returnedComment = commentService.addComment(comment);

            return new ResponseEntity<>(returnedComment, HttpStatus.CREATED);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }
    

}
