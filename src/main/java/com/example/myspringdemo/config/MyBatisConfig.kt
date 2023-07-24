package com.example.myspringdemo.config

import com.example.myspringdemo.common.JacksonObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport


@Configuration
open class MyBatisConfig : WebMvcConfigurationSupport() {


    @Override
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/")
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/")
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
        registry.addResourceHandler("/mapper/**").addResourceLocations("classpath*:mapper/*.xml")
    }



    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {

        val messageConverter = MappingJackson2HttpMessageConverter()
        messageConverter.objectMapper = JacksonObjectMapper()
        converters.add(0, messageConverter)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // 所有接口
            .allowCredentials(true) // 是否发送 Cookie
            .allowedOriginPatterns("*") // 支持域
            .allowedMethods(*arrayOf("GET", "POST", "PUT", "DELETE")) // 支持方法
            .allowedHeaders("*")
            .exposedHeaders("*")
    }
}