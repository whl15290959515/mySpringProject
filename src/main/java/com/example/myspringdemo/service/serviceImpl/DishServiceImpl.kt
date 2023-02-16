package com.example.myspringdemo.service.serviceImpl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.myspringdemo.entity.Dish
import com.example.myspringdemo.entity.DishDto
import com.example.myspringdemo.entity.DishFlavor
import com.example.myspringdemo.mapper.DishMapper
import com.example.myspringdemo.service.DishFlavorService
import com.example.myspringdemo.service.DishService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Function
import java.util.stream.Collectors

@Service
open class DishServiceImpl : ServiceImpl<DishMapper,Dish>(),DishService {


    @Autowired
   private val dishFlavorService: DishFlavorService?=null

    @Transactional
    override fun savaFlavor(dishDto: DishDto) {
        save(dishDto)
        val id = dishDto.id
        var flavors = dishDto.flavors

        flavors = flavors.stream().map(Function<DishFlavor, Any> { item: DishFlavor ->
            item.dishId = id
            item
        }).collect(Collectors.toList()) as List<DishFlavor>
        dishFlavorService?.saveBatch(flavors)


    }


    override fun getByIdWithFlavor(id: Long): DishDto {

        //查询菜品基本信息，从dish表查询
        val dish = getById(id)

        val dishDto = DishDto()
        BeanUtils.copyProperties(dish, dishDto)
        //查询当前菜品对应的口味信息，从dish_flavor表查询
        val queryWrapper = LambdaQueryWrapper<DishFlavor>()
        queryWrapper.eq(DishFlavor::getDishId, dish.id)
        val myFlavors = dishFlavorService!!.list(queryWrapper)
        dishDto.flavors=myFlavors
        return dishDto
    }

    @Transactional
    override fun updateFlavor(dishDto: DishDto) {
        //更新dish表基本信息
        updateById(dishDto)
        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        val queryWrapper: LambdaQueryWrapper<DishFlavor> = LambdaQueryWrapper<DishFlavor>()
        queryWrapper.eq(DishFlavor::getDishId, dishDto.id)
        dishFlavorService!!.remove(queryWrapper)
        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        var flavors: List<DishFlavor> = dishDto.flavors
        flavors = flavors.stream().map<DishFlavor> { item: DishFlavor ->
            item.setDishId(dishDto.id)
            item
        }.collect(Collectors.toList<DishFlavor>())

        dishFlavorService.saveBatch(flavors)

    }

}