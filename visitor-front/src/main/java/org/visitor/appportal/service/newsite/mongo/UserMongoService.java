package org.visitor.appportal.service.newsite.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.visitor.beans.mongo.UserMongoBean;

@Service("userMongoService")
public class UserMongoService {
	@Autowired
	private MongoTemplate anotherMongoTemplate;
	
	public void saveUserDetail(UserMongoBean userMongoBean) {
		anotherMongoTemplate.insert(userMongoBean);
	}
}
