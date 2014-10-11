package org.visitor.appportal.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "message_content")
public class MessageContent implements Serializable, Copyable<MessageContent> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(MessageContent.class);

    // Raw attributes
    private Long messageId; // pk
    private String qq;
    private String nickName;
    private String mobileName;
    private String mobileNumber;
    private String content; // not null
    private Date createDate; // not null
    private Integer status; // not null
    private Date replyDate;
    private String replyBy;
    private Integer messageType; // not null
    private String sourceMessageId;
    private Float score;
    private Long productId;
    private String sourceProductId;
    private Date importDate;
    // ---------------------------
    // Constructors
    // ---------------------------

    public MessageContent() {
    }

    public MessageContent(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    @JsonIgnore
    public Long getPrimaryKey() {
        return getMessageId();
    }

    public void setPrimaryKey(Long messageId) {
        setMessageId(messageId);
    }
    
    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [messageId] ------------------------

    @Column(name = "message_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    // -- [qq] ------------------------

    @Length(max = 20)
    @Column(length = 20)
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    // -- [nickName] ------------------------

    @Length(max = 120)
    @Column(name = "nick_name", length = 120)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    // -- [mobileName] ------------------------

    @Length(max = 200)
    @Column(name = "mobile_name", length = 200)
    public String getMobileName() {
        return mobileName;
    }

    public void setMobileName(String mobileName) {
        this.mobileName = mobileName;
    }

    // -- [mobileNumber] ------------------------

    @Length(max = 16)
    @Column(name = "mobile_number", length = 16)
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    // -- [content] ------------------------

    @NotEmpty
    @Length(max = 512)
    @Column(nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    // -- [status] ------------------------

    @NotNull
    @Column(nullable = false, precision = 10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // -- [replyDate] ------------------------

    @Column(name = "reply_date", length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    // -- [replyBy] ------------------------

    @Length(max = 60)
    @Column(name = "reply_by", length = 60)
    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }

    // -- [messageType] ------------------------

    @NotNull
    @Column(name = "message_type", nullable = false, precision = 10)
    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    /**
	 * @return the sourceMessageId
	 */
    @Length(max = 60)
    @Column(name = "source_message_Id", length = 60)
	public String getSourceMessageId() {
		return sourceMessageId;
	}

	/**
	 * @param sourceMessageId the sourceMessageId to set
	 */
	public void setSourceMessageId(String sourceMessageId) {
		this.sourceMessageId = sourceMessageId;
	}

	/**
	 * @return the score
	 */
    @Digits(integer = 3, fraction = 1)
    @Column(precision = 3, scale = 1)
	public Float getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(Float score) {
		this.score = score;
	}

	/**
	 * @return the productId
	 */
	@Column(name = "product_id", precision = 20)
	public Long getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * @return the sourceProductId
	 */
    @Length(max = 60)
    @Column(name = "source_product_Id", length = 60)
	public String getSourceProductId() {
		return sourceProductId;
	}

	/**
	 * @param sourceProductId the sourceProductId to set
	 */
	public void setSourceProductId(String sourceProductId) {
		this.sourceProductId = sourceProductId;
	}

	/**
	 * @return the importDate
	 */
    @Column(name = "import_date", length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)	
	public Date getImportDate() {
		return importDate;
	}

	/**
	 * @param importDate the importDate to set
	 */
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
    // -----------------------------------------
    // Set defaults values
    // -----------------------------------------

	/**
     * Set the default values.
     */
    public void initDefaultValues() {
    	this.setMessageType(0);//默认为评论
    	this.setCreateDate(new Date());
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
    public boolean equals(Object messageContent) {
        if (this == messageContent) {
            return true;
        }

        if (!(messageContent instanceof MessageContent)) {
            return false;
        }

        MessageContent other = (MessageContent) messageContent;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;

    private Object _getUid() {
        if (_uid == null) {
            if (null != this.getPrimaryKey()) {
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
     * Construct a readable string representation for this MessageContent instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("messageContent.messageId=[").append(getMessageId()).append("]\n");
        result.append("messageContent.qq=[").append(getQq()).append("]\n");
        result.append("messageContent.nickName=[").append(getNickName()).append("]\n");
        result.append("messageContent.mobileName=[").append(getMobileName()).append("]\n");
        result.append("messageContent.mobileNumber=[").append(getMobileNumber()).append("]\n");
        result.append("messageContent.content=[").append(getContent()).append("]\n");
        result.append("messageContent.createDate=[").append(getCreateDate()).append("]\n");
        result.append("messageContent.status=[").append(getStatus()).append("]\n");
        result.append("messageContent.replyDate=[").append(getReplyDate()).append("]\n");
        result.append("messageContent.replyBy=[").append(getReplyBy()).append("]\n");
        result.append("messageContent.messageType=[").append(getMessageType()).append("]\n");
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
    public MessageContent copy() {
        MessageContent messageContent = new MessageContent();
        copyTo(messageContent);
        return messageContent;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(MessageContent messageContent) {
        messageContent.setMessageId(getMessageId());
        messageContent.setQq(getQq());
        messageContent.setNickName(getNickName());
        messageContent.setMobileName(getMobileName());
        messageContent.setMobileNumber(getMobileNumber());
        messageContent.setContent(getContent());
        messageContent.setCreateDate(getCreateDate());
        messageContent.setStatus(getStatus());
        messageContent.setReplyDate(getReplyDate());
        messageContent.setReplyBy(getReplyBy());
        messageContent.setMessageType(getMessageType());
    }
    
    public enum MessageTypeEnum {
    	Comment(0), Reply(1);
       	private Integer value;
    	private MessageTypeEnum(Integer value) {
    		this.value = value;
    	}
		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}
    	
		public static MessageTypeEnum getInstance(Integer value) {
			if(null == value) {
				return null;
			}
			MessageTypeEnum[] values = MessageTypeEnum.values();
			for(MessageTypeEnum v : values) {
				if(v.getValue().intValue() == value.intValue()) {
					return v;
				}
			}
			return null;
		}
    }
    
    public enum StatusEnum {
    	Enable(0), Disabled(1);
    	private Integer value;
    	private StatusEnum(Integer value) {
    		this.value = value;
    	}
		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}
    	
		public static StatusEnum getInstance(Integer value) {
			if(null == value) {
				return null;
			}
			StatusEnum[] values = StatusEnum.values();
			for(StatusEnum v : values) {
				if(v.getValue().intValue() == value.intValue()) {
					return v;
				}
			}
			return null;
		}
    }
}