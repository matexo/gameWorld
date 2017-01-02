package com.gameworld.app.service.impl;

import com.gameworld.app.domain.Game;
import com.gameworld.app.domain.GamerProfile;
import com.gameworld.app.domain.User;
import com.gameworld.app.repository.GamerProfileRepository;
import com.gameworld.app.repository.MessageRepository;
import com.gameworld.app.repository.UserRepository;
import com.gameworld.app.security.AuthoritiesConstants;
import com.gameworld.app.security.SecurityUtils;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Conversation.
 */
@Service
@Transactional
public class ConversationServiceImpl implements ConversationService {

    private final Logger log = LoggerFactory.getLogger(ConversationServiceImpl.class);

    @Inject
    private ConversationRepository conversationRepository;

    @Inject
    private ConversationSearchRepository conversationSearchRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private GamerProfileRepository gamerProfileRepository;


    public Conversation save(Conversation conversation) {
        log.debug("Request to save Conversation : {}", conversation);
        Conversation result = conversationRepository.save(conversation);
        conversationSearchRepository.save(result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<Conversation> findAll(Pageable pageable) {
        log.debug("Request to get all Conversations");
        Page<Conversation> result = null;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            result = conversationRepository.findAll(pageable);
        else result = conversationRepository.findAllByGamerName(SecurityUtils.getCurrentUserLogin(), pageable);
        result.getContent().forEach(conversation -> conversation.setMessages(messageRepository.getLastMessage(conversation.getId())));
        return result;
    }

    @Transactional(readOnly = true)
    public Conversation findOne(Long id) {
        log.debug("Request to get Conversation : {}", id);
        Conversation conversation = null;
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            conversation = conversationRepository.findOne(id);
        else
            conversation = conversationRepository.findOneByGamerName(id , SecurityUtils.getCurrentUserLogin());
        return conversation;
    }

    public void delete(Long id) {
        log.debug("Request to delete Conversation : {}", id);
        conversationRepository.delete(id);
        conversationSearchRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<Conversation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Conversations for query {}", query);
        Page<Conversation> result = conversationSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Transactional
    public Conversation getConversation(Long recievierId) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (user.isPresent()) {
            GamerProfile receiverProfile = gamerProfileRepository.findOne(recievierId);
            if (receiverProfile == null) return null;
            GamerProfile senderProfile = user.get().getGamerProfile();
            Set<Conversation> conversations = senderProfile.getConversations();
            Boolean isExistConversation = false;
            for (Conversation conversation : conversations) {
                Set<GamerProfile> gamerProfiles = conversation.getProfiles();
                for (GamerProfile gamerProfile : gamerProfiles) {
                    if (gamerProfile.getId().equals(recievierId)) {
                        return conversation;
                    }
                }
            }
            Conversation newConversation = new Conversation();
            newConversation.addProfiles(senderProfile);
            newConversation.addProfiles(receiverProfile);
            newConversation = conversationRepository.save(newConversation);
            conversationSearchRepository.save(newConversation);
            return newConversation;
        }
        return null;
    }
}
