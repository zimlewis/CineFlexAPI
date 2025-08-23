package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Account;
import com.cineflex.api.model.Comment;
import com.cineflex.api.model.Episode;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.CommentService;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.ShowService;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/episodes")
public class EpisodeAPI {

    private final ShowService showService;
    private final JsonService jsonService;
    private final CommentService commentService;
    private final AuthenticationService authenticationService;

    public EpisodeAPI (
        ShowService showService,
        JsonService jsonService,
        CommentService commentService,
        AuthenticationService authenticationService
    ) {
        this.showService = showService;
        this.jsonService = jsonService;
        this.commentService = commentService;
        this.authenticationService = authenticationService;
    }


    @GetMapping("/{id}/is-liked")
    public ResponseEntity<Boolean> isLike (
        @PathVariable String id
    ) {
        try {
            Account a = authenticationService.getAccount();

            if (a == null) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }

            Boolean isLiked = showService.isLiked(UUID.fromString(id), a.getId());

            return new ResponseEntity<>(isLiked, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<Integer> getLikeCount (
        @PathVariable String id
    ) {
        try {
            UUID episode = UUID.fromString(id);

            Integer likeCount = showService.getLikeCount(episode);

            return new ResponseEntity<>(likeCount, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    

    @PostMapping("/{id}/unlike")
    public ResponseEntity<?> unlikeEpisode(
        @PathVariable String id
    ) {
        try {
            Account a = authenticationService.getAccount();
            UUID account = a.getId();
            UUID episode = UUID.fromString(id);

            showService.unlikeAEpisode(episode, account);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }    
    
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeEpisode (
        @PathVariable String id
    ) {
        try {
            Account a = authenticationService.getAccount();
            UUID account = a.getId();
            UUID episode = UUID.fromString(id);

            showService.likeAEpisode(episode, account);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<Episode> getEpisodeById(@PathVariable String id) {
        try {
            Episode episode = showService.findEpisodeById(UUID.fromString(id));

            if (episode == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, 
                    "Cannot find the season with given id"
                )).build();
            }

            return new ResponseEntity<>(episode, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Episode> updateEpisode(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            UUID seasonId = jsonService.getOrNull(jsonNode, "season", String.class) == null?null : UUID.fromString(jsonService.getOrNull(jsonNode, "show", String.class));
            Episode episode = Episode.builder()
                .id(UUID.fromString(id))
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .number(jsonService.getOrNull(jsonNode, "number", String.class))
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .url(jsonService.getOrNull(jsonNode, "url", String.class))
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .duration(jsonService.getOrNull(jsonNode, "duration", Integer.class))
                .openingStart(jsonService.getOrNull(jsonNode, "openingStart", Integer.class))
                .openingEnd(jsonService.getOrNull(jsonNode, "openingEnd", Integer.class))
                .view(0)
                .season(seasonId)
                .build();
            
            Episode returnEpisode = showService.updateEpisode(episode);

            return new ResponseEntity<>(returnEpisode, HttpStatus.OK);
            
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEpisode(@PathVariable String id) {
        try {
            showService.deleteEpisode(UUID.fromString(id));

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(
        @PathVariable String id,
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "5") Integer size
    ) {
        try {
            List<Comment> comments = commentService.getAllCommentsFromEpisode(page, size, UUID.fromString(id));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", commentService.getAllCommentsFromEpisodePageCount(size, UUID.fromString(id)).toString());

            return new ResponseEntity<>(comments, headers, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }

    @PostMapping("/{id}/view-history")
    public ResponseEntity<?> updateViewHistory(
        @PathVariable String id,
        @RequestBody JsonNode body
    ) {
        try {
            Integer duration = jsonService.getOrNull(body, "duration", Integer.class);

            if (duration == null) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Duration field must exist");
            }

            Account account = authenticationService.getAccount();

            if (account == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You did not log in");
            }

            UUID episodeId = UUID.fromString(id);

            showService.addViewHistory(account.getId(), episodeId, duration);
            return new ResponseEntity<>(HttpStatus.OK);

        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{episode}/views") 
    public ResponseEntity<Integer> getViewsCount (
        @PathVariable String episode
    ) {
        try {
            UUID id = UUID.fromString(episode);
            Integer views = showService.getActualView(id);

            return new ResponseEntity<>(views, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }

    @PostMapping("/{episode}/views")
    public ResponseEntity<Integer> addViewsCount (
        @PathVariable String episode
    ) {
        try {
            UUID id = UUID.fromString(episode);

            showService.incrementView(id);
            Integer views = showService.getActualView(id);

            return new ResponseEntity<>(views, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }
    
    

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> postACommentToEpisode(@RequestBody JsonNode jsonNode, @PathVariable String id) {
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

            UUID episode = UUID.fromString(id);

            Comment comment = Comment.builder()
                    .content(jsonService.getOrNull(jsonNode, "content", String.class))
                    .account(user.getId())
                    .build();

            Comment returnedComment = commentService.addToEpisodeComment(comment, episode);

            return new ResponseEntity<>(returnedComment, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getReason())).build();
        }
    }

    @GetMapping("/liked")
    public ResponseEntity<?> getLikedEpisodes() {
        Account account = authenticationService.getAccount();
        return ResponseEntity.ok(showService.getLikedEpisodes(account.getId()));
    }


}
