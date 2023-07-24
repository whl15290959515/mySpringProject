package com.example.myspringdemo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.myspringdemo.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
@ServletComponentScan //过滤器注解
@EnableTransactionManagement
@MapperScan(basePackages="com.example.myspringdemo.mapper")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
public class MySpringDemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(MySpringDemoApplication.class, args);
        log.info("启动成功");

//        try {
//            Runtime.getRuntime().exec("cmd /c start http://localhost:8086/pages/login.html");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // String driverName="oracle.jdbc.driver.OracleDriver";//加载驱动
        // String dbURL="jdbc:oracle:thin:@127.0.0.1:1521:ORCL";//localhost代表本机，也可以是 127.0.0.1，可以填写具体IP

        String driverName = "com.mysql.cj.jdbc.Driver";//加载驱动
        String dbURL = "jdbc:mysql://localhost:3306/db_test";//localhost代表本机，也可以是 127.0.0.1，可以填写具体IP

        // String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";//这是要连接的数据库加载器
        // String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=Library";//这是要连接的端口号以及数据库名称
        String userName = "root";//用户名
        String userPassword = "whl123456";//用户密码
        try {
            Class.forName(driverName);
            System.out.println("加载驱动成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("加载驱动失败");
        }
        try {
            Connection dbConn = DriverManager.getConnection(dbURL, userName,userPassword);
            System.out.println(dbConn+"连接数据库成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库连接失败");

        }

    }
}
