package com.example.myspringdemo.service.serviceImpl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.toolkit.IdWorker
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.myspringdemo.common.BaseContext
import com.example.myspringdemo.entity.OrderDetail
import com.example.myspringdemo.entity.Orders
import com.example.myspringdemo.entity.ShoppingCart
import com.example.myspringdemo.mapper.OrdersMapper
import com.example.myspringdemo.service.*
import com.example.myspringdemo.utils.MySlf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Function
import java.util.stream.Collectors

@MySlf4j
@Service
open class OrdersServiceImpl : ServiceImpl<OrdersMapper, Orders>(), OrdersService {


    @Autowired
    val shoppingCartService:ShoppingCartService?=null

    @Autowired
    val orderDetailService:OrderDetailService?=null

    @Autowired
    val userService:UserService?=null

    @Autowired
    val addressBookService:AddressBookService?=null

    @Transactional
    override fun submitOrders(orders: Orders) {
        //拿到当前用户id
        val currentId = BaseContext.getCurrentId()
        //结算时判断是否因为禁售，使购物车列表发生变化，如果含有禁售商品，结束本次订单提交并提醒用户
        //查询当前用户购物车的数据，如果有优惠券，是否使用了优惠券
        val lambdaQueryWrapper=LambdaQueryWrapper<ShoppingCart>()
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,currentId)
        val list = shoppingCartService?.list(lambdaQueryWrapper)//当前购物车的数据
        //查询用户数据
        val userInfo = userService?.getById(currentId)
        //查询地址数据
        val addressInfo = addressBookService?.getById(orders.addressBookId)
        //生成订单明细
        val amount = AtomicInteger(0)
        val orderId = IdWorker.getId() //订单号
        val orderDetails = list!!.stream().map{ item: ShoppingCart ->
                val orderDetail = OrderDetail()
                orderDetail.orderId = orderId
                orderDetail.number = item.number
                orderDetail.dishFlavor = item.dishFlavor
                orderDetail.dishId = item.dishId
                orderDetail.setmealId = item.setmealId
                orderDetail.name = item.name
                orderDetail.image = item.image
                orderDetail.amount = item.amount
                amount.addAndGet(item.amount.multiply(BigDecimal(item.number)).intValueExact())
                orderDetail
            }.collect(Collectors.toList())


        orders.id = orderId
        orders.orderTime = LocalDateTime.now()
        orders.checkoutTime = LocalDateTime.now()
        orders.status = 2
        orders.amount = BigDecimal(amount.get()) //总金额
        orders.userId = currentId
        orders.number = orderId.toString()
        orders.userName = userInfo?.name
        orders.consignee = addressInfo?.consignee
        orders.phone = addressInfo?.phone
        orders.address = ((if (addressInfo?.provinceName == null) "" else addressInfo.provinceName)
                + (if (addressInfo?.cityName == null) "" else addressInfo.cityName)
                + (if (addressInfo?.districtName == null) "" else addressInfo.districtName)
                + if (addressInfo?.detail == null) "" else addressInfo.detail)
        //生成订单一条
        save(orders)
        orderDetailService?.saveBatch(orderDetails)
        //如果一切正常提交订单信息并清空购物车
        shoppingCartService?.remove(lambdaQueryWrapper)


    }
}