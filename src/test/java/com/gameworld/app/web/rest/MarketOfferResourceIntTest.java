package com.gameworld.app.web.rest;

import com.gameworld.app.GameWorldApp;

import com.gameworld.app.domain.MarketOffer;
import com.gameworld.app.repository.MarketOfferRepository;
import com.gameworld.app.service.MarketOfferService;
import com.gameworld.app.repository.search.MarketOfferSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gameworld.app.domain.enumeration.OfferType;
import com.gameworld.app.domain.enumeration.OfferStatus;
import com.gameworld.app.domain.enumeration.GameState;
/**
 * Test class for the MarketOfferResource REST controller.
 *
 * @see MarketOfferResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameWorldApp.class)
public class MarketOfferResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_END_DATE);

    private static final OfferType DEFAULT_OFFER_TYPE = OfferType.SELL;
    private static final OfferType UPDATED_OFFER_TYPE = OfferType.BUY;

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    private static final OfferStatus DEFAULT_OFFER_STATUS = OfferStatus.NEW;
    private static final OfferStatus UPDATED_OFFER_STATUS = OfferStatus.ENDED;

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final GameState DEFAULT_GAME_STATE = GameState.NEW;
    private static final GameState UPDATED_GAME_STATE = GameState.USED;

    private static final Double DEFAULT_SHIPPING_COST = 0D;
    private static final Double UPDATED_SHIPPING_COST = 1D;

    @Inject
    private MarketOfferRepository marketOfferRepository;

    @Inject
    private MarketOfferService marketOfferService;

    @Inject
    private MarketOfferSearchRepository marketOfferSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMarketOfferMockMvc;

    private MarketOffer marketOffer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MarketOfferResource marketOfferResource = new MarketOfferResource();
        ReflectionTestUtils.setField(marketOfferResource, "marketOfferService", marketOfferService);
        this.restMarketOfferMockMvc = MockMvcBuilders.standaloneSetup(marketOfferResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketOffer createEntity(EntityManager em) {
        MarketOffer marketOffer = new MarketOffer()
                .createDate(DEFAULT_CREATE_DATE)
                .endDate(DEFAULT_END_DATE)
                .offerType(DEFAULT_OFFER_TYPE)
                .price(DEFAULT_PRICE)
                .offerStatus(DEFAULT_OFFER_STATUS)
                .description(DEFAULT_DESCRIPTION)
                .gameState(DEFAULT_GAME_STATE)
                .shippingCost(DEFAULT_SHIPPING_COST);
        return marketOffer;
    }

    @Before
    public void initTest() {
        marketOfferSearchRepository.deleteAll();
        marketOffer = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarketOffer() throws Exception {
        int databaseSizeBeforeCreate = marketOfferRepository.findAll().size();

        // Create the MarketOffer

        restMarketOfferMockMvc.perform(post("/api/market-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marketOffer)))
                .andExpect(status().isCreated());

        // Validate the MarketOffer in the database
        List<MarketOffer> marketOffers = marketOfferRepository.findAll();
        assertThat(marketOffers).hasSize(databaseSizeBeforeCreate + 1);
        MarketOffer testMarketOffer = marketOffers.get(marketOffers.size() - 1);
        assertThat(testMarketOffer.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testMarketOffer.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMarketOffer.getOfferType()).isEqualTo(DEFAULT_OFFER_TYPE);
        assertThat(testMarketOffer.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMarketOffer.getOfferStatus()).isEqualTo(DEFAULT_OFFER_STATUS);
        assertThat(testMarketOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMarketOffer.getGameState()).isEqualTo(DEFAULT_GAME_STATE);
        assertThat(testMarketOffer.getShippingCost()).isEqualTo(DEFAULT_SHIPPING_COST);

        // Validate the MarketOffer in ElasticSearch
        MarketOffer marketOfferEs = marketOfferSearchRepository.findOne(testMarketOffer.getId());
        assertThat(marketOfferEs).isEqualToComparingFieldByField(testMarketOffer);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketOfferRepository.findAll().size();
        // set the field null
        marketOffer.setCreateDate(null);

        // Create the MarketOffer, which fails.

        restMarketOfferMockMvc.perform(post("/api/market-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marketOffer)))
                .andExpect(status().isBadRequest());

        List<MarketOffer> marketOffers = marketOfferRepository.findAll();
        assertThat(marketOffers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOfferTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketOfferRepository.findAll().size();
        // set the field null
        marketOffer.setOfferType(null);

        // Create the MarketOffer, which fails.

        restMarketOfferMockMvc.perform(post("/api/market-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marketOffer)))
                .andExpect(status().isBadRequest());

        List<MarketOffer> marketOffers = marketOfferRepository.findAll();
        assertThat(marketOffers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketOfferRepository.findAll().size();
        // set the field null
        marketOffer.setPrice(null);

        // Create the MarketOffer, which fails.

        restMarketOfferMockMvc.perform(post("/api/market-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marketOffer)))
                .andExpect(status().isBadRequest());

        List<MarketOffer> marketOffers = marketOfferRepository.findAll();
        assertThat(marketOffers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOfferStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketOfferRepository.findAll().size();
        // set the field null
        marketOffer.setOfferStatus(null);

        // Create the MarketOffer, which fails.

        restMarketOfferMockMvc.perform(post("/api/market-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marketOffer)))
                .andExpect(status().isBadRequest());

        List<MarketOffer> marketOffers = marketOfferRepository.findAll();
        assertThat(marketOffers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMarketOffers() throws Exception {
        // Initialize the database
        marketOfferRepository.saveAndFlush(marketOffer);

        // Get all the marketOffers
        restMarketOfferMockMvc.perform(get("/api/market-offers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(marketOffer.getId().intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].offerType").value(hasItem(DEFAULT_OFFER_TYPE.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].offerStatus").value(hasItem(DEFAULT_OFFER_STATUS.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].gameState").value(hasItem(DEFAULT_GAME_STATE.toString())))
                .andExpect(jsonPath("$.[*].shippingCost").value(hasItem(DEFAULT_SHIPPING_COST.doubleValue())));
    }

    @Test
    @Transactional
    public void getMarketOffer() throws Exception {
        // Initialize the database
        marketOfferRepository.saveAndFlush(marketOffer);

        // Get the marketOffer
        restMarketOfferMockMvc.perform(get("/api/market-offers/{id}", marketOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(marketOffer.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.offerType").value(DEFAULT_OFFER_TYPE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.offerStatus").value(DEFAULT_OFFER_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.gameState").value(DEFAULT_GAME_STATE.toString()))
            .andExpect(jsonPath("$.shippingCost").value(DEFAULT_SHIPPING_COST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMarketOffer() throws Exception {
        // Get the marketOffer
        restMarketOfferMockMvc.perform(get("/api/market-offers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarketOffer() throws Exception {
        // Initialize the database
        marketOfferService.save(marketOffer);

        int databaseSizeBeforeUpdate = marketOfferRepository.findAll().size();

        // Update the marketOffer
        MarketOffer updatedMarketOffer = marketOfferRepository.findOne(marketOffer.getId());
        updatedMarketOffer
                .createDate(UPDATED_CREATE_DATE)
                .endDate(UPDATED_END_DATE)
                .offerType(UPDATED_OFFER_TYPE)
                .price(UPDATED_PRICE)
                .offerStatus(UPDATED_OFFER_STATUS)
                .description(UPDATED_DESCRIPTION)
                .gameState(UPDATED_GAME_STATE)
                .shippingCost(UPDATED_SHIPPING_COST);

        restMarketOfferMockMvc.perform(put("/api/market-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMarketOffer)))
                .andExpect(status().isOk());

        // Validate the MarketOffer in the database
        List<MarketOffer> marketOffers = marketOfferRepository.findAll();
        assertThat(marketOffers).hasSize(databaseSizeBeforeUpdate);
        MarketOffer testMarketOffer = marketOffers.get(marketOffers.size() - 1);
        assertThat(testMarketOffer.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testMarketOffer.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMarketOffer.getOfferType()).isEqualTo(UPDATED_OFFER_TYPE);
        assertThat(testMarketOffer.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMarketOffer.getOfferStatus()).isEqualTo(UPDATED_OFFER_STATUS);
        assertThat(testMarketOffer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMarketOffer.getGameState()).isEqualTo(UPDATED_GAME_STATE);
        assertThat(testMarketOffer.getShippingCost()).isEqualTo(UPDATED_SHIPPING_COST);

        // Validate the MarketOffer in ElasticSearch
        MarketOffer marketOfferEs = marketOfferSearchRepository.findOne(testMarketOffer.getId());
        assertThat(marketOfferEs).isEqualToComparingFieldByField(testMarketOffer);
    }

    @Test
    @Transactional
    public void deleteMarketOffer() throws Exception {
        // Initialize the database
        marketOfferService.save(marketOffer);

        int databaseSizeBeforeDelete = marketOfferRepository.findAll().size();

        // Get the marketOffer
        restMarketOfferMockMvc.perform(delete("/api/market-offers/{id}", marketOffer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean marketOfferExistsInEs = marketOfferSearchRepository.exists(marketOffer.getId());
        assertThat(marketOfferExistsInEs).isFalse();

        // Validate the database is empty
        List<MarketOffer> marketOffers = marketOfferRepository.findAll();
        assertThat(marketOffers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMarketOffer() throws Exception {
        // Initialize the database
        marketOfferService.save(marketOffer);

        // Search the marketOffer
        restMarketOfferMockMvc.perform(get("/api/_search/market-offers?query=id:" + marketOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketOffer.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
            .andExpect(jsonPath("$.[*].offerType").value(hasItem(DEFAULT_OFFER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].offerStatus").value(hasItem(DEFAULT_OFFER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].gameState").value(hasItem(DEFAULT_GAME_STATE.toString())))
            .andExpect(jsonPath("$.[*].shippingCost").value(hasItem(DEFAULT_SHIPPING_COST.doubleValue())));
    }
}
