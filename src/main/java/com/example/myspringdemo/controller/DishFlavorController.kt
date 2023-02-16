package com.example.myspringdemo.controller

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.example.myspringdemo.common.R
import com.example.myspringdemo.entity.Category
import com.example.myspringdemo.entity.Dish
import com.example.myspringdemo.entity.DishDto
import com.example.myspringdemo.entity.DishFlavor
import com.example.myspringdemo.entity.SetMeal
import com.example.myspringdemo.entity.SetMealDish
import com.example.myspringdemo.service.*
import com.example.myspringdemo.utils.MySlf4j.Companion.log
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import lombok.extern.log4j.Log4j2
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@Api(tags = ["测试测试"])
@Log4j2
@RestController
@RequestMapping("/dish")
class DishFlavorController {
    @Autowired
    val dishService:DishService?=null


    @Autowired
    val dishFlavorService:DishFlavorService?=null

    @Autowired
    val categoryService:CategoryService?=null

    @Autowired
    val setMealDishService: SetMealDishService?=null


    @Autowired
    val setMealService:SetMealService?=null
    @PostMapping
    fun sava(@RequestBody dish: DishDto):R<String>{
        dishService?.savaFlavor(dish)
        return R.success("添加成功")
    }


    /**
     * 修改菜品售卖状态
     * @param status
     * @param ids
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "菜品售卖状态")
    @ApiImplicitParam(name = "status",value = "当前状态", required = true)
    fun statusWithIds(@PathVariable("status") status: Int?, @RequestParam("ids") ids: List<Long>): R<String>? {
        log.info("售卖状态：{},ids:{}", status, ids)
        //查询当前菜品是否包含在已售套餐内
        if (fetchIds(ids)) return R.error("该菜品正在套餐中售卖")
            val dish = Dish()
            for (dishId in ids) {
                dish.id = dishId
                dish.status=status
                dishService?.updateById(dish)
            }
            return R.success("修改售卖状态成功")
    }


    @DeleteMapping
    fun deleteById(@RequestParam("ids") ids:List<Long>):R<String>{
        if (fetchIds(ids)) return R.error("该菜品正在套餐中售卖")
        val dish = Dish()
        for (dishId in ids) {
            dish.id = dishId
            dish.isDeleted=0
            dishService?.updateById(dish)
        }
        return R.success("菜品已删除")
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    operator fun get(@PathVariable id: Long?): R<DishDto?>? {
        val dishDto = dishService!!.getByIdWithFlavor(id!!)
        return R.success(dishDto)
    }

    @PutMapping
    fun update(@RequestBody dish: DishDto):R<String>{

        dishService?.updateFlavor(dish)
        return R.success("修改菜品信息成功")
    }


    //查询菜品数据
    @GetMapping("/list")
    fun queryCategory(dish:Dish):R<List<DishDto>>{


    val lambdaQueryWrapper= LambdaQueryWrapper<Dish>()
        lambdaQueryWrapper.eq(dish.categoryId!=null,Dish::getCategoryId,dish.categoryId)
        lambdaQueryWrapper.eq(Dish::getStatus,1)
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime)
        val dishList = dishService!!.list(lambdaQueryWrapper)

    val collect = dishList.stream().map{ item->

            val dishDto = DishDto()
            BeanUtils.copyProperties(item, dishDto)
            val categoryId = item.categoryId //分类id
            //根据id查询分类对象
            val category: Category = categoryService!!.getById(categoryId)
            dishDto.categoryName = category.name
            val dishId = item.id
            val dishQueryWrapper= LambdaQueryWrapper<DishFlavor>()
            dishQueryWrapper.eq(DishFlavor::getDishId, dishId)
            val dishFlavor= dishFlavorService?.list(dishQueryWrapper)
            dishDto.flavors = dishFlavor!!
            dishDto

    }.collect(Collectors.toList())
    return R.success(collect)
    }



    @GetMapping("/page")
    fun page(page:Long,pageSize:Long,name:String?) : R<Page<DishDto>>? {
        val pageInfo = Page<Dish>(page, pageSize)
        val dishDtoPage = Page<DishDto>()
        val queryWrapper = LambdaQueryWrapper<Dish>()
        queryWrapper.like(name != null, Dish::getName, name)
        queryWrapper.orderByDesc(Dish::getUpdateTime)
        dishService!!.page(pageInfo, queryWrapper)
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records")
        val records = pageInfo.records
        val list = records.stream().map { item ->
            val dishDto = DishDto()
            BeanUtils.copyProperties(item, dishDto)
            val categoryId = item.categoryId
            val category = categoryService!!.getById(categoryId)
            dishDto.categoryName =category.name
            dishDto
        }.collect(Collectors.toList())
        dishDtoPage.records= list
        return R.success(dishDtoPage)
    }

    fun fetchIds(ids:List<Long>):Boolean{
        val lambdaQueryWrapper =LambdaQueryWrapper<SetMealDish>()
        val one = setMealDishService?.list(lambdaQueryWrapper)
        if (one!=null){
            for (dishIds in ids){
                for (dishData in one){
                    if (dishData.dishId==dishIds){
                        val byId = setMealService?.getById(dishData.setmealId)
                        if (byId?.status==1){
                            return true
                        }else{
                            break
                        }
                    }
                }
            }
        }
        return false
    }
}