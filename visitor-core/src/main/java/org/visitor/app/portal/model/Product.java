/**
 * 
 */
package org.visitor.app.portal.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductDetail;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.ProductState;

/**
 * @author mengw
 *
 */
public class Product implements Comparable<Product>{
	private ProductList productList;
    private ProductPic iconPic;//icon
    private ProductPic coverPic;//cover img
    
    private String fileSize;//文件大小，不需要预先写入redis
    private Long dlCount; //下载数，不需要预先写入redis
    private Long fileId;//文件ID，不需要预先写入redis
    private List<ProductSiteFolder> folders;
    private Folder folder;
    private transient ProductState productState;
    private List<ProductFile> productFiles;//产品的资源文件，不需要预先写入redis
    
    private String productSourceName;//产品来源名称，通过productList.sourceId从redis中取。
    private transient String highLightName;//高亮显示的产品名称
    private transient String comeFrom;//来自哪种推荐，这个属性只在取出的时候设置，不用存储到Redis中，而且可以为NULL
    private transient int showSequence = 1;//推荐类别的显示顺序，这是主顺序，必须有
    private transient long insertOrder = 0L;//插入时间，同一推荐类别，前台显示的先后顺序，这个字段相当于辅助排序
    
    private transient String oldVersion;//原版本类型
    private ProductPic topicPic;
    
    private List<ProductDetail> detailList;//产品列表，需要序列化
    private transient ProductFile productFile;
    private transient ProductDetail productDetail;//与专题相关的产品细节
    
	/**
	 * 
	 */
	public Product() {
		// TODO Auto-generated constructor stub
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
	 * @return the productList
	 */
	public ProductList getProductList() {
		return productList;
	}

	/**
	 * @param productList the productList to set
	 */
	public void setProductList(ProductList productList) {
		this.productList = productList;
	}

	/**
	 * @return the fileSize
	 */
	@JsonIgnore
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the dlCount
	 */
	@JsonIgnore
	public Long getDlCount() {
		return dlCount;
	}

	/**
	 * @param dlCount the dlCount to set
	 */
	public void setDlCount(Long dlCount) {
		this.dlCount = dlCount;
	}

	/**
	 * @return the fileId
	 */
	@JsonIgnore
	public Long getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the folders
	 */
	public List<ProductSiteFolder> getFolders() {
		return folders;
	}

	/**
	 * @param folders the folders to set
	 */
	public void setFolders(List<ProductSiteFolder> folders) {
		this.folders = folders;
	}
	
	/**
	 * @return the productState
	 */
	@JsonIgnore
	public ProductState getProductState() {
		return productState;
	}

	/**
	 * @param productState the productState to set
	 */
	public void setProductState(ProductState productState) {
		this.productState = productState;
	}

	@JsonIgnore
	public String getStarLevel() {
		if(null != this.getProductList() && null != this.getProductList().getStarLevel()) {
			String start = StringUtils.substring(this.getProductList().getStarLevel(), 0, 1);
			String end = StringUtils.substring(this.getProductList().getStarLevel(), 2, 3);
			if(!StringUtils.equals(start, "0")) {//Whne zero, should be 1.
				if(!StringUtils.equals(end, "0")) {
					return start + "~";
				} else {
					return start;
				}
			}
		}
		return "1";
	}
	
	@JsonIgnore
	public boolean isValid() {
		if(null != getProductList() && null != getProductList().getDownStatus() 
				&& getProductList().getDownStatus().intValue() == ProductList.ENABLE) {
			return true;
		}
		return false;
	}

	/**
	 * @return the productFiles
	 */
	@JsonIgnore
	public List<ProductFile> getProductFiles() {
		return productFiles;
	}

	/**
	 * @param productFiles the productFiles to set
	 */
	public void setProductFiles(List<ProductFile> productFiles) {
		this.productFiles = productFiles;
	}

	public List<ProductDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ProductDetail> detailList) {
		this.detailList = detailList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Product [productList=" + productList + ", iconPic=" + iconPic
				+ ", coverPic=" + coverPic + ", fileSize=" + fileSize
				+ ", dlCount=" + dlCount + ", fileId=" + fileId + ", folders="
				+ folders + ", productState=" + productState
				+ ", productFiles=" + productFiles + ", productSourceName="
				+ productSourceName + "]";
	}

	public void addProductSiteFolder(ProductSiteFolder psf) {
		if(this.folders == null) {
			folders = new ArrayList<ProductSiteFolder>();
		}
		this.folders.add(psf);
	}

	/**
	 * @return the productSourceName
	 */
	@JsonIgnore
	public String getProductSourceName() {
		return productSourceName;
	}

	/**
	 * @param productSourceName the productSourceName to set
	 */
	public void setProductSourceName(String productSourceName) {
		this.productSourceName = productSourceName;
	}
	
	@JsonIgnore
	public Folder getFolder() {
		return this.folder;
	}

	/**
	 * @param folder the folder to set
	 */
	public void setFolder(Folder folder) {
		this.folder = folder;
	}
	

	/**
	 * Symbian版的取星级方法
	 * @return
	 */
	@JsonIgnore
	public Integer getSymbStarLevel() {
		if(null != this.getProductList() && null != this.getProductList().getStarLevel()) {
			String start = StringUtils.substring(this.getProductList().getStarLevel(), 0, 1);
			if(!StringUtils.equals(start, "0")) {//Whne zero, should be 1.
				return Integer.parseInt(start);
			}
		}
		return 1;
	}

	/**
	 * @return the comeFrom
	 */
	@JsonIgnore
	public String getComeFrom() {
		return comeFrom;
	}

	/**
	 * @param comeFrom the comeFrom to set
	 */
	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	/**
	 * @return the showSequence
	 */
	@JsonIgnore
	public int getShowSequence() {
		return showSequence;
	}

	/**
	 * @param showSequence the showSequence to set
	 */
	public void setShowSequence(int showSequence) {
		this.showSequence = showSequence;
	}

	/**
	 * @return the insertOrder
	 */
	@JsonIgnore
	public long getInsertOrder() {
		return insertOrder;
	}

	/**
	 * @param insertOrder the insertOrder to set
	 */
	public void setInsertOrder(long insertOrder) {
		this.insertOrder = insertOrder;
	}
	
	@JsonIgnore
	public String getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
	}

