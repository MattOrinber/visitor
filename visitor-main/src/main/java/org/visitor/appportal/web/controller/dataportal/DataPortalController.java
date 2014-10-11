package org.visitor.appportal.web.controller.dataportal;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.alibaba.fastjson.JSON;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductPicRepository;

/**
 * 对外数据接口
 * 
 * @author mengw
 * 
 */
@Controller
@RequestMapping("/dataportal/")
public class DataPortalController {

	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private ProductPicRepository productPicRepository;

	@RequestMapping(value = "productlist/productlistinfo", method = RequestMethod.GET)
	public String productListInfo(HttpServletRequest request) {
		String productId = request.getParameter("pid");
		ProductList productList = productListRepository.findByProductId(Long
				.parseLong(productId));
		StringBuffer resultSB = new StringBuffer(productId).append("#")
				.append(productList.getName()).append("#")
				.append(productList.getDownStatus());
		request.setAttribute("productList", resultSB.toString());
		return "dataportal/productlist/productlistinfo";
	}

	@RequestMapping(value = "productlist/pic", method = RequestMethod.GET)
	public String productListPic(HttpServletRequest request) {
		String productId = request.getParameter("pid");
		List<ProductPic> productPics = productPicRepository
				.findByProductIdAndPicType(Long.parseLong(productId),
						ProductPic.ICON);
		StringBuffer resultSB = new StringBuffer(productId).append("#").append(
				productPics.get(0).getPicPath());
		request.setAttribute("productPic", resultSB.toString());
		return "dataportal/productlist/pic";
	}

}
