/**
 *
 */
package org.visitor.appportal.redis;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import org.visitor.app.portal.model.FolderRank.RankTypeEnum;

/**
 * @author mengw
 */
public class RedisKeys {

    /**
     *
     */
    public RedisKeys() {
        // TODO Auto-generated constructor stub
    }

    public static String getPageableProductKey(Integer siteId, Long folderId, String platformVersionId, String level) {
        return "z-pageableproduct:" + siteId + ":siteid:" + folderId + ":folderid:" + platformVersionId + ":platformversion:" + level + ":level";
    }

    public static String getProductKey() {
        return "h-product";
    }

    public static String getProductFileKey() {
        return "h-productfile";
    }

    /**
     * 产品推荐的KEY，只存产品推荐ID
     *
     * @param siteId
     * @param folderId
     * @param pageId
     * @param containerId
     * @return
     */
    public static String getProductRecommandKey(Integer siteId, Long folderId,
                                                Long pageId, Long containerId) {
        return "z-productcontainer:" + siteId + ":siteid:" + folderId +
                ":folderid:" + pageId + ":pageid:" + containerId + ":containerid";
    }

    /**
     * 产品推荐KEY，对应hash结构，存储产品推荐的ID和内容
     *
     * @return
     */
    public static String getProductRecommandKey() {
        return "h-productcontainer";
    }

    public static String getProductPicListKey() {
        return "h-productpic";
    }

    public static String getFolderRankKey(Integer siteId, Long folderId,
                                          RankTypeEnum rankType, Long platformVersionId, Integer level) {
        return "z-folderrank:" + siteId + ":siteid:" + folderId + ":folderid:"
                + rankType + ":rank:" + platformVersionId + ":version:" + level + ":level";
    }

    /**
     * 推荐数据规则
     *
     * @return
     */
    public static String getProductIntelligentRecommendKey(Integer siteId) {
        return "h-product-intelligent:" + siteId;
    }

    /**
     * 推荐数据规则
     *
     * @return
     */
    public static String getFolderProductIntelligentRecommendKey() {
        return "h-product-folder-intelligent";
    }

    /**
     * 下载推荐
     *
     * @return
     */
    public static String getProductRecommendBehaviorsKey(Integer siteId) {
        return "h-product-rec-beha:" + siteId;
    }

    /**
     * 相似推荐
     *
     * @return
     */
    public static String getProductRecommendSimilarsKey(Integer siteId) {
        return "h-product-rec-simi:" + siteId;
    }

    /**
     * 推荐库
     *
     * @param siteId
     * @return
     */
    public static String getProductRecommendStorageKey(Integer siteId) {
        return "z-product-rec-store:" + siteId + ":siteid";
    }

    /**
     * 删除频道排行时的key。
     *
     * @param siteId
     * @param folderId
     * @return
     */
    public static String getFolderRankKey4Delete(Integer siteId, Long folderId) {
        return "z-folderrank:" + siteId + ":siteid:" + folderId + ":folderid:*";
    }

    public static String getProductCommentKey() {
        return "h-comment";
    }

    public static String getCategoryIdKey() {
        return "h-categoryid";
    }

    public static String getCategoryChildrenKey() {
        return "h-categorychildren";
    }

    public static String getCategoryResolutionKey() {
        return "h-categoryresolution";
    }

    public static String getCategoryPlatformKey() {
        return "h-categoryplatform";
    }

    /**
     * 平台版本中版本名称对应的ID的hash
     *
     * @return
     */
    public static String getCategoryPlatformVersionKey() {
        return "h-categoryplatformversion";
    }

    /**
     * 平台版本的hash key。
     *
     * @param platformName
     * @param versionName
     * @return
     */
    public static String getCategoryPlatformVersionNameKey(String platformName, String versionName) {
        return "hk-categoryplatformversion:" + StringUtils.lowerCase(StringUtils.trim(platformName)
                + ":platformname:" + StringUtils.trim(versionName)) + ":versionname";
    }

    public static String getCategoryTagKey() {
        return "h-categorytag";
    }

    public static String getFolderIdKey() {
        return "h-folder";
    }

    public static String getFolderPathHashKey(Integer siteId, String path) {
        return "hk-path:" + siteId + ":siteid:" + path + ":path";
    }

    public static String getFolderPathKey() {
        return "h-folderpath";
    }

    public static String getFolderAncestorsKey() {
        return "h-folderancestor";
    }

    public static String getFolderChildrenKey(Integer siteId, Long folderId) {
        return "z-folderchildren:" + folderId + ":folderid:" + siteId + ":siteid";
    }

    /**
     * htmlpage 的hash key。
     *
     * @return
     */
    public static String getHtmlPageIdKey() {
        return "h-htmlpage";
    }

    /**
     * 页面内容编辑的hash key
     *
     * @return
     */
    public static String getHtmlPageContentKey() {
        return "h-htmlpagecontent";
    }

    /**
     * 模板发布时的hash key
     *
     * @return
     */
    public static String getTemplateContentKey() {
        return "h-templatecontent";
    }

