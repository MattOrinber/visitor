package org.visitor.apportal.web.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import org.visitor.apportal.web.entity.Menu;
import org.visitor.apportal.web.entity.Role;

@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class RoleMybatisDaoTest extends SpringTransactionalTestCase {

    @Autowired
    private RoleMybatisDao dao;

    @Test
    public void testInsertAndDeleteRoleMenu() {
        Role role = new Role(0L);//用于测试id=0

        List<Menu> menuList = new ArrayList<Menu>();
        Menu menu = new Menu(1L);
        Menu menu2 = new Menu(2L);
        menuList.add(menu);
        menuList.add(menu2);

        role.setMenuList(menuList);

        int count = countRowsInTable("web_role_menu");
        dao.insertRoleMenu(role);
        int afterInsertcount = countRowsInTable("web_role_menu");

        Assert.assertEquals(count + 2, afterInsertcount);
        
        dao.deleteRoleMenu(role.getId());
        int afterDeleteCount = countRowsInTable("web_role_menu");
        
        Assert.assertEquals(count, afterDeleteCount);
    }

}