	/**
	 * @return the topicPic
	 */
	public ProductPic getTopicPic() {
		return topicPic;
	}

	/**
	 * @param topicPic the topicPic to set
	 */
	public void setTopicPic(ProductPic topicPic) {
		this.topicPic = topicPic;
	}
	
	@JsonIgnore
	public ProductDetail getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(ProductDetail productDetail) {
		this.productDetail = productDetail;
	}	

	@JsonIgnore
	public ProductFile getProductFile() {
		return productFile;
	}

	public void setProductFile(ProductFile productFile) {
		this.productFile = productFile;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((productList == null) ? 0 : productList.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		Product other = (Product) obj;
		if (productList == null) {
			if (other.productList != null)
				return false;
		} else if (!productList.equals(other.productList))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Product o) {
		// TODO Auto-generated method stub
		if(this == o || this.equals(o)){
			return 0;
		}else {
			if(this.getShowSequence() == o.getShowSequence()){//如果主次序相同，比较副次序
				long myOrderId = this.getInsertOrder();
				long compareId = o.getInsertOrder();
			
				return myOrderId>compareId?1:-1;
			}else {
				return this.showSequence > o.getShowSequence() ? 1:-1;
			}
		}
	}
	
	/**
	 * 判断产品是否绑定到指定站点
	 * @param siteId
	 * @return true-已绑定到指定站点；false-未绑定
	 */
	public boolean matchSiteId(Integer siteId) {
		if(null != this.getFolders() && null != siteId) {
			for(ProductSiteFolder psf : this.getFolders()) {
				if(null != psf && psf.getSiteId() != null && psf.getSiteId().intValue() == siteId.intValue()) {
					return true;
				}
			}
		}
		return false;
	}

	public String getHighLightName() {
		return highLightName;
	}

	public void setHighLightName(String highLightName) {
		this.highLightName = highLightName;
	}
	
}
