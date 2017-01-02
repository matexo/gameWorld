package com.gameworld.app.service.impl;

import com.gameworld.app.domain.Conversation;
import com.gameworld.app.repository.ConversationRepository;
import com.gameworld.app.repository.GamerProfileRepository;
import com.gameworld.app.repository.search.ConversationSearchRepository;
import com.gameworld.app.security.SecurityUtils;
import com.gameworld.app.service.MessageService;
import com.gameworld.app.domain.Message;
import com.gameworld.app.repository.MessageRepository;
import com.gameworld.app.repository.search.MessageSearchRepository;
import com.gameworld.app.util.DateUtil;
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
 * Service Implementation for managing Message.
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService{

    private final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    @Inject
    private ConversationRepository conversationRepository;

    @Inject
    private GamerProfileRepository gamerProfileRepository;

    @Inject
    private ConversationSearchRepository conversationSearchRepository;

    public Message save(Message message) {
        log.debug("Request to save Message : {}", message);
        Message msg = new Message();
        msg.setSendTime(DateUtil.getNowDateTime());
        msg.setIsNew(true);
        msg.setAuthorProfile(gamerProfileRepository.findGamerProfileByName(SecurityUtils.getCurrentUserLogin()));
        msg.setText(message.getText());
        // pobierze conversation i sprawdzi czy ok
        msg.setConversation(message.getConversation());
        msg = messageRepository.save(msg);
        messageSearchRepository.save(msg);
        Conversation conversation =msg.getConversation();
        conversation.setLastMessage(msg);
        conversation.setHasNew(true);
        conversation.setLastUpdate(DateUtil.getNowDateTime());
        conversation = conversationRepository.save(conversation);
        conversationSearchRepository.save(conversation);
        return msg;
    }

    @Transactional(readOnly = true)
    public Page<Message> findAll(Pageable pageable) {
        log.debug("Request to get all Messages");
        Page<Message> result = messageRepository.findAll(pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public Message findOne(Long id) {
        log.debug("Request to get Message : {}", id);
        Message message = messageRepository.findOne(id);
        return message;
    }

    public void delete(Long id) {
        log.debug("Request to delete Message : {}", id);
        messageRepository.delete(id);
        messageSearchRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<Message> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Messages for query {}", query);
        Page<Message> result = messageSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Override
    public Page<Message> findAllMessageToConversation(Long conversationId, Pageable pageable) {
        Page<Message> result = null;
        // czy user nalezy do konversacji
        // jak tak to daj wyniki
        result = messageRepository.findAllMessageToConversation(conversationId,pageable);
        return result;
    }
}
