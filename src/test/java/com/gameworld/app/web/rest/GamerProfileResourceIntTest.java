package com.gameworld.app.web.rest;

import com.gameworld.app.GameWorldApp;

import com.gameworld.app.domain.GamerProfile;
import com.gameworld.app.repository.GamerProfileRepository;
import com.gameworld.app.service.GamerProfileService;
import com.gameworld.app.repository.search.GamerProfileSearchRepository;

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
 * Test class for the GamerProfileResource REST controller.
 *
 * @see GamerProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameWorldApp.class)
public class GamerProfileResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private GamerProfileRepository gamerProfileRepository;

    @Inject
    private GamerProfileService gamerProfileService;

    @Inject
    private GamerProfileSearchRepository gamerProfileSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGamerProfileMockMvc;

    private GamerProfile gamerProfile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GamerProfileResource gamerProfileResource = new GamerProfileResource();
        ReflectionTestUtils.setField(gamerProfileResource, "gamerProfileService", gamerProfileService);
        this.restGamerProfileMockMvc = MockMvcBuilders.standaloneSetup(gamerProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GamerProfile createEntity(EntityManager em) {
        GamerProfile gamerProfile = new GamerProfile()
                .name(DEFAULT_NAME);
        return gamerProfile;
    }

    @Before
    public void initTest() {
        gamerProfileSearchRepository.deleteAll();
        gamerProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createGamerProfile() throws Exception {
        int databaseSizeBeforeCreate = gamerProfileRepository.findAll().size();

        // Create the GamerProfile

        restGamerProfileMockMvc.perform(post("/api/gamer-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gamerProfile)))
                .andExpect(status().isCreated());

        // Validate the GamerProfile in the database
        List<GamerProfile> gamerProfiles = gamerProfileRepository.findAll();
        assertThat(gamerProfiles).hasSize(databaseSizeBeforeCreate + 1);
        GamerProfile testGamerProfile = gamerProfiles.get(gamerProfiles.size() - 1);
        assertThat(testGamerProfile.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the GamerProfile in ElasticSearch
        GamerProfile gamerProfileEs = gamerProfileSearchRepository.findOne(testGamerProfile.getId());
        assertThat(gamerProfileEs).isEqualToComparingFieldByField(testGamerProfile);
    }

    @Test
    @Transactional
    public void getAllGamerProfiles() throws Exception {
        // Initialize the database
        gamerProfileRepository.saveAndFlush(gamerProfile);

        // Get all the gamerProfiles
        restGamerProfileMockMvc.perform(get("/api/gamer-profiles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gamerProfile.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getGamerProfile() throws Exception {
        // Initialize the database
        gamerProfileRepository.saveAndFlush(gamerProfile);

        // Get the gamerProfile
        restGamerProfileMockMvc.perform(get("/api/gamer-profiles/{id}", gamerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gamerProfile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGamerProfile() throws Exception {
        // Get the gamerProfile
        restGamerProfileMockMvc.perform(get("/api/gamer-profiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGamerProfile() throws Exception {
        // Initialize the database
        gamerProfileService.save(gamerProfile);

        int databaseSizeBeforeUpdate = gamerProfileRepository.findAll().size();

        // Update the gamerProfile
        GamerProfile updatedGamerProfile = gamerProfileRepository.findOne(gamerProfile.getId());
        updatedGamerProfile
                .name(UPDATED_NAME);

        restGamerProfileMockMvc.perform(put("/api/gamer-profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGamerProfile)))
                .andExpect(status().isOk());

        // Validate the GamerProfile in the database
        List<GamerProfile> gamerProfiles = gamerProfileRepository.findAll();
        assertThat(gamerProfiles).hasSize(databaseSizeBeforeUpdate);
        GamerProfile testGamerProfile = gamerProfiles.get(gamerProfiles.size() - 1);
        assertThat(testGamerProfile.getName()).isEqualTo(UPDATED_NAME);

        // Validate the GamerProfile in ElasticSearch
        GamerProfile gamerProfileEs = gamerProfileSearchRepository.findOne(testGamerProfile.getId());
        assertThat(gamerProfileEs).isEqualToComparingFieldByField(testGamerProfile);
    }

    @Test
    @Transactional
    public void deleteGamerProfile() throws Exception {
        // Initialize the database
        gamerProfileService.save(gamerProfile);

        int databaseSizeBeforeDelete = gamerProfileRepository.findAll().size();

        // Get the gamerProfile
        restGamerProfileMockMvc.perform(delete("/api/gamer-profiles/{id}", gamerProfile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean gamerProfileExistsInEs = gamerProfileSearchRepository.exists(gamerProfile.getId());
        assertThat(gamerProfileExistsInEs).isFalse();

        // Validate the database is empty
        List<GamerProfile> gamerProfiles = gamerProfileRepository.findAll();
        assertThat(gamerProfiles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGamerProfile() throws Exception {
        // Initialize the database
        gamerProfileService.save(gamerProfile);

        // Search the gamerProfile
        restGamerProfileMockMvc.perform(get("/api/_search/gamer-profiles?query=id:" + gamerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gamerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
