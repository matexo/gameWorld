package com.gameworld.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "send_time")
    private ZonedDateTime sendTime;

    @Column(name = "is_new")
    private Boolean isNew;

    @ManyToOne
    private GamerProfile authorProfile;

    @ManyToOne
    private Conversation conversation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Message text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getSendTime() {
        return sendTime;
    }

    public Message sendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public void setSendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public Boolean isIsNew() {
        return isNew;
    }

    public Message isNew(Boolean isNew) {
        this.isNew = isNew;
        return this;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public GamerProfile getAuthorProfile() {
        return authorProfile;
    }

    public Message authorProfile(GamerProfile gamerProfile) {
        this.authorProfile = gamerProfile;
        return this;
    }

    public void setAuthorProfile(GamerProfile gamerProfile) {
        this.authorProfile = gamerProfile;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public Message conversation(Conversation conversation) {
        this.conversation = conversation;
        return this;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        if(message.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", sendTime='" + sendTime + "'" +
            ", isNew='" + isNew + "'" +
            '}';
    }
}
