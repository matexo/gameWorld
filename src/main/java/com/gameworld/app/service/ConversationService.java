package com.gameworld.app.service;

import com.gameworld.app.domain.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Conversation.
 */
public interface ConversationService {

    Conversation save(Conversation conversation);
    Page<Conversation> findAll(Pageable pageable);
    Conversation findOne(Long id);
    void delete(Long id);

    Page<Conversation> search(String query, Pageable pageable);

    Conversation getConversation(Long recievierId);
}
