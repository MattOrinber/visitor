/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.visitor.appportal.domain.Label;
import org.visitor.appportal.repository.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.app.portal.model.IntelligentItemRecommend;
import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.RecommendRule.RecommendRuleTypeEnum;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.site.ProductListService;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;
import org.visitor.appportal.web.vo.RecommendProduct;

@Controller
@RequestMapping("/domain/productlist/")
public class ProductListControllerWithPathVariable extends BaseController {
    @Autowired
    Properties systemProperties;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private ProductListRepository productListRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductFileRepository productFileRepository;
    @Autowired
    private ProductPicRepository productPicRepository;
    @Autowired
    private ProductSiteFolderRepository productSiteFolderRepository;
    @Autowired
    private ProductContainerRepository productContainerRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private LabelRepository labelRepository;


    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p/>
     * The path variable is converted by SpringMVC to a ProductList via the
     * {@link ProductListFormatter}. Before being passed as an argument to the
     * handler, SpringMVC binds the attributes on the resulting model, then each
     * handler method may receive the entity, potentially modified, as an
     * argument.
     */
    @ModelAttribute
    public ProductList getProductList(@PathVariable("pk") Long pk) {
        return productListRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     * 代码转移到Service
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute ProductList productList, Model model,
                       HttpServletRequest request) {

        Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
        if (siteId != null) {

            ProductListService pls = this.getServiceFactory().getProductListService(siteId);
            pls.show(productList, model);

            return "domain/productlist/show";
        } else {
            return "redirect:/login";
        }
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update(Model model) {
        model.addAttribute("safeTypeList", Arrays.asList(ProductList.SafeTypeEnum.values()));
        model.addAttribute("productCategoryList", categoryService.findCategorySelectTree(Category.PRODUCT_CATEGORY));
        model.addAttribute("billingTypeList", categoryService.findCategoryChild(Category.BILLING_TYPE, null));
        model.addAttribute("operationList", categoryService.findCategoryChild(Category.OPERATION_MODEL, null));
        model.addAttribute("cooperationList", categoryService.findCategoryChild(Category.COOPERATION, null));
        model.addAttribute("operatorList", categoryService.findCategoryChild(Category.OPERATOR, null));
        model.addAttribute("importByList", categoryService.findCategoryChild(Category.IMPORTBY, null));
        model.addAttribute("productSourceList", categoryService.findCategoryChild(Category.PRODUCTSOURCE, null));
        model.addAttribute("merchantList", categoryService.findCategoryChild(Category.MERCHANT, null));
        return "domain/productlist/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = {PUT, POST})
    public String update(@Valid @ModelAttribute ProductList productList, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return update(model);
        }

        if (productList.getCategoryId() != null) {
            productList.setCategory(categoryRepository.findOne(productList.getCategoryId()));
        }

        if (productList.getBillingTypeId() != null) {
            productList.setBillingType(categoryRepository.findOne(productList.getBillingTypeId()));
        }

        if (productList.getOperationModelId() != null) {
            productList.setOperationModel(categoryRepository.findOne(productList.getOperationModelId()));
        }

        if (productList.getCooperationModelId() != null) {
            productList.setCooperationModel(categoryRepository.findOne(productList.getCooperationModelId()));
        }

        if (productList.getOperatorId() != null) {
            productList.setOperator(categoryRepository.findOne(productList.getOperatorId()));
        }

        if (productList.getSourceId() != null) {
            productList.setProductSource(categoryRepository.findOne(productList.getSourceId()));
        }

        if (productList.getMerchantId() != null) {
            productList.setMerchant(categoryRepository.findOne(productList.getMerchantId()));
        }

        Date date = new Date();
        productList.setModDate(date);
        productList.setModBy(AccountContext.getAccountContext().getUsername());
        productListRepository.save(productList);
        return "redirect:/domain/productlist/show/" + productList.getPrimaryKey();
    }

    /**
     * 更新产品下线状态,修改成功跳转到search页面
     *
     * @param productList
     * @param status
     * @param bindingResult
     * @param model
     * @return
     * @author mengw
     */
    @RequestMapping(value = "updateStatus/{pk}", method = {GET, PUT, POST})
    public String updateStatus(@ModelAttribute ProductList productList,//
                               @RequestParam(value = "status", required = true) Integer status,//
                               @RequestParam(value = "reason", required = false) String reason,
                               Model model) {
        productList.setDownStatus(status);
        productList.setDownReason(reason);
        if (productList.getDownStatus().equals(ProductList.ENABLE)) {

            Date date = new Date();
            productList.setModDate(date);
            productList.setModBy(AccountContext.getAccountContext().getUsername());
        }

        productListRepository.save(productList);
        return "redirect:/domain/productlist/show/" + productList.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really
     * deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete(Model model, @RequestParam(required = false, value = "type") Integer type) {
        model.addAttribute("type", type);
        return "domain/productlist/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = {PUT, POST, DELETE})
    public String delete(@ModelAttribute ProductList productList, @RequestParam(required = false, value = "type") Integer type) {
        //当前状态是下架，改为上架
        if (null != productList.getDownStatus() && productList.getDownStatus().intValue() == ProductList.DISNABLE
                && productList.getSafeType().intValue() != ProductList.SafeTypeEnum.Unsafe.ordinal()) {
            //只有在产品为未知或者是安全的情况下，才可以上架
            productList.setDownStatus(ProductList.ENABLE);
            productList.setDownReason(null);
        } else {
            //下架，下架原因？
            productList.setDownStatus(ProductList.DISNABLE);
        }
        productList.setModDate(new Date());
        productList.setModBy(AccountContext.getAccountContext().getUsername());
        this.productListRepository.save(productList);

        if (productList.getDownStatus().intValue() == ProductList.DISNABLE) {
            productService.setProduct2Redis(productList);
        }

        if (null != type && type.intValue() == 1) {
            //如果是下架，则操作结束后显示详情页
            return "redirect:/domain/productlist/show/" + productList.getPrimaryKey();
        }
        return "redirect:/domain/productlist/search";
    }

    /**
     * Serves the safetype form asking the user if the entity should be really
     * deleted.
     */
    @RequestMapping(value = "safetype/{pk}", method = GET)
    public String safeType(@ModelAttribute ProductList productList, Model model) {
        model.addAttribute("fileSafeTypeList", Arrays.asList(ProductFile.SafeTypeEnum.values()));
        model.addAttribute("productFiles", productFileRepository.findFileByProductId(productList.getPrimaryKey()));
        return "domain/productlist/safetype";
    }

    /**
     * Performs the safetype action and redirect to the search view.
     */
    @RequestMapping(value = "safetype/{pk}", method = {PUT, POST, DELETE})
    public String safetype(@ModelAttribute ProductList productList,
                           @RequestParam(required = true, value = "fileIds") List<String> fileIds,
                           @RequestParam(required = true, value = "safeTypes") List<String> safeTypes,
                           @RequestParam(required = true, value = "autoScans") List<String> autoScans) {

        List<ProductFile> productFiles = productFileRepository.findFileByProductId(productList.getPrimaryKey());

        if (!fileIds.isEmpty() && !safeTypes.isEmpty() && !autoScans.isEmpty() && !productFiles.isEmpty()
                && fileIds.size() == productFiles.size()
                && safeTypes.size() == productFiles.size()
                && autoScans.size() == productFiles.size()) {
            for (int i = 0; i < productFiles.size(); i++) {
                productFiles.get(i).setSafeType(Integer.valueOf(safeTypes.get(i)));
                productFiles.get(i).setAutoScan(Integer.valueOf(autoScans.get(i)));
            }

            productFileRepository.save(productFiles);

            //更新产品安全状态
            if (safeTypes.contains(ProductFile.SafeTypeEnum.Safe.getValue().toString())) {
                productList.setSafeType(ProductList.SafeTypeEnum.Safe.ordinal());
            }
            if (safeTypes.contains(ProductFile.SafeTypeEnum.Unknown.getValue().toString())) {
                productList.setSafeType(ProductList.SafeTypeEnum.Unknown.ordinal());
            }
            if (safeTypes.contains(ProductFile.SafeTypeEnum.Lowrisk.getValue().toString())
                    || safeTypes.contains(ProductFile.SafeTypeEnum.Midrisk.getValue().toString())
                    || safeTypes.contains(ProductFile.SafeTypeEnum.Virus.getValue().toString())) {
                productList.setSafeType(ProductList.SafeTypeEnum.Unsafe.ordinal());
            }
            productListRepository.save(productList);

        }
        return "redirect:/domain/productlist/show/" + productList.getPrimaryKey();
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "preview/recommend/{pk}", method = {GET})
    public String previewrecommend(@ModelAttribute ProductList productList, Model model, HttpServletRequest request) {
        Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
        List<IntelligentItemRecommend> intelligentItemRecommends = productService.getIntelligentItemRecommends(siteId, productList);
        List<RecommendProduct> rps = new ArrayList<RecommendProduct>();
        if (intelligentItemRecommends != null && intelligentItemRecommends.size() > 0) {
            for (IntelligentItemRecommend ii : intelligentItemRecommends) {
                if (ii != null) {
                    String name = RecommendRuleTypeEnum.getInstance(ii.getName()).getDisplayName();
                    List<Long> pids = ii.getProductIds();
                    List<ProductList> productLists = new ArrayList<ProductList>();
                    if (pids != null && pids.size() > 0) {
                        productLists = productListRepository.findByProductIds(pids);
                    }
                    RecommendProduct rp = new RecommendProduct();
                    rp.setName(name);
                    rp.setProductLists(productLists);
                    rps.add(rp);
                }
            }
        }

        model.addAttribute("recommendProducts", rps);
        return "domain/productlist/recommend";
    }

    @RequestMapping(value = "/label/*/{pk}", method = GET)
    public String labelSearch(@ModelAttribute LabelSearchForm labelSearchForm, @PathVariable("pk") Long pk, Model model) {
        model.addAttribute("pk", pk);
        return "domain/productlist/label/addLabels";
    }

    @RequestMapping(value = "/addLabelList/{itemIds}/{pk}", method = GET)
    public String deleteMulti(@PathVariable("itemIds") String itemIds, @PathVariable("pk") Long pk) {
        ProductList productList = productListRepository.findByProductId(pk);
        if (StringUtils.isNotBlank(itemIds)) {
            String arrOfItem[] = itemIds.split("_");
            for (String itemId : arrOfItem) {
                Label label = labelRepository.findByLabelId(Integer.parseInt(itemId));
                if (label.getProducts().contains(productList)) {
                    continue;
                } else {
                    label.getProducts().add(productList);
                    productList.getLabels().add(label);
                }
            }
            productListRepository.save(productList);
        }
        return "redirect:/domain/productlist/show/" + productList.getPrimaryKey();
    }
}