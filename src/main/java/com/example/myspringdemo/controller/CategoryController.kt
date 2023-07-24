package com.example.myspringdemo.controller

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.example.myspringdemo.common.R
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import lombok.extern.log4j.Log4j2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import com.example.myspringdemo.entity.Category
import com.example.myspringdemo.service.CategoryService



/*
*分类管理
* */
@Log4j2
@RestController
@RequestMapping("/category")
class CategoryController {

    @Autowired
    val categoryService: CategoryService? =null



    /*
    *新增菜品分类
    * */
    @PostMapping
    fun save(@RequestBody category:Category): R<String>{

        categoryService!!.save(category)
        return R.success("添加成功")
    }


    @GetMapping("/page")
    fun page(page: Long,pageSize:Long) :R<Page<Category>>{
        val pageInfo=Page<Category>(page,pageSize)
        val lambdaQueryWrapper=LambdaQueryWrapper<Category>()
        lambdaQueryWrapper.orderByAsc(Category::getSort)
        categoryService!!.page(pageInfo,lambdaQueryWrapper)
        return R.success(pageInfo)
    }

    @DeleteMapping
    fun  deleteById(@RequestBody category: Category):R<String>{
        categoryService?.remove(category.id)
        return R.success("删除成功")
    }

    @PutMapping
    fun delete(@RequestBody category: Category):R<String>{
        categoryService?.updateById(category)
        return R.success("修改成功")
    }

    @GetMapping("/list")
    fun query(category: Category):R<List<Category>>{
        val  lambdaQueryWrapper=LambdaQueryWrapper<Category>()
        lambdaQueryWrapper.eq(category.type!=null,Category::getType,category.type)
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime)
        val list = categoryService?.list(lambdaQueryWrapper)

        return R.success(list)
    }
}