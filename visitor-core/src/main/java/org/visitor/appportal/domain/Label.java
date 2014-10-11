package org.visitor.appportal.domain;

import org.visitor.app.portal.model.Product;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;
import javax.validation.constraints.NotNull;
/**
 * Created by liuxb on 14-8-29.
 */

@Entity
@Table(name = "labels")
public class Label implements Serializable, Copyable<Label>{

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Label.class);
    /** enable */
    public static final int ENABLE = 0;
    /** disable */
    public static final int DISABLE = 1;

    // Raw attributes
    private Integer labelId; //
    private String name; // not null
    private String createBy;//VARCHAR(60) NOT NULL COMMENT '创建人',
    private Date createDate;// DATETIME NOT NULL COMMENT '创建日期',
    private Integer siteId;
    private String modifyBy;//VARCHAR(60) NOT NULL COMMENT '创建人',
    private Date modifyDate;// DATETIME NOT NULL COMMENT '创建日期',
    private Integer status;

    @Column(name = "status", nullable = false, precision = 20)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private transient List<ProductList> products;
    // ---------------------------
    // Constructors
    // ---------------------------

    public Label() {
    }
    public Label(int primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public Integer getPrimaryKey() {
        return getLabelId();
    }

    public void setPrimaryKey(int labelId) {
        setLabelId(labelId);
    }

    @Column(name = "site_id", nullable = false, precision = 20)
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    @Column(name = "label_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    @NotBlank
    @Length(max = 200)
    @Column(name = "label_name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(max = 200)
    @Column(name = "create_by", nullable = false, length = 60)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "create_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Length(max = 200)
    @Column(name = "modify_by", nullable = false, length = 60)
    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    @Column(name = "modify_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @ManyToMany(mappedBy="labels", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    public List<ProductList> getProducts() {
        return products;
    }

    public void setProducts(List<ProductList> products) {
        this.products = products;
    }

    @Override
    public Label copy() {
        Label label = new Label();
        copyTo(label);
        return label;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(Label label) {
        label.setLabelId(getLabelId());
        label.setName(getName());
        label.setCreateBy(getCreateBy());
        label.setCreateDate(getCreateDate());
        label.setProducts(getProducts());
        label.setSiteId(getSiteId());

    }
    @Override
    public boolean equals(Object label) {
        if (this == label) {
            return true;
        }

        if (!(label instanceof Label)) {
            return false;
        }

        Label other = (Label) label;
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

}
