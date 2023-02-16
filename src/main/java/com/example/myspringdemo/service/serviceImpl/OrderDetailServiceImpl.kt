package com.example.myspringdemo.service.serviceImpl

import org.springframework.stereotype.Service
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.myspringdemo.entity.OrderDetail
import com.example.myspringdemo.mapper.OrderDetailMapper
import com.example.myspringdemo.service.OrderDetailService
import com.example.myspringdemo.utils.MySlf4j

@MySlf4j
@Service
open class OrderDetailServiceImpl : ServiceImpl<OrderDetailMapper, OrderDetail>(), OrderDetailService {
}