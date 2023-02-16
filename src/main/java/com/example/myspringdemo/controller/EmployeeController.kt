package com.example.myspringdemo.controller

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.example.myspringdemo.common.R
import com.example.myspringdemo.entity.Employee
import com.example.myspringdemo.mapper.EmployeeMapper
import com.example.myspringdemo.service.EmployeeService
import lombok.extern.log4j.Log4j2
import org.apache.commons.lang.StringUtils
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.DigestUtils
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest


@Log4j2
@RestController
@RequestMapping("/employee")
open class EmployeeController {

    @Autowired
     val employeeService: EmployeeService? = null
    @PostMapping("/login")
    fun login(httpServletRequest: HttpServletRequest, @RequestBody employee: Employee): R<Employee>? {

        var password=employee.password
            password=DigestUtils.md5DigestAsHex(password.toByteArray())
        val emp = employeeService!!.getByUser(employee.username)
        if (emp.password != password) return  R.error("请核对密码是否正确")
        if (emp.status==0){return R.error("账号已禁用") }
        httpServletRequest.session.setAttribute("employee",emp.id)
        return R.success(emp)
}
    @PostMapping("/logout")
    fun logout(httpServletRequest: HttpServletRequest):R<String>?{
        httpServletRequest.session.removeAttribute("employee")
        return R.success("退出成功")
    }


    //添加用户
    @PostMapping
    fun save(httpServletRequest: HttpServletRequest,@RequestBody employee: Employee): R<String>? {
        employee.password=DigestUtils.md5DigestAsHex("123456".toByteArray())
        employee.createTime= LocalDateTime.now()
        employee.updateTime= LocalDateTime.now()
        val attribute = httpServletRequest.session.getAttribute("employee")
        employee.createUser= attribute as Long
        employee.updateUser= attribute
        if (attribute!=1L){
            return R.success("无权限新增用户，请联系管理员")
        }
        employeeService!!.savaUser(employee)
        return R.success("新增用户成功")
    }

    /*
    * 用户列表分页
    * */
    @GetMapping("/page")
    fun page(page:Long,pageSize:Long,name:String?): R<Page<Employee>>{
        val pageInfo = Page<Employee>(page, pageSize)
        val lambdaQueryWrapper=LambdaQueryWrapper<Employee>()
        lambdaQueryWrapper
            .like(StringUtils.isNotEmpty(name),Employee::getUsername,name).or()
            .like(StringUtils.isNotEmpty(name),Employee::getName,name).or()
            .like(StringUtils.isNotEmpty(name),Employee::getPhone,name)
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime)
        employeeService!!.page(pageInfo,lambdaQueryWrapper)
        return R.success(pageInfo)
    }

    //禁用员工账号
    @PutMapping
    fun update(httpServletRequest: HttpServletRequest,@RequestBody employee: Employee):R<String>{
        val attribute = httpServletRequest.session.getAttribute("employee")
        if (employee.id==1L){
            return R.error("无法禁用管理员账号")
        }
        employee.updateTime= LocalDateTime.now()
        employee.updateUser= attribute as Long?
        employeeService?.updateByUser(employee)
        return R.success("修改成功")
    }


    // 修改页面反查详情接口
    @GetMapping("/{id}")
    fun getById(@PathVariable id:Long):R<Employee>{

        val byId = employeeService?.getByUserId(id)

        return if (byId!=null){ R.success(byId) }else {R.error("修改失败")}
    }


}


