package com.cineflex.API.controller;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.cineflex.API.model.Account;
import com.cineflex.API.model.Comment;
import com.cineflex.API.model.Episode;
import com.cineflex.API.model.Favorite;
import com.cineflex.API.model.Genre;
import com.cineflex.API.model.Like;
import com.cineflex.API.model.Rating;
import com.cineflex.API.model.ReportComment;
import com.cineflex.API.model.Season;
import com.cineflex.API.model.Show;
import com.cineflex.API.model.ShowGenre;
import com.cineflex.API.model.Subscription;
import com.cineflex.API.model.VerificationToken;
import com.cineflex.API.model.ViewHistory;
import com.cineflex.API.repository.AccountRepository;
import com.cineflex.API.repository.CommentRepository;
import com.cineflex.API.repository.EpisodeRepository;
import com.cineflex.API.repository.FavoriteRepository;
import com.cineflex.API.repository.GenreRepository;
import com.cineflex.API.repository.LikeRepository;
import com.cineflex.API.repository.RatingRepository;
import com.cineflex.API.repository.ReportCommentRepository;
import com.cineflex.API.repository.SeasonRepository;
import com.cineflex.API.repository.ShowGenreRepository;
import com.cineflex.API.repository.ShowRepository;
import com.cineflex.API.repository.SubscriptionRepository;
import com.cineflex.API.repository.VerificationTokenRepository;
import com.cineflex.API.repository.ViewHistoryRepository;





@RestController
public class TestAPI {
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;
    private final EpisodeRepository episodeRepository;
    private final FavoriteRepository favoriteRepository;
    private final GenreRepository genreRepository;
    private final LikeRepository likeRepository;
    private final RatingRepository ratingRepository;
    private final ReportCommentRepository reportCommentRepository;
    private final SeasonRepository seasonRepository;
    private final ShowGenreRepository showGenreRepository;
    private final ShowRepository showRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ViewHistoryRepository viewHistoryRepository;

    public TestAPI (
        AccountRepository accountRepository, // done
        CommentRepository commentRepository, // done
        EpisodeRepository episodeRepository, // done
        FavoriteRepository favoriteRepository, // done
        GenreRepository genreRepository, // aaa
        LikeRepository likeRepository, // done
        RatingRepository ratingRepository,
        ReportCommentRepository reportCommentRepository, // done
        SeasonRepository seasonRepository, // done
        ShowGenreRepository showGenreRepository, //aaa
        ShowRepository showRepository, //aaa
        SubscriptionRepository subscriptionRepository, // done
        VerificationTokenRepository verificationTokenRepository, // done
        ViewHistoryRepository viewHistoryRepository
    ) {
        this.accountRepository = accountRepository;
        this.commentRepository = commentRepository;
        this.episodeRepository = episodeRepository;
        this.favoriteRepository = favoriteRepository;
        this.genreRepository = genreRepository;
        this.likeRepository = likeRepository;
        this.ratingRepository = ratingRepository;
        this.reportCommentRepository = reportCommentRepository;
        this.seasonRepository = seasonRepository;
        this.showGenreRepository = showGenreRepository;
        this.showRepository = showRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.viewHistoryRepository = viewHistoryRepository;
    }

    @GetMapping("/add")
    public int addTeString() {
        ViewHistory viewHistory = ViewHistory.builder()
            .account(UUID.fromString("5f854c50-2116-4cc1-a31a-8f66643c091e"))
            .episode(UUID.fromString("acc3f0a0-fcdc-4317-9350-cb63862f5eee"))
            .watchedTime(LocalDateTime.now())
            .duration(5)
            .build();
        
        viewHistoryRepository.create(viewHistory);

        return 0;
    }
    

    @GetMapping("/list")
    public List<ViewHistory> test() {

        return viewHistoryRepository.readAll();
    }

    @GetMapping("/single")
    public VerificationToken getSingle() {
        return verificationTokenRepository.read(UUID.fromString("0641d0fd-4741-41ab-9f87-dffee91193f3"));
    }

    @GetMapping("/update")
    public String update() {
        VerificationToken verificationToken = VerificationToken.builder()
            .account(UUID.fromString("464007fa-f3b4-47aa-9bd6-3c0d9b3bdd33"))
            .token("adasdasdasdasd qe12312312")
            .build();
        
        verificationTokenRepository.update(UUID.fromString("0641d0fd-4741-41ab-9f87-dffee91193f3") ,verificationToken);
        return new String();
    }
    
    
    @GetMapping("/delete")
    public String deleteString() {
        verificationTokenRepository.delete(UUID.fromString("0641d0fd-4741-41ab-9f87-dffee91193f3"));
        return new String();
    }
    
    
}
