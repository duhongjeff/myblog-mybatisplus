package com.jeff.mp.annotation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

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

    //transient keyword prevents the serialization of fields, thereby excluding non-table fields
    private transient String remark1;

    //The static keyword publicizes fields to exclude non-table fields
    public static String remark2;

    //Exclude non-table fields through the annotations provided by mp
    @TableField(exist = false)
    private String remark;


}
