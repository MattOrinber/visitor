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
@Table(name = "product_container")
public class ProductContainer implements Serializable, Copyable<ProductContainer> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ProductContainer.class);
	public static final Integer ENABLE = 0;
	public static final Integer DISABLE = 1;

	// Raw attributes
	private Long productContainerId; // pk
	private Long sortOrder; // not null
	
	private Date startDate;
	private Date endDate;
	private Integer status = ENABLE; // not null
	private Long click = 0L; // not null
	private String createBy; // not null
	private Date createDate; // not null
	private String modBy; // not null
	private Date modDate; // not null
	private Long picId;

	// Technical attributes for query by example
	private Long containerId; // not null
	private Long folderId; // not null
	private Long pageId; // not null
	private Long productId; // not null
	private Integer siteId; // not null
	
	private Integer type;//
	
	private Long advertiseId;/*广告ID*/
	private Long tfolderId;/*频道类型*/


	// Many to one
	private ProductList product; // not null (productId)
	private HtmlPage page; // not null (pageId)
	private Site site; // not null (siteId)
	private Folder folder; // not null (folderId)
	private RecommandContainer container; // not null (containerId)

	private transient Advertise advertise;
	private transient Folder tfolder;
	// ---------------------------
	// Constructors
	// ---------------------------

	public ProductContainer() {
	}

	public ProductContainer(Long primaryKey) {
		this();
		setPrimaryKey(primaryKey);
	}

	// ---------------------------
	// Identifiable implementation
	// ---------------------------

	@Transient
	@XmlTransient
	public Long getPrimaryKey() {
		return getProductContainerId();
	}

	public void setPrimaryKey(Long productContainerId) {
		setProductContainerId(productContainerId);
	}

	// -------------------------------
	// Getter & Setter
	// -------------------------------

	// -- [productContainerId] ------------------------

	@Column(name = "product_container_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getProductContainerId() {
		return productContainerId;
	}

	public void setProductContainerId(Long productContainerId) {
		this.productContainerId = productContainerId;
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

	// -- [folderId] ------------------------
	@NotNull
	@Column(name = "folder_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	// -- [pageId] ------------------------
	@NotNull
	@Column(name = "page_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
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

	// -- [startDate] ------------------------

	@Column(name = "start_date", length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	// -- [endDate] ------------------------

	@Column(name = "end_date", length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	// -- [click] ------------------------

	@NotNull
	@Column(nullable = false, precision = 20)
	public Long getClick() {
		return click;
	}

	public void setClick(Long click) {
		this.click = click;
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

	// -- [picId] ------------------------

	@Column(name = "pic_id", precision = 20)
	public Long getPicId() {
		return picId;
	}

	public void setPicId(Long picId) {
		this.picId = picId;
	}

	// -- [siteId] ------------------------
	@NotNull
	@Column(name = "site_id", nullable = false, precision = 10, insertable = false, updatable = false)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
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
	// many-to-one: ProductContainer.pageId ==> HtmlPage.pageId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@JoinColumn(name = "page_id", nullable = false)
	@ManyToOne(fetch = LAZY)
	@JsonIgnore
	public HtmlPage getPage() {
		return page;
	}

	/**
	 * Set the page without adding this ProductContainer instance on the passed
	 * page If you want to preserve referential integrity we recommend to use
	 * instead the corresponding adder method provided by HtmlPage
	 */
	public void setPage(HtmlPage page) {
		this.page = page;

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (page != null) {
			setPageId(page.getPageId());
		} else {
			setPageId(null);
		}
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-one: ProductContainer.siteId ==> Site.siteId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	@JoinColumn(name = "site_id", nullable = false)
	@ManyToOne(fetch = LAZY)
	@JsonIgnore
	public Site getSite() {
		return site;
	}

	/**
	 * Set the site without adding this ProductContainer instance on the passed
	 * site If you want to preserve referential integrity we recommend to use
	 * instead the corresponding adder method provided by Site
	 */
	public void setSite(Site site) {
		this.site = site;

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (site != null) {
			setSiteId(site.getSiteId());
		} else {
			setSiteId(null);
		}
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-one: ProductContainer.folderId ==> Folder.folderId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@JoinColumn(name = "folder_id", nullable = false)
	@ManyToOne(fetch = LAZY)
	@JsonIgnore
	public Folder getFolder() {
		return folder;
	}

	/**
	 * Set the folder without adding this ProductContainer instance on the
	 * passed folder If you want to preserve referential integrity we recommend
	 * to use instead the corresponding adder method provided by Folder
	 */
	public void setFolder(Folder folder) {
		this.folder = folder;

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (folder != null) {
			setFolderId(folder.getFolderId());
		} else {
			setFolderId(null);
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

	
	//以下为新增加的三个字段
	@NotNull
	@Column(nullable = false, precision = 10)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "advertise_id", precision = 20, insertable = false, updatable = false)
	public Long getAdvertiseId() {
		return advertiseId;
	}

	public void setAdvertiseId(Long advertiseId) {
		this.advertiseId = advertiseId;
	}

	@Column(name = "t_folder_id", precision = 20, insertable = false, updatable = false)
	public Long getTfolderId() {
		return tfolderId;
	}

	public void setTfolderId(Long tfolderId) {
		this.tfolderId = tfolderId;
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

		if (!(productContainer instanceof ProductContainer)) {
			return false;
		}

		ProductContainer other = (ProductContainer) productContainer;
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
		result.append("productContainer.productContainerId=[").append(getProductContainerId()).append("]\n");
		result.append("productContainer.containerId=[").append(getContainerId()).append("]\n");
		result.append("productContainer.folderId=[").append(getFolderId()).append("]\n");
		result.append("productContainer.pageId=[").append(getPageId()).append("]\n");
		result.append("productContainer.productId=[").append(getProductId()).append("]\n");
		result.append("productContainer.sortOrder=[").append(getSortOrder()).append("]\n");
		result.append("productContainer.startDate=[").append(getStartDate()).append("]\n");
		result.append("productContainer.endDate=[").append(getEndDate()).append("]\n");
		result.append("productContainer.status=[").append(getStatus()).append("]\n");
		result.append("productContainer.click=[").append(getClick()).append("]\n");
		result.append("productContainer.createBy=[").append(getCreateBy()).append("]\n");
		result.append("productContainer.createDate=[").append(getCreateDate()).append("]\n");
		result.append("productContainer.modBy=[").append(getModBy()).append("]\n");
		result.append("productContainer.modDate=[").append(getModDate()).append("]\n");
		result.append("productContainer.picId=[").append(getPicId()).append("]\n");
		result.append("productContainer.siteId=[").append(getSiteId()).append("]\n");
		
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
	public ProductContainer copy() {
		ProductContainer productContainer = new ProductContainer();
		copyTo(productContainer);
		return productContainer;
	}

	/**
	 * Copy the current properties to the given object
	 */
	@Override
	public void copyTo(ProductContainer productContainer) {
		productContainer.setProductContainerId(getProductContainerId());
		// productContainer.setContainerId(getContainerId());
		// productContainer.setFolderId(getFolderId());
		// productContainer.setPageId(getPageId());
		// productContainer.setProductId(getProductId());
		productContainer.setSortOrder(getSortOrder());
		productContainer.setStartDate(getStartDate());
		productContainer.setEndDate(getEndDate());
		productContainer.setStatus(getStatus());
		productContainer.setClick(getClick());
		productContainer.setCreateBy(getCreateBy());
		productContainer.setCreateDate(getCreateDate());
		productContainer.setModBy(getModBy());
		productContainer.setModDate(getModDate());
		productContainer.setPicId(getPicId());
		// productContainer.setSiteId(getSiteId());
		productContainer.setType(getType());

		if (getProduct() != null) {
			productContainer.setProduct(new ProductList(getProduct().getPrimaryKey()));
		}
		if (getPage() != null) {
			productContainer.setPage(new HtmlPage(getPage().getPrimaryKey()));
		}
		if (getSite() != null) {
			productContainer.setSite(new Site(getSite().getPrimaryKey()));
		}
		if (getFolder() != null) {
			productContainer.setFolder(new Folder(getFolder().getPrimaryKey()));
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

	public enum StatusEnum {
		Valid(0), Invalid(1);
		private Integer value;
		private String displayName;

		private StatusEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "显示";
				break;
			case 1:
				displayName = "不显示";
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

		public static StatusEnum getInstance(Integer value) {
			if (null != value) {
				StatusEnum[] enums = StatusEnum.values();
				for (StatusEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
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