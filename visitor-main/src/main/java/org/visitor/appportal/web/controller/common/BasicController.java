package org.visitor.appportal.web.controller.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.visitor.appportal.service.newsite.VisitorArticleService;
import org.visitor.appportal.visitor.beans.ArticleTemp;
import org.visitor.appportal.visitor.beans.ContainerTemp;
import org.visitor.appportal.visitor.beans.FloopyTemp;
import org.visitor.appportal.visitor.beans.PayTemp;
import org.visitor.appportal.visitor.beans.ProductAddressTemp;
import org.visitor.appportal.visitor.beans.ProductDetailTemp;
import org.visitor.appportal.visitor.beans.ProductPriceMultiTemp;
import org.visitor.appportal.visitor.beans.ProductTemp;
import org.visitor.appportal.visitor.beans.UserTemp;
import org.visitor.appportal.visitor.domain.Article;
import org.visitor.appportal.visitor.domain.City;
import org.visitor.appportal.visitor.domain.Container;
import org.visitor.appportal.visitor.domain.FloopyThing;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductOrder;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.controller.service.PageActivitiesService;
import org.visitor.appportal.web.controller.service.PageCityService;
import org.visitor.appportal.web.controller.service.PageConstantService;
import org.visitor.appportal.web.controller.service.PageOrdersService;
import org.visitor.appportal.web.controller.service.PageRecommendService;
import org.visitor.appportal.web.controller.service.PageUserService;
import org.visitor.appportal.web.mailutils.SendMailUtils;
import org.visitor.appportal.web.mailutils.UserMailException;
import org.visitor.appportal.web.utils.WebInfo;

import com.alibaba.fastjson.JSON;

public class BasicController {
	protected static final Logger log = LoggerFactory.getLogger(BasicController.class);
	private ObjectMapper objectMapper;
	
	@Autowired
	private PageRecommendService pageRecommendService;
	@Autowired
	private PageOrdersService pageOrdersService;
	@Autowired
	private PageActivitiesService pageActivitiesService;
	@Autowired
	private PageCityService pageCityService;
	@Autowired
	private PageConstantService pageConstantService;
	@Autowired
	private PageUserService pageUserService;
	@Autowired
	private VisitorArticleService visitorArticleService;
	
	public BasicController() {	
	}
	
	private String getJsonStr(HttpServletRequest request) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream in;
		try {
			in = request.getInputStream();
			byte[] buf = new byte[1024];
			for (;;) {
				int len = in.read(buf);
				if (len == -1) {
					break;
				}

				if (len > 0) {
					baos.write(buf, 0, len);
				}
			}
			if (baos.size() <= 0)
			{
				return null;
			}
			byte[] bytes = baos.toByteArray();
			String originStr = new String(bytes);
			
			return originStr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public UserTemp getUserJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			UserTemp userT = JSON.parseObject(originStr, UserTemp.class);
			return userT;
		}

