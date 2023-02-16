package com.example.myspringdemo.service.serviceImpl

import org.springframework.stereotype.Service
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.myspringdemo.entity.SetMeal
import com.example.myspringdemo.entity.SetMealDish
import com.example.myspringdemo.mapper.SetMealDishMapper
import com.example.myspringdemo.service.SetMealDishService

@Service
open class SetMealDishServiceImpl : ServiceImpl<SetMealDishMapper, SetMealDish>(), SetMealDishService {
}