package com.gameworld.app.service;

import com.gameworld.app.domain.GameType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing GameType.
 */
public interface GameTypeService {

    /**
     * Save a gameType.
     *
     * @param gameType the entity to save
     * @return the persisted entity
     */
    GameType save(GameType gameType);

    /**
     *  Get all the gameTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GameType> findAll(Pageable pageable);

    /**
     *  Get the "id" gameType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GameType findOne(Long id);

    /**
     *  Delete the "id" gameType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the gameType corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GameType> search(String query, Pageable pageable);
}