    /**
     * 模板对象的hashkey
     *
     * @return
     */
    public static String getTemplateKey() {
        return "h-template";
    }

    public static String getSystemPreferenceKey() {
        return "h-systempreference";
    }

    public static String getDefaultSearchKeywordKey(Integer siteId) {
        return "hk-defaultsearchkeyword:" + siteId + ":siteid";
    }

    /**
     * 产品来源的排序
     *
     * @param productSource
     * @return
     */
    public static String getProductSourceOrderKey(Long productSource) {
        return "hk-productsourceorder:" + productSource + ":productsource";
    }

    /**
     * 系统设置中，需要通过外部url进行下载的cp的id列表。
     *
     * @return
     */
    public static String getExternalUrlCpIdsKey() {
        return "hk-externalurlcpid";
    }

    /**
     * 关联同一个tag的频道ID即：tag-folder关联。
     *
     * @return
     */
    public static String getTagFolderKey() {
        return "h-tagfolder";
    }

    /**
     * @return
     */
    public static String getAllSiteKey() {
        return "h-site";
    }

    public static String getTestModelListKey() {
        return "hk-testmodellist";
    }

    /**
     * 客户端系统设置，用于进行测试的平台ID对应的key。
     *
     * @return
     */
    public static String getTestPlatformKey() {
        return "hk-testplatform";
    }

    /**
     * 客户端系统设置，用于进行测试的平台版本ID对应的key。
     *
     * @return
     */
    public static String getTestPlatformVersionKey() {
        return "hk-testplatformversion";
    }

    /**
     * 客户端系统设置，用于进行测试的分辨率ID对应的key。
     *
     * @return
     */
    public static String getTestResolutionKey() {
        return "hk-testresolution";
    }

    public static String getPlatformVersionDefaultResolutionKey(
            Long platformId, Long platformVersionId) {
        return "hk-platformveriondefaultresolution:" + platformId +
                ":platformId:" + platformVersionId + ":platformversionid";
    }

    public static String getHtmlPagePublishedMessage() {
        return "m-htmlPagePublished";
    }

    public static String getTemplatePublishedMessage() {
        return "m-templatePublished";
    }

    public static String getCategoryPublishedMessage() {
        return "m-categorypublished";
    }

    /**
     * 站点修改/删除时发布饿
     *
     * @return
     */
    public static String getSitePublishedMessage() {
        return "m-sitepublished";
    }

    public static String getCacheElementPublishedMessage() {
        return "m-cacheelementpublished";
    }

    /**
     * 存储用户信息的KEY
     *
     * @param uid
     * @return
     */
    public static String getUserPrerenceKey(String uid) {
        return "v-up:" + uid;
    }

    public static String getSearchMaxShowPageNumber(Integer siteId) {
        return "hk-searchmaxshowpage:" + siteId + ":siteid";
    }

    public static String getSearchMaxShowItemNumberKey(Integer siteId) {
        return "hk-searchmaxshowiteminlastpage:" + siteId + ":siteid";
    }

    /**
     * 产品详情页是否显示产品评论
     *
     * @param siteId
     * @return
     */
    public static String getIfShowProductComments(Integer siteId) {
        return "hk-ifshowproductcomment:" + siteId + ":siteid";
    }

    /**
     * 该站点是否通过搜索出产品列表。
     *
     * @param siteId
     * @return
     */
    public static String getShowProductListBySearch(Integer siteId) {
        return "hk-ifshowproductsbysearch:" + siteId + ":siteid";
    }

    /**
     * 频道排行显示的记录数。
     *
     * @param siteId
     * @return
     */
    public static String getFolderRankMaxCount(Integer siteId) {
        return "hk-folderankmaxcount:" + siteId + ":siteid";
    }

    /**
     * 产品推荐，容器的key
     *
     * @return
     */
    public static String getRecommandContainerKey() {
        return "h-recommandcontainer";
    }

    /**
     * 产品在频道中的置顶阀值。大于此值的产品会被置顶。新创建的产品绑定
     * 频道时会设置为小于此值。
     *
     * @return
     */
    public static String getFolderProductMaxSortOrderKey() {
        return "hk-folderproductmaxsortorder";
    }

    /**
     * 站点中所有最新发布的产品ID
     *
     * @param siteId
     * @return
     */
    public static String getProductPublishFirstKey(Integer siteId) {
        return "z-siteproductrecent:" + siteId + ":siteid";
    }

    /**
     * 产品列表页显示的产品个数。
     *
     * @return
     */
    public static String getProductDisplayPageSizeKey() {
        return "hk-productdisplaypagesize";
    }

    /**
     * 产品列表页显示的产品个数。
     *
     * @return
     */
    public static String getCategoryDefaultDisName(String categoryId) {
        return "hk-categorydisname:" + categoryId + ":categoryId";
    }

    /**
     * 显示截图数量。
     *
     * @param siteId
     * @return
     */
    public static String getShowProductPicCountKey(Integer siteId) {
        return "hk-showproductpiccount:" + siteId + ":siteid";
    }

