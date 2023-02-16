package com.example.myspringdemo.service.serviceImpl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.myspringdemo.common.CustomException
import com.example.myspringdemo.entity.Category
import com.example.myspringdemo.entity.Dish
import com.example.myspringdemo.entity.SetMeal
import com.example.myspringdemo.mapper.CategoryMapper
import com.example.myspringdemo.service.CategoryService
import com.example.myspringdemo.service.DishService
import com.example.myspringdemo.service.SetMealService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class CategoryServiceImpl: ServiceImpl<CategoryMapper, Category>(), CategoryService {

    @Autowired
    val dishService: DishService?=null
    @Autowired
    val setMealService:SetMealService?=null

    override fun remove(id: Long) {
        val lambdaQuery = LambdaQueryWrapper<Dish>()
        lambdaQuery.eq(Dish::getCategoryId, id)
        val count = dishService!!.count(lambdaQuery)
        if (count > 0) {
            throw CustomException("当前分类下关联了菜品，无法删除")
        }

        val lambdaQuery1 = LambdaQueryWrapper<SetMeal>()
        lambdaQuery1.eq(SetMeal::getCategoryId, id)
        val count1 = setMealService!!.count()
        if (count1 > 0) {
            throw CustomException("当前分类下关联了套餐，无法删除")
        }


        removeById(id)
    }
}