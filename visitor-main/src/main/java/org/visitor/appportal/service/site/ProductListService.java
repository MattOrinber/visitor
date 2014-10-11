package org.visitor.appportal.service.site;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.ProductDetail;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductContainerRepository;
import org.visitor.appportal.repository.ProductDetailRepository;
import org.visitor.appportal.repository.ProductFileRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductPicRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.service.SystemPreference;

public abstract class ProductListService extends SiteService {

	@Autowired
	Properties systemProperties;
	@Autowired
	SystemPreference systemPreference;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private ProductFileRepository productFileRepository;
	@Autowired
	private ProductPicRepository productPicRepository;
	@Autowired
	private ProductSiteFolderRepository productSiteFolderRepository;
	@Autowired
	private ProductContainerRepository productContainerRepository;
	@Autowired
	private ProductDetailRepository productDetailRepository;
	
	public ProductDetail getProductDetail(Long productId) {
		// TODO Auto-generated method stub
		return this.productDetailRepository.findByProductIdAndSiteId(productId, getSiteId());
	}

	public void show(ProductList productList, Model model) {
		// TODO Auto-generated method stub
		model.addAttribute("downloadDomain", systemProperties.getProperty("download.domain"));
		model.addAttribute("picDomain", systemProperties.getProperty("pic.domain"));
		// 频道绑定
		List<ProductSiteFolder> psfs = productSiteFolderRepository.findProductSiteFolders(productList.getProductId());
		
		boolean canCreate = false;
		for (ProductSiteFolder p : psfs) {
			List<Folder> pathAsc = folderRepository.findFolderBread(p.getFolderId());
			StringBuffer path = new StringBuffer();
			for (Folder folder : pathAsc) {
				path.append("/").append(folder.getName());
			}
			p.getFolder().setPath(path.toString());
			
			if(p.getSiteId() == this.getSiteId().intValue()){
				canCreate = true;/*只有该产品绑定到了当前站点下的某个频道了，我们才允许创建其产品详情*/
			}
		}		

		model.addAttribute("folderList", psfs);
		// 产品文件
		model.addAttribute("productFiles", productFileRepository.findByProductId(productList.getPrimaryKey()));
		// 产品图片

		model.addAttribute("productPics", productPicRepository.getProductPics(productList.getProductId()));
		// 产品推荐
		List<ProductContainer> list = productContainerRepository.findByProductId(productList.getProductId());
		model.addAttribute("recommandList", list);
		
		//产品详情
		List<ProductDetail> details = productDetailRepository.findByProductId(productList.getProductId());
		model.addAttribute("productDetails", details);
		
		if(canCreate && (details != null && details.size() > 0)){
			
			//如果有本站点的Detail，则不显示
			for(ProductDetail detail : details){
				if(detail.getSiteId() == getSiteId().intValue()){
					canCreate = false;
					break;
				}
			}
		}
		
		/**是否显示创建按钮*/
		model.addAttribute("canCreate", canCreate);

	}

	/**
	 * 保存产品详情
	 * @param bindingResult
	 * @param productDetail
	 * @return
	 */
	public ResultEnum createProductDetail(BindingResult bindingResult,
			ProductDetail productDetail, MultipartFile picFile) {
		// TODO Auto-generated method stub
		if(bindingResult.hasErrors()){
			return ResultEnum.ERROR;
		}
		
		ProductDetail pd = this.productDetailRepository.findByProductIdAndSiteId(productDetail.getProductId(), getSiteId());
		/**一个产品在同一站点下只能有一个详情*/
		if(pd != null) {
			return ResultEnum.EXISTED;
		}
		
		productDetail.setSite(getSite());
		productDetail.setSortId(0);
		productDetail.setProduct(this.productListRepository.findOne(productDetail.getProductId()));
		
		this.productDetailRepository.save(productDetail);
		/**图片上传处理，由于用户也可以不上传，所以这里需要做空判断*/
		if (!picFile.isEmpty()) {

			String path = getPath(productDetail.getPrimaryKey(),FilenameUtils.getExtension(picFile.getOriginalFilename()));
        	String picRootPath = this.systemProperties.getProperty("product.pic.root");
        	
    		File outputFile = new File(picRootPath, path);
    		if (!outputFile.getParentFile().exists()) {
    			System.out.println("mkdir : " + outputFile.getParentFile().mkdirs() + " ,path "+ outputFile.getParentFile().getAbsolutePath());
    		}
    		try {
    			picFile.transferTo(outputFile);
    			
    			/**更新路径*/
    			productDetail.setPicPath(path);
    			this.productDetailRepository.save(productDetail);
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
       	} 
		
		return ResultEnum.OK;
	}
	
	/**
	 * 根据id生成相对路径
	 * 
	 * @author mengw
	 * 
	 * @param picId2
	 */
	private String getPath(Long picId, String suffix) {
		String fileName = StringUtils.leftPad(String.valueOf(picId), 8, "0");
		StringBuffer path = new StringBuffer("/" + fileName);
		for (int i = path.indexOf("/"); i < path.length() - 2; i = path.lastIndexOf("/")) {
			path.insert(i + 3, "/");
		}
		return "/" + Picture.PictureType.special.getDisplayName() + path + String.valueOf(picId) + "." + suffix;
	}

	public ResultEnum updateProductDetail(ProductDetail productDetail,
			BindingResult bindingResult, MultipartFile picFile) {
		// TODO Auto-generated method stub
		
		if(bindingResult.hasErrors()){
			return ResultEnum.ERROR;
		}
		
		/**图片上传处理，由于用户也可以不上传，所以这里需要做空判断*/
		if (!picFile.isEmpty()) {

			String path = getPath(productDetail.getPrimaryKey(),FilenameUtils.getExtension(picFile.getOriginalFilename()));
        	String picRootPath = this.systemProperties.getProperty("product.pic.root");
        	
    		File outputFile = new File(picRootPath, path);
    		if (!outputFile.getParentFile().exists()) {
    			System.out.println("mkdir : " + outputFile.getParentFile().mkdirs() + " ,path "+ outputFile.getParentFile().getAbsolutePath());
    		}
    		
    		try {
    			
    			picFile.transferTo(outputFile);
    			/**更新路径*/
    			productDetail.setPicPath(path);
    			this.productDetailRepository.save(productDetail);
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
       	} 
		
		this.productDetailRepository.save(productDetail);
		
		return ResultEnum.OK;
	}

}
