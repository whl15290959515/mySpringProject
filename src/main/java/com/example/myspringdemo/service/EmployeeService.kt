package com.example.myspringdemo.service

import com.baomidou.mybatisplus.extension.service.IService
import com.example.myspringdemo.entity.Employee

interface EmployeeService : IService<Employee> {

    fun getByUser(userName: String):Employee

    fun getByUserId(id:Long):Employee

    fun updateByUser(employee: Employee)


    fun savaUser(employee: Employee)
}