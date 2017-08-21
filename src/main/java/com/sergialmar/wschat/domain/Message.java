package com.sergialmar.wschat.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by mac on 8/8/17.
 */
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @SequenceGenerator(name = "messages_id_seq",
            sequenceName = "messages_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "messages_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_on", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    private ZonedDateTime createdOn;

    @Column(name = "text")
    private String text;

    @Column(name = "chat_user")
    private Long userId;

    @Column(name = "offer")
    private Long offerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
