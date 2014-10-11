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
@Table(name = "html_page_bak")
public class HtmlPageBak implements Identifiable<Long>, Serializable, Copyable<HtmlPageBak> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HtmlPageBak.class);

    // Raw attributes
    private Long pageBakId; // pk
    private Long pageId; // not null
    private String name; // not null
    private String path; // not null
    private Long templateId; // not null
    private Long folderId; // not null
    private String meta;
    private String tempMeta;
    private Date publishDate; // not null
    private String publishBy; // not null
    private Boolean ifDefaultPage; // not null
    private Integer siteId;
    private Integer pageType; // not null
    private String createBy; // not null
    private Date createDate; // not null
    private Date modDate; // not null
    private String modBy; // not null
    private String description;

    // ---------------------------
    // Constructors
    // ---------------------------

    public HtmlPageBak() {
    }

    public HtmlPageBak(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getPageBakId();
    }

    public void setPrimaryKey(Long pageBakId) {
        setPageBakId(pageBakId);
    }

    @Transient
    @XmlTransient
    public boolean isPrimaryKeySet() {
        return isPageBakIdSet();
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [pageBakId] ------------------------

    @Column(name = "page_bak_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getPageBakId() {
        return pageBakId;
    }

    public void setPageBakId(Long pageBakId) {
        this.pageBakId = pageBakId;
    }

    @Transient
    public boolean isPageBakIdSet() {
        return pageBakId != null;
    }

    /**
     * Helper method to set the pageBakId attribute via an int.
     * @see #setPageBakId(Long)
     */
    public void setPageBakId(int pageBakId) {
        this.pageBakId = Long.valueOf(pageBakId);
    }

    // -- [pageId] ------------------------

    @NotNull
    @Column(name = "page_id", nullable = false, precision = 20)
    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    /**
     * Helper method to set the pageId attribute via an int.
     * @see #setPageId(Long)
     */
    public void setPageId(int pageId) {
        this.pageId = Long.valueOf(pageId);
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

    // -- [path] ------------------------

    @NotEmpty
    @Length(max = 255)
    @Column(nullable = false)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    // -- [folderId] ------------------------

    @NotNull
    @Column(name = "folder_id", nullable = false, precision = 20)
    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    /**
     * Helper method to set the folderId attribute via an int.
     * @see #setFolderId(Long)
     */
    public void setFolderId(int folderId) {
        this.folderId = Long.valueOf(folderId);
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

    // -- [publishDate] ------------------------

    @NotNull
    @Column(name = "publish_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    // -- [publishBy] ------------------------

    @NotEmpty
    @Length(max = 60)
    @Column(name = "publish_by", nullable = false, length = 60)
    public String getPublishBy() {
        return publishBy;
    }

    public void setPublishBy(String publishBy) {
        this.publishBy = publishBy;
    }

    // -- [ifDefaultPage] ------------------------

    @NotNull
    @Column(name = "if_default_page", nullable = false, length = 0)
    public Boolean getIfDefaultPage() {
        return ifDefaultPage;
    }

    public void setIfDefaultPage(Boolean ifDefaultPage) {
        this.ifDefaultPage = ifDefaultPage;
    }

    // -- [siteId] ------------------------

    @Column(name = "site_id", precision = 10)
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    // -- [pageType] ------------------------

    @NotNull
    @Column(name = "page_type", nullable = false, precision = 10)
    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
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
    public boolean equals(Object htmlPageBak) {
        if (this == htmlPageBak) {
            return true;
        }

        if (!(htmlPageBak instanceof HtmlPageBak)) {
            return false;
        }

        HtmlPageBak other = (HtmlPageBak) htmlPageBak;
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
     * Construct a readable string representation for this HtmlPageBak instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("htmlPageBak.pageBakId=[").append(getPageBakId()).append("]\n");
        result.append("htmlPageBak.pageId=[").append(getPageId()).append("]\n");
        result.append("htmlPageBak.name=[").append(getName()).append("]\n");
        result.append("htmlPageBak.path=[").append(getPath()).append("]\n");
        result.append("htmlPageBak.templateId=[").append(getTemplateId()).append("]\n");
        result.append("htmlPageBak.folderId=[").append(getFolderId()).append("]\n");
        result.append("htmlPageBak.meta=[").append(getMeta()).append("]\n");
        result.append("htmlPageBak.tempMeta=[").append(getTempMeta()).append("]\n");
        result.append("htmlPageBak.publishDate=[").append(getPublishDate()).append("]\n");
        result.append("htmlPageBak.publishBy=[").append(getPublishBy()).append("]\n");
        result.append("htmlPageBak.ifDefaultPage=[").append(getIfDefaultPage()).append("]\n");
        result.append("htmlPageBak.siteId=[").append(getSiteId()).append("]\n");
        result.append("htmlPageBak.pageType=[").append(getPageType()).append("]\n");
        result.append("htmlPageBak.createBy=[").append(getCreateBy()).append("]\n");
        result.append("htmlPageBak.createDate=[").append(getCreateDate()).append("]\n");
        result.append("htmlPageBak.modDate=[").append(getModDate()).append("]\n");
        result.append("htmlPageBak.modBy=[").append(getModBy()).append("]\n");
        result.append("htmlPageBak.description=[").append(getDescription()).append("]\n");
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
    public HtmlPageBak copy() {
        HtmlPageBak htmlPageBak = new HtmlPageBak();
        copyTo(htmlPageBak);
        return htmlPageBak;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(HtmlPageBak htmlPageBak) {
        htmlPageBak.setPageBakId(getPageBakId());
        htmlPageBak.setPageId(getPageId());
        htmlPageBak.setName(getName());
        htmlPageBak.setPath(getPath());
        htmlPageBak.setTemplateId(getTemplateId());
        htmlPageBak.setFolderId(getFolderId());
        htmlPageBak.setMeta(getMeta());
        htmlPageBak.setTempMeta(getTempMeta());
        htmlPageBak.setPublishDate(getPublishDate());
        htmlPageBak.setPublishBy(getPublishBy());
        htmlPageBak.setIfDefaultPage(getIfDefaultPage());
        htmlPageBak.setSiteId(getSiteId());
        htmlPageBak.setPageType(getPageType());
        htmlPageBak.setCreateBy(getCreateBy());
        htmlPageBak.setCreateDate(getCreateDate());
        htmlPageBak.setModDate(getModDate());
        htmlPageBak.setModBy(getModBy());
        htmlPageBak.setDescription(getDescription());
    }
}