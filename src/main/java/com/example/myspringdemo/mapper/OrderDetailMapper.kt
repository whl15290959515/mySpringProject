package com.example.myspringdemo.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import com.example.myspringdemo.entity.OrderDetail

@Mapper
interface OrderDetailMapper : BaseMapper<OrderDetail> {
}