package com.gameworld.app.repository;

import com.gameworld.app.domain.TradeOffer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT tradeOffer " +
        "FROM TradeOffer tradeOffer " +
        "JOIN tradeOffer.createProfile " +
        "WHERE tradeOffer.createProfile.name = :username " +
        "ORDER BY tradeOffer.timestamp DESC",
        countQuery = "SELECT count(tradeOffer) " +
            "FROM TradeOffer tradeOffer " +
            "JOIN tradeOffer.createProfile " +
            "WHERE tradeOffer.createProfile.name = :username " +
            "ORDER BY tradeOffer.timestamp DESC")
    Page<TradeOffer> findAllTradeOffersCreatedByUser(@Param("username") String username , Pageable pageable);

    @Query(value = "SELECT offers " +
        "FROM MarketOffer marketOffer " +
        "JOIN marketOffer.offers offers " +
        "JOIN marketOffer.createProfile " +
        "WHERE marketOffer.createProfile.name = :username " +
        "AND offers.status = 'PENDING' " +
        "ORDER BY offers.timestamp",
    countQuery = "SELECT count(offers) " +
        "FROM MarketOffer marketOffer " +
        "JOIN marketOffer.offers offers " +
        "JOIN marketOffer.createProfile " +
        "WHERE marketOffer.createProfile.name = :username " +
        "AND offers.status = 'PENDING' ")
    Page<TradeOffer> findAllTradeOffersAssignedToUser(@Param("username") String username , Pageable pageable);

}
