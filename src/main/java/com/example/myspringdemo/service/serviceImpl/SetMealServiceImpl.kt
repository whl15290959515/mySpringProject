package com.example.myspringdemo.service.serviceImpl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.myspringdemo.common.CustomException
import com.example.myspringdemo.entity.Dish
import com.example.myspringdemo.entity.SetMeal
import com.example.myspringdemo.entity.SetMealDish
import com.example.myspringdemo.entity.SetmealDto
import com.example.myspringdemo.mapper.SetMealMapper
import com.example.myspringdemo.service.SetMealDishService
import com.example.myspringdemo.service.SetMealService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors


@Service
open class SetMealServiceImpl : ServiceImpl<SetMealMapper, SetMeal>(), SetMealService {


    @Autowired
    val setMealDishService: SetMealDishService?=null

    @Transactional//事务注解 保证数据成功失败的状态
    override fun savaSetMealInfo(setMealDto: SetmealDto) {
        save(setMealDto)

        val setMealDishes = setMealDto.setmealDishes

        setMealDishes.stream().map { item->
            item.setmealId= setMealDto.id
            item
        }.collect(Collectors.toList())
        setMealDishService?.saveBatch(setMealDishes)
    }


    @Transactional
    override fun deleteById(id: List<Long>) {
        val lambdaQueryWrapper=LambdaQueryWrapper<SetMeal>()
        lambdaQueryWrapper.`in`(SetMeal::getId,id)
        lambdaQueryWrapper.eq(SetMeal::getIsDeleted,1)
        if (count(lambdaQueryWrapper)>0){
            throw CustomException("不能删除正在售卖的套餐，请先停售该套餐")
        }
        removeByIds(id)
        val lambdaWrapper=LambdaQueryWrapper<SetMealDish>()
        lambdaWrapper.`in`(SetMealDish::getSetmealId,id)
        setMealDishService?.remove(lambdaWrapper)
    }


    @Transactional
    override fun updateSetMealInfo(setMealDto: SetmealDto) {
        val setMealDishes = setMealDto.setmealDishes
        setMealDishes.stream().map { item->
            item.setmealId= setMealDto.id
            item
        }.collect(Collectors.toList())
        setMealDishService?.updateBatchById(setMealDishes)
    }
}