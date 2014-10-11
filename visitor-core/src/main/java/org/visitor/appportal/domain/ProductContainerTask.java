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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "product_container_task")
public class ProductContainerTask implements Serializable, Copyable<ProductContainerTask> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ProductContainerTask.class);
	public static final Integer ENABLE = 0;
	public static final Integer DISABLE = 1;

	// Raw attributes
	private Long productContainerTaskId; // pk

	private Long containerId;       // not null
	private Long productId;         // not null
	private Long advertiseId;       /*广告ID*/
	private Long tfolderId;         /*频道类型*/
	
	private Integer displayDate;
	private Integer status = ENABLE;// not null
	private Long sortOrder;
	private Integer type;
	
	private String createBy;        // not null
	private Date createDate;        // not null
	private String modBy;           // not null
	private Date modDate;           // not null

	// Many to one
	private RecommandContainer container; // not null (containerId)
	private ProductList product;    // not null (productId)
	private transient Advertise advertise;
	private transient Folder tfolder;
	// ---------------------------
	// Constructors
	// ---------------------------

	public ProductContainerTask() {
	}

	public ProductContainerTask(Long primaryKey) {
		this();
		setPrimaryKey(primaryKey);
	}

	// ---------------------------
	// Identifiable implementation
	// ---------------------------

	@Transient
	@XmlTransient
	public Long getPrimaryKey() {
		return getProductContainerTaskId();
	}

	public void setPrimaryKey(Long productContainerTaskId) {
		setProductContainerTaskId(productContainerTaskId);
	}

	// -------------------------------
	// Getter & Setter
	// -------------------------------

	// -- [productContainerTaskId] ------------------------
	@Column(name = "product_container_task_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getProductContainerTaskId() {
		return productContainerTaskId;
	}

	public void setProductContainerTaskId(Long productContainerTaskId) {
		this.productContainerTaskId = productContainerTaskId;
	}

	// -- [containerId] ------------------------
	@NotNull
	@Column(name = "container_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	// -- [productId] ------------------------
	@Column(name = "product_id", precision = 20, insertable = false, updatable = false)
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	// -- [sortOrder] ------------------------
	@Column(name = "sort_order")
	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
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

	// -- [advertiseId] ------------------------
	@Column(name = "advertise_id", precision = 20, insertable = false, updatable = false)
	public Long getAdvertiseId() {
		return advertiseId;
	}

	public void setAdvertiseId(Long advertiseId) {
		this.advertiseId = advertiseId;
	}

	// -- [tfolderId] ------------------------
	@Column(name = "t_folder_id", precision = 20, insertable = false, updatable = false)
	public Long getTfolderId() {
		return tfolderId;
	}

	public void setTfolderId(Long tfolderId) {
		this.tfolderId = tfolderId;
	}

	// -- [displayDate] ------------------------
	@Column(name = "display_date", precision = 8)
	public Integer getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(Integer displayDate) {
		this.displayDate = displayDate;
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

	// --------------------------------------------------------------------
	// Many to One support
	// --------------------------------------------------------------------

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-one: ProductContainer.productId ==> ProductList.productId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@JoinColumn(name = "product_id")
	@ManyToOne(fetch = LAZY)
	@JsonIgnore
	public ProductList getProduct() {
		return product;
	}

	/**
	 * Set the product without adding this ProductContainer instance on the
	 * passed product If you want to preserve referential integrity we recommend
	 * to use instead the corresponding adder method provided by ProductList
	 */
	public void setProduct(ProductList product) {
		this.product = product;

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (product != null) {
			setProductId(product.getProductId());
		} else {
			setProductId(null);
		}
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-one: ProductContainer.containerId ==>
	// RecommandContainer.containerId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@JoinColumn(name = "container_id", nullable = false)
	@ManyToOne(fetch = LAZY)
	@JsonIgnore
	public RecommandContainer getContainer() {
		return container;
	}

	/**
	 * Set the container without adding this ProductContainer instance on the
	 * passed container If you want to preserve referential integrity we
	 * recommend to use instead the corresponding adder method provided by
	 * RecommandContainer
	 */
	public void setContainer(RecommandContainer container) {
		this.container = container;

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (container != null) {
			setContainerId(container.getContainerId());
		} else {
			setContainerId(null);
		}
	}

	@JoinColumn(name = "advertise_id")
	@ManyToOne(fetch = LAZY)
	@JsonIgnore
	public Advertise getAdvertise() {
		return advertise;
	}

	public void setAdvertise(Advertise advertise) {
		this.advertise = advertise;
		
		if(advertise != null){
			this.setAdvertiseId(advertise.getAdvertiseId());
		}else {
			this.setAdvertiseId(null);
		}
	}

	@JoinColumn(name = "t_folder_id")
	@ManyToOne(fetch = LAZY)
	@JsonIgnore
	public Folder getTfolder() {
		return tfolder;
	}

	public void setTfolder(Folder tfolder) {
		this.tfolder = tfolder;
		
		if(tfolder != null){
			this.setTfolderId(tfolder.getFolderId());
		}else {
			this.setTfolderId(null);
		}
	}
	//以上为新增加的三个字段
	
	
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
	public boolean equals(Object productContainer) {
		if (this == productContainer) {
			return true;
		}

		if (!(productContainer instanceof ProductContainerTask)) {
			return false;
		}

		ProductContainerTask other = (ProductContainerTask) productContainer;
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
	 * Construct a readable string representation for this ProductContainer
	 * instance.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("productContainer.productContainerTaskId=[").append(getProductContainerTaskId()).append("]\n");
		result.append("productContainer.containerId=[").append(getContainerId()).append("]\n");
		result.append("productContainer.productId=[").append(getProductId()).append("]\n");
		result.append("productContainer.sortOrder=[").append(getSortOrder()).append("]\n");
		result.append("productContainer.displayDate=[").append(getDisplayDate()).append("]\n");
		result.append("productContainer.status=[").append(getStatus()).append("]\n");
		result.append("productContainer.createBy=[").append(getCreateBy()).append("]\n");
		result.append("productContainer.createDate=[").append(getCreateDate()).append("]\n");
		result.append("productContainer.modBy=[").append(getModBy()).append("]\n");
		result.append("productContainer.modDate=[").append(getModDate()).append("]\n");
		result.append("productContainer.type=[").append(this.getType()).append("]\n");
		result.append("productContainer.advertiseId=[").append(this.getAdvertiseId()).append("]\n");
		result.append("productContainer.tfolderId=[").append(this.getTfolderId()).append("]\n");

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
	public ProductContainerTask copy() {
		ProductContainerTask productContainer = new ProductContainerTask();
		copyTo(productContainer);
		return productContainer;
	}

	/**
	 * Copy the current properties to the given object
	 */
	@Override
	public void copyTo(ProductContainerTask productContainer) {
		productContainer.setProductContainerTaskId(getProductContainerTaskId());
		productContainer.setDisplayDate(getDisplayDate());
		productContainer.setSortOrder(getSortOrder());
		productContainer.setStatus(getStatus());
		productContainer.setCreateBy(getCreateBy());
		productContainer.setCreateDate(getCreateDate());
		productContainer.setModBy(getModBy());
		productContainer.setModDate(getModDate());
		productContainer.setType(getType());

		if (getProduct() != null) {
			productContainer.setProduct(new ProductList(getProduct().getPrimaryKey()));
		}

		if (getContainer() != null) {
			productContainer.setContainer(new RecommandContainer(getContainer().getPrimaryKey()));
		}
		
		if(getAdvertise() !=null){
			productContainer.setAdvertise(new Advertise(getAdvertise().getPrimaryKey()));
		}
		
		if(getTfolder()!=null){
			productContainer.setTfolder(new Folder(getTfolder().getPrimaryKey()));
		}
		
	}
	
	//容器存储的类型，枚举
	public enum TypeEnum {
		Product(1), Folder(2),Advertise(3);
		private Integer value;
		private String displayName;

		private TypeEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 1:
				displayName = "产品";
				break;
			case 2:
				displayName = "频道";
				break;
			case 3:
				displayName = "广告";
				break;
			}
		}

		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}

		/**
		 * @return the displayName
		 */
		public String getDisplayName() {
			return displayName;
		}

		public static TypeEnum getInstance(Integer value) {
			if (null != value) {
				TypeEnum[] enums = TypeEnum.values();
				for (TypeEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
}