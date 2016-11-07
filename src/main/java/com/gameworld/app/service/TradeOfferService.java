package com.gameworld.app.service;

import com.gameworld.app.domain.TradeOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing TradeOffer.
 */
public interface TradeOfferService {

    /**
     * Save a tradeOffer.
     *
     * @param tradeOffer the entity to save
     * @return the persisted entity
     */
    TradeOffer save(TradeOffer tradeOffer);

    /**
     *  Get all the tradeOffers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TradeOffer> findAll(Pageable pageable);

    /**
     *  Get the "id" tradeOffer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TradeOffer findOne(Long id);

    /**
     *  Delete the "id" tradeOffer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the tradeOffer corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TradeOffer> search(String query, Pageable pageable);
}
