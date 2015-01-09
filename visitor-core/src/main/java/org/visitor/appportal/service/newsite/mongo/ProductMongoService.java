package org.visitor.appportal.service.newsite.mongo;

import org.springframework.stereotype.Service;
import org.visitor.appportal.visitor.beans.mongo.ProductMongoBean;
import org.visitor.appportal.visitor.beans.mongo.ProductPriceSetMongoBean;
import org.visitor.appportal.web.mongo.MongoTemplate;

@Service("productMongoService")
public class ProductMongoService {
	private MongoTemplate anotherMongoTemplate = MongoTemplate.getInstance();
	
	public void saveProductDetail(ProductMongoBean productMongoBean) {
		anotherMongoTemplate.insert(productMongoBean, 0);
	}
	
	public void saveProductExtraPriceSet(ProductPriceSetMongoBean ppmb) {
		anotherMongoTemplate.insert(ppmb, 0);
	}
}
