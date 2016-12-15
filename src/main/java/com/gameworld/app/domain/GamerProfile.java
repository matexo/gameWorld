package com.gameworld.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A GamerProfile.
 */
@Entity
@Table(name = "gamer_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gamerprofile")
public class GamerProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone")
    private Integer phone;

    @OneToOne
    @JoinColumn(unique = true)
    private Adress adress;

    @OneToMany(mappedBy = "createProfile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MarketOffer> marketOffers = new HashSet<>();

    @OneToMany(mappedBy = "createProfile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TradeOffer> tradeOffers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "gamer_profile_searched_games",
               joinColumns = @JoinColumn(name="gamer_profiles_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="searched_games_id", referencedColumnName="ID"))
    private Set<Game> searchedGames = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "gamer_profile_conversations",
               joinColumns = @JoinColumn(name="gamer_profiles_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="conversations_id", referencedColumnName="ID"))
    @JsonIgnore
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "authorProfile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @OneToOne(mappedBy = "gamerProfile")
    @JoinColumn(unique = true)
    @JsonIgnore
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public GamerProfile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public GamerProfile surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getPhone() {
        return phone;
    }

    public GamerProfile phone(Integer phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Adress getAdress() {
        return adress;
    }

    public GamerProfile adress(Adress adress) {
        this.adress = adress;
        return this;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

    public Set<MarketOffer> getMarketOffers() {
        return marketOffers;
    }

    public GamerProfile marketOffers(Set<MarketOffer> marketOffers) {
        this.marketOffers = marketOffers;
        return this;
    }

    public GamerProfile addMarketOffers(MarketOffer marketOffer) {
        marketOffers.add(marketOffer);
        marketOffer.setCreateProfile(this);
        return this;
    }

    public GamerProfile removeMarketOffers(MarketOffer marketOffer) {
        marketOffers.remove(marketOffer);
        marketOffer.setCreateProfile(null);
        return this;
    }

    public void setMarketOffers(Set<MarketOffer> marketOffers) {
        this.marketOffers = marketOffers;
    }

    public Set<TradeOffer> getTradeOffers() {
        return tradeOffers;
    }

    public GamerProfile tradeOffers(Set<TradeOffer> tradeOffers) {
        this.tradeOffers = tradeOffers;
        return this;
    }

    public GamerProfile addTradeOffers(TradeOffer tradeOffer) {
        tradeOffers.add(tradeOffer);
        tradeOffer.setCreateProfile(this);
        return this;
    }

    public GamerProfile removeTradeOffers(TradeOffer tradeOffer) {
        tradeOffers.remove(tradeOffer);
        tradeOffer.setCreateProfile(null);
        return this;
    }

    public void setTradeOffers(Set<TradeOffer> tradeOffers) {
        this.tradeOffers = tradeOffers;
    }

    public Set<Game> getSearchedGames() {
        return searchedGames;
    }

    public GamerProfile searchedGames(Set<Game> games) {
        this.searchedGames = games;
        return this;
    }

    public GamerProfile addSearchedGames(Game game) {
        searchedGames.add(game);
        return this;
    }

    public GamerProfile removeSearchedGames(Game game) {
        searchedGames.remove(game);
        return this;
    }

    public void setSearchedGames(Set<Game> games) {
        this.searchedGames = games;
    }

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public GamerProfile conversations(Set<Conversation> conversations) {
        this.conversations = conversations;
        return this;
    }

    public GamerProfile addConversations(Conversation conversation) {
        conversations.add(conversation);
        conversation.getProfiles().add(this);
        return this;
    }

    public GamerProfile removeConversations(Conversation conversation) {
        conversations.remove(conversation);
        conversation.getProfiles().remove(this);
        return this;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public GamerProfile comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public GamerProfile addComments(Comment comment) {
        comments.add(comment);
        comment.setAuthorProfile(this);
        return this;
    }

    public GamerProfile removeComments(Comment comment) {
        comments.remove(comment);
        comment.setAuthorProfile(null);
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GamerProfile gamerProfile = (GamerProfile) o;
        if(gamerProfile.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, gamerProfile.id);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GamerProfile{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
