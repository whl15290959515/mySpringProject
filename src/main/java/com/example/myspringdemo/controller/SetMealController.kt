package com.example.myspringdemo.controller

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.example.myspringdemo.common.R
import com.example.myspringdemo.entity.SetMeal
import com.example.myspringdemo.entity.SetmealDto
import com.example.myspringdemo.service.CategoryService
import com.example.myspringdemo.service.SetMealDishService
import com.example.myspringdemo.service.SetMealService
import com.example.myspringdemo.utils.MySlf4j
import com.example.myspringdemo.utils.MySlf4j.Companion.log
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors


@MySlf4j
@RestController
@RequestMapping("/setmeal")
class SetMealController {

    @Autowired
    val setMealService:SetMealService?=null
    @Autowired
    val categoryService:CategoryService?=null


    @Autowired
    val setMealDishService:SetMealDishService?=null
    @PostMapping
    fun sava(@RequestBody setMealDto: SetmealDto):R<String> {
        setMealService?.savaSetMealInfo(setMealDto)
        return R.success("添加成功")
    }

    @DeleteMapping
    fun delete(@RequestParam id:List<Long>):R<String>{
        setMealService?.deleteById(id)
        return R.success("删除成功")
    }

    @GetMapping("/list")
    fun list( setMeal: SetMeal):R<List<SetMeal>> {
        val lambdaQueryWrapper=LambdaQueryWrapper<SetMeal>()
        lambdaQueryWrapper.eq(setMeal.categoryId!=null,SetMeal::getCategoryId,setMeal.categoryId)
        lambdaQueryWrapper.eq(setMeal.status!=null,SetMeal::getStatus,setMeal.status)
        lambdaQueryWrapper.orderByDesc(SetMeal::getUpdateTime)
        val list = setMealService?.list(lambdaQueryWrapper)
       return R.success(list)
    }

    @GetMapping("/page")
    fun page(page:Long,pageSize:Long,searchName:String?):R<Page<SetmealDto>>{
        //分页构造器对象
        val pageInfo= Page<SetMeal>(page,pageSize)
        val dtoPage = Page<SetmealDto>()
        val queryWrapper: LambdaQueryWrapper<SetMeal> = LambdaQueryWrapper<SetMeal>()
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(searchName!=null, SetMeal::getName, searchName)
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(SetMeal::getUpdateTime)
        setMealService?.page(pageInfo, queryWrapper)
        //对象拷贝
        //org.springframework.beans.BeanUtils
        //BeanUtils.copyProperties（a,b） 当前包中该方法为a拷贝到b
        BeanUtils.copyProperties(pageInfo, dtoPage, "records")
        val records= pageInfo.records
        val list = records.stream().map { item: SetMeal ->
            val setMealDto = SetmealDto()
            //对象拷贝
            BeanUtils.copyProperties(item, setMealDto)
            //根据分类id查询分类对象
            val category = categoryService!!.getById(item.categoryId)
            if (category != null) { //分类名称
                setMealDto.categoryName = category.name }
            setMealDto
        }.collect(Collectors.toList())
        dtoPage.records = list
        return R.success(dtoPage)
    }



    /**
     * 修改套餐售卖状态
     * @param status
     * @param ids
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "套餐售卖状态")
    @ApiImplicitParam(name = "status",value = "套餐当前状态", required = true)
    fun statusWithIds(@PathVariable("status") status: Int?, @RequestParam("ids") ids: List<Long?>): R<String>? {
        log.info("套餐售卖状态：{},ids:{}", status, ids)
        //查询当前菜品是否包含在已售套餐内
        val dish = SetMeal()
        //如果不在套餐内出售
        for (dishId in ids) {
            dish.id = dishId
            dish.status=status
            setMealService?.updateById(dish)
        }
        return R.success("修改套餐售卖状态成功")
    }


    @GetMapping("/{id}")
    fun updateByIdInfo(@PathVariable id:Long):R<SetmealDto>{
        val byId = setMealService?.getById(id)

        log.info(""+byId)
        val dto= SetmealDto()



//        setMealService?.querySetMealInfo()
        return R.success(dto)
    }

}