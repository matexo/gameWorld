package com.gameworld.app.service;

import com.gameworld.app.domain.GamerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing GamerProfile.
 */
public interface GamerProfileService {

    /**
     * Save a gamerProfile.
     *
     * @param gamerProfile the entity to save
     * @return the persisted entity
     */
    GamerProfile save(GamerProfile gamerProfile);

    /**
     *  Get all the gamerProfiles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GamerProfile> findAll(Pageable pageable);

    /**
     *  Get the "id" gamerProfile.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GamerProfile findOne(Long id);

    /**
     *  Delete the "id" gamerProfile.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the gamerProfile corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GamerProfile> search(String query, Pageable pageable);
}
