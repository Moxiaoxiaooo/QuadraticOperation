package com.moxiaoxiao;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 墨小小
 * <p>
 * 分数类
 */
@Setter
@Getter
public class Fraction implements Comparable {

    /**
     * 分子
     */
    private int numerator;

    /**
     * 分母
     */
    private int denominator;

    @Override
    public String toString() {
        this.optimize();
        //分很多种情况：分子为0  分母为 1  分子>分母 分子==分母 分子<分母
        if (numerator == 0) {
            return "0";
        } else if (denominator == 1) {
            return "" + numerator;
        } else if (numerator == denominator) {
            return "1";
        } else if (numerator > denominator) {
            return (numerator / denominator) + "'" + (numerator % denominator) + "/" + denominator;
        } else {
            //-667/468
            if (Math.abs(numerator) > denominator) {
                return "-" + ((-numerator) / denominator) + "'" + ((-numerator) % denominator) + "/" + denominator;
            } else {
                return numerator + "/" + denominator;
            }
        }
    }

    /**
     * 获取分数的值
     *
     * @return 分数的值
     */
    public double getValue() {
        return numerator / denominator;
    }

    /**
     * 优化分数
     */
    public Fraction optimize() {
        //考虑 14/7  8/6 5/5 4/6 这种需要除公约数的情况
        if (this.getNumerator() >= this.getDenominator()) {
            int commonDivisor = getCommonDivisor(this.getNumerator(), this.getDenominator());
            this.setDenominator(this.getDenominator() / commonDivisor);
            this.setNumerator(this.getNumerator() / commonDivisor);
        }
        return this;
    }


    /**
     * 通过辗转相除法获取公约数
     *
     * @param a
     * @param b
     * @return 公约数
     */
    public static int getCommonDivisor(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }
}
