package com.example.myspringdemo.controller

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import com.example.myspringdemo.common.BaseContext
import com.example.myspringdemo.common.R
import com.example.myspringdemo.entity.ShoppingCart
import com.example.myspringdemo.service.ShoppingCartService
import com.example.myspringdemo.utils.MySlf4j
import com.example.myspringdemo.utils.MySlf4j.Companion.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@MySlf4j
@RestController
@RequestMapping("/shoppingCart")
class ShoppingCartController {
    @Autowired
    val shoppingCartService: ShoppingCartService? =null


    //添加菜品或者套餐
    @PostMapping("/add")
    fun  add(@RequestBody shoppingCart: ShoppingCart): R<ShoppingCart> {
        val currentId = BaseContext.getCurrentId()
        shoppingCart.userId=currentId
        //dishId存在时，添加数据为菜品， setMealId不为空时添加数据为套餐
        val dishId = shoppingCart.dishId
        val lambdaQueryWrapper=LambdaQueryWrapper<ShoppingCart>()
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,shoppingCart.userId)

        if (dishId!=null){
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.dishId)
        }else{
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.setmealId)
        }
        var shoppingCartDataInfo= shoppingCartService?.getOne(lambdaQueryWrapper)//添加到购物车中的数据
        if (shoppingCartDataInfo!=null){
            //不为空证明已存在相同菜品，number+1
            var number = shoppingCartDataInfo.number
            shoppingCartDataInfo.number=number+1
            shoppingCartService?.updateById(shoppingCartDataInfo)
        }else{
            /**
             *  不存在相同菜品，把传入的实体保存到数据库
             * p1:添加菜品时，如果是多人协作，需要先判断当前菜品，或者当前套餐是否在售卖列表中
             * p2:如果在列表中，判断菜品当前的状态是否是停售
             * p3:单个套餐最多智能点99份，防止恶意点餐
             * p4:是否使用了优惠券，使用优惠券成功后核销优惠券
             */
            shoppingCart.number=1
            shoppingCart.createTime= LocalDateTime.now()
            shoppingCartService?.save(shoppingCart)
            shoppingCartDataInfo=shoppingCart
        }
        return R.success(shoppingCartDataInfo)
    }

    /**
     * 减少菜品或套餐
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    fun sub(@RequestBody shoppingCart: ShoppingCart): R<ShoppingCart> {
        val currentId = BaseContext.getCurrentId()
        shoppingCart.userId = currentId
        //1.区分减少的是菜品还是套餐
        val queryWrapper = LambdaQueryWrapper<ShoppingCart>()
        queryWrapper.eq(ShoppingCart::getUserId, currentId)
        val dishId = shoppingCart.dishId
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId,dishId)
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.setmealId)
        }
        val shopping = shoppingCartService?.getOne(queryWrapper)
        if (shopping!=null) {
            if (shopping.number>1){
                shopping.number = shopping.number - 1
                shoppingCartService?.updateById(shopping)
            }else  {
                shopping.number = shopping.number - 1
                shoppingCartService?.removeById(shopping)
            }
        }
        return R.success(shopping)
    }

    //查询当前用户下的购物车集合
    @GetMapping("/list")
    fun list():R<List<ShoppingCart>>{
    val lambdaQueryWrapper=LambdaQueryWrapper<ShoppingCart>()
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime)
        val list = shoppingCartService?.list(lambdaQueryWrapper)

        return R.success(list)
    }


    //清空购物车
    @DeleteMapping("/clean")
    fun cleanShoppingCart():R<String>{
        val lambdaQueryWrapper=LambdaQueryWrapper<ShoppingCart>()
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
        shoppingCartService?.remove(lambdaQueryWrapper)
        return R.success("清空购物车成功")
    }
}