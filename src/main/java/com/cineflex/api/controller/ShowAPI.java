package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Account;
import com.cineflex.api.model.Comment;
import com.cineflex.api.model.Genre;
import com.cineflex.api.model.Rating;
import com.cineflex.api.model.Season;
import com.cineflex.api.model.Show;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.CommentService;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.ShowService;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.text.html.parser.Entity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;






@RestController
@RequestMapping("/api/shows")
public class ShowAPI {

    private final ShowService showService;
    private final JsonService jsonService;
    private final CommentService commentService;
    private final AuthenticationService authenticationService;

    // Inject show service
    public ShowAPI (
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
    
    @GetMapping("/query")
    public ResponseEntity<List<Show>> queryForShow(
        @RequestParam(required = false) List<String> genres,
        @RequestParam(required = false) String ageRating,
        @RequestParam(required = false) Boolean series,
        @RequestParam(required = false) LocalDate from,
        @RequestParam(required = false) LocalDate to,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "5") Integer size

    ) {
        try {
            if (genres == null) {
                genres = new ArrayList<>();
            }
            List<Show> shows = showService.queryShow(
                genres,
                ageRating,
                series,
                keyword,
                from,
                to,
                page,
                size
            );

            Integer pageCount = showService.queryShowSizeCount(
                genres, 
                ageRating, 
                series, 
                keyword, 
                from, 
                to, 
                size
            );

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", pageCount.toString());


            return new ResponseEntity<>(shows, headers, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    

    // Get single show database
    @GetMapping("/{id}")
    public ResponseEntity<Show> findShow(@PathVariable String id) {

        try {
            Show show = showService.findShowById(UUID.fromString(id));

            if (show == null) {
                
                return ResponseEntity
                    .of(ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND, // not found status code
                        "Cannot find show with given id" // error message
                    )) 
                    .build(); // Return an error if the id could not be found
            }

            return ResponseEntity.ok(show); // Return a show if the id is correct
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Show>> search() {
        try {
            List<Show> shows;

            shows = showService.findAllShows(0, 5);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", showService.getAllShowsPageCount(5).toString()); 

            ResponseEntity<List<Show>> responseEntity = new ResponseEntity<List<Show>>(shows, headers, HttpStatus.OK);

            return responseEntity;
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    // Get all show
    @GetMapping("")
    public ResponseEntity<List<Show>> find(
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "5") Integer size
    ) {

        try {
            List<Show> shows;

            shows = showService.findAllShows(page, size);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", showService.getAllShowsPageCount(size).toString()); 

            ResponseEntity<List<Show>> responseEntity = new ResponseEntity<List<Show>>(shows, headers, HttpStatus.OK);

            return responseEntity;
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/is-favorite")
    public ResponseEntity<Boolean> isFavorite (
        @PathVariable String id
    ) {
        try {
            Account a = authenticationService.getAccount();

            if (a == null) {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }

            Boolean isFavorite = showService.isFavorited(UUID.fromString(id), a.getId());

            return new ResponseEntity<>(isFavorite, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/favorite")
    public ResponseEntity<Integer> getFavoriteCount (
        @PathVariable String id
    ) {
        try {
            UUID show = UUID.fromString(id);

            Integer favoriteCount = showService.getFavoriteCount(show);

            return new ResponseEntity<>(favoriteCount, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    

    @PostMapping("/{id}/unfavorite")
    public ResponseEntity<?> unfavoriteShow(
        @PathVariable String id
    ) {
        try {
            Account a = authenticationService.getAccount();
            UUID account = a.getId();
            UUID show = UUID.fromString(id);

            showService.unfavoriteAShow(show, account);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }    
    
    @PostMapping("/{id}/favorite")
    public ResponseEntity<?> favoriteShow(
        @PathVariable String id
    ) {
        try {
            Account a = authenticationService.getAccount();
            UUID account = a.getId();
            UUID show = UUID.fromString(id);

            showService.favoriteAShow(show, account);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    

    // Add show to database
    @PostMapping("")
    public ResponseEntity<Show> addShow(@RequestBody JsonNode jsonNode) {
        try {

            // Get the show content from json node
            Show show = Show.builder()
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .thumbnail(jsonService.getOrNull(jsonNode, "thumbnail", String.class))
                .onGoing(jsonService.getOrNull(jsonNode, "onGoing", Boolean.class))
                .isSeries(jsonService.getOrNull(jsonNode, "isSeries", Boolean.class))
                .ageRating(jsonService.getOrNull(jsonNode, "ageRating", String.class))
                .build();
            
            Show returnShow = showService.addShow(show);

            return new ResponseEntity<Show>(returnShow, HttpStatus.CREATED);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }

    }

    // Update show
    @PutMapping("/{id}")
    public ResponseEntity<Show> updateShow(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            // Get the show content from json node
            Show show = Show.builder()
                .id(UUID.fromString(id))
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .thumbnail(jsonService.getOrNull(jsonNode, "thumbnail", String.class))
                .onGoing(jsonService.getOrNull(jsonNode, "onGoing", Boolean.class))
                .isSeries(jsonService.getOrNull(jsonNode, "isSeries", Boolean.class))
                .ageRating(jsonService.getOrNull(jsonNode, "ageRating", String.class))
                .build();
            
            Show returnShow = showService.updateShow(show);

            return ResponseEntity.ok(returnShow);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    // Delete show
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShow(@PathVariable String id) {
        try {
            showService.deleteShow(UUID.fromString(id));

            return ResponseEntity.ok(null);
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getStackTrace().toString());
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/seasons")
    public ResponseEntity<List<Season>> getSeasonsOfAShow(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "100") Integer size, @PathVariable String id) {
        try {
            List<Season> seasons = showService.findSeaonsByShows(page, size, UUID.fromString(id));


            
            // if (genres != null) {
            //     shows = showService.findShowByGenre(genres.toArray(new String[0]));
            // }
            // else {
            //     shows = showService.findAllShows(0, 5);
            // }
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", showService.getSeasonsByShowsPageCount(size).toString()); 

            ResponseEntity<List<Season>> responseEntity = new ResponseEntity<List<Season>>(seasons, headers, HttpStatus.OK);

            return responseEntity;
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }
    
    @PostMapping("/{id}/seasons")
    public ResponseEntity<Season> addSeasonToShowShowAPI(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            Season season = Season.builder()
                .title(jsonService.getOrNull(jsonNode, "title", String.class))
                .releaseDate(jsonNode.has("releaseDate")?LocalDate.parse(jsonNode.get("releaseDate").asText()):null)
                .description(jsonService.getOrNull(jsonNode, "description", String.class))
                .show(UUID.fromString(id))
                .build();
            
            Season returnSeason = showService.addSeason(season);

            return new ResponseEntity<Season>(returnSeason, HttpStatus.CREATED);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/genres")
    public ResponseEntity<List<Genre>> getGenresOfShow(@PathVariable String id) {
        try {
            List<Genre> genres = showService.getShowGenres(UUID.fromString(id));

            return new ResponseEntity<>(genres, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }
    

    @PutMapping("/{id}/genres")
    public ResponseEntity<List<Genre>> addGernesToShow(@PathVariable String id, @RequestBody JsonNode jsonNode) {
        try {
            UUID showId = UUID.fromString(id);
            JsonNode genreIdsNode = jsonNode.get("genres");

            if (genreIdsNode == null || !genreIdsNode.isArray()) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_ACCEPTABLE,
                    "genres field must be an array"
                )).build();
            }

            List<UUID> genreIds = new ArrayList<>();

            for (JsonNode node : genreIdsNode) {
                UUID genreId = UUID.fromString(node.asText());
                genreIds.add(genreId);
            }

            List<Genre> genres = showService.updateGenresOfShow(showId, genreIds.toArray(UUID[]::new));

            return new ResponseEntity<>(genres, HttpStatus.CREATED);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), e.getReason()
            )).build();
        }
        catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()
            )).build();
        }
    }
    
    
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(
        @PathVariable String id,
        @RequestParam(required = false, defaultValue = "0") Integer page, 
        @RequestParam(required = false, defaultValue = "12") Integer size
    ) {
        try {
            List<Comment> comments = commentService.getAllCommentsFromShow(page, size, UUID.fromString(id));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Page", commentService.getAllCommentsFromShowPageCount(size, UUID.fromString(id)).toString());

            return new ResponseEntity<>(comments, headers, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/avg-rate")
    public ResponseEntity<Double> getShowAverageRate(@PathVariable String id) {
        try {
            UUID show = UUID.fromString(id);
            Double average = showService.getShowAverageRate(show);

            return new ResponseEntity<>(average ,HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/{id}/rate")
    public ResponseEntity<Rating> getShowRateByAccount(@PathVariable String id) {
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
                    "Did not provide show"
                )).build();
            }

            UUID show = UUID.fromString(id);

            Rating rating = showService.getShowUserRate(show, user.getId());
            return new ResponseEntity<>(rating, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getReason()
            )).build();
        }

    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Rating> rateShow(@RequestBody JsonNode jsonNode, @PathVariable String id) {
        try {

            Account user = authenticationService.getAccount();
            Integer value = jsonService.getOrNull(jsonNode, "value", Integer.class);

            if (user == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        "The client did not logged in")).build();
            }


            if (id == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Did not provide show"
                )).build();
            }

            UUID show = UUID.fromString(id);

            Rating rating = showService.rateShow(show, user.getId(), value);
            return new ResponseEntity<>(rating, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getReason())).build();
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
                    "Did not provide show"
                )).build();
            }

            UUID show = UUID.fromString(id);

            Comment comment = Comment.builder()
                    .content(jsonService.getOrNull(jsonNode, "content", String.class))
                    .account(user.getId())
                    .build();

            Comment returnedComment = commentService.addToShowComment(comment, show);

            return new ResponseEntity<>(returnedComment, HttpStatus.CREATED);
        } 
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getReason())).build();
        }
    }
    @GetMapping("/anime")
    public Integer getAnimeCount() {
        return showService.getTotalAnimeShows();
    }

    @GetMapping("/phim")
    public Integer getPhimCount() {
        return showService.getTotalPhimShows();
    }

}
