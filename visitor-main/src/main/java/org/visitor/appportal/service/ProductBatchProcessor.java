/**
 * 
 */
package org.visitor.appportal.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.visitor.app.portal.model.Product;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.MessageContent;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.redis.CategoryRedisRepository;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.web.utils.EncryptionUtil;
import org.visitor.appportal.web.vo.Product4Json;
import org.visitor.appportal.web.vo.ProductFile4Json;
import org.visitor.appportal.web.vo.ProductFileSuite4Json;
import org.visitor.util.AppStringUtils;
import org.visitor.util.FileUtil;

import com.alibaba.fastjson.JSON;

/**
 * @author mengw
 *
 */
@Service
public class ProductBatchProcessor {

	protected static final Logger logger = LoggerFactory.getLogger(ProductBatchProcessor.class);
	
	public static final String JSON_FILE = "product.json";
	public static final String COVER_IMG = "img1";
	public static final String ICON_IMG = "img2";
	public static final String ILLU_IMG = "img3";
	public static final String FILES = "files";
	
	private static final String copyImgLogFile = "copyImg.log";
	private static final String copyFileLogFile = "copyFile.log";
	private static final String noImg1LogFile = "noimg1.log";
	private static final String noImg2LogFile = "noimg2.log";
	private static final String noImg3LogFile = "noimg3.log";
	private static final String parseJsonLogFile = "parser.log";
	private static final String validationLogFile = "validate.log";
	private static final String invalideProduct = "invalide-product.log";
	
	private ObjectMapper objectMapper;
	@Autowired
	private SearchIndexService searchService;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private ValidatorFactory validatorFactory;
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private SystemPreference systemPreference;
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private ProductRedisRepository productRedisRepository;
	@Autowired
	private CategoryRedisRepository categoryRedisRepository;
	
	private String createBy = "admin";
	private String importBy = "自动导入";
	
	/**
	 * 
	 */
	public ProductBatchProcessor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 更新产品文件。为已经入库的产品补产品文件。
	 * @param product
	 * @param file
	 */
	public void updateProductFiles(Product4Json product4Json, File productFolder) {
		//Load original prd if exists.
//		ProductList prd = productListRepository.findByOperatorIdAndProductSourceId(
//				product4Json.getProduct().getOperatorId(), product4Json.getProduct().getProductSourceId());
		ProductList prd = new ProductList();
		Long productId = product4Json.getProduct().getProductId();
		if (productId != null) {
			prd = productListRepository.findByProductId(productId);
		} else {
			prd = null;
		}
		if(null == prd) {//The product is NOT import to current database.
			this.createProduct(product4Json, productFolder);
		} else {
			Map<String, ProductFile> picMap = getProductFilesMap(product4Json.getProductFiles(), 
					productFolder, product4Json.getProduct().getOperatorId());
			this.productService.updateProductFiles(prd, picMap);
			
			this.copyProductFiles(productFolder, picMap);
			//更新redis
			List<ProductFile> productFiles = new ArrayList<ProductFile>();
			productFiles.addAll(picMap.values());
			this.productRedisRepository.setProductFiles(prd.getProductId(), productFiles);
			
			//更新产品版本
			if (productFiles != null && productFiles.size() > 0) {
				int versionCode = 0;
				String versionName = "";
				for (ProductFile pf : productFiles) {
					if (pf.getVersionCode() > versionCode && StringUtils.isNotEmpty(pf.getVersionName())) {
						versionCode = pf.getVersionCode();
						versionName = pf.getVersionName();
					}
				}
				if (StringUtils.isNotEmpty(versionName)) {
					prd.setProductVersion(versionName);
					productListRepository.save(prd);
				}
			}
			
			//更新solr
			Product product = productRedisRepository.getProductByProductId(prd.getProductId(), false);
			product.setProductFiles(productFiles);
			searchService.createIndex(product);
		}
	}
	
