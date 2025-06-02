package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cineflex.api.model.Episode;
import com.cineflex.api.model.Season;
import com.cineflex.api.model.Show;
import com.cineflex.api.repository.EpisodeRepository;
import com.cineflex.api.repository.SeasonRepository;
import com.cineflex.api.repository.ShowRepository;


// This service handle everything that is related to shows (show, episode, season,...)
@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;

    // Inject repository to service
    public ShowService (
        ShowRepository showRepository,
        SeasonRepository seasonRepository,
        EpisodeRepository episodeRepository
    ) {
        this.showRepository = showRepository;
        this.seasonRepository = seasonRepository;
        this.episodeRepository = episodeRepository;
    }

    /* ---- INSERT METHOD ---- */
    public Show addShow(Show show) {
        UUID id = UUID.randomUUID(); // Use uuid to create unique id for record
        show.setId(id); // Set the id to the show
        show.setCreatedTime(LocalDateTime.now());
        show.setUpdatedTime(LocalDateTime.now());

        showRepository.create(show); // Add show

        return showRepository.read(id);
    }

    public Season addSeason(Season season) {
        UUID id = UUID.randomUUID();
        season.setId(id);
        season.setCreatedTime(LocalDateTime.now());
        season.setUpdatedTime(LocalDateTime.now());

        seasonRepository.create(season);

        return seasonRepository.read(id);
    }

    public Episode addEpisode(Episode episode) {
        UUID id = UUID.randomUUID();
        episode.setId(id);
        episode.setCreatedTime(LocalDateTime.now());
        episode.setUpdatedTime(LocalDateTime.now());

        episodeRepository.create(episode);

        return episodeRepository.read(id);
    }



    /* ---- UPDATE METHOD ---- */
    public Show updateShow(Show show) {
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

    public Season updateSeason(Season season) {
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

    public Episode updateEpisode(Episode episode) {
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

    /* ---- DELETE METHOD ---- */
    @Transactional
    public void deleteShow(UUID... ids) {
        List<UUID> seasons = seasonRepository.getByShow(ids).stream().map(s -> s.getId()).toList(); // get all seasons id related to shows
        deleteSeason(seasons.toArray(new UUID[0])); // delete the seasons
        showRepository.delete(ids); // Delete with repository using given information
    } 

    @Transactional
    public void deleteSeason(UUID... ids) {
        List<UUID> episodes = episodeRepository.getBySeason(ids).stream().map(e -> e.getId()).toList(); // get all episodes id related to seasons
        deleteEpisode(episodes.toArray(new UUID[0])); // delete all the episodes
        seasonRepository.delete(ids);
    }

    @Transactional
    public void deleteEpisode(UUID... ids) {
        episodeRepository.delete(ids);
    }

    /* --- READ METHOD --- */
    // Find by id
    public Show findShowById(UUID id) {
        return showRepository.read(id);
    }

    public Season findSeasonById(UUID id) {
        return seasonRepository.read(id);
    }

    public Episode findEpisodeById(UUID id) {
        return episodeRepository.read(id);
    }

    // Find all
    public List<Show> findAllShows() {
        return showRepository.readAll();
    }

    public List<Season> findAllSeasons() {
        return seasonRepository.readAll();
    }

    public List<Episode> findAllEpisodes() {
        return episodeRepository.readAll();
    }

    // find the season using the given id show, this could be multiple id
    public List<Season> findSeaonsByShows(UUID... ids) {
        return seasonRepository.getByShow(ids);
    }

    // find the episodes using given id season
    public List<Episode> findEpisodesBySeasons(UUID... ids) {
        return episodeRepository.getBySeason(ids);
    }


}