package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "product")
public class Product {
	
	//raw attributes
	private Long productId;
	private Integer productHometype;
	private Integer productRoomType;
	private Integer productAccomodates;
	private Integer productAvailabletype;
	private Integer productBaseprice;
	private Integer productCurrency;
	private String productOverviewtitle;
	private String productPhotopaths;
	private String productAmenities;
	private Integer productRoomnum;
	private Integer productBedsnum;
	private Integer productBathroomnum;
	private Long productAddressid;
	private Integer productPriceperweek;
	private Integer productPricepermonth;
	private Integer productTermminstay;
	private Integer productTermmaxstay;
	private Integer productCheckinafter;
	private Integer productCheckoutafter;
	private Integer productCancellationpolicy;
	private String productAvailkey;
	
	@Column(name = "product_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	@Column(name = "product_hometype", nullable = false, precision = 2)
	public Integer getProductHometype() {
		return productHometype;
	}
	public void setProductHometype(Integer productHometype) {
		this.productHometype = productHometype;
	}
	
	@Column(name = "product_roomtype", nullable = false, precision = 2)
	public Integer getProductRoomType() {
		return productRoomType;
	}
	public void setProductRoomType(Integer productRoomType) {
		this.productRoomType = productRoomType;
	}
	
	@Column(name = "product_accomodates", nullable = false, precision = 2)
	public Integer getProductAccomodates() {
		return productAccomodates;
	}
	public void setProductAccomodates(Integer productAccomodates) {
		this.productAccomodates = productAccomodates;
	}
	
	@Column(name = "product_availabletype", precision = 2)
	public Integer getProductAvailabletype() {
		return productAvailabletype;
	}
	public void setProductAvailabletype(Integer productAvailabletype) {
		this.productAvailabletype = productAvailabletype;
	}
	
	@Column(name = "product_baseprice", precision = 6)
	public Integer getProductBaseprice() {
		return productBaseprice;
	}
	public void setProductBaseprice(Integer productBaseprice) {
		this.productBaseprice = productBaseprice;
	}
	
	@Column(name = "product_currency", precision = 4)
	public Integer getProductCurrency() {
		return productCurrency;
	}
	public void setProductCurrency(Integer productCurrency) {
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
	
	@Column(name = "product_roomnum", precision = 2)
	public Integer getProductRoomnum() {
		return productRoomnum;
	}
	public void setProductRoomnum(Integer productRoomnum) {
		this.productRoomnum = productRoomnum;
	}
	
	@Column(name = "product_bedsnum", precision = 2)
	public Integer getProductBedsnum() {
		return productBedsnum;
	}
	public void setProductBedsnum(Integer productBedsnum) {
		this.productBedsnum = productBedsnum;
	}
	
	@Column(name = "product_bathroomnum", precision = 2)
	public Integer getProductBathroomnum() {
		return productBathroomnum;
	}
	public void setProductBathroomnum(Integer productBathroomnum) {
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
	
	@Column(name = "product_checkinafter", precision = 2)
	public Integer getProductCheckinafter() {
		return productCheckinafter;
	}
	public void setProductCheckinafter(Integer productCheckinafter) {
		this.productCheckinafter = productCheckinafter;
	}
	
	@Column(name = "product_checkoutafter", precision = 2)
	public Integer getProductCheckoutafter() {
		return productCheckoutafter;
	}
	public void setProductCheckoutafter(Integer productCheckoutafter) {
		this.productCheckoutafter = productCheckoutafter;
	}
	
	@Column(name = "product_cancelationpolicy", precision = 2)
	public Integer getProductCancellationpolicy() {
		return productCancellationpolicy;
	}
	public void setProductCancellationpolicy(Integer productCancellationpolicy) {
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
}
