package com.example.myspringdemo.service.serviceImpl

import org.springframework.stereotype.Service
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.myspringdemo.entity.ShoppingCart
import com.example.myspringdemo.mapper.ShoppingCartMapper
import com.example.myspringdemo.service.ShoppingCartService
import com.example.myspringdemo.utils.MySlf4j

@MySlf4j
@Service
open class ShoppingCartImpl : ServiceImpl<ShoppingCartMapper, ShoppingCart>(), ShoppingCartService {
}