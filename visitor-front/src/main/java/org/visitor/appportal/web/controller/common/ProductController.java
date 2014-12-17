package org.visitor.appportal.web.controller.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorProductAddressService;
import org.visitor.appportal.service.newsite.VisitorProductDetailInfoService;
import org.visitor.appportal.service.newsite.VisitorProductMultiPriceService;
import org.visitor.appportal.service.newsite.VisitorProductOperationService;
import org.visitor.appportal.service.newsite.VisitorProductService;
import org.visitor.appportal.service.newsite.VisitorUserInternalMailService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.beans.ProductAddressTemp;
import org.visitor.appportal.visitor.beans.ProductDetailTemp;
import org.visitor.appportal.visitor.beans.ProductOperationTemp;
import org.visitor.appportal.visitor.beans.ProductPriceMultiTemp;
import org.visitor.appportal.visitor.beans.ProductTemp;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.beans.UserInternalMailTemp;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductAddress;
import org.visitor.appportal.visitor.domain.ProductDetailInfo;
import org.visitor.appportal.visitor.domain.ProductMultiPrice;
import org.visitor.appportal.visitor.domain.ProductOperation;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserInternalMail;
import org.visitor.appportal.web.utils.ProductInfo;
import org.visitor.appportal.web.utils.ProductInfo.StatusTypeEnum;
import org.visitor.appportal.web.utils.RegisterInfo;
import org.visitor.appportal.web.utils.RegisterInfo.UserMailStatusEnum;
import org.visitor.appportal.web.utils.WebInfo;

