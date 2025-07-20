package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Comment;
import com.cineflex.api.model.CommentSection;
import com.cineflex.api.repository.CommentRepository;
import com.cineflex.api.repository.CommentSectionRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentSectionRepository commentSectionRepository;

    public CommentService (
        CommentRepository commentRepository,
        CommentSectionRepository commentSectionRepository
    ) {
        this.commentRepository = commentRepository;
        this.commentSectionRepository = commentSectionRepository;
    }

    public Comment addToShowComment(Comment comment, UUID show) {
        try {
            UUID id = UUID.randomUUID();
            comment.setId(id);
            comment.setCreatedTime(LocalDateTime.now());
            comment.setUpdatedTime(LocalDateTime.now());

            CommentSection commentSection = commentSectionRepository.getCommentSectionOfShow(show);
            comment.setSection(commentSection.getId());

            commentRepository.create(comment);

            Comment returnedComment = commentRepository.read(id);
            return returnedComment;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } 
    }

    public Comment addToEpisodeComment(Comment comment, UUID episode) {
        try {
            UUID id = UUID.randomUUID();
            comment.setId(id);
            comment.setCreatedTime(LocalDateTime.now());
            comment.setUpdatedTime(LocalDateTime.now());

            CommentSection commentSection = commentSectionRepository.getCommentSectionOfEpisode(episode);
            comment.setSection(commentSection.getId());

            commentRepository.create(comment);

            Comment returnedComment = commentRepository.read(id);
            return returnedComment;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Comment> getAllCommentsFromEpisode(Integer page, Integer size, UUID id) {
        try {
            List<Comment> comments = commentRepository.getCommentsByEpisode(page, size, id);

            comments.sort((c1, c2) -> c1.getCreatedTime().compareTo(c2.getCreatedTime()));

            return comments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Comment> getAllCommentsFromShow(Integer page, Integer size, UUID ...id) {
        try {
            List<Comment> comments = commentRepository.getCommentsByShow(page, size, id);

            comments.sort((c1, c2) -> c1.getCreatedTime().compareTo(c2.getCreatedTime()));

            return comments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getAllCommentsFromEpisodePageCount(Integer size, UUID ...ids) {
        try {
            Integer pageCount = commentRepository.getPageCountByEpisode(size, ids);

            return pageCount;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getAllCommentsFromShowPageCount(Integer size, UUID ...ids) {
        try {
            Integer pageCount = commentRepository.getPageCountByShow(size, ids);

            return pageCount;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public CommentSection createCommentSection() {
        try {
            UUID id = UUID.randomUUID();
            CommentSection commentSection = CommentSection.builder()
                .id(id)
                .build();

            commentSectionRepository.create(commentSection);

            commentSection = commentSectionRepository.read(id);
            return commentSection;
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
