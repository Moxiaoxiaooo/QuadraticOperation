package com.moxiaoxiao;

import javafx.print.Collation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
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


    /**
     * 计算一个分数表达式
     *
     * @param formula 要被计算的式子
     * @return 计算后的分数值
     */
    public static Fraction calculateFormula(CalculationFormula formula) {
        //初始化俩个栈，符号栈和算术栈
        Stack<Character> operatorStack = new Stack<>();
        Stack<Object> formulaStack = new Stack<>();
        //通过共用的下标，轮流insert进入stack
        for (int i = 0; i < formula.getNumbers().length; i++) {
            //1 + （ （ 2 + 3 ） * 4 ） - 5
            //处理左括号
            for (int j = 0; j < formula.getLBrackets()[i]; j++) {
                operatorStack.push('(');
            }
            //处理数字
            formulaStack.push(formula.getNumbers()[i]);
            //处理右括号
            for (int j = 0; j < formula.getRBrackets()[i]; j++) {
                while (true) {
                    char temp = operatorStack.pop();
                    if (temp == '(') {
                        break;
                    }
                    formulaStack.push(temp);
                }
            }
            //处理运算符
            if (i < formula.getNumbers().length - 1) {
                char operator = formula.getOperators()[i];
                while (true) {
                    if (operatorStack.empty() || operatorStack.peek() == '(') {
                        operatorStack.push(operator);
                        break;
                    } else if ((operator == '×' || operator == '*' || operator == '÷') && (operatorStack.peek() == '+' || operatorStack.peek() == '-')) {
                        operatorStack.push(operator);
                        break;
                    } else {
                        formulaStack.push(operatorStack.pop());
                    }
                }
            }
        }
        //处理剩下的运算符号
        while (!operatorStack.empty()) {
            formulaStack.push(operatorStack.pop());
        }
        //将运算式栈转为一般链表，并存入List以计算
        List<Object> formulaList = new LinkedList<>();
        while (!formulaStack.empty()) {
            formulaList.add(formulaStack.pop());
        }
        Collections.reverse(formulaList);
        //用一个栈零时存数字
        Stack<Fraction> fractionStack = new Stack<>();
        for (Object o : formulaList) {
            if (o instanceof Fraction) {
                //是个数字
                fractionStack.push((Fraction) o);
            } else if (o instanceof Character) {
                //是个计算符号
                Fraction num2 = fractionStack.pop();
                Fraction num1 = fractionStack.pop();
                Fraction num3 = calculateFraction(num1, num2, (char) o);
                fractionStack.push(num3);
            }
        }
        return fractionStack.pop().optimize();
    }


    /**
     * 计算两个分数
     *
     * @param a1       左边的分数
     * @param a2       右边的分数
     * @param operator 操作符号
     * @return 一个分数值
     */
    public static Fraction calculateFraction(Fraction a1, Fraction a2, char operator) {
        Fraction result = new Fraction();
        switch (operator) {
            case '+': {
                //分母相同，分子直接相加
                if (a1.getDenominator() == a2.getDenominator()) {
                    result.setDenominator(a1.getDenominator());
                    result.setNumerator(a1.getNumerator() + a2.getNumerator());
                } else {
                    result.setDenominator(a1.getDenominator() * a2.getDenominator());
                    result.setNumerator(a1.getNumerator() * a2.getDenominator() + a2.getNumerator() * a1.getDenominator());
                }
                break;
            }
            case '-': {
                //分母相同，分子直接相减
                if (a1.getDenominator() == a2.getDenominator()) {
                    result.setDenominator(a1.getDenominator());
                    result.setNumerator(a1.getNumerator() - a2.getNumerator());
                } else {
                    result.setDenominator(a1.getDenominator() * a2.getDenominator());
                    result.setNumerator(a1.getNumerator() * a2.getDenominator() - a2.getNumerator() * a1.getDenominator());
                }
                break;
            }
            case '×':
            case '*': {
                //分子*分子 分母*分母
                result.setNumerator(a1.getNumerator() * a2.getNumerator());
                result.setDenominator(a1.getDenominator() * a2.getDenominator());
                break;
            }
            case '÷': {
                //分子=a1分子*a2分母 分母=a1分母*a2分子
                result.setNumerator(a1.getNumerator() * a2.getDenominator());
                result.setDenominator(a1.getDenominator() * a2.getNumerator());
                break;
            }
        }
        result.optimize();
        return result;
    }
}
