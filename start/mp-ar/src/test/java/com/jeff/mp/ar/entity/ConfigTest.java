package com.jeff.mp.ar.entity;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeff.mp.ar.ArApp;
import com.jeff.mp.ar.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArApp.class)
public class ConfigTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void mySelect() {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.likeRight(User::getName, "wong")
                .and(q -> q.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> list = userMapper.selectAll(query);
        list.forEach(System.out::println);
    }

    @Test
    public void selectList_entity() {
        User whereUser = new User();
        whereUser.setName("lee");
        whereUser.setAge(32);
        whereUser.setEmail("");
        QueryWrapper<User> query = new QueryWrapper<>(whereUser);

        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }
}
