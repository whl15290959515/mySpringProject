package com.example.myspringdemo.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.example.myspringdemo.entity.SetMeal
import org.apache.ibatis.annotations.Mapper


@Mapper
interface SetMealMapper:BaseMapper<SetMeal> {
}