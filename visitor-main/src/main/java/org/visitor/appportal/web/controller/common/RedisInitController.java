package org.visitor.appportal.web.controller.common;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.visitor.appportal.domain.*;
import org.visitor.appportal.redis.*;
import org.visitor.appportal.repository.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.app.portal.model.FolderRank.RankTypeEnum;
import org.visitor.app.portal.model.Product;
import org.visitor.app.portal.model.ProductIntelligentRecommend;
import org.visitor.appportal.domain.PageContainer.ShowTypeEnum;
import org.visitor.appportal.domain.Site.StatusEnum;
import org.visitor.appportal.service.ProductService;

@Controller
@RequestMapping("/domain/init/redis/")
public class RedisInitController {

    private static final Logger logger = LoggerFactory.getLogger(RedisInitController.class);

    @Autowired
    private SiteRedisRepository siteRedisRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryRedisRepository categoryRedisRepository;
    @Autowired
    private RecommandContainerRepository recommandContainerRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private ProductListRepository productListRepository;
    @Autowired
    private ProductSiteFolderRepository productSiteFolderRepository;
    @Autowired
    private SearchKeywordRepository searchKeywordRepository;
    @Autowired
    private ProductRedisRepository productRedisRepository;
    @Autowired
    private ProductContainerRepository productContainerRepository;
    @Autowired
    private ProductStateRepository productStateRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private AdvertiseRedisRepository advertiseRedisRepository;
    @Autowired
    private AdvertiseRepository advertiseRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private RecommendRuleRepository recommendRuleRepository;
    @Autowired
    private ProductSiteRecommendRepository productSiteRecommendRepository;
    @Autowired
    private RecommendRuleAcrossRepository recommendRuleAcrossRepository;
    @Autowired
    private RecommendRuleAcrossRedisRepository recommendRuleAcrossRedisRepository;
    @Autowired
    private HtmlPageRepository htmlPageRepository;
    @Autowired
    private ProductFileRepository productFileRepository;
    @Autowired
    private ProductContainerTaskRepository productContainerTaskRepository;
    @Autowired
    private PageContainerRepository pageContainerRepository;

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    LabelRedisRepository labelRedisRepository;

    /**
     * all sites info
     *
     * @return
     */
    @RequestMapping(value = "site", method = {GET, POST})
    public String siteAll() {
        Iterator<Site> allsite = siteRepository.findAll().iterator();
        while (allsite.hasNext()) {
            siteRedisRepository.deleteSite(allsite.next());
        }

        List<Site> sites = siteRepository.findByStatus(StatusEnum.Enable.ordinal());
        if (sites != null && sites.size() > 0) {
            for (Site site : sites) {
                siteRedisRepository.setSite(site);
            }
        }
        return "domain/common/publish";
    }

    /**
     * site publish
     *
     * @return
     */
    @RequestMapping("site/{siteId}")
    public String site(@PathVariable("siteId") Integer siteId) {
        Site site = siteRepository.findOne(siteId);
        siteRedisRepository.setSite(site);
        return "domain/common/publish";
    }

    /**
     * 发布站点中所有的频道信息。不包括频道中绑定的产品。
     */
    @RequestMapping("site/folder/{siteId}")
    public String siteFolder(@PathVariable("siteId") Integer siteId) {
        List<Folder> b_folders = folderRepository.findBySiteIdAndFolderType(siteId, Folder.FOLDER_TYPE_BUSSINESS);
        List<Folder> s_folders = folderRepository.findBySiteIdAndFolderType(siteId, Folder.FOLDER_TYPE_SYSTEM);

        List<String> folderIds = new ArrayList<String>();//重新更新缓存数据
        //发布两类频道
        if (b_folders != null && b_folders.size() > 0) {
            for (Folder folder : b_folders) {
                this.folder(folder.getFolderId());
                folderIds.add(String.valueOf(folder.getFolderId()));
            }
        }

        if (s_folders != null && s_folders.size() > 0) {
            for (Folder folder : s_folders) {
                this.folder(folder.getFolderId());
                folderIds.add(String.valueOf(folder.getFolderId()));
            }
        }
        this.siteRedisRepository.sendCacheElementMessage(Folder.class.getName(), folderIds);
        return "domain/common/publish";
    }

