package com.gameworld.app.service.impl;

import com.gameworld.app.service.GameTypeService;
import com.gameworld.app.domain.GameType;
import com.gameworld.app.repository.GameTypeRepository;
import com.gameworld.app.repository.search.GameTypeSearchRepository;
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
 * Service Implementation for managing GameType.
 */
@Service
@Transactional
public class GameTypeServiceImpl implements GameTypeService{

    private final Logger log = LoggerFactory.getLogger(GameTypeServiceImpl.class);
    
    @Inject
    private GameTypeRepository gameTypeRepository;

    @Inject
    private GameTypeSearchRepository gameTypeSearchRepository;

    /**
     * Save a gameType.
     *
     * @param gameType the entity to save
     * @return the persisted entity
     */
    public GameType save(GameType gameType) {
        log.debug("Request to save GameType : {}", gameType);
        GameType result = gameTypeRepository.save(gameType);
        gameTypeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the gameTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<GameType> findAll(Pageable pageable) {
        log.debug("Request to get all GameTypes");
        Page<GameType> result = gameTypeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one gameType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public GameType findOne(Long id) {
        log.debug("Request to get GameType : {}", id);
        GameType gameType = gameTypeRepository.findOne(id);
        return gameType;
    }

    /**
     *  Delete the  gameType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete GameType : {}", id);
        gameTypeRepository.delete(id);
        gameTypeSearchRepository.delete(id);
    }

    /**
     * Search for the gameType corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<GameType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GameTypes for query {}", query);
        Page<GameType> result = gameTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
