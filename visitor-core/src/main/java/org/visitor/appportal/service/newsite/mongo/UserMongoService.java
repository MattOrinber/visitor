package org.visitor.appportal.service.newsite.mongo;

import org.springframework.stereotype.Service;
import org.visitor.appportal.visitor.beans.mongo.UserMongoBean;
import org.visitor.appportal.web.mongo.MongoTemplate;

@Service("userMongoService")
public class UserMongoService {
	private MongoTemplate anotherMongoTemplate = MongoTemplate.getInstance();
	
	public void saveUserDetail(UserMongoBean userMongoBean) {
		anotherMongoTemplate.insert(userMongoBean, 0);
	}
}
