package com.gameworld.app.web.rest;

import com.gameworld.app.domain.MarketOffer;
import com.gameworld.app.service.GamefinderService;
import com.gameworld.app.service.dto.GamefinderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Matexo on 2017-01-08.
 */
@RestController
@RequestMapping("/api")
public class GamefinderResource {

    @Inject
    private GamefinderService gamefinderService;

    @GetMapping("/gamefinder")
    public ResponseEntity<List<MarketOffer>> findGames(@RequestParam("marketOfferId") Long id ,
                                                       @RequestParam("sameCity") Boolean sameCity,
                                                       @RequestParam("perfectMatch") Boolean perfectMatch) {
        GamefinderDTO data = new GamefinderDTO();
        data.setMarketOfferId(id);
        data.setSameCity(sameCity);
        data.setPerfectMatch(perfectMatch);
        List<MarketOffer> matchedOffers = gamefinderService.findPairForMarketOffer(data);
        return new ResponseEntity<List<MarketOffer>>(matchedOffers,HttpStatus.OK);
    }

}
