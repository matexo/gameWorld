package com.gameworld.app.web.rest;

import com.gameworld.app.GameWorldApp;

import com.gameworld.app.domain.Platform;
import com.gameworld.app.repository.PlatformRepository;
import com.gameworld.app.service.PlatformService;
import com.gameworld.app.repository.search.PlatformSearchRepository;

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
 * Test class for the PlatformResource REST controller.
 *
 * @see PlatformResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameWorldApp.class)
public class PlatformResourceIntTest {

    private static final String DEFAULT_PLATFORM = "AAAAA";
    private static final String UPDATED_PLATFORM = "BBBBB";

    @Inject
    private PlatformRepository platformRepository;

    @Inject
    private PlatformService platformService;

    @Inject
    private PlatformSearchRepository platformSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPlatformMockMvc;

    private Platform platform;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlatformResource platformResource = new PlatformResource();
        ReflectionTestUtils.setField(platformResource, "platformService", platformService);
        this.restPlatformMockMvc = MockMvcBuilders.standaloneSetup(platformResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Platform createEntity(EntityManager em) {
        Platform platform = new Platform()
                .platform(DEFAULT_PLATFORM);
        return platform;
    }

    @Before
    public void initTest() {
        platformSearchRepository.deleteAll();
        platform = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlatform() throws Exception {
        int databaseSizeBeforeCreate = platformRepository.findAll().size();

        // Create the Platform

        restPlatformMockMvc.perform(post("/api/platforms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(platform)))
                .andExpect(status().isCreated());

        // Validate the Platform in the database
        List<Platform> platforms = platformRepository.findAll();
        assertThat(platforms).hasSize(databaseSizeBeforeCreate + 1);
        Platform testPlatform = platforms.get(platforms.size() - 1);
        assertThat(testPlatform.getPlatform()).isEqualTo(DEFAULT_PLATFORM);

        // Validate the Platform in ElasticSearch
        Platform platformEs = platformSearchRepository.findOne(testPlatform.getId());
        assertThat(platformEs).isEqualToComparingFieldByField(testPlatform);
    }

    @Test
    @Transactional
    public void checkPlatformIsRequired() throws Exception {
        int databaseSizeBeforeTest = platformRepository.findAll().size();
        // set the field null
        platform.setPlatform(null);

        // Create the Platform, which fails.

        restPlatformMockMvc.perform(post("/api/platforms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(platform)))
                .andExpect(status().isBadRequest());

        List<Platform> platforms = platformRepository.findAll();
        assertThat(platforms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlatforms() throws Exception {
        // Initialize the database
        platformRepository.saveAndFlush(platform);

        // Get all the platforms
        restPlatformMockMvc.perform(get("/api/platforms?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(platform.getId().intValue())))
                .andExpect(jsonPath("$.[*].platform").value(hasItem(DEFAULT_PLATFORM.toString())));
    }

    @Test
    @Transactional
    public void getPlatform() throws Exception {
        // Initialize the database
        platformRepository.saveAndFlush(platform);

        // Get the platform
        restPlatformMockMvc.perform(get("/api/platforms/{id}", platform.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(platform.getId().intValue()))
            .andExpect(jsonPath("$.platform").value(DEFAULT_PLATFORM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlatform() throws Exception {
        // Get the platform
        restPlatformMockMvc.perform(get("/api/platforms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlatform() throws Exception {
        // Initialize the database
        platformService.save(platform);

        int databaseSizeBeforeUpdate = platformRepository.findAll().size();

        // Update the platform
        Platform updatedPlatform = platformRepository.findOne(platform.getId());
        updatedPlatform
                .platform(UPDATED_PLATFORM);

        restPlatformMockMvc.perform(put("/api/platforms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPlatform)))
                .andExpect(status().isOk());

        // Validate the Platform in the database
        List<Platform> platforms = platformRepository.findAll();
        assertThat(platforms).hasSize(databaseSizeBeforeUpdate);
        Platform testPlatform = platforms.get(platforms.size() - 1);
        assertThat(testPlatform.getPlatform()).isEqualTo(UPDATED_PLATFORM);

        // Validate the Platform in ElasticSearch
        Platform platformEs = platformSearchRepository.findOne(testPlatform.getId());
        assertThat(platformEs).isEqualToComparingFieldByField(testPlatform);
    }

    @Test
    @Transactional
    public void deletePlatform() throws Exception {
        // Initialize the database
        platformService.save(platform);

        int databaseSizeBeforeDelete = platformRepository.findAll().size();

        // Get the platform
        restPlatformMockMvc.perform(delete("/api/platforms/{id}", platform.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean platformExistsInEs = platformSearchRepository.exists(platform.getId());
        assertThat(platformExistsInEs).isFalse();

        // Validate the database is empty
        List<Platform> platforms = platformRepository.findAll();
        assertThat(platforms).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPlatform() throws Exception {
        // Initialize the database
        platformService.save(platform);

        // Search the platform
        restPlatformMockMvc.perform(get("/api/_search/platforms?query=id:" + platform.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(platform.getId().intValue())))
            .andExpect(jsonPath("$.[*].platform").value(hasItem(DEFAULT_PLATFORM.toString())));
    }
}
