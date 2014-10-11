package org.visitor.appportal.service.game;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Label;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.redis.RedisKeys;
import org.visitor.appportal.repository.LabelRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.service.site.SiteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuxb on 14-9-3.
 */

@Service
public class LabelService {

    @Autowired
    private ProductListRepository productListRepository;
    @Autowired
    private LabelRepository labelRepository;


    public boolean addproductForLabel(Label label, BindingResult bindingResult,
                                      String product) {
        //1. 验证产品ID的合法性
        product = StringUtils.trim(product);
        product = StringUtils.replaceEach(product, new String[]{",", "，", " "}, new String[]{";", ";", ";"});
        String[] productSize = StringUtils.split(product, ";");

        List<Long> prdIds = new ArrayList<Long>();

        StringBuffer sb = new StringBuffer();
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        for (String productId : productSize) {
            if (StringUtils.isNotBlank(productId) && StringUtils.isNumeric(productId)) {
                prdIds.add(Long.valueOf(productId));
            } else {
                sb.append(productId).append(" ");
            }
        }
        if (sb.length() > 0) {
            bindingResult.rejectValue("productId", "productContainer.productId.invalideNumber",
                    "产品ID[。" + sb.toString() + "] 不是数字。");
            return false;
        }


        //验证产品是否存在
        for (Long productId : prdIds) {
            ProductList productList = productListRepository.findByProductId(productId);
            if (productList == null) {
                sb1.append(productId).append(";");
            }else if (label.getProducts().contains(productList)){//验证是否重复
                sb2.append(productId).append(";");
            }else {
                productList.getLabels().add(label);
                label.getProducts().add(productList);

            }

        }
        if (sb1.length() > 0) {// 验证产品是否存在
            bindingResult.rejectValue("productId", "productContainer.productId.none",
                    new String[]{sb.toString()}, "产品[" + sb.toString() + "] 不存在，或者不属于当前站点。");
            return false;
        }

        if (sb2.length() > 0) {// 验证重复推荐
            bindingResult.rejectValue("productId", "productContainer.productId.binded",
                    new String[]{sb1.toString()}, "产品[" + sb1.toString() + "] 已经绑定了。");
            return false;
        }
        Date date = new Date();
        label.setModifyDate(date);
        label.setModifyBy(AccountContext.getAccountContext().getAccount().getUsername());
        labelRepository.save(label);
        return true;
    }

}