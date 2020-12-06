package com.jeff.mp.annotation.dao;

import com.jeff.mp.annotation.AnnotationApp;
import com.jeff.mp.annotation.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnnotationApp.class)
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void select() {
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }

    @Test
    public void insert() {
        User user = new User();
        user.setName("peter");
        user.setAge(31);
        user.setManagerId(2L);
//        user.setEmail("lmq@baomidou.com");
        user.setCreateTime(LocalDateTime.now());
        user.setRemark("remarks~~");
        int row = userMapper.insert(user);
        System.out.println("affected rows：" + row);
        System.out.println("auto incremented id: " + user.getId());
    }

}