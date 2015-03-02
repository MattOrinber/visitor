package org.visitor.apportal.web.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;
import org.visitor.apportal.web.entity.order.OrderDetailVO;
import org.visitor.apportal.web.repository.order.OrderDetailMybatisDao;

@DirtiesContext
@ContextConfiguration(locations = "/applicationContext.xml")
public class OrderDetailMybatisDaoTest extends SpringTransactionalTestCase {

	@Autowired
	private OrderDetailMybatisDao dao;

	@Test
	public void testFindAll() {
		OrderDetailVO detail = dao.findByOrderId(106L);
		System.out.println(detail);
		Assert.assertNotNull(detail);
	}

}
