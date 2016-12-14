package com.gameworld.app.web.rest;

import com.gameworld.app.GameWorldApp;

import com.gameworld.app.domain.Adress;
import com.gameworld.app.repository.AdressRepository;
import com.gameworld.app.service.AdressService;
import com.gameworld.app.repository.search.AdressSearchRepository;

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
 * Test class for the AdressResource REST controller.
 *
 * @see AdressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameWorldApp.class)
public class AdressResourceIntTest {

    private static final String DEFAULT_STREET_NAME = "AAAAA";
    private static final String UPDATED_STREET_NAME = "BBBBB";

    private static final String DEFAULT_HOUSE_NO = "AAAAA";
    private static final String UPDATED_HOUSE_NO = "BBBBB";

    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBB";

    @Inject
    private AdressRepository adressRepository;

    @Inject
    private AdressService adressService;

    @Inject
    private AdressSearchRepository adressSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAdressMockMvc;

    private Adress adress;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdressResource adressResource = new AdressResource();
        ReflectionTestUtils.setField(adressResource, "adressService", adressService);
        this.restAdressMockMvc = MockMvcBuilders.standaloneSetup(adressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adress createEntity(EntityManager em) {
        Adress adress = new Adress()
                .streetName(DEFAULT_STREET_NAME)
                .houseNo(DEFAULT_HOUSE_NO)
                .city(DEFAULT_CITY)
                .zipCode(DEFAULT_ZIP_CODE);
        return adress;
    }

    @Before
    public void initTest() {
        adressSearchRepository.deleteAll();
        adress = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdress() throws Exception {
        int databaseSizeBeforeCreate = adressRepository.findAll().size();

        // Create the Adress

        restAdressMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adress)))
                .andExpect(status().isCreated());

        // Validate the Adress in the database
        List<Adress> adresses = adressRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeCreate + 1);
        Adress testAdress = adresses.get(adresses.size() - 1);
        assertThat(testAdress.getStreetName()).isEqualTo(DEFAULT_STREET_NAME);
        assertThat(testAdress.getHouseNo()).isEqualTo(DEFAULT_HOUSE_NO);
        assertThat(testAdress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAdress.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);

        // Validate the Adress in ElasticSearch
        Adress adressEs = adressSearchRepository.findOne(testAdress.getId());
        assertThat(adressEs).isEqualToComparingFieldByField(testAdress);
    }

    @Test
    @Transactional
    public void checkStreetNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adressRepository.findAll().size();
        // set the field null
        adress.setStreetName(null);

        // Create the Adress, which fails.

        restAdressMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adress)))
                .andExpect(status().isBadRequest());

        List<Adress> adresses = adressRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHouseNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = adressRepository.findAll().size();
        // set the field null
        adress.setHouseNo(null);

        // Create the Adress, which fails.

        restAdressMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adress)))
                .andExpect(status().isBadRequest());

        List<Adress> adresses = adressRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = adressRepository.findAll().size();
        // set the field null
        adress.setCity(null);

        // Create the Adress, which fails.

        restAdressMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adress)))
                .andExpect(status().isBadRequest());

        List<Adress> adresses = adressRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = adressRepository.findAll().size();
        // set the field null
        adress.setZipCode(null);

        // Create the Adress, which fails.

        restAdressMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adress)))
                .andExpect(status().isBadRequest());

        List<Adress> adresses = adressRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdresses() throws Exception {
        // Initialize the database
        adressRepository.saveAndFlush(adress);

        // Get all the adresses
        restAdressMockMvc.perform(get("/api/adresses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(adress.getId().intValue())))
                .andExpect(jsonPath("$.[*].streetName").value(hasItem(DEFAULT_STREET_NAME.toString())))
                .andExpect(jsonPath("$.[*].houseNo").value(hasItem(DEFAULT_HOUSE_NO.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())));
    }

    @Test
    @Transactional
    public void getAdress() throws Exception {
        // Initialize the database
        adressRepository.saveAndFlush(adress);

        // Get the adress
        restAdressMockMvc.perform(get("/api/adresses/{id}", adress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adress.getId().intValue()))
            .andExpect(jsonPath("$.streetName").value(DEFAULT_STREET_NAME.toString()))
            .andExpect(jsonPath("$.houseNo").value(DEFAULT_HOUSE_NO.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdress() throws Exception {
        // Get the adress
        restAdressMockMvc.perform(get("/api/adresses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdress() throws Exception {
        // Initialize the database
        adressService.save(adress);

        int databaseSizeBeforeUpdate = adressRepository.findAll().size();

        // Update the adress
        Adress updatedAdress = adressRepository.findOne(adress.getId());
        updatedAdress
                .streetName(UPDATED_STREET_NAME)
                .houseNo(UPDATED_HOUSE_NO)
                .city(UPDATED_CITY)
                .zipCode(UPDATED_ZIP_CODE);

        restAdressMockMvc.perform(put("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAdress)))
                .andExpect(status().isOk());

        // Validate the Adress in the database
        List<Adress> adresses = adressRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeUpdate);
        Adress testAdress = adresses.get(adresses.size() - 1);
        assertThat(testAdress.getStreetName()).isEqualTo(UPDATED_STREET_NAME);
        assertThat(testAdress.getHouseNo()).isEqualTo(UPDATED_HOUSE_NO);
        assertThat(testAdress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAdress.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);

        // Validate the Adress in ElasticSearch
        Adress adressEs = adressSearchRepository.findOne(testAdress.getId());
        assertThat(adressEs).isEqualToComparingFieldByField(testAdress);
    }

    @Test
    @Transactional
    public void deleteAdress() throws Exception {
        // Initialize the database
        adressService.save(adress);

        int databaseSizeBeforeDelete = adressRepository.findAll().size();

        // Get the adress
        restAdressMockMvc.perform(delete("/api/adresses/{id}", adress.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean adressExistsInEs = adressSearchRepository.exists(adress.getId());
        assertThat(adressExistsInEs).isFalse();

        // Validate the database is empty
        List<Adress> adresses = adressRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAdress() throws Exception {
        // Initialize the database
        adressService.save(adress);

        // Search the adress
        restAdressMockMvc.perform(get("/api/_search/adresses?query=id:" + adress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adress.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetName").value(hasItem(DEFAULT_STREET_NAME.toString())))
            .andExpect(jsonPath("$.[*].houseNo").value(hasItem(DEFAULT_HOUSE_NO.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())));
    }
}
