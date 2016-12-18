package com.gameworld.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gameworld.app.domain.TradeOffer;
import com.gameworld.app.service.TradeOfferService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TradeOffer.
 */
@RestController
@RequestMapping("/api")
public class TradeOfferResource {

    private final Logger log = LoggerFactory.getLogger(TradeOfferResource.class);

    @Inject
    private TradeOfferService tradeOfferService;

    /**
     * POST  /trade-offers : Create a new tradeOffer.
     *
     * @param tradeOffer the tradeOffer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tradeOffer, or with status 400 (Bad Request) if the tradeOffer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trade-offers")
    @Timed
    public ResponseEntity<TradeOffer> createTradeOffer(@Valid @RequestBody TradeOffer tradeOffer) throws URISyntaxException {
        log.debug("REST request to save TradeOffer : {}", tradeOffer);
        if (tradeOffer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tradeOffer", "idexists", "A new tradeOffer cannot already have an ID")).body(null);
        }
        TradeOffer result = tradeOfferService.addNewTradeOffer(tradeOffer);
        return ResponseEntity.created(new URI("/api/trade-offers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tradeOffer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trade-offers : Updates an existing tradeOffer.
     *
     * @param tradeOffer the tradeOffer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tradeOffer,
     * or with status 400 (Bad Request) if the tradeOffer is not valid,
     * or with status 500 (Internal Server Error) if the tradeOffer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trade-offers")
    @Timed
    public ResponseEntity<TradeOffer> updateTradeOffer(@Valid @RequestBody TradeOffer tradeOffer) throws URISyntaxException {
        log.debug("REST request to update TradeOffer : {}", tradeOffer);
        if (tradeOffer.getId() == null) {
            return createTradeOffer(tradeOffer);
        }
        TradeOffer result = tradeOfferService.save(tradeOffer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tradeOffer", tradeOffer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trade-offers : get all the tradeOffers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tradeOffers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/trade-offers")
    @Timed
    public ResponseEntity<List<TradeOffer>> getAllTradeOffers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TradeOffers");
        Page<TradeOffer> page = tradeOfferService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trade-offers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /trade-offers/:id : get the "id" tradeOffer.
     *
     * @param id the id of the tradeOffer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tradeOffer, or with status 404 (Not Found)
     */
    @GetMapping("/trade-offers/{id}")
    @Timed
    public ResponseEntity<TradeOffer> getTradeOffer(@PathVariable Long id) {
        log.debug("REST request to get TradeOffer : {}", id);
        TradeOffer tradeOffer = tradeOfferService.findOne(id);
        return Optional.ofNullable(tradeOffer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trade-offers/:id : delete the "id" tradeOffer.
     *
     * @param id the id of the tradeOffer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trade-offers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTradeOffer(@PathVariable Long id) {
        log.debug("REST request to delete TradeOffer : {}", id);
        tradeOfferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tradeOffer", id.toString())).build();
    }

    /**
     * SEARCH  /_search/trade-offers?query=:query : search for the tradeOffer corresponding
     * to the query.
     *
     * @param query the query of the tradeOffer search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/trade-offers")
    @Timed
    public ResponseEntity<List<TradeOffer>> searchTradeOffers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of TradeOffers for query {}", query);
        Page<TradeOffer> page = tradeOfferService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/trade-offers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
