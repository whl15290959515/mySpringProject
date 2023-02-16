package com.example.myspringdemo.controller

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.example.myspringdemo.common.BaseContext
import com.example.myspringdemo.common.R
import com.example.myspringdemo.entity.AddressBook
import com.example.myspringdemo.entity.OrderDetail
import com.example.myspringdemo.entity.OrderDto
import com.example.myspringdemo.entity.Orders
import com.example.myspringdemo.service.AddressBookService
import com.example.myspringdemo.service.OrderDetailService
import com.example.myspringdemo.service.OrdersService
import com.example.myspringdemo.utils.MySlf4j
import org.apache.commons.lang.StringUtils
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.function.Function
import java.util.stream.Collectors


@MySlf4j
@RestController
@RequestMapping("/order")
class OrderController {
    @Autowired
    val ordersService:OrdersService?=null

    @Autowired
    val addressBookService:AddressBookService?=null


    @Autowired
    val orderDetailService:OrderDetailService?=null

    @PostMapping("/submit")
    fun getOrders(@RequestBody orders: Orders):R<Orders>{


        ordersService?.submitOrders(orders)
      return  R.success(orders)
    }


    //订单明细
    @GetMapping("/page")
    fun page(page:Long, pageSize:Long, orderNumber:String?): R<Page<Orders>>{
        val pageInfo=Page<Orders>(page,pageSize)
        val lambdaQueryWrapper=LambdaQueryWrapper<Orders>()
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(orderNumber),Orders::getNumber,orderNumber)
        lambdaQueryWrapper.orderByDesc(Orders::getOrderTime)
        ordersService?.page(pageInfo,lambdaQueryWrapper)
        val records = pageInfo.records
        val collect = records.stream().map { item ->
            val ordersDto = Orders()
            BeanUtils.copyProperties(item, ordersDto)
            val queryWrapper = LambdaQueryWrapper<AddressBook>()
            queryWrapper.eq(AddressBook::getUserId, ordersDto.userId)
            val userInfo =addressBookService?.getOne(queryWrapper)
                ordersDto.userName = userInfo?.consignee
                ordersDto
        }.collect(Collectors.toList())
        pageInfo.records=collect
        return R.success(pageInfo)
    }


    //抽离的一个方法，通过订单id查询订单明细，得到一个订单明细的集合
    //这里抽离出来是为了避免在stream中遍历的时候直接使用构造条件来查询导致eq叠加，从而导致后面查询的数据都是null
    private fun getOrderDetailListByOrderId(orderId: Long): List<OrderDetail> {
        val queryWrapper = LambdaQueryWrapper<OrderDetail>()
        queryWrapper.eq(OrderDetail::getOrderId,orderId)

        return orderDetailService!!.list(queryWrapper)

    }

    /**
     * 用户端展示自己的订单分页查询
     * @param page
     * @param pageSize
     * @return
     * 遇到的坑：原来分页对象中的records集合存储的对象是分页泛型中的对象，里面有分页泛型对象的数据
     * 传过来了分页数据，其他所有的数据都要从本地线程存储的用户id开始查询，
     * 出现了一个用户id查询到 n个订单对象，然后又使用 n个订单对象又去查询 m 个订单明细对象，
     */
    @GetMapping("/userPage")
    fun page(page: Long, pageSize: Long): R<Page<OrderDto>> {
        val pageInfo = Page<Orders>(page, pageSize)
        val pageDto: Page<OrderDto> = Page<OrderDto>(page, pageSize)
        val queryWrapper = LambdaQueryWrapper<Orders>()
        queryWrapper.eq( Orders::getUserId,BaseContext.getCurrentId())
        queryWrapper.orderByDesc(Orders::getOrderTime)
        ordersService?.page(pageInfo, queryWrapper)
        val records = pageInfo.records
        val orderDtoList=records.stream().map { item ->
            val orderDto = OrderDto()
            val orderId = item.getId() //获取订单id
            val orderDetailList = getOrderDetailListByOrderId(orderId)
            BeanUtils.copyProperties(item, orderDto)
            orderDto.detailList= orderDetailList as List<OrderDetail>
            orderDto
        }.collect(Collectors.toList()) as MutableList<OrderDto>
        BeanUtils.copyProperties(pageInfo, pageDto, "records")
        pageDto.records= orderDtoList
        return R.success(pageDto)
    }
}