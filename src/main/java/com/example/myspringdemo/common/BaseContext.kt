package com.example.myspringdemo.common


//全局获取用户id
class BaseContext {
    companion object{
        var threadLocal = ThreadLocal<Long>()

        fun setCurrentId(long: Long){
            threadLocal.set(long)
        }

        fun getCurrentId():Long{
          return  threadLocal.get()
        }

    }


}