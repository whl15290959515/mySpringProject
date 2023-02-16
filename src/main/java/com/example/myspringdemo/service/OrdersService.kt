package com.example.myspringdemo.service

import com.baomidou.mybatisplus.extension.service.IService
import com.example.myspringdemo.entity.Orders

interface OrdersService : IService<Orders> {
    fun submitOrders(orders: Orders)

}