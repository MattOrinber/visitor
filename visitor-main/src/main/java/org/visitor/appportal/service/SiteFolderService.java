/*

 * Template pack-backend:src/main/java/project/manager/ManagerImpl.e.vm.java
 */
package org.visitor.appportal.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.HtmlPage.PageTypeEnum;
import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.domain.SiteValue;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductContainerRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.SiteValueRepository;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.PinyinUtil;
import org.visitor.appportal.web.utils.SearchParameters;
import org.visitor.appportal.web.vo.FolderOption;

/**
 * 
 * Default implementation of the {@link SiteFolderService} interface
 * 
 * @see SiteFolderService
 */
@Service("siteFolderService")
public class SiteFolderService {

	private static final Logger logger = LoggerFactory.getLogger(SiteFolderService.class);
	
	private long spaceNeeded = 2l;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	public SiteRepository siteRepository;
	
	@Autowired
	public SiteValueRepository siteValueRepository;

	@Autowired
	public FolderRepository folderRepository;
	
	@Autowired
	public PageContainerRepository pageContainerRepository;
	
	@Autowired
	public ProductContainerRepository productContainerRepository;
	
	@Autowired
	public HtmlPageRepository htmlPageRepository;
	
	@Autowired
	private ProductSiteFolderRepository productSiteFolderRepository;
	
	@Autowired
	public PictureRepository pictureRepository;
	
	/**
	 * find child folders by parent
	 * 
	 * @param folder
	 * @param sort
	 * @return folders(child)
	 */
	public List<Folder> findFolderChild(Long folder, Sort sort){
		return folderRepository.findByParentFolderIdAndStatus(folder, Folder.ENABLE, sort);
	}
	
