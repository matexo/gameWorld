package com.gameworld.app.service.impl;

import com.gameworld.app.domain.User;
import com.gameworld.app.domain.enumeration.OfferStatus;
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
import org.thymeleaf.util.DateUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    /**
     * Save a marketOffer.
     *
     * @param marketOffer the entity to save
     * @return the persisted entity
     */
    public MarketOffer save(MarketOffer marketOffer) {
        log.debug("Request to save MarketOffer : {}", marketOffer);
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if(user.isPresent())
            marketOffer.createProfile(user.get().getGamerProfile());
        else return null;
        if(marketOffer.getOfferStatus() == null) marketOffer.offerStatus(OfferStatus.NEW);
        if(marketOffer.getCreateDate() == null) marketOffer.createDate(DateUtil.getNowDateTime());
        MarketOffer result = marketOfferRepository.save(marketOffer);
        marketOfferSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the marketOffers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MarketOffer> findAll(Pageable pageable) {
        log.debug("Request to get all MarketOffers");
        Page<MarketOffer> result = marketOfferRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one marketOffer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MarketOffer findOne(Long id) {
        log.debug("Request to get MarketOffer : {}", id);
        MarketOffer marketOffer = marketOfferRepository.findOne(id);
        return marketOffer;
    }

    /**
     *  Delete the  marketOffer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MarketOffer : {}", id);
        marketOfferRepository.delete(id);
        marketOfferSearchRepository.delete(id);
    }

    /**
     * Search for the marketOffer corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MarketOffer> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MarketOffers for query {}", query);
        Page<MarketOffer> result = marketOfferSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
