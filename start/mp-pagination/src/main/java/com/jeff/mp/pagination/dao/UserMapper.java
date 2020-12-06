package com.jeff.mp.pagination.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeff.mp.pagination.entity.User;
import com.jeff.mp.pagination.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {
    /**
     * self defined sql
     */
//    @Select("select * from `user` ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * Custom paging + multi-table joint check
     */
    IPage<User> selectUserPage(Page<User> page, @Param("vo") UserVo userVo);

}
