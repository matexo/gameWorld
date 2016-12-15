package com.gameworld.app.service.impl;

import com.gameworld.app.domain.GamerProfile;
import com.gameworld.app.domain.User;
import com.gameworld.app.repository.UserRepository;
import com.gameworld.app.security.SecurityUtils;
import com.gameworld.app.service.MarketOfferService;
import com.gameworld.app.domain.MarketOffer;
import com.gameworld.app.repository.MarketOfferRepository;
import com.gameworld.app.repository.search.MarketOfferSearchRepository;
import com.gameworld.app.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.util.Optional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MarketOffer.
 */
@Service
@Transactional
public class MarketOfferServiceImpl implements MarketOfferService{

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
        if(user.isPresent())
            authorProfile = user.get().getGamerProfile();
        marketOffer.initNewMarketOffer(authorProfile , DateUtil.getNowDateTime());
        //walidacja?
        MarketOffer result = marketOfferRepository.save(marketOffer);
        marketOfferSearchRepository.save(result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<MarketOffer> findAll(Pageable pageable) {
        log.debug("Request to get all MarketOffers");
        Page<MarketOffer> result = marketOfferRepository.findAll(pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public MarketOffer findOne(Long id) {
        log.debug("Request to get MarketOffer : {}", id);
        MarketOffer marketOffer = marketOfferRepository.findOne(id);
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
        if(user.isPresent())
            marketOffers = marketOfferRepository.findAllMarketOfferCreatedByUser(user.get().getGamerProfile().getId() , pageable);
        return marketOffers;
    }
}
