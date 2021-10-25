package com.moxiaoxiao;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 墨小小
 * @date 21-10-25 20:09
 */
@Setter
@Getter
public class FormulaAndAns {

    /**
     * 计算式
     */
    private CalculationFormula formula;

    /**
     * 结果值
     */
    private Fraction ans;


    @Override
    public String toString() {
        return "Formula : " + formula.toString() + "    Ans : " + ans.toString();
    }
}
