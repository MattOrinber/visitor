/**
 * 
 */
package org.visitor.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductOperation;
import org.visitor.appportal.domain.ProductPic;

/**
 * @author mengw
 *
 */
public class AppStringUtils {
	public static final String TIMESTAMP_FORMAT = "yyyyMMddhhmmssSSS";
	
    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;
    
	/**
	 * 根据名称来获取高度和宽度
	 * @param resolution
	 * @return
	 */
	public static String[] getScreenWidthAndHeight(String resolution) {
		String arrays[] = StringUtils.split(resolution.toUpperCase(), "X");
		if(null != arrays) {
			for(int i=0;i<arrays.length;i++) {
				arrays[i] = StringUtils.trimToEmpty(arrays[i]);
			}
		}
		return arrays;
	}
	// 过滤特殊字符
	public static String stringFilter(String str) {   
		// 只允许字母和数字		
        // String   regEx  =  "[^a-zA-Z0-9]";                   
        // 清除掉所有特殊字符
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）" +
				"——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(str);   
		return m.replaceAll("").trim();   
	}   
 	
	/**
	 * 根据分辨率得到屏幕面积（高X宽）
	 * @param resolution
	 * @return
	 */
	public static Long getScreenSize(String resolution) {
		String[] array = getScreenWidthAndHeight(resolution);
		if(array.length == 2) {
			if(StringUtils.isNumeric(array[0]) && StringUtils.isNumeric(array[1])) {
				return Long.valueOf(Integer.valueOf(array[0]) * Integer.valueOf(array[1]));
			}
		}
		return null;
	}
	
