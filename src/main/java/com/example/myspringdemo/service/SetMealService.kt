package com.example.myspringdemo.service

import com.baomidou.mybatisplus.extension.service.IService
import com.example.myspringdemo.entity.SetMeal
import com.example.myspringdemo.entity.SetmealDto

interface SetMealService : IService<SetMeal> {

    fun savaSetMealInfo(setMealDto: SetmealDto)


    //删除or批量删除套餐
    fun deleteById(id:List<Long>)


    fun updateSetMealInfo(setMealDto: SetmealDto)

}