package com.gameworld.app.service.impl;

import com.gameworld.app.service.ConversationService;
import com.gameworld.app.domain.Conversation;
import com.gameworld.app.repository.ConversationRepository;
import com.gameworld.app.repository.search.ConversationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Conversation.
 */
@Service
@Transactional
public class ConversationServiceImpl implements ConversationService{

    private final Logger log = LoggerFactory.getLogger(ConversationServiceImpl.class);
    
    @Inject
    private ConversationRepository conversationRepository;

    @Inject
    private ConversationSearchRepository conversationSearchRepository;

    /**
     * Save a conversation.
     *
     * @param conversation the entity to save
     * @return the persisted entity
     */
    public Conversation save(Conversation conversation) {
        log.debug("Request to save Conversation : {}", conversation);
        Conversation result = conversationRepository.save(conversation);
        conversationSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the conversations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Conversation> findAll(Pageable pageable) {
        log.debug("Request to get all Conversations");
        Page<Conversation> result = conversationRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one conversation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Conversation findOne(Long id) {
        log.debug("Request to get Conversation : {}", id);
        Conversation conversation = conversationRepository.findOne(id);
        return conversation;
    }

    /**
     *  Delete the  conversation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Conversation : {}", id);
        conversationRepository.delete(id);
        conversationSearchRepository.delete(id);
    }

    /**
     * Search for the conversation corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Conversation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Conversations for query {}", query);
        Page<Conversation> result = conversationSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
