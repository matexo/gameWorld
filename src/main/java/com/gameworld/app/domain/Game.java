package com.gameworld.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "producer", nullable = false)
    private String producer;

    @NotNull
    @Column(name = "year_of_production", nullable = false)
    private Integer yearOfProduction;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "cover_image")
    private byte[] coverImage;

    @Column(name = "cover_image_content_type")
    private String coverImageContentType;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp;

    @NotNull
    @Column(name = "blockade", nullable = false)
    private Boolean blockade;

    @OneToOne
    @JoinColumn(unique = true)
    private GameType gameType;

    @OneToOne
    @JoinColumn(unique = true)
    private Platform platform;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Game title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public Game producer(String producer) {
        this.producer = producer;
        return this;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Integer getYearOfProduction() {
        return yearOfProduction;
    }

    public Game yearOfProduction(Integer yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
        return this;
    }

    public void setYearOfProduction(Integer yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public String getDescription() {
        return description;
    }

    public Game description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public Game coverImage(byte[] coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverImageContentType() {
        return coverImageContentType;
    }

    public Game coverImageContentType(String coverImageContentType) {
        this.coverImageContentType = coverImageContentType;
        return this;
    }

    public void setCoverImageContentType(String coverImageContentType) {
        this.coverImageContentType = coverImageContentType;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public Game timestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean isBlockade() {
        return blockade;
    }

    public Game blockade(Boolean blockade) {
        this.blockade = blockade;
        return this;
    }

    public void setBlockade(Boolean blockade) {
        this.blockade = blockade;
    }

    public GameType getGameType() {
        return gameType;
    }

    public Game gameType(GameType gameType) {
        this.gameType = gameType;
        return this;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Game platform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        if(game.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", producer='" + producer + "'" +
            ", yearOfProduction='" + yearOfProduction + "'" +
            ", description='" + description + "'" +
            ", coverImage='" + coverImage + "'" +
            ", coverImageContentType='" + coverImageContentType + "'" +
            ", timestamp='" + timestamp + "'" +
            ", blockade='" + blockade + "'" +
            '}';
    }
}
