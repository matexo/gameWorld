package com.gameworld.app.repository;

import com.gameworld.app.domain.MarketOffer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MarketOffer entity.
 */
@SuppressWarnings("unused")
public interface MarketOfferRepository extends JpaRepository<MarketOffer,Long> {

}
