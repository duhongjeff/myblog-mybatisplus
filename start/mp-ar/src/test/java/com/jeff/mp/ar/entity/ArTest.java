package com.jeff.mp.ar.entity;

import com.jeff.mp.ar.ArApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * AR mode
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArApp.class)
public class ArTest {

    @Test
    public void insert() {
        User user = new User();
        user.setName("tom");
        user.setAge(31);
        user.setManagerId(1088248166370832385L);
//        user.setEmail("lh@baomidou.com");
        user.setCreateTime(LocalDateTime.now());
        boolean success = user.insert();
        System.out.println("insert successfully?：" + success);
    }

    @Test
    public void selectById() {
        User user = new User();
        User userSelect = user.selectById(1088248166370832385L);
        System.out.println("return the same object?" + (userSelect == user));
        System.out.println(userSelect);
    }

    @Test
    public void selectById2() {
        User user = new User();
        user.setId(1088248166370832385L);
        User userSelect = user.selectById();
        System.out.println("return the same object：" + (userSelect == user));
        System.out.println(userSelect);
    }

    @Test
    public void updateById() {
        User user = new User();
        user.setId(1170760467657715713L);
        user.setName("Rio");
        boolean success = user.updateById();
        System.out.println("insert successfully?：" + success);
    }

    /**
     * Delete:
     * If there is no specified id, the number of affected rows is 0, which is also regarded as a successful deletion
     */
    @Test
    public void deleteById() {
        User user = new User();
        user.setId(1170760467657715766L);
        boolean success = user.deleteById();
        System.out.println("insert successfully?：" + success);
    }

    /**
     * Add or update:
     * If no id is set, it is directly regarded as insert
     */
    @Test
    public void insertOrUpdate() {
        User user = new User();
        user.setName("Ken");
        user.setAge(24);
        user.setEmail("ken@baomidou.com");
        user.setCreateTime(LocalDateTime.now());
        boolean success = user.insertOrUpdate();
        System.out.println("insert(update) successfully?：" + success);
    }

    /**
     * Add or update:
     * If you set the id, first query whether there is a record with this id, if there is a record with this id, it will be regarded as update, if not, it will be regarded as insert
     */
    @Test
    public void insertOrUpdate2() {
        User user = new User();
        user.setId(1170767968797159426L);
        user.setAge(25);
        user.setCreateTime(LocalDateTime.now());
        boolean success = user.insertOrUpdate();
        System.out.println("insert(update) successfully?：" + success);
    }
}