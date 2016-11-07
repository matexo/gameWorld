package com.gameworld.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GameType.
 */
@Entity
@Table(name = "game_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gametype")
public class GameType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "game_type", nullable = false)
    private String gameType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameType() {
        return gameType;
    }

    public GameType gameType(String gameType) {
        this.gameType = gameType;
        return this;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameType gameType = (GameType) o;
        if(gameType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, gameType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GameType{" +
            "id=" + id +
            ", gameType='" + gameType + "'" +
            '}';
    }
}
