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
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * 增加站点信息
 * @author mengw
 *
 */
@Entity
@Table(name = "recommand_container")
public class RecommandContainer implements Serializable, Copyable<RecommandContainer> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(RecommandContainer.class);

    // Raw attributes
    private Long containerId; // pk
    private String name; // not null
    private String description;
    private String createBy; // not null
    private Date createDate; // not null
    private String modBy; // not null
    private Date modDate; // not null
    private Integer status; // not null
    private Long picId;
    private Integer siteId;

    private transient Picture pic; // (picId)
    private transient Site site;
    // ---------------------------
    // Constructors
    // ---------------------------

    public RecommandContainer() {
    }

    public RecommandContainer(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getContainerId();
    }

    public void setPrimaryKey(Long containerId) {
        setContainerId(containerId);
    }
    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [containerId] ------------------------

    @Column(name = "container_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    // -- [name] ------------------------

    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // -- [description] ------------------------

    @Length(max = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // -- [createBy] ------------------------

    @Length(max = 60)
    @Column(name = "create_by", nullable = false, length = 60)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    // -- [createDate] ------------------------

    @Column(name = "create_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    // -- [modBy] ------------------------

    @Length(max = 60)
    @Column(name = "mod_by", nullable = false, length = 60)
    public String getModBy() {
        return modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
    }

    // -- [modDate] ------------------------

    @Column(name = "mod_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    // -- [status] ------------------------

    @Column(nullable = false, precision = 10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    // -- [picId] ------------------------
	@Column(name = "pic_id", precision = 20, insertable = false, updatable = false)
    public Long getPicId() {
        return picId;
    }
    public void setPicId(Long picId) {
        this.picId = picId;
    }
 
    // -- [siteId] ------------------------
	@Column(name = "site_id", precision = 10, insertable = false, updatable = false)
    public Integer getSiteId() {
        return siteId;
    }
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: Folder.picId ==> Picture.picId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "pic_id")
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    public Picture getPic() {
        return pic;
    }

    @JoinColumn(name = "site_id")
    @ManyToOne(cascade = PERSIST)
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (site != null) {
            setSiteId(site.getSiteId());
        } else {
            setSiteId(null);
        }
    }

    /**
     * Set the pic without adding this Folder instance on the passed pic
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Picture
     */
    public void setPic(Picture pic) {
        this.pic = pic;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (pic != null) {
            setPicId(pic.getPictureId());
        } else {
            setPicId(null);
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
    public boolean equals(Object recommandContainer) {
        if (this == recommandContainer) {
            return true;
        }

        if (!(recommandContainer instanceof RecommandContainer)) {
            return false;
        }

        RecommandContainer other = (RecommandContainer) recommandContainer;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;

    private Object _getUid() {
        if (_uid == null) {
            if (null != this.getContainerId()) {
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
     * Construct a readable string representation for this RecommandContainer instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("recommandContainer.containerId=[").append(getContainerId()).append("]\n");
        result.append("recommandContainer.name=[").append(getName()).append("]\n");
        result.append("recommandContainer.description=[").append(getDescription()).append("]\n");
        result.append("recommandContainer.createBy=[").append(getCreateBy()).append("]\n");
        result.append("recommandContainer.createDate=[").append(getCreateDate()).append("]\n");
        result.append("recommandContainer.modBy=[").append(getModBy()).append("]\n");
        result.append("recommandContainer.modDate=[").append(getModDate()).append("]\n");
        result.append("recommandContainer.status=[").append(getStatus()).append("]\n");
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
    public RecommandContainer copy() {
        RecommandContainer recommandContainer = new RecommandContainer();
        copyTo(recommandContainer);
        return recommandContainer;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(RecommandContainer recommandContainer) {
        recommandContainer.setContainerId(getContainerId());
        recommandContainer.setName(getName());
        recommandContainer.setDescription(getDescription());
        recommandContainer.setCreateBy(getCreateBy());
        recommandContainer.setCreateDate(getCreateDate());
        recommandContainer.setModBy(getModBy());
        recommandContainer.setModDate(getModDate());
        recommandContainer.setStatus(getStatus());
    }
}