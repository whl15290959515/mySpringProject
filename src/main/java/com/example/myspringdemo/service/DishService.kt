package com.example.myspringdemo.service

import com.baomidou.mybatisplus.extension.service.IService
import com.example.myspringdemo.entity.Dish
import com.example.myspringdemo.entity.DishDto

interface DishService : IService<Dish> {

    fun savaFlavor(dishDto: DishDto)

    fun getByIdWithFlavor(id:Long):DishDto

    fun updateFlavor(dishDto: DishDto)
}