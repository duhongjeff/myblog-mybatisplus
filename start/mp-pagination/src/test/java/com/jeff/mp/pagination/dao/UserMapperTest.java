package com.jeff.mp.pagination.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeff.mp.pagination.PaginationApp;
import com.jeff.mp.pagination.entity.User;
import com.jeff.mp.pagination.vo.UserVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaginationApp.class)
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    /**
     * Custom Sql:
     * 1. Annotation form: com.jeff.mp.demo4.dao.UserMapper
     * 2.xml file format: /resources/mapper/UserMapper.xml
     */
    @Test
    public void mySelect() {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.likeRight(User::getName, "Wang")
                .and(q -> q.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> list = userMapper.selectAll(query);
        list.forEach(System.out::println);
    }

    /**
     * Single table paging: selectPage
     * Return a list of entity classes
     */
    @Test
    public void selectPage() {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.ge(User::getAge, 26).orderByDesc(User::getCreateTime);

        Page<User> page = new Page<>(1, 2);
        userMapper.selectPage(page, query);

        System.out.println("total pages：" + page.getPages());
        System.out.println("total records：" + page.getTotal());
        List<User> list = page.getRecords();//The object returned by paging is the same as the object passed in
        list.forEach(System.out::println);
    }

    /**
     * Single table paging: selectPage
     * Do not check the number of records
     */
    @Test
    public void selectPage_notSearchCount() {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.ge(User::getAge, 26).orderByDesc(User::getCreateTime);

        Page<User> page = new Page<>(1, 2, false);//Do not check the number of records
        IPage<User> iPage = userMapper.selectPage(page, query);//You can also use the new Page object to receive the returned data

        System.out.println("total pages：" + iPage.getPages());
        System.out.println("total records：" + iPage.getTotal());
        List<User> list = iPage.getRecords();
        list.forEach(System.out::println);
    }

    /**
     * Single table paging: selectMapsPage
     * Return to Map list
     */
    @Test
    public void selectMapsPage() {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.ge(User::getAge, 26).orderByDesc(User::getCreateTime);

        Page<User> page = new Page<>(1, 2);
        IPage<Map<String, Object>> iPage = userMapper.selectMapsPage(page, query);

        System.out.println("total pages：" + page.getPages());
        System.out.println("total records：" + iPage.getTotal());
        List<Map<String, Object>> list = iPage.getRecords();
        list.forEach(System.out::println);
    }

    /**
     * Customized multi-table joint check and paging
     * <p>
     * ps: I think that using exists for multiple tables is more efficient, more versatile, and simpler than using join.
     * Although only the data of a single main table is found, the business layer can query the corresponding page data of the associated sub-table again according to the needs of the main table id
     * Of course, you can also do asynchronous access query on the web side to prevent too much data in one query, which takes too long to reduce user experience
     */
    @Test
    public void selectUserPage() {
        UserVo userVo = new UserVo();
        userVo.setAgeStart(25);
        userVo.setHobby("study");

        Page<User> page = new Page<>(1, 2);
        userMapper.selectUserPage(page, userVo);

        System.out.println("total pages：" + page.getPages());
        System.out.println("total records：" + page.getTotal());
        List<User> list = page.getRecords();
        list.forEach(System.out::println);
    }


}