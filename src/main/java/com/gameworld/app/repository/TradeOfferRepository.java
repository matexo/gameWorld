package com.gameworld.app.repository;

import com.gameworld.app.domain.TradeOffer;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TradeOffer entity.
 */
@SuppressWarnings("unused")
public interface TradeOfferRepository extends JpaRepository<TradeOffer,Long> {

    @Query("select distinct tradeOffer from TradeOffer tradeOffer left join fetch tradeOffer.offerGames")
    List<TradeOffer> findAllWithEagerRelationships();

    @Query("select tradeOffer from TradeOffer tradeOffer left join fetch tradeOffer.offerGames where tradeOffer.id =:id")
    TradeOffer findOneWithEagerRelationships(@Param("id") Long id);

}
