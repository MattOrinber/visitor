package org.visitor.appportal.web.controller.common;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorProductService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.visitor.beans.ProductDetailTemp;
import org.visitor.appportal.visitor.beans.ProductTemp;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.utils.ProductInfo;
import org.visitor.appportal.web.utils.WebInfo;

@Controller
@RequestMapping("/product/")
public class ProductController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private VisitorProductService visitorProductService;
	@Autowired
	private ProductRedisService productRedisService;
	
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
		
		visitorProductService.saveProduct(product);
		productRedisService.saveUserProductToRedis(userTemp, product);
		
		ResultJson rj = new ResultJson();
		
		rj.setResult(0);
		rj.setResultDesc(ProductInfo.PRODUCT_CREATE_SUCCESS);
		rj.setProductId(product.getProductId());
		
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
			
			product.setProductStatus(ProductInfo.ONLINE_STATUS);
			
			visitorProductService.saveProduct(product);
			productRedisService.saveUserProductToRedis(userTemp, product);
			productRedisService.saveProductToRedis(product); //online
			
			//mongo part;
			;
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
}
