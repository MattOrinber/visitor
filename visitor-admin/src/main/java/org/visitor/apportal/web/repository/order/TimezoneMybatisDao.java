package org.visitor.apportal.web.repository.order;

import java.util.List;
import java.util.Map;

import org.visitor.apportal.web.repository.MyBatisRepository;
import org.visitor.appportal.visitor.domain.TimeZone;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现.
 * 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author yong.cao
 * @create-time 2015-2-28
 * @revision 1.0.0
 * @E_mail yong.cao@renren-inc.com
 */
@MyBatisRepository
public interface TimezoneMybatisDao {

	TimeZone findById(String timezone_city,
			Short timezone_id);

	List<TimeZone> findAll();

	List<TimeZone> find(Map<String, Object> parameters);

}
