/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;

import org.springframework.data.jpa.domain.Specification;

import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.web.utils.SearchForm;

public class ProductPicSearchForm extends SearchForm<ProductPic> implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProductPic productPic = new ProductPic();
    private Long proId;//产品id
    private Long ppId;//productPicId
    

    public Long getPpId() {
		return ppId;
	}

	public void setPpId(Long ppId) {
		this.ppId = ppId;
	}

	public Long getProId() {
		return proId;
	}

	public void setProId(Long proId) {
		this.proId = proId;
	}

	/**
     * The ProductPic instance used as an example.
     */
    public ProductPic getProductPic() {
        return productPic;
    }

	@Override
	public Specification<ProductPic> toSpecification() {
		// TODO Auto-generated method stub
		return null;
	}
}