    /**
     * 站点排行榜
     *
     * @param siteId
     * @param rankType
     * @param platformVersionId
     * @param level
     * @return
     */
    public static String getSiteRankKey(Integer siteId, RankTypeEnum rankType, Long platformVersionId, Integer level) {
        return "z-siterank:" + siteId + ":siteid:" + rankType + ":rank:" + platformVersionId + ":version:" + level + ":level";
    }

    /**
     * 删除站点排行时的key。
     *
     * @param siteId
     * @return
     */
    public static String getSiteRankKey4Delete(Integer siteId) {
        return "z-siterank:" + siteId + ":siteid:*";
    }

    public static String getPictureKey() {
        return "h-picture";
    }

    public static String getProductStateKey(RankTypeEnum totaldownload) {
        return "h-productstate:" + totaldownload + ":rank";
    }

    public static String getAdvertiseKey() {
        return "h-advertise";
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public static String getFolderRankDailyKey(Long folderId,
                                               RankTypeEnum rankType, Long platformVersionId, Long resolutionId) {
        return "v-pfd:" + folderId + ":" + rankType.ordinal() + ":" +
                platformVersionId + ":" + resolutionId + ":" + sdf.format(new Date());
    }

    /**
     * 搜索关键词的hashkey
     *
     * @return
     */
    public static String getSearchKeywordKey() {
        return "h-searchkeyword";
    }

    public static String getSearchKeywordSortedKey(Integer siteId) {
        return "z-searchkeyword:" + siteId;
    }

    /**
     * 单个搜索词。
     *
     * @param siteId
     * @param platformId
     * @param keyword
     * @return
     */
    public static String getSearchKeywordFieldKey(Integer siteId, Long platformId, String keyword) {
        return "hk-keyword:" + siteId + ":" + StringUtils.lowerCase(keyword);
    }

    /**
     * 交叉推荐规则
     *
     * @param serviceId
     * @param siteId
     * @param folderId
     * @param platformId
     * @param type
     * @return
     */
    public static String getRecommendRuleAcrossFieldKey(int serviceId,
                                                        int siteId, Long folderId, Long platformId, Integer type) {
        return "hk-rra:" + serviceId + ":" + siteId + ":" + folderId + ":" + type;
    }

    public static String getRecommendRuleAcrossKey() {
        return "h-recommandruleacross";
    }

    /**
     * 容器按照平台版本／分辨率等级分开的产品ID列表
     *
     * @param siteId
     * @param containerId
     * @param platformVersionId
     * @param level
     * @return
     */
    public static String getSiteContainerKey(Integer siteId, Long containerId, Long platformVersionId, int level) {
        return "z-sc:" + containerId + ":" + platformVersionId + ":" + level;
    }

    public static String getSiteContainerKey4Delete(Integer siteId, Long containerId) {
        return "z-sc:" + containerId + ":*:*";
    }

    /**
     * 每个交叉产品对应的推荐产品ID的hashkey
     *
     * @return
     */
    public static String getProductAcrossRecommandKey() {
        return "h-productacrossrecommand";
    }

    /**
     * 每个交叉推荐产品的fieldKey，value为String 类型的用半角都好分割的id列表。
     *
     * @param productId
     * @param serviceId
     * @param siteId
     * @param platformId
     * @return
     */
    public static String getProductAcrossRecommandFieldKey(Long productId,
                                                           int serviceId, int siteId, Long platformId) {
        return "hk-par:" + serviceId + ":" + siteId + ":" + productId;
    }

    /**
     * 用户对站点的第一次访问时间
     *
     * @return
     */
    public static String getUserSiteDataKey() {
        return "h-usersitedate";
    }

    public static String getUserSiteDataFieldKey(String uid, Integer siteId) {
        return uid + "----" + siteId;
    }

    /**
     * APU KEYS
     */
    /**
     * APU 用户忽略更新产品列表
     *
     * @param userKey
     * @return
     */
    public static String getApuUserIgnoreProductKey(String userKey) {
        return "apu-z-uip:" + userKey;
    }

    /**
     * APU 用户可更新产品列表
     *
     * @param userKey
     * @return
     */
    public static String getApuUserUpdateProductKey(String userKey) {
        return "apu-z-usrup:" + userKey;
    }


    public static String getApuProductFileApkInfo() {
        return "apu-h-apkinfo";
    }

    /**
     * APU 用户的更新设置状态
     *
     * @param userKey 用户
     * @return
     */
    public static String getApuSettingStatusKey(String userKey) {
        // TODO Auto-generated method stub
        return "apu-s-settinginfo:" + userKey;
    }

    public static String getApuRedisSplit() {
        return "---";
    }

    /**
     * APU 用户可更新产品列表
     *
     * @param userKey 用户:imei + '---' + imsi
     * @param siteId  所属站点
     * @return
     */
    public static String getApuUserUpdateProductKey(String userKey, Integer siteId) {
        // TODO Auto-generated method stub
        return "apu-z-susrup:" + userKey + getApuRedisSplit() + siteId;
    }

    public static String getLabelProductKey(Integer labelId) {
        return "labelProductList:" + labelId;
    }
}
