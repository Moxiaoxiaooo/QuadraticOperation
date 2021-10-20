package com.moxiaoxiao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 墨小小
 * <p>
 * 计算部分
 * +−×÷
 */
public class Calculation {

    public static String getResult(String str) throws IndexOutOfBoundsException, Exception {

        //处理包裹在最外层的括号
        if (str.startsWith("(") && str.endsWith(")")) {
            str = str.substring(1, str.length() - 1);
        }
        //处理空格
        str = str.replaceAll(" ", "");
        //处理一下计算过程中出现的--情况,首位--直接去掉，中间--变为+
        str = str.startsWith("--") ? str.substring(2) : str;
        str = str.replaceAll("--", "+");
        str = str.replaceAll("\\+-", "-");
        System.out.println("新表达式：" + str);
        if (str.matches("-{0,1}[0-9]+([.][0-9]+){0,1}")) {//不存在运算符了，即递归结束，这里的正则为匹配所有的正负整数及小数
            return str;
        }
        //表示每次递归计算完一步后的表达式
        String newExpr = null;
        // 第一步：从最后的最里面的括号开始去括号至无括号
        if (str.contains("(")) {
            //最后一个左括号的索引值
            int lIndex = str.lastIndexOf("(");
            //该左括号对应的右括号的索引
            int rIndex = str.indexOf(")", lIndex);
            //括号中的字表达式
            String subExpr = str.substring(lIndex + 1, rIndex);
            System.out.println("准备括号：(" + subExpr + ")");
            newExpr = str.substring(0, lIndex) + getResult(subExpr) //调用本身，计算括号中表达式结果
                    + str.substring(rIndex + 1);
            return getResult(newExpr);
        }

        // 第二步：去乘除至无乘除
        if (str.contains("*") || str.contains("/") || str.contains("×") || str.contains("÷")) {
            //该正则表示匹配一个乘除运算，如1.2*3  1.2/3  1.2*-2 等
            Pattern p = Pattern.compile("[0-9]+([.][0-9]+){0,1}[*/×÷]-{0,1}[0-9]+([.][0-9]+){0,1}");
            Matcher m = p.matcher(str);
            //找到了匹配乘除的计算式
            if (m.find()) {
                //第一个乘除表达式
                String temp = m.group();
                System.out.println("计算乘除：" + temp);
                String[] a = temp.split("[*/×÷]");
                newExpr = str.substring(0, m.start())
                        + doubleCal(Double.parseDouble(a[0]), Double.parseDouble(a[1]), temp.charAt(a[0].length()))
                        + str.substring(m.end());
            }
            return getResult(newExpr);
        }

        // 第三步：去加减至无加减
        if (str.contains("+") || str.contains("-")) {
            //该正则表示匹配一个乘除运算，如1.2+3  1.2-3  1.2--2  1.2+-2等
            Pattern p = Pattern.compile("-{0,1}[0-9]+([.][0-9]+){0,1}[+-][0-9]+([.][0-9]+){0,1}");
            Matcher m = p.matcher(str);
            if (m.find()) {
                //第一个加减表达式
                String temp = m.group();
                System.out.println("计算加减：" + temp);
                String[] a = temp.split("\\b[+-]", 2);
                newExpr = str.substring(0, m.start())
                        + doubleCal(Double.parseDouble(a[0]), Double.parseDouble(a[1]), temp.charAt(a[0].length()))
                        + str.substring(m.end());
            }
            return getResult(newExpr);
        }

        throw new Exception("计算错误，请确定表达式： " + str + " 是否存在错误或者括号正常匹配");
    }


    /**
     * 计算数值
     *
     * @param a1       第一个数字
     * @param a2       第二个数字
     * @param operator 计算式符号
     * @return 计算结果
     * @throws Exception 异常
     */
    public static double doubleCal(double a1, double a2, char operator) throws Exception {
        switch (operator) {
            case '+': {
                return a1 + a2;
            }
            case '-': {
                return a1 - a2;
            }
            case '*':
            case '×': {
                return a1 * a2;
            }
            case '/':
            case '÷': {
                if (a2 == 0) {
                    throw new Exception("请确保计算式除数不为0！");
                } else {
                    return a1 / a2;
                }
            }
            default: {
                break;
            }
        }
        throw new Exception("非 +-×÷ 表达式，请确保符号正确");
    }
}