    /**
     * folder update.
     */
    @RequestMapping("folder/{folderId}")
    public String folder(@PathVariable("folderId") Long folderId) {
        Folder folder = folderRepository.findOne(folderId);

        //只发布可用的频道信息
        if (folder.getStatus() != null && folder.getStatus().intValue() == 0) {
            siteRedisRepository.setFolder(folder);
            List<Folder> folders = folderRepository.findFolderBread(folder.getFolderId());
            List<Long> folderIds = new ArrayList<Long>();
            for (Folder f : folders) {
                folderIds.add(f.getFolderId());
            }
            siteRedisRepository.setFolderAncestors(folder.getFolderId(), folderIds);

            if (folder.getParentFolder() != null) {
                siteRedisRepository.setFolderChildren(folder.getSiteId(), folder.getParentFolderId(),
                        folder.getFolderId(), folder.getSortOrder().getTime());
            }
        } else {
            siteRedisRepository.deleteFolder(folder);
            siteRedisRepository.deleteFolderAncestors(folder.getFolderId());
            if (folder.getParentFolder() != null) {
                siteRedisRepository.deleteFolderChildren(folder.getSiteId(), folder.getParentFolderId(),
                        folder.getFolderId());
            }
        }

        List<Category> tags = folder.getTags();
        if (tags != null && tags.size() > 0) {
            for (Category c : tags) {
                List<Folder> folders = c.getFolders();
                if (null != folders && folders.size() > 0) {
                    List<Long> folderIds = new ArrayList<Long>();
                    for (Folder f : folders) {
                        folderIds.add(f.getFolderId());
                    }
                    siteRedisRepository.setFolderListByTagId(c.getCategoryId(), folderIds);
                }
            }
        }

        List<String> ids = new ArrayList<String>();
        ids.add(String.valueOf(folderId));
        this.siteRedisRepository.sendCacheElementMessage(Folder.class.getName(), ids);

        return "domain/common/publish";
    }

    /**
     * 发布站点中频道与产品的关联关系，不包括产品的重新发布。
     */
    @RequestMapping("site/product/list/{siteId}")
    public String siteProduct(@PathVariable("siteId") Integer siteId) {
        List<Folder> folders = folderRepository.findBySiteIdAndFolderType(siteId, Folder.FOLDER_TYPE_BUSSINESS);
        if (folders != null && folders.size() > 0) {
            for (Folder f : folders) {
                this.publishProductList(f);
            }
        }
        return "domain/common/publish";
    }

    /**
     * 发布站点中频道与产品的关联关系，不包括产品的重新发布。
     */
    @RequestMapping("site/product/rank/new/{siteId}")
    public String siteRankProduct(@PathVariable("siteId") Integer siteId) {
        List<Folder> folders = folderRepository.findBySiteIdAndFolderType(siteId, Folder.FOLDER_TYPE_BUSSINESS);
        if (folders != null && folders.size() > 0) {
            for (Folder f : folders) {
                List<ProductSiteFolder> list = productSiteFolderRepository.findByFolderId(f.getFolderId());
                if (null != list) {
                    for (ProductSiteFolder psf : list) {
                        productService.publishNewProduct(psf, psf.getProduct().getCreateDate().getTime());
                    }
                    list = null;
                }
            }
        }
        return "domain/common/publish";
    }

    /**
     * 发布频道中关联产品列表。
     */
    @RequestMapping("folder/product/list/{folderId}")
    public String folderListProducts(@PathVariable("folderId") Long folderId) {
        Folder folder = folderRepository.findOne(folderId);
        this.publishProductList(folder);
        return "domain/common/publish";
    }

