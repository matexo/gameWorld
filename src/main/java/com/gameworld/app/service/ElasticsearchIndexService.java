package com.gameworld.app.service;

import com.codahale.metrics.annotation.Timed;
import com.gameworld.app.domain.*;
import com.gameworld.app.repository.*;
import com.gameworld.app.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    @Inject
    private AdressRepository adressRepository;

    @Inject
    private AdressSearchRepository adressSearchRepository;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private CommentSearchRepository commentSearchRepository;

    @Inject
    private ConversationRepository conversationRepository;

    @Inject
    private ConversationSearchRepository conversationSearchRepository;

    @Inject
    private GameRepository gameRepository;

    @Inject
    private GameSearchRepository gameSearchRepository;

    @Inject
    private GamerProfileRepository gamerProfileRepository;

    @Inject
    private GamerProfileSearchRepository gamerProfileSearchRepository;

    @Inject
    private GameTypeRepository gameTypeRepository;

    @Inject
    private GameTypeSearchRepository gameTypeSearchRepository;

    @Inject
    private MarketOfferRepository marketOfferRepository;

    @Inject
    private MarketOfferSearchRepository marketOfferSearchRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    @Inject
    private PlatformRepository platformRepository;

    @Inject
    private PlatformSearchRepository platformSearchRepository;

    @Inject
    private TradeOfferRepository tradeOfferRepository;

    @Inject
    private TradeOfferSearchRepository tradeOfferSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    @Timed
    public void reindexAll() {
        reindexForClass(Adress.class, adressRepository, adressSearchRepository);
        reindexForClass(Comment.class, commentRepository, commentSearchRepository);
        reindexForClass(Conversation.class, conversationRepository, conversationSearchRepository);
        reindexForClass(Game.class, gameRepository, gameSearchRepository);
        reindexForClass(GamerProfile.class, gamerProfileRepository, gamerProfileSearchRepository);
        reindexForClass(GameType.class, gameTypeRepository, gameTypeSearchRepository);
        reindexForClass(MarketOffer.class, marketOfferRepository, marketOfferSearchRepository);
        reindexForClass(Message.class, messageRepository, messageSearchRepository);
        reindexForClass(Platform.class, platformRepository, platformSearchRepository);
        reindexForClass(TradeOffer.class, tradeOfferRepository, tradeOfferSearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    private <T> void reindexForClass(Class<T> entityClass, JpaRepository<T, Long> jpaRepository,
                                                          ElasticsearchRepository<T, Long> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
