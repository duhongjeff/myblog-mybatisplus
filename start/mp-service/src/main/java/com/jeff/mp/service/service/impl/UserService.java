package com.jeff.mp.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeff.mp.service.dao.UserMapper;
import com.jeff.mp.service.entity.User;
import com.jeff.mp.service.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

}
