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
public class Fraction {

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
        return numerator + (denominator == 1 ? "" : ("/" + denominator));
    }

    /**
     * 获取分数的值
     *
     * @return 分数的值
     */
    public double getValue() {
        return numerator / denominator;
    }
}
