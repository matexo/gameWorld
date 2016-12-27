package com.gameworld.app.service;

import com.gameworld.app.domain.TradeOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing TradeOffer.
 */
public interface TradeOfferService {

    TradeOffer save(TradeOffer tradeOffer);
    Page<TradeOffer> findAll(Pageable pageable);
    TradeOffer findOne(Long id);
    void delete(Long id);

    Page<TradeOffer> search(String query, Pageable pageable);

    TradeOffer addNewTradeOffer(TradeOffer tradeOffer);
    Page<TradeOffer> findAllCreatedByUser(Pageable pageable);
    Page<TradeOffer> findAllAssignedToUser(Pageable pageable);
    boolean acceptTradeOffer(Long tradeOfferId);
    boolean rejectTradeOffer(Long tradeOfferId);
    boolean cancelTradeOffer(Long tradeOfferId);
}
