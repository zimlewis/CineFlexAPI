package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Comment;
import com.cineflex.api.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService (
        CommentRepository commentRepository
    ) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Comment comment) {
        try {
            UUID id = UUID.randomUUID();
            comment.setId(id);
            comment.setCreatedTime(LocalDateTime.now());
            comment.setUpdatedTime(LocalDateTime.now());


            commentRepository.create(comment);

            Comment returnedComment = commentRepository.read(id);
            return returnedComment;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Comment> getAllCommentsFromEpisode(UUID id) {
        try {
            List<Comment> comments = commentRepository.getCommentsByEpisode(id);

            comments.sort((c1, c2) -> c1.getCreatedTime().compareTo(c2.getCreatedTime()));

            return comments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void deleteComments(UUID ...ids) {
        try {
            commentRepository.delete(ids);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