    /**
     * 发布频道中关联产品列表的实现
     *
     * @param folder
     */
    private void publishProductList(Folder folder) {
        long c = System.currentTimeMillis();

        productRedisRepository.deleteSiteFolderKeys(folder.getSiteId(), folder.getFolderId());
        logger.info("[redis] delete folder[" + folder.getFolderId() + "] list keys complete !");

        List<ProductSiteFolder> list = productSiteFolderRepository.findByFolderId(folder.getFolderId());
        if (null != list) {
            for (ProductSiteFolder psf : list) {
                productService.redisAddFolderProduct(psf);
            }
            logger.info("[redis] update folder[" + folder.getFolderId() + "] product list publish finshed!Total :" + list.size()
                    + "! Time :" + (System.currentTimeMillis() - c) / 1000);
            list = null;
        }
    }

    /**
     * 发布站点中所有的产品信息，包括频道与产品的关联关系。
     */
    @RequestMapping("site/product/{siteId}")
    public String siteProducts(@PathVariable("siteId") Integer siteId) {
        List<Folder> folders = folderRepository.findBySiteIdAndFolderType(siteId, Folder.FOLDER_TYPE_BUSSINESS);
        if (folders != null && folders.size() > 0) {
            for (Folder f : folders) {
                publishFolderProducts(f.getFolderId());
            }
        }
        return "domain/common/publish";
    }

    /**
     * 发布站点产品的下载数
     */
    @RequestMapping("site/product/totaldl/{siteId}")
    public String siteProductsTotaldl(@PathVariable("siteId") Integer siteId) {

        List<Folder> folders = folderRepository.findBySiteIdAndFolderType(siteId, Folder.FOLDER_TYPE_BUSSINESS);
        if (folders != null && folders.size() > 0) {
            for (Folder f : folders) {
                List<Object[]> list = productStateRepository.getFolderProductTotalDl(f.getFolderId());

                for (Object[] o : list) {
                    String productId = String.valueOf(o[0]);
                    Long stat = (Long) o[1];
                    productRedisRepository.setProductState(productId, RankTypeEnum.TotalDownload, stat);
                    logger.info("[redis] total_dl product[" + productId + "]" + "[" + String.valueOf(stat) + "]");
                }
            }
        }
        return "domain/common/publish";
    }

    /**
     * 发布频道中的所有产品信息。
     */
    @RequestMapping("folder/product/{folderId}")
    public String folderProducts(@PathVariable("folderId") Long folderId) {
        this.publishFolderProducts(folderId);
        return "domain/common/publish";
    }

    private void publishFolderProducts(Long folderId) {
        long c = System.currentTimeMillis();
        int search = 0;
        List<ProductList> list = productListRepository.findByFolderId(folderId);
        List<String> productIds = new ArrayList<String>();
        if (null != list && list.size() > 0) {
            List<Product> ps = new ArrayList<Product>();
            for (ProductList p : list) {
                Product pro = productService.setProduct2Redis1(p);
                if (pro != null) {
                    ps.add(pro);
                }
                if (ps.size() >= 100) {
                    productService.setProducts2Search(ps);
                    search = search + ps.size();
                    ps.clear();
                }

                productIds.add(String.valueOf(p.getProductId()));
            }
            productService.setProducts2Search(ps);
            search = search + ps.size();
            logger.info("[redis] update folder [" + folderId + "] product publish finshed! Total Redis:" + list.size()
                    + " ! Total Search:" + search
                    + " ! Time :" + (System.currentTimeMillis() - c) / 1000);

            //将这批产品进行重新发布，即更新客户端缓存
            this.siteRedisRepository.sendCacheElementMessage(Product.class.getName(), productIds);
            productIds.clear();
            ps = null;
            list = null;
        }
    }

    @RequestMapping("search/product/{folderId}")
    public String folderProductsToSearch(@PathVariable("folderId") Long folderId) {
        long c = System.currentTimeMillis();
        List<ProductList> list = productListRepository.findByFolderId(folderId);
        int search = 0;
        if (null != list && list.size() > 0) {
            List<Product> ps = new ArrayList<Product>();
            for (ProductList p : list) {
                Product pro = productService.getProductByProductList(p);
                if (pro != null && pro.getProductList() != null) {
                    ps.add(pro);
                }
                if (ps.size() >= 100) {
                    //添加索引
                    productService.setProducts2Search(ps);
                    search = search + ps.size();
                    ps.clear();
                }
            }
            productService.setProducts2Search(ps);
            search = search + ps.size();
            logger.info("[redis] update folder [" + folderId + "] product publish finshed! Total Redis:" + list.size()
                    + " ! Total Search:" + search
                    + " ! Time :" + (System.currentTimeMillis() - c) / 1000);
            ps = null;
            list = null;
        }
        return "domain/common/publish";
    }

