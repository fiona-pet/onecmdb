package org.cmdb.repository;

import org.cmdb.TestSmartApplication;
import org.cmdb.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 用户信息
 * Created by tom on 2017-03-07 13:25:01.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestSmartApplication.class)
@ComponentScan("com.its.smart")
@EnableAutoConfiguration
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Test
    public void findAll() {
        List<User> users = dao.findAllBy();

        Assert.assertNotNull(users);

        Assert.assertEquals(users.size(), 0);
    }
}
