package com.example.myspringdemo.service

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.example.myspringdemo.entity.Category
import com.example.myspringdemo.entity.Dish
import org.springframework.beans.factory.annotation.Autowired

 interface CategoryService :IService<Category> {
    fun remove(id: Long)
}