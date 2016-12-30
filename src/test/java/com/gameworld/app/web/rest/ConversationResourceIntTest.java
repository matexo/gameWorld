package com.gameworld.app.web.rest;

import com.gameworld.app.GameWorldApp;

import com.gameworld.app.domain.Conversation;
import com.gameworld.app.repository.ConversationRepository;
import com.gameworld.app.service.ConversationService;
import com.gameworld.app.repository.search.ConversationSearchRepository;

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

/**
 * Test class for the ConversationResource REST controller.
 *
 * @see ConversationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameWorldApp.class)
public class ConversationResourceIntTest {

    private static final Boolean DEFAULT_HAS_NEW = false;
    private static final Boolean UPDATED_HAS_NEW = true;

    private static final ZonedDateTime DEFAULT_LAST_UPDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_UPDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_UPDATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_LAST_UPDATE);

    @Inject
    private ConversationRepository conversationRepository;

    @Inject
    private ConversationService conversationService;

    @Inject
    private ConversationSearchRepository conversationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restConversationMockMvc;

    private Conversation conversation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConversationResource conversationResource = new ConversationResource();
        ReflectionTestUtils.setField(conversationResource, "conversationService", conversationService);
        this.restConversationMockMvc = MockMvcBuilders.standaloneSetup(conversationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conversation createEntity(EntityManager em) {
        Conversation conversation = new Conversation()
                .hasNew(DEFAULT_HAS_NEW)
                .lastUpdate(DEFAULT_LAST_UPDATE);
        return conversation;
    }

    @Before
    public void initTest() {
        conversationSearchRepository.deleteAll();
        conversation = createEntity(em);
    }

    @Test
    @Transactional
    public void createConversation() throws Exception {
        int databaseSizeBeforeCreate = conversationRepository.findAll().size();

        // Create the Conversation

        restConversationMockMvc.perform(post("/api/conversations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(conversation)))
                .andExpect(status().isCreated());

        // Validate the Conversation in the database
        List<Conversation> conversations = conversationRepository.findAll();
        assertThat(conversations).hasSize(databaseSizeBeforeCreate + 1);
        Conversation testConversation = conversations.get(conversations.size() - 1);
        assertThat(testConversation.isHasNew()).isEqualTo(DEFAULT_HAS_NEW);
        assertThat(testConversation.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);

        // Validate the Conversation in ElasticSearch
        Conversation conversationEs = conversationSearchRepository.findOne(testConversation.getId());
        assertThat(conversationEs).isEqualToComparingFieldByField(testConversation);
    }

    @Test
    @Transactional
    public void getAllConversations() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversations
        restConversationMockMvc.perform(get("/api/conversations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(conversation.getId().intValue())))
                .andExpect(jsonPath("$.[*].hasNew").value(hasItem(DEFAULT_HAS_NEW.booleanValue())))
                .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE_STR)));
    }

    @Test
    @Transactional
    public void getConversation() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get the conversation
        restConversationMockMvc.perform(get("/api/conversations/{id}", conversation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conversation.getId().intValue()))
            .andExpect(jsonPath("$.hasNew").value(DEFAULT_HAS_NEW.booleanValue()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingConversation() throws Exception {
        // Get the conversation
        restConversationMockMvc.perform(get("/api/conversations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConversation() throws Exception {
        // Initialize the database
        conversationService.save(conversation);

        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();

        // Update the conversation
        Conversation updatedConversation = conversationRepository.findOne(conversation.getId());
        updatedConversation
                .hasNew(UPDATED_HAS_NEW)
                .lastUpdate(UPDATED_LAST_UPDATE);

        restConversationMockMvc.perform(put("/api/conversations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedConversation)))
                .andExpect(status().isOk());

        // Validate the Conversation in the database
        List<Conversation> conversations = conversationRepository.findAll();
        assertThat(conversations).hasSize(databaseSizeBeforeUpdate);
        Conversation testConversation = conversations.get(conversations.size() - 1);
        assertThat(testConversation.isHasNew()).isEqualTo(UPDATED_HAS_NEW);
        assertThat(testConversation.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);

        // Validate the Conversation in ElasticSearch
        Conversation conversationEs = conversationSearchRepository.findOne(testConversation.getId());
        assertThat(conversationEs).isEqualToComparingFieldByField(testConversation);
    }

    @Test
    @Transactional
    public void deleteConversation() throws Exception {
        // Initialize the database
        conversationService.save(conversation);

        int databaseSizeBeforeDelete = conversationRepository.findAll().size();

        // Get the conversation
        restConversationMockMvc.perform(delete("/api/conversations/{id}", conversation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean conversationExistsInEs = conversationSearchRepository.exists(conversation.getId());
        assertThat(conversationExistsInEs).isFalse();

        // Validate the database is empty
        List<Conversation> conversations = conversationRepository.findAll();
        assertThat(conversations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchConversation() throws Exception {
        // Initialize the database
        conversationService.save(conversation);

        // Search the conversation
        restConversationMockMvc.perform(get("/api/_search/conversations?query=id:" + conversation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversation.getId().intValue())))
            .andExpect(jsonPath("$.[*].hasNew").value(hasItem(DEFAULT_HAS_NEW.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE_STR)));
    }
}