    /**
     * 频道下的产品发布到redis
     *
     * @param folderId
     * @return
     */
    @RequestMapping("redis/product/{folderId}")
    public String folderProductsToRedis(@PathVariable("folderId") Long folderId) {
        List<ProductList> list = productListRepository.findByFolderId(folderId);
        if (null != list && list.size() > 0) {
            for (ProductList p : list) {
                Product pro = productService.getProductByProductList(p);
                if (pro != null && pro.getProductList() != null) {
                    productRedisRepository.setProduct(p.getProductId(), pro);
                }
            }
        }
        return "domain/common/publish";
    }

    /**
     * product update.
     */
    @RequestMapping("product/{productId}")
    public String product(@PathVariable("productId") Long productListId) {
        productService.setProduct2Redis(productListRepository.findByProductId(productListId));

        List<ProductSiteFolder> list = productSiteFolderRepository.findByProductId(productListId);
        if (null != list) {
            for (ProductSiteFolder psf : list) {
                productService.redisAddFolderProduct(psf);
            }

            List<String> productIds = new ArrayList<String>();
            productIds.add(String.valueOf(productListId));
            this.siteRedisRepository.sendCacheElementMessage(Product.class.getName(), productIds);

            logger.info("[redis] product/" + productListId + " publish finshed!");
        }

        return "domain/common/publish";
    }

