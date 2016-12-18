package com.gameworld.app.service.impl;

import com.gameworld.app.domain.MarketOffer;
import com.gameworld.app.domain.User;
import com.gameworld.app.domain.enumeration.TradeOfferStatus;
import com.gameworld.app.repository.MarketOfferRepository;
import com.gameworld.app.repository.UserRepository;
import com.gameworld.app.security.SecurityUtils;
import com.gameworld.app.service.TradeOfferService;
import com.gameworld.app.domain.TradeOffer;
import com.gameworld.app.repository.TradeOfferRepository;
import com.gameworld.app.repository.search.TradeOfferSearchRepository;
import com.gameworld.app.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TradeOffer.
 */
@Service
@Transactional
public class TradeOfferServiceImpl implements TradeOfferService{

    private final Logger log = LoggerFactory.getLogger(TradeOfferServiceImpl.class);

    @Inject
    private TradeOfferRepository tradeOfferRepository;

    @Inject
    private TradeOfferSearchRepository tradeOfferSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MarketOfferRepository marketOfferRepository;

    /**
     * Save a tradeOffer.
     *
     * @param tradeOffer the entity to save
     * @return the persisted entity
     */
    public TradeOffer save(TradeOffer tradeOffer) {
        log.debug("Request to save TradeOffer : {}", tradeOffer);
        TradeOffer result = tradeOfferRepository.save(tradeOffer);
        tradeOfferSearchRepository.save(result);
        return result;
    }

    public TradeOffer addNewTradeOffer(TradeOffer tradeOffer) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if(user.isPresent())
            tradeOffer.setCreateProfile(user.get().getGamerProfile());
        else return null;
        tradeOffer.setStatus(TradeOfferStatus.PENDING);
        tradeOffer.setTimestamp(DateUtil.getNowDateTime());
        MarketOffer marketOfferFromDB = marketOfferRepository.findOne(tradeOffer.getMarketOffer().getId());
        if(marketOfferFromDB != null && marketOfferFromDB.isCurrent())
            tradeOffer.setMarketOffer(marketOfferFromDB);
        else return null;
        TradeOffer tradeOfferFromDB = tradeOfferRepository.save(tradeOffer);
        tradeOfferSearchRepository.save(tradeOfferFromDB);
        return tradeOfferFromDB;
    }

    /**
     *  Get all the tradeOffers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TradeOffer> findAll(Pageable pageable) {
        log.debug("Request to get all TradeOffers");
        Page<TradeOffer> result = tradeOfferRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one tradeOffer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TradeOffer findOne(Long id) {
        log.debug("Request to get TradeOffer : {}", id);
        TradeOffer tradeOffer = tradeOfferRepository.findOneWithEagerRelationships(id);
        return tradeOffer;
    }

    /**
     *  Delete the  tradeOffer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TradeOffer : {}", id);
        tradeOfferRepository.delete(id);
        tradeOfferSearchRepository.delete(id);
    }

    /**
     * Search for the tradeOffer corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TradeOffer> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TradeOffers for query {}", query);
        Page<TradeOffer> result = tradeOfferSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
