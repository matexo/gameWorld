package com.gameworld.app.service.impl;

import com.gameworld.app.domain.GamerProfile;
import com.gameworld.app.domain.User;
import com.gameworld.app.repository.GamerProfileRepository;
import com.gameworld.app.repository.UserRepository;
import com.gameworld.app.security.SecurityUtils;
import com.gameworld.app.service.GameService;
import com.gameworld.app.domain.Game;
import com.gameworld.app.repository.GameRepository;
import com.gameworld.app.repository.search.GameSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Game.
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    @Inject
    private GameRepository gameRepository;

    @Inject
    private GameSearchRepository gameSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private GamerProfileRepository gamerProfileRepository;

    public Game save(Game game) {
        log.debug("Request to save Game : {}", game);
        Game result = gameRepository.save(game);
        gameSearchRepository.save(result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<Game> findAll(Pageable pageable) {
        log.debug("Request to get all Games");
        Page<Game> result = gameRepository.findAll(pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public Game findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        Game game = gameRepository.findOne(id);
        return game;
    }

    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.delete(id);
        gameSearchRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<Game> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Games for query {}", query);
        Page<Game> result = gameSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Transactional
    public void addGameToWishList(Long gameId) {
        GamerProfile gamerProfile = gamerProfileRepository.findGamerProfileByName(SecurityUtils.getCurrentUserLogin());
        Game game = gameRepository.findOne(gameId);
        if(gamerProfile != null && game != null) {
            gamerProfile.addSearchedGames(game);
            gamerProfileRepository.save(gamerProfile);
        }
    }


    @Transactional(readOnly = true)
    public Page<Game> getGamesFromWishlist(Pageable pageable) {
        return gameRepository.getGamesFromWishlist(SecurityUtils.getCurrentUserLogin() , pageable);
    }

    @Transactional
    public void removeGameFromWishlist(Long gameId) {
        GamerProfile gamerProfile = gamerProfileRepository.findGamerProfileByName(SecurityUtils.getCurrentUserLogin());
        if(gamerProfile != null) {
            Game gameToRemove = null;
            for(Game gameFromProfile : gamerProfile.getSearchedGames())
                if(gameFromProfile.getId().equals(gameId))
                    gameToRemove = gameFromProfile;
            gamerProfile.removeSearchedGames(gameToRemove);
            gamerProfileRepository.save(gamerProfile);
        }
    }


}