	/**
	 * find folder's products
	 * @param folderId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page<ProductSiteFolder> findFolderProducts(Long folderId, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ProductSiteFolder> criteriaQuery = builder.createQuery(ProductSiteFolder.class);
        Root<ProductSiteFolder> root = criteriaQuery.from(ProductSiteFolder.class);
        criteriaQuery.where(builder.equal(root.get("folderId"), folderId));
        criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
		
		TypedQuery<ProductSiteFolder> query = em.createQuery(criteriaQuery);
		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		Long total = productSiteFolderRepository.countFolderProducts(folderId);
		
		return new PageImpl<ProductSiteFolder>(query.getResultList(), pageable, total.longValue());
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductList> findSiteProducts(Integer siteId, ProductList productList, List<DateRange> dateRanges, Pageable pageable) {
        String jpql = "select p from ProductSiteFolder psf ,ProductList p where psf.productSiteFolderPk.productId = p.productId and psf.productSiteFolderPk.siteId = :siteId";
        jpql = jpql + this.siteProductWhere(productList, dateRanges);
        jpql = jpql + " order by p.modDate desc";
		Query query = em.createQuery(jpql).setParameter("siteId", siteId);
		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		return query.getResultList();
	}
	
	public Long countSiteProducts(Integer siteId, ProductList productList, List<DateRange> dateRanges) {
        String jpql = "select count(p) from ProductSiteFolder psf ,ProductList p where psf.productSiteFolderPk.productId = p.productId and psf.productSiteFolderPk.siteId = :siteId";
        jpql = jpql + this.siteProductWhere(productList, dateRanges);
        Query query = em.createQuery(jpql).setParameter("siteId", siteId);
        Object count = query.getSingleResult();
        if (count != null)
        	return (Long)count;
		return 0l;
	}
	
	public List<ProductList> findBindProducts(Integer bind, ProductList productList, List<DateRange> dateRanges, Pageable pageable) {
		String jpql = "";
		if (bind.intValue() == 0) {
			jpql = "select p from ProductList p where p.productId in (select distinct(psf.productSiteFolderPk.productId) from ProductSiteFolder psf)";
		} else {
			jpql = "select p from ProductList p where p.productId not in (select distinct(psf.productSiteFolderPk.productId) from ProductSiteFolder psf)";
		}
        jpql = jpql + this.siteProductWhere(productList, dateRanges);
        jpql = jpql + " order by p.modDate desc";
		Query query = em.createQuery(jpql);
		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		@SuppressWarnings("unchecked")
		List<ProductList> res = query.getResultList();
		return res;
	}
	
	public Long countBindProducts(Integer bind, ProductList productList, List<DateRange> dateRanges) {
		String jpql = "";
		if (bind.intValue() == 0) {
			jpql = "select count(p) from ProductList p where p.productId in (select distinct(psf.productSiteFolderPk.productId) from ProductSiteFolder psf)";
		} else {
			jpql = "select count(p) from ProductList p where p.productId not in (select distinct(psf.productSiteFolderPk.productId) from ProductSiteFolder psf)";
		}jpql = jpql + this.siteProductWhere(productList, dateRanges);
        Query query = em.createQuery(jpql);
        Object count = query.getSingleResult();
        if (count != null)
        	return (Long)count;
		return 0l;
	}
	
	public String siteProductWhere(ProductList productList, List<DateRange> dateRanges){
		StringBuffer sql = new StringBuffer();
		//name
		if (StringUtils.isNotEmpty(productList.getName())) {
			sql.append(" and p.name like '%").append(productList.getName()).append("%'");
		}
		//createBy
		if (StringUtils.isNotEmpty(productList.getCreateBy())) {
			sql.append(" and p.createBy = '").append(productList.getCreateBy()).append("'");
		}
		//billingTypeId
		if (productList.getBillingTypeId() != null) {
			sql.append(" and p.billingTypeId = ").append(productList.getBillingTypeId());
		}
		//categoryId
		if (productList.getCategoryId() != null) {
			sql.append(" and p.categoryId = ").append(productList.getCategoryId());
		}
		//sourceId
		if (productList.getSourceId() != null) {
			sql.append(" and p.sourceId = ").append(productList.getSourceId());
		}
		//operatorId
		if (productList.getOperatorId() != null) {
			sql.append(" and p.operatorId = ").append(productList.getOperatorId());
		}
		//merchantId
		if (productList.getMerchantId() != null) {
			sql.append(" and p.merchantId = ").append(productList.getMerchantId());
		}
		//cooperationModelId
		if (productList.getCooperationModelId() != null) {
			sql.append(" and p.cooperationModelId = ").append(productList.getCooperationModelId());
		}
		//productSourceId
		if (StringUtils.isNotEmpty(productList.getProductSourceId())) {
			sql.append(" and p.productSourceId = '").append(productList.getProductSourceId()).append("'");
		}
		//description
		if (StringUtils.isNotEmpty(productList.getDescription())) {
			sql.append(" and p.description like '%").append(productList.getDescription()).append("%'");
		}
		//downStatus
		if (productList.getDownStatus() != null) {
			sql.append(" and p.downStatus = ").append(productList.getDownStatus());
		}
		//safeType
		if (productList.getSafeType() != null) {
			sql.append(" and p.safeType = ").append(productList.getSafeType());
		}
		
		//productType
		if (productList.getProductType() != null) {
			sql.append(" and p.productType = ").append(productList.getProductType());
		}
		
		if (dateRanges != null && dateRanges.size() > 0) {
			for (DateRange dr : dateRanges) {
		        if (dr.isBetween()) {
		        	sql.append(" and (p.").append(dr.getField()).append(" between '").append(dateToString((Date) dr.getFrom())).append("' and '").append(dateToString((Date) dr.getTo())).append("')");
		        } else if (dr.isFromSet()) {
		        	sql.append(" and (p.").append(dr.getField()).append(" > '").append(dateToString((Date) dr.getFrom())).append("')");
		        } else if (dr.isToSet()) {
		        	sql.append(" and (p.").append(dr.getField()).append(" < '").append(dateToString((Date) dr.getTo())).append("')");
		        }
			}
		}
		
		return sql.toString();
	}
	
	private String dateToString(Date date) {
		if (date != null) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			return format.format(date);
		}
		return "";
	}
		
	/**
	 * update site values
	 * 
	 * @param siteId
	 * @param type
	 * @param values
	 */
	@Transactional
	public void updateSiteValues(Integer siteId, Integer type, List<Long> values) {
		siteValueRepository.delete(siteValueRepository.findBySiteIdAndType(siteId, type));
		
		List<SiteValue> svs = new ArrayList<SiteValue>();
		// TODO Auto-generated method stub
		if (values != null && values.size() > 0) {
			for (Long v : values) {
				SiteValue sitevalue = new SiteValue();
				sitevalue.setSiteId(siteId);
				sitevalue.setType(type);
				sitevalue.setValue(v);
				svs.add(sitevalue);
			}
			siteValueRepository.save(svs);
		}
	}
	
