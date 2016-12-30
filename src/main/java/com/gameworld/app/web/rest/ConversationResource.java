package com.gameworld.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gameworld.app.domain.Conversation;
import com.gameworld.app.service.ConversationService;
import com.gameworld.app.web.rest.util.HeaderUtil;
import com.gameworld.app.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Conversation.
 */
@RestController
@RequestMapping("/api")
public class ConversationResource {

    private final Logger log = LoggerFactory.getLogger(ConversationResource.class);

    @Inject
    private ConversationService conversationService;

    @PostMapping("/conversations")
    @Timed
    public ResponseEntity<Conversation> createConversation(@RequestBody Conversation conversation) throws URISyntaxException {
        log.debug("REST request to save Conversation : {}", conversation);
        if (conversation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("conversation", "idexists", "A new conversation cannot already have an ID")).body(null);
        }
        Conversation result = conversationService.save(conversation);
        return ResponseEntity.created(new URI("/api/conversations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("conversation", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/conversations")
    @Timed
    public ResponseEntity<Conversation> updateConversation(@RequestBody Conversation conversation) throws URISyntaxException {
        log.debug("REST request to update Conversation : {}", conversation);
        if (conversation.getId() == null) {
            return createConversation(conversation);
        }
        Conversation result = conversationService.save(conversation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("conversation", conversation.getId().toString()))
            .body(result);
    }

    @GetMapping("/conversations")
    @Timed
    public ResponseEntity<List<Conversation>> getAllConversations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Conversations");
        Page<Conversation> page = conversationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/conversations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/conversations/{id}")
    @Timed
    public ResponseEntity<Conversation> getConversation(@PathVariable Long id) {
        log.debug("REST request to get Conversation : {}", id);
        Conversation conversation = conversationService.findOne(id);
        return Optional.ofNullable(conversation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/conversations/{id}")
    @Timed
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id) {
        log.debug("REST request to delete Conversation : {}", id);
        conversationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("conversation", id.toString())).build();
    }

    @GetMapping("/_search/conversations")
    @Timed
    public ResponseEntity<List<Conversation>> searchConversations(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Conversations for query {}", query);
        Page<Conversation> page = conversationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/conversations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/conversations/receiver/{receiverId}")
    public ResponseEntity<Conversation> getConversationToReceiver(@PathVariable Long receiverId) {
        Conversation conversation = conversationService.getConversation(receiverId);
        HttpHeaders headers = HeaderUtil.createEntityCreationAlert("conversation", "api/conversations/receiver/:receiverId");
        return new ResponseEntity<>(conversation,headers,HttpStatus.OK);
    }


}