    /**
     * 发布指定推荐里的产品信息，这里的产品信息是广义的
     * 对本方法做出修改，使其在更新产品推荐数据之前，删除该容器中原来的信息
     *
     * @param recommandContainerId
     * @param withProduct
     * @return
     */
    @RequestMapping("productContainer/{productContainerId}/{pageId}/{withProduct}")
    public String recommandContainer(
            @PathVariable("productContainerId") Long recommandContainerId,
            @PathVariable("pageId") Long pageId,
            @PathVariable("withProduct") String withProduct) {
        RecommandContainer container = recommandContainerRepository.findOne(recommandContainerId);
        if (null != container && null != container.getPrimaryKey()) {
            //发布容器本身
            productRedisRepository.setRecommandContainer(container);
            //发布容器中的产品
            if (StringUtils.equalsIgnoreCase(withProduct, "withProduct")) {

                //按理说，在这里应该清理一下该推荐位的的旧元素，以更新
                HtmlPage page = htmlPageRepository.findOne(pageId);
                if (page != null) {
                    productRedisRepository.clearItem(page, recommandContainerId);

                    List<Order> list = new ArrayList<Order>();
                    list.add(new Order(Direction.DESC, "sortOrder"));
                    Sort sort = new Sort(list);

				    /*在这里，我们只取和当前页面相关的推荐信息*/
                    List<ProductContainer> pcList = productContainerRepository.findByPageIdAndContainerId(pageId, recommandContainerId, sort);

                    productRedisRepository.deleteProductIdInContainer(page.getSiteId(), recommandContainerId);

					/*以下为发布过程，和原来保持一致*/
                    if (null != pcList) {
                        for (ProductContainer pc : pcList) {
                            productRedisRepository.setProductRecommand(pc, pc.getSortOrder());
                            //产品本身还没有发布，需要重新发布产品。实际这是不应该的事情。
                            //2011-12-30: 由于产品容器中可能还包括频道和广告，所以这里需要做进一步的调整，不能一直发布产品
                            if (pc.getType() == ProductContainer.TypeEnum.Product.getValue()) {

                                List<ProductSiteFolder> psfs = productSiteFolderRepository.findProductSiteFolders(pc.getProductId());
                                if (psfs != null && psfs.size() > 0) {
                                    for (ProductSiteFolder psf : psfs) {
                                        List<String> match_res = productService.matchVersionLevel(psf);
                                        if (match_res != null && match_res.size() > 0) {
                                            for (String match : match_res) {
                                                Long versionId = Long.valueOf(match.split(" ")[0]);
                                                Integer level = Integer.valueOf(match.split(" ")[1]);
                                                productRedisRepository.setProductIdsInContainer(
                                                        psf.getSiteId(),
                                                        pc.getContainerId(),
                                                        pc,
                                                        versionId, level);
                                            }
                                        }
                                    }
                                }

                                Product product = productRedisRepository.getProductByProductId(pc.getProductId(), false);
                                if (null == product) {
                                    //对于已经下架的产品，不需要再次发布产品了。
                                    ProductList temp = this.productListRepository.findByProductId(pc.getProductId());
                                    if (null != temp && temp.isEnable()) {
                                        this.productService.setProduct2Redis(temp);
                                        //更新容器自身产品
                                    }
                                }
                            } else if (pc.getType() == ProductContainer.TypeEnum.Folder.getValue()) {
                                Folder folder = siteRedisRepository.getFolderById(pc.getTfolderId());
                                if (null == folder) {
                                    this.folder(pc.getTfolderId());// this.productListRepository.findOne(pc.getProductId()));
                                }
                            } else {
                                Advertise advertise = advertiseRedisRepository.getAdvertiseById(pc.getAdvertiseId());
                                if (null == advertise) {
                                    Advertise temp = this.advertiseRepository.findOne(pc.getAdvertiseId());
                                    if (null != temp && temp.isEnable()) {
                                        //advertiseRedisRepository.setAdvertise(temp);
                                        //重新执行一次发广告的操作
                                        this.advertise(pc.getAdvertiseId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "domain/common/publish";
    }

    /**
     * searchkeyword list update.
     */
    @RequestMapping(value = "searchkeywords", method = {GET, POST})
    public String searchkeywords(@RequestParam(value = "siteId", required = true) Integer siteId) {
        Sort sort = new Sort(Direction.ASC, "sortOrder");

        //如果只是查找可用的关键词的话，那么那些已删除的关键词在Redis中的信息就无从更新了
        List<SearchKeyword> searchKeywords = searchKeywordRepository.findBySiteIdAndStatus(siteId, SearchKeyword.StatusEnum.Enable.ordinal(), sort);
        siteRedisRepository.setSearchKeywords(siteId, searchKeywords);

        //siteRedisRepository.deleteSearchKeyword(siteId, searchKeywords);
        //搜索关键词，只需要发消息即可，不需要保持关键词，因为前台只缓存可运营的关键词
        List<String> folderIds = new ArrayList<String>();
        this.siteRedisRepository.sendCacheElementMessage(SearchKeyword.class.getName(), folderIds);
        return "domain/common/publish";
    }

    /**
     * searchkeyword list update.
     */
    @RequestMapping(value = "searchkeyword", method = {GET, POST})
    public String searchkeyword(@RequestParam(value = "siteId", required = true) Integer siteId,
                                @RequestParam(value = "keywordId", required = true) Long wordId) {

        //发布该站点下的所有搜索词
        return this.searchkeywords(siteId);
    }

    @RequestMapping(value = "productContainers", method = {GET, POST})
    public String publishAllRecommandContainers() {
        Iterable<RecommandContainer> all = this.recommandContainerRepository.findAll();
        for (Iterator<RecommandContainer> it = all.iterator(); it.hasNext(); ) {
            RecommandContainer rc = it.next();
            List<HtmlPage> htmlPages = htmlPageRepository.findPagesByContainerId(rc.getContainerId());
            if (htmlPages != null && htmlPages.size() > 0) {
                for (HtmlPage htmlPage : htmlPages) {
                    this.recommandContainer(rc.getContainerId(), htmlPage.getPageId(), "withProduct");
                }
            }
        }
        return "domain/common/publish";
    }

    /**
     * 将所有的category信息发布到redis。
     *
     * @return
     */
    @RequestMapping(value = "category", method = {GET, POST})
    public String publishAllCategory() {
        Iterable<Category> cats = this.categoryRepository.findAll();
        for (Category category : cats) {
            //分类的子分类
            List<Category> categoryList = categoryRepository.findByParentCategoryIdAndStatus(
                    category.getCategoryId(), Category.ENABLE, null);
            List<Category> ancestors = categoryRepository.findAncestors(category.getCategoryId());
            categoryRedisRepository.categoryUpdate(category, categoryList, ancestors, null);

            if (category.getNsThread().equals(Category.TAG)) {
                List<Folder> folders = category.getFolders();
                if (null != folders) {
                    List<Long> ids = new ArrayList<Long>();
                    for (Folder f : folders) {
                        ids.add(f.getFolderId());
                    }
                    siteRedisRepository.setFolderListByTagId(category.getCategoryId(), ids);
                }
            }
        }
        //只需要清除缓存
        this.siteRedisRepository.sendCacheElementMessage(Category.class.getName(), new ArrayList<String>());
        return "domain/common/publish";
    }

    /**
     * 更新全部站点频道排行
     */
    //@Scheduled(cron="0 0 5 ? * *")
    public void updateSiteFolderRankProducts() {
        logger.info("--------------------------- Scheduled starting ---------------------------");
        List<Site> sites = siteRepository.findByStatus(StatusEnum.Enable.ordinal());
        if (sites != null && sites.size() > 0) {
            for (Site site : sites) {
                List<Folder> folders = folderRepository.findBySiteIdAndFolderType(site.getSiteId(), Folder.FOLDER_TYPE_BUSSINESS);
                if (folders != null && folders.size() > 0) {
                    for (Folder f : folders) {
                        this.publishProductList(f);
                    }
                }
            }
        }
        logger.info("--------------------------- Scheduled ending ---------------------------");
    }

    /**
     * 广告发布到redis.
     */
    @RequestMapping("advertise/{advertiseId}")
    public String advertise(@PathVariable("advertiseId") Long advertiseId) {

        Advertise ad = advertiseRepository.findOne(advertiseId);
        /**
         * 设置和广告相关的图片
         * 这里我们直接使用在SiteRedisRepository中的setPicture方法
         */
        siteRedisRepository.setPicture(pictureRepository.findOne(ad.getPictureId()).copy());
		
		/*将指定产品存入到redis中*/
        advertiseRedisRepository.setAdvertise(ad);


        /**
         * 根据广告类型来决定是否发布对应的产品和频道
         */
        int adType = ad.getType();
        if (adType == Advertise.AdvertiseTypeEnum.Folder.ordinal()) {
            this.folder(ad.getFolderId());
            logger.info("[redis] advertise/folder/" + advertiseId + " publish finshed!");
        } else if (adType == Advertise.AdvertiseTypeEnum.Product.ordinal()) {
            this.product(ad.getProductId());
            logger.info("[redis] advertise/product/" + advertiseId + " publish finshed!");
        }

        //清除广告的缓存
        List<String> ids = new ArrayList<String>();
        ids.add(String.valueOf(advertiseId));
        this.siteRedisRepository.sendCacheElementMessage(Advertise.class.getName(), ids);

        List<String> picIds = new ArrayList<String>();
        picIds.add(String.valueOf(ad.getPictureId()));
        siteRedisRepository.sendCacheElementMessage(Picture.class.getName(), picIds);
		
		/*记录日志*/
        logger.info("[redis] advertise/" + advertiseId + " publish finshed!");
        logger.info("[redis] picture/" + ad.getPictureId() + " publish finshed!");

        return "domain/common/publish";
    }

    /**
     * 发布推荐库产品
     *
     * @param productId
     * @return
     */
    @RequestMapping("site/auto/recommend/storage/{siteId}")
    public String publishRecmmendStorage(@PathVariable("siteId") Integer siteId) {
        productService.publishRecmmendStorage(siteId);
        return "domain/common/publish";
    }

    /**
     * 发布产品推荐
     *
     * @param productId
     * @return
     */
    @RequestMapping("product/recommend/{siteId}/{productId}/{ifPublishDate}")
    public String recommend(@PathVariable("siteId") Integer siteId, @PathVariable("productId") Long productId, @PathVariable("ifPublishDate") boolean publishDate) {
        RecommendRule rr = productService.getProductRecommendRule(siteId, productId);
        if (rr != null) {
            productService.setRecommendRule2Redis(siteId, productId, rr);
            if (publishDate) {
                productService.setRecommendData2Redis(siteId, productId, rr);
            }
        }
        logger.info("[redis] recommend product : " + productId + " publish finshed!");
        return "domain/common/publish";
    }

    /**
     * 发布频道推荐规则
     *
     * @param productId
     * @return
     */
    @RequestMapping("folder/recommend/{folderId}")
    public String recommendFolder(@PathVariable("folderId") Long folderId) {

        RecommendRule rr = recommendRuleRepository.findByFolderId(folderId);

        ProductIntelligentRecommend pir = productService.copyToIntelligentRecommend(rr);

        productRedisRepository.setFolderIntelligentRecommend(folderId, pir);

        return "domain/common/publish";
    }

    /**
     * 发布站点产品推荐
     *
     * @param productId
     * @return
     */
    @RequestMapping("site/product/recommend/{siteId}/{ifPublishDate}")
    public String siteRecommend(@PathVariable("siteId") Integer siteId, @PathVariable("ifPublishDate") boolean publishDate) {
        List<Folder> folders = folderRepository.findBySiteIdAndFolderType(siteId, Folder.FOLDER_TYPE_BUSSINESS);
        if (null != folders && folders.size() > 0) {
            for (Folder folder : folders) {
                folderRecommend(folder.getFolderId(), publishDate);
            }
        }
        return "domain/common/publish";
    }

    /**
     * 发布频道产品推荐
     *
     * @param productId
     * @return
     */
    @RequestMapping("folder/product/recommend/{folderId}/{ifPublishDate}")
    public String folderRecommend(@PathVariable("folderId") Long folderId, @PathVariable("ifPublishDate") boolean publishDate) {
        List<ProductSiteFolder> list = productSiteFolderRepository.findByFolderId(folderId);
        Folder f = folderRepository.findByFolderId(folderId);
        if (null != list && list.size() > 0) {
            for (ProductSiteFolder psf : list) {
                recommend(f.getSiteId(), psf.getProductId(), publishDate);
            }
        }
        return "domain/common/publish";
    }

    @RequestMapping("recommendruleacross/{ruleId}")
    public String recommendruleacross(@PathVariable("ruleId") Long ruleId) {
        RecommendRuleAcross recommendRuleAcross = recommendRuleAcrossRepository.findByRuleId(ruleId);
        if (recommendRuleAcross != null) {
            recommendRuleAcrossRedisRepository.setRecommendRuleAcross(recommendRuleAcross, null);
        }
        return "domain/common/publish";
    }

    @RequestMapping("site/product/apkinfo/{siteId}")
    public String siteProductApkInfo(@PathVariable("siteId") Integer siteId) {
        List<Folder> folders = folderRepository.findBySiteIdAndFolderType(siteId, Folder.FOLDER_TYPE_BUSSINESS);
        if (folders != null && folders.size() > 0) {
            for (Folder f : folders) {
                long c = System.currentTimeMillis();
                List<ProductList> list = productListRepository.findByFolderId(f.getFolderId());
                if (null != list && list.size() > 0) {
                    for (ProductList p : list) {
                        if (p.getDownStatus().intValue() == ProductList.ENABLE) {
                            List<ProductFile> productFiles = productFileRepository.findFileByProductId(p.getProductId());
                            productService.setProductFileApkInfo(productFiles);
                        }
                    }
                }
                logger.info("[redis] publish apkinfo [" + f.getFolderId().toString() + "] product publish finshed! Total Redis:" + list.size()
                        + " ! Time :" + (System.currentTimeMillis() - c) / 1000);
            }
        }
        return "domain/common/publish";
    }

    /**
     * 指定发布某天的定时产品
     */
    @RequestMapping("productContainer/task/{containerId}/{displaydate}")
    public String recommandContainerTask(@PathVariable("containerId") Long containerId,
                                         @PathVariable("displaydate") Integer displaydate) {

        List<Order> list = new ArrayList<Order>();
        list.add(new Order(Direction.DESC, "sortOrder"));
        Sort sort = new Sort(list);

        List<ProductContainerTask> pctList = this.productContainerTaskRepository.findByContainerIdAndDisplayDate(containerId, displaydate, sort);

        List<PageContainer> pages = pageContainerRepository.findByContainerId(containerId);

        if (pages != null && pages.size() > 0) {
            for (PageContainer page : pages) {
                List<ProductContainer> pcList = null;
                HtmlPage htmlPage = this.htmlPageRepository.findOne(page.getPageId());

                if (page.getShowType() == ShowTypeEnum.Auto.getValue().intValue()) {
                    pcList = productService.getPcListFromTaskList(pctList, htmlPage);

                    productRedisRepository.clearItem(htmlPage, containerId);

                    productRedisRepository.deleteProductIdInContainer(page.getSiteId(), containerId);

					/*以下为发布过程，和原来保持一致*/
                    if (null != pcList) {
                        for (ProductContainer pc : pcList) {
                            productRedisRepository.setProductRecommand(pc, pc.getSortOrder());
                            if (pc.getType() == ProductContainer.TypeEnum.Product.getValue()) {

                                List<ProductSiteFolder> psfs = productSiteFolderRepository.findProductSiteFolders(pc.getProductId());
                                if (psfs != null && psfs.size() > 0) {
                                    for (ProductSiteFolder psf : psfs) {
                                        List<String> match_res = productService.matchVersionLevel(psf);
                                        if (match_res != null && match_res.size() > 0) {
                                            for (String match : match_res) {
                                                Long versionId = Long.valueOf(match.split(" ")[0]);
                                                Integer level = Integer.valueOf(match.split(" ")[1]);
                                                productRedisRepository.setProductIdsInContainer(
                                                        psf.getSiteId(),
                                                        pc.getContainerId(),
                                                        pc,
                                                        versionId, level);
                                            }
                                        }
                                    }
                                }

                                Product product = productRedisRepository.getProductByProductId(pc.getProductId(), false);
                                if (null == product) {
                                    //对于已经下架的产品，不需要再次发布产品了。
                                    ProductList temp = this.productListRepository.findByProductId(pc.getProductId());
                                    if (null != temp && temp.isEnable()) {
                                        this.productService.setProduct2Redis(temp);
                                        //更新容器自身产品
                                    }
                                }
                            } else if (pc.getType() == ProductContainer.TypeEnum.Folder.getValue()) {
                                Folder folder = siteRedisRepository.getFolderById(pc.getTfolderId());
                                if (null == folder) {
                                    this.folder(pc.getTfolderId());// this.productListRepository.findOne(pc.getProductId()));
                                }
                            } else {
                                Advertise advertise = advertiseRedisRepository.getAdvertiseById(pc.getAdvertiseId());
                                if (null == advertise) {
                                    Advertise temp = this.advertiseRepository.findOne(pc.getAdvertiseId());
                                    if (null != temp && temp.isEnable()) {
                                        //advertiseRedisRepository.setAdvertise(temp);
                                        //重新执行一次发广告的操作
                                        this.advertise(pc.getAdvertiseId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "domain/common/publish";
    }

    /**
     * 发布单个标签。
     */
    @RequestMapping("label/{lid}")
    public String labelPublish(@PathVariable("lid") Integer labelId) {
        String key = RedisKeys.getLabelProductKey(labelId);
        Label label = labelRepository.findByLabelId(labelId);
        List<ProductList> productLists = label.getProducts();
        String[] ids = new String[productLists.size()];
        for (int i =0; i < ids.length;i++){
            ids[i] = productLists.get(i).getProductId().toString();
        }
        labelRedisRepository.setLabelProductList(key, ids);
        return "domain/common/publish";
    }
    /**
     * 发布全部标签
     */
    @RequestMapping("label")
    public String labelPublishAll() {
        for(Label label : labelRepository.findAll()){
            labelPublish(label.getLabelId());
        }
        return "domain/common/publish";
    }


}
