package com.gameworld.app.service;

import com.gameworld.app.domain.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface GameService {


    Game save(Game game);
    Page<Game> findAll(Pageable pageable);
    Game findOne(Long id);
    void delete(Long id);

    Page<Game> search(String query, Pageable pageable);

    void addGameToWishList(Long gameId);
    Page<Game> getGamesFromWishlist(Pageable pageable);
    void removeGameFromWishlist(Long gameId);
}
