package org.visitor.appportal.visitor.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

//paypal order table bean
@Entity
@Table(name = "pay_order")
public class ProductPayOrder { 
	private Long payOrderId; //pay_order_id
	private String payOrderOids; //pay_order_oids //maybe there are combined orders
	private Integer payStatus; //pay_status
	private String receiverEmail; //receiver_email
	private String receiverId; //receiver_id
	private String residenceCountry; //residence_country
	private Integer testIpn; //test_ipn
	private String transactionId; //transaction_subject
	private String txnId; //txn_id
	private String txnType; //txn_type
	private String payerEmail; //payer_email
	private String payerId; //payer_id
	private String payerStatus; //payer_status
	private String firstName; //first_name
	private String lastName; //last_name
	private String addressCity; //address_city
	private String addressCountry; //address_country
	private String addressCountryCode; //address_country_code
	private String addressName; //address_name
	private String addressState; //address_state
	private String addressStatus; //address_status
	private String addressStreet; //address_street
	private String addressZip; //address_zip
	private String custom; //custom
	private Double handlingAmount; //handling_amount
	private String itemName; //item_name
	private String itemNumber; //item_number
	private String mcCurrency; //mc_currency
	private Double mcFee; //mc_fee
	private Double mcGross; //mc_gross
	private Date paymentDate; //payment_date
	private Double paymentFee; //payment_fee
	private Double paymentGross; //payment_gross
	private String paymentStatus; //payment_status
	private String paymentType; //payment_type
	private String checkoutStatus; //protection_eligibility
	private Integer quantity; //quantity
	private Double shipping; //shipping
	private Double tax; //tax
	private String notifyVersion; //notify_version
	private String charset; //charset
	private String verifySign;//verify_sign
	private String payOrderOwnerEmail;
	private String payerPhoneNum;
	private String paymentRequestId;
	private String checkoutErrorCode;
	private String checkoutErrorShortmsg;
	
	@Column(name = "pay_order_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getPayOrderId() {
		return payOrderId;
	}
	public void setPayOrderId(Long payOrderId) {
		this.payOrderId = payOrderId;
	}
	
	@Length(max = 127)
	@Column(name = "pay_order_oids", nullable = false)
	public String getPayOrderOids() {
		return payOrderOids;
	}
	public void setPayOrderOids(String payOrderOids) {
		this.payOrderOids = payOrderOids;
	}
	
