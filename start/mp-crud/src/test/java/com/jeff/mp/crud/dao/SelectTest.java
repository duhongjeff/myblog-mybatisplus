package com.jeff.mp.crud.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.jeff.mp.crud.CrudApp;
import com.jeff.mp.crud.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrudApp.class)
public class SelectTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void selectById() {
        User user = userMapper.selectById(1087982257332887553L);
        System.out.println(user);
    }

    @Test
    public void selectBatchIds() {
        List<Long> ids = Arrays.asList(
                1087982257332887553L,
                1094590409767661570L,
                1094592041087729666L
        );
        List<User> list = userMapper.selectBatchIds(ids);
        list.forEach(System.out::println);
    }

    @Test
    public void selectByMap() {
        Map<String, Object> map = new HashMap<>();
        //The key of the map refers to the column name in the mysql table, not the attribute name of the java entity
        map.put("name", "peter");
        map.put("manager_id", 1088248166370832385L);
        List<User> list = userMapper.selectByMap(map);
        list.forEach(System.out::println);
    }

    @Test
    public void selectList_all() {
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }

    /**
     * The name contains 'rain' and the age is less than 40
     * SELECT * FROM `user`
     * WHERE `name` LIKE'%rain%' AND `age`< 40
     */
    @Test
    public void selectList_like_lt() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "rain").lt("age", 40);
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * The name contains 'rain', and the age is greater than or equal to 20 and less than or equal to 40, and the email is not empty
     * SELECT * FROM `user`
     * WHERE `name` LIKE'%rain%' AND `age` <= 40 AND `age` >= 20 AND `email` IS NOT NULL
     */
    @Test
    public void selectList_between_isNotNull() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "rain").between("age", 20, 40).isNotNull("email");
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Last name Zhao or age greater than or equal to 25, sorted in descending order of age, same age sorted in ascending order of id
     * SELECT * FROM `user`
     * WHERE `name` LIKE'Zhao%' OR `age` >= 25 ORDER BY `age` DESC, `id` ASC;
     */
    @Test
    public void selectList_or_orderByDesc_orderByAsc() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.likeRight("name", "Zhao").or().ge("age", 20)
                .orderByDesc("age").orderByAsc("id");
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * The creation date is February 14, 2019, and the immediate superior is Wang
     * SELECT * FROM `user`
     * WHERE DATE_FORMAT(create_time,'%Y-%m-%d')='2019-02-14'
     * AND manager_id IN (SELECT id FROM `user` WHERE `name` LIKE'wong%')
     */
    @Test
    public void selectList_apply_inSql() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.apply("DATE_FORMAT(create_time,'%Y-%m-%d')={0}", "2019-02-14")
                .inSql("manager_id", "SELECT id FROM `user` WHERE `name` LIKE 'wong%'");
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Last name is Wang and (age is less than 40 or mail is not empty)
     * SELECT * FROM `user`
     * WHERE `name` LIKE'Wang%' AND (`age`< 40 OR `email` IS NOT NULL)
     */
    @Test
    public void selectList_and_lambda() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.likeRight("name", "Wang")
                .and(q -> q.lt("age", 40).or().isNotNull("email"));
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Name is Wang and or (age less than 40 and age greater than 20 and the mail is not empty)
     * SELECT * FROM `user`
     * WHERE `name` LIKE'Wang%' OR (`age`< 40 AND `age`> 20 AND `email` IS NOT NULL)
     */
    @Test
    public void selectList_or_lambda() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.likeRight("name", "Wang")
                .or(q -> q.lt("age", 40).gt("age", 20).isNotNull("email"));
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * (Age is less than 40 or the mailbox is not empty) and name is Wang
     * SELECT * FROM `user`
     * WHERE (`age`< 40 OR `email` IS NOT NULL) AND `name` LIKE'Wang%'
     */
    @Test
    public void selectList_nested() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.nested(q -> q.lt("age", 40).or().isNotNull("email"))
                .likeRight("name", "Wang");
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Age 30, 31, 34, 35
     * SELECT * FROM `user` WHERE `age` IN (30,31,34,35);
     */
    @Test
    public void selectList_in() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.in("age", Arrays.asList(30, 31, 34, 35));
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Return only one that meets the conditions (only call the last time, there is a risk of SQL injection)
     * SELECT * FROM `user` WHERE `age` IN (30,31,34,35) LIMIT 1;
     */
    @Test
    public void selectList_last() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Only query specified fields
     * SELECT `name`,`age` FROM `user` WHERE `age` IN (30,31,34,35) LIMIT 1;
     */
    @Test
    public void selectList_select_include() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.select("name", "age").in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Exclude specified fields
     */
    @Test
    public void selectList_select_exclude() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1")
                .select(
                        User.class,
                        info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id")
                );
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Conditional judgment
     */
    @Test
    public void selectList_condition() {
        String name = "John";
        String email = "";
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like(StringUtils.isNotEmpty(name), "name", name)
                .like(StringUtils.isNotEmpty(email), "email", email);
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Entity class as conditional constructor
     * The default is equivalent query, you can set custom conditions in the entity class properties
     */
    @Test
    public void selectList_entity() {
        User whereUser = new User();
        whereUser.setName("Peter");
        whereUser.setAge(32);//age<30
        QueryWrapper<User> query = new QueryWrapper<>(whereUser);
        query.eq("manager_id", "1088248166370832385");

        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * allEq
     */
    @Test
    public void selectList_allEq() {
        QueryWrapper<User> query = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "peter");
        params.put("age", 31);
        params.put("email", null);
        query.allEq((k, v) -> !k.equals("name"), params, false);//First param is a filter
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Application scenario of selectMaps 1: When there are too many columns in the table, but only a few columns are actually needed, it is not necessary to return an entity class at this time
     */
    @Test
    public void selectMaps() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "rain").lt("age", 40).select("name", "age");
        List<Map<String, Object>> maps = userMapper.selectMaps(query);
        maps.forEach(System.out::println);
    }

    /**
     * SelectMaps application scenario 2: query statistical results
     * According to the direct superior grouping, query the average age, maximum age, and minimum age of each group, and only select the groups whose total age is less than 100
     * SELECT AVG(age) avg_age,MIN(age) min_age,MAX(age) max_age
     * FROM `user`
     * GROUP BY `manager_id`
     * HAVING SUM(age)<100;
     */
    @Test
    public void selectMaps2() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.select("AVG(age) avg_age", "MIN(age) min_age", "MAX(age) max_age")
                .groupBy("manager_id")
                .having("SUM(age)<{0}", 100);
        List<Map<String, Object>> maps = userMapper.selectMaps(query);
        maps.forEach(System.out::println);
    }

    /**
     * selectObjs only returns the first column, other columns are abandoned
     * Application scenario: when you only need to return one column
     */
    @Test
    public void selectObjs() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "rain").lt("age", 40).select("name", "age");
        List<Object> list = userMapper.selectObjs(query);
        list.forEach(System.out::println);
    }

    /**
     * Return the total number of records
     */
    @Test
    public void selectCount() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "rain").lt("age", 40);
        Integer count = userMapper.selectCount(query);
        System.out.println("total records：" + count);
    }

    /**
     * selectOne: Only one record can be queried, and an error will be reported if multiple records are queried
     */
    @Test
    public void selectOne() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "liu").lt("age", 40);
        User user = userMapper.selectOne(query);
        System.out.println(user);
    }

    /**
     * lambda conditional constructor
     */
    @Test
    public void lambdaQueryWrapper1() {
//        LambdaQueryWrapper<User> lambdaQ = new QueryWrapper<User>().lambda();
//        LambdaQueryWrapper<User> lambdaQ = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> lambdaQ = Wrappers.<User>lambdaQuery();

        lambdaQ.like(User::getName, "rain").lt(User::getAge, 40);
        List<User> list = userMapper.selectList(lambdaQ);
        list.forEach(System.out::println);
    }

    /**
     * Lambda conditional constructor: prevent miswriting (for example, the column name "name" may be miswritten)
     */
    @Test
    public void lambdaQueryWrapper2() {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.likeRight(User::getName, "Wang")
                .and(q -> q.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * Chained lambda conditional constructor: a more elegant way of writing
     */
    @Test
    public void lambdaQueryChainWrapper() {
        List<User> list = new LambdaQueryChainWrapper<User>(userMapper)
                .likeRight(User::getName, "Wang")
                .and(
                        q -> q
                                .lt(User::getAge, 40)
                                .or()
                                .isNotNull(User::getEmail)
                )
                .list();
        list.forEach(System.out::println);
    }

}