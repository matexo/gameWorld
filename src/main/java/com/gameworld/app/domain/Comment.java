package com.gameworld.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.gameworld.app.domain.enumeration.Rating;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "comment", nullable = false)
    private String comment;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false)
    private Rating rating;

    @Column(name = "timestamp")
    private ZonedDateTime timestamp;

    @ManyToOne
    private GamerProfile authorProfile;

    @ManyToOne
    private MarketOffer marketOffer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public Comment comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Rating getRating() {
        return rating;
    }

    public Comment rating(Rating rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public Comment timestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public GamerProfile getAuthorProfile() {
        return authorProfile;
    }

    public Comment authorProfile(GamerProfile gamerProfile) {
        this.authorProfile = gamerProfile;
        return this;
    }

    public void setAuthorProfile(GamerProfile gamerProfile) {
        this.authorProfile = gamerProfile;
    }

    public MarketOffer getMarketOffer() {
        return marketOffer;
    }

    public Comment marketOffer(MarketOffer marketOffer) {
        this.marketOffer = marketOffer;
        return this;
    }

    public void setMarketOffer(MarketOffer marketOffer) {
        this.marketOffer = marketOffer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        if(comment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
            "id=" + id +
            ", comment='" + comment + "'" +
            ", rating='" + rating + "'" +
            ", timestamp='" + timestamp + "'" +
            '}';
    }
}
