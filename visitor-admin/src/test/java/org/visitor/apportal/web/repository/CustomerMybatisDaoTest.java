package org.visitor.apportal.web.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;
import org.visitor.apportal.web.repository.order.CustomerMybatisDao;
import org.visitor.appportal.visitor.domain.User;

@DirtiesContext
@ContextConfiguration(locations = "/applicationContext.xml")
public class CustomerMybatisDaoTest extends SpringTransactionalTestCase {

	@Autowired
	private CustomerMybatisDao dao;

	@Test
	public void testFindAll() {
		List<User> users = dao.findAll();
		System.out.println("");
		System.out.println("find all size:" + users.size());
		System.out.println("");
		Assert.assertNotNull(users);
	}

	@Test
	public void testFind() {
		Map<String, Object> filterParams = new HashMap<String, Object>();
		filterParams.put("userType", -1);
		List<User> users = dao.find(filterParams, new RowBounds(0, 10));
		System.out.println("");
		System.out.println(users);
		System.out.println("");
		System.out.println("find size:" + users.size());
		Assert.assertTrue(users.size() == 9);

		filterParams.put("userType", 0);
		users = dao.find(filterParams, new RowBounds(0, 10));
		Assert.assertTrue(users.size() < 9);

	}

}
