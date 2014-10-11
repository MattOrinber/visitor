/**
 * 
 */
package org.visitor.app.portal.model;

import org.apache.commons.lang.StringUtils;

/**
 * 系统定义的常量
 * @author mengw
 *
 */
public abstract class SystemConstants {

	/**
	 * 站点中的模板目录
	 */
	public static final String Site_Template_Path = "_template";
	/**
	 * 站点中的单独页所在目录
	 */
	public static final String Fragement_Path = "container";
	/**
	 * 生成的模板文件后缀
	 */
	public static final String Template_File_Suffix = ".ftl";
	
	/**
	 * 标签模板文件后缀
	 */
	public static final String Template_Tag_Suffix = ".ftl";
	//进行预览的页面名称后缀
	public static final String HtmlPagePreview = "preview";

	
	/**
	 * 频道页面的部分路径。即siteId + "/" + path + ".jspx"或者
	 * siteId + "/" + path + "-" + preview + ".jspx"（预览页面使用）
	 * @param siteId
	 * @param path
	 * @param isPreviewPage
	 * @return
	 */
	public static String getHtmlPageFullPath(Integer siteId, String path,
			boolean isPreviewPage) {
		return siteId + (StringUtils.startsWith(path, "/") ? path : "/" + path) + 
			(isPreviewPage ? "-" + HtmlPagePreview : "") + Template_File_Suffix;
	}
}
