package com.gameworld.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gameworld.app.domain.Game;
import com.gameworld.app.domain.GamerProfile;
import com.gameworld.app.domain.MarketOffer;
import com.gameworld.app.service.GameService;
import com.gameworld.app.service.GamefinderService;
import com.gameworld.app.web.rest.util.HeaderUtil;
import com.gameworld.app.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Game.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    @Inject
    private GameService gameService;

    @Inject
    private GamefinderService gamefinderService;


    @PostMapping("/games")
    @Timed
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game game) throws URISyntaxException {
        log.debug("REST request to save Game : {}", game);
        if (game.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("game", "idexists", "A new game cannot already have an ID")).body(null);
        }
        Game result = gameService.save(game);
        return ResponseEntity.created(new URI("/api/games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("game", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/games")
    @Timed
    public ResponseEntity<Game> updateGame(@Valid @RequestBody Game game) throws URISyntaxException {
        log.debug("REST request to update Game : {}", game);
        if (game.getId() == null) {
            return createGame(game);
        }
        Game result = gameService.save(game);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("game", game.getId().toString()))
            .body(result);
    }

    @GetMapping("/games")
    @Timed
    public ResponseEntity<List<Game>> getAllGames(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Games");
        Page<Game> page = gameService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/games");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/games/{id}")
    @Timed
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        log.debug("REST request to get Game : {}", id);
        Game game = gameService.findOne(id);
        return Optional.ofNullable(game)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/games/{id}")
    @Timed
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        log.debug("REST request to delete Game : {}", id);
        gameService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("game", id.toString())).build();
    }

    @GetMapping("/_search/games")
    @Timed
    public ResponseEntity<List<Game>> searchGames(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Games for query {}", query);
        Page<Game> page = gameService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/games");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/games/{id}")
    @Timed
    public ResponseEntity<Void> addGameToWishList(@PathVariable Long id) {
        gameService.addGameToWishList(id);
        return new ResponseEntity<>(HeaderUtil.createEntityUpdateAlert("wishList", id.toString()), HttpStatus.OK);
    }

    @GetMapping("/games/wishlist")
    @Timed
    public ResponseEntity<List<Game>> getGamesFromWishlist(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Games");
        Page<Game> page = gameService.getGamesFromWishlist(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/games/wishlist");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/games/wishlist/{id}")
    @Timed
    public ResponseEntity<Void> removeGameFromWishlist(@PathVariable Long id) {
        gameService.removeGameFromWishlist(id);
        return new ResponseEntity<>(HeaderUtil.createEntityDeletionAlert("wishList", id.toString()), HttpStatus.OK);
    }

}