	/**
	 * create site and init folder & page
	 * @param site
	 */
	@Transactional
	public void saveSiteInit(Site site) {
		// TODO Auto-generated method stub
		siteRepository.save(site);
		
		Folder folder = new Folder();
		folder.setName("首页");
		folder.setPath("/home");
		folder.setStatus(0);
		folder.setSortOrder(site.getCreateDate());
		folder.setCreateBy(site.getCreateBy());
		folder.setCreateDate(site.getCreateDate());
		folder.setModBy(site.getCreateBy());
		folder.setModDate(site.getCreateDate());
		folder.setFolderType(Folder.FOLDER_TYPE_SYSTEM);
		folder.setSite(site);
		this.saveSystemFolderInit(folder);
		
		Folder folder_search = new Folder();
		folder_search.setName("搜索");
		folder_search.setPath("/search");
		folder_search.setStatus(0);
		folder_search.setSortOrder(site.getCreateDate());
		folder_search.setCreateBy(site.getCreateBy());
		folder_search.setCreateDate(site.getCreateDate());
		folder_search.setModBy(site.getCreateBy());
		folder_search.setModDate(site.getCreateDate());
		folder_search.setFolderType(Folder.FOLDER_TYPE_BUSSINESS);
		folder_search.setSite(site);
		folder_search.setListPage(folder.getListPage());
		folder_search.setDetailPage(folder.getDetailPage());
		folder_search.setNaviPage(folder.getNaviPage());
		this.saveFolder(folder_search);
		
		Folder folder_rank = new Folder();
		folder_rank.setName("排行");
		folder_rank.setPath("/rank");
		folder_rank.setStatus(0);
		folder_rank.setSortOrder(site.getCreateDate());
		folder_rank.setCreateBy(site.getCreateBy());
		folder_rank.setCreateDate(site.getCreateDate());
		folder_rank.setModBy(site.getCreateBy());
		folder_rank.setModDate(site.getCreateDate());
		folder_rank.setFolderType(Folder.FOLDER_TYPE_BUSSINESS);
		folder_rank.setSite(site);
		folder_rank.setListPage(folder.getListPage());
		folder_rank.setDetailPage(folder.getDetailPage());
		folder_rank.setNaviPage(folder.getNaviPage());
		this.saveFolder(folder_rank);
	}
	
