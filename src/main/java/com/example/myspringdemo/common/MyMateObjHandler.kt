package com.example.myspringdemo.common

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import lombok.extern.log4j.Log4j2
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
@Log4j2
class MyMateObjHandler: MetaObjectHandler{



 override fun insertFill(metaObject: MetaObject) {
  metaObject.setValue("createTime",LocalDateTime.now())
  metaObject.setValue("updateTime",LocalDateTime.now())
  metaObject.setValue("createUser",BaseContext.getCurrentId())
  metaObject.setValue("updateUser",BaseContext.getCurrentId())
 }

 override fun updateFill(metaObject: MetaObject) {
  metaObject.setValue("updateTime",LocalDateTime.now())
  metaObject.setValue("updateUser",BaseContext.getCurrentId())


 }
}