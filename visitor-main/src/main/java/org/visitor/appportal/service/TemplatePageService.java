package org.visitor.appportal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.visitor.app.portal.model.TemplateItems;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.PageContainer.ShowTypeEnum;
import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.domain.Template;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.TemplateRepository;

@Service
public class TemplatePageService {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(TemplatePageService.class);
	
	@Autowired
	public SiteRepository siteRepository;
	@Autowired
	public FolderRepository folderRepository;
	@Autowired
	public HtmlPageRepository htmlPageRepository;
	@Autowired
	private RecommandContainerRepository recommandContainerRepository;
	@Autowired
	public TemplateRepository templateRepository;
	
	@Autowired
	public PageContainerRepository pageContainerRepository;
	/**
	 * change site template
	 * 
	 * @param templateId
	 * @param site
	 * @param type
	 */
	@Transactional
	public void changeTemplate(Long templateId, Site site, Integer type) {
		HtmlPage hp = htmlPageRepository
				.findBySiteIdAndIfDefaultPageAndPageType(site.getSiteId(),
						HtmlPage.DEFAULT_PAGE, type);
		hp.setTemplate(templateRepository.findOne(templateId));
		htmlPageRepository.save(hp);
		siteRepository.save(site);
	}

	/**
	 * 
	 * @param htmlPage
	 * @param containerIds
	 */
	@Transactional
	public void saveHtmlPageWithContainerIds(HtmlPage htmlPage, List<Long> containerIds) {
		htmlPageRepository.save(htmlPage);
		PageContainer entity = new PageContainer();
		List<PageContainer> list = pageContainerRepository.findByPageId(htmlPage.getPageId());
		
		if(null != containerIds && containerIds.size() > 0) {
			List<PageContainer> list_del = new ArrayList<PageContainer>();
			for(Long id : containerIds) {
				RecommandContainer container = recommandContainerRepository.findOne(id);
				if(null != container) {
					boolean isExist = false;
					if (list != null && list.size() > 0) {
						for (PageContainer pc : list) {
							if (pc.getContainerId().longValue() == container.getContainerId().longValue()) {
								isExist = true;
								list_del.add(pc);
							}
						}
					}
					
					if (!isExist) {
						entity.setContainer(container);
						entity.setSite(htmlPage.getFolder().getSite());
						entity.setFolder(htmlPage.getFolder());
						entity.setPage(htmlPage);
						entity.setCreateBy(htmlPage.getModBy());
						entity.setCreateDate(new Date());
						entity.setModBy(htmlPage.getModBy());
						entity.setModDate(new Date());
						
						entity.setShowType(ShowTypeEnum.Manual.getValue());
						
						pageContainerRepository.save(entity);
					}
				}
			}
			
			if (list_del != null && list_del.size() >0) {
				list.removeAll(list_del);
			}
			
		}
		if (list != null && list.size() > 0) {
			for(PageContainer pc : list) {
				this.pageContainerRepository.delete(pc);
			}
		}
	}
	
	/**
	 * publish folder page
	 */
	@Transactional
	public void savePublishHtmlPage(HtmlPage htmlPage) {
		// TODO Auto-generated method stub
		htmlPageRepository.save(htmlPage);
		//if folder is bussiness folder , then update folder page by type
		Folder folder = htmlPage.getFolder();
		if (folder.getFolderType() == Folder.FOLDER_TYPE_BUSSINESS) {
			switch (htmlPage.getPageType()) {
			case 0:
				folder.setIndexPage(htmlPage);
				break;
			case 1:
				folder.setListPage(htmlPage);
				break;
			case 2:
				folder.setDetailPage(htmlPage);
				break;
			case 3:
				folder.setNaviPage(htmlPage);
				break;
			}
			folderRepository.save(folder);
		}
	}

	/**
	 * get the template list from the content of a html page.
	 * 从一段HTML文本中分析出其应用的模板，模板类型包括头模板，单独页，系统模板，其中头模板和单独页都是以导入的方式引用，可以看做一类。
	 * @param meta
	 * @return
	 */
	public List<TemplateItems> getTemplateListFromContent(String meta) {
		// TODO Auto-generated method stub
		List<TemplateItems> tempList = new ArrayList<TemplateItems>();
		if(StringUtils.isNotBlank(meta)){
			String items [] = meta.split("\n");
			
			Template tmp = null;
			Map<String,String> map = null;
			
			for(String item:items){
				item = item.trim();
				if(!item.equals("")){
					if(item.startsWith("<#include")){
						//此类模板为导入的模板，需要找出其模板ID
						//有一个要求，就是说，这一串导入的代码中，只有文件名才能为数字，其它都不能为数字（即路径不能为数字）
						item = item.replaceAll("[^0-9]*", "");//将所有非数字的字符都去掉
						//item=item.substring(item.lastIndexOf("/"),item.indexOf("")
						if(StringUtils.isNumeric(item)){
							tempList.add(new TemplateItems(templateRepository.findOne(Long.valueOf(item)),null));
						}
					}else if(item.startsWith("<@tiles.insertDefinition")){
						//这类为tiles组件，需要通过名称来查找其组件
//						String tilesName = item.replaceAll("^<@tiles.in.*name=", "").replaceAll("templateType=.*$", "").replace("\"", "").trim();
						
//						tmp = templateRepository.findByName(tilesName);
						map = new HashMap<String,String>();
					}else if(item.startsWith("<@tiles.putAttribute")){
						String t3[] = item.split(" ");
						
						if(t3.length>=3){
							if(t3[1].startsWith("name=") && t3[2].startsWith("value=")){
								map.put(t3[1].replace("\"", "").replace("name=",""),t3[2].replace("\"", "").replace("value=",""));
							}
						}

					}else if(item.equals("</@tiles.insertDefinition>")){
						if(tmp !=null){//map 可以为空，表示没有参数
							tempList.add(new TemplateItems(tmp,map));
						}
						tmp = null;map= null;
					}
				}
			}
		}
		return tempList;
	}
}
