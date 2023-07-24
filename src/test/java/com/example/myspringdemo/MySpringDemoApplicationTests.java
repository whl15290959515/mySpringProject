package com.example.myspringdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;


@SpringBootTest
class MySpringDemoApplicationTests {
 private final List<String> list1 = new ArrayList<>();

    @Test
//    public void main() throws IOException{
//
////        小明写了一段代码，想实现替换文件部分内容的目标，但是代码似乎写得有些问题，请你纠正并优化八戒的代码
////        （请不要在原来的代码上做修改），并给出你的优化逻辑，优化点多多益善。
//        File file = new File("some file path");
//        List list1 = new ArrayList();
//        FileInputStream fileInputStream = new FileInputStream(file);
//        byte[] bytes = new byte[1024];
//        int i = fileInputStream.read(bytes);
//        if(i > 0) {
//            do {
//                i = fileInputStream.read(bytes);
//                String s = new String(bytes, 0 , i);
//                list1.add(s);
//            } while(i != -1);
//        }
//        i = fileInputStream.read(bytes);
//        String start = new String(bytes, 0, i);
//        list1.add(start);
//        for (int i1 = 0; i1 < list1.size(); i1++) {
//            String s = (String) list1.get(i1);
//            if(s.contains("1")) {
//                s = s.replace("1", "a");
//            } else if(s.contains("2")) {
//                s = s.replace("2", "b");
//            } else if(s.contains("3")) {
//                s = s.replace("3", "c");
//            } else if(s.contains("4")) {
//                s = s.replace("4", "d");
//            }
//            list1.remove(i1);
//            list1.add(i1, s);
//        }
//        FileOutputStream fos = new FileOutputStream(file);
//        for (Object o : list1) {
//            fos.write(o.toString().getBytes());
//        }
//test();
//    }
//

    //我写的
    public void main() throws IOException{
        int sums= 'a'+'b';
        System.out.println(sums);
            String dataStr = "2023-04-19 00:05";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            try {
                Date ckDate = sdf.parse(dataStr);
                System.out.println("原始时间为：" + dataStr);
                System.out.println("转换后时间为：" + getTime(ckDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        Map <String, int[]> m1=new HashMap<String,int[]>();
        String key1="Lucy";
        int[] m2={100,10,1};
        m1.put(key1, m2);
        System.out.println("================="+m1.get(key1));


        Integer[] num = {1, 2, 3, 4, 5, 6};
        Integer[] evens = Stream.of(num).filter(n -> n%2 == 0).toArray(Integer[]::new);
        System.out.println("================="+evens);


        int[] numbers = {1, 2, 3, 4, 5, 6};

        int sum = Arrays.stream(numbers).reduce(0, (a, b) -> a + b);

        System.out.println("sum : " + sum);


        Arrays.sort(numbers);

        File file = new File("/Users/aaronchen/Workspace/javaDemo/xiaoming.txt");
        if (!file.exists()){
            return;
        }
        List<String> list = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            byte[] bytes = new byte[1024];
            int len=0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                String s = new String(bytes,0,len, StandardCharsets.ISO_8859_1);//当给定的所有字节在缺省字符集中并非全部有效时，这个构造器的行为是不确定的。
                list.add(s);
            }
            for (int j=list.size()-1; j>=0; j--) {
                String s=list.get(j);
                if(s.contains("1")||s.contains("2")||s.contains("3")||s.contains("4")){
                    s =s.replaceAll("1","a").replaceAll("2","b")
                            .replaceAll("3","c").replaceAll("4","d");
                }//使用if else会导致每个字符串只有一部分被执行替换，如果这个字符串中还有其他需要被替换的就不会执行。
                list.remove(j);
                list.add(j, s);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file)){
            for (String o : list) {
                fos.write(o.getBytes());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(list);

    }


    // 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
    public static String getTime(Date date) {
        boolean sameYear = false;
        String todySDF = "HH:mm";
        String yesterDaySDF = "昨天";
        String beforYesterDaySDF = "前天";
        String otherSDF = "MM-dd HH:mm";
        String otherYearSDF = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sfd = null;
        String time = "";
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(now);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);

        if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
            sameYear = true;
        } else {
            sameYear = false;
        }

        if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
            sfd = new SimpleDateFormat(todySDF);
            time = sfd.format(date);
            return time;
        } else {
            todayCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
                sfd = new SimpleDateFormat(todySDF);
                time = yesterDaySDF + " " + sfd.format(date);

                //time = yesterDaySDF;
                return time;
            }
            todayCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是前天

                sfd = new SimpleDateFormat(todySDF);
                time = beforYesterDaySDF + " " + sfd.format(date);
                //time = beforYesterDaySDF;
                return time;
            }
        }

        if (sameYear) {
            sfd = new SimpleDateFormat(otherSDF);
            time = sfd.format(date);
        } else {
            sfd = new SimpleDateFormat(otherYearSDF);
            time = sfd.format(date);
        }

        return time;
    }



}








