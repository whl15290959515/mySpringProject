package com.example.myspringdemo.controller

import com.example.myspringdemo.common.R
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/common")
class CommonController  {
    @Value("\${testPath.path}")
    var path:String=""

    @PostMapping("/upload")
    fun upload(file:MultipartFile): R<String> {
        val originalFilename = file.originalFilename
        val suffix = originalFilename!!.substring(originalFilename.lastIndexOf("."))
        val fileName=UUID.randomUUID().toString()+suffix
        val dir= File(path)
        if (!dir.exists()){
            dir.mkdirs()
        }
        file.transferTo(File(path +fileName))
        return R.success(fileName)
    }


    @GetMapping("/download")
    fun downLoad(name:String?,httpServletResponse: HttpServletResponse){

        try {
            //输入流，通过输入流读取文件内容
            val fileInputStream = FileInputStream(File(path + name))

            //输出流，通过输出流将文件写回浏览器
            val outputStream: ServletOutputStream = httpServletResponse.outputStream
            httpServletResponse.contentType = "image/jpeg"
            var len = 0
            val bytes = ByteArray(1024)
            while (fileInputStream.read(bytes).also { len = it } != -1) {
                outputStream.write(bytes, 0, len)
                outputStream.flush()
            }

            //关闭资源
            outputStream.close()
            fileInputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}