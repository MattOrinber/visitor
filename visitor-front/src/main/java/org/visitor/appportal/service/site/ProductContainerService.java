package org.visitor.appportal.service.site;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.ProductContainerRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;

/**
 * 容器产品对应关系Service
 * @author mengw
 *
 */
public abstract class ProductContainerService extends SiteService {
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired	
	private AdvertiseRepository advertiseRepository;
    @Autowired
    private PageContainerRepository pageContainerRepository;
	@Autowired
	private ProductContainerRepository productContainerRepository;
	@Autowired
	private HtmlPageRepository htmlPageRepository;
	@Autowired
	private RecommandContainerRepository recommandContainerRepository;

	/**
	 * 根据产品类型，产品ID来添加容器内的数据
	 * @param productContainer 产品－容器对象
	 * @param productId 产品ID
	 * @param type 产品类型
	 * @param model
	 */
	public void createForGet(ProductContainer productContainer, Long productId,
			Integer type, Model model) {
		// TODO Auto-generated method stub
		/**容器类型*/
		ProductContainer.TypeEnum pcte=ProductContainer.TypeEnum.getInstance(type);
		model.addAttribute("product_id", productId);
		productContainer.setType(type);
		productContainer.setSiteId(getSiteId());
		
		/**根据产品的类型来决定是取哪一种产品类型*/
		if(pcte==ProductContainer.TypeEnum.Product){
			ProductList product = this.productListRepository.findOne(productId);
			productContainer.setProduct(product);
			model.addAttribute("productList", product);
		}else if(pcte==ProductContainer.TypeEnum.Folder){
			Folder folder = this.folderRepository.findOne(productId);
			productContainer.setTfolder(folder);
			model.addAttribute("productList", folder);
		}else {
			Advertise advertise = this.advertiseRepository.findOne(productId);
			productContainer.setAdvertise(advertise);
			model.addAttribute("productList", advertise);
		}
		
		
		/**不用再选择站点了，但是需要选择频道*/
		
		List<Long> folderIds = pageContainerRepository.findByContainerFolderId(getSiteId());
		Map<Long, String> map = new LinkedHashMap<Long, String>();
		for (Long folderId : folderIds) {
			Folder folder = folderRepository.findOne(folderId);
			map.put(folderId,folder.getName());
		}
		model.addAttribute("folderMap", map);
		
	}

	/**
	 * 保存过程的逻辑
	 * @param productContainer
	 * @param bindingResult
	 * @param model
	 * @return path 成功后的路径，如果路径为null，则表示不成功
	 */
	public String createForPost(ProductContainer productContainer,
			BindingResult bindingResult, Model model, Long productId) {
		// TODO Auto-generated method stub
		Integer type = productContainer.getType();
		
		ProductContainer.TypeEnum pcte=ProductContainer.TypeEnum.getInstance(type);
		String path = null;
		if (!bindingResult.hasErrors()) {
			//这个需要根据推荐类型的不同分别查询
			List<ProductContainer> pcs = null;
			if(pcte == ProductContainer.TypeEnum.Product){
				pcs = productContainerRepository.findByPageIdAndContainerIdAndProductIdAndType(productContainer.getPageId(),
					productContainer.getContainerId(), productId,type);
				path = "productlist";
			}else if (pcte == ProductContainer.TypeEnum.Folder){
				pcs = productContainerRepository.findByPageIdAndContainerIdAndTfolderIdAndType(productContainer.getPageId(),
					productContainer.getContainerId(), productId,type);
				path = "folder";
			}else {
				pcs = productContainerRepository.findByPageIdAndContainerIdAndAdvertiseIdAndType(productContainer.getPageId(),
					productContainer.getContainerId(), productId,type);
				path = "advertise";
			}
			
			if(pcs.size() > 0) {
				bindingResult.rejectValue("containerId", "productcontainer.productid.existed", new Object[]{productId}, 
					"产品：" + productId + "已经在本容器推荐过了");//.addError(new ObjectError("errors", "该推荐已存在"));
				return null;
			}

			Date date = new Date();
			productContainer.setCreateDate(date);
			productContainer.setModDate(date);
			productContainer.setModBy(AccountContext.getAccountContext().getUsername());
			productContainer.setCreateBy(AccountContext.getAccountContext().getUsername());
			productContainer.setSite(getSite());
			productContainer.setFolder(folderRepository.findOne(productContainer.getFolderId()));
			productContainer.setPage(htmlPageRepository.findOne(productContainer.getPageId()));
			productContainer.setContainer(recommandContainerRepository.findOne(productContainer.getContainerId()));
			
			if(pcte == ProductContainer.TypeEnum.Product){
				productContainer.setProduct(productListRepository.findOne(productId));
			}else if (pcte == ProductContainer.TypeEnum.Folder){
				productContainer.setTfolder(folderRepository.findOne(productId));
			}else {
				productContainer.setAdvertise(advertiseRepository.findOne(productId));
			}
			
			/*找最大的排序值*/
			Long sortOrder = productContainerRepository.findMaxSortOrder(productContainer.getSiteId(), 
					productContainer.getFolderId(), productContainer.getPageId(), productContainer.getContainerId());
			if (sortOrder == null) {
				sortOrder = 0l;
			}
			productContainer.setSortOrder(sortOrder + 1);
			productContainerRepository.save(productContainer);
			
			return path;
		}
		
		return null;
	}

}
