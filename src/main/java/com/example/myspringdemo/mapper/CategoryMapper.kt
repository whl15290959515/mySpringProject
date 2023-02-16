package com.example.myspringdemo.mapper

import com.example.myspringdemo.entity.Category

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper

@Mapper
interface CategoryMapper :BaseMapper<Category> {
}