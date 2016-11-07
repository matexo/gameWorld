package com.gameworld.app.service;

import com.gameworld.app.domain.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Platform.
 */
public interface PlatformService {

    /**
     * Save a platform.
     *
     * @param platform the entity to save
     * @return the persisted entity
     */
    Platform save(Platform platform);

    /**
     *  Get all the platforms.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Platform> findAll(Pageable pageable);

    /**
     *  Get the "id" platform.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Platform findOne(Long id);

    /**
     *  Delete the "id" platform.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the platform corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Platform> search(String query, Pageable pageable);
}
