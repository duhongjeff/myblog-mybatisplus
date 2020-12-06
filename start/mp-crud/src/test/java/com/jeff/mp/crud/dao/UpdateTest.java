package com.jeff.mp.crud.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.jeff.mp.crud.CrudApp;
import com.jeff.mp.crud.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrudApp.class)
public class UpdateTest {
    @Autowired
    UserMapper userMapper;

    /**
     * Update:updateById
     */
    @Test
    public void updateById() {
        User user = new User();
        user.setId(1088248166370832385L);
        user.setAge(26);
        user.setEmail("wtf2@baomidou.com");
        int rows = userMapper.updateById(user);
        System.out.println("affected rows：" + rows);
    }

    /**
     * Update: By wrapper
     */
    @Test
    public void update() {
        UpdateWrapper<User> update = new UpdateWrapper<>();
        update.eq("name", "Lee").eq("age", 28);

        User user = new User();
        user.setAge(29);
        user.setEmail("lee@baomidou.com");
        int rows = userMapper.update(user, update);
        System.out.println("affected rows：" + rows);
    }

    /**
     * Update: via entity(or wrapper)
     */
    @Test
    public void update_entity() {
        User whereUser = new User();
        whereUser.setName("Lee");

        UpdateWrapper<User> update = new UpdateWrapper<>(whereUser);
        update.eq("age", 29);

        User user = new User();
        user.setAge(30);
        user.setEmail("lee@baomidou.com");

        int rows = userMapper.update(user, update);
        System.out.println("affected rows：" + rows);
    }

    /**
     * Update:
     * If you only update one or a few columns, use set to be more concise
     */
    @Test
    public void update_set() {
        UpdateWrapper<User> update = new UpdateWrapper<>();
        update.eq("name", "lee")
                .eq("age", 30)
                .set("age", 31)
                .set("email", "lee@baomidou.com");

        int rows = userMapper.update(null, update);
        System.out.println("affected rows：" + rows);
    }

    /**
     * Update:
     * Use LambdaUpdateWrapper to prevent column names from being written by mistake
     */
    @Test
    public void update_lambda() {
        LambdaUpdateWrapper<User> update = new LambdaUpdateWrapper<>();

        update.eq(User::getName, "lee")
                .eq(User::getAge, 31)
                .set(User::getAge, 32)
                .set(User::getEmail, "lee@baomidou.com");

        int rows = userMapper.update(null, update);
        System.out.println("affected rows：" + rows);
    }

    /**
     * Update:
     * Use LambdaUpdateChainWrapper to make the code more concise
     */
    @Test
    public void update_lambdaChain() {
        User user = new User();
        user.setAge(33);
        user.setEmail("lee@baomidou.com");

        boolean success = new LambdaUpdateChainWrapper<User>(userMapper)
                .eq(User::getName, "lee")
                .eq(User::getAge, 32)
                .update(user);

        System.out.println("update successful?：" + success);
    }

    /**
     * delete
     */
    @Test
    public void delete() {
        int rows = userMapper.deleteById(1170243901535006722L);

        System.out.println("affected rows：" + rows);
    }

}