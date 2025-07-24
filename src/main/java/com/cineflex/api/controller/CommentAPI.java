package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Account;
import com.cineflex.api.model.Comment;
import com.cineflex.api.model.ReportComment;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.CommentService;
import com.cineflex.api.service.JsonService;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/comments")
public class CommentAPI {

    private final JsonService jsonService;
    private final AuthenticationService authenticationService;
    private final CommentService commentService;

    public CommentAPI(
            JsonService jsonService,
            AuthenticationService authenticationService,
            CommentService commentService
        ) {
        this.jsonService = jsonService;
        this.authenticationService = authenticationService;
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById (
        @PathVariable String id
    ) {
        try {
            UUID getId = UUID.fromString(id);

            Comment comment = commentService.getCommentById(getId);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(
        @PathVariable String id
    ) {
        try {
            UUID deleteId = UUID.fromString(id);

            commentService.deleteComments(deleteId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/section/{id}")
    public ResponseEntity<List<Comment>> getComments(
        @PathVariable String id,
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "5") Integer size
    ) {
        try {
            List<Comment> comments = commentService.getAllCommentsFromSection(page, size, UUID.fromString(id));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", commentService.getAllCommentsFromSectionPageCount(size, UUID.fromString(id)).toString());

            return new ResponseEntity<>(comments, headers, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }

    @PostMapping("/section/{id}")
    public ResponseEntity<Comment> postACommentToSection(@RequestBody JsonNode jsonNode, @PathVariable String id) {
        try {
            Account user = authenticationService.getAccount();

            if (user == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        "The client did not logged in")).build();
            }


            if (id == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Did not provide episode"
                )).build();
            }

            UUID section = UUID.fromString(id);

            Comment comment = Comment.builder()
                    .content(jsonService.getOrNull(jsonNode, "content", String.class))
                    .account(user.getId())
                    .build();

            Comment returnedComment = commentService.addToSectionComment(comment, section);

            return new ResponseEntity<>(returnedComment, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getReason())).build();
        }
    }
    

    @GetMapping("")
    public ResponseEntity<List<Comment>> getPaginatedComment(
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "100") Integer size
    ) {
        try {
            List<Comment> comments = commentService.getAllComments(page, size);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", commentService.getAllCommentsPageCount(size).toString());

            return new ResponseEntity<>(comments, headers, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }

    

    @PostMapping("/{id}/report")
    public ResponseEntity<ReportComment> postMethodName(
        @PathVariable String id,
        @RequestBody JsonNode jsonNode
    ) {
        try {
            Account user = authenticationService.getAccount();
            
            ReportComment requestBody = ReportComment.builder()
                .account(user.getId())
                .comment(UUID.fromString(id))
                .content(jsonService.getOrNull(jsonNode, "content", String.class))
                .build();
            
            ReportComment responseBody = commentService.reportAComment(requestBody);

            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }
    
    

}