	/**
	 * 为产品补录评论。
	 * @param product4Json
	 */
	public void updateProductComment(Product4Json product4Json, File productFolder) {
		//Load original prd if exists.
//		ProductList prd = productListRepository.findByOperatorIdAndProductSourceId(
//		product4Json.getProduct().getOperatorId(), product4Json.getProduct().getProductSourceId());
		ProductList prd = new ProductList();
		Long productId = product4Json.getProduct().getProductId();
		if (productId != null) {
			prd = productListRepository.findByProductId(productId);
		} else {
			prd = null;
		}
		List<MessageContent> comments = product4Json.getComments();

		if(null != prd && null != comments && comments.size() > 0) {
			this.productService.updateProductComments(prd, comments);
			//更新redis。
			this.productRedisRepository.setProductComments(prd.getProductId(), comments);
		} else {
			logger.info("Not saved productId: " + product4Json.getProduct().getProductSourceId());
		}
	}
	
	/**
	 * 重新关联产品与频道对应
	 * @param product
	 * @param productFolder
	 */
	public void updateProductFolder(Product4Json product4Json, File productFolder) {
		//Load original prd if exists.
//		ProductList prd = productListRepository.findByOperatorIdAndProductSourceId(
//		product4Json.getProduct().getOperatorId(), product4Json.getProduct().getProductSourceId());
		ProductList prd = new ProductList();
		Long productId = product4Json.getProduct().getProductId();
		if (productId != null) {
			prd = productListRepository.findByProductId(productId);
		} else {
			prd = null;
		}
		if(null == prd) {//The product is NOT import to current database.
			this.createProduct(product4Json, productFolder);
		} else {
			if(null != product4Json.getFolders() && product4Json.getFolders().size() > 0) {
				List<Folder> list = new ArrayList<Folder>();
				for(Long id : product4Json.getFolders()) {
					Folder f = this.folderRepository.findOne(id);
					if(null != f) {
						f.setSite(this.siteRepository.findOne(f.getSiteId()));
						list.add(f);
					}
				}
				List<ProductSiteFolder> siteFolders = productService.updateProductFolders(prd, list);
				Product product = productRedisRepository.getProductByProductId(prd.getProductId(), false);
				product.setFolders(siteFolders);
				this.searchService.createIndex(product);
			}
		}
	}
	
	/**
	 * 为已经入库的产品补产品图片。如果产品没有入库，则重新入库。
	 * @param product4Json
	 * @param productFolder 产品文件及信息所在目录。默认为产品ID。
	 */
	public void updateProductImage(Product4Json product4Json, File productFolder) {
		//Load original prd if exists.
//		ProductList prd = productListRepository.findByOperatorIdAndProductSourceId(
//		product4Json.getProduct().getOperatorId(), product4Json.getProduct().getProductSourceId());
		ProductList prd = new ProductList();
		Long productId = product4Json.getProduct().getProductId();
		if (productId != null) {
			prd = productListRepository.findByProductId(productId);
		} else {
			prd = null;
		}
		if(null == prd) {//The product is NOT import to current database.
			this.createProduct(product4Json, productFolder);
		} else {
			Map<String, ProductPic> picMap = getProductImagesMap(product4Json);
			List<ProductPic> pps = this.productService.updateProductIlluImages(prd, picMap);
			Product product = this.productRedisRepository.getProductByProductId(prd.getProductId(), false);
			List<ProductPic> pics = new ArrayList<ProductPic>();
			if (pps != null && pps.size() > 0) {
				for(ProductPic productPic : pps) {
					if(null != productPic) {
						if(productPic.getPicType().intValue() != ProductPic.PRINT_SCREEN) {//不是截图
							if(productPic.getPicType().intValue() == ProductPic.COVER) {//封面
								product.setCoverPic(productPic);
							} else if(productPic.getPicType().intValue() == ProductPic.ICON) {//图标
								product.setIconPic(productPic);
							}
						} else {
							pics.add(productPic);
						}
					}
				}
			}
			copyProductImages(productFolder, picMap);
			//可能封面图或者截图发生了改变，重新更新redis和solr
			this.productRedisRepository.setProduct(prd.getProductId(), product);
			productRedisRepository.setProductPics(prd.getProductId(), pics);
			this.searchService.createIndex(product);
		}
	}

