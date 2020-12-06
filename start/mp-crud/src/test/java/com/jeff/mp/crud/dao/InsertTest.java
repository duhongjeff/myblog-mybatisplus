package com.jeff.mp.crud.dao;

import com.jeff.mp.crud.CrudApp;
import com.jeff.mp.crud.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrudApp.class)
public class InsertTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void insert() {
        User user = new User();
        user.setName("peter");
        user.setAge(31);
        user.setManagerId(1088248166370832385L);
//        user.setEmail("lmq@baomidou.com");
        user.setCreateTime(LocalDateTime.now());
        int row = userMapper.insert(user);
        System.out.println("affected rows：" + row);
        System.out.println("auto incremented id: " + user.getId());
    }

}