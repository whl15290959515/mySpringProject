package com.example.myspringdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.Scanner;


@SpringBootTest
class MySpringDemoApplicationTests {


    @Test
    public void main() {

        System.out.println("选择");
        System.out.println("哈哈  嘿嘿  嘎嘎  解解");
        String next="哈哈";
        String[] c={"嘎嘎","嘿嘿","哈哈","解解"};
        String s = RandomStr(c);
        System.out.println("random生成的结果为"+s);
        if (next.equals(s)){
            System.out.println("相同");
        }else {
            System.out.println(5);
        }
    }



    public static String RandomStr(String[] strs){
        int random_index = (int) (Math.random()*strs.length);
        return strs[random_index];
    }






}
