package com.gameworld.app.service.impl;

import com.gameworld.app.domain.GamerProfile;
import com.gameworld.app.domain.MarketOffer;
import com.gameworld.app.domain.enumeration.OfferStatus;
import com.gameworld.app.repository.GamerProfileRepository;
import com.gameworld.app.repository.MarketOfferRepository;
import com.gameworld.app.security.SecurityUtils;
import com.gameworld.app.service.CommentService;
import com.gameworld.app.domain.Comment;
import com.gameworld.app.repository.CommentRepository;
import com.gameworld.app.repository.search.CommentSearchRepository;
import com.gameworld.app.service.MarketOfferService;
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
 * Service Implementation for managing Comment.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private CommentSearchRepository commentSearchRepository;

    @Inject
    private MarketOfferRepository marketOfferRepository;

    @Inject
    private GamerProfileRepository gamerProfileRepository;

    /**
     * Save a comment.
     *
     * @param comment the entity to save
     * @return the persisted entity
     */
    public Comment save(Comment comment) {
        log.debug("Request to save Comment : {}", comment);
        String username = SecurityUtils.getCurrentUserLogin();
        GamerProfile gamerProfile = gamerProfileRepository.findGamerProfileByName(username);                            // pobranie profilu osoby
        if (gamerProfile != null &&                                                                                     // jezeli profil istnieje
            comment.getMarketOffer() != null &&                                                                         // i jest zaznaczenie do ktorej oferty ma byc dodany komenatrz
            comment.getMarketOffer().getId() != null) {
            MarketOffer marketOffer = marketOfferRepository.findOne(comment.getMarketOffer().getId());                  // pobieramy marketOffer z bazy                      // pobieramy marketOffer z bazy
            if (marketOffer != null && marketOffer.getOfferStatus().equals(OfferStatus.ENDED)) {                        // jezeli istnieje na bazie i jest zakonczona
                if (marketOffer.getCreateProfile().getName().equals(gamerProfile.getName()) ||                           // sprawdzamy czy uzytkownik ma mozliwosc dodania komentarza do oferty
                    marketOffer.getEndOfferProfile().getName().equals(gamerProfile.getName()))                          // musi byc osoba ktora stworzyla oferte albo ja zakonczyla
                    for (Comment com : marketOffer.getComments()) {                                                         // dodatkwo mozna dodac tylko jeden komenatrz
                        if (com.getAuthorProfile().getName().equals(gamerProfile.getName()))
                            return null;
                    }
                Comment newComment = new Comment();                                                                     // tworzymy nowy komenatrz na bazie tego co dostalismy
                newComment.setAuthorProfile(gamerProfile);
                newComment.setTimestamp(DateUtil.getNowDateTime());
                if (comment.getRating() == null)
                    return null;                                                            // musi byc jego stan
                newComment.setRating(comment.getRating());
                newComment.setComment(comment.getComment());
                newComment.setMarketOffer(marketOffer);
                newComment = commentRepository.save(newComment);
                commentSearchRepository.save(newComment);
                //czy potrzebne jest zapisywanie do marketSearchrepo
                return newComment;
            }
        }
        return null;
    }

    /**
     * Get all the comments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Comment> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        Page<Comment> result = commentRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one comment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Comment findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        Comment comment = commentRepository.findOne(id);
        return comment;
    }

    /**
     * Delete the  comment by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.delete(id);
        commentSearchRepository.delete(id);
    }

    /**
     * Search for the comment corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Comment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Comments for query {}", query);
        Page<Comment> result = commentSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
