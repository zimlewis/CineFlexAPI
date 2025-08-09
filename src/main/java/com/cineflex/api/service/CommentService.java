package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Comment;
import com.cineflex.api.model.CommentSection;
import com.cineflex.api.model.ReportComment;
import com.cineflex.api.repository.CommentRepository;
import com.cineflex.api.repository.CommentSectionRepository;
import com.cineflex.api.repository.ReportCommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentSectionRepository commentSectionRepository;
    private final ReportCommentRepository reportCommentRepository;

    public CommentService (
        CommentRepository commentRepository,
        CommentSectionRepository commentSectionRepository,
        ReportCommentRepository reportCommentRepository
    ) {
        this.commentRepository = commentRepository;
        this.reportCommentRepository = reportCommentRepository;
        this.commentSectionRepository = commentSectionRepository;
    }

    public Comment getCommentById(UUID id) {
        try {
            Comment comment = commentRepository.read(id);

            return comment;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } 
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

    public Comment addToSectionComment(Comment comment, UUID section) {
        try {
            UUID id = UUID.randomUUID();
            comment.setId(id);
            comment.setCreatedTime(LocalDateTime.now());
            comment.setUpdatedTime(LocalDateTime.now());

            comment.setSection(section);

            commentRepository.create(comment);

            Comment returnedComment = commentRepository.read(id);
            return returnedComment;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getAllReportCommentsPageCount(Integer size) {
        try {
            Integer count = reportCommentRepository.getPageCount(size);

            return count;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<ReportComment> getAllReportComments(Integer page, Integer size) {
        try{
            List<ReportComment> reportComments = reportCommentRepository.readAll(page, size);

            return reportComments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Comment> getAllCommentsFromSection(Integer page, Integer size, UUID id) {
        try {
            List<Comment> comments = commentRepository.getCommentsBySection(page, size, id);

            return comments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Comment> getAllCommentsFromSectionDeleted(Integer page, Integer size, UUID id) {
        try {
            List<Comment> comments = commentRepository.getCommentsBySectionDeleted(page, size, id);

            return comments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Comment> getAllCommentsFromEpisode(Integer page, Integer size, UUID id) {
        try {
            List<Comment> comments = commentRepository.getCommentsByEpisode(page, size, id);

            return comments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Comment> getAllCommentsFromShow(Integer page, Integer size, UUID ...id) {
        try {
            List<Comment> comments = commentRepository.getCommentsByShow(page, size, id);

            return comments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Comment> getAllComments(Integer page,Integer size) {
        try {
            List<Comment> comments = commentRepository.readAll(page, size);

            return comments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getAllCommentsPageCount(Integer size) {
        try {
            Integer pageCount = commentRepository.getPageCount(size);

            return pageCount;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getAllCommentsFromSectionPageCount(Integer size, UUID ...ids) {
        try {
            Integer pageCount = commentRepository.getPageCountBySection(size, ids);

            return pageCount;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getAllCommentsFromSectionDeletedPageCount(Integer size, UUID ...ids) {
        try {
            Integer pageCount = commentRepository.getPageCountBySection(size, ids);

            return pageCount;
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

    public CommentSection createCommentSection(String alias) {
        try {
            UUID id = UUID.randomUUID();
            CommentSection commentSection = CommentSection.builder()
                .id(id)
                .alias(alias)
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

    public List<Comment> getAllCommentsFromSection(Integer page, Integer size, UUID ...id) {
        try {
            List<Comment> comments = commentRepository.getCommentsByShow(page, size, id);

            return comments;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public CommentSection getCommentSection(UUID id) {
        try {
            return commentSectionRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional
    public void approveReport(UUID id) {
        try {
            ReportComment reportComment = reportCommentRepository.read(id);

            commentRepository.delete(reportComment.getComment());
            reportComment.setStatus(1);

            reportCommentRepository.update(id, reportComment);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional
    public void ignoreReport(UUID id) {
        try {
            ReportComment reportComment = reportCommentRepository.read(id);
            reportComment.setStatus(1);

            reportCommentRepository.update(id, reportComment);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ReportComment reportAComment(ReportComment body) {
        try {
            UUID id = UUID.randomUUID();
            body.setId(id);
            body.setCreatedTime(LocalDateTime.now());
            body.setUpdatedTime(LocalDateTime.now());
            body.setStatus(0);

            reportCommentRepository.create(body);

            return reportCommentRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Integer getCommentSectionsPageCount(Integer size) {
        try {
            Integer pageCount = commentSectionRepository.getPageCount(size);

            return pageCount;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<CommentSection> getCommentSections(Integer page, Integer size) {
        try {
            List<CommentSection> commentSections = commentSectionRepository.readAll(page, size);

            return commentSections;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