	/**
	 * save system folder
	 */
	@Transactional
	public void saveSystemFolderInit(Folder folder) {
		// TODO Auto-generated method stub
		Site site = folder.getSite();
		this.saveFolder(folder);

		// -- save index page
		HtmlPage htmlPageIndex = new HtmlPage();
		htmlPageIndex.setName(PageTypeEnum.Index.getDisplayName());
		htmlPageIndex.setPath(folder.getPath() + "/" + PageTypeEnum.Index.getSuffix()); // path
		htmlPageIndex.setTemplate(site.getIndexTemplate());
		htmlPageIndex.setFolder(folder);
		htmlPageIndex.setCreateBy(folder.getCreateBy());
		htmlPageIndex.setCreateDate(folder.getCreateDate());
		htmlPageIndex.setModBy(folder.getModBy());
		htmlPageIndex.setModDate(folder.getModDate());
		htmlPageIndex.setIfDefaultPage(HtmlPage.DEFAULT_PAGE);
		htmlPageIndex.setSiteId(folder.getSiteId());
		htmlPageIndex.setPageType(HtmlPage.PAGE_TEMPLATE_INDEX);
		htmlPageIndex.setIfQueryElement(false);
		htmlPageIndex.setPublishStatus(HtmlPage.PUBLISH_NO);
		htmlPageRepository.save(htmlPageIndex);

		// -- save list page
		HtmlPage htmlPageList = new HtmlPage();
		htmlPageList.setName(PageTypeEnum.List.getDisplayName());
		htmlPageList.setPath(folder.getPath() + "/" + PageTypeEnum.List.getSuffix()); // path
		htmlPageList.setTemplate(site.getListTemplate());
		htmlPageList.setFolder(folder);
		htmlPageList.setCreateBy(folder.getCreateBy());
		htmlPageList.setCreateDate(folder.getCreateDate());
		htmlPageList.setModBy(folder.getModBy());
		htmlPageList.setModDate(folder.getModDate());
		htmlPageList.setIfDefaultPage(HtmlPage.DEFAULT_PAGE);
		htmlPageList.setSiteId(folder.getSiteId());
		htmlPageList.setPageType(HtmlPage.PAGE_TEMPLATE_LIST);
		htmlPageList.setIfQueryElement(true);
		htmlPageList.setPublishStatus(HtmlPage.PUBLISH_NO);
		htmlPageRepository.save(htmlPageList);

		// -- save detail page
		HtmlPage htmlPageDetail = new HtmlPage();
		htmlPageDetail.setName(PageTypeEnum.Detail.getDisplayName());
		htmlPageDetail.setPath(folder.getPath() + "/" + PageTypeEnum.Detail.getSuffix()); // path
		htmlPageDetail.setTemplate(site.getDetailTemplate());
		htmlPageDetail.setFolder(folder);
		htmlPageDetail.setCreateBy(folder.getCreateBy());
		htmlPageDetail.setCreateDate(folder.getCreateDate());
		htmlPageDetail.setModBy(folder.getModBy());
		htmlPageDetail.setModDate(folder.getModDate());
		htmlPageDetail.setIfDefaultPage(HtmlPage.DEFAULT_PAGE);
		htmlPageDetail.setSiteId(folder.getSiteId());
		htmlPageDetail.setPageType(HtmlPage.PAGE_TEMPLATE_DETAIL);
		htmlPageDetail.setIfQueryElement(false);
		htmlPageDetail.setPublishStatus(HtmlPage.PUBLISH_NO);
		htmlPageRepository.save(htmlPageDetail);

		// -- save navi page
		HtmlPage htmlPageNavi = new HtmlPage();
		htmlPageNavi.setName(PageTypeEnum.Navi.getDisplayName());
		htmlPageNavi.setPath(folder.getPath() + "/" + PageTypeEnum.Navi.getSuffix()); // path
		htmlPageNavi.setTemplate(site.getNaviTemplate());
		htmlPageNavi.setFolder(folder);
		htmlPageNavi.setCreateBy(folder.getCreateBy());
		htmlPageNavi.setCreateDate(folder.getCreateDate());
		htmlPageNavi.setModBy(folder.getModBy());
		htmlPageNavi.setModDate(folder.getModDate());
		htmlPageNavi.setIfDefaultPage(HtmlPage.DEFAULT_PAGE);
		htmlPageNavi.setSiteId(folder.getSiteId());
		htmlPageNavi.setPageType(HtmlPage.PAGE_TEMPLATE_NAVI);
		htmlPageNavi.setIfQueryElement(false);
		htmlPageNavi.setPublishStatus(HtmlPage.PUBLISH_NO);
		htmlPageRepository.save(htmlPageNavi);

		folder.setIndexPage(htmlPageIndex);
		folder.setListPage(htmlPageList);
		folder.setDetailPage(htmlPageDetail);
		folder.setNaviPage(htmlPageNavi);

		this.saveFolder(folder);
	}
	
	/**
	 * save bussindess folder
	 */
	@Transactional
	public void saveBussinessFolderInit(Folder folder) {
		// TODO Auto-generated method stub
		List<Folder> folders = folderRepository.findBySiteIdAndFolderType(folder.getSiteId(), Folder.FOLDER_TYPE_SYSTEM);
		if (folders != null && folders.size() > 0) {
			folder.setListPage(folders.get(0).getListPage());
			folder.setDetailPage(folders.get(0).getDetailPage());
			folder.setNaviPage(folders.get(0).getNaviPage());
		} else {
			folder.setListPage(null);
			folder.setDetailPage(null);
			folder.setNaviPage(null);
		}

		String parentpath = "";
		String path = "/" + PinyinUtil.cn2Spell(folder.getName());

		if (folder.getParentFolderId() != null) {
			Folder parentFolder = folderRepository.findOne(folder.getParentFolderId());
			folder.setParentFolder(parentFolder);
			parentpath = parentFolder.getPath();
			path = parentpath + path;
		}

		List<Folder> fList = folderRepository.findBySiteIdAndPath(folder.getSiteId(), path);
		if (fList != null && fList.size() > 0) {
			path = path + String.valueOf(fList.size());
		}
		folder.setPath(path);
		folder.setSite(siteRepository.findOne(folder.getSiteId()));
		if (folder.getPicId() != null) {
			folder.setPic(pictureRepository.findOne(folder.getPicId()));
		}

		this.saveFolder(folder);
	}

	
	/**
	 * construts the folder select tree
	 * @param siteId
	 * @return
	 */
	public List<FolderOption> findFolderSelectTree(Integer siteId) {
		Iterable<Folder> it = folderRepository.findBySiteIdAndStatus(siteId, Folder.ENABLE);//.findAll();
		List<FolderOption> list = new ArrayList<FolderOption>();
		List<Folder> top = new ArrayList<Folder>();
		for(Folder f : it) {
			if(f.getParentFolder() == null) {
				top.add(f);
			}
		}
		for(Folder f : top) {
			this.makeOptionTree(f, f.getChildren(), list);
		}
		return list;
	}
	
