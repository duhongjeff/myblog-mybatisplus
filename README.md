<center><b>Mybatis Plus</b></center>
A ready to use plugin on the top of Mybatis, get your CURD operation withany any code!</br>
Author: Jeff Du Hong

1. Summary

   MyBatis-Plus (MP for short) is an enhancement tool for MyBatis. On the basis of MyBatis, it only enhances and does not change. It is born to simplify development and improve efficiency. You can get to know more in https://baomidou.com/en/

   - Avoid to generate a batch of xml mappers which have to be maintained once database design changed.
   - No intrusion: The introduction of it will not affect your existing projects. Mybatis Plus is on the top of mybatis framework, using reflection mechansim to map fields and auto generate different methods xml mapper files, if you have already defined in your own methods in mappers, the system ones will be overridden by yours
   - CURD such as saveOrUpdate, selectByPagination, selectByNotNullCriterias are already there in service level and DAO level 

2. Quick Demo

   - Environment: Spring Boot 2.16 + Java 8 + Mybatis plus 3.12 + mysql

   - The below displays a simple and complete demo for mybatis plus, compared with mybatis, no need to additionally put xml mapper and implementations in UserMapper class

     ![image-20201206090643373](/Users/jeff/Library/Application Support/typora-user-images/image-20201206090643373.png)

     ![image-20201206091028523](/Users/jeff/Library/Application Support/typora-user-images/image-20201206091028523.png)

