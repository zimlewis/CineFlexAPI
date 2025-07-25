package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.CommentSection;
import com.cineflex.api.model.Episode;
import com.cineflex.api.model.Genre;
import com.cineflex.api.model.Season;
import com.cineflex.api.model.Show;
import com.cineflex.api.model.ShowGenre;
import com.cineflex.api.model.ViewHistory;
import com.cineflex.api.repository.CommentRepository;
import com.cineflex.api.repository.EpisodeRepository;
import com.cineflex.api.repository.GenreRepository;
import com.cineflex.api.repository.SeasonRepository;
import com.cineflex.api.repository.ShowGenreRepository;
import com.cineflex.api.repository.ShowRepository;
import com.cineflex.api.repository.ViewHistoryRepository;


// This service handle everything that is related to shows (show, episode, season,...)
@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final GenreRepository genreRepository;
    private final ShowGenreRepository showGenreRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final ViewHistoryRepository viewHistoryRepository;
    private final RedisTemplate<String, Integer> redisTemplate;
    private static final String VIEW_KEY_PREFIX = "view.count:";

    // Inject repository to service
    public ShowService (
        ShowRepository showRepository,
        SeasonRepository seasonRepository,
        EpisodeRepository episodeRepository,
        GenreRepository genreRepository,
        ShowGenreRepository showGenreRepository,
        CommentService commentService,
        CommentRepository commentRepository,
        RedisTemplate<String, Integer> redisTemplate,
        ViewHistoryRepository viewHistoryRepository
    ) {
        this.showRepository = showRepository;
        this.seasonRepository = seasonRepository;
        this.episodeRepository = episodeRepository;
        this.genreRepository = genreRepository;
        this.showGenreRepository = showGenreRepository;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.redisTemplate = redisTemplate;
        this.viewHistoryRepository = viewHistoryRepository;
    }

    /* ---- INSERT METHOD ---- */
    public Show addShow(Show show) {
        try {
            UUID id = UUID.randomUUID(); // Use uuid to create unique id for record
            show.setId(id); // Set the id to the show
            show.setCreatedTime(LocalDateTime.now());
            show.setUpdatedTime(LocalDateTime.now());

            CommentSection commentSection = commentService.createCommentSection();
            System.out.println(commentSection);
            show.setCommentSection(commentSection.getId());

            
            showRepository.create(show); // Add show

            return showRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Season addSeason(Season season) {
        try {
            UUID id = UUID.randomUUID();
            season.setId(id);
            season.setCreatedTime(LocalDateTime.now());
            season.setUpdatedTime(LocalDateTime.now());

            seasonRepository.create(season);

            return seasonRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Episode addEpisode(Episode episode) {
        try {
            UUID id = UUID.randomUUID();
            episode.setId(id);
            episode.setCreatedTime(LocalDateTime.now());
            episode.setUpdatedTime(LocalDateTime.now());

            CommentSection commentSection = commentService.createCommentSection();
            episode.setCommentSection(commentSection.getId());

            episodeRepository.create(episode);

            return episodeRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }



    /* ---- UPDATE METHOD ---- */
    public Show updateShow(Show show) {
        try {
            UUID id = show.getId(); // Get the id from show to update
            Show oldShow = findShowById(id);

            show.setTitle(Objects.requireNonNullElse(show.getTitle(), oldShow.getTitle()));
            show.setDescription(Objects.requireNonNullElse(show.getDescription(), oldShow.getDescription()));
            show.setReleaseDate(Objects.requireNonNullElse(show.getReleaseDate(), oldShow.getReleaseDate()));
            show.setThumbnail(Objects.requireNonNullElse(show.getThumbnail(), oldShow.getThumbnail()));
            show.setCreatedTime(oldShow.getCreatedTime());
            show.setUpdatedTime(LocalDateTime.now());
            show.setOnGoing(Objects.requireNonNullElse(show.getOnGoing(), oldShow.getOnGoing()));
            show.setIsSeries(Objects.requireNonNullElse(show.getIsSeries(), oldShow.getIsSeries()));
            show.setAgeRating(Objects.requireNonNullElse(show.getAgeRating(), oldShow.getAgeRating()));

            showRepository.update(id, show); // Update the show with the given information
            
            return showRepository.read(id); // Return the updated show
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Season updateSeason(Season season) {
        try {
            UUID id = season.getId();
            Season oldSeason = seasonRepository.read(id);

            season.setTitle(Objects.requireNonNullElse(season.getTitle(), oldSeason.getTitle()));
            season.setReleaseDate(oldSeason.getReleaseDate());
            season.setCreatedTime(oldSeason.getCreatedTime());
            season.setUpdatedTime(LocalDateTime.now());
            season.setDescription(Objects.requireNonNullElse(season.getDescription(), oldSeason.getDescription()));
            season.setShow(Objects.requireNonNullElse(season.getShow(), oldSeason.getShow()));

            seasonRepository.update(id, season);

            return seasonRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public Episode updateEpisode(Episode episode) {
        try {
            UUID id = episode.getId();
            Episode oldEpisode = episodeRepository.read(id);

            episode.setTitle(Objects.requireNonNullElse(episode.getTitle(), oldEpisode.getTitle()));
            episode.setNumber(Objects.requireNonNullElse(episode.getNumber(), oldEpisode.getNumber()));
            episode.setDescription(Objects.requireNonNullElse(episode.getDescription(), oldEpisode.getDescription()));
            episode.setUrl(Objects.requireNonNullElse(episode.getUrl(), oldEpisode.getUrl()));
            episode.setReleaseDate(Objects.requireNonNullElse(episode.getReleaseDate(), oldEpisode.getReleaseDate()));
            episode.setCreatedTime(oldEpisode.getCreatedTime());
            episode.setUpdatedTime(LocalDateTime.now());
            episode.setDuration(Objects.requireNonNullElse(episode.getDuration(), oldEpisode.getDuration()));
            episode.setOpeningStart(Objects.requireNonNullElse(episode.getOpeningStart(), oldEpisode.getOpeningStart()));
            episode.setOpeningEnd(Objects.requireNonNullElse(episode.getOpeningEnd(), oldEpisode.getOpeningEnd()));
            episode.setView(Objects.requireNonNullElse(episode.getView(), oldEpisode.getView()));
            episode.setSeason(Objects.requireNonNullElse(episode.getSeason(), oldEpisode.getSeason()));

            episodeRepository.update(id, episode);

            return episodeRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    /* ---- DELETE METHOD ---- */
    @Transactional
    public void deleteShow(UUID... ids) {
        try {
            List<UUID> seasons = seasonRepository.getByShow(0, Integer.MAX_VALUE, ids).stream().map(s -> s.getId()).toList(); // get all seasons id related to shows
            deleteSeason(seasons.toArray(new UUID[0])); // delete the seasons
            showGenreRepository.deleteByShow(ids);
            showRepository.delete(ids); // Delete with repository using given information
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    } 

    @Transactional
    public void deleteSeason(UUID... ids) {
        try {
            List<UUID> episodes = episodeRepository.getBySeason(0, Integer.MAX_VALUE, ids).stream().map(e -> e.getId()).toList(); // get all episodes id related to seasons
            deleteEpisode(episodes.toArray(new UUID[0])); // delete all the episodes
            seasonRepository.delete(ids);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Transactional
    public void deleteEpisode(UUID... ids) {
        try {
            List<UUID> commentIds = commentRepository.getCommentsByEpisode(0, Integer.MAX_VALUE, ids).stream().map((comment) -> comment.getId()).toList();
            commentService.deleteComments(commentIds.toArray(new UUID[0]));
            episodeRepository.delete(ids);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /* --- READ METHOD --- */
    // Find by id
    public Show findShowById(UUID id) {
        try {
            return showRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Season findSeasonById(UUID id) {
        try {
            return seasonRepository.read(id);

        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Episode findEpisodeById(UUID id) {
        try {
            return episodeRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Find all
    public List<Show> findAllShows(Integer page, Integer size) {
        try {
            return showRepository.readAll(page, size);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public Integer getAllShowsPageCount(Integer size) {
        try {
            return showRepository.getPageCount(size);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Season> findAllSeasons(Integer page, Integer size) {
        try {
            return seasonRepository.readAll(page, size);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public List<Episode> findAllEpisodes(Integer page, Integer size) {
        try {
            return episodeRepository.readAll(page, size);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    // find the season using the given id show, this could be multiple id
    public List<Season> findSeaonsByShows(Integer page, Integer size, UUID... ids) {
        try {
            List<Season> seasons = seasonRepository.getByShow(page, size, ids);

            seasons.sort((s1, s2) -> s1.getReleaseDate().compareTo(s2.getReleaseDate()));

            return seasons;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
    
    public Integer getSeasonsByShowsPageCount(Integer size, UUID ...ids) {
        try {
            return seasonRepository.getPageCountByShow(size, ids);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // find the episodes using given id season
    public List<Episode> findEpisodesBySeasons(Integer page, Integer size, UUID... ids) {
        try {
            List<Episode> episodes = episodeRepository.getBySeason(page, size, ids);

            return episodes;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public Integer getEpisodesBySeasonsPageCount(Integer size, UUID ...ids) {
        try {
            return episodeRepository.getPageCountBySeason(size, ids);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<Genre> getShowGenres(UUID showId) {
        try {
            List<Genre> showGenres = showRepository.getGenres(showId);

            return showGenres;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Update genres of a show
    @Transactional
    public List<Genre> updateGenresOfShow(UUID showId, UUID... genreIds) {
        try {
            List<Genre> genres = showRepository.updateGenres(showId, genreIds);

            return genres;
        }
        catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    // Add a genre to show
    @Transactional
    public List<Genre> addGenresToShow(UUID showId, UUID... genreIds) {
        try {
            List<Genre> showGenres = showRepository.getGenres(showId);
            List<UUID> showGenresId = showGenres.stream()
                .map(genre -> genre.getId())
                .toList();
            
            List<UUID> genresIdList = List.of(genreIds);

            List<UUID> missingId = genresIdList.stream()
                .filter(id -> !showGenresId.contains(id))
                .toList()
                .stream()
                .distinct()
                .toList();

            List<Genre> addedToShowGenre = showRepository.addGenres(showId, missingId.toArray(new UUID[0]));
 
            return addedToShowGenre;
        }
        catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    public List<Show> findShowByGenre(String... names) {
        try {
            List<UUID> ids = genreRepository.getIdsByNames(names);

            List<Show> shows = showRepository.showByGenres(ids.toArray(new UUID[0]));

            return shows;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    public ViewHistory getViewHistoryOfAccountAndEpisode(UUID account, UUID episode) {
        try {
            ViewHistory viewHistory = viewHistoryRepository.getViewHistory(account, episode);

            return viewHistory;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        } 
    }

    public void addViewHistory(UUID account, UUID episode, Integer timestamp) {
        try {
            ViewHistory viewHistory = viewHistoryRepository.getViewHistory(account, episode);

            if (viewHistory == null) {
                viewHistory = ViewHistory.builder()
                    .episode(episode)
                    .account(account)
                    .createdTime(LocalDateTime.now())
                    .updatedTime(LocalDateTime.now())
                    .duration(timestamp)
                    .isDeleted(false)
                    .build();
                viewHistoryRepository.create(viewHistory);
                return;
            } 

            viewHistory.setUpdatedTime(LocalDateTime.now());
            viewHistory.setDuration(timestamp);

            viewHistoryRepository.updateViewHisotry(viewHistory);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    public List<ViewHistory> getViewHistoryAccount(Integer page, Integer size, UUID id) {
        try {
            List<ViewHistory> viewHistories = viewHistoryRepository.getViewHistoriesByAccount(page, size, id);

            return viewHistories;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    public Integer getViewHistoryAccountPageCount(Integer size, UUID id) {
        try {
            Integer pageCount = viewHistoryRepository.getViewHistoriesByAccountPageCount(size, id);
            return pageCount;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

}