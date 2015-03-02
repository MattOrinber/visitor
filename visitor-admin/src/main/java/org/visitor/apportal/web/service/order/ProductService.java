package org.visitor.apportal.web.service.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.apportal.web.repository.order.ProductMybatisDao;
import org.visitor.appportal.visitor.domain.Product;

@Component
@Transactional
public class ProductService {

	@Autowired
	private ProductMybatisDao productDao;

	/**
	 * 创建分页查询.
	 * 
	 * @param filterParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return Page<Test>
	 */
	public Page<Product> getProduct(Map<String, Object> filterParams,
			int pageNumber, int pageSize) {

		List<Product> products = productDao.find(filterParams,
				buildRowBounds(pageNumber, pageSize));
		int totalnum = productDao.findTotalNum(filterParams);

		return new PageImpl<Product>(products, new PageRequest(pageNumber - 1,
				pageSize), totalnum);
	}

	/**
	 * 查找所有的Product.
	 * 
	 * @return List<Product>
	 */
	public List<Product> getAllProduct() {
		return productDao.findAll();
	}

	/**
	 * 创建分页请求.
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return RowBounds
	 */
	private RowBounds buildRowBounds(int pageNumber, int pageSize) {
		return new RowBounds((pageNumber - 1) * pageSize, pageSize);
	}

	/**
	 * 保存Product.
	 * 
	 * @param newProduct
	 */
	public void saveProduct(Product newProduct) {
		if (newProduct.getProductId() != null) {
			productDao.update(newProduct);
		} else {
			productDao.insert(newProduct);
		}
	}

	/**
	 * 根据主键获取Product
	 * 
	 * @param id
	 * @return Product
	 */
	public Product getProduct(Long product_id) {
		return productDao.findByProductId(product_id);
	}

	/**
	 * 根据主键删除Product
	 * 
	 * @param id
	 */
	public void deleteProduct(Long product_id) {
		productDao.delete(product_id);
	}

}
