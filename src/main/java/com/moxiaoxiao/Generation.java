package com.moxiaoxiao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author 墨小小
 * <p>
 * 生成问题部分
 */
public class Generation {

    /**
     * @param size           计算式个数
     * @param upperLimit     数字的上限值
     * @param operatorCounts 符号个数
     * @return
     */
    public static List<CalculationFormula> generateQuestionList(int size, int upperLimit, int operatorCounts) {
        List<CalculationFormula> calculationFormulaList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            calculationFormulaList.add(generateRandomCalculationFormula(upperLimit, operatorCounts));
        }
        return calculationFormulaList;
    }


    public static CalculationFormula generateRandomCalculationFormula(int upperLimit, int operatorCounts) {
        //            operatorCounts = generateRandomInt(operatorCounts)+1;
        //先生成数字
        CalculationFormula calculationFormula = new CalculationFormula();
        calculationFormula.setNumbers(new Fraction[operatorCounts + 1]);
        calculationFormula.setOperators(new char[operatorCounts]);
        calculationFormula.setLBrackets(new int[operatorCounts + 1]);
        calculationFormula.setRBrackets(new int[operatorCounts + 1]);
        //生成第一个数字
        if (generateRandomInt(2) >= 1) {
            calculationFormula.getNumbers()[0] = generateRandomFraction(upperLimit);
        } else {
            calculationFormula.getNumbers()[0] = generateRandomNatureNumber(upperLimit);
        }
        for (int j = 1; j < operatorCounts + 1; j++) {
            //考虑是生成分数还是整数
            if (generateRandomInt(2) >= 1) {
                calculationFormula.getNumbers()[j] = generateRandomFraction(upperLimit);
            } else {
                calculationFormula.getNumbers()[j] = generateRandomNatureNumber(upperLimit);
            }
            //生成操作符号
            char operator = generateRandomOperator();
            calculationFormula.getOperators()[j - 1] = operator;
            //考虑可能出现 ÷0 的情况
            if ('÷' == operator) {
                while (calculationFormula.getNumbers()[j].getValue() == 0) {
                    if (generateRandomInt(2) >= 1) {
                        calculationFormula.getNumbers()[j] = generateRandomFraction(upperLimit);
                    } else {
                        calculationFormula.getNumbers()[j] = generateRandomNatureNumber(upperLimit);
                    }
                }
            }
            //考虑可能出现 e1-e2 < 0 的情况
            if ('-' == operator) {
                while (calculationFormula.getNumbers()[j - 1].getValue() - calculationFormula.getNumbers()[j].getValue() < 0) {
                    if (generateRandomInt(2) >= 1) {
                        calculationFormula.getNumbers()[j] = generateRandomFraction(upperLimit);
                    } else {
                        calculationFormula.getNumbers()[j] = generateRandomNatureNumber(upperLimit);
                    }
                    if (generateRandomInt(2) >= 1) {
                        calculationFormula.getNumbers()[j - 1] = generateRandomFraction(upperLimit);
                    } else {
                        calculationFormula.getNumbers()[j - 1] = generateRandomNatureNumber(upperLimit);
                    }
                }
            }
        }

        /*
        length = oper + 1;
        L: [0,length-1)
        R: [1,length)
         */
        //随机生成括号
        Set<String> indexSet = new TreeSet<>();
        int lBracketCounts = generateRandomInt(operatorCounts + 1);
        while (lBracketCounts > 0) {
            int lIndex = generateRandomInt(operatorCounts);
            int rIndex = generateRandomInt(operatorCounts - lIndex) + lIndex + 1;
            if (lIndex == rIndex) {
                continue;
            }
            indexSet.add(lIndex + ":" + rIndex);
            lBracketCounts--;
        }
        indexSet.remove("0:" + operatorCounts);
        for (String temp : indexSet) {
            String[] index = temp.split(":");
            calculationFormula.getLBrackets()[Integer.parseInt(index[0])]++;
            calculationFormula.getRBrackets()[Integer.parseInt(index[1])]++;
        }

        //优化重复括号和开头末尾括号
        //开头末尾
        if (calculationFormula.getLBrackets()[0] > 0 &&
                calculationFormula.getRBrackets()[operatorCounts] > 0) {
            int min = Math.min(calculationFormula.getLBrackets()[0], calculationFormula.getRBrackets()[operatorCounts]);
            calculationFormula.getLBrackets()[0] -= min;
            calculationFormula.getRBrackets()[operatorCounts] -= min;
        }
        //循环遍历左括号，除去(((Number)) + )的情况
        for (int i = 0; i < operatorCounts; i++) {
            if (calculationFormula.getRBrackets()[i] > 0) {
                int num = Math.min(calculationFormula.getLBrackets()[i], calculationFormula.getRBrackets()[i]);
                calculationFormula.getLBrackets()[i] -= num;
                calculationFormula.getRBrackets()[i] -= num;
            }
        }

        //去除远距离的重复的括号
        Set<String> recordSet = new TreeSet<>();
        for (int i = calculationFormula.getLBrackets().length - 2; i >= 0; i--) {
            if (calculationFormula.getLBrackets()[i] > 0) {
                for (int j = i + 1; j < calculationFormula.getRBrackets().length; j++) {
                    if (calculationFormula.getRBrackets()[j] > 0) {
                        //做记录，并对应减少
                        int min = Math.min(calculationFormula.getLBrackets()[i], calculationFormula.getRBrackets()[j]);
                        calculationFormula.getLBrackets()[i] -= min;
                        calculationFormula.getRBrackets()[j] -= min;
                        //左括号下标:右括号下标
                        recordSet.add(i + ":" + j);
                    }
                    if (calculationFormula.getLBrackets()[i] == 0) {
                        break;
                    }
                }
            }
        }
        //9/4×6/3)+(0×0/6
        //去掉开头末尾
//        recordSet.remove("0:" + operatorCounts);
        for (String temp : recordSet) {
            String[] index = temp.split(":");
            calculationFormula.getLBrackets()[Integer.parseInt(index[0])]++;
            calculationFormula.getRBrackets()[Integer.parseInt(index[1])]++;
        }

        return calculationFormula;
    }

    /**
     * 随机生成计算符号
     */
    public static char generateRandomOperator() {
        switch (generateRandomInt(4)) {
            case 0:
                return '+';
            case 1:
                return '-';
            case 2:
                return '×';
            case 3:
                return '÷';
            default:
                return '\0';
        }
    }


    /**
     * 生成一个分母可以为1的分数
     *
     * @param upperLimit 上限
     * @return 具体分数
     */
    public static Fraction generateRandomFraction(int upperLimit) {
        Fraction result = new Fraction();
        result.setNumerator(generateRandomInt(upperLimit));
        int num = 0;
        //循环生成直至分母不为0
        while ((num = generateRandomInt(upperLimit)) == 0) ;
        result.setDenominator(num);
        return result;
    }

    /**
     * 生成一个分母绝对为1的分数
     *
     * @param upperLimit 上限
     * @return 具体分数
     */
    public static Fraction generateRandomNatureNumber(int upperLimit) {
        Fraction result = new Fraction();
        result.setNumerator(generateRandomInt(upperLimit));
        result.setDenominator(1);
        return result;
    }

    /**
     * 生成一个随机的，处于区间 [ 0 , upperLimit ) 的整数
     *
     * @param upperLimit 上限
     * @return 具体的值
     */
    public static int generateRandomInt(int upperLimit) {
        return (int) (Math.random() * upperLimit * 100 % upperLimit);
    }


}