	public void updateProductInfo(Product4Json product4Json, File productFolder) {
		//Load original prd if exists.
//		ProductList prd = productListRepository.findByOperatorIdAndProductSourceId(
//		product4Json.getProduct().getOperatorId(), product4Json.getProduct().getProductSourceId());
		ProductList prd = new ProductList();
		Long productId = product4Json.getProduct().getProductId();
		if (productId != null) {
			prd = productListRepository.findByProductId(productId);
		} else {
			prd = null;
		}
		if(null == prd) {//The product is NOT import to current database.
			this.createProduct(product4Json, productFolder);
		} else {
			prd.setName(product4Json.getProduct().getName());
			prd.setPrice(product4Json.getProduct().getPrice());
			prd.setDescription(product4Json.getProduct().getDescription());
			prd.setProductVersion(product4Json.getProduct().getProductVersion());
			if(StringUtils.isNotBlank(product4Json.getProduct().getTagLine())) {
				prd.setTagLine(product4Json.getProduct().getTagLine());
			}
			prd.setImportBy("纪颖");
			
			productListRepository.save(prd);
			Product product = productRedisRepository.getProductByProductId(prd.getProductId(), false);
			product.setProductList(prd);
			productRedisRepository.setProduct(prd.getProductId(), product);
		}
	}

