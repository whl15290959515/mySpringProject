package com.example.myspringdemo.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import com.example.myspringdemo.entity.User

@Mapper
interface UserMapper : BaseMapper<User> {
}