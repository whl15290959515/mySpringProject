package com.example.myspringdemo.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.example.myspringdemo.entity.Employee
import org.apache.ibatis.annotations.Mapper

@Mapper
interface EmployeeMapper :BaseMapper<Employee> {

    fun getByUser(userName: String):Employee

    fun getByUserId(id:Long):Employee

    fun updateByUser(employee: Employee)

    fun savaUser(employee: Employee)

}