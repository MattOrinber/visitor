/**
 *
 */
package org.visitor.appportal.redis;

import org.visitor.app.portal.model.FolderRank.RankTypeEnum;
import org.visitor.app.portal.model.PagerDisplay;
import org.visitor.app.portal.model.Product;
import org.visitor.app.portal.model.ProductIntelligentRecommend;
import org.visitor.app.portal.model.UserPreference;
import org.visitor.appportal.domain.*;
import org.visitor.util.AppStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author mengw
 */
@Repository
public class LabelRedisRepository {

    protected static final Logger log = LoggerFactory.getLogger(LabelRedisRepository.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapperWrapper objectMapperWrapper;
    @Autowired
    private StringRedisTemplate compressStringRedisTemplate;

    /**
     *
     */
    public LabelRedisRepository() {

    }

    public void setLabelProductList(String key, String... ids) {
        stringRedisTemplate.delete(key);

        stringRedisTemplate.opsForList().leftPushAll(key,ids);
    }

    public List<String> getLabelProductList(Integer labelId) {
        String key = RedisKeys.getLabelProductKey(labelId);
        return stringRedisTemplate.opsForList().range(key, 0, 10000);
    }

}