package org.visitor.appportal.service.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.visitor.appportal.service.app.AppAdvertiseService;
import org.visitor.appportal.service.app.AppCategoryService;
import org.visitor.appportal.service.app.AppPictureService;
import org.visitor.appportal.service.app.AppProductContainerService;
import org.visitor.appportal.service.app.AppProductListService;
import org.visitor.appportal.service.app.AppRecommandService;
import org.visitor.appportal.service.app.AppRecommendRuleService;
import org.visitor.appportal.service.app.AppSearchKeywordService;
import org.visitor.appportal.service.app.AppTemplateService;
import org.visitor.appportal.service.game.GameAdvertiseService;
import org.visitor.appportal.service.game.GameCategoryService;
import org.visitor.appportal.service.game.GamePictureService;
import org.visitor.appportal.service.game.GameProductContainerService;
import org.visitor.appportal.service.game.GameProductListService;
import org.visitor.appportal.service.game.GameRecommandService;
import org.visitor.appportal.service.game.GameRecommendRuleService;
import org.visitor.appportal.service.game.GameSearchKeywordService;
import org.visitor.appportal.service.game.GameTemplateService;
import org.visitor.appportal.service.ios.IosAdvertiseService;
import org.visitor.appportal.service.ios.IosCategoryService;
import org.visitor.appportal.service.ios.IosPictureService;
import org.visitor.appportal.service.ios.IosProductContainerService;
import org.visitor.appportal.service.ios.IosProductListService;
import org.visitor.appportal.service.ios.IosRecommandService;
import org.visitor.appportal.service.ios.IosRecommendRuleService;
import org.visitor.appportal.service.ios.IosSearchKeywordService;
import org.visitor.appportal.service.ios.IosTemplateService;
import org.visitor.appportal.service.symbian.SymbianAdvertiseService;
import org.visitor.appportal.service.symbian.SymbianCategoryService;
import org.visitor.appportal.service.symbian.SymbianPictureService;
import org.visitor.appportal.service.symbian.SymbianProductContainerService;
import org.visitor.appportal.service.symbian.SymbianProductListService;
import org.visitor.appportal.service.symbian.SymbianRecommandService;
import org.visitor.appportal.service.symbian.SymbianRecommendRuleService;
import org.visitor.appportal.service.symbian.SymbianSearchKeywordService;
import org.visitor.appportal.service.symbian.SymbianTemplateService;
import org.visitor.appportal.web.utils.SiteUtil.IDEnum;

@Component
public class ServiceFactory {

	@Autowired
	AppPictureService appPictureService;
	@Autowired
	SymbianPictureService symbianPictureService;
	@Autowired
	GamePictureService gamePictureService;
	@Autowired
	IosPictureService iosPictureService;
	
	@Autowired
	AppRecommandService appRecommandService;
	@Autowired
	SymbianRecommandService symbianRecommandService;
	@Autowired
	GameRecommandService gameRecommandService;
	@Autowired
	IosRecommandService iosRecommandService;
	
	@Autowired
	AppTemplateService appTemplateService;
	@Autowired
	SymbianTemplateService symbianTemplateService;
	@Autowired
	GameTemplateService gameTemplateService;
	@Autowired
	IosTemplateService iosTemplateService;
	
	@Autowired
	AppSearchKeywordService appSearchKeywordService;
	@Autowired
	SymbianSearchKeywordService symbianSearchKeywordService;
	@Autowired
	GameSearchKeywordService gameSearchKeywordService;
	@Autowired
	IosSearchKeywordService iosSearchKeywordService;

	@Autowired
	AppAdvertiseService appAdvertiseService;
	@Autowired
	SymbianAdvertiseService symbianAdvertiseService;
	@Autowired
	GameAdvertiseService gameAdvertiseService;
	@Autowired
	IosAdvertiseService iosAdvertiseService;
	
	@Autowired
	AppProductListService appProductListService;
	@Autowired
	SymbianProductListService symbianProductListService;
	@Autowired
	GameProductListService gameProductListService;
	@Autowired
	IosProductListService iosProductListService;

	@Autowired
	AppCategoryService appCategoryService;
	@Autowired
	SymbianCategoryService symbianCategoryService;
	@Autowired
	GameCategoryService gameCategoryService;
	@Autowired
	IosCategoryService iosCategoryService;
	
