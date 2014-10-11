/*

 * Template pack-mvc-3:src/main/java/web/controller/SearchForm.e.vm.java
 */
package org.visitor.appportal.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.repository.base.DateRange;
import org.visitor.appportal.web.utils.DateRangeUtil;
import org.visitor.appportal.web.utils.SearchForm;

public class ProductFileSearchForm extends SearchForm<ProductFile> implements Serializable {
	private static final long serialVersionUID = 1L;
	private ProductFile productFile = new ProductFile();
	/**
	 * 适配机型,以半角逗号分隔的机型名称列表。
	 */
	private String models;
	private List<Long> repeated = new ArrayList<Long>();
	private List<Long> unmatch = new ArrayList<Long>();
	private List<Long> matched = new ArrayList<Long>();
	private Long fileId;
	
	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public List<Long> getRepeated() {
		return repeated;
	}

	public void setRepeated(List<Long> repeated) {
		this.repeated = repeated;
		if (repeated==null) {
			this.repeated=new ArrayList<Long>();
		}
	}

	public List<Long> getUnmatch() {
		return unmatch;
	}

	public void setUnmatch(List<Long> unmatch) {
		this.unmatch = unmatch;
	}

	public List<Long> getMatched() {
		return matched;
	}

	public void setMatched(List<Long> matched) {
		this.matched = matched;
		if (matched==null) {
			this.matched=new ArrayList<Long>();
		}
	}

	private CommonsMultipartFile fileJar;

	public CommonsMultipartFile getFileJar() {
		return fileJar;
	}

	public void setFileJar(CommonsMultipartFile fileJar) {
		this.fileJar = fileJar;
	}

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}

	/**
	 * The ProductFile instance used as an example.
	 */
	public ProductFile getProductFile() {
		return productFile;
	}

	// ------------------------
	// support for date ranges
	// ------------------------
	private DateRangeUtil createDateRange = new DateRangeUtil("createDate");
	private DateRangeUtil modDateRange = new DateRangeUtil("modDate");

	/**
	 * The {@link DateRangeUtil} for the createDate attribute.
	 */
	public DateRangeUtil getCreateDateRange() {
		return createDateRange;
	}

	/**
	 * The {@link DateRangeUtil} for the modDate attribute.
	 */
	public DateRangeUtil getModDateRange() {
		return modDateRange;
	}

	@Override
	protected List<DateRange> getDateRanges() {
		List<DateRange> result = new ArrayList<DateRange>();
		result.add(getCreateDateRange());
		result.add(getModDateRange());
		return result;
	}

	@Override
	public Specification<ProductFile> toSpecification() {
		return new Specification<ProductFile>(){

			@Override
			public Predicate toPredicate(Root<ProductFile> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if(null != createDateRange.getFromDate()) {
					Path<Date> from = root.get("createDate");
					predicate = cb.and(predicate, cb.greaterThanOrEqualTo(from, createDateRange.getFromDate()));
				}
				
				if(null != createDateRange.getToDate()) {
					Path<Date> from = root.get("createDate");
					predicate = cb.and(predicate, cb.lessThanOrEqualTo(from, createDateRange.getFromDate()));
				}
				if(StringUtils.isNotBlank(getProductFile().getFileSuffix())) {
					Path<String> fileSuffix = root.get("fileSuffix");
					predicate = cb.and(predicate, cb.like(fileSuffix,
							StringUtils.lowerCase(getProductFile().getFileSuffix())));
				}
				if(StringUtils.isNotBlank(getProductFile().getFileName())) {
					Path<String> fileSuffix = root.get("fileName");
					predicate = cb.and(predicate, cb.like(fileSuffix,
							StringUtils.lowerCase(getProductFile().getFileName())));
				}
				//增加对产品ID的限制
				if(null != getProductFile().getProductId()) {
					Path<String> productId = root.get("productId");
					predicate = cb.and(predicate, cb.equal(productId, getProductFile().getProductId()));					
				}
				if(null != getProductFile().getStatus()) {
					predicate = cb.and(predicate, cb.equal(root.get("status"), getProductFile().getStatus()));					
				}
				if(StringUtils.isNotBlank(getProductFile().getFilePath())) {
					Path<String> filePath = root.get("filePath");
					predicate = cb.and(predicate, cb.like(filePath,
							StringUtils.lowerCase(getProductFile().getFilePath())));
				}
				if(getProductFile().getSafeType() != null) {
					Path<String> safeType = root.get("safeType");
					predicate = cb.and(predicate, cb.equal(safeType, getProductFile().getSafeType()));
				}
				if(getProductFile().getAutoScan() != null) {
					Path<String> autoScan = root.get("autoScan");
					predicate = cb.and(predicate, cb.equal(autoScan, getProductFile().getAutoScan()));
				}
				if(StringUtils.isNotBlank(getProductFile().getFileName())) {
					Path<String> fileName = root.get("fileName");
					predicate = cb.and(predicate, cb.like(fileName,
							"%" + StringUtils.lowerCase(getProductFile().getFileName()) + "%"));
				}
				if(null != getProductFile().getProduct()) {
					ProductList product = getProductFile().getProduct();
					if(StringUtils.isNotEmpty(product.getName())) {
						Path<String> name = root.get("product").get("name");
						predicate = cb.and(predicate, cb.like(name, "%" + StringUtils.lowerCase(product.getName() + "%")));
					}
					if(null != product.getOperatorId()) {
						predicate = cb.and(predicate, cb.equal(root.get("product").get("operatorId"), product.getOperatorId()));
					}
					if(null != product.getCategoryId()) {
						predicate = cb.and(predicate, cb.equal(root.get("product").get("categoryId"), product.getCategoryId()));
					}
					if(null != product.getProductId()) {
						predicate = cb.and(predicate, cb.equal(root.get("product").get("productId"), product.getProductId()));
					}
					if(null != product.getSourceId()) {
						predicate = cb.and(predicate, cb.equal(root.get("product").get("sourceId"), product.getSourceId()));
					}
					if(null != product.getProductType()) {
						predicate = cb.and(predicate, cb.equal(root.get("product").get("productType"), product.getProductType()));
					}
				}
				return predicate;
			}
			
		};
	}

}