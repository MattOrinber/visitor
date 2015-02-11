package org.visitor.apportal.web.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.visitor.apportal.web.entity.Test;
import org.visitor.apportal.web.repository.TestMybatisDao;

@Component
@Transactional
public class SchedulerService {

    Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private TestMybatisDao testDao;


    /**
     * 初始化DB
     */
    public void initializeDB() {
        initializeTest();
        logger.info(new Date() + ": refresh data finish.");
    }

    /**
     * 初始化Test表
     */
    private void initializeTest() {
        Test test = new Test();
        test.setId(1L);
        test.setMsg("test");
        testDao.deleteAll();
        testDao.insert(test);
        logger.info(new Date() + ": initialize table Test.");
    }

}
