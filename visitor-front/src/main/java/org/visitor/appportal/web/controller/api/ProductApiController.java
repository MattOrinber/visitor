package org.visitor.appportal.web.controller.api;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.app.portal.model.Product;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.web.vo.CategoryOption;
import org.visitor.appportal.web.vo.Product4Json;
import org.visitor.appportal.web.vo.ProductFile4Json;

@Controller
@RequestMapping("/api/")
public class ProductApiController {
	
	protected static final Logger logger = LoggerFactory.getLogger(ProductApiController.class);
	
	private ObjectMapper objectMapper;
	
    @Autowired
    private ProductApiService productApiService;
    
    @Autowired
    private ProductListRepository productListRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private ProductSiteFolderRepository productSiteFolderRepository;
    
	@Autowired
	private SiteRedisRepository siteRedisRepository;
    
	@Autowired
	private SystemPreference systemPreference;
    
    private static String createBy = "admin";
    
    final static String NO_ICON = "API 产品ICON不存在";
    final static String NO_COVER = "API 产品封面图不存在";
    final static String NO_FILE = "API 产品文件不存在";
    final static String NO_APP_FOLDER = "API 绑定频道不存在";
    final static String NO_BIND_FOLDER = "API 没有绑定频道";
    final static String MORE_FOLDER = "API 产品只能绑定一个频道";
    final static String NO_APP_PRODUCT = "API APP产品不存在";
    
    final static String SUCCESS = "API 同步更新成功";
    final static String FAIL = "API 同步更新失败";
    final static String PUBLISH_FAIL = "API 产品发布失败";
	