	@Column(name = "pay_status", nullable = false, precision = 2)
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	
	@Length(max = 63)
	@Column(name = "receiver_email")
	public String getReceiverEmail() {
		return receiverEmail;
	}
	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}
	
	@Length(max = 31)
	@Column(name = "receiver_id")
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	@Length(max = 4)
	@Column(name = "residence_country")
	public String getResidenceCountry() {
		return residenceCountry;
	}
	public void setResidenceCountry(String residenceCountry) {
		this.residenceCountry = residenceCountry;
	}
	
	@Column(name = "test_ipn", precision = 2)
	public Integer getTestIpn() {
		return testIpn;
	}
	public void setTestIpn(Integer testIpn) {
		this.testIpn = testIpn;
	}
	
	@Length(max = 63)
	@Column(name = "transaction_id")
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	@Length(max = 31)
	@Column(name = "txn_id")
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	@Length(max = 31)
	@Column(name = "txn_type")
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	
	@Length(max = 63)
	@Column(name = "payer_email")
	public String getPayerEmail() {
		return payerEmail;
	}
	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}
	
	@Length(max = 31)
	@Column(name = "payer_id")
	public String getPayerId() {
		return payerId;
	}
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	
	@Length(max = 16)
	@Column(name = "payer_status")
	public String getPayerStatus() {
		return payerStatus;
	}
	public void setPayerStatus(String payerStatus) {
		this.payerStatus = payerStatus;
	}
	
	@Length(max = 31)
	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Length(max = 31)
	@Column(name = "last_name")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Length(max = 63)
	@Column(name = "address_city")
	public String getAddressCity() {
		return addressCity;
	}
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	
	@Length(max = 63)
	@Column(name = "address_country")
	public String getAddressCountry() {
		return addressCountry;
	}
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}
	
	@Length(max = 4)
	@Column(name = "address_country_code")
	public String getAddressCountryCode() {
		return addressCountryCode;
	}
	public void setAddressCountryCode(String addressCountryCode) {
		this.addressCountryCode = addressCountryCode;
	}
	
	@Length(max = 63)
	@Column(name = "address_name")
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	
	@Length(max = 31)
	@Column(name = "address_state")
	public String getAddressState() {
		return addressState;
	}
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}
	
	@Length(max = 16)
	@Column(name = "address_status")
	public String getAddressStatus() {
		return addressStatus;
	}
	public void setAddressStatus(String addressStatus) {
		this.addressStatus = addressStatus;
	}
	
	@Length(max = 31)
	@Column(name = "address_street")
	public String getAddressStreet() {
		return addressStreet;
	}
	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}
	
	@Length(max = 16)
	@Column(name = "address_zip")
	public String getAddressZip() {
		return addressZip;
	}
	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}
	
	@Length(max = 31)
	@Column(name = "custom")
	public String getCustom() {
		return custom;
	}
	public void setCustom(String custom) {
		this.custom = custom;
	}
	
	@Column(name="handling_amount", precision = 22, scale=2)
	public Double getHandlingAmount() {
		return handlingAmount;
	}
	public void setHandlingAmount(Double handlingAmount) {
		this.handlingAmount = handlingAmount;
	}
	
	@Length(max = 31)
	@Column(name = "item_name")
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@Length(max = 31)
	@Column(name = "item_number")
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	
	@Length(max = 4)
	@Column(name = "mc_currency")
	public String getMcCurrency() {
		return mcCurrency;
	}
	public void setMcCurrency(String mcCurrency) {
		this.mcCurrency = mcCurrency;
	}
	
	@Column(name="mc_fee", precision = 22, scale=2)
	public Double getMcFee() {
		return mcFee;
	}
	public void setMcFee(Double mcFee) {
		this.mcFee = mcFee;
	}
	
	@Column(name="mc_gross", precision = 22, scale=2)
	public Double getMcGross() {
		return mcGross;
	}
	public void setMcGross(Double mcGross) {
		this.mcGross = mcGross;
	}
	
	@Column(name = "payment_date", nullable = true, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	@Column(name="payment_fee", precision = 22, scale=2)
	public Double getPaymentFee() {
		return paymentFee;
	}
	public void setPaymentFee(Double paymentFee) {
		this.paymentFee = paymentFee;
	}
	
	@Column(name="payment_gross", precision = 22, scale=2)
	public Double getPaymentGross() {
		return paymentGross;
	}
	public void setPaymentGross(Double paymentGross) {
		this.paymentGross = paymentGross;
	}
	
	@Length(max = 16)
	@Column(name = "payment_status")
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	@Length(max = 16)
	@Column(name = "payment_type")
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	@Length(max = 63)
	@Column(name = "checkout_status")
	public String getCheckoutStatus() {
		return checkoutStatus;
	}
	public void setCheckoutStatus(String checkoutStatus) {
		this.checkoutStatus = checkoutStatus;
	}
	
	@Column(name = "quantity", precision = 10)
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Column(name="shipping", precision = 22, scale=2)
	public Double getShipping() {
		return shipping;
	}
	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}
	
	@Column(name="tax", precision = 22, scale=2)
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	
	@Length(max = 16)
	@Column(name = "notify_version")
	public String getNotifyVersion() {
		return notifyVersion;
	}
	public void setNotifyVersion(String notifyVersion) {
		this.notifyVersion = notifyVersion;
	}
	
	@Length(max = 31)
	@Column(name = "charset")
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	@Length(max = 127)
	@Column(name = "verify_sign")
	public String getVerifySign() {
		return verifySign;
	}
	public void setVerifySign(String verifySign) {
		this.verifySign = verifySign;
	}
	
	@Length(max = 64)
	@Column(name = "pay_order_owner_email", nullable = false)
	public String getPayOrderOwnerEmail() {
		return payOrderOwnerEmail;
	}
	public void setPayOrderOwnerEmail(String payOrderOwnerEmail) {
		this.payOrderOwnerEmail = payOrderOwnerEmail;
	}
	
	@Length(max = 31)
	@Column(name = "payer_phone_num")
	public String getPayerPhoneNum() {
		return payerPhoneNum;
	}
	public void setPayerPhoneNum(String payerPhoneNum) {
		this.payerPhoneNum = payerPhoneNum;
	}
	
	@Length(max = 63)
	@Column(name = "payment_request_id")
	public String getPaymentRequestId() {
		return paymentRequestId;
	}
	public void setPaymentRequestId(String paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}
	
	@Length(max = 16)
	@Column(name = "checkout_error_code")
	public String getCheckoutErrorCode() {
		return checkoutErrorCode;
	}
	public void setCheckoutErrorCode(String checkoutErrorCode) {
		this.checkoutErrorCode = checkoutErrorCode;
	}
	
	@Length(max = 127)
	@Column(name="checkout_error_shormsg")
	public String getCheckoutErrorShortmsg() {
		return checkoutErrorShortmsg;
	}
	public void setCheckoutErrorShortmsg(String checkoutErrorShortmsg) {
		this.checkoutErrorShortmsg = checkoutErrorShortmsg;
	}
	
}
