package com.gameworld.app.repository;

import com.gameworld.app.domain.Game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Game entity.
 */
@SuppressWarnings("unused")
public interface GameRepository extends JpaRepository<Game,Long> {

    @Query(value = "SELECT games " +
        "FROM GamerProfile gp " +
        "JOIN gp.searchedGames games " +
        "WHERE gp.name = :username"
        ,countQuery = "SELECT count(games) " +
        "FROM GamerProfile gp " +
        "JOIN gp.searchedGames games " +
        "WHERE gp.name = :username")
    Page<Game> getGamesFromWishlist(@Param("username") String username , Pageable pageable);
}