	@Autowired
	AppRecommendRuleService appRecommendRuleService;
	@Autowired
	SymbianRecommendRuleService symbianRecommendRuleService;
	@Autowired
	GameRecommendRuleService gameRecommendRuleService;
	@Autowired
	IosRecommendRuleService iosRecommendRuleService;

	
	@Autowired
	AppProductContainerService appProductContainerService;
	@Autowired
	SymbianProductContainerService symbianProductContainerService;
	@Autowired
	GameProductContainerService gameProductContainerService;
	@Autowired
	IosProductContainerService iosProductContainerService;
	
	public ProductContainerService getProductContainerService(Integer siteId){
		
		IDEnum siteID = IDEnum.getInstance(siteId);
		
		switch(siteID){
			case ANDROID:
				return appProductContainerService;
			case SYMBIAN:
				return symbianProductContainerService;
			case GAME:
				return gameProductContainerService;
			case IOS:
				return iosProductContainerService;
			default:
				return appProductContainerService;
		}
	}
	
	public CategoryService getCategoryService(Integer siteId){
		
		IDEnum siteID = IDEnum.getInstance(siteId);
		
		switch(siteID){
			case ANDROID:
				return appCategoryService;
			case SYMBIAN:
				return symbianCategoryService;
			case GAME:
				return gameCategoryService;
			case IOS:
				return iosCategoryService;
			default:
				return appCategoryService;
		}
	}
	
	public ProductListService getProductListService(Integer siteId){
		
		IDEnum siteID = IDEnum.getInstance(siteId);
		
		switch(siteID){
			case ANDROID:
				return appProductListService;
			case SYMBIAN:
				return symbianProductListService;
			case GAME:
				return gameProductListService;
			case IOS:
				return iosProductListService;
			default:
				return appProductListService;
		}
	}
	
	
	public AdvertiseService getAdvertiseService(Integer siteId){
		
		IDEnum siteID = IDEnum.getInstance(siteId);
		
		switch(siteID){
			case ANDROID:
				return appAdvertiseService;
			case SYMBIAN:
				return symbianAdvertiseService;
			case GAME:
				return gameAdvertiseService;
			case IOS:
				return iosAdvertiseService;
			default:
				return appAdvertiseService;
		}
	}
	
	public SearchKeywordService getSearchKeywordService(Integer siteId){
		
		IDEnum siteID = IDEnum.getInstance(siteId);
		
		switch(siteID){
			case ANDROID:
				return appSearchKeywordService;
			case SYMBIAN:
				return symbianSearchKeywordService;
			case GAME:
				return gameSearchKeywordService;
			case IOS:
				return iosSearchKeywordService;
			default:
				return appSearchKeywordService;
		}
	}

	public TemplateService getTemplateService(Integer siteId){
		
		IDEnum siteID = IDEnum.getInstance(siteId);
		
		switch(siteID){
			case ANDROID:
				return appTemplateService;
			case SYMBIAN:
				return symbianTemplateService;
			case GAME:
				return gameTemplateService;
			case IOS:
				return iosTemplateService;
			default:
				return appTemplateService;
		}
	}
	
	public PictureService getPicService(Integer siteId){
		
		IDEnum siteID = IDEnum.getInstance(siteId);
		
		switch(siteID){
			case ANDROID:
				return appPictureService;
			case SYMBIAN:
				return symbianPictureService;
			case GAME:
				return gamePictureService;
			case IOS:
				return iosPictureService;
			default:
				return appPictureService;
		}
	}

	public RecommandService getRecommandService(Integer siteId){
		
		IDEnum siteID = IDEnum.getInstance(siteId);
		
		switch(siteID){
			case ANDROID:
				return appRecommandService;
			case SYMBIAN:
				return symbianRecommandService;
			case GAME:
				return gameRecommandService;
			case IOS:
				return iosRecommandService;
			default:
				return appRecommandService;
		}
	}

	public RecommendRuleService getRecommendRuleService(Integer siteId){
		
		IDEnum siteID = IDEnum.getInstance(siteId);
		
		switch(siteID){
			case ANDROID:
				return appRecommendRuleService;
			case SYMBIAN:
				return symbianRecommendRuleService;
			case GAME:
				return gameRecommendRuleService;
			case IOS:
				return iosRecommendRuleService;
			default:
				return appRecommendRuleService;
		}
	}

}
