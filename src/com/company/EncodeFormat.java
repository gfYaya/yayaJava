package com.company;

import java.util.Arrays;

/**
 * Created by Yaya on 2017/7/25.
 * 编码转换测试 ,将换一个utf-8的编码字符串 转换成一个 gbk格式的字符串,并变化为16进制
 */
public class EncodeFormat {
    public static void main(String[] args){
        String old = "你好";
        System.out.println("old bytes: " + Arrays.toString(old.getBytes()));
        try {
            String news = new String(old.getBytes("UTF-8"), "GBK");  //old.getBytes("UTF-8") 拿到的二进制编码应该还是utf-8的格式
            //String news = new String(old.getBytes("GBK"));
            //String news = new String(old.getBytes(),"GBK");
            //String news = new String(old.getBytes("GBK"),"UTF-8");
            System.out.println(news);
            System.out.println("new bytes: " +Arrays.toString(news.getBytes("GBK")));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}
