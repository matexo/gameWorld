package com.gameworld.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "has_new")
    private Boolean hasNew;

    @Column(name = "last_update")
    private ZonedDateTime lastUpdate;

    @OneToOne
    @JoinColumn(unique = true)
    private Message lastMessage;

    @OneToMany(mappedBy = "conversation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();

    @ManyToMany(mappedBy = "conversations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GamerProfile> profiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isHasNew() {
        return hasNew;
    }

    public Conversation hasNew(Boolean hasNew) {
        this.hasNew = hasNew;
        return this;
    }

    public void setHasNew(Boolean hasNew) {
        this.hasNew = hasNew;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Conversation lastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public Conversation lastMessage(Message message) {
        this.lastMessage = message;
        return this;
    }

    public void setLastMessage(Message message) {
        this.lastMessage = message;
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
            ", hasNew='" + hasNew + "'" +
            ", lastUpdate='" + lastUpdate + "'" +
            '}';
    }
}
