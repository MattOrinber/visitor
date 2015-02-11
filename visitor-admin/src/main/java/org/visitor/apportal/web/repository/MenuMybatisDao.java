package org.visitor.apportal.web.repository;

import java.util.List;
import java.util.Map;

import org.visitor.apportal.web.entity.Menu;

@MyBatisRepository
public interface MenuMybatisDao {

    Menu findById(long id);

    Menu findMenuByName(String menuName);

    List<Menu> findAll();

    List<Menu> findMenuByRoleID(long id);

    List<Menu> findMenuByUserID(long id);

    List<Menu> find(Map<String, Object> parameters);

    void insert(Menu menu);

    void update(Menu menu);

    void delete(long id);

    void deleteChildMenu(String parentIds);


}