    /**
     * Returns a human-readable version of the file size, where the input
     * represents a specific number of bytes.
     *
     * @param size  the number of bytes
     * @return a human-readable display value (includes units)
     */
    public static String byteCountToDisplaySize(long size) {
        String displaySize;

        if (size / ONE_GB > 0) {
            displaySize = String.valueOf(size / ONE_GB) + " G";
        } else if (size / ONE_MB > 0) {
            displaySize = String.valueOf(size / ONE_MB) + " M";
        } else if (size / ONE_KB > 0) {
            displaySize = String.valueOf(size / ONE_KB) + " K";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }
    
	/**
	 * 随机数，起始＋范围
	 * @param start 起始值（最小值）
	 * @param range（范围）
	 * @return
	 */
	public static long getRandomNumber(int start, int range) {
		return Long.valueOf((int)(Math.random()*range + start));
	}
    
	/**
	 * '操作类型。0-修改信息；2-绑定频道；3-上传文件；4-换包；5-修改适配机型；
	 * 6-上传截图；7-修改封面图；8-修改图标；
	 * 9-删除截图；10-设置截图为封面图',
	 * @param productPic
	 * @return
	 */
	public static ProductOperation getChangePicOperation(ProductPic productPic) {
		ProductOperation operation = new ProductOperation();
		operation.setProductId(productPic.getProductId());
		if(productPic.getPicType().intValue() == ProductPic.COVER) {
			operation.setContent("更换封图：" + productPic.getProductPicId());
			operation.setType(ProductOperation.OperationTypeEnum.ChangeCover.ordinal());
		} else if(productPic.getPicType().intValue() == ProductPic.ICON){
			operation.setContent("更换图标：" + productPic.getProductPicId());
			operation.setType(ProductOperation.OperationTypeEnum.ChangeIcon.ordinal());
		}
		operation.setCreateDate(new Date());
		return operation;
	}
	
	/**
	 * 创建截图
	 * @param productPic
	 * @return
	 */
	public static ProductOperation getSavePicOperation(ProductPic productPic) {
		ProductOperation operation = new ProductOperation();
		operation.setProductId(productPic.getProductId());
		operation.setContent("上传截图：" + productPic.getProductPicId());
		operation.setType(ProductOperation.OperationTypeEnum.UploadIllu.ordinal());
		operation.setCreateDate(new Date());
		return operation;
	}
	
	/**
	 * 设为封面图
	 * @param oldCover 
	 * @param newCover
	 * @return
	 */
	public static ProductOperation getToCoverOperation(ProductPic newCover, ProductPic oldCover) {
		ProductOperation operation = new ProductOperation();
		operation.setProductId(newCover.getProductId());
		operation.setContent("设置新封面图：" + newCover.getProductPicId() + "，原封面图：" + oldCover.getProductPicId());
		operation.setType(ProductOperation.OperationTypeEnum.SetCover.ordinal());
		operation.setCreateDate(new Date());
		return operation;
	}
	
	/**
	 * 创建新产品
	 * @param productList
	 * @param iconPic 
	 * @param coverPic 
	 * @return
	 */
	public static ProductOperation getCreateOperation(ProductList productList, 
			ProductPic coverPic, ProductPic iconPic) {
		ProductOperation operation = new ProductOperation();
		operation.setProductId(productList.getProductId());
		operation.setContent("创建产品：" + productList.getName() + 
				"，封面图：" + coverPic.getProductPicId() + 
				"，截图：" + iconPic.getProductPicId());
		operation.setType(ProductOperation.OperationTypeEnum.Create.ordinal());
		operation.setCreateDate(new Date());
		return operation;
	}
	
	/**
	 * 上传产品文件日志
	 * @param productFile
	 * @return
	 */
	public static ProductOperation getUploadFileOperation(ProductFile productFile) {
		ProductOperation operation = new ProductOperation();
		operation.setProductId(productFile.getProductId());
		operation.setContent("上传文件：" + productFile.getFileId());
		operation.setType(ProductOperation.OperationTypeEnum.UploadFile.ordinal());
		operation.setCreateDate(new Date());
		return operation;
	}
	
	/**
	 * 文件换包日志
	 * @param productFile
	 * @return
	 */
	public static ProductOperation getChangeFileOperation(
			ProductFile productFile) {
		ProductOperation operation = new ProductOperation();
		operation.setProductId(productFile.getProductId());
		operation.setContent("更换文件：" + productFile.getFileId());
		operation.setType(ProductOperation.OperationTypeEnum.ChangeFile.ordinal());
		operation.setCreateDate(new Date());
		return operation;
	}
	
	/**
	 * 删除产品文件
	 * @param productFile
	 * @return
	 */
	public static ProductOperation getDeleteFileOperation(
			ProductFile productFile) {
		ProductOperation operation = new ProductOperation();
		operation.setProductId(productFile.getProductId());
		operation.setContent("删除文件：" + productFile.getFileId());
		operation.setType(ProductOperation.OperationTypeEnum.DeleteFile.ordinal());
		operation.setCreateDate(new Date());
		return operation;
	}
	
	public static ProductOperation getOnlineFileOperation(
			ProductFile productFile) {
		ProductOperation operation = new ProductOperation();
		operation.setProductId(productFile.getProductId());
		operation.setContent("文件上线：" + productFile.getFileId());
		operation.setType(ProductOperation.OperationTypeEnum.OnlineFile.ordinal());
		operation.setCreateDate(new Date());
		return operation;
	}	
	
	/**
	 * 产生指定范围的的maxCount个随机数。即每个随机数最大不会超过maxNumber。
	 * 算法的随机
	 * @param maxCount 产生的随机数个数
	 * @param maxNumber 产生的随机数中的最大数
	 * @return
	 */
	public static Integer[] getRandomNumbers(int maxCount, int maxNumber) {
		//这个算法的危险之处就在于，如果maxCount>maxNumber，就会进入死循环
		if(maxCount>maxNumber) return null; 
		
		Integer[] intRet = new Integer[maxCount];

		int intRd = 0; // 存放随机数
		int count = 0; // 记录生成的随机数个数
		int flag = 0; // 是否已经生成过标志
		
		while (count < maxCount) {
			Random rdm = new Random(System.currentTimeMillis());
			intRd = Math.abs(rdm.nextInt()) % (maxNumber + 1);
			flag = 0;
			for (int i = 0; i < count; i++) {
				if (intRet[i] == intRd) {
					flag = 1;
					break;
				}
			}
			
			if (flag == 0) {
				intRet[count++] = new Integer(intRd);
			}
		}
		return intRet;
	}

	public static String getTruncatedFileSize(String fileSize) {
		if(StringUtils.isNotBlank(fileSize)) {
			fileSize = StringUtils.remove(fileSize, " ");
			if(!StringUtils.contains(fileSize, "byte")) {
				fileSize = StringUtils.remove(fileSize.toUpperCase(), "B");
			}
		}
		return fileSize;
	}
	
	public static String numberToDisplaySize(Long size) {
        return String.valueOf(size);
	}
	
	public static List<Long> formatStringArrayToLongList(String text, String split) {
		if (StringUtils.isNotEmpty(text)) {
			String[] text_ = text.split(split);
			List<Long> texts = new ArrayList<Long>();
			for (int i=0 ; i < text_.length ; i++) {
				texts.add(Long.valueOf(text_[i]));
			}
			return texts;
		} else {
			return null;
		}
	}
	public static Long[] getRangeLongValues(String[] arrays, Long pageNumber, Integer pageSize) {
		if(pageNumber == null) {
			pageNumber = 1l;
		}
		Long[] result = new Long[arrays.length];
		for(int i=0;i<arrays.length;i++) {
			if(StringUtils.isNotBlank(arrays[i]) && StringUtils.isNumeric(arrays[i])) {
				result[i] = Long.valueOf(arrays[i]);
			}
		}
		return result;
	}
	
	/**
	 * 下载链接的相对路径uid/branding/timestamp/file/path.apk
	 * @param uid
	 * @param branding
	 * @param path
	 * @return
	 */
	public static String getDownloadFilePath(String uid, String branding, String path) {
		//"/" + upf.getUid() + "/" + upf.getBranding() + "/file" + productFile.getFilePath()
		return "/" + uid + "/" + branding + "/" + new DateTime().toString(TIMESTAMP_FORMAT) + "/file" + path;
	}

	public static String getStringValue(Object value) {
		return null == value ? null : String.valueOf(value);
	}
	
	public static Long getLongValue(String value) {
		if(StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
			return Long.valueOf(value);
		}
		return null;
	}
	
	public static Long getLongValue(Object fieldValue) {
		if(null != fieldValue) {
			return (Long)fieldValue;
		}
		return null;
	}
	public static Double getDoubleValue(Object fieldValue) {
		if(null != fieldValue) {
			return (Double)fieldValue;
		}
		return null;
	}
	public static Date getDateValue(Object fieldValue) {
		if(null != fieldValue) {
			return (Date)fieldValue;
		}
		return null;
	}
	public static Integer getIntegerValue(Object fieldValue) {
		if(null != fieldValue) {
			return (Integer)fieldValue;
		}
		return null;
	}
	
}
