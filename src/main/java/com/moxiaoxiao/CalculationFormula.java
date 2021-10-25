package com.moxiaoxiao;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 墨小小
 * <p>
 * 计算式类
 */
@Setter
@Getter
public class CalculationFormula {

    /**
     * 计算符号
     */
    private char[] operators;

    /**
     * 左括号位置
     * 下标代表包裹的数字序号，值代表括号个数
     */
    private int[] lBrackets;

    /**
     * 右括号位置
     * 下标代表包裹的数字序号，值代表括号个数
     */
    private int[] rBrackets;

    /**
     * 分数
     */
    private Fraction[] numbers;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            //拼接数字
            for (int j = 0; j < lBrackets[i]; j++) {
                stringBuilder.append("(");
            }
            stringBuilder.append(numbers[i].toString());
            for (int j = 0; j < rBrackets[i]; j++) {
                stringBuilder.append(")");
            }
            if (i < operators.length) {
                stringBuilder.append(operators[i]);
            }
        }
        return stringBuilder.toString();
    }


}