@Controller
@RequestMapping("/product/")
public class ProductController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private VisitorProductService visitorProductService;
	@Autowired
	private ProductRedisService productRedisService;
	@Autowired
	private VisitorProductAddressService visitorProductAddressService;
	@Autowired
	private VisitorProductDetailInfoService visitorProductDetailInfoService;
	@Autowired
	private VisitorProductMultiPriceService visitorProductMultiPriceService;
	@Autowired
	private VisitorProductOperationService visitorProductOperationService;
	@Autowired
	private VisitorUserInternalMailService visitorUserInternalMailService;
	@Autowired
	private UserRedisService userRedisService;
	
	@RequestMapping("create")
	public void createProduct(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductTemp pt = super.getProductTempJson(request);
		
		String homeType = pt.getProductHomeTypeStr();
		String roomType = pt.getProductRoomTypeStr();
		String accomodates = pt.getProductAccomodatesStr();
		String city = pt.getProductCityStr();
		
		Product product = new Product();
		
		product.setProductHometype(homeType);
		product.setProductRoomType(roomType);
		product.setProductAccomodates(accomodates);
		product.setProductCity(city);
		
		Date newDate = new Date();
		
		product.setProductCreateDate(newDate);
		product.setProductUpdateDate(newDate);
		
		product.setProductStatus(ProductInfo.EDIT_STATUS);
		
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		product.setProductPublishUserId(userTemp.getUserId());
		product.setProductPublishUserEmail(userTemp.getUserEmail());
		
		visitorProductService.saveProduct(product);
		productRedisService.saveUserProductToRedis(userTemp, product);
		
		ResultJson rj = new ResultJson();
		
		rj.setResult(0);
		rj.setResultDesc(ProductInfo.PRODUCT_CREATE_SUCCESS);
		rj.setProductId(product.getProductId());
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("availtype")
	public void productAvailType(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductDetailTemp pdt = super.getProductDetailTempJson(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getUserProductFromRedis(userTemp, pdt.getProductIdStr());
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_DETAIL_UPDATE_SUCCESS;
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		} else if (product.getProductAvailabletype().intValue() == ProductInfo.EDIT_STATUS.intValue()) {
			// do store and to redis to mongo stuff
			String productAvailabletypeStr = pdt.getProductAvailableTypeStr();
			if (StringUtils.isNotEmpty(productAvailabletypeStr)) {
				Integer productAvailabletype = Integer.valueOf(productAvailabletypeStr);
				product.setProductAvailabletype(productAvailabletype);
			}
			
			visitorProductService.saveProduct(product);
			productRedisService.saveUserProductToRedis(userTemp, product); //save to redis edit status
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("savedetail")
	public void savePorductDetail(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductDetailTemp pdt = super.getProductDetailTempJson(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getUserProductFromRedis(userTemp, pdt.getProductIdStr());
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_DETAIL_UPDATE_SUCCESS;
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		} else {
			// do store and to redis to mongo stuff
			String productAvailabletypeStr = pdt.getProductAvailableTypeStr();
			if (StringUtils.isNotEmpty(productAvailabletypeStr)) {
				Integer productAvailabletype = Integer.valueOf(productAvailabletypeStr);
				product.setProductAvailabletype(productAvailabletype);
			}
			
			product.setProductCurrency(pdt.getProductCurrencyStr());
			
			String productBasepriceStr = pdt.getProductBasepriceStr();
			if (StringUtils.isNotEmpty(productBasepriceStr)) {
				Integer productBaseprice = Integer.valueOf(productBasepriceStr);
				product.setProductBaseprice(productBaseprice);
			}
			
			product.setProductRoomnum(pdt.getProductBedroomNumStr());
			product.setProductBedsnum(pdt.getProductBedsNumStr());
			product.setProductBathroomnum(pdt.getProductBathroomNumStr());
			
			product.setProductOverviewtitle(pdt.getProductOverviewTitleStr());
			
			String amenitiesFinal = ProductInfo.AMENITIES_TYPE_MOSTCOMMON.toString() + ProductInfo.AMENITIES_INDEX_SPLIT + pdt.getAmenitiesMostCommonSelected() + ProductInfo.AMENITIES_ITEM_SPLIT
					+ ProductInfo.AMENITIES_TYPE_EXTRAS.toString() + ProductInfo.AMENITIES_INDEX_SPLIT + pdt.getAmenitiesExtrasSelected() + ProductInfo.AMENITIES_ITEM_SPLIT
					+ ProductInfo.AMENITIES_TYPE_SPECIALFEATURE.toString() + ProductInfo.AMENITIES_INDEX_SPLIT + pdt.getAmenitiesSpecialFeaturesSelected() + ProductInfo.AMENITIES_ITEM_SPLIT
					+ ProductInfo.AMENITIES_TYPE_HOMESAFETY.toString() + ProductInfo.AMENITIES_INDEX_SPLIT + pdt.getAmenitiesHomeSafetySelected();
			
			product.setProductAmenities(amenitiesFinal);
			
			String productPriceperweekStr = pdt.getProductPricePerWeekStr();
			if (StringUtils.isNotEmpty(productPriceperweekStr)) {
				Integer productPriceperweek = Integer.valueOf(productPriceperweekStr);
				product.setProductPriceperweek(productPriceperweek);
			}
			
			String productPricepermonthStr = pdt.getProductPricePerMonthStr();
			if (StringUtils.isNotEmpty(productPricepermonthStr)) {
				Integer productPricepermonth = Integer.valueOf(productPricepermonthStr);
				product.setProductPricepermonth(productPricepermonth);
			}
			
			String productTermminstayStr = pdt.getMinStayStr();
			if (StringUtils.isNotEmpty(productTermminstayStr)) {
				Integer productTermminstay = Integer.valueOf(productTermminstayStr);
				product.setProductTermminstay(productTermminstay);
			}
			
			String productTermmaxstayStr = pdt.getMaxStayStr();
			if (StringUtils.isNotEmpty(productTermmaxstayStr)) {
				Integer productTermmaxstay = Integer.valueOf(productTermmaxstayStr);
				product.setProductTermmaxstay(productTermmaxstay);
			}
			
			product.setProductCheckinafter(pdt.getProductCheckinAfterStr());
			product.setproductCheckoutbefore(pdt.getProductCheckoutBeforeStr());
			product.setProductCancellationpolicy(pdt.getProductCancellationPolicyStr());
			
			Date upDate = new Date();
			product.setProductUpdateDate(upDate);
			
			product.setProductStatus(ProductInfo.ONLINE_STATUS);
			
			visitorProductService.saveProduct(product);
			productRedisService.saveUserProductToRedis(userTemp, product);
			productRedisService.saveProductToRedis(product); //online
			
			//details part;
			ProductDetailInfo pdiBean = new ProductDetailInfo();
			
			pdiBean.setPriProductId(product.getProductId());
			pdiBean.setPdiOwnerEmail(userTemp.getUserEmail());
			pdiBean.setPdiProductOverviewDetail(pdt.getProductOverviewDetailStr());
			pdiBean.setPdiProductExtraDirection(pdt.getProductExtraInfoDirectionStr());
			pdiBean.setPdiProductExtraGuestAccess(pdt.getProductExtraInfoGuestAccessStr());
			pdiBean.setPdiProductExtraGuestInteraction(pdt.getProductExtraInfoGuestInteractionStr());
			pdiBean.setPdiProductExtraHouseManual(pdt.getProductExtraInfoHouseManualStr());
			pdiBean.setPdiProductExtraHouseRule(pdt.getProductExtraInfoHouseRuleStr());
			pdiBean.setPdiProductExtraNeighborhood(pdt.getProductExtraInfoNeighborhoodStr());
			pdiBean.setPdiProductExtraOtherNote(pdt.getProductExtraInfoOtherNoteStr());
			pdiBean.setPdiProductExtraSpace(pdt.getProductExtraInfoSpaceStr());
			pdiBean.setPdiProductExtraTransit(pdt.getProductExtraInfoTransitStr());
			
			visitorProductDetailInfoService.saveProductDetailInfo(pdiBean);
			productRedisService.saveProductDetailInfoToRedis(pdiBean);
			//need to save to redis
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("updateAddress")
	public void productUpdateAddress(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductAddressTemp pdt = super.getProductAddressTempJson(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getUserProductFromRedis(userTemp, pdt.getProductIdStr());
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_ADDRESS_SAVE_SUCCESS;
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		} else {
			// do store and to redis to mongo stuff
			
			ProductAddress productAddress = new ProductAddress();
			
			productAddress.setPaProductid(product.getProductId());
			productAddress.setPaCountry(pdt.getProductCountryStr());
			productAddress.setPaCity(pdt.getProductCityStr());
			productAddress.setPaState(pdt.getProductStateStr());
			productAddress.setPaStreetaddress(pdt.getProductStreetAddressStr());
			productAddress.setPaZipcode(pdt.getProductZipcodeStr());
			productAddress.setPaDetail(pdt.getProductAddressDetailStr());
			
			visitorProductAddressService.saveProductAddress(productAddress);
			
			product.setProductAddressid(productAddress.getPaId());
			visitorProductService.saveProduct(product);
			productRedisService.saveUserProductToRedis(userTemp, product);
			productRedisService.saveProductToRedis(product); //online
			productRedisService.saveProductAddressToRedis(productAddress);
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("multiprice")
	public void productMultiPriceSet(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductPriceMultiTemp pdt = super.getProductPriceMultiTempJson(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getUserProductFromRedis(userTemp, pdt.getProductIdStr());
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_EXTRA_PRICE_SET_SAVE_SUCCESS;
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		} else {
			// do store and to redis to mongo stuff
			if (!productRedisService.ifContainsPriceKeySet(product.getProductId(), pdt.getAdditionalPriceKeyStr())) {
				ProductMultiPrice pmpT = new ProductMultiPrice();
				
				pmpT.setPmpProductId(product.getProductId());
				pmpT.setPmpProductPriceKey(pdt.getAdditionalPriceKeyStr());
				pmpT.setPmpProductPriceValue(Integer.valueOf(pdt.getAdditionalPriceValue()));
				pmpT.setPmpStatus(0);
				
				visitorProductMultiPriceService.saveProductMultiPrice(pmpT);
				productRedisService.saveProductMultiPriceToRedis(pmpT);
			} else {
				ProductMultiPrice pmp = productRedisService.getProductMultiPriceSetByProductIdAndKey(product.getProductId(), pdt.getAdditionalPriceKeyStr());
				pmp.setPmpProductPriceValue(Integer.valueOf(pdt.getAdditionalPriceValue()));
				visitorProductMultiPriceService.saveProductMultiPrice(pmp);
				productRedisService.saveProductMultiPriceToRedis(pmp);
			}
			//need to save to redis
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("saveOperation")
	public void productSaveOperation(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductOperationTemp pot = super.getProductOperationTempJson(request);
		
		String productIdStr = pot.getProductIdStr();
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getProductFromRedis(Long.valueOf(productIdStr));
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_OPERATION_SAVE_SUCCESS;
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FOR_OPERATION_UPDATE;
		} else {
			String poTypeStr = pot.getPoTypeStr();
			String poStartDateStr = pot.getPoStartDateStr();
			String poEndDateStr = pot.getPoEndDateStr();
			String poCurrencyStr = pot.getPoCurrencyStr();
			String poPriceStr = pot.getPoPricePerNightStr();
			String poNoticeStr = pot.getPoNoticeStr();
			
			ProductOperation poTemp = new ProductOperation();
			poTemp.setPoProductid(product.getProductId());
			poTemp.setPoCreateby(userTemp.getUserEmail());
			
			poTemp.setPoType(Integer.valueOf(poTypeStr));
			poTemp.setPoCurrency(poCurrencyStr);
			poTemp.setPoPricePerNight(Integer.valueOf(poPriceStr));
			poTemp.setPoNotice(poNoticeStr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			try {
				Date poStartDate = sdf.parse(poStartDateStr);
				Date poEndDate = sdf.parse(poEndDateStr);
				
				poTemp.setPoStartDate(poStartDate);
				poTemp.setPoEndDate(poEndDate);
				
				visitorProductOperationService.saveProductOperation(poTemp);
				productRedisService.setProductOperationToRedis(poTemp);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("deleteOperation")
	public void productDeleteOperation(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductOperationTemp pot = super.getProductOperationTempJson(request);
		
		String poIdStr = pot.getPoIdStr();
		String productIdStr = pot.getProductIdStr();
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_OPERATION_SAVE_SUCCESS;
		
		if (StringUtils.isNotEmpty(poIdStr) && StringUtils.isNotEmpty(productIdStr)) {
			ProductOperation poTemp = productRedisService.getProductOperationFromRedis(productIdStr, poIdStr);
			
			if (poTemp != null) {
				poTemp.setPoStatus(StatusTypeEnum.Inactive.ordinal());
				visitorProductOperationService.saveProductOperation(poTemp);
				productRedisService.deleteProductOperationToRedis(poTemp);
			}
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("getAllOperation")
	public void getProductOperationList(HttpServletRequest request,
			HttpServletResponse response) {
		ProductOperationTemp pot = super.getProductOperationTempJson(request);
		String productIdStr = pot.getProductIdStr();
		
		List<ProductOperation> list = new ArrayList<ProductOperation>();
		
		if (StringUtils.isNotEmpty(productIdStr)) {
			list = productRedisService.getProductOperationList(Long.valueOf(productIdStr));
		} 
		
		super.sendJSONResponse(list, response);
	}
	
	@RequestMapping("saveInternalMail")
	public void saveUserInternalMail(HttpServletRequest request,
			HttpServletResponse response) {
		//do internal
		Integer result = 0;
		String resultDesc = RegisterInfo.USER_INTERNALMAIL_SAVE_SUCCESS;
		
		UserInternalMailTemp uimT = super.getUserInternalMailTempJson(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		String uimIdStr = uimT.getUimIdStr();
		
		if (StringUtils.isNotEmpty(uimIdStr)) {
			UserInternalMail uimFetch = userRedisService.getUserInternalMailAlways(uimIdStr);
			String userTEmailStr = userTemp.getUserEmail();
			
			String contentStr = "";
			if (StringUtils.equals(userTEmailStr, uimFetch.getUimFromUserMail())) {
				contentStr = uimFetch.getUimContent() + RegisterInfo.USER_EMAIL_CONTENT_SPLIT + RegisterInfo.USER_EMAIL_PREFIX_FROM + RegisterInfo.USER_EMAIL_SPLIT + uimT.getContentStr();
			} else {
				contentStr = uimFetch.getUimContent() + RegisterInfo.USER_EMAIL_CONTENT_SPLIT + RegisterInfo.USER_EMAIL_PREFIX_TO + RegisterInfo.USER_EMAIL_SPLIT + uimT.getContentStr();
			}
			
			uimFetch.setUimContent(contentStr);
			visitorUserInternalMailService.saveVisitorUserInternalMail(uimFetch);
			userRedisService.setUserInternalMailAlways(uimFetch);
			userRedisService.setUserInternalMailUnread(uimFetch);
		} else {
			String pidStr = uimT.getProductIdStr();
			String contentStr = uimT.getContentStr();
			
			if (StringUtils.isNotEmpty(pidStr)) {
				Long productId = Long.valueOf(pidStr);
				UserInternalMail uim = new UserInternalMail();
				uim.setUimProductId(productId);
				
				Product product = productRedisService.getProductFromRedis(productId);
				uim.setUimFromUserMail(userTemp.getUserEmail());
				uim.setUimToUserMail(product.getProductPublishUserEmail());
				uim.setUimStatus(UserMailStatusEnum.Unread.ordinal());
				uim.setUimContent(RegisterInfo.USER_EMAIL_PREFIX_FROM + RegisterInfo.USER_EMAIL_SPLIT + contentStr);
				visitorUserInternalMailService.saveVisitorUserInternalMail(uim);
				userRedisService.setUserInternalMailAlways(uim);
				userRedisService.setUserInternalMailUnread(uim);
			} else {
				log.info("product Id null for user internal mail save");
				result = -1;
				resultDesc = RegisterInfo.USER_INTERNALMAIL_SAVE_FAIL;
			}
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
}
