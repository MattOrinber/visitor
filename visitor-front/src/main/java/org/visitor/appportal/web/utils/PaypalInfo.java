package org.visitor.appportal.web.utils;

public class PaypalInfo {
	public static final String receiver_email = "receiver_email";
	public static final String receiver_id = "receiver_id";
	public static final String residence_country = "residence_country";
	public static final String test_ipn = "test_ipn";
	public static final String transaction_subject = "transaction_subject";
	public static final String txn_id = "txn_id";
	public static final String txn_type = "txn_type";
	public static final String payer_email = "payer_email";
	public static final String payer_id = "payer_id";
	public static final String payer_status = "payer_status";
	public static final String first_name = "first_name";
	public static final String last_name = "last_name";
	public static final String address_city = "address_city";
	public static final String address_country = "address_country";
	public static final String address_country_code = "address_country_code";
	public static final String address_name = "address_name";
	public static final String address_state = "address_state";
	public static final String address_status = "address_status";
	public static final String address_street = "address_street";
	public static final String address_zip = "address_zip";
	public static final String custom = "custom";
	public static final String handling_amount = "handling_amount";
	public static final String item_name = "item_name";
	public static final String item_number = "item_number";
	public static final String mc_currency = "mc_currency";
	public static final String mc_fee = "mc_fee";
	public static final String mc_gross = "mc_gross";
	public static final String payment_date = "payment_date";
	public static final String payment_fee = "payment_fee";
	public static final String payment_gross = "payment_gross";
	public static final String payment_status = "payment_status";
	public static final String payment_type = "payment_type";
	public static final String protection_eligibility = "protection_eligibility";
	public static final String quantity = "quantity";
	public static final String shipping = "shipping";
	public static final String tax = "tax";
	public static final String notify_version = "notify_version";
	public static final String charset = "charset";
	public static final String verify_sign = "verify_sign";
	
	
	
	public static final String status_paymentCompleted = "Completed";
	public static final String status_paymentVerified = "VERIFIED";
	public static final String status_paymentInvalid = "INVALID";
	
	
	public static final String floopy_paypalMerchantId = "payPalMerchantId";
	public static final String floopy_paypalMerchantEmail = "payPalMerchantEmail";
	public static final String floopy_paypalCallBackURL = "paypalCallbackURL";
	
	
	//express checkout info
	public static final String paypalExpressCheckoutMethodPayment = "paypalExpressCheckoutMethodPayment";
	public static final String paypalExpressCheckoutMethodDetail = "paypalExpressCheckoutMethodDetail";
	public static final String paypalExpressCheckoutMethodSet = "paypalExpressCheckoutMethodSet";
	public static final String paypalExpressCheckoutVersion = "paypalExpressCheckoutVersion";
	public static final String paypalExpressCheckoutSignature = "paypalExpressCheckoutSignature";
	public static final String paypalExpressCheckoutPassword = "paypalExpressCheckoutPassword";
	public static final String paypalExpressCheckoutUser = "paypalExpressCheckoutUser";
	public static final String paypalExpressCheckoutRedirectRUL = "paypalExpressCheckoutRedirectRUL";
	public static final String paypalExpressCheckoutURL = "paypalExpressCheckoutURL";
	
	//fix parameter
	public static final String pec_p_fix_action = "Sale";
	public static final String pec_r_fix_ack = "Success";
	
	//express checkout single sale parameters
	public static final String pec_p_user = "USER";
	public static final String pec_p_password = "PWD";
	public static final String pec_p_signature = "SIGNATURE";
	public static final String pec_p_version = "VERSION";
	public static final String pec_p_method = "METHOD";
	public static final String pec_p_amount = "PAYMENTREQUEST_0_AMT";
	public static final String pec_p_paymentaction = "PAYMENTREQUEST_0_PAYMENTACTION"; //Sale
	public static final String pec_p_currencycode = "PAYMENTREQUEST_0_CURRENCYCODE"; //
	
	public static final String pec_p_returnurl = "returnUrl";
	public static final String pec_p_cancelurl = "cancelUrl";
	
	//GetExpressCheckoutDetails
	public static final String pec_r_token = "TOKEN";
	public static final String pec_r_ack = "ACK";

}
