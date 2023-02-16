package com.example.myspringdemo.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import com.example.myspringdemo.entity.DishFlavor

@Mapper
interface DishFlavorMapper : BaseMapper<DishFlavor> {
}