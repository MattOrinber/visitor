package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
@Table(name = "category")
public class Category implements Serializable, Copyable<Category> {
	/**
	 * 节点状态:有效
	 */
	public static final int ENABLE = 0;
	/**
	 * 节点状态:无效
	 */
	public static final int DISABLE = 1;
	/**
	 * 产品分类
	 */
	public static final long PRODUCT_CATEGORY = 1L;
	/**
	 * opera版本
	 */
	public static final long OPERA_VERSION = 2L;
	/**
	 * 手机平台
	 */
	public static final long PLATFORM = 3L;
	/**
	 * 手机平台版本，发布分类时使用
	 */
	public static final long PLATFORMVERSION = -3L;
	/**
	 * 手机品牌
	 */
	public static final long BRAND = 4L;
	/**
	 * 分辨率
	 */
	public static final long RESOLUTION = 5L;
	/**
	 * 计费方式
	 */
	public static final long BILLING_TYPE = 6L;
	/**
	 * 合作方式
	 */
	public static final long COOPERATION = 7L;
	/**
	 * 运营方式
	 */
	public static final long OPERATION_MODEL = 8L;
	/**
	 * 标签
	 */
	public static final long TAG = 9L;
	/**
	 * 运营商
	 */
	public static final long OPERATOR = 10L;
	/**
	 * 引进人
	 */
	public static final long IMPORTBY = 11L;
	/**
	 * 产品来源
	 */
	public static final long PRODUCTSOURCE = 12L;
	/**
	 * 广告主CP
	 */
	public static final long MERCHANT = 13L;

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Category.class);

	// Raw attributes
	private Long categoryId; // pk
	private String name; // not null
	private String description;
	private String enName;
	private Integer status = ENABLE; // not null
	private Long nsThread; // not null
	private Long nsLeft; // not null
	private Long nsRight; // not null
	private String createBy; // not null
	private Date createDate; // not null
	private String modBy; // not null
	private Date modDate; // not null
	private Integer sortOrder;//not null
	// Technical attributes for query by example
	private Long parentCategoryId;

	private Float latitude;//纬度，分辨率转换值，无需存数据库
	private Float longitude;//经度，平台版本转换值，无需存数据库，由OS + platform + version/10转换而来
	// Many to one
	private transient Category parentCategory; // (parentCategoryId)
	
	private transient List<Category> children;
	// Many to many
	private transient List<Folder> folders;

	// ---------------------------
	// Constructors
	// ---------------------------

	public Category() {
	}

	public Category(Long primaryKey) {
		this();
		setPrimaryKey(primaryKey);
	}

	// ---------------------------
	// Identifiable implementation
	// ---------------------------

	@Transient
	@XmlTransient
	public Long getPrimaryKey() {
		return getCategoryId();
	}

	public void setPrimaryKey(Long categoryId) {
		setCategoryId(categoryId);
	}

	// -------------------------------
	// Getter & Setter
	// -------------------------------

	// -- [categoryId] ------------------------

	@Column(name = "category_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	// -- [enName] ------------------------

	@Length(max = 200)
	@Column(name = "en_name", length = 200)
	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
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
	
	

	// -- [nsThread] ------------------------

	// @NotNull
	// @Column(name = "ns_thread", nullable = false, precision = 20)
	// public Long getNsThread() {
	// return nsThread;
	// }
	//
	// public void setNsThread(Long nsThread) {
	// this.nsThread = nsThread;
	// }
	//
	// /**
	// * Helper method to set the nsThread attribute via an int.
	// * @see #setNsThread(Long)
	// */
	// public void setNsThread(int nsThread) {
	// this.nsThread = Long.valueOf(nsThread);
	// }
	//
	// // -- [nsLeft] ------------------------
	//
	// @NotNull
	// @Column(name = "ns_left", nullable = false, precision = 20)
	// public Long getNsLeft() {
	// return nsLeft;
	// }
	//
	// public void setNsLeft(Long nsLeft) {
	// this.nsLeft = nsLeft;
	// }
	//
	// /**
	// * Helper method to set the nsLeft attribute via an int.
	// * @see #setNsLeft(Long)
	// */
	// public void setNsLeft(int nsLeft) {
	// this.nsLeft = Long.valueOf(nsLeft);
	// }
	//
	// // -- [nsRight] ------------------------
	//
	// @NotNull
	// @Column(name = "ns_right", nullable = false, precision = 20)
	// public Long getNsRight() {
	// return nsRight;
	// }
	//
	// public void setNsRight(Long nsRight) {
	// this.nsRight = nsRight;
	// }
	//
	// /**
	// * Helper method to set the nsRight attribute via an int.
	// * @see #setNsRight(Long)
	// */
	// public void setNsRight(int nsRight) {
	// this.nsRight = Long.valueOf(nsRight);
	// }

	// -- [createBy] ------------------------

	/**
	 * @return the latitude
	 */
	@Transient
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	@Transient
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	@Length(max = 60)
	@Column(name = "create_by", nullable = true, length = 60)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	// -- [createDate] ------------------------

	@Column(name = "create_date", nullable = true, length = 19)
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
	@Column(name = "mod_by", nullable = true, length = 60)
	public String getModBy() {
		return modBy;
	}

	public void setModBy(String modBy) {
		this.modBy = modBy;
	}

	// -- [modDate] ------------------------

	@Column(name = "mod_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}
	// -- [sortOrder] ------------------------

	/**
	 * @return the sortOrder
	 */
	@Column(name = "sort_order")
	public Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	// -- [parentCategoryId] ------------------------

	@Column(name = "parent_category_id", precision = 20, insertable = false, updatable = false)
	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	// --------------------------------------------------------------------
	// Many to One support
	// --------------------------------------------------------------------

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-one: Category.parentCategoryId ==> Category.categoryId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@JoinColumn(name = "parent_category_id")
	@ManyToOne(cascade = PERSIST, fetch = FetchType.EAGER)
	@JsonIgnore
	public Category getParentCategory() {
		return parentCategory;
	}

	/**
	 * Set the parentCategory without adding this Category instance on the
	 * passed parentCategory If you want to preserve referential integrity we
	 * recommend to use instead the corresponding adder method provided by
	 * Category
	 */
	public void setParentCategory(Category parentCategory) {
		
		//System.out.println("a");
		this.parentCategory = parentCategory;
		//System.out.println("b");

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (parentCategory != null) {
			setParentCategoryId(parentCategory.getCategoryId());
		} else {
			setParentCategoryId(null);
		}
        if(null != this.parentCategory) {
        	this.parentCategory.addChild(this);
        }
	}

	private void addChild(Category category) {
    	if(null == this.children) {
    		this.children = new ArrayList<Category>();
    	}
    	this.children.add(category);		
	}

	// -----------------------------------------
	// Set defaults values
	// -----------------------------------------

	/**
	 * Set the default values.
	 */
	public void initDefaultValues() {
		this.setCreateDate(new Date());
		this.setModDate(this.getCreateDate());
		this.setStatus(0);
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
	public boolean equals(Object category) {
		if (this == category) {
			return true;
		}

		if (!(category instanceof Category)) {
			return false;
		}

		Category other = (Category) category;
		return _getUid().equals(other._getUid());
	}

	@Override
	public int hashCode() {
		return _getUid().hashCode();
	}

	private Object _uid;

	private Object _getUid() {
		if (_uid == null) {
			if (null != this.getCategoryId()) {
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
	 * @return the children
	 */
	@Transient
	public List<Category> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Category> children) {
		this.children = children;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", name=" + name
				+ ", description=" + description + ", enName=" + enName
				+ ", status=" + status + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", modBy=" + modBy
				+ ", modDate=" + modDate + ", sortOrder=" + sortOrder
				+ ", parentCategoryId=" + parentCategoryId + ", latitude="
				+ latitude + ", longitude=" + longitude + ", parentCategory="
				+ parentCategory + ", folders=" + folders + "]";
	}

	// -----------------------------------------
	// Copyable Implementation
	// (Support for REST web layer)
	// -----------------------------------------

	/**
	 * Return a copy of the current object
	 */
	@Override
	public Category copy() {
		Category category = new Category();
		copyTo(category);
		return category;
	}

	/**
	 * Copy the current properties to the given object
	 */
	@Override
	public void copyTo(Category category) {
		category.setCategoryId(getCategoryId());
		category.setName(getName());
		category.setDescription(getDescription());
		category.setEnName(getEnName());
		category.setStatus(getStatus());
//		category.setNodeInfo(getNodeInfo());
		category.setNsThread(getNsThread());
		category.setNsLeft(getNsLeft());
		category.setNsRight(getNsRight());
		category.setCreateBy(getCreateBy());
		category.setCreateDate(getCreateDate());
		category.setModBy(getModBy());
		category.setModDate(getModDate());
		category.setParentCategoryId(getParentCategoryId());
		if (getParentCategory() != null) {
			category.setParentCategory(getParentCategory());
		}
	}

	
	/**
	 * @return the nsThread
	 */
	@Column(name = "ns_thread")
	public Long getNsThread() {
		return nsThread;
	}

	/**
	 * @param nsThread the nsThread to set
	 */
	public void setNsThread(Long nsThread) {
		this.nsThread = nsThread;
	}

	/**
	 * @return the nsLeft
	 */
	@Column(name = "ns_left")
	public Long getNsLeft() {
		return nsLeft;
	}

	/**
	 * @param nsLeft the nsLeft to set
	 */
	public void setNsLeft(Long nsLeft) {
		this.nsLeft = nsLeft;
	}

	/**
	 * @return the nsRight
	 */
	@Column(name = "ns_right")
	public Long getNsRight() {
		return nsRight;
	}

	/**
	 * @param nsRight the nsRight to set
	 */
	public void setNsRight(Long nsRight) {
		this.nsRight = nsRight;
	}

	@Transient
	@JsonIgnore
	public Long getId() {
		return this.getCategoryId();
	}

    @ManyToMany(mappedBy="tags", fetch=FetchType.LAZY)
    @JsonIgnore
	public List<Folder> getFolders() {
		return folders;
	}

	/**
	 * @param folders the folders to set
	 */
	public void setFolders(List<Folder> folders) {
		this.folders = folders;
	}
    
}