	/**
	 * 创建产品  & 绑定频道
	 * @return
	 */
	@RequestMapping(value = "product/create", method = POST)
	@ResponseBody
	public String createProduct(@RequestParam(value = "product_j", required = true) String product_j) {
		
		ProductApiReturn<Long> json = new ProductApiReturn<Long>(ProductApiReturn.STATUS_FAIL);

		try {
			if (StringUtils.isNotEmpty(product_j)) {

				// 转换JSON文件
				Product4Json productJson;
				productJson = this.getObjectMapper().readValue(product_j, Product4Json.class);

				if (null != productJson) {
					boolean check = true;
					List<Long> folderIds = productJson.getFolders();
					if (folderIds != null && folderIds.size() > 0) {
							for (Long folder_id : folderIds) {
								Folder f = this.folderRepository.findByFolderId(folder_id);
								if(null == f) {
									json.setMessage(NO_APP_FOLDER);
									check = false;
								}
							}
					} else {
						json.setMessage(NO_BIND_FOLDER);
						check = false;
					}
					
					ProductPic cover = productJson.getCoverPic();
					if (cover != null && StringUtils.isNotEmpty(cover.getPicPath())) {
						File c_f = new File(systemPreference.getProductPicDirectory() + cover.getPicPath());
						if (c_f == null || !c_f.exists()) {
							json.setMessage(NO_COVER);
							check = false;
						}
					} else {
						json.setMessage(NO_COVER);
						check = false;
					}
					
					ProductPic icon = productJson.getIconPic();
					if (icon != null && StringUtils.isNotEmpty(icon.getPicPath())) {
						File i_f = new File(systemPreference.getProductPicDirectory() + icon.getPicPath());
						if (i_f == null || !i_f.exists()) {
							json.setMessage(NO_ICON);
							check = false;
						}
					} else {
						json.setMessage(NO_ICON);
						check = false;
					}
					
					List<ProductFile4Json> pfiles = productJson.getProductFiles();
					if (pfiles == null || pfiles.size() <= 0) {
						json.setMessage(NO_FILE);
						check = false;
					}
					
					if (check) {					
						ProductList productList = this.productApiService.saveProduct(productJson);
						if (productList != null) {
							json.setValue(productList.getProductId());                      //产品ID
							json.setStatus(ProductApiReturn.STATUS_SUCCESS);                //状态 1：成功
							json.setMessage(SUCCESS);
							try {
								productService.setProduct2Redis(productListRepository.findByProductId(productList.getProductId()));
								
								List<ProductSiteFolder> list = productSiteFolderRepository.findByProductId(productList.getProductId());
								if(null != list) {
									for(ProductSiteFolder psf : list) {
										productService.redisAddFolderProduct(psf);
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								json.setMessage(PUBLISH_FAIL);
							}
							
						} else {
							json.setMessage(FAIL);
						}
					}
				}
				
			}
			return URLEncoder.encode(this.getObjectMapper().writeValueAsString(json), "utf-8");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"value\":null,\"status\":1,\"values\":null,\"message\":\"api system error\"}";
		}
	}

	/**
	 * 更新产品信息
	 * @return
	 */
	@RequestMapping(value = "product/update", method = POST)
	@ResponseBody
	public String updateProduct(@RequestParam(value = "product_j", required = true) String product_j) {
		
		ProductApiReturn<Long> json = new ProductApiReturn<Long>(ProductApiReturn.STATUS_FAIL);

		try {

			// 转换JSON文件
			Product4Json productJson;
			productJson = this.getObjectMapper().readValue(product_j, Product4Json.class);

			Long productId = productJson.getProduct().getProductId();
			if (productId != null) {
				
				ProductList prd = productListRepository.findByProductId(productId);
				if (prd != null ) {
					
					boolean check = true;
					List<Long> folderIds = productJson.getFolders();
					if (folderIds != null && folderIds.size() > 0) {
							for (Long folder_id : folderIds) {
								Folder f = this.folderRepository.findByFolderId(folder_id);
								if(null == f) {
									json.setMessage(NO_APP_FOLDER);
									check = false;
								}
							}
					} else {
						json.setMessage(NO_BIND_FOLDER);
						check = false;
					}
					
					ProductPic cover = productJson.getCoverPic();
					if (cover != null && StringUtils.isNotEmpty(cover.getPicPath())) {
						File c_f = new File(systemPreference.getProductPicDirectory() + cover.getPicPath());
						if (c_f == null || !c_f.exists()) {
							json.setMessage(NO_COVER);
							check = false;
						}
					} else {
						json.setMessage(NO_COVER);
						check = false;
					}
					
					ProductPic icon = productJson.getIconPic();
					if (icon != null && StringUtils.isNotEmpty(icon.getPicPath())) {
						File i_f = new File(systemPreference.getProductPicDirectory() + icon.getPicPath());
						if (i_f == null || !i_f.exists()) {
							json.setMessage(NO_ICON);
							check = false;
						}
					} else {
						json.setMessage(NO_ICON);
						check = false;
					}
					
					List<ProductFile4Json> pfiles = productJson.getProductFiles();
					if (pfiles == null || pfiles.size() <= 0) {
						json.setMessage(NO_FILE);
						check = false;
					}
					
					if (check) {
						prd = this.productApiService.saveProduct(productJson);
						
						prd.setDownStatus(ProductList.ENABLE);
						productListRepository.save(prd);
						
						json.setValue(prd.getProductId());
						json.setStatus(ProductApiReturn.STATUS_SUCCESS);
						json.setMessage(SUCCESS);
						try {
							productService.setProduct2Redis(productListRepository.findByProductId(prd.getProductId()));
							
							List<ProductSiteFolder> list = productSiteFolderRepository.findByProductId(prd.getProductId());
							if(null != list) {
								for(ProductSiteFolder psf : list) {
									productService.redisAddFolderProduct(psf);
								}
							}
							
							List<String> productIds = new ArrayList<String>();
							productIds.add(String.valueOf(prd.getProductId()));
							this.siteRedisRepository.sendCacheElementMessage(Product.class.getName(), productIds);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							json.setMessage(PUBLISH_FAIL);
						}
					}					
				} else {
					json.setMessage(NO_APP_PRODUCT);
					json.setStatus(ProductApiReturn.STATUS_FAIL);   //产品不存在
				}
			}
			
			return URLEncoder.encode(this.getObjectMapper().writeValueAsString(json), "utf-8");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"value\":null,\"status\":1,\"values\":null,\"message\":\"api system error\"}";
		}
	}

	/**
	 * 产品分类
	 * @return
	 */
	@RequestMapping(value = "product/category", method = POST)
	@ResponseBody
	public String categoryList() {

		ProductApiReturn<CategoryOption> json = new ProductApiReturn<CategoryOption>(ProductApiReturn.STATUS_FAIL);

		List<CategoryOption> categoryOption = categoryService
				.findCategorySelectTree(Category.PRODUCT_CATEGORY);
		try {
			if (categoryOption != null) {
				json.setValues(categoryOption);
				json.setStatus(ProductApiReturn.STATUS_SUCCESS);
				String value = this.getObjectMapper().writeValueAsString(json);
				return URLEncoder.encode(value, "utf-8");
			}

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

		return null;
	}
	
	/**
	 * 更新产品状态（0：上架    1：下架）
	 * 可批量
	 * @return
	 */
	@RequestMapping(value = "product/status", method = POST)
	@ResponseBody
	public String updateStatus(
			@RequestParam(value = "productid", required = true) Long productId,
			@RequestParam(value = "status", required = true) Integer status,
			@RequestParam(value = "reason", required = false) String reason) {

		ProductApiReturn<Long> json = new ProductApiReturn<Long>(ProductApiReturn.STATUS_FAIL);

		if (productId != null) {
			try {
				ProductList productList = productListRepository.findByProductId(productId);
				if (productList != null) {

					productList.setDownStatus(status);
					productList.setDownReason(reason);

					Date date = new Date();
					productList.setModDate(date);
					productList.setModBy("admin");

					productListRepository.save(productList);
					// 同步redis，搜索
					productService.setProduct2Redis(productList);

					json.setStatus(ProductApiReturn.STATUS_SUCCESS);
					json.setValue(productList.getProductId());


				} else {
					json.setMessage(NO_APP_PRODUCT);
					json.setStatus(ProductApiReturn.STATUS_FAIL);
				}

				return URLEncoder.encode(this.getObjectMapper().writeValueAsString(json), "utf-8");
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

		return null;
	}
	
	/**
	 * 创建字典信息
	 * @return
	 */
	@RequestMapping(value = "category/create", method = POST)
	@ResponseBody
	public String createCategoryCp(@RequestParam(value = "parentid", required = true) Long parentId,
			@RequestParam(value = "name", required = true) String name) {

		ProductApiReturn<Long> json = new ProductApiReturn<Long>(ProductApiReturn.STATUS_FAIL);

		Category category = new Category();

		try {
			if (StringUtils.isNotEmpty(name)) {
				Date date = new Date();
				category.setName(name);
				category.setCreateDate(date);
				category.setModDate(date);
				category.setModBy(createBy);
				category.setCreateBy(createBy);
				category.setParentCategory(categoryRepository.findOne(parentId));
				categoryService.saveNode(category);

				json.setValue(category.getCategoryId());
				json.setStatus(ProductApiReturn.STATUS_SUCCESS);
				json.setMessage("API 字典创建成功");
				
			} else {
				json.setMessage("API 名称不正确");
			}

			return this.getObjectMapper().writeValueAsString(json);

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

		return null;
	}
		
	/**
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		if(null == objectMapper) {
			this.objectMapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(df);
			objectMapper.getDeserializationConfig().disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		return objectMapper;
	}

}
