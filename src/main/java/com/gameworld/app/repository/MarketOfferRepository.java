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

    @Query(value = "SELECT marketOffer FROM MarketOffer marketOffer JOIN FETCH marketOffer.createProfile gp WHERE gp.id = :gamerProfileId ORDER BY marketOffer.createDate DESC",
        countQuery = "SELECT count(marketOffer) FROM MarketOffer marketOffer JOIN marketOffer.createProfile gp WHERE gp.id = :gamerProfileId  ORDER BY marketOffer.createDate DESC")
    Page<MarketOffer> findAllMarketOfferCreatedByUser(@Param("gamerProfileId") Long gamerProfileId , Pageable pageable);

@Query(value = "SELECT mo " +
    "FROM MarketOffer mo " +
    "JOIN mo.createProfile profile " +
    "WHERE mo.offerStatus = 'NEW' " +
    "AND mo.endDate IS NULL " +
    "AND profile.name <> :username " +
    "ORDER BY mo.createDate DESC",
countQuery =
    "SELECT count(mo) " +
    "FROM MarketOffer mo " +
    "JOIN mo.createProfile profile " +
    "WHERE mo.offerStatus = 'NEW' " +
    "AND mo.endDate IS NULL " +
    "AND profile.name <> :username " +
    "ORDER BY mo.createDate DESC")
Page<MarketOffer> findAllCurrentOffers(@Param("username") String username ,  Pageable pageable);

    @Query(value = "SELECT mo FROM MarketOffer mo " +
        "JOIN FETCH mo.endOfferProfile eop " +
        "JOIN FETCH mo.offers offers " +
        "WHERE eop.name = :username " +
        "AND mo.offerStatus = 'ENDED' " +
        "AND mo.endDate IS NOT NULL " +
//        "AND offers.status = 'ACCEPTED' " +
        "ORDER BY mo.endDate ",
    countQuery = "SELECT count(mo) " +
        "FROM MarketOffer mo " +
        "JOIN mo.endOfferProfile eop " +
        "JOIN mo.offers offers " +
        "WHERE eop.name = :username " +
        "AND mo.offerStatus = 'ENDED' " +
        "AND mo.endDate IS NOT NULL " +
//        "AND offers.status = 'ACCEPTED' " +
        "ORDER BY mo.endDate ")
    Page<MarketOffer> findMarketOffersEndByUser(@Param("username") String username , Pageable pageable);

    @Query(value = "SELECT mo FROM MarketOffer mo " +
        "JOIN FETCH mo.createProfile cp " +
        "JOIN FETCH mo.offers offers " +
        "WHERE cp.name = :username " +
        "AND mo.offerStatus = 'ENDED' " +
        "AND mo.endDate IS NOT NULL " +
//        "AND offers.status = 'ACCEPTED'" +
        "ORDER BY mo.endDate ",
        countQuery = "SELECT count(mo) " +
         "FROM MarketOffer mo " +
         "JOIN mo.createProfile cp " +
         "JOIN mo.offers offers " +
         "WHERE cp.name = :username " +
         "AND mo.offerStatus = 'ENDED' " +
         "AND mo.endDate IS NOT NULL " +
//         "AND offers.status = 'ACCEPTED' " +
         "ORDER BY mo.endDate ")
    Page<MarketOffer> findEndedMarketOffers(@Param("username") String username , Pageable pageable);


    @Query(value = "SELECT marketOffer " +
        "FROM MarketOffer marketOffer " +
        "JOIN marketOffer.game game " +
        "JOIN marketOffer.createProfile gp " +
        "WHERE game.id IN :ids " +
        "AND marketOffer.offerStatus = 'NEW' " +
        "AND gp.name <> :username " +
        "ORDER BY marketOffer.createDate DESC" ,
        countQuery = "SELECT count(marketOffer) " +
            "FROM MarketOffer marketOffer " +
            "JOIN marketOffer.game game " +
            "JOIN marketOffer.createProfile gp " +
            "WHERE game.id IN :ids " +
            "AND marketOffer.offerStatus = 'NEW' " +
            "AND gp.name <> :username " +
            "ORDER BY marketOffer.createDate DESC")
    Page<MarketOffer> findMarketOfferByGameId(@Param("ids") List<Long> ids,@Param("username") String username, Pageable pageable);

}
