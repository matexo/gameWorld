package com.gameworld.app.service.impl;

import com.gameworld.app.service.GamerProfileService;
import com.gameworld.app.domain.GamerProfile;
import com.gameworld.app.repository.GamerProfileRepository;
import com.gameworld.app.repository.search.GamerProfileSearchRepository;
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
 * Service Implementation for managing GamerProfile.
 */
@Service
@Transactional
public class GamerProfileServiceImpl implements GamerProfileService{

    private final Logger log = LoggerFactory.getLogger(GamerProfileServiceImpl.class);
    
    @Inject
    private GamerProfileRepository gamerProfileRepository;

    @Inject
    private GamerProfileSearchRepository gamerProfileSearchRepository;

    /**
     * Save a gamerProfile.
     *
     * @param gamerProfile the entity to save
     * @return the persisted entity
     */
    public GamerProfile save(GamerProfile gamerProfile) {
        log.debug("Request to save GamerProfile : {}", gamerProfile);
        GamerProfile result = gamerProfileRepository.save(gamerProfile);
        gamerProfileSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the gamerProfiles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<GamerProfile> findAll(Pageable pageable) {
        log.debug("Request to get all GamerProfiles");
        Page<GamerProfile> result = gamerProfileRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one gamerProfile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public GamerProfile findOne(Long id) {
        log.debug("Request to get GamerProfile : {}", id);
        GamerProfile gamerProfile = gamerProfileRepository.findOneWithEagerRelationships(id);
        return gamerProfile;
    }

    /**
     *  Delete the  gamerProfile by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete GamerProfile : {}", id);
        gamerProfileRepository.delete(id);
        gamerProfileSearchRepository.delete(id);
    }

    /**
     * Search for the gamerProfile corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<GamerProfile> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GamerProfiles for query {}", query);
        Page<GamerProfile> result = gamerProfileSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