3. Demo Source Code

   - put mybatis plus maven dependency in pom.xml. To avoid any conflict, don't put other mybatis related dependency like mybatis-spring-boot-starter

   - The pom.xml

     ```xml
             <dependency>
                 <groupId>com.baomidou</groupId>
                 <artifactId>mybatis-plus-boot-starter</artifactId>
                 <version>3.1.2</version>
             </dependency>
     ```

   - Configure data source and logger (to print out executing sql) in yml

     ```yml
     # datasource configuration
     spring:
       datasource:
         driver-class-name: com.mysql.cj.jdbc.Driver
         url: jdbc:mysql://testhost:3306/mp?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
         username: root
         password: '#Dsf135246'
     
     # logging
     logging:
       level:
         root: warn
         com.dsf.mp.annotation.dao: trace
       pattern:
         console: '%p%m%n'
         
     # global configuration for mybatis plus
     mybatis-plus:
       global-config:
         db-config:
           id-type: id_worker
           insert-strategy: not_null
           update-strategy: not_null
     ```

   - Add @MapperScan in Spring Boot starter class

     ```text
     about @MapperScan
     This annotation enables the system to find all interfaces under mentioned objects to generate proxy objects, otherwise need to put @Mapper in each interface
     ```

     ```java
     @SpringBootApplication
     @MapperScan("com.dsf.mp.crud.dao")
     public class CrudApp {
         public static void main(String[] args) {
             SpringApplication.run(CrudApp.class, args);
         }
     }
     ```

   - Add entity class

     ```java
     @Data
     @TableName("mp_user")
     public class User {
     
         @TableId(value = "user_id")
         private Long id;
     
         @TableField("user_name")
         private String name;
     
         private Integer age;
     
         private String email;
     
         private Long managerId;
     
       	@TableField(fill = FieldFill.INSERT)
         private LocalDateTime createTime;
       
       	//Below are three ways to exclude currenct field being serialised so as to exclude map to database field, we only need one of the three which will work
       
         private transient String remark1;
     
         public static String remark2;
     
         @TableField(exist=false)
         private String remark;
     }
     ```

   - Add user mapper

     ```java
     public interface UserMapper extends BaseMapper<User> {
     
     }
     ```

     <b>Common Annotation</b>

     @TableName

     |    Properties    |  Type   | Required | Default val | Description                                                  |
     | :--------------: | :-----: | :------: | :---------: | ------------------------------------------------------------ |
     |      value       | String  |    N     |     ""      | DB table name                                                |
     |    resultMap     | String  |    N     |     ""      | resultMap id in *mapper.xml                                  |
     |      schema      | String  |    N     |     ""      | schema(@since 3.1.1)                                         |
     | keepGlobalPrefix | boolean |    N     |    false    | true: keep using the tablePrefix in Global Configuration(if tablePrefix configurated in Global, will set the Global value here automatically)(@since 3.1.1) |

     @TableId (We use ID_WORKER as PK generation rule in our demo which returns a 64 digits big integer)

     | Properties |  Type  | Required | Default val |                         Description                          |
     | :--------: | :----: | :------: | :---------: | :----------------------------------------------------------: |
     |   value    | String |    N     |     ""      | column name of the PK, if java entity propertyName is "id", will be recognized as PK |
     |    type    |  Enum  |    N     | IdType.NONE |          PK type(e.g. AUTO, UUID, ID_WORKER, INPUT)          |

     |      Val      |              Descp              |
     | :-----------: | :-----------------------------: |
     |     AUTO      |       autoincrement by DB       |
     |     INPUT     |      user specify manually      |
     |   ID_WORKER   | distributed union ID, Long type |
     |     UUID      |  UUID String with length of 32  |
     |     NONE      |             nothing             |
     | ID_WORKER_STR |    String value of ID_WORKER    |

     @TableField

     |    Properties    |  Type   | Required | Default val |                         Description                          |
     | :--------------: | :-----: | :------: | :---------: | :----------------------------------------------------------: |
     |      value       | String  |    N     |     ""      |                         column name                          |
     |        el        | String  |    N     |     ""      | Mapped to `#{ ... }` for native, equivalently write `#{ ... }` in *mapper.xml |
     |      exist       | boolean |    N     |    true     |         false: NOT a column, just temporary property         |
     |    condition     | String  |    N     |     ""      | config the expression in where condition, by default it's `EQUAL`, [reference](https://github.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-annotation/src/main/java/com/baomidou/mybatisplus/annotation/SqlCondition.java) |
     |      update      | String  |    N     |     ""      | e.g. value="version", update="%s+1", when do update, 'version=version+1' will be appended to `update xx_table set xxx=xxx` (this property has higher priority than `el` ) |
     |  insertStrategy  |  Enum   |    N     |   DEFAULT   | specify the strategy of this column when do insert, e.g. NOT_NULL: `insert into table_a(<if test="columnProperty != null">column</if>) values (<if test="columnProperty != null">#{columnProperty}</if>)` (since v_3.1.2) |
     |  updateStrategy  |  Enum   |    N     |   DEFAULT   | specify the strategy of this column when do update, e.g. IGNORED: `update table_a set column=#{columnProperty}` (since v_3.1.2) |
     |  whereStrategy   |  Enum   |    N     |   DEFAULT   | specify the strategy of this column when do query, e.g. NOT_EMPTY: `where <if test="columnProperty != null and columnProperty!=''">column=#{columnProperty}</if>` (since v_3.1.2) |
     |       fill       |  Enum   |    N     |   DEFAULT   |      auto fill strategy: INSERT, UPDATE, INSERT_UPDATE       |
     |      select      | boolean |    N     |    true     |   false: this column will not appear in select expression    |
     | keepGlobalFormat | boolean |    N     |    false    | whether keep the Global column name format(e.g. UnderscoreToCamelCase) (@since 3.1.1) |

     ###### Field Strategy

     |    Val    |                            Descp                             |
     | :-------: | :----------------------------------------------------------: |
     |  IGNORED  |                           ignored                            |
     | NOT_NULL  | <if test="columnProperty != null">column=#{columnProperty}</if> |
     | NOT_EMPTY | <if test="columnProperty != null and columnProperty!=''">(only support String column, for other types, will processed as NOT_NULL) |
     |  DEFAULT  |                    keep the Global config                    |

     ###### Fill Strategy

     |      Val      |                            Descp                             |
     | :-----------: | :----------------------------------------------------------: |
     |    DEFAULT    |                            bypass                            |
     |    INSERT     | fill the column when do insert(should specify the filled value in MetaObjectHandler) |
     |    UPDATE     |                fill the column when do update                |
     | INSERT_UPDATE |          fill the column when do both insert/update          |

     

