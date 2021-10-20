package com.moxiaoxiao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.moxiaoxiao.Calculation.getResult;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        String str = "(((1+2-3*4+5)))";
        System.out.println("开始计算表达式：" + str);
        try {
            System.out.println("结果为：" + getResult(str));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out");
        }

    }


}
