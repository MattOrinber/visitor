package org.visitor.appportal.web.controller.common;

import java.io.IOException;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.visitor.appportal.service.newsite.S3Service;
import org.visitor.appportal.service.newsite.VisitorProductAddressService;
import org.visitor.appportal.service.newsite.VisitorProductDetailInfoService;
import org.visitor.appportal.service.newsite.VisitorProductMultiPriceService;
import org.visitor.appportal.service.newsite.VisitorProductOperationService;
import org.visitor.appportal.service.newsite.VisitorProductPictureService;
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
import org.visitor.appportal.visitor.domain.ProductPicture;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserInternalMail;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.ProductInfo;
import org.visitor.appportal.web.utils.ProductInfo.StatusTypeEnum;
import org.visitor.appportal.web.utils.RegisterInfo;
import org.visitor.appportal.web.utils.RegisterInfo.UserMailStatusEnum;
import org.visitor.appportal.web.utils.WebInfo;

import com.amazonaws.services.s3.model.ObjectMetadata;

@Controller
@RequestMapping("product")
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
	private VisitorProductPictureService visitorProductPictureService;
	@Autowired
	private VisitorUserInternalMailService visitorUserInternalMailService;
	@Autowired
	private UserRedisService userRedisService;
	@Autowired
	private S3Service s3Service;
	
	@RequestMapping("/create")
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
	
	@RequestMapping("/availtype")
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
		} else if (product.getProductStatus().intValue() == ProductInfo.EDIT_STATUS.intValue()) {
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
		
		checkIfProductCanBePublish(rj, product);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("/pricing")
	public void productPricing(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductDetailTemp pdt = super.getProductDetailTempJson(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getUserProductFromRedis(userTemp, pdt.getProductIdStr());
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_DETAIL_UPDATE_SUCCESS;
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		} else if (product.getProductStatus().intValue() == ProductInfo.EDIT_STATUS.intValue()) {
			// do store and to redis to mongo stuff
			String productCurrencyStr = pdt.getProductCurrencyStr();
			String productBasePricingStr = pdt.getProductBasepriceStr();
			if (StringUtils.isNotEmpty(productCurrencyStr) &&
					StringUtils.isNotEmpty(productBasePricingStr)) {
				product.setProductCurrency(productCurrencyStr);
				product.setProductBaseprice(productBasePricingStr);
			}
			
			visitorProductService.saveProduct(product);
			productRedisService.saveUserProductToRedis(userTemp, product); //save to redis edit status
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		checkIfProductCanBePublish(rj, product);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("/description")
	public void productDescription(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductDetailTemp pdt = super.getProductDetailTempJson(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getUserProductFromRedis(userTemp, pdt.getProductIdStr());
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_DETAIL_UPDATE_SUCCESS;
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		} else if (product.getProductStatus().intValue() == ProductInfo.EDIT_STATUS.intValue()) {
			// do store and to redis to mongo stuff
			String productTitleStr = pdt.getProductOverviewTitleStr();
			String productDetailStr = pdt.getProductOverviewDetailStr();
			if (StringUtils.isNotEmpty(productTitleStr) &&
					StringUtils.isNotEmpty(productDetailStr)) {
				product.setProductOverviewtitle(productTitleStr);;
				
				//details part;
				ProductDetailInfo pdiBean = new ProductDetailInfo();
				
				pdiBean.setPriProductId(product.getProductId());
				pdiBean.setPdiOwnerEmail(userTemp.getUserEmail());
				pdiBean.setPdiProductOverviewDetail(productDetailStr);
				
				//product detail info save
				visitorProductDetailInfoService.saveProductDetailInfo(pdiBean);
				productRedisService.saveProductDetailInfoToRedis(pdiBean);
				
				//product basic info save
				visitorProductService.saveProduct(product);
				productRedisService.saveUserProductToRedis(userTemp, product); //save to redis edit status
			}
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		checkIfProductCanBePublish(rj, product);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("/address")
	public void productAddress(HttpServletRequest request, 
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
			productAddress.setPaDetail(pdt.getProductAddressDetailStr());
			
			visitorProductAddressService.saveProductAddress(productAddress);
			
			product.setProductAddressid(productAddress.getPaId());
			visitorProductService.saveProduct(product);
			productRedisService.saveUserProductToRedis(userTemp, product);
			//productRedisService.saveProductToRedis(product); //online
			productRedisService.saveProductAddressToRedis(productAddress);
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		checkIfProductCanBePublish(rj, product);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("/picture")
	public void productPicCreate(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value = "fileProductIcon", required = true) MultipartFile fileProductPic, 
			@RequestParam(value = "pid", required = true) String pidStr) {
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getUserProductFromRedis(userTemp, pidStr);
		
		Integer result = -1;
		String resultDesc = "not do";
		ResultJson resultJ = new ResultJson();
		
		if (product != null) {
			if (fileProductPic != null && !fileProductPic.isEmpty()) {
				ObjectMetadata meta = new ObjectMetadata();
				meta.setContentLength(fileProductPic.getSize());
				meta.setContentType(fileProductPic.getContentType());
				
				try {
					String fileOriUrl= "product/"+product.getProductId()+"/"+fileProductPic.getOriginalFilename();
					if (log.isInfoEnabled()) {
						log.info("fileOri url: >"+fileOriUrl+"<");
					}
					if (!productRedisService.checkIfPictureUrlExists(product.getProductId(), fileOriUrl)) {
						String awsBucketName = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgStatic);
						s3Service.createNewFile(fileOriUrl, fileProductPic.getInputStream(), awsBucketName, meta);
						
						String imgDomain = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgDomain);
						
						ProductPicture productPic = new ProductPicture();
						productPic.setProductPicProductId(product.getProductId());
						productPic.setProductPicProductUrl(fileOriUrl);
						productPic.setProductPicStatus(StatusTypeEnum.Active.ordinal());
						
						visitorProductPictureService.saveProductPicture(productPic);
						productRedisService.setProductPictureToRedis(productPic);
						productRedisService.savePictureUrlToRedis(productPic);
						
						result = 0;
						resultDesc = ProductInfo.PRODUCT_PICTURE_SAVE_SUCCESS;
						
						String displayUrl = imgDomain + awsBucketName + "/" + fileOriUrl; //实际访问图片的全路径
						
						product.setProductPhotopaths(displayUrl);
						
						visitorProductService.saveProduct(product);
						productRedisService.saveUserProductToRedis(userTemp, product);
						
						resultJ.setImageUrl(displayUrl);
						resultJ.setProductId(product.getProductId());
						resultJ.setProductPicId(productPic.getProductPicId());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		}
		
		resultJ.setResult(result);
		resultJ.setResultDesc(resultDesc);
		
		checkIfProductCanBePublish(resultJ, product);
		
		super.sendJSONResponse(resultJ, response);
	}
	
	@RequestMapping("/delpicture")
	public void deletePicture(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value = "picId", required = true) Long picId, 
			@RequestParam(value = "pid", required = true) Long pid) {
		Integer result = 0;
		String resultDesc = "success";
		ResultJson resultJ = new ResultJson();
		
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		Product product = productRedisService.getUserProductFromRedis(userTemp, String.valueOf(pid.longValue()));
		
		if (product != null) {
			ProductPicture poT = productRedisService.getProductPictureFromRedis(pid, picId);
			if (poT != null) {
				poT.setProductPicStatus(StatusTypeEnum.Inactive.ordinal());
				
				visitorProductPictureService.deleteProductPicture(poT);
				productRedisService.deletePictureUrlFromRedis(poT);
				productRedisService.deleteProductPictureFromRedis(pid, picId);
				
				resultJ.setProductId(product.getProductId());
				resultJ.setProductPicId(poT.getProductPicId());
			} else {
				result = -1;
				resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
			}
			checkIfProductCanBePublish(resultJ, product);
		} else {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		}
		
		resultJ.setResult(result);
		resultJ.setResultDesc(resultDesc);
		
		super.sendJSONResponse(resultJ, response);
	}
	
	@RequestMapping("/cancellationpolicy")
	public void saveCancellationPolicy(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductDetailTemp pdt = super.getProductDetailTempJson(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getUserProductFromRedis(userTemp, pdt.getProductIdStr());
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_CANCELLATION_POLICY_UPDATE_SUCCESS;
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		} else if (product.getProductStatus().intValue() == ProductInfo.EDIT_STATUS.intValue()) {
			// do store and to redis to mongo stuff
			String productCancellationPolicyStr = pdt.getProductCancellationPolicyStr();
			if (StringUtils.isNotEmpty(productCancellationPolicyStr)) {
				product.setProductCancellationpolicy(productCancellationPolicyStr);
			}
			
			visitorProductService.saveProduct(product);
			productRedisService.saveUserProductToRedis(userTemp, product); //save to redis edit status
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		checkIfProductCanBePublish(rj, product);
		
		super.sendJSONResponse(rj, response);
	}
	
	private void checkIfProductCanBePublish(ResultJson rj, Product product) {
		if (product.getProductAvailabletype() != null &&
				product.getProductCurrency() != null &&
				product.getProductBaseprice() != null &&
				product.getProductOverviewtitle() != null &&
				product.getProductAddressid() != null &&
				product.getProductCancellationpolicy() != null &&
				productRedisService.containsPicture(product.getProductId())) {
			rj.setProductCan(1);
		} else {
			rj.setProductCan(0);
		}
	}
	
	@RequestMapping("/publishproduct")
	public void publishProduct(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductDetailTemp pdt = super.getProductDetailTempJson(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Product product = productRedisService.getUserProductFromRedis(userTemp, pdt.getProductIdStr());
		
		Integer result = 0;
		String resultDesc = ProductInfo.PRODUCT_PUBLISH_SUCCESS;
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		} else if (product.getProductStatus().intValue() == ProductInfo.EDIT_STATUS.intValue()) {
			// do publish stuff
			product.setProductStatus(ProductInfo.ONLINE_STATUS.intValue());
			
			visitorProductService.saveProduct(product);
			productRedisService.saveUserProductToRedis(userTemp, product); //save to redis edit status
			
			//save city and product
			productRedisService.saveProductToRedis(product);
			productRedisService.saveProductCityOrderToRedis(product);
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
				//Integer productBaseprice = Integer.valueOf(productBasepriceStr);
				product.setProductBaseprice(productBasepriceStr);
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
		ResultJson rj = new ResultJson();
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FORUPDATE;
		} else {
			// do store and to redis to mongo stuff
			if (!productRedisService.ifContainsPriceKeySet(product.getProductId(), pdt.getAdditionalPriceKeyStr())) {
				ProductMultiPrice pmpT = new ProductMultiPrice();
				
				pmpT.setPmpProductId(product.getProductId());
				pmpT.setPmpProductPriceKey(pdt.getAdditionalPriceKeyStr());
				pmpT.setPmpProductPriceValue(Double.valueOf(pdt.getAdditionalPriceValue()));
				pmpT.setPmpStatus(0);
				
				visitorProductMultiPriceService.saveProductMultiPrice(pmpT);
				productRedisService.saveProductMultiPriceToRedis(pmpT);
				
				rj.setPmpId(pmpT.getPmpId());
			} else {
				ProductMultiPrice pmp = productRedisService.getProductMultiPriceSetByProductIdAndKey(product.getProductId(), pdt.getAdditionalPriceKeyStr());
				pmp.setPmpProductPriceValue(Double.valueOf(pdt.getAdditionalPriceValue()));
				visitorProductMultiPriceService.saveProductMultiPrice(pmp);
				productRedisService.saveProductMultiPriceToRedis(pmp);
				rj.setPmpId(pmp.getPmpId());
			}
			//need to save to redis
		}
		
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("delmultiprice")
	public void productMultiPriceDelete(HttpServletRequest request, 
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
			if (productRedisService.ifContainsPriceKeySet(product.getProductId(), pdt.getAdditionalPriceKeyStr())) {
				ProductMultiPrice pmpT = productRedisService.getProductMultiPriceSetByProductIdAndKey(product.getProductId(), pdt.getAdditionalPriceKeyStr());
				
				pmpT.setPmpStatus(StatusTypeEnum.Inactive.ordinal());
				
				visitorProductMultiPriceService.saveProductMultiPrice(pmpT);
				productRedisService.removePriceKey(product.getProductId(), pdt.getAdditionalPriceKeyStr());
			}
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
		ResultJson rj = new ResultJson();
		
		if (product == null) {
			result = -1;
			resultDesc = ProductInfo.PRODUCT_NOTFOUND_FOR_OPERATION_UPDATE;
		} else {
			String poTypeStr = pot.getPoTypeStr();
			String poStartDateStr = pot.getPoStartDateStr();
			String poEndDateStr = pot.getPoEndDateStr();
			//String poCurrencyStr = pot.getPoCurrencyStr();
			String poPriceStr = pot.getPoPricePerNightStr();
			String poNoticeStr = pot.getPoNoticeStr();
			
			ProductOperation poTemp = new ProductOperation();
			poTemp.setPoProductid(product.getProductId());
			poTemp.setPoCreateby(userTemp.getUserEmail());
			
			poTemp.setPoType(Integer.valueOf(poTypeStr));
			poTemp.setPoCurrency(product.getProductCurrency());
			poTemp.setPoStatus(StatusTypeEnum.Active.ordinal());
			if (StringUtils.isNotEmpty(poPriceStr)) {
				poTemp.setPoPricePerNight(Double.valueOf(poPriceStr));
			} else {
				poTemp.setPoPricePerNight(Double.valueOf(product.getProductBaseprice()));
			}
			poTemp.setPoNotice(poNoticeStr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			try {
				Date poStartDate = sdf.parse(poStartDateStr);
				Date poEndDate = sdf.parse(poEndDateStr);
				
				poTemp.setPoStartDate(poStartDate);
				poTemp.setPoEndDate(poEndDate);
				
				visitorProductOperationService.saveProductOperation(poTemp);
				productRedisService.setProductOperationToRedis(poTemp);
				
				rj.setPoType(poTemp.getPoType());
				rj.setPoPrice(poTemp.getPoPricePerNight());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
			uim.setUimContent(contentStr);
			
			Date date = new Date();
			uim.setUimCreateDate(date);
			
			visitorUserInternalMailService.saveVisitorUserInternalMail(uim);
			userRedisService.setUserInternalMailUnread(uim);
		} else {
			log.info("product Id null for user internal mail save");
			result = -1;
			resultDesc = RegisterInfo.USER_INTERNALMAIL_SAVE_FAIL;
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("getInternalMailUnread")
	public void getUserInternalMailList(HttpServletRequest request,
			HttpServletResponse response,
			Model model) {
		//do internal
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		String userEmailStr = userTemp.getUserEmail();
		
		List<UserInternalMail> listUIM = userRedisService.getUserInternalMailToMe(userEmailStr);
		if (listUIM != null && listUIM.size() > 0) {
			model.addAttribute("internalMailList", listUIM);
		}
	}
	
	@RequestMapping("getMailCount")
	public void countUserInternalMail(HttpServletRequest request,
			HttpServletResponse response) {
		//do internal
		Integer result = 0;
		String resultDesc = RegisterInfo.USER_INTERNALMAIL_SAVE_SUCCESS;
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		if (userTemp != null) {
			result = userRedisService.getUserInternalMailUnreadCount(userTemp.getUserEmail());
		} else {
			result = -1;
			resultDesc = RegisterInfo.USER_NOT_LOGGED_IN;
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
}
