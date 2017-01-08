package com.gameworld.app.service;

import com.gameworld.app.domain.Adress;
import com.gameworld.app.domain.Game;
import com.gameworld.app.domain.GamerProfile;
import com.gameworld.app.domain.MarketOffer;
import com.gameworld.app.domain.enumeration.OfferStatus;
import com.gameworld.app.domain.enumeration.OfferType;
import com.gameworld.app.repository.GameRepository;
import com.gameworld.app.repository.GamerProfileRepository;
import com.gameworld.app.repository.MarketOfferRepository;
import com.gameworld.app.repository.TradeOfferRepository;
import com.gameworld.app.security.SecurityUtils;
import com.gameworld.app.service.dto.GamefinderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.error.Mark;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Matexo on 2017-01-07.
 */
@Service
public class GamefinderServiceImpl implements GamefinderService {

    @Autowired
    private MarketOfferRepository marketOfferRepository;

    @Autowired
    private TradeOfferRepository tradeOfferRepository;

    @Autowired
    private GamerProfileRepository gamerProfileRepository;

    @Autowired
    private GameRepository gameRepository;

    @Inject
    private EntityManager entityManager;

    public void test() {
    }

    @Transactional(readOnly = true)
    public List<MarketOffer> findPairForMarketOffer(GamefinderDTO gamefinderDTO) {
        gamefinderDTO.setMarketOfferId(6L);
        gamefinderDTO.setPerfectMatch(false);

        String username = SecurityUtils.getCurrentUserLogin();
        Page<Game> wishList = gameRepository.getGamesFromWishlist(username, null);
        List<Long> gamesId = new ArrayList<>();
        for (Game game : wishList.getContent())
            gamesId.add(game.getId());
        MarketOffer marketOffer = marketOfferRepository.findOne(gamefinderDTO.getMarketOfferId());

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MarketOffer> query = builder.createQuery(MarketOffer.class);
        Root<MarketOffer> marketOfferRoot = query.from(MarketOffer.class);
        Join<MarketOffer, GamerProfile> profileJoin = marketOfferRoot.join("createProfile");
        Join<MarketOffer, Game> gameJoin = marketOfferRoot.join("game");
        List<Predicate> predicates = new ArrayList<>();
        if (gamefinderDTO.getSameCity()) {
            GamerProfile gamerProfile = gamerProfileRepository.findGamerProfileByName(username);
            String city = gamerProfile.getAdress().getCity();
            Join<GamerProfile, Adress> adressJoin = profileJoin.join("adress");
            Predicate sameCity = builder.equal(adressJoin.get("city"), city);
            predicates.add(sameCity);
        }
        OfferType offerType = marketOffer.getOfferType();
        OfferType oppositeOfferType = OfferType.EXCHANGE;
        if (offerType.equals(OfferType.SELL))
            oppositeOfferType = OfferType.BUY;
        else if (offerType.equals(OfferType.BUY))
            oppositeOfferType = OfferType.SELL;
        Predicate findingOfferType = builder.equal(marketOfferRoot.get("offerType"), oppositeOfferType);
        predicates.add(findingOfferType);

        if (!offerType.equals(OfferType.EXCHANGE)) {
            Predicate isCostLess = builder.lessThanOrEqualTo(marketOfferRoot.get("price"), marketOffer.getPrice());
            predicates.add(isCostLess);
        } else {
            Expression<Long> exp = gameJoin.get("id");
            Predicate gameIdIn = exp.in(gamesId);
            predicates.add(gameIdIn);
        }

        Predicate profileNameNotEqual = builder.notEqual(profileJoin.get("name"), username);
        predicates.add(profileNameNotEqual);
        Predicate newOfferStatus = builder.equal(marketOfferRoot.get("offerStatus"), OfferStatus.NEW);
        predicates.add(newOfferStatus);

        query
            .select(marketOfferRoot)
            .where(builder.and(predicates.toArray(new Predicate[predicates.size()])))
            .groupBy(marketOfferRoot.get("id"))
            .orderBy();

        List<MarketOffer> resultList = entityManager.createQuery(query).getResultList();
        if (gamefinderDTO.getPerfectMatch()) {
            List<MarketOffer> newResultSet = new ArrayList<>();
            for (MarketOffer mo : resultList) {
                Set<Game> wishlist2 = mo.getCreateProfile().getSearchedGames();
                for (Game gameFromWishList : wishlist2) {
                    if (gameFromWishList.getId().equals(marketOffer.getGame().getId()))
                        newResultSet.add(mo);
                }
            }
            resultList = newResultSet;
        }
        return resultList;
    }
}
