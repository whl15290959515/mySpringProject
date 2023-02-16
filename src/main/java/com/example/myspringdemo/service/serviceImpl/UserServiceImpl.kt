package com.example.myspringdemo.service.serviceImpl

import org.springframework.stereotype.Service
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.myspringdemo.entity.User
import com.example.myspringdemo.mapper.UserMapper
import com.example.myspringdemo.service.UserService

@Service
open class UserServiceImpl : ServiceImpl<UserMapper, User>(), UserService {

}