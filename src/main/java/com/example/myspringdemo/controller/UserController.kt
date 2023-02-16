package com.example.myspringdemo.controller

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.example.myspringdemo.common.R
import com.example.myspringdemo.entity.User
import com.example.myspringdemo.service.UserService
import com.example.myspringdemo.utils.MailUtils
import com.example.myspringdemo.utils.MySlf4j
import com.example.myspringdemo.utils.MySlf4j.Companion.log
import com.example.myspringdemo.utils.ValidateCodeUtils
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpSession

@MySlf4j
@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    val userService:UserService?=null
    @PostMapping("/sendMsg")
    fun sendMessage(@RequestBody user: User,httpSession: HttpSession):R<String> {
        if (StringUtils.isNotEmpty(user.phone)){
            val code = ValidateCodeUtils.generateValidateCode(4).toString()
            log.info("登陆验证码为${code}")
            MailUtils.sendMail("1207139268@qq.com","验证码邮件",code)
            httpSession.setAttribute(user.phone,code)

            return R.success("验证码为$code")
        }
        return R.error("验证码发送失败")
    }

    @PostMapping("/login")
    fun login(@RequestBody userMap: Map<String,String>,httpSession: HttpSession):R<User> {
        val  phone= userMap["phone"].toString()
        val code = userMap["code"].toString()
        val lambdaQueryWrapper= LambdaQueryWrapper<User>()
        lambdaQueryWrapper.eq(User::getPhone,phone)
        val one = userService?.getOne(lambdaQueryWrapper)
        val attribute = httpSession.getAttribute(phone)
        if (attribute!=null&&attribute.equals(code)){
            if (one==null){
                val userInfo=User()
                userInfo.phone=phone
                userInfo.status=1//0禁用1正常
                userService?.save(userInfo)
            }
            httpSession.setAttribute("user", one?.getId())
            return R.success(one)
        }
        return R.error("验证码错误")
    }
}