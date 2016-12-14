package com.gameworld.app.service.impl;

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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Game.
 */
@Service
@Transactional
public class GameServiceImpl implements GameService{

    private final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);
    
    @Inject
    private GameRepository gameRepository;

    @Inject
    private GameSearchRepository gameSearchRepository;

    /**
     * Save a game.
     *
     * @param game the entity to save
     * @return the persisted entity
     */
    public Game save(Game game) {
        log.debug("Request to save Game : {}", game);
        Game result = gameRepository.save(game);
        gameSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the games.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Game> findAll(Pageable pageable) {
        log.debug("Request to get all Games");
        Page<Game> result = gameRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one game by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Game findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        Game game = gameRepository.findOne(id);
        return game;
    }

    /**
     *  Delete the  game by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.delete(id);
        gameSearchRepository.delete(id);
    }

    /**
     * Search for the game corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Game> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Games for query {}", query);
        Page<Game> result = gameSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
