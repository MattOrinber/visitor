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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "template_bak")
public class TemplateBak implements Identifiable<Long>, Serializable, Copyable<TemplateBak> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TemplateBak.class);

    // Raw attributes
    private Long templateBakId; // pk
    private Long templateId; // not null
    private Integer type; // not null
    private String name; // not null
    private String meta;
    private String tempMeta;
    private String createBy; // not null
    private Date createDate; // not null
    private Date modDate; // not null
    private String modBy; // not null
    private Date publishDate;
    private String publishBy;
    private String description;

    // ---------------------------
    // Constructors
    // ---------------------------

    public TemplateBak() {
    }

    public TemplateBak(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getTemplateBakId();
    }

    public void setPrimaryKey(Long templateBakId) {
        setTemplateBakId(templateBakId);
    }

    @Transient
    @XmlTransient
    public boolean isPrimaryKeySet() {
        return isTemplateBakIdSet();
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [templateBakId] ------------------------

    @Column(name = "template_bak_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getTemplateBakId() {
        return templateBakId;
    }

    public void setTemplateBakId(Long templateBakId) {
        this.templateBakId = templateBakId;
    }

    @Transient
    public boolean isTemplateBakIdSet() {
        return templateBakId != null;
    }

    /**
     * Helper method to set the templateBakId attribute via an int.
     * @see #setTemplateBakId(Long)
     */
    public void setTemplateBakId(int templateBakId) {
        this.templateBakId = Long.valueOf(templateBakId);
    }

    // -- [templateId] ------------------------

    @NotNull
    @Column(name = "template_id", nullable = false, precision = 20)
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    /**
     * Helper method to set the templateId attribute via an int.
     * @see #setTemplateId(Long)
     */
    public void setTemplateId(int templateId) {
        this.templateId = Long.valueOf(templateId);
    }

    // -- [type] ------------------------

    @NotNull
    @Column(nullable = false, precision = 10)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    // -- [meta] ------------------------

    @Length(max = 65535)
    @Column(length = 65535)
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    // -- [tempMeta] ------------------------

    @Length(max = 65535)
    @Column(name = "temp_meta", length = 65535)
    public String getTempMeta() {
        return tempMeta;
    }

    public void setTempMeta(String tempMeta) {
        this.tempMeta = tempMeta;
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

    // -- [modDate] ------------------------

    @NotNull
    @Column(name = "mod_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    // -- [modBy] ------------------------

    @NotEmpty
    @Length(max = 60)
    @Column(name = "mod_by", nullable = false, length = 60)
    public String getModBy() {
        return modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
    }

    // -- [publishDate] ------------------------

    @Column(name = "publish_date", length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    // -- [publishBy] ------------------------

    @Length(max = 60)
    @Column(name = "publish_by", length = 60)
    public String getPublishBy() {
        return publishBy;
    }

    public void setPublishBy(String publishBy) {
        this.publishBy = publishBy;
    }

    // -- [description] ------------------------

    @Length(max = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public boolean equals(Object templateBak) {
        if (this == templateBak) {
            return true;
        }

        if (!(templateBak instanceof TemplateBak)) {
            return false;
        }

        TemplateBak other = (TemplateBak) templateBak;
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
     * Construct a readable string representation for this TemplateBak instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("templateBak.templateBakId=[").append(getTemplateBakId()).append("]\n");
        result.append("templateBak.templateId=[").append(getTemplateId()).append("]\n");
        result.append("templateBak.type=[").append(getType()).append("]\n");
        result.append("templateBak.name=[").append(getName()).append("]\n");
        result.append("templateBak.meta=[").append(getMeta()).append("]\n");
        result.append("templateBak.tempMeta=[").append(getTempMeta()).append("]\n");
        result.append("templateBak.createBy=[").append(getCreateBy()).append("]\n");
        result.append("templateBak.createDate=[").append(getCreateDate()).append("]\n");
        result.append("templateBak.modDate=[").append(getModDate()).append("]\n");
        result.append("templateBak.modBy=[").append(getModBy()).append("]\n");
        result.append("templateBak.publishDate=[").append(getPublishDate()).append("]\n");
        result.append("templateBak.publishBy=[").append(getPublishBy()).append("]\n");
        result.append("templateBak.description=[").append(getDescription()).append("]\n");
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
    public TemplateBak copy() {
        TemplateBak templateBak = new TemplateBak();
        copyTo(templateBak);
        return templateBak;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(TemplateBak templateBak) {
        templateBak.setTemplateBakId(getTemplateBakId());
        templateBak.setTemplateId(getTemplateId());
        templateBak.setType(getType());
        templateBak.setName(getName());
        templateBak.setMeta(getMeta());
        templateBak.setTempMeta(getTempMeta());
        templateBak.setCreateBy(getCreateBy());
        templateBak.setCreateDate(getCreateDate());
        templateBak.setModDate(getModDate());
        templateBak.setModBy(getModBy());
        templateBak.setPublishDate(getPublishDate());
        templateBak.setPublishBy(getPublishBy());
        templateBak.setDescription(getDescription());
    }
}