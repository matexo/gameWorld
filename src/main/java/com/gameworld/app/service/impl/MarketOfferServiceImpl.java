package com.gameworld.app.service.impl;

import com.gameworld.app.domain.*;
import com.gameworld.app.domain.enumeration.OfferStatus;
import com.gameworld.app.repository.CommentRepository;
import com.gameworld.app.repository.GamerProfileRepository;
import com.gameworld.app.repository.UserRepository;
import com.gameworld.app.repository.search.CommentSearchRepository;
import com.gameworld.app.security.SecurityUtils;
import com.gameworld.app.service.MarketOfferService;
import com.gameworld.app.repository.MarketOfferRepository;
import com.gameworld.app.repository.search.MarketOfferSearchRepository;
import com.gameworld.app.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.Mark;

import javax.inject.Inject;
import java.util.Optional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MarketOffer.
 */
@Service
@Transactional
public class MarketOfferServiceImpl implements MarketOfferService {

    private final Logger log = LoggerFactory.getLogger(MarketOfferServiceImpl.class);

    @Inject
    private MarketOfferRepository marketOfferRepository;

    @Inject
    private MarketOfferSearchRepository marketOfferSearchRepository;

    @Inject
    private UserRepository userRepository;


    public MarketOffer save(MarketOffer marketOffer) {
        log.debug("Request to save MarketOffer : {}", marketOffer);
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        GamerProfile authorProfile = null;
        if (user.isPresent())
            authorProfile = user.get().getGamerProfile();
        marketOffer.initNewMarketOffer(authorProfile, DateUtil.getNowDateTime());
        MarketOffer result = marketOfferRepository.save(marketOffer);
        marketOfferSearchRepository.save(result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<MarketOffer> findAll(Pageable pageable, String username) {
        log.debug("Request to get all MarketOffers");
        Page<MarketOffer> result = marketOfferRepository.findAllCurrentOffers(username, pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public MarketOffer findOne(Long id) {
        log.debug("Request to get MarketOffer : {}", id);
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Long profileId = null;
        if (user.isPresent())
            profileId = user.get().getGamerProfile().getId();
        MarketOffer marketOffer = marketOfferRepository.findOne(id);
        if (!marketOffer.getCreateProfile().getId().equals(profileId))
            marketOffer.setOffers(null);
        return marketOffer;
    }

    public void delete(Long id) {
        log.debug("Request to delete MarketOffer : {}", id);
        marketOfferRepository.delete(id);
        marketOfferSearchRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<MarketOffer> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MarketOffers for query {}", query);
        Page<MarketOffer> result = marketOfferSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<MarketOffer> findAllMarketOfferCreatedByUser(Pageable pageable) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Page<MarketOffer> marketOffers = null;
        if (user.isPresent())
            marketOffers = marketOfferRepository.findAllMarketOfferCreatedByUser(user.get().getGamerProfile().getId(), pageable);
        return marketOffers;
    }

    @Transactional
    public void finalizeOffer(Long id) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (user.isPresent()) {
            MarketOffer marketOffer = marketOfferRepository.findOne(id);
            if (marketOffer != null && marketOffer.isCurrent()) {
                marketOffer.finalizeOffer(user.get().getGamerProfile());
                MarketOffer marketOfferFromDB = marketOfferRepository.save(marketOffer);
                marketOfferSearchRepository.save(marketOfferFromDB);
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<MarketOffer> findMarketOffersEndByUser(Pageable pageable) {
        Page<MarketOffer> marketOffers = null;
        String username = SecurityUtils.getCurrentUserLogin();
        marketOffers = marketOfferRepository.findMarketOffersEndByUser(username, pageable);
        return marketOffers;
    }

    @Transactional(readOnly = true)
    public Page<MarketOffer> findEndedMarketOffers(Pageable pageable) {
        Page<MarketOffer> marketOffers = null;
        String username = SecurityUtils.getCurrentUserLogin();
        marketOffers = marketOfferRepository.findEndedMarketOffers(username, pageable);
        return marketOffers;
    }

    @Transactional
    public void cancelMarketOffer(Long marketOfferId) {
        String username = SecurityUtils.getCurrentUserLogin();
        MarketOffer marketOffer = marketOfferRepository.findOne(marketOfferId);
        if (marketOffer != null && marketOffer.getCreateProfile().getName().equals(username) && marketOffer.getOfferStatus().equals(OfferStatus.NEW)) {
            marketOffer.setOfferStatus(OfferStatus.CANCELLED);
            marketOffer.setEndDate(DateUtil.getNowDateTime());
            marketOffer = marketOfferRepository.save(marketOffer);
            marketOfferSearchRepository.save(marketOffer);
        }
    }


}
