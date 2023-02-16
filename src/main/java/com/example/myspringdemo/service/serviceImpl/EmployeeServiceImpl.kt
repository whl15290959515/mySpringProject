package com.example.myspringdemo.service.serviceImpl

import com.example.myspringdemo.entity.Employee
import com.example.myspringdemo.mapper.EmployeeMapper
import com.example.myspringdemo.service.EmployeeService
import org.springframework.stereotype.Service
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired



@Service
 open class EmployeeServiceImpl:ServiceImpl<EmployeeMapper,Employee>(), EmployeeService {


  @Autowired
  val employeeMapper:EmployeeMapper?=null

 override fun getByUser(userName: String): Employee {

  return employeeMapper!!.getByUser(userName)

 }

 override fun getByUserId(id: Long): Employee {
  return employeeMapper!!.getByUserId(id)

 }

 override fun updateByUser(employee: Employee) {

    employeeMapper?.updateByUser(employee)
 }



 override fun savaUser(employee: Employee) {

  employeeMapper?.savaUser(employee)
 }


}