package com.jeff.mp.ar.entity;

import com.jeff.mp.ar.ArApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArApp.class)
public class PrimaryKeyStrategyTest {

    @Test
    public void insert() {
        User user = new User();
//        user.setId(123456L);//@TableId(type = IdType.AUTO)时不生效
        user.setName("wong");
        user.setAge(31);
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        boolean success = user.insert();
        System.out.println("insert successfully?：" + success);
    }

}