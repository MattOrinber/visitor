package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "message_reply")
public class MessageReply implements Identifiable<Long>, Serializable, Copyable<MessageReply> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(MessageReply.class);

    // Raw attributes
    private Long replyId; // pk
    private Integer num; // not null
    private Date createDate; // not null
    private String createBy; // not null
    private Integer status; // not null
    private String content; // not null

    // Technical attributes for query by example
    private Long messageId; // not null

    // Many to one
    private MessageContent message; // not null (messageId)

    // ---------------------------
    // Constructors
    // ---------------------------

    public MessageReply() {
    }

    public MessageReply(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getReplyId();
    }

    public void setPrimaryKey(Long replyId) {
        setReplyId(replyId);
    }

    @Transient
    @XmlTransient
    public boolean isPrimaryKeySet() {
        return isReplyIdSet();
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [replyId] ------------------------

    @Column(name = "reply_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    @Transient
    public boolean isReplyIdSet() {
        return replyId != null;
    }

    /**
     * Helper method to set the replyId attribute via an int.
     * @see #setReplyId(Long)
     */
    public void setReplyId(int replyId) {
        this.replyId = Long.valueOf(replyId);
    }

    // -- [messageId] ------------------------

    @Column(name = "message_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getMessageId() {
        return messageId;
    }

    private void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    // -- [num] ------------------------

    @NotNull
    @Column(nullable = false, precision = 10)
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    // -- [createDate] ------------------------

    @NotNull
    @Column(name = "create_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    // -- [createBy] ------------------------

    @NotEmpty
    @Length(max = 60)
    @Column(name = "create_by", nullable = false, length = 60)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    // -- [status] ------------------------

    @NotNull
    @Column(nullable = false, precision = 10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // -- [content] ------------------------

    @NotEmpty
    @Length(max = 255)
    @Column(nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // --------------------------------------------------------------------
    // Many to One support
    // --------------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: MessageReply.messageId ==> MessageContent.messageId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @NotNull
    @JoinColumn(name = "message_id", nullable = false)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    public MessageContent getMessage() {
        return message;
    }

    /**
     * Set the message without adding this MessageReply instance on the passed message
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by MessageContent
     */
    public void setMessage(MessageContent message) {
        this.message = message;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (message != null) {
            setMessageId(message.getMessageId());
        } else {
            setMessageId(null);
        }
    }

    // -----------------------------------------
    // Set defaults values
    // -----------------------------------------

    /**
     * Set the default values.
     */
    public void initDefaultValues() {
    }

    // -----------------------------------------
    // equals and hashCode
    // -----------------------------------------

    // The first time equals or hashCode is called,
    // we check if the primary key is present or not.
    // If yes: we use it in equals/hashCode
    // If no: we use a VMID during the entire life of this
    // instance even if later on this instance is assigned
    // a primary key.

    @Override
    public boolean equals(Object messageReply) {
        if (this == messageReply) {
            return true;
        }

        if (!(messageReply instanceof MessageReply)) {
            return false;
        }

        MessageReply other = (MessageReply) messageReply;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;

    private Object _getUid() {
        if (_uid == null) {
            if (isPrimaryKeySet()) {
                _uid = getPrimaryKey();
            } else {
                _uid = new java.rmi.dgc.VMID();
                logger
                        .warn("DEVELOPER: hashCode has changed!."
                                + "If you encounter this message you should take the time to carefuly review equals/hashCode for: "
                                + getClass().getCanonicalName());
            }
        }
        return _uid;
    }

    // -----------------------------------------
    // toString
    // -----------------------------------------

    /**
     * Construct a readable string representation for this MessageReply instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("messageReply.replyId=[").append(getReplyId()).append("]\n");
        result.append("messageReply.messageId=[").append(getMessageId()).append("]\n");
        result.append("messageReply.num=[").append(getNum()).append("]\n");
        result.append("messageReply.createDate=[").append(getCreateDate()).append("]\n");
        result.append("messageReply.createBy=[").append(getCreateBy()).append("]\n");
        result.append("messageReply.status=[").append(getStatus()).append("]\n");
        result.append("messageReply.content=[").append(getContent()).append("]\n");
        return result.toString();
    }

    // -----------------------------------------
    // Copyable Implementation
    // (Support for REST web layer)
    // -----------------------------------------

    /**
     * Return a copy of the current object
     */
    @Override
    public MessageReply copy() {
        MessageReply messageReply = new MessageReply();
        copyTo(messageReply);
        return messageReply;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(MessageReply messageReply) {
        messageReply.setReplyId(getReplyId());
        //messageReply.setMessageId(getMessageId());
        messageReply.setNum(getNum());
        messageReply.setCreateDate(getCreateDate());
        messageReply.setCreateBy(getCreateBy());
        messageReply.setStatus(getStatus());
        messageReply.setContent(getContent());
        if (getMessage() != null) {
            messageReply.setMessage(new MessageContent(getMessage().getPrimaryKey()));
        }
    }
}