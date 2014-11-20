package org.visitor.appportal.web.controller.common;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorProductService;
import org.visitor.appportal.visitor.beans.ProductTemp;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.web.utils.WebInfo;

@Controller
@RequestMapping("/product/")
public class ProductController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private VisitorProductService visitorProductService;
	
	@RequestMapping("create")
	public void createProduct(HttpServletRequest request, 
			HttpServletResponse response) {
		ProductTemp pt = super.getProductTempJson(request);
		
		Integer homeType = Integer.valueOf(pt.getProductHomeTypeStr());
		Integer roomType = Integer.valueOf(pt.getProductRoomTypeStr());
		Integer accomodates = Integer.valueOf(pt.getProductAccomodatesStr());
		String city = pt.getProductCityStr();
		
		Product product = new Product();
		
		product.setProductHometype(homeType);
		product.setProductRoomType(roomType);
		product.setProductAccomodates(accomodates);
		product.setProductCity(city);
		
		Date newDate = new Date();
		
		product.setProductCreateDate(newDate);
		product.setProductUpdateDate(newDate);
		
		Long userIdTemp = (Long) request.getAttribute(WebInfo.UserID);
		product.setProductPublishUserId(userIdTemp);
		
		// redis need to store z-set
		;
		
		visitorProductService.saveProduct(product);
	}
}
