package org.visitor.appportal.domain;

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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "product_operation")
public class ProductOperation implements Identifiable<Long>, Serializable, Copyable<ProductOperation> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductOperation.class);

    // Raw attributes
    private Long operationId; // pk
    private Long productId; // not null
    private Integer type; // not null
    private String content; // not null
    private String createBy; // not null
    private Date createDate; // not null

    private ProductList product; // not null (productId)
    // ---------------------------
    // Constructors
    // ---------------------------

    public ProductOperation() {
    }

    public ProductOperation(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getOperationId();
    }

    public void setPrimaryKey(Long operationId) {
        setOperationId(operationId);
    }

    @Transient
    @XmlTransient
    public boolean isPrimaryKeySet() {
        return isOperationIdSet();
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [operationId] ------------------------

    @Column(name = "operation_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    @Transient
    public boolean isOperationIdSet() {
        return operationId != null;
    }

    // -- [productId] ------------------------

    @NotNull
    @Column(name = "product_id", nullable = false, precision = 20)
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    // -- [content] ------------------------

    @NotEmpty
    @Length(max = 65535)
    @Column(nullable = false, length = 65535)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductPic.productId ==> ProductList.productId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    public ProductList getProduct() {
        return product;
    }

    /**
     * Set the product without adding this ProductPic instance on the passed product
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by ProductList
     */
    public void setProduct(ProductList product) {
        this.product = product;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (product != null) {
            setProductId(product.getProductId());
        } else {
            setProductId(null);
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
    public boolean equals(Object productOperation) {
        if (this == productOperation) {
            return true;
        }

        if (!(productOperation instanceof ProductOperation)) {
            return false;
        }

        ProductOperation other = (ProductOperation) productOperation;
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
     * Construct a readable string representation for this ProductOperation instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("productOperation.operationId=[").append(getOperationId()).append("]\n");
        result.append("productOperation.productId=[").append(getProductId()).append("]\n");
        result.append("productOperation.type=[").append(getType()).append("]\n");
        result.append("productOperation.content=[").append(getContent()).append("]\n");
        result.append("productOperation.createBy=[").append(getCreateBy()).append("]\n");
        result.append("productOperation.createDate=[").append(getCreateDate()).append("]\n");
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
    public ProductOperation copy() {
        ProductOperation productOperation = new ProductOperation();
        copyTo(productOperation);
        return productOperation;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(ProductOperation productOperation) {
        productOperation.setOperationId(getOperationId());
        productOperation.setProductId(getProductId());
        productOperation.setType(getType());
        productOperation.setContent(getContent());
        productOperation.setCreateBy(getCreateBy());
        productOperation.setCreateDate(getCreateDate());
    }
    
    /**
     * 	 * '操作类型。0-修改信息；2-绑定频道；3-上传文件；4-换包；5-修改适配机型；
	 * 6-上传截图；7-修改封面图；8-修改图标；
	 * 9-删除截图；10-设置截图为封面图',
     * @author mengw
     *
     */
    public enum OperationTypeEnum {
    	Create, ChangeInfo, BindFolder, UploadFile, ChangeFile, DeleteFile, 
    	ChangeSuite, UploadIllu, ChangeCover, ChangeIcon, DelteIllu, SetCover, OnlineFile;

		public static OperationTypeEnum getInstance(Integer type) {
			OperationTypeEnum[] values = OperationTypeEnum.values();
			for(OperationTypeEnum value : values) {
				if(value.ordinal() == type.intValue()) {
					return value;
				}
			}
			return null;
		}
    }
}