	public void createProduct(Product4Json product4Json, File productFolder) {
		//Load original prd if exists.
//		ProductList prd = productListRepository.findByOperatorIdAndProductSourceId(
//		product4Json.getProduct().getOperatorId(), product4Json.getProduct().getProductSourceId());
		ProductList prd = new ProductList();
		Long productId = product4Json.getProduct().getProductId();
		if (productId != null) {
			prd = productListRepository.findByProductId(productId);
		} else {
			prd = null;
		}
		Map<String, ProductPic> picMap = getProductImagesMap(product4Json);
		Map<String, ProductFile> productFiles = getProductFilesMap(product4Json.getProductFiles(), 
				productFolder, product4Json.getProduct().getOperatorId());
		List<ProductSiteFolder> productSites = getProductSiteFolders(prd, product4Json.getFolders());
		List<MessageContent> comments = product4Json.getComments();
		if(null == prd) {
			prd = product4Json.getProduct().copy();
			prd.initDefaultValues();
			prd.setCreateBy(createBy);
			prd.setModBy(createBy);
			//prd.setImportBy(createBy);
			//Set the operator value.
			prd.setOperator(categoryRepository.findOne(prd.getOperatorId()));
			String starLevel = prd.getStarLevel();
			if(StringUtils.length(starLevel) > 7) {
				prd.setStarLevel(StringUtils.substring(starLevel, 0, 3));
			} else if (StringUtils.isEmpty(starLevel)) {
				prd.setStarLevel("2.5");
			}
		} else {//Copy updated properties value.
			prd.setName(product4Json.getProduct().getName());
			prd.setPrice(product4Json.getProduct().getPrice());
			prd.setDescription(product4Json.getProduct().getDescription());
			prd.setProductVersion(product4Json.getProduct().getProductVersion());
			prd.setCategoryId(product4Json.getProduct().getCategoryId());
			if(StringUtils.isEmpty(prd.getTagLine()) && StringUtils.isNotBlank(product4Json.getProduct().getTagLine())) {
				prd.setTagLine(product4Json.getProduct().getTagLine());
			}
			prd.setOperator(categoryRepository.findOne(product4Json.getProduct().getOperatorId()));
		}
		if(prd.getBillingTypeId().intValue() == 1) {
			prd.setBillingTypeId(344l);
		}
		prd.setModDate(new Date());
		prd.setModBy(createBy);

		validateProductInfor(prd, comments, productFolder.getParentFile());
		
		if(product4Json.getIconPic() == null || product4Json.getProductFiles() == null || product4Json.getProductFiles().size() == 0) {
			if(logger.isDebugEnabled()) {
				logger.debug("No icon or product files: " + productFolder.getName());
			}
			try {
				FileUtil.writeStringToFile(new File(productFolder.getParent(), invalideProduct), 
						productFolder.getName() + "\n\r", "gbk", true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			Product product = productService.createProductWithImgAndFiles(prd, picMap, productFiles, productSites, comments);
			if(null != product) {
				try {
					copyProductImages(productFolder, picMap);
					copyProductFiles(productFolder, productFiles);
					//更新redis以及solr
					this.productService.setProduct2Redis(product.getProductList());					

				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public long getResolutionId() {
		return 5l;
	}
	
	private Map<String, ProductPic> getProductImagesMap(Product4Json product4Json) {
		Map<String, ProductPic> map = new HashMap<String, ProductPic>();
		ProductPic cover = product4Json.getCoverPic();
		cover.setProduct(product4Json.getProduct());
		cover.setPicType(ProductPic.COVER);
		map.put(product4Json.getCoverPic().getPicPath(), cover);
		
		ProductPic icon = product4Json.getIconPic();
		icon.setProduct(product4Json.getProduct());
		icon.setPicType(ProductPic.ICON);
		map.put(product4Json.getIconPic().getPicPath(), icon);

		
		if(null != product4Json.getIlluPics()) {
			for(ProductPic pic : product4Json.getIlluPics()) {
				pic.setProduct(product4Json.getProduct());
				pic.setPicType(ProductPic.PRINT_SCREEN);
				map.put(pic.getPicPath(), pic);
			}
		}
		return map;
	}

	
	public Product4Json getProductInfor(File file) {
		File src = new File(file, JSON_FILE);
		if (src != null) {

			String content = null;
			try {
				content = FileUtils.readFileToString(src, "GBK");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Product4Json product4Json = null;
			try {
				product4Json = this.getObjectMapper().readValue(content, Product4Json.class);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					FileUtil.writeStringToFile(new File(file.getParent(), parseJsonLogFile), 
							file.getName() + "\n\r", "gbk", true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			if(null != product4Json) {
				List<ProductPic> coverList = this.getProductPicList(file, COVER_IMG);
				product4Json.setCoverPic(coverList == null || coverList.size() == 0 ? null : coverList.get(0));
				coverList = this.getProductPicList(file, ICON_IMG);
				product4Json.setIconPic(coverList == null || coverList.size() == 0 ? null : coverList.get(0));
				
				if(null == product4Json.getCoverPic()) {
					product4Json.setCoverPic(product4Json.getIconPic());
					try {
						FileUtil.writeStringToFile(new File(file.getParent(), noImg1LogFile), 
								file.getName() + "\n\r", "gbk", true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if(null == product4Json.getIconPic()) {
					product4Json.setIconPic(product4Json.getCoverPic());
					try {
						FileUtil.writeStringToFile(new File(file.getParent(), noImg2LogFile), 
								file.getName() + "\n\r", "gbk", true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
				List<ProductPic> pics = this.getProductPicList(file, ILLU_IMG);
				product4Json.setIlluPics(pics);
				if(null == product4Json.getIlluPics() || product4Json.getIlluPics().size() == 0) {
					try {
						FileUtil.writeStringToFile(new File(file.getParent(), noImg3LogFile), 
								file.getName() + "\n\r", "gbk", true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}				
				}
				
				if(StringUtils.isBlank(product4Json.getProduct().getImportBy())) {
					product4Json.getProduct().setImportBy(importBy);
				}
				return product4Json;
			}
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
	
	private Map<String, ProductFile> getProductFilesMap(List<ProductFile4Json> productFiles, File productFolder, Long cpId) {
		Map<String, ProductFile> map = new HashMap<String, ProductFile>();
		List<Long> cpIds = categoryRedisRepository.getConfigure4ExternalUrlCpIds();
		for(ProductFile4Json json : productFiles) {
			ProductFile file = json.toProductFile();
			file.initDefaultValues();
			file.setCreateBy(createBy);
			file.setModBy(createBy);
			
			file.setOs(categoryRepository.findOne(json.getOsId()));
			file.setPlatform(categoryRepository.findOne(json.getPlatformId()));
			List<Category> list = categoryRepository.findByParentCategoryIdAndName(json.getPlatformId(), json.getMinVersion().trim());
			file.setPlatformVersion(null != list && list.size() > 0 ? list.get(0) : null);
			file.setResolution(this.getMinScreenSize(json.getResolutions()));
			
			if(null != cpIds && cpIds.contains(cpId)) {//The download url will redirect to external url.
				map.put(file.getFileUrl(), file);
			} else {//The resource file are downloaded to local disk.
				File realdFile = new File(productFolder + "/" + FILES, file.getFileName());
				file.setFilePath(realdFile.getAbsolutePath());
				if(realdFile.exists() && !realdFile.isDirectory()) {
					try {
						if(StringUtils.equalsIgnoreCase(FilenameUtils.getExtension(realdFile.getName()), "jad")) {
							file.setJadMd5(EncryptionUtil.getMD5(FileUtils.openInputStream(realdFile)));
							//Read jar File md5
							File jarFile = new File(realdFile.getParent(), FilenameUtils.getBaseName(file.getFileName()) + ".jar");
							file.setJarMd5(EncryptionUtil.getMD5(FileUtils.openInputStream(jarFile)));
							file.setFileSize(AppStringUtils.byteCountToDisplaySize(jarFile.length()));
						} else {//Only read one file
							file.setJarMd5(EncryptionUtil.getMD5(FileUtils.openInputStream(realdFile)));
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					//Only save existed files.
					map.put(file.getFilePath(), file);
				}
			}
		}
		return map;
	}

	private List<ProductSiteFolder> getProductSiteFolders(ProductList productList, List<Long> folders) {
		List<ProductSiteFolder> list = new ArrayList<ProductSiteFolder>();
		if(null != folders) {
			//Only process NOT imported product
			if(null == productList || null == productList.getPrimaryKey()) {//NOT exists yes
				Map<Integer, FolderBean> siteFolder = new HashMap<Integer, FolderBean>();
				for(Long folderId : folders) {
					Folder f = this.folderRepository.findOne(folderId);
					if(null != f) {
						if(null != siteFolder.get(f.getSiteId())) {
							siteFolder.get(f.getSiteId()).setExisted(true);
						} else {
							siteFolder.put(f.getSiteId(), new FolderBean(f, false));
						}
					}
				}
				//Generate ProductSiteFolder
				for(Map.Entry<Integer, FolderBean> entry : siteFolder.entrySet()) {
					if(!entry.getValue().existed) {
						ProductSiteFolder e = new ProductSiteFolder();
						e.initDefaultValues();
						e.setCreateBy(createBy);
						e.setFolder(entry.getValue().folder);
						e.setSite(siteRepository.findOne(entry.getValue().folder.getSiteId()));
						list.add(e);
					}
				}
			}
		}
		return list;
	}
	
	private Category getMinScreenSize(List<ProductFileSuite4Json> resolutions) {
		long value = 0;
		String resolution = "";
		for(ProductFileSuite4Json json : resolutions) {
            String[] array = StringUtils.split(StringUtils.upperCase(json.getResolution()), "X");
            if(null != array && array.length == 2) {
            	if(StringUtils.isNumeric(array[0]) && StringUtils.isNumeric(array[1])) {
            		long current = (Long.valueOf(Integer.valueOf(array[0]) * Integer.valueOf(array[1])));
            		if(current < value || value == 0) {
            			value = current;
            			resolution = json.getResolution();
            		}
            	}
            }
		}
		List<Category> list = categoryRepository.findByParentCategoryIdAndName(getResolutionId(), resolution.toLowerCase());
		if(null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	
	private class FolderBean{
		private Folder folder;
		private Boolean existed;
		public FolderBean(Folder folder, Boolean existed) {
			this.folder = folder;
			this.existed = existed;
		}
		/**
		 * @param existed the existed to set
		 */
		public void setExisted(Boolean existed) {
			this.existed = existed;
		}
	}
	
	protected void copyProductImages(File productFolder, Map<String, ProductPic> picMap) {
		//Copy imges
		String picRootPath = systemPreference.getProductPicRoot();
		for(Map.Entry<String, ProductPic> entry : picMap.entrySet()) {
			//Only copy files when id is set i.e saved in db.
			if(null != entry.getValue().getPrimaryKey()) {
				String picPath = entry.getValue().getPicPath();
				File outputFile = new File(picRootPath, picPath);
				if (!outputFile.getParentFile().exists()) {
					outputFile.getParentFile().mkdirs();
				}
				try {
					FileUtils.copyFile(new File(entry.getKey()), outputFile);
				}catch (Exception ce) {
					ce.printStackTrace();
					try {
						FileUtil.writeStringToFile(new File(productFolder.getParentFile(), copyImgLogFile), 
								entry.getKey() + "\n\r", "gbk", true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}					
				}
			}
		}
	}
	
	protected void copyProductFiles(File productFolder, Map<String, ProductFile> productFiles) {
		//Copy imges
		String picRootPath = systemPreference.getProductFileRoot();;
		for(Map.Entry<String, ProductFile> entry : productFiles.entrySet()) {
			if(null != entry.getValue().getPrimaryKey()) {//Only copy the files with id set.
				String picPath = entry.getValue().getFilePath();
				File outputFile = new File(picRootPath, picPath);
				if (!outputFile.getParentFile().exists()) {
					outputFile.getParentFile().mkdirs();
				}
				
				if(logger.isDebugEnabled()) {
					logger.debug("Copy file: " + entry.getKey() + " ---> " + outputFile.getAbsolutePath());
				}
				try {
					File jadFile = new File(entry.getKey());
					FileUtils.copyFile(jadFile, outputFile);
					//This is jad file
					if(StringUtils.equalsIgnoreCase(FilenameUtils.getExtension(jadFile.getName()), "jad")) {
						String fileName = StringUtils.remove(entry.getKey(), ".jad");
						String dest = StringUtils.remove(picPath, ".jad");
						FileUtils.copyFile(new File(fileName + ".jar"), new File(picRootPath, dest + ".jar"));
					}
				}catch (Exception ce) {
					ce.printStackTrace();
					try {
						FileUtil.writeStringToFile(new File(productFolder.getParentFile(), copyFileLogFile), 
								entry.getKey() + "\n\r", "gbk", true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}					
				}
			}
		}
	}
	
	private void validateProductInfor(ProductList prd,
			List<MessageContent> comments, File logDirectory) {
		StringBuffer sb = new StringBuffer();
		Set<ConstraintViolation<ProductList>> constraints = validatorFactory.getValidator().validate(prd);
		if(null != constraints && constraints.size() > 0) {
			sb.append(prd.getProductSourceId()).append("\t");
			for(ConstraintViolation<ProductList> vialation : constraints) {
				sb.append(vialation.getPropertyPath().toString());
				sb.append("=>");
				sb.append(vialation.getMessage());
				sb.append("\t");
			}
		}
		if(sb.length() > 0) {
			try {
				FileUtil.writeStringToFile(new File(logDirectory, validationLogFile), 
						sb.toString() + "\n\r", "gbk", true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private List<ProductPic> getProductPicList(File file, String illuImg) {
		File src = new File(file, illuImg);
		if(src.exists()) {
			File[] files = src.listFiles();
			if(null != files && files.length > 0) {
				List<ProductPic> list = new ArrayList<ProductPic>();
				for(File imgFile : files) {
					if(imgFile.exists() && !imgFile.isDirectory()) {
						ProductPic cover = new ProductPic();
						BufferedImage image;
						try {
							image = ImageIO.read(imgFile);
							cover.initDefaultValues();
							cover.setPicPath(imgFile.getAbsolutePath());//here should keep original path.
							cover.setPicSize(AppStringUtils.byteCountToDisplaySize(imgFile.length()));
							cover.setPicStyle(FilenameUtils.getExtension(imgFile.getName()));
							cover.setPicPixels(image.getWidth() + "*" + image.getHeight());
							try {
								cover.setJarMd5(EncryptionUtil.getMD5(FileUtils.openInputStream(imgFile)));
							} catch (IOException e) {
								e.printStackTrace();
							}
							cover.setCreateBy(createBy);
							list.add(cover);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return list;
			}
		}
		return null;
	}
	
	public void excuteDownProducts(String path){
		File file = new File(path);
		String products = "";
		List<Long> productIds = new ArrayList<Long>();
    	try {
    		products = FileUtils.readFileToString(file, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (StringUtils.isNotEmpty(products)) {
			productIds = JSON.parseArray(products, Long.class);
		}
		if (productIds != null && productIds.size() > 0) {
			for (Long productId : productIds) {
				ProductList productList = productListRepository.findByProductId(productId);
				productList.setDownStatus(ProductList.DISNABLE);
				productList.setDownReason("api down");
				productService.setProduct2Redis(productList);
			}
		}
	}

}
