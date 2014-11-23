package org.visitor.appportal.visitor.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "product")
public class Product {
	
	//raw attributes
	private Long productId;
	private String productHometype;
	private String productRoomType;
	private String productAccomodates;
	private Integer productAvailabletype;
	private Integer productBaseprice;
	private String productCurrency;
	private String productOverviewtitle;
	private String productPhotopaths;
	private String productAmenities;
	private String productRoomnum;
	private String productBedsnum;
	private String productBathroomnum;
	private Long productAddressid;
	private Integer productPriceperweek;
	private Integer productPricepermonth;
	private Integer productTermminstay;
	private Integer productTermmaxstay;
	private String productCheckinafter;
	private String productCheckoutafter;
	private String productCancellationpolicy;
	private String productAvailkey;
	private Integer productStatus;
	private Date productCreateDate;
	private String productCity;
	private Date productUpdateDate;
	private Long productPublishUserId;
	
	@Column(name = "product_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	@Length(max = 31)
	@Column(name = "product_hometype", nullable = false)
	public String getProductHometype() {
		return productHometype;
	}
	public void setProductHometype(String productHometype) {
		this.productHometype = productHometype;
	}
	
	@Length(max = 31)
	@Column(name = "product_roomtype", nullable = false)
	public String getProductRoomType() {
		return productRoomType;
	}
	public void setProductRoomType(String productRoomType) {
		this.productRoomType = productRoomType;
	}
	
	@Length(max = 7)
	@Column(name = "product_accomodates", nullable = false)
	public String getProductAccomodates() {
		return productAccomodates;
	}
	public void setProductAccomodates(String productAccomodates) {
		this.productAccomodates = productAccomodates;
	}
	
	@Column(name = "product_availabletype", precision = 2)
	public Integer getProductAvailabletype() {
		return productAvailabletype;
	}
	public void setProductAvailabletype(Integer productAvailabletype) {
		this.productAvailabletype = productAvailabletype;
	}
	
	@Column(name = "product_baseprice", precision = 10)
	public Integer getProductBaseprice() {
		return productBaseprice;
	}
	public void setProductBaseprice(Integer productBaseprice) {
		this.productBaseprice = productBaseprice;
	}
	
	@Length(max = 7)
	@Column(name = "product_currency")
	public String getProductCurrency() {
		return productCurrency;
	}
	public void setProductCurrency(String productCurrency) {
		this.productCurrency = productCurrency;
	}
	
	@Length(max = 127)
	@Column(name = "product_overview_title")
	public String getProductOverviewtitle() {
		return productOverviewtitle;
	}
	public void setProductOverviewtitle(String productOverviewtitle) {
		this.productOverviewtitle = productOverviewtitle;
	}
	
	@Length(max = 511)
	@Column(name = "product_photo_paths")
	public String getProductPhotopaths() {
		return productPhotopaths;
	}
	public void setProductPhotopaths(String productPhotopaths) {
		this.productPhotopaths = productPhotopaths;
	}
	
	@Length(max = 511)
	@Column(name = "product_amenities")
	public String getProductAmenities() {
		return productAmenities;
	}
	public void setProductAmenities(String productAmenities) {
		this.productAmenities = productAmenities;
	}
	
	@Length(max = 7)
	@Column(name = "product_roomnum", nullable = false)
	public String getProductRoomnum() {
		return productRoomnum;
	}
	public void setProductRoomnum(String productRoomnum) {
		this.productRoomnum = productRoomnum;
	}
	
	@Length(max = 7)
	@Column(name = "product_bedsnum", nullable = false)
	public String getProductBedsnum() {
		return productBedsnum;
	}
	public void setProductBedsnum(String productBedsnum) {
		this.productBedsnum = productBedsnum;
	}
	
	@Length(max = 7)
	@Column(name = "product_bathroomnum", nullable = false)
	public String getProductBathroomnum() {
		return productBathroomnum;
	}
	public void setProductBathroomnum(String productBathroomnum) {
		this.productBathroomnum = productBathroomnum;
	}
	
	@Column(name = "product_addressid", precision = 20)
	public Long getProductAddressid() {
		return productAddressid;
	}
	public void setProductAddressid(Long productAddressid) {
		this.productAddressid = productAddressid;
	}
	
	@Column(name = "product_priceperweek", precision = 8)
	public Integer getProductPriceperweek() {
		return productPriceperweek;
	}
	public void setProductPriceperweek(Integer productPriceperweek) {
		this.productPriceperweek = productPriceperweek;
	}
	
	@Column(name = "product_pricepermonth", precision = 8)
	public Integer getProductPricepermonth() {
		return productPricepermonth;
	}
	public void setProductPricepermonth(Integer productPricepermonth) {
		this.productPricepermonth = productPricepermonth;
	}
	
	@Column(name = "product_termminstay", precision = 4)
	public Integer getProductTermminstay() {
		return productTermminstay;
	}
	public void setProductTermminstay(Integer productTermminstay) {
		this.productTermminstay = productTermminstay;
	}
	
	@Column(name = "product_termmaxstay", precision = 4)
	public Integer getProductTermmaxstay() {
		return productTermmaxstay;
	}
	public void setProductTermmaxstay(Integer productTermmaxstay) {
		this.productTermmaxstay = productTermmaxstay;
	}
	
	@Length(max = 31)
	@Column(name = "product_checkinafter", nullable = false)
	public String getProductCheckinafter() {
		return productCheckinafter;
	}
	public void setProductCheckinafter(String productCheckinafter) {
		this.productCheckinafter = productCheckinafter;
	}
	
	@Length(max = 31)
	@Column(name = "product_checkoutafter", nullable = false)
	public String getProductCheckoutafter() {
		return productCheckoutafter;
	}
	public void setProductCheckoutafter(String productCheckoutafter) {
		this.productCheckoutafter = productCheckoutafter;
	}
	
	@Length(max = 127)
	@Column(name = "product_cancelationpolicy", nullable = false)
	public String getProductCancellationpolicy() {
		return productCancellationpolicy;
	}
	public void setProductCancellationpolicy(String productCancellationpolicy) {
		this.productCancellationpolicy = productCancellationpolicy;
	}
	
	@Length(max = 31)
	@Column(name = "product_availkey")
	public String getProductAvailkey() {
		return productAvailkey;
	}
	public void setProductAvailkey(String productAvailkey) {
		this.productAvailkey = productAvailkey;
	}
	
	@NotNull
	@Column(name = "product_status", nullable = false, precision = 2)
	public Integer getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}
	
	@Column(name = "product_create_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getProductCreateDate() {
		return productCreateDate;
	}
	public void setProductCreateDate(Date productCreateDate) {
		this.productCreateDate = productCreateDate;
	}
	
	@Length(max = 31)
	@NotNull
	@Column(name = "product_city")
	public String getProductCity() {
		return productCity;
	}
	public void setProductCity(String productCity) {
		this.productCity = productCity;
	}
	
	@Column(name = "product_update_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getProductUpdateDate() {
		return productUpdateDate;
	}
	public void setProductUpdateDate(Date productUpdateDate) {
		this.productUpdateDate = productUpdateDate;
	}
	
	@NotNull
	@Column(name = "product_publish_user_id", nullable = false, precision = 20)
	public Long getProductPublishUserId() {
		return productPublishUserId;
	}
	public void setProductPublishUserId(Long productPublishUserId) {
		this.productPublishUserId = productPublishUserId;
	}
}
