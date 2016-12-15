package com.gameworld.app.service;

import com.gameworld.app.domain.MarketOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing MarketOffer.
 */
public interface MarketOfferService {

    /**
     * Save a marketOffer.
     *
     * @param marketOffer the entity to save
     * @return the persisted entity
     */
    MarketOffer save(MarketOffer marketOffer);

    /**
     *  Get all the marketOffers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MarketOffer> findAll(Pageable pageable);

    /**
     *  Get the "id" marketOffer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MarketOffer findOne(Long id);

    /**
     *  Delete the "id" marketOffer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the marketOffer corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MarketOffer> search(String query, Pageable pageable);

    Page<MarketOffer> findAllMarketOfferCreatedByUser(Pageable pageable);
}
