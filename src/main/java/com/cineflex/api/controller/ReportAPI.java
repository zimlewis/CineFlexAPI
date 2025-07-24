package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Comment;
import com.cineflex.api.model.ReportComment;
import com.cineflex.api.service.CommentService;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/reports")
public class ReportAPI {
    
    private final CommentService commentService;

    public ReportAPI (
        CommentService commentService
    ) {
        this.commentService = commentService;
    }

    @GetMapping("")
    public ResponseEntity<List<ReportComment>> getPaginatedComment(
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "5") Integer size
    ) {
        try {
            List<ReportComment> reportComments = commentService.getAllReportComments(page, size);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", commentService.getAllReportCommentsPageCount(size).toString());

            return new ResponseEntity<>(reportComments, headers, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }
    

}
