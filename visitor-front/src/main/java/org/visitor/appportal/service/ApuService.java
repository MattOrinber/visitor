package org.visitor.appportal.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.visitor.app.portal.model.Product;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.redis.ApuRedisRepository;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.redis.RedisKeys;

@Service("apuService")
public class ApuService {
	
	@Autowired
	private ApuRedisRepository apuRedisRepository;
	@Autowired
	private ProductRedisRepository productRedisRepository;
	
	public void setProductFileApkInfo(ProductFile productFile) {
		if (!StringUtils.isEmpty(productFile.getPackageName()) && !StringUtils.isEmpty(productFile.getIdentity())) {
				boolean up_flag = true;
				String cur_value = apuRedisRepository.getProductFileApkInfo(productFile.getPackageName(), productFile.getIdentity());
				if (StringUtils.isNotEmpty(cur_value)) {
					String cur_infos[] = cur_value.split(RedisKeys.getApuRedisSplit());
					if (cur_infos != null && cur_infos.length >= 2) {
						String productId = cur_infos[0];
						String versionCode = cur_infos[1];
						Product product = productRedisRepository.getProductByProductId(Long.valueOf(productId), false);
						if (product != null && product.getProductList() != null) {
							if (product.getProductList().getMerchantId() != 515L ||
									product.getProductList().getCooperationModelId() != 403L) {
								if (Integer.valueOf(versionCode) >= productFile.getVersionCode()) {
									up_flag = false;
								}
							}
						}
					}
				}
				if (up_flag) {
					apuRedisRepository.setProductFileApkInfo(productFile);
				}
			}
		}
	
	public List<Product> getUserApuIgnoreProductId(String uid,long start, long end) {
		Set<String> productIds = apuRedisRepository.getUserApuIgnoreProductId(uid, start, end);
		if (productIds != null && productIds.size() > 0) {
			return productRedisRepository.getMultipleProductWithProductFiles(productIds);
		}
		return null;
	}
	
	/*public List<Product> getUserApuUpdateProductId(String uid,long start, long end) {
		Set<String> productIdsAndVersions = apuRedisRepository.getUserApuUpdateProductId(uid, start, end);
		if (productIdsAndVersions != null && productIdsAndVersions.size() > 0) {
			if (productIdsAndVersions != null && productIdsAndVersions.size() > 0) {
				return productRedisRepository.getMultipleProductWithProductFilesAndVersion(productIdsAndVersions);
			}
			return null;
		}
		return null;
	}*/

}
