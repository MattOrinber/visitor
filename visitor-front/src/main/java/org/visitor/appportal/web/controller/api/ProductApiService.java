package org.visitor.appportal.web.controller.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
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
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.web.utils.EncryptionUtil;
import org.visitor.appportal.web.vo.Product4Json;
import org.visitor.appportal.web.vo.ProductFile4Json;
import org.visitor.util.AppStringUtils;

@Service
public class ProductApiService {
	
	protected static final Logger logger = LoggerFactory.getLogger(ProductApiService.class);
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductListRepository productListRepository;
	
	@Autowired
	private FolderRepository folderRepository;
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private SystemPreference systemPreference;
	
	private String createBy = "admin";
		
	/**
	 * 保存产品信息
	 * 2013-05-02 修改，广告主产品只要绑定频道则入推荐库
	 * @param product4Json
	 * @param productFolder
	 * @return ProductList
	 */
	public ProductList saveProduct(Product4Json product4Json) {
		//Load original prd if exists.
		ProductList prd = new ProductList();
		Long productId = product4Json.getProduct().getProductId();
		if (productId != null) {
			prd = productListRepository.findByProductId(productId);
		}
		
		//产品图片
		Map<String, ProductPic> picMap = getProductImagesMap(product4Json);
		
		//产品文件
		Map<String, ProductFile> productFiles = getProductFilesMap(product4Json.getProductFiles());
		
		//频道绑定
		List<ProductSiteFolder> productSites = getProductSiteFolders(prd, product4Json.getFolders());
				
		//评论
		List<MessageContent> comments = product4Json.getComments();
		
		if(null == prd || prd.getProductId() == null) {
			prd = product4Json.getProduct().copy();
			prd.initDefaultValues();
			prd.setOperator(categoryRepository.findOne(prd.getOperatorId()));
			String starLevel = prd.getStarLevel();
			if(StringUtils.length(starLevel) > 7) {
				prd.setStarLevel(StringUtils.substring(starLevel, 0, 3));
			} else if (StringUtils.isEmpty(starLevel)) {
				prd.setStarLevel("2.5");
			}
			prd.setCreateBy(createBy);
			prd.setProductType(ProductList.ProductTypeEnum.Downloadable.ordinal());
		} else {//Copy updated properties value.
			prd.setName(product4Json.getProduct().getName());
			prd.setPrice(product4Json.getProduct().getPrice());
			prd.setDescription(product4Json.getProduct().getDescription());
			prd.setProductVersion(product4Json.getProduct().getProductVersion());
			prd.setCategoryId(product4Json.getProduct().getCategoryId());
			if(StringUtils.isNotBlank(product4Json.getProduct().getTagLine())) {
				prd.setTagLine(product4Json.getProduct().getTagLine());
			}
		}
		if(prd.getBillingTypeId().intValue() == 1) {
			prd.setBillingTypeId(344l);
		}
		prd.setModDate(new Date());
		prd.setModBy(createBy);
		
		Product product = productService.createApiProductWithImgAndFiles(prd, picMap, productFiles, productSites, comments);
		if(null != product) {
			try {
				copyProductImages(picMap);
				copyProductFiles(productFiles);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return product.getProductList();
		}
		return prd;
	}
	
	/**
	 * 组织产品图片
	 * 
	 * @param product4Json
	 * @return map
	 */
	private Map<String, ProductPic> getProductImagesMap(Product4Json product4Json) {

		Map<String, ProductPic> map = new HashMap<String, ProductPic>();
		
		//封面图
		ProductPic cover = product4Json.getCoverPic();
		cover = getProductPic(product4Json.getCoverPic().getPicPath());
		if (cover != null) {
			cover.setProduct(product4Json.getProduct());
			cover.setPicType(ProductPic.COVER);
			product4Json.setCoverPic(cover);
			map.put(product4Json.getCoverPic().getPicPath() + "_cover", cover);
		}
		
		//ICON
		ProductPic icon = product4Json.getIconPic();
		icon = getProductPic(product4Json.getIconPic().getPicPath());
		if (icon != null) {
			icon.setProduct(product4Json.getProduct());
			icon.setPicType(ProductPic.ICON);
			product4Json.setIconPic(icon);
			map.put(product4Json.getIconPic().getPicPath(), icon);
		}
		
		//截图（多张）
		List<ProductPic> pics = product4Json.getIlluPics();
		List<ProductPic> illuPics = new ArrayList<ProductPic>();
		if (pics != null && pics.size() > 0) {
			for (ProductPic pic : pics) {
				illuPics.add(getProductPic(pic.getPicPath()));
			}
		}
		product4Json.setIlluPics(illuPics);		
		if(null != product4Json.getIlluPics()) {
			for(ProductPic pic : product4Json.getIlluPics()) {
				pic.setProduct(product4Json.getProduct());
				pic.setPicType(ProductPic.PRINT_SCREEN);
				map.put(pic.getPicPath(), pic);
			}
		}
		return map;
	}
	
	/**
	 * 获取图片基本信息
	 * 
	 * @param file
	 * @param illuImg
	 * @return
	 */
	private ProductPic getProductPic(String path) {
		File imgFile = new File(systemPreference.getProductPicDirectory() + path);
		if(imgFile.exists() && !imgFile.isDirectory()) {
			ProductPic pic = new ProductPic();
			BufferedImage image;
			try {
				image = ImageIO.read(imgFile);
				pic.initDefaultValues();
				pic.setPicPath(imgFile.getAbsolutePath());//here should keep original path.
				pic.setPicSize(AppStringUtils.byteCountToDisplaySize(imgFile.length()));
				pic.setPicStyle(FilenameUtils.getExtension(imgFile.getName()));
				pic.setPicPixels(image.getWidth() + "*" + image.getHeight());
				try {
					pic.setJarMd5(EncryptionUtil.getMD5(FileUtils.openInputStream(imgFile)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				pic.setCreateBy(createBy);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return pic;
		}
		return null;
	}
	
	/**
	 * 组织产品文件
	 * 
	 * @param productFiles
	 * @param productFolder
	 * @param cpId
	 * @return map
	 */
	private Map<String, ProductFile> getProductFilesMap(List<ProductFile4Json> productFiles) {
		Map<String, ProductFile> map = new HashMap<String, ProductFile>();
		
		for(ProductFile4Json json : productFiles) {
			ProductFile file = json.toProductFile();
			file.initDefaultValues();
			file.setCreateBy(createBy);
			file.setModBy(createBy);
			
			file.setOs(categoryRepository.findOne(json.getOsId()));
			file.setPlatform(categoryRepository.findOne(json.getPlatformId()));
			List<Category> list = categoryRepository.findByParentCategoryIdAndName(json.getPlatformId(), json.getMinVersion().trim());
			file.setPlatformVersion(null != list && list.size() > 0 ? list.get(0) : null);
			file.setResolution(categoryRepository.findOne(318L));   //min
			file.setDlCount(0l);
			file.setVersionOperator(">=");
			
			File realFile = new File(systemPreference.getProductFileDirectory() + file.getFilePath());
			
			if(realFile.exists() && !realFile.isDirectory()) {
				try {
					if(StringUtils.equalsIgnoreCase(FilenameUtils.getExtension(realFile.getName()), "jad")) {
						file.setJadMd5(EncryptionUtil.getMD5(FileUtils.openInputStream(realFile)));
						//Read jar File md5
						File jarFile = new File(realFile.getParent(), FilenameUtils.getBaseName(file.getFileName()) + ".jar");
						file.setJarMd5(EncryptionUtil.getMD5(FileUtils.openInputStream(jarFile)));
						file.setFileSize(AppStringUtils.byteCountToDisplaySize(jarFile.length()));
					} else {//Only read one file
						file.setJarMd5(EncryptionUtil.getMD5(FileUtils.openInputStream(realFile)));
						file.setFileSize(AppStringUtils.byteCountToDisplaySize(realFile.length()));
					}
						
				} catch (IOException e) {
					e.printStackTrace();
				}
					//Only save existed files.
				map.put(file.getFilePath(), file);
			}
		}
		return map;
	}
	
	/**
	 * 组织产品绑定频道
	 * 
	 * @param productList
	 * @param folders
	 * @return list
	 */
	private List<ProductSiteFolder> getProductSiteFolders(ProductList productList, List<Long> folders) {
		List<ProductSiteFolder> list = new ArrayList<ProductSiteFolder>();
		if(null != folders) {
			//Only process NOT imported product
				Map<Integer, FolderBean> siteFolder = new HashMap<Integer, FolderBean>();
				for(Long folderId : folders) {
					Folder f = this.folderRepository.findByFolderId(folderId);
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
		return list;
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
	
	/**
	 * 拷贝图片
	 * @param picMap
	 */
	protected void copyProductImages(Map<String, ProductPic> picMap) {
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
					FileUtils.copyFile(new File(entry.getKey().replaceAll("_cover", "")), outputFile);
				}catch (Exception ce) {
					ce.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 拷贝文件
	 * @param productFiles
	 */
	protected void copyProductFiles(Map<String, ProductFile> productFiles) {
		//Copy imges
		String picRootPath = systemPreference.getProductFileRoot();;
		for(Map.Entry<String, ProductFile> entry : productFiles.entrySet()) {
			if(null != entry.getValue().getPrimaryKey()) {//Only copy the files with id set.
				ProductFile pf = entry.getValue();
				String picPath = pf.getFilePath();
				if (StringUtils.isNotEmpty(picPath)) {
					File outputFile = new File(picRootPath, picPath);
					if (!outputFile.getParentFile().exists()) {
						outputFile.getParentFile().mkdirs();
					}
					
					if(logger.isDebugEnabled()) {
						logger.debug("Copy file: " + entry.getKey() + " ---> " + outputFile.getAbsolutePath());
					}
					try {
						File jadFile = new File(systemPreference.getProductFileDirectory() + entry.getKey());
						FileUtils.copyFile(jadFile, outputFile);
						//This is jad file
						if(StringUtils.equalsIgnoreCase(FilenameUtils.getExtension(jadFile.getName()), "jad")) {
							String fileName = StringUtils.remove(entry.getKey(), ".jad");
							String dest = StringUtils.remove(picPath, ".jad");
							FileUtils.copyFile(new File(systemPreference.getProductFileDirectory(),fileName + ".jar"), new File(picRootPath, dest + ".jar"));
						}
						this.productService.saveProductFileApkInfo(pf);
					}catch (Exception ce) {
						ce.printStackTrace();
					}
				}
			}
		}
	}
}
