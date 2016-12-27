package com.gameworld.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gameworld.app.domain.MarketOffer;
import com.gameworld.app.service.MarketOfferService;
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
 * REST controller for managing MarketOffer.
 */
@RestController
@RequestMapping("/api")
public class MarketOfferResource {

    private final Logger log = LoggerFactory.getLogger(MarketOfferResource.class);

    @Inject
    private MarketOfferService marketOfferService;

    /**
     * POST  /market-offers : Create a new marketOffer.
     *
     * @param marketOffer the marketOffer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new marketOffer, or with status 400 (Bad Request) if the marketOffer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/market-offers")
    @Timed
    public ResponseEntity<MarketOffer> createMarketOffer(@Valid @RequestBody MarketOffer marketOffer) throws URISyntaxException {
        log.debug("REST request to save MarketOffer : {}", marketOffer);
        if (marketOffer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("marketOffer", "idexists", "A new marketOffer cannot already have an ID")).body(null);
        }
        MarketOffer result = marketOfferService.save(marketOffer);
        return ResponseEntity.created(new URI("/api/market-offers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("marketOffer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /market-offers : Updates an existing marketOffer.
     *
     * @param marketOffer the marketOffer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated marketOffer,
     * or with status 400 (Bad Request) if the marketOffer is not valid,
     * or with status 500 (Internal Server Error) if the marketOffer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/market-offers")
    @Timed
    public ResponseEntity<MarketOffer> updateMarketOffer(@Valid @RequestBody MarketOffer marketOffer) throws URISyntaxException {
        log.debug("REST request to update MarketOffer : {}", marketOffer);
        if (marketOffer.getId() == null) {
            return createMarketOffer(marketOffer);
        }
        MarketOffer result = marketOfferService.save(marketOffer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("marketOffer", marketOffer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /market-offers : get all the marketOffers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of marketOffers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/market-offers")
    @Timed
    public ResponseEntity<List<MarketOffer>> getAllMarketOffers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MarketOffers");
        Page<MarketOffer> page = marketOfferService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/market-offers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /market-offers/:id : get the "id" marketOffer.
     *
     * @param id the id of the marketOffer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the marketOffer, or with status 404 (Not Found)
     */
    @GetMapping("/market-offers/{id}")
    @Timed
    public ResponseEntity<MarketOffer> getMarketOffer(@PathVariable Long id) {
        log.debug("REST request to get MarketOffer : {}", id);
        MarketOffer marketOffer = marketOfferService.findOne(id);
        return Optional.ofNullable(marketOffer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /market-offers/:id : delete the "id" marketOffer.
     *
     * @param id the id of the marketOffer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/market-offers/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarketOffer(@PathVariable Long id) {
        log.debug("REST request to delete MarketOffer : {}", id);
        marketOfferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("marketOffer", id.toString())).build();
    }

    /**
     * SEARCH  /_search/market-offers?query=:query : search for the marketOffer corresponding
     * to the query.
     *
     * @param query the query of the marketOffer search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/market-offers")
    @Timed
    public ResponseEntity<List<MarketOffer>> searchMarketOffers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of MarketOffers for query {}", query);
        Page<MarketOffer> page = marketOfferService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/market-offers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/market-offers/my")
    public ResponseEntity<List<MarketOffer>> getMarketOffersForProfile(Pageable pageable) throws URISyntaxException {
        Page<MarketOffer> page = marketOfferService.findAllMarketOfferCreatedByUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/market-offers/my");
        return new ResponseEntity<>(page.getContent() , headers , HttpStatus.OK);
    }

    @PutMapping("/market-offers/buy")
    public ResponseEntity<Void> finalizeOffer(@RequestBody MarketOffer marketOffer) {
        log.debug("REST request to finalize MarketOffer : {}", marketOffer.getId());
        marketOfferService.finalizeOffer(marketOffer.getId());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("marketOffer", marketOffer.getId().toString())).build();
    }

    @GetMapping("/market-offers/ended")
    public ResponseEntity<List<MarketOffer>> getMarketOffersEndByUser(Pageable pageable) throws URISyntaxException {
        Page<MarketOffer> page = marketOfferService.findMarketOffersEndByUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/market-offers/ended");
        return new ResponseEntity<>(page.getContent() , headers , HttpStatus.OK);
    }

    @GetMapping("/market-offers/my/ended")
    public ResponseEntity<List<MarketOffer>> getEndedMarketOffers(Pageable pageable) throws URISyntaxException {
        Page<MarketOffer> page = marketOfferService.findEndedMarketOffers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/market-offers/my/ended");
        return new ResponseEntity<>(page.getContent() , headers , HttpStatus.OK);
    }

}
