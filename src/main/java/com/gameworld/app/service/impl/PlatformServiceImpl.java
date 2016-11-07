package com.gameworld.app.service.impl;

import com.gameworld.app.service.PlatformService;
import com.gameworld.app.domain.Platform;
import com.gameworld.app.repository.PlatformRepository;
import com.gameworld.app.repository.search.PlatformSearchRepository;
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
 * Service Implementation for managing Platform.
 */
@Service
@Transactional
public class PlatformServiceImpl implements PlatformService{

    private final Logger log = LoggerFactory.getLogger(PlatformServiceImpl.class);
    
    @Inject
    private PlatformRepository platformRepository;

    @Inject
    private PlatformSearchRepository platformSearchRepository;

    /**
     * Save a platform.
     *
     * @param platform the entity to save
     * @return the persisted entity
     */
    public Platform save(Platform platform) {
        log.debug("Request to save Platform : {}", platform);
        Platform result = platformRepository.save(platform);
        platformSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the platforms.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Platform> findAll(Pageable pageable) {
        log.debug("Request to get all Platforms");
        Page<Platform> result = platformRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one platform by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Platform findOne(Long id) {
        log.debug("Request to get Platform : {}", id);
        Platform platform = platformRepository.findOne(id);
        return platform;
    }

    /**
     *  Delete the  platform by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Platform : {}", id);
        platformRepository.delete(id);
        platformSearchRepository.delete(id);
    }

    /**
     * Search for the platform corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Platform> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Platforms for query {}", query);
        Page<Platform> result = platformSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