	private void makeOptionTree(Folder node, List<Folder> children, List<FolderOption> out) {
		FolderOption co = new FolderOption(node);
		out.add(co);
		if (children != null && children.size() > 0) {
			for (Folder obj : children) {
				makeOptionTree(obj, obj.getChildren(), out);
			}
		}
	}
	
	/**
	 * delete folder node
	 * 
	 * @param folder
	 */
	@Transactional
	public void deleteFolder(Folder folder){
		
		//删除关联标签
		folder.setTags(new ArrayList<Category>());
		folder.setIndexPageId(null);
		folder.setListPageId(null);
		folder.setDetailPageId(null);
		folder.setNaviPageId(null);
		folderRepository.save(folder);

		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setPageSize(100000);
		
		//delete folder'recommond products
		List<PageContainer> pgcs = pageContainerRepository.findByfolderId(folder.getFolderId());
		if (pgcs != null && pgcs.size() > 0) {
			pageContainerRepository.delete(pgcs);
		}
		
		//delete folder container
		List<ProductContainer> pcs = productContainerRepository.findByFolderId(folder.getFolderId());
		if (pcs != null && pcs.size() > 0) {
			productContainerRepository.delete(pcs);
		}
		
		//delete folder page
		HtmlPage hp = new HtmlPage();
		hp.setFolder(folder);
		List<HtmlPage> hps = htmlPageRepository.findByFolderId(folder.getFolderId());
		if (hps != null && hps.size() > 0) {
			htmlPageRepository.delete(hps);	
		}
		
		//delete folder'products
		productSiteFolderRepository.deleteByFolderId(folder.getFolderId());
		
		folderRepository.delete(folder);				
	}

	/**
	 * save tree node
	 * 
	 * @param node
	 */
	public void saveFolder(Folder node) {
		if (null == node.getId()) {// create new folder
			long newLeft = 1l;
			long newRight = 2l;
			// Create new root folder.
			Session sess = (Session)em.getDelegate();
			if (node.getParentFolder() == null) {
				// Root node of a tree, new thread
				node.setNsLeft(newLeft);
				node.setNsRight(newRight);
				node.setNsThread(newRight);
				folderRepository.save(node);
				node.setNsThread(node.getId());
				org.hibernate.Query updateRight = sess.createQuery(
						"update Folder n set " + " n.nsThread = :thread "
								+ " where n.folderId = :folderId ");
				updateRight.setParameter("folderId", node.getId());
				updateRight.setParameter("thread", node.getId());
				updateRight.executeUpdate();
			} else {// Has root, then create child of root.
				// Child node of a parent
				Long parentThread = node.getParentFolder().getNsThread();
				logger.debug((node == null) + "  " + (node.getParentFolder() == null) + "  "
						+ (node.getParentFolder().getNsRight()));
				newLeft = node.getParentFolder().getNsRight();
				newRight = newLeft + spaceNeeded - 1;

				// update right
				org.hibernate.Query updateRight = sess.createQuery(
						"update Folder n set " + " n.nsRight = n.nsRight + :spaceNeeded "
								+ " where n.nsThread = :thread and n.nsRight >= :right");
				updateRight.setParameter("spaceNeeded", spaceNeeded);
				updateRight.setParameter("thread", parentThread);
				updateRight.setParameter("right", newLeft);
				updateRight.executeUpdate();

				// Update left
				org.hibernate.Query updateLeft = sess.createQuery(
						"update Folder n set " + " n.nsLeft = n.nsLeft + :spaceNeeded "
								+ " where n.nsThread = :thread and n.nsLeft > :right");
				updateLeft.setParameter("spaceNeeded", spaceNeeded);
				updateLeft.setParameter("thread", parentThread);
				updateLeft.setParameter("right", newLeft);
				updateLeft.executeUpdate();

				node.setNsLeft(newLeft);
				node.setNsRight(newRight);
				node.setNsThread(parentThread);
				folderRepository.save(node);
			}
		}
		
		
		folderRepository.save(node);
	}
}