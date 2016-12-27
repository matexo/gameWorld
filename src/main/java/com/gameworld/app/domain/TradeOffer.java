package com.gameworld.app.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameworld.app.util.DateUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.gameworld.app.domain.enumeration.TradeOfferStatus;

/**
 * A TradeOffer.
 */
@Entity
@Table(name = "trade_offer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tradeoffer")
public class TradeOffer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "payment")
    private Double payment;

    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TradeOfferStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "trade_offer_offer_games",
               joinColumns = @JoinColumn(name="trade_offers_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="offer_games_id", referencedColumnName="ID"))
    private Set<Game> offerGames = new HashSet<>();

    @ManyToOne
    private GamerProfile createProfile;

    @ManyToOne
    @JsonBackReference
    private MarketOffer marketOffer;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Long marketOfferId;

    public Long getMarketOfferId() {
        return marketOfferId;
    }

    public void setMarketOfferId(Long marketOfferId) {
        this.marketOfferId = marketOfferId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPayment() {
        return payment;
    }

    public TradeOffer payment(Double payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public TradeOffer timestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public TradeOfferStatus getStatus() {
        return status;
    }

    public TradeOffer status(TradeOfferStatus status) {
        this.status = status;
        return this;
    }

    public Set<Game> getOfferGames() {
        return offerGames;
    }

    public TradeOffer offerGames(Set<Game> games) {
        this.offerGames = games;
        return this;
    }

    public TradeOffer addOfferGames(Game game) {
        offerGames.add(game);
        return this;
    }

    public TradeOffer removeOfferGames(Game game) {
        offerGames.remove(game);
        return this;
    }

    public void setOfferGames(Set<Game> games) {
        this.offerGames = games;
    }

    public GamerProfile getCreateProfile() {
        return createProfile;
    }

    public TradeOffer createProfile(GamerProfile gamerProfile) {
        this.createProfile = gamerProfile;
        return this;
    }

    public void setCreateProfile(GamerProfile gamerProfile) {
        this.createProfile = gamerProfile;
    }

    public MarketOffer getMarketOffer() {
        return marketOffer;
    }

    public TradeOffer marketOffer(MarketOffer marketOffer) {
        this.marketOffer = marketOffer;
        return this;
    }

    public void setMarketOffer(MarketOffer marketOffer) {
        this.marketOffer = marketOffer;
    }

    public void changeStatus(TradeOfferStatus status) {
        this.status = status;
        timestamp = DateUtil.getNowDateTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TradeOffer tradeOffer = (TradeOffer) o;
        if(tradeOffer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tradeOffer.id);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TradeOffer{" +
            "id=" + id +
            ", payment='" + payment + "'" +
            ", timestamp='" + timestamp + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
