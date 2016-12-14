package com.gameworld.app.web.rest;

import com.gameworld.app.GameWorldApp;

import com.gameworld.app.domain.Game;
import com.gameworld.app.repository.GameRepository;
import com.gameworld.app.service.GameService;
import com.gameworld.app.repository.search.GameSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GameResource REST controller.
 *
 * @see GameResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameWorldApp.class)
public class GameResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final String DEFAULT_PRODUCER = "AAAAA";
    private static final String UPDATED_PRODUCER = "BBBBB";

    private static final Integer DEFAULT_YEAR_OF_PRODUCTION = 1;
    private static final Integer UPDATED_YEAR_OF_PRODUCTION = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_COVER_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COVER_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_COVER_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COVER_IMAGE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIMESTAMP_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_TIMESTAMP);

    private static final Boolean DEFAULT_BLOCKADE = false;
    private static final Boolean UPDATED_BLOCKADE = true;

    @Inject
    private GameRepository gameRepository;

    @Inject
    private GameService gameService;

    @Inject
    private GameSearchRepository gameSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGameMockMvc;

    private Game game;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameResource gameResource = new GameResource();
        ReflectionTestUtils.setField(gameResource, "gameService", gameService);
        this.restGameMockMvc = MockMvcBuilders.standaloneSetup(gameResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createEntity(EntityManager em) {
        Game game = new Game()
                .title(DEFAULT_TITLE)
                .producer(DEFAULT_PRODUCER)
                .yearOfProduction(DEFAULT_YEAR_OF_PRODUCTION)
                .description(DEFAULT_DESCRIPTION)
                .coverImage(DEFAULT_COVER_IMAGE)
                .coverImageContentType(DEFAULT_COVER_IMAGE_CONTENT_TYPE)
                .timestamp(DEFAULT_TIMESTAMP)
                .blockade(DEFAULT_BLOCKADE);
        return game;
    }

    @Before
    public void initTest() {
        gameSearchRepository.deleteAll();
        game = createEntity(em);
    }

    @Test
    @Transactional
    public void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game

        restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(game)))
                .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = games.get(games.size() - 1);
        assertThat(testGame.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testGame.getProducer()).isEqualTo(DEFAULT_PRODUCER);
        assertThat(testGame.getYearOfProduction()).isEqualTo(DEFAULT_YEAR_OF_PRODUCTION);
        assertThat(testGame.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGame.getCoverImage()).isEqualTo(DEFAULT_COVER_IMAGE);
        assertThat(testGame.getCoverImageContentType()).isEqualTo(DEFAULT_COVER_IMAGE_CONTENT_TYPE);
        assertThat(testGame.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testGame.isBlockade()).isEqualTo(DEFAULT_BLOCKADE);

        // Validate the Game in ElasticSearch
        Game gameEs = gameSearchRepository.findOne(testGame.getId());
        assertThat(gameEs).isEqualToComparingFieldByField(testGame);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setTitle(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(game)))
                .andExpect(status().isBadRequest());

        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProducerIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setProducer(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(game)))
                .andExpect(status().isBadRequest());

        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearOfProductionIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setYearOfProduction(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(game)))
                .andExpect(status().isBadRequest());

        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setTimestamp(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(game)))
                .andExpect(status().isBadRequest());

        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBlockadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setBlockade(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(game)))
                .andExpect(status().isBadRequest());

        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGames() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the games
        restGameMockMvc.perform(get("/api/games?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].producer").value(hasItem(DEFAULT_PRODUCER.toString())))
                .andExpect(jsonPath("$.[*].yearOfProduction").value(hasItem(DEFAULT_YEAR_OF_PRODUCTION)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].coverImageContentType").value(hasItem(DEFAULT_COVER_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].coverImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_COVER_IMAGE))))
                .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP_STR)))
                .andExpect(jsonPath("$.[*].blockade").value(hasItem(DEFAULT_BLOCKADE.booleanValue())));
    }

    @Test
    @Transactional
    public void getGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(game.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.producer").value(DEFAULT_PRODUCER.toString()))
            .andExpect(jsonPath("$.yearOfProduction").value(DEFAULT_YEAR_OF_PRODUCTION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.coverImageContentType").value(DEFAULT_COVER_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.coverImage").value(Base64Utils.encodeToString(DEFAULT_COVER_IMAGE)))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP_STR))
            .andExpect(jsonPath("$.blockade").value(DEFAULT_BLOCKADE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGame() throws Exception {
        // Initialize the database
        gameService.save(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game
        Game updatedGame = gameRepository.findOne(game.getId());
        updatedGame
                .title(UPDATED_TITLE)
                .producer(UPDATED_PRODUCER)
                .yearOfProduction(UPDATED_YEAR_OF_PRODUCTION)
                .description(UPDATED_DESCRIPTION)
                .coverImage(UPDATED_COVER_IMAGE)
                .coverImageContentType(UPDATED_COVER_IMAGE_CONTENT_TYPE)
                .timestamp(UPDATED_TIMESTAMP)
                .blockade(UPDATED_BLOCKADE);

        restGameMockMvc.perform(put("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGame)))
                .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeUpdate);
        Game testGame = games.get(games.size() - 1);
        assertThat(testGame.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testGame.getProducer()).isEqualTo(UPDATED_PRODUCER);
        assertThat(testGame.getYearOfProduction()).isEqualTo(UPDATED_YEAR_OF_PRODUCTION);
        assertThat(testGame.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGame.getCoverImage()).isEqualTo(UPDATED_COVER_IMAGE);
        assertThat(testGame.getCoverImageContentType()).isEqualTo(UPDATED_COVER_IMAGE_CONTENT_TYPE);
        assertThat(testGame.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testGame.isBlockade()).isEqualTo(UPDATED_BLOCKADE);

        // Validate the Game in ElasticSearch
        Game gameEs = gameSearchRepository.findOne(testGame.getId());
        assertThat(gameEs).isEqualToComparingFieldByField(testGame);
    }

    @Test
    @Transactional
    public void deleteGame() throws Exception {
        // Initialize the database
        gameService.save(game);

        int databaseSizeBeforeDelete = gameRepository.findAll().size();

        // Get the game
        restGameMockMvc.perform(delete("/api/games/{id}", game.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean gameExistsInEs = gameSearchRepository.exists(game.getId());
        assertThat(gameExistsInEs).isFalse();

        // Validate the database is empty
        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGame() throws Exception {
        // Initialize the database
        gameService.save(game);

        // Search the game
        restGameMockMvc.perform(get("/api/_search/games?query=id:" + game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].producer").value(hasItem(DEFAULT_PRODUCER.toString())))
            .andExpect(jsonPath("$.[*].yearOfProduction").value(hasItem(DEFAULT_YEAR_OF_PRODUCTION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].coverImageContentType").value(hasItem(DEFAULT_COVER_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_COVER_IMAGE))))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP_STR)))
            .andExpect(jsonPath("$.[*].blockade").value(hasItem(DEFAULT_BLOCKADE.booleanValue())));
    }
}
