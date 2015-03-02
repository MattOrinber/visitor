
package org.visitor.apportal.web.service.config;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.apportal.web.repository.config.CityInfoMybatisDao;
import org.visitor.appportal.visitor.domain.City;

@Component
@Transactional
public class CityInfoService {

    @Autowired
    private CityInfoMybatisDao city_infoDao;

    /**
     * 创建分页查询.
     * 
     * @param filterParams
     * @param pageNumber
     * @param pageSize
     * @param sortType
     * @return Page<Test>
     */
    public Page<City> getCity_info(Map<String, Object> filterParams, int pageNumber, int pageSize) {

        List<City> city_infos = city_infoDao.find(filterParams, buildRowBounds(pageNumber, pageSize));
        int totalnum = city_infoDao.findTotalNum(filterParams);

        return new PageImpl<City>(city_infos, new PageRequest(pageNumber - 1, pageSize), totalnum);
    }

    /**
     * 查找所有的City.
     * 
     * @return List<City>
     */
    public List<City> getAllCity_info() {
        return city_infoDao.findAll();
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
     * 保存City_info.
     * 
     * @param newCity
     */
    public void saveCity_info(City newCity_info) {
        if (newCity_info.getCityId() != null && newCity_info.getCityName() != null     ) {
            city_infoDao.update(newCity_info);
        } else {
            city_infoDao.insert(newCity_info);
        }
    }

    /**
     * 根据主键获取City_info
     * 
     * @param id
     * @return City_info
     */
    public City getCity_info(Long city_id) {
        return city_infoDao.findById(city_id);
    }

    /**
     * 根据主键删除City_info
     * 
     * @param id
     */
    public void deleteCity_info(Long city_id) {
        city_infoDao.delete(city_id);
    }

}
