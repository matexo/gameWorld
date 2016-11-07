package com.gameworld.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gameworld.app.domain.GamerProfile;
import com.gameworld.app.service.GamerProfileService;
import com.gameworld.app.web.rest.util.HeaderUtil;
import com.gameworld.app.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing GamerProfile.
 */
@RestController
@RequestMapping("/api")
public class GamerProfileResource {

    private final Logger log = LoggerFactory.getLogger(GamerProfileResource.class);
        
    @Inject
    private GamerProfileService gamerProfileService;

    /**
     * POST  /gamer-profiles : Create a new gamerProfile.
     *
     * @param gamerProfile the gamerProfile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gamerProfile, or with status 400 (Bad Request) if the gamerProfile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gamer-profiles")
    @Timed
    public ResponseEntity<GamerProfile> createGamerProfile(@RequestBody GamerProfile gamerProfile) throws URISyntaxException {
        log.debug("REST request to save GamerProfile : {}", gamerProfile);
        if (gamerProfile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("gamerProfile", "idexists", "A new gamerProfile cannot already have an ID")).body(null);
        }
        GamerProfile result = gamerProfileService.save(gamerProfile);
        return ResponseEntity.created(new URI("/api/gamer-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("gamerProfile", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gamer-profiles : Updates an existing gamerProfile.
     *
     * @param gamerProfile the gamerProfile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gamerProfile,
     * or with status 400 (Bad Request) if the gamerProfile is not valid,
     * or with status 500 (Internal Server Error) if the gamerProfile couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gamer-profiles")
    @Timed
    public ResponseEntity<GamerProfile> updateGamerProfile(@RequestBody GamerProfile gamerProfile) throws URISyntaxException {
        log.debug("REST request to update GamerProfile : {}", gamerProfile);
        if (gamerProfile.getId() == null) {
            return createGamerProfile(gamerProfile);
        }
        GamerProfile result = gamerProfileService.save(gamerProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("gamerProfile", gamerProfile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gamer-profiles : get all the gamerProfiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gamerProfiles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/gamer-profiles")
    @Timed
    public ResponseEntity<List<GamerProfile>> getAllGamerProfiles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of GamerProfiles");
        Page<GamerProfile> page = gamerProfileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gamer-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gamer-profiles/:id : get the "id" gamerProfile.
     *
     * @param id the id of the gamerProfile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gamerProfile, or with status 404 (Not Found)
     */
    @GetMapping("/gamer-profiles/{id}")
    @Timed
    public ResponseEntity<GamerProfile> getGamerProfile(@PathVariable Long id) {
        log.debug("REST request to get GamerProfile : {}", id);
        GamerProfile gamerProfile = gamerProfileService.findOne(id);
        return Optional.ofNullable(gamerProfile)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gamer-profiles/:id : delete the "id" gamerProfile.
     *
     * @param id the id of the gamerProfile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gamer-profiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteGamerProfile(@PathVariable Long id) {
        log.debug("REST request to delete GamerProfile : {}", id);
        gamerProfileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gamerProfile", id.toString())).build();
    }

    /**
     * SEARCH  /_search/gamer-profiles?query=:query : search for the gamerProfile corresponding
     * to the query.
     *
     * @param query the query of the gamerProfile search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/gamer-profiles")
    @Timed
    public ResponseEntity<List<GamerProfile>> searchGamerProfiles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of GamerProfiles for query {}", query);
        Page<GamerProfile> page = gamerProfileService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/gamer-profiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
