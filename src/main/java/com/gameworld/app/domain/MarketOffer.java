package com.gameworld.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import com.gameworld.app.domain.enumeration.OfferType;

import com.gameworld.app.domain.enumeration.OfferStatus;

/**
 * A MarketOffer.
 */
@Entity
@Table(name = "market_offer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "marketoffer")
public class MarketOffer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private ZonedDateTime createDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "offer_type", nullable = false)
    private OfferType offerType;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1000")
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "offer_status", nullable = false)
    private OfferStatus offerStatus;

    @OneToOne
    @JoinColumn
    private Game game;

    @OneToOne
    @JoinColumn
    private GamerProfile endOfferProfile;

    @OneToMany(mappedBy = "marketOffer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TradeOffer> offers = new HashSet<>();

    @OneToMany(mappedBy = "marketOffer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    private GamerProfile createProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public MarketOffer createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public MarketOffer endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public MarketOffer offerType(OfferType offerType) {
        this.offerType = offerType;
        return this;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

    public Double getPrice() {
        return price;
    }

    public MarketOffer price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public OfferStatus getOfferStatus() {
        return offerStatus;
    }

    public MarketOffer offerStatus(OfferStatus offerStatus) {
        this.offerStatus = offerStatus;
        return this;
    }

    public void setOfferStatus(OfferStatus offerStatus) {
        this.offerStatus = offerStatus;
    }

    public Game getGame() {
        return game;
    }

    public MarketOffer game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GamerProfile getEndOfferProfile() {
        return endOfferProfile;
    }

    public MarketOffer endOfferProfile(GamerProfile gamerProfile) {
        this.endOfferProfile = gamerProfile;
        return this;
    }

    public void setEndOfferProfile(GamerProfile gamerProfile) {
        this.endOfferProfile = gamerProfile;
    }

    public Set<TradeOffer> getOffers() {
        return offers;
    }

    public MarketOffer offers(Set<TradeOffer> tradeOffers) {
        this.offers = tradeOffers;
        return this;
    }

    public MarketOffer addOffers(TradeOffer tradeOffer) {
        offers.add(tradeOffer);
        tradeOffer.setMarketOffer(this);
        return this;
    }

    public MarketOffer removeOffers(TradeOffer tradeOffer) {
        offers.remove(tradeOffer);
        tradeOffer.setMarketOffer(null);
        return this;
    }

    public void setOffers(Set<TradeOffer> tradeOffers) {
        this.offers = tradeOffers;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public MarketOffer comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public MarketOffer addComments(Comment comment) {
        comments.add(comment);
        comment.setMarketOffer(this);
        return this;
    }

    public MarketOffer removeComments(Comment comment) {
        comments.remove(comment);
        comment.setMarketOffer(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public GamerProfile getCreateProfile() {
        return createProfile;
    }

    public MarketOffer createProfile(GamerProfile gamerProfile) {
        this.createProfile = gamerProfile;
        return this;
    }

    public void setCreateProfile(GamerProfile gamerProfile) {
        this.createProfile = gamerProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarketOffer marketOffer = (MarketOffer) o;
        if(marketOffer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, marketOffer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MarketOffer{" +
            "id=" + id +
            ", createDate='" + createDate + "'" +
            ", endDate='" + endDate + "'" +
            ", offerType='" + offerType + "'" +
            ", price='" + price + "'" +
            ", offerStatus='" + offerStatus + "'" +
            '}';
    }
}
