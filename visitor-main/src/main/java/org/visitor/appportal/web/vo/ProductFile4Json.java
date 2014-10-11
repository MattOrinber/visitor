/**
 * 
 */
package org.visitor.appportal.web.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import org.visitor.appportal.domain.ProductFile;

/**
 * @author mengw
 *
 */
public class ProductFile4Json {
	// Raw attributes
	private Long fileId; // pk
	private String filePath;
	private String fileUrl;
	private String fileName; // not null
	private String fileSize; // not null
	private String fileSuffix; // not null
	private Integer status; // not null
	private String createBy; // not null
	private Date createDate; // not null
	private String modBy; // not null
	private Date modDate; // not null
	private String jarMd5;
	private String jadMd5; // not null
	private Long dlCount; // not null

	// Technical attributes for query by example
	private Long productId; // not null
	private String minVersion;
    private Long osId;
    private Long platformId;
    private String versionOperator;
	private String packageName;
	private Integer versionCode;
	private String versionName;
	private Integer sdkVersion;
	private Integer minSdkVersion;
	private Integer targetSdkVersion;
	private String identity;
	
	private List<ProductFileSuite4Json> resolutions;
	/**
	 * 
	 */
	public ProductFile4Json() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the fileId
	 */
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
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the fileUrl
	 */
	public String getFileUrl() {
		return fileUrl;
	}
	/**
	 * @param fileUrl the fileUrl to set
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the fileSize
	 */
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
	 * @return the fileSuffix
	 */
	public String getFileSuffix() {
		return fileSuffix;
	}
	/**
	 * @param fileSuffix the fileSuffix to set
	 */
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the modBy
	 */
	public String getModBy() {
		return modBy;
	}
	/**
	 * @param modBy the modBy to set
	 */
	public void setModBy(String modBy) {
		this.modBy = modBy;
	}
	/**
	 * @return the modDate
	 */
	public Date getModDate() {
		return modDate;
	}
	/**
	 * @param modDate the modDate to set
	 */
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}
	/**
	 * @return the jarMd5
	 */
	public String getJarMd5() {
		return jarMd5;
	}
	/**
	 * @param jarMd5 the jarMd5 to set
	 */
	public void setJarMd5(String jarMd5) {
		this.jarMd5 = jarMd5;
	}
	/**
	 * @return the jadMd5
	 */
	public String getJadMd5() {
		return jadMd5;
	}
	/**
	 * @param jadMd5 the jadMd5 to set
	 */
	public void setJadMd5(String jadMd5) {
		this.jadMd5 = jadMd5;
	}
	/**
	 * @return the dlCount
	 */
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
	 * @return the productId
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	/**
	 * @return the minVersion
	 */
	public String getMinVersion() {
		return minVersion;
	}
	/**
	 * @param minVersion the minVersion to set
	 */
	public void setMinVersion(String minVersion) {
		this.minVersion = minVersion;
	}
	/**
	 * @return the resolutions
	 */
	public List<ProductFileSuite4Json> getResolutions() {
		return resolutions;
	}
	/**
	 * @param resolutions the resolutions to set
	 */
	public void setResolutions(List<ProductFileSuite4Json> resolutions) {
		this.resolutions = resolutions;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductFile4Json [fileId=" + fileId + ", filePath=" + filePath
				+ ", fileUrl=" + fileUrl + ", fileName=" + fileName
				+ ", fileSize=" + fileSize + ", fileSuffix=" + fileSuffix
				+ ", status=" + status + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", modBy=" + modBy
				+ ", modDate=" + modDate + ", jarMd5=" + jarMd5 + ", jadMd5="
				+ jadMd5 + ", dlCount=" + dlCount + ", productId=" + productId
				+ ", resolutions=" + resolutions + "]";
	}
	
	public ProductFile toProductFile() {
		ProductFile productFile = new ProductFile();
		productFile.setFileId(getFileId());
		productFile.setFilePath(getFilePath());
		productFile.setFileUrl(getFileUrl());
		productFile.setProductId(getProductId());
		productFile.setFileName(getFileName());
		if(StringUtils.isNotBlank(getFileSize()) 
				&& StringUtils.isNumeric(getFileSize())) {
			try {
			productFile.setFileSize(FileUtils.byteCountToDisplaySize(Long.valueOf(getFileSize())));
			}catch (Exception e) {
				productFile.setFileSize(getFileSize());
			}
		} else {
			productFile.setFileSize(getFileSize());
		}
		productFile.setFileSuffix(getFileSuffix());
		productFile.setStatus(getStatus());
		productFile.setCreateBy(getCreateBy());
		productFile.setCreateDate(getCreateDate());
		productFile.setModBy(getModBy());
		productFile.setModDate(getModDate());
		productFile.setJarMd5(getJarMd5());
		productFile.setJadMd5(getJadMd5());
		productFile.setDlCount(getDlCount());
		productFile.setVersionOperator(getVersionOperator());
        productFile.setPackageName(getPackageName());
        productFile.setVersionCode(getVersionCode());
        productFile.setVersionName(getVersionName());
        productFile.setSdkVersion(getSdkVersion());
        productFile.setMinSdkVersion(getMinSdkVersion());
        productFile.setTargetSdkVersion(getTargetSdkVersion());
        productFile.setIdentity(getIdentity());
		return productFile;
	}
	/**
	 * @return the osId
	 */
	public Long getOsId() {
		return osId;
	}
	/**
	 * @param osId the osId to set
	 */
	public void setOsId(Long osId) {
		this.osId = osId;
	}
	/**
	 * @return the platformId
	 */
	public Long getPlatformId() {
		return platformId;
	}
	/**
	 * @param platformId the platformId to set
	 */
	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}
	/**
	 * @return the versionOperator
	 */
	public String getVersionOperator() {
		return versionOperator;
	}
	/**
	 * @param versionOperator the versionOperator to set
	 */
	public void setVersionOperator(String versionOperator) {
		this.versionOperator = versionOperator;
	}
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * @return the versionCode
	 */
	public Integer getVersionCode() {
		return versionCode;
	}
	/**
	 * @param versionCode the versionCode to set
	 */
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return versionName;
	}
	/**
	 * @param versionName the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	/**
	 * @return the sdkVersion
	 */
	public Integer getSdkVersion() {
		return sdkVersion;
	}
	/**
	 * @param sdkVersion the sdkVersion to set
	 */
	public void setSdkVersion(Integer sdkVersion) {
		this.sdkVersion = sdkVersion;
	}
	/**
	 * @return the minSdkVersion
	 */
	public Integer getMinSdkVersion() {
		return minSdkVersion;
	}
	/**
	 * @param minSdkVersion the minSdkVersion to set
	 */
	public void setMinSdkVersion(Integer minSdkVersion) {
		this.minSdkVersion = minSdkVersion;
	}
	/**
	 * @return the targetSdkVersion
	 */
	public Integer getTargetSdkVersion() {
		return targetSdkVersion;
	}
	/**
	 * @param targetSdkVersion the targetSdkVersion to set
	 */
	public void setTargetSdkVersion(Integer targetSdkVersion) {
		this.targetSdkVersion = targetSdkVersion;
	}
	/**
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}
	/**
	 * @param identity the identity to set
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
}
