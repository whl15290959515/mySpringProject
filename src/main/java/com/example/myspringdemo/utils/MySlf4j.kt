package com.example.myspringdemo.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.annotation.ElementType





@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MySlf4j {
    companion object{
        val <reified T> T.log: Logger
            inline get() = LoggerFactory.getLogger(T::class.java)
    }
}
