package com.example.myspringdemo.service.serviceImpl

import org.springframework.stereotype.Service
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.myspringdemo.entity.DishFlavor
import com.example.myspringdemo.mapper.DishFlavorMapper
import com.example.myspringdemo.service.DishFlavorService

@Service
open class DishFlavorImpl : ServiceImpl<DishFlavorMapper, DishFlavor>(), DishFlavorService {
}