4. CURD Operation

   By extending the BaseMapper, CRUD implementations are by default there so we won't add more in this demo. We will deep into that part soon.

   The BaseMapper provides the below:

   ```java
   public interface BaseMapper<T> extends Mapper<T> {
       int insert(T entity);
   
       int deleteById(Serializable id);
   
       int deleteByMap(@Param("cm") Map<String, Object> columnMap);
   
       int delete(@Param("ew") Wrapper<T> wrapper);
   
       int deleteBatchIds(@Param("coll") Collection<? extends Serializable> idList);
   
       int updateById(@Param("et") T entity);
   
       int update(@Param("et") T entity, @Param("ew") Wrapper<T> updateWrapper);
   
       T selectById(Serializable id);
   
       List<T> selectBatchIds(@Param("coll") Collection<? extends Serializable> idList);
   
       List<T> selectByMap(@Param("cm") Map<String, Object> columnMap);
   
       T selectOne(@Param("ew") Wrapper<T> queryWrapper);
   
       Integer selectCount(@Param("ew") Wrapper<T> queryWrapper);
   
       List<T> selectList(@Param("ew") Wrapper<T> queryWrapper);
   
       List<Map<String, Object>> selectMaps(@Param("ew") Wrapper<T> queryWrapper);
   
       List<Object> selectObjs(@Param("ew") Wrapper<T> queryWrapper);
   
       IPage<T> selectPage(IPage<T> page, @Param("ew") Wrapper<T> queryWrapper);
   
       IPage<Map<String, Object>> selectMapsPage(IPage<T> page, @Param("ew") Wrapper<T> queryWrapper);
   }
   ```

   - select by id

     ```java
     @Test
     public void selectById() {
         User user = userMapper.selectById(1087982257332887553L);
         System.out.println(user);
     }
     ```

   - select by list of (batch of) ids

     ```java
     List<Long> ids = Arrays.asList(
         1087982257332887553L,
         1094590409767661570L,
         1094592041087729666L
     );
     
     List<User> list = userMapper.selectBatchIds(ids);
     list.forEach(System.out::println);
     ```

   - select by map

     ```java
     @Test
     public void selectByMap() {
         Map<String, Object> map = new HashMap<>();
         //key of map is the column name，not java entity name
         map.put("name", "tom");
         map.put("manager_id", 1088248166370832385L);
         List<User> list = userMapper.selectByMap(map);
         list.forEach(System.out::println);
     }
     ```

   - select all 

     ```java
     @Test
     public void selectList_all() {
         List<User> list = userMapper.selectList(null);
         list.forEach(System.out::println);
     }
     ```

   - select with like and less than condition

     ```java
     //SELECT * FROM `user` WHERE `name` LIKE '%to%' AND `age`< 40
     @Test
     public void selectList_like_lt() {
         QueryWrapper<User> query = new QueryWrapper<>();
         query.like("name", "to").lt("age", 40);
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select with between and not null condition

     ```java
     //SELECT * FROM `user` WHERE `name` LIKE '%to%' AND `age` <= 40 AND `age` >= 20 AND `email` IS NOT NULL
     @Test
     public void selectList_between_isNotNull() {
         QueryWrapper<User> query = new QueryWrapper<>();
         query.like("name", "to").between("age", 20, 40).isNotNull("email");
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select with or condition

     ```java
     //SELECT * FROM `user` WHERE `name` LIKE 'to%' OR `age` >= 25 ORDER BY `age` DESC , `id` ASC;
     @Test
     public void selectList_or_orderByDesc_orderByAsc() {
         QueryWrapper<User> query = new QueryWrapper<>();
         query.likeRight("name", "to").or().ge("age", 20)
         .orderByDesc("age").orderByAsc("id"); 
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select with specified date format and subquery

     ```java
     //SELECT * FROM `user` WHERE DATE_FORMAT(create_time,'%Y-%m-%d')='2019-02-14'
     // AND manager_id IN (SELECT id FROM `user` WHERE `name` LIKE 'to%')
     @Test
     public void selectList_apply_inSql() {
         QueryWrapper<User> query = new QueryWrapper<>();
         query.apply("DATE_FORMAT(create_time,'%Y-%m-%d')={0}", "2019-02-14")
         .inSql("manager_id", "SELECT id FROM `user` WHERE `name` LIKE 'to%'");
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select with nested query

     ```java
     //* SELECT * FROM `user` WHERE (`age`< 40 OR `email` IS NOT NULL) AND `name` LIKE 'to%'
     @Test
     public void selectList_nested() {
         QueryWrapper<User> query = new QueryWrapper<>();
         query.nested(q -> q.lt("age", 40).or().isNotNull("email"))
         .likeRight("name", "to");
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select with subquery

     ```java
     //SELECT * FROM `user` WHERE `age` IN (30,31,34,35);
     //.last("limit 1"); limit 1
     @Test
     public void selectList_in() {
         QueryWrapper<User> query = new QueryWrapper<>();
         query.in("age", Arrays.asList(30, 31, 34, 35));
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select certain columns only

     ```java
     //SELECT `name`,`age` FROM `user` WHERE `age` IN (30,31,34,35) LIMIT 1;
     @Test
     public void selectList_select_include() {
         QueryWrapper<User> query = new QueryWrapper<>();
         query.select("name", "age").in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select without certain columns but the rest

     ```java
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
     ```

   - select with not-null fields condition

     ```java
     @Test
     public void selectList_condition() {
         String name = "to";
         String email = "";
         QueryWrapper<User> query = new QueryWrapper<>();
         query.like(StringUtils.isNotEmpty(name), "name", name)
              .like(StringUtils.isNotEmpty(email), "email", email);
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select with entity as pass-in parameters

     ```java
     @Test
     public void selectList_entity() {
         User whereUser = new User();
         whereUser.setName("tom");//name like "tom"
         whereUser.setAge(32);//age<30
       
         QueryWrapper<User> query = new QueryWrapper<>(whereUser);
         query.eq("manager_id", "1088248166370832385");
     
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select with pass-in fields all equal, if the field is null, ignore

     ```java
     @Test
     public void selectList_allEq() {
         QueryWrapper<User> query = new QueryWrapper<>();
       
         Map<String, Object> params = new HashMap<>();
         params.put("name", "tom");
         params.put("age", 31);
         params.put("email", null);
         //first param: filter condition, second:map params, third: choose false to ignore query 'is null' 
         query.allEq((k, v) -> !k.equals("name"), params, false);
             
         List<User> list = userMapper.selectList(query);
         list.forEach(System.out::println);
     }
     ```

   - select with aggregated functions

     ```java
     /*SELECT AVG(age) avg_age,MIN(age) min_age,MAX(age) max_age FROM `user` GROUP BY `manager_id` HAVING SUM(age)<100; */
     @Test
     public void selectMaps2() {
         QueryWrapper<User> query = new QueryWrapper<>();
         query.select("AVG(age) avg_age", "MIN(age) min_age", "MAX(age) max_age")
             .groupBy("manager_id")
             .having("SUM(age)<{0}", 100); 
         List<Map<String, Object>> maps = userMapper.selectMaps(query);
         maps.forEach(System.out::println);
     }
     ```

   - select count

     ```java
     @Test
     public void selectCount() {
         QueryWrapper<User> query = new QueryWrapper<>();
         query.like("name", "to").lt("age", 40);
         Integer count = userMapper.selectCount(query);
         System.out.println("total records：" + count);
     }
     ```

   - update by id

     ```java
     @Test
     public void updateById() {
         User user = new User();
         user.setId(1088248166370832385L);
         user.setAge(26);
         user.setEmail("wtf2@baomidou.com");
         int rows = userMapper.updateById(user);
         System.out.println("affected rows：" + rows);
         }
     ```

   - update by entity

     ```java
     @Test
     public void update() {
         UpdateWrapper<User> update = new UpdateWrapper<>();
         update.eq("name", "lee").eq("age", 28);
     
         User user = new User();
         user.setAge(29);
         user.setEmail("lyw2@baomidou.com");
         int rows = userMapper.update(user, update);
         System.out.println("affected rows：" + rows);
     }
     ```

     ```java
     @Test
     public void update_entity() {
         User whereUser = new User();
         whereUser.setName("lee");
         
         UpdateWrapper<User> update = new UpdateWrapper<>(whereUser);
         update.eq("age", 29);
     
         User user = new User();
         user.setAge(30);
         user.setEmail("lyw3@baomidou.com");
     
         int rows = userMapper.update(user, update);
         System.out.println("affected rows：" + rows);
     }
     ```

   - insert

     ```java
     @Test
     public void insert() {
         User user = new User();
         user.setName("tom");
         user.setAge(31);
         user.setManagerId(1088248166370832385L);
       
         user.setCreateTime(LocalDateTime.now());
         int row = userMapper.insert(user);
         System.out.println("affected rows："+row);
         System.out.println("auto-generated id: "+user.getId());
     }
     ```

   - delete by id

     ```java
     @Test
     public void deleteById() {
         int rows = userMapper.deleteById(1170243901535006722L);
         System.out.println("affected rows：" + rows);
     }
     ```

   - delete by map

     ```java
     @Test
     public void deleteByMap(){
         Map<String,Object> map = new HashMap<>();
         map.put("name","tom");
         map.put("age",31);
     
         int rows = userMapper.deleteByMap(map);
     
         System.out.println("affected rows：" + rows);
     }
     ```

   - delete batch ids

     ```java
     @Test
     public void deleteBatchIds(){
         List<Long> ids = Arrays.asList(
             1170735636174278657L,
             1170735727241048065L
         );
     
         int rows = userMapper.deleteBatchIds(ids);
     
         System.out.println("affected rows：" + rows);
     }
     ```

   - delete entity

     ```java
      @Test
      public void delete_entity(){
          User whereUser = new User();
          whereUser.setName("tom");
     
          LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>(whereUser);
          query.lt(User::getAge,50);
     
          int rows = userMapper.delete(query);
     
          System.out.println("affected rows：" + rows);
      }
     ```

     
