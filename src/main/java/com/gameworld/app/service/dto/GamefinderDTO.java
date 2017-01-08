package com.gameworld.app.service.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by Matexo on 2017-01-08.
 */
public class GamefinderDTO {

    private Boolean sameCity;
    private Boolean perfectMatch;
    private Long marketOfferId;

    public Long getMarketOfferId() {
        return marketOfferId;
    }

    public void setMarketOfferId(Long marketOfferId) {
        this.marketOfferId = marketOfferId;
    }

    public Boolean getSameCity() {
        return sameCity;
    }

    public void setSameCity(Boolean sameCity) {
        this.sameCity = sameCity;
    }

    public Boolean getPerfectMatch() {
        return perfectMatch;
    }

    public void setPerfectMatch(Boolean perfectMatch) {
        this.perfectMatch = perfectMatch;
    }
}
