package com.gameworld.app.service;

import com.gameworld.app.domain.MarketOffer;
import com.gameworld.app.service.dto.GamefinderDTO;

import java.util.List;

/**
 * Created by Matexo on 2017-01-07.
 */
public interface GamefinderService {

    List<MarketOffer> findPairForMarketOffer(GamefinderDTO gamefinderDTO);
}
