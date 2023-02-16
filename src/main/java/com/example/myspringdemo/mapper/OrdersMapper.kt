package com.example.myspringdemo.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.example.myspringdemo.entity.Orders
import org.apache.ibatis.annotations.Mapper

@Mapper
interface OrdersMapper : BaseMapper<Orders> {

}