		return null;
	}
	
	public FloopyTemp getFloopyJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			FloopyTemp floopyT = JSON.parseObject(originStr, FloopyTemp.class);
			return floopyT;
		}
		
		return null;
	}
	
	public ContainerTemp getContainerJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ContainerTemp containerT = JSON.parseObject(originStr, ContainerTemp.class);
			return containerT;
		}
		
		return null;
	}
	
	public ArticleTemp getArticleJSON(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ArticleTemp articleT = JSON.parseObject(originStr, ArticleTemp.class);
			return articleT;
		}
		
		return null;
	}
	
	public PayTemp getPayJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			PayTemp payT = JSON.parseObject(originStr, PayTemp.class);
			return payT;
		}
		
		return null;
	}
	
	public ProductTemp getProductTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ProductTemp productT = JSON.parseObject(originStr, ProductTemp.class);
			return productT;
		}
		
		return null;
	}
	
	public ProductDetailTemp getProductDetailTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ProductDetailTemp productDT = JSON.parseObject(originStr, ProductDetailTemp.class);
			return productDT;
		}
		
		return null;
	}
	
	public ProductAddressTemp getProductAddressTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ProductAddressTemp productAT = JSON.parseObject(originStr, ProductAddressTemp.class);
			return productAT;
		}
		
		return null;
	}
	
	public ProductPriceMultiTemp getProductPriceMultiTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ProductPriceMultiTemp productPMT = JSON.parseObject(originStr, ProductPriceMultiTemp.class);
			return productPMT;
		}
		
		return null;
	}
	
	//send JSON response utility
	public void sendJSONResponse(Object obj, HttpServletResponse response) {
		try {
			String resultJsonStr = this.getObjectMapper().writeValueAsString(obj);
			PrintWriter out = response.getWriter();
			out.print(resultJsonStr);
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void logTheJsonResult(Object obj) {
		if (log.isInfoEnabled()) {
			String resultJsonStr;
			try {
				resultJsonStr = this.getObjectMapper().writeValueAsString(obj);
				log.info("json String result: >" + resultJsonStr + "<");
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the objectMapper
	 */
	private ObjectMapper getObjectMapper() {
		if(null == objectMapper) {
			this.objectMapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(df);
			objectMapper.getDeserializationConfig().disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		return objectMapper;
	}
	
	
	//send email utility
	public void sendEmail(String title, String content, String toAddress) {
		SendMailUtils sendMU = new SendMailUtils();
		sendMU.setTitle(title);
		sendMU.setContent(content);
		sendMU.setToAddress(toAddress);
		try
		{
			sendMU.sendEmailHtml();
		} catch (UserMailException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (log.isInfoEnabled()) {
				log.info("sendmail exception: >"+e.getMessage()+"<");
			}
		}
	}
	
	protected void setPageModel(HttpServletRequest request, Integer pageType, Model model) {
		Long itemCount = null;
		switch (pageType) {
		case 0:
			itemCount = pageUserService.getUserCount();
			break;
		case 1:
			itemCount = pageConstantService.getFloopyCount();
			break;
		case 2:
			itemCount = pageCityService.getCityCount();
			break;
		case 3:
			itemCount = pageRecommendService.getContainerCount();
			break;
		case 4:
			itemCount = pageActivitiesService.getActivitiesCount();
			break;
		case 5:
			itemCount = pageOrdersService.countOrders();
			break;
		case 6:
			itemCount = visitorArticleService.countArticle();
			break;
		case 7:
			setUserModel(request, model);
			break;
		case 8:
			break;
		case 9:
			break;
		case 10:
			setArticleModel(request, model);
			break;
		}
		
		if (itemCount != null && itemCount.longValue() > 0) {
			model.addAttribute("itemCount", itemCount);
			Long pageNum = itemCount/WebInfo.pageSize;
			Long residue = itemCount%(WebInfo.pageSize);
			if (residue.longValue() > 0) {
				pageNum += 1;
			}
			
			model.addAttribute("pageNumber", pageNum);
			
			Long pageIdx = null;
			String pStr = request.getParameter("p");
			if (StringUtils.isNotEmpty(pStr)) {
				pageIdx = Long.valueOf(pStr);
			} else {
				pageIdx = 1L;
			}
			model.addAttribute("pageIndex", pageIdx);
			
			if (pageIdx == pageNum) {
				model.addAttribute("ifEnd", 2); //页尾
			} else if (pageIdx.longValue() == 1) {
				model.addAttribute("ifEnd", 0); //页头
			} else {
				model.addAttribute("ifEnd", 1);
			}
			
			switch (pageType) {
			case 0:
				List<User> itemList = pageUserService.getUserList(pageIdx);
				model.addAttribute("itemList", itemList);
				break;
			case 1:
				List<FloopyThing> floopyList = pageConstantService.getPagedFloopies(pageIdx);
				model.addAttribute("itemList", floopyList);
				break;
			case 2:
				List<City> cityList = pageCityService.getPagedCities(pageIdx);
				model.addAttribute("itemList", cityList);
				break;
			case 3:
				List<Container> containerList = pageRecommendService.getPagedContainers(pageIdx);
				model.addAttribute("itemList", containerList);
				break;
			case 4:
				List<Product> productList = pageActivitiesService.getPagedProducts(pageIdx);
				model.addAttribute("itemList", productList);
				break;
			case 5:
				List<ProductOrder> orderList = pageOrdersService.getPagedOrders(pageIdx);
				model.addAttribute("itemList", orderList);
				break;
			case 6:
				List<Article> articleList = visitorArticleService.getArticleByPage(pageIdx);
				model.addAttribute("itemList", articleList);
				break;
			}
		}
		
		model.addAttribute("pageType", pageType);
	}

	private void setUserModel(HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub
		String emailStr = request.getParameter("mailStr");
		if (StringUtils.isNotEmpty(emailStr)) {
			User user = pageUserService.getUserByEmail(emailStr);
			model.addAttribute("userInfo", user);
		}
	}

	private void setArticleModel(HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub
		String nameStr = request.getParameter("name");
		if (StringUtils.isNotEmpty(nameStr)) {
			Article art = visitorArticleService.getArticleByName(nameStr);
			model.addAttribute("article", art);
		}
	}
}
