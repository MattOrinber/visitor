/**
 * 
 */
package org.visitor.appportal.web.vo;

import java.util.List;

import org.visitor.appportal.domain.MessageContent;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductPic;

/**
 * @author mengw
 *
 */
public class Product4Json {
	private ProductList product;
	private List<ProductFile4Json> productFiles;
	private ProductPic iconPic;
	private ProductPic coverPic;
	private List<ProductPic> illuPics;
	private List<MessageContent> comments;
	
	private List<Long> folders;
	/**
	 * 
	 */
	public Product4Json() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the product
	 */
	public ProductList getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(ProductList product) {
		this.product = product;
	}
	/**
	 * @return the productFiles
	 */
	public List<ProductFile4Json> getProductFiles() {
		return productFiles;
	}
	/**
	 * @param productFiles the productFiles to set
	 */
	public void setProductFiles(List<ProductFile4Json> productFiles) {
		this.productFiles = productFiles;
	}
	/**
	 * @return the iconPic
	 */
	public ProductPic getIconPic() {
		return iconPic;
	}
	/**
	 * @param iconPic the iconPic to set
	 */
	public void setIconPic(ProductPic iconPic) {
		this.iconPic = iconPic;
	}
	/**
	 * @return the coverPic
	 */
	public ProductPic getCoverPic() {
		return coverPic;
	}
	/**
	 * @param coverPic the coverPic to set
	 */
	public void setCoverPic(ProductPic coverPic) {
		this.coverPic = coverPic;
	}
	/**
	 * @return the illuPics
	 */
	public List<ProductPic> getIlluPics() {
		return illuPics;
	}
	/**
	 * @param illuPics the illuPics to set
	 */
	public void setIlluPics(List<ProductPic> illuPics) {
		this.illuPics = illuPics;
	}
	/**
	 * @return the folders
	 */
	public List<Long> getFolders() {
		return folders;
	}
	/**
	 * @param folders the folders to set
	 */
	public void setFolders(List<Long> folders) {
		this.folders = folders;
	}
	/**
	 * @return the comments
	 */
	public List<MessageContent> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<MessageContent> comments) {
		this.comments = comments;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Product4Json [product=" + product + ", productFiles="
				+ productFiles + ", iconPic=" + iconPic + ", coverPic="
				+ coverPic + ", illuPics=" + illuPics + ", comments="
				+ comments + ", folders=" + folders + "]";
	}

}
