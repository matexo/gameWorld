package com.gameworld.app.service;

import com.gameworld.app.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Message.
 */
public interface MessageService {

    Message save(Message message);
    Page<Message> findAll(Pageable pageable);
    Message findOne(Long id);
    void delete(Long id);

    Page<Message> search(String query, Pageable pageable);

    Page<Message> findAllMessageToConversation(Long conversationId , Pageable pageable);
}
