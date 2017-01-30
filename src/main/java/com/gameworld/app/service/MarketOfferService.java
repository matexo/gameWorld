package com.gameworld.app.service;

import com.gameworld.app.domain.Comment;
import com.gameworld.app.domain.MarketOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing MarketOffer.
 */
public interface MarketOfferService {

    MarketOffer save(MarketOffer marketOffer);
    Page<MarketOffer> findAll(Pageable pageable , String username);
    MarketOffer findOne(Long id);
    void delete(Long id);

    Page<MarketOffer> search(String query, Pageable pageable);

    Page<MarketOffer> findAllMarketOfferCreatedByUser(Pageable pageable);
    void finalizeOffer(Long id);
    Page<MarketOffer> findMarketOffersEndByUser(Pageable pageable);
    Page<MarketOffer> findEndedMarketOffers(Pageable pageable);
    void cancelMarketOffer( Long marketOfferId);
}
