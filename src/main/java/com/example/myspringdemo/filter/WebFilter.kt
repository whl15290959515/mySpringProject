package com.example.myspringdemo.filter

import com.alibaba.fastjson.JSON
import com.example.myspringdemo.common.BaseContext
import com.example.myspringdemo.common.R
import com.example.myspringdemo.utils.MySlf4j.Companion.log
import lombok.extern.slf4j.Slf4j
import org.springframework.util.AntPathMatcher
import java.util.*
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashSet


@Slf4j
@WebFilter(filterName = "WebFilter",urlPatterns = ["/*"])
class WebFilter : Filter {
    //静态变量
    companion object  {
        @JvmStatic
        val  antPathMatcher=AntPathMatcher()
    }
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, p2: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse
        log.info("拦截到请求"+request.requestURI)

        //定义不需要处理的请求路径
        val urls = arrayOf(
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/common/**",
            "/user/sendMsg",
            "/user/login",
            "/doc.html"
        )
        val check = checkUri(urls, httpServletRequest.requestURI)
        if (check){
            p2!!.doFilter(httpServletRequest,httpServletResponse)
            return
        }
        if (httpServletRequest.session.getAttribute("employee")!=null){
            val attribute = httpServletRequest.session.getAttribute("employee") as Long
            BaseContext.setCurrentId(attribute)

            p2!!.doFilter(httpServletRequest,httpServletResponse)
            return
        }

        if (httpServletRequest.session.getAttribute("user")!=null){
            val userAttribute = httpServletRequest.session.getAttribute("user") as Long
            BaseContext.setCurrentId(userAttribute)

            p2!!.doFilter(httpServletRequest,httpServletResponse)
            return
        }
        httpServletResponse.writer.write(JSON.toJSONString(R.error<Any>("NOTLOGIN")))
    }
    private fun checkUri(url: Array<String>,uri: String ): Boolean {
       for (item in url){
           if (antPathMatcher.match(item, uri)){
               return true
           }
       }
        return false
    }



}