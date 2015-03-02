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
import org.visitor.apportal.web.entity.order.OrderVO;
import org.visitor.apportal.web.repository.order.OrderMybatisDao;
import org.visitor.appportal.visitor.domain.ProductOrder;

@DirtiesContext
@ContextConfiguration(locations = "/applicationContext.xml")
public class ProductOrderMybatisDaoTest extends SpringTransactionalTestCase {
	
	@Autowired
	private OrderMybatisDao dao;
	
	@Test
	public void testFindAll(){
		List<ProductOrder> orders = dao.findAll();
		Assert.assertNotNull(orders);
	}
	
	@Test
	public void testFindAllOrder(){
		List<OrderVO> orders = dao.findAllOrder();
		System.out.println(orders);
		Assert.assertNotNull(orders);
	}
	
	@Test
	public void testFind(){
		Map<String, Object> filterParams = new HashMap<String, Object>();
		List<OrderVO> orders = dao.find(filterParams,  new RowBounds(0, 10));
		System.out.println("");
		System.out.println(orders);
		System.out.println("");
		Assert.assertNotNull(orders);
	}
	
	
	
}
