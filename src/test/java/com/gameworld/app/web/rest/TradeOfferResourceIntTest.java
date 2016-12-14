package com.gameworld.app.web.rest;

import com.gameworld.app.GameWorldApp;

import com.gameworld.app.domain.TradeOffer;
import com.gameworld.app.repository.TradeOfferRepository;
import com.gameworld.app.service.TradeOfferService;
import com.gameworld.app.repository.search.TradeOfferSearchRepository;

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

import com.gameworld.app.domain.enumeration.TradeOfferStatus;
/**
 * Test class for the TradeOfferResource REST controller.
 *
 * @see TradeOfferResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameWorldApp.class)
public class TradeOfferResourceIntTest {

    private static final Double DEFAULT_PAYMENT = 1D;
    private static final Double UPDATED_PAYMENT = 2D;

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIMESTAMP_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_TIMESTAMP);

    private static final TradeOfferStatus DEFAULT_STATUS = TradeOfferStatus.PENDING;
    private static final TradeOfferStatus UPDATED_STATUS = TradeOfferStatus.REJECTED;

    @Inject
    private TradeOfferRepository tradeOfferRepository;

    @Inject
    private TradeOfferService tradeOfferService;

    @Inject
    private TradeOfferSearchRepository tradeOfferSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTradeOfferMockMvc;

    private TradeOffer tradeOffer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TradeOfferResource tradeOfferResource = new TradeOfferResource();
        ReflectionTestUtils.setField(tradeOfferResource, "tradeOfferService", tradeOfferService);
        this.restTradeOfferMockMvc = MockMvcBuilders.standaloneSetup(tradeOfferResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeOffer createEntity(EntityManager em) {
        TradeOffer tradeOffer = new TradeOffer()
                .payment(DEFAULT_PAYMENT)
                .timestamp(DEFAULT_TIMESTAMP)
                .status(DEFAULT_STATUS);
        return tradeOffer;
    }

    @Before
    public void initTest() {
        tradeOfferSearchRepository.deleteAll();
        tradeOffer = createEntity(em);
    }

    @Test
    @Transactional
    public void createTradeOffer() throws Exception {
        int databaseSizeBeforeCreate = tradeOfferRepository.findAll().size();

        // Create the TradeOffer

        restTradeOfferMockMvc.perform(post("/api/trade-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeOffer)))
                .andExpect(status().isCreated());

        // Validate the TradeOffer in the database
        List<TradeOffer> tradeOffers = tradeOfferRepository.findAll();
        assertThat(tradeOffers).hasSize(databaseSizeBeforeCreate + 1);
        TradeOffer testTradeOffer = tradeOffers.get(tradeOffers.size() - 1);
        assertThat(testTradeOffer.getPayment()).isEqualTo(DEFAULT_PAYMENT);
        assertThat(testTradeOffer.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testTradeOffer.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the TradeOffer in ElasticSearch
        TradeOffer tradeOfferEs = tradeOfferSearchRepository.findOne(testTradeOffer.getId());
        assertThat(tradeOfferEs).isEqualToComparingFieldByField(testTradeOffer);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeOfferRepository.findAll().size();
        // set the field null
        tradeOffer.setTimestamp(null);

        // Create the TradeOffer, which fails.

        restTradeOfferMockMvc.perform(post("/api/trade-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeOffer)))
                .andExpect(status().isBadRequest());

        List<TradeOffer> tradeOffers = tradeOfferRepository.findAll();
        assertThat(tradeOffers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeOfferRepository.findAll().size();
        // set the field null
        tradeOffer.setStatus(null);

        // Create the TradeOffer, which fails.

        restTradeOfferMockMvc.perform(post("/api/trade-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeOffer)))
                .andExpect(status().isBadRequest());

        List<TradeOffer> tradeOffers = tradeOfferRepository.findAll();
        assertThat(tradeOffers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTradeOffers() throws Exception {
        // Initialize the database
        tradeOfferRepository.saveAndFlush(tradeOffer);

        // Get all the tradeOffers
        restTradeOfferMockMvc.perform(get("/api/trade-offers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tradeOffer.getId().intValue())))
                .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.doubleValue())))
                .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getTradeOffer() throws Exception {
        // Initialize the database
        tradeOfferRepository.saveAndFlush(tradeOffer);

        // Get the tradeOffer
        restTradeOfferMockMvc.perform(get("/api/trade-offers/{id}", tradeOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tradeOffer.getId().intValue()))
            .andExpect(jsonPath("$.payment").value(DEFAULT_PAYMENT.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTradeOffer() throws Exception {
        // Get the tradeOffer
        restTradeOfferMockMvc.perform(get("/api/trade-offers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTradeOffer() throws Exception {
        // Initialize the database
        tradeOfferService.save(tradeOffer);

        int databaseSizeBeforeUpdate = tradeOfferRepository.findAll().size();

        // Update the tradeOffer
        TradeOffer updatedTradeOffer = tradeOfferRepository.findOne(tradeOffer.getId());
        updatedTradeOffer
                .payment(UPDATED_PAYMENT)
                .timestamp(UPDATED_TIMESTAMP)
                .status(UPDATED_STATUS);

        restTradeOfferMockMvc.perform(put("/api/trade-offers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTradeOffer)))
                .andExpect(status().isOk());

        // Validate the TradeOffer in the database
        List<TradeOffer> tradeOffers = tradeOfferRepository.findAll();
        assertThat(tradeOffers).hasSize(databaseSizeBeforeUpdate);
        TradeOffer testTradeOffer = tradeOffers.get(tradeOffers.size() - 1);
        assertThat(testTradeOffer.getPayment()).isEqualTo(UPDATED_PAYMENT);
        assertThat(testTradeOffer.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testTradeOffer.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the TradeOffer in ElasticSearch
        TradeOffer tradeOfferEs = tradeOfferSearchRepository.findOne(testTradeOffer.getId());
        assertThat(tradeOfferEs).isEqualToComparingFieldByField(testTradeOffer);
    }

    @Test
    @Transactional
    public void deleteTradeOffer() throws Exception {
        // Initialize the database
        tradeOfferService.save(tradeOffer);

        int databaseSizeBeforeDelete = tradeOfferRepository.findAll().size();

        // Get the tradeOffer
        restTradeOfferMockMvc.perform(delete("/api/trade-offers/{id}", tradeOffer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean tradeOfferExistsInEs = tradeOfferSearchRepository.exists(tradeOffer.getId());
        assertThat(tradeOfferExistsInEs).isFalse();

        // Validate the database is empty
        List<TradeOffer> tradeOffers = tradeOfferRepository.findAll();
        assertThat(tradeOffers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTradeOffer() throws Exception {
        // Initialize the database
        tradeOfferService.save(tradeOffer);

        // Search the tradeOffer
        restTradeOfferMockMvc.perform(get("/api/_search/trade-offers?query=id:" + tradeOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tradeOffer.getId().intValue())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP_STR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
