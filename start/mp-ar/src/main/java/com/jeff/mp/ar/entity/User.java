package com.jeff.mp.ar.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> {
    private static final long serialVersionUID = -3420609408231585157L;


    /**
     * Setting of local primary key strategy:
     * AUTO(0): The database ID increases automatically and follows the database, not suitable for distributed scenarios
     * NONE(1): By default, follow the global strategy when the ID is not set locally
     * INPUT(2): The user enters the ID, which can be used as a test when the amount of data is small
     * <p>
     * --The following three types are automatically filled only when the ID of the inserted object is empty
     * ID_WORKER(3): The primary key of the snowflake algorithm of numeric type, which is also the global default strategy
     * UUID(4): UUID is 128 bits, it has strong identification, but the string is too long will affect the query time, it is used when the query time is not sensitive
     * ID_WORKER_STR(5): String type snowflake algorithm primary key
     */
    @TableId(type = IdType.NONE)
    private Long id;

    @TableField(condition = SqlCondition.LIKE)
    private String name;

    @TableField(condition = "%s&lt;#{%s}")
    private Integer age;

    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String email;

    private Long managerId;

    private LocalDateTime createTime;


}
