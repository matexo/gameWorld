package com.gameworld.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Conversation.
 */
@Entity
@Table(name = "conversation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "conversation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "conversation" , fetch = FetchType.LAZY)
    @JsonManagedReference
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();

    @ManyToMany(mappedBy = "conversations")
    @JsonManagedReference
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GamerProfile> profiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public Conversation messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public Conversation addMessages(Message message) {
        messages.add(message);
        message.setConversation(this);
        return this;
    }

    public Conversation removeMessages(Message message) {
        messages.remove(message);
        message.setConversation(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<GamerProfile> getProfiles() {
        return profiles;
    }

    public Conversation profiles(Set<GamerProfile> gamerProfiles) {
        this.profiles = gamerProfiles;
        return this;
    }

    public Conversation addProfiles(GamerProfile gamerProfile) {
        profiles.add(gamerProfile);
        gamerProfile.getConversations().add(this);
        return this;
    }

    public Conversation removeProfiles(GamerProfile gamerProfile) {
        profiles.remove(gamerProfile);
        gamerProfile.getConversations().remove(this);
        return this;
    }

    public void setProfiles(Set<GamerProfile> gamerProfiles) {
        this.profiles = gamerProfiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conversation conversation = (Conversation) o;
        if(conversation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, conversation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Conversation{" +
            "id=" + id +
            '}';
    }
}
