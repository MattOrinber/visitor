package org.visitor.apportal.web.repository.config;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.visitor.apportal.web.repository.MyBatisRepository;
import org.visitor.appportal.visitor.domain.City;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现.
 * 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author yong.cao
 * @create-time 2015-2-25
 * @revision 1.0.0
 * @E_mail yong.cao@renren-inc.com
 */
@MyBatisRepository
public interface CityInfoMybatisDao {

	City findById(@Param("city_id") Long city_id);

	List<City> findAll();

	List<City> find(Map<String, Object> parameters, RowBounds bounds);

	int findTotalNum(Map<String, Object> parameters);

	void insert(City city_info);

	void update(City city_info);

	void delete(Long city_id);
}
