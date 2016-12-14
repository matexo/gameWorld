package com.gameworld.app.service.impl;

import com.gameworld.app.service.CommentService;
import com.gameworld.app.domain.Comment;
import com.gameworld.app.repository.CommentRepository;
import com.gameworld.app.repository.search.CommentSearchRepository;
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
public class CommentServiceImpl implements CommentService{

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    
    @Inject
    private CommentRepository commentRepository;

    @Inject
    private CommentSearchRepository commentSearchRepository;

    /**
     * Save a comment.
     *
     * @param comment the entity to save
     * @return the persisted entity
     */
    public Comment save(Comment comment) {
        log.debug("Request to save Comment : {}", comment);
        Comment result = commentRepository.save(comment);
        commentSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the comments.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Comment> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        Page<Comment> result = commentRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one comment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Comment findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        Comment comment = commentRepository.findOne(id);
        return comment;
    }

    /**
     *  Delete the  comment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.delete(id);
        commentSearchRepository.delete(id);
    }

    /**
     * Search for the comment corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Comment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Comments for query {}", query);
        Page<Comment> result = commentSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
