package com.gameworld.app.web.rest;

import com.gameworld.app.GameWorldApp;

import com.gameworld.app.domain.GameType;
import com.gameworld.app.repository.GameTypeRepository;
import com.gameworld.app.service.GameTypeService;
import com.gameworld.app.repository.search.GameTypeSearchRepository;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GameTypeResource REST controller.
 *
 * @see GameTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameWorldApp.class)
public class GameTypeResourceIntTest {

    private static final String DEFAULT_GAME_TYPE = "AAAAA";
    private static final String UPDATED_GAME_TYPE = "BBBBB";

    @Inject
    private GameTypeRepository gameTypeRepository;

    @Inject
    private GameTypeService gameTypeService;

    @Inject
    private GameTypeSearchRepository gameTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGameTypeMockMvc;

    private GameType gameType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameTypeResource gameTypeResource = new GameTypeResource();
        ReflectionTestUtils.setField(gameTypeResource, "gameTypeService", gameTypeService);
        this.restGameTypeMockMvc = MockMvcBuilders.standaloneSetup(gameTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameType createEntity(EntityManager em) {
        GameType gameType = new GameType()
                .gameType(DEFAULT_GAME_TYPE);
        return gameType;
    }

    @Before
    public void initTest() {
        gameTypeSearchRepository.deleteAll();
        gameType = createEntity(em);
    }

    @Test
    @Transactional
    public void createGameType() throws Exception {
        int databaseSizeBeforeCreate = gameTypeRepository.findAll().size();

        // Create the GameType

        restGameTypeMockMvc.perform(post("/api/game-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameType)))
                .andExpect(status().isCreated());

        // Validate the GameType in the database
        List<GameType> gameTypes = gameTypeRepository.findAll();
        assertThat(gameTypes).hasSize(databaseSizeBeforeCreate + 1);
        GameType testGameType = gameTypes.get(gameTypes.size() - 1);
        assertThat(testGameType.getGameType()).isEqualTo(DEFAULT_GAME_TYPE);

        // Validate the GameType in ElasticSearch
        GameType gameTypeEs = gameTypeSearchRepository.findOne(testGameType.getId());
        assertThat(gameTypeEs).isEqualToComparingFieldByField(testGameType);
    }

    @Test
    @Transactional
    public void checkGameTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameTypeRepository.findAll().size();
        // set the field null
        gameType.setGameType(null);

        // Create the GameType, which fails.

        restGameTypeMockMvc.perform(post("/api/game-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameType)))
                .andExpect(status().isBadRequest());

        List<GameType> gameTypes = gameTypeRepository.findAll();
        assertThat(gameTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGameTypes() throws Exception {
        // Initialize the database
        gameTypeRepository.saveAndFlush(gameType);

        // Get all the gameTypes
        restGameTypeMockMvc.perform(get("/api/game-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gameType.getId().intValue())))
                .andExpect(jsonPath("$.[*].gameType").value(hasItem(DEFAULT_GAME_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getGameType() throws Exception {
        // Initialize the database
        gameTypeRepository.saveAndFlush(gameType);

        // Get the gameType
        restGameTypeMockMvc.perform(get("/api/game-types/{id}", gameType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gameType.getId().intValue()))
            .andExpect(jsonPath("$.gameType").value(DEFAULT_GAME_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGameType() throws Exception {
        // Get the gameType
        restGameTypeMockMvc.perform(get("/api/game-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGameType() throws Exception {
        // Initialize the database
        gameTypeService.save(gameType);

        int databaseSizeBeforeUpdate = gameTypeRepository.findAll().size();

        // Update the gameType
        GameType updatedGameType = gameTypeRepository.findOne(gameType.getId());
        updatedGameType
                .gameType(UPDATED_GAME_TYPE);

        restGameTypeMockMvc.perform(put("/api/game-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGameType)))
                .andExpect(status().isOk());

        // Validate the GameType in the database
        List<GameType> gameTypes = gameTypeRepository.findAll();
        assertThat(gameTypes).hasSize(databaseSizeBeforeUpdate);
        GameType testGameType = gameTypes.get(gameTypes.size() - 1);
        assertThat(testGameType.getGameType()).isEqualTo(UPDATED_GAME_TYPE);

        // Validate the GameType in ElasticSearch
        GameType gameTypeEs = gameTypeSearchRepository.findOne(testGameType.getId());
        assertThat(gameTypeEs).isEqualToComparingFieldByField(testGameType);
    }

    @Test
    @Transactional
    public void deleteGameType() throws Exception {
        // Initialize the database
        gameTypeService.save(gameType);

        int databaseSizeBeforeDelete = gameTypeRepository.findAll().size();

        // Get the gameType
        restGameTypeMockMvc.perform(delete("/api/game-types/{id}", gameType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean gameTypeExistsInEs = gameTypeSearchRepository.exists(gameType.getId());
        assertThat(gameTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<GameType> gameTypes = gameTypeRepository.findAll();
        assertThat(gameTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGameType() throws Exception {
        // Initialize the database
        gameTypeService.save(gameType);

        // Search the gameType
        restGameTypeMockMvc.perform(get("/api/_search/game-types?query=id:" + gameType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameType.getId().intValue())))
            .andExpect(jsonPath("$.[*].gameType").value(hasItem(DEFAULT_GAME_TYPE.toString())));
    }
}
