package org.visitor.appportal.web.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.DefaultTagProvider;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagInfo;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.visitor.app.portal.model.SystemConstants;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.Template;
import org.visitor.appportal.domain.Template.TypeEnum;
import org.visitor.appportal.service.newsite.SystemPreference;

@Component("TemplatePage")
@Scope("prototype")
public class TemplatePage {
	private static Logger logger = LoggerFactory.getLogger(TemplatePage.class.getName());
	private HtmlCleaner cleaner = null;
	
	public static final String TAG_TILES_INSERTDEFINITION = "_tiles.insertDefinition";
	public static final String TAG_TILES_PUTATTRIBUTE = "_tiles.putAttribute";//

	public static final String TAG_TILES_INSERTDEFINITION_ORIGIN = "@tiles.insertDefinition";
	public static final String TAG_TILES_PUTATTRIBUTE_ORIGON = "@tiles.putAttribute";//

	@Autowired
	private SystemPreference systemPreference;
	
	public File getTemplatePath(Long templateId, String templateName, Integer type) {
		Template.TypeEnum value = TypeEnum.getInstance(type);
		switch(value) {
		case Fragement:
			return new File(systemPreference.getTemplateDefaultPath(), 
					templateId.toString() + SystemConstants.Template_File_Suffix);
		case Template:
			return new File(systemPreference.getTemplateDefaultPath(), 
					templateId.toString() + SystemConstants.Template_File_Suffix);
		case System:
			return new File(systemPreference.getTemplateContainerPath(), 
					templateId.toString() + SystemConstants.Template_File_Suffix);
		case Tags:
			if(StringUtils.isNotBlank(templateName)) {
				return new File(systemPreference.getTemplateTagPath(),
						StringUtils.replace(templateName, ":", "/") + SystemConstants.Template_Tag_Suffix);
			}
		}
		return null;
	}

	public String createHtmlPageFile(HtmlPage htmlPage, boolean previewContent) {
		File file = getPagePath(htmlPage.getSiteId(), htmlPage.getPath(), previewContent);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		StringBuilder content = new StringBuilder();
		if(logger.isDebugEnabled()) {
			logger.debug("Create HtmlPage: " + htmlPage.getPageId() + " "
					+ htmlPage.getName() + " file:" + file.getAbsolutePath());
		}
		if(previewContent) {//预览内容
			if (StringUtils.isNotEmpty(htmlPage.getTempMeta())) {
				content.append(htmlPage.getTempMeta());
			}
		} else {
			if (StringUtils.isNotEmpty(htmlPage.getMeta())) {
				content.append(htmlPage.getMeta());
			}
		}
		try {
			FileUtils.writeStringToFile(file, content.toString(), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}
	
	/**
	 * save new template and init content
	 * @param templateId
	 */
	public String createTemplateFile(Template template) {
		File file = getTemplatePath(template.getTemplateId(), template.getName(), template.getType());
		if(logger.isDebugEnabled()) {
			logger.debug("Create Template: " + template.getTemplateId() + " "
					+ template.getName() + " file:" + file.getAbsolutePath());
		}
		String content = "";
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			content = template.getMeta();

			FileUtils.writeStringToFile(file, content, "utf-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * get page path ,if no exist then create
	 * @param siteId
	 * @param path
	 * @param previewContent 
	 */
	protected File getPagePath(Integer siteId, String path, boolean previewContent) {		
		return new File(systemPreference.getHtmlPageBasePath(), 
				SystemConstants.getHtmlPageFullPath(siteId, path, previewContent));
	}

	
	/**
	 * @return the cleaner
	 */
	public HtmlCleaner getCleaner() {
		if(null == cleaner) {//自定义解析的tag
			DefaultTagProvider provider = DefaultTagProvider.getInstance();
			TagInfo tagInfo = new TagInfo(TAG_TILES_INSERTDEFINITION, 0, 2, false, false, false);
			provider.addTagInfo(tagInfo);
			tagInfo = new TagInfo("_tiles.putAttribute", 0, 2, false, false, false);
			provider.addTagInfo(tagInfo);
			cleaner = new HtmlCleaner(provider);  
		}
		return cleaner;
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> extractContainerIds(String content) {
		if(StringUtils.isNotBlank(content)) {
			content = getTransfaeredContent(content);
			TagNode node = getCleaner().clean(content); 
			//得到所有的自定义组件
			List<TagNode> list = node.getElementListByName(TAG_TILES_INSERTDEFINITION, true);
			if(null != list) {
				List<Long> result = new ArrayList<Long>();
				for(TagNode parent : list) {
					List<TagNode> atts = parent.getElementListByAttValue("name", "containerId", true, true);
					for(TagNode name : atts) {
						String value = name.getAttributeByName("value");
						if(StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
							result.add(Long.valueOf(value));
						}
					}
				}
				return result;
			}
		}
		return null;
	}
	/**
	 * 将"@"标签替换为“_”。
	 * @param content
	 * @return
	 */
	protected String getTransfaeredContent(String content) {
		//替换@tiles:insertDefintion为_tiles:insertDefinition
		content = StringUtils.replace(content, TAG_TILES_INSERTDEFINITION_ORIGIN, TAG_TILES_INSERTDEFINITION);
		content = StringUtils.replace(content, "/" + TAG_TILES_INSERTDEFINITION_ORIGIN, "/" + TAG_TILES_INSERTDEFINITION);

		content = StringUtils.replace(content, TAG_TILES_PUTATTRIBUTE_ORIGON, TAG_TILES_PUTATTRIBUTE);
		return content;
	}

	@SuppressWarnings("unchecked")
	public List<Long> extractFragementIds(String content) {
		if(StringUtils.isNotBlank(content)) {
			//需要替换原来的内容
			content = getTransfaeredContent(content);
			TagNode node = getCleaner().clean(content); 
			//得到所有的自定义组件
			List<TagNode> list = node.getElementListByName(TAG_TILES_INSERTDEFINITION, true);
			if(null != list) {
				List<Long> result = new ArrayList<Long>();
				for(TagNode parent : list) {
					List<TagNode> atts = parent.getAllElementsList(true);
					for(TagNode attr : atts) {
						String name = attr.getAttributeByName("name");
						String value = attr.getAttributeByName("value");
						if(StringUtils.isNotBlank(name) && name.indexOf("templateId") >= 0
								&& StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
							if(!result.contains(Long.valueOf(value))) {
								result.add(Long.valueOf(value));
							}
						}
					}
				}
				return result;
			}
		}
		return null;
	}
}
