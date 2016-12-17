package com.gameworld.app.repository;

import com.gameworld.app.domain.Conversation;
import com.gameworld.app.domain.MarketOffer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the MarketOffer entity.
 */
@SuppressWarnings("unused")
public interface MarketOfferRepository extends JpaRepository<MarketOffer,Long> {

    @Query(value = "SELECT marketOffer FROM MarketOffer marketOffer JOIN FETCH marketOffer.createProfile gp WHERE gp.id = :id",
        countQuery = "SELECT count(marketOffer) FROM MarketOffer marketOffer JOIN marketOffer.createProfile gp WHERE gp.id = :id")
    Page<MarketOffer> findAllMarketOfferCreatedByUser(@Param("id") Long id , Pageable pageable);

    @Query(value = "SELECT mo FROM MarketOffer mo WHERE mo.offerStatus = 'NEW' AND mo.endDate is null",
    countQuery = "SELECT count(mo) FROM MarketOffer mo WHERE mo.offerStatus = 'NEW' AND mo.endDate is null")
    Page<MarketOffer> findAllCurrentOffers(Pageable pageable);

}
