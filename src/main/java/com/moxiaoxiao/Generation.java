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
        }

        //todo 括号去重:((1+1))  从最右边的同一位上的多个（ 开始 找匹配的 ），如果）有多个，就同时删减，再递归处理次个（
        //添加左边的括号
        /*
        length = oper + 1;
        L: [0,length-1)
        R: [1,length)
         */
        //随机生成括号
        int lBracketCounts = generateRandomInt(operatorCounts + 1);
        while (lBracketCounts > 0) {
            int generateLCounts = generateRandomInt(lBracketCounts) + 1;
            int lIndex = generateRandomInt(operatorCounts);
            //有可能随机到同样的index，所以用 += 而不能直接 =
            calculationFormula.getLBrackets()[lIndex] += generateLCounts;
            lBracketCounts -= generateLCounts;
            int rBracketCounts = generateLCounts;
            //生成左括号后生成对应个数的右括号
            while (rBracketCounts > 0) {
                int generateRCounts = generateRandomInt(rBracketCounts) + 1;
                //生成的位置必定在左括号下标的右边
                int rIndex = generateRandomInt(operatorCounts - lIndex) + 1 + lIndex;
                calculationFormula.getRBrackets()[rIndex] += generateRCounts;
                rBracketCounts -= generateRCounts;
               /* //除去重复生成的括号
                if (calculationFormula.getRBrackets()[rIndex] > 1 && calculationFormula.getLBrackets()[lIndex] > 1) {
                    calculationFormula.getLBrackets()[lIndex] -= generateRCounts;
                    calculationFormula.getRBrackets()[rIndex] -= generateRCounts;
                }*/
            }
        }
        //优化重复括号和开头末尾括号
/*        //开头末尾
        if (calculationFormula.getLBrackets()[0] > 0 &&
                calculationFormula.getRBrackets()[operatorCounts] > 0) {
            int min = Math.min(calculationFormula.getLBrackets()[0], calculationFormula.getRBrackets()[operatorCounts]);
            calculationFormula.getLBrackets()[0] -= min;
            calculationFormula.getRBrackets()[operatorCounts] -= min;
        }*/
        //循环遍历左括号，除去(((Number)) + )的情况
        for (int i = 0; i < operatorCounts; i++) {
            if (calculationFormula.getRBrackets()[i] > 0) {
                int num = Math.min(calculationFormula.getLBrackets()[i], calculationFormula.getRBrackets()[i]);
                calculationFormula.getLBrackets()[i] -= num;
                calculationFormula.getRBrackets()[i] -= num;
            }
        }
        //去除远距离的重复的括号
        System.out.println("in");
        System.out.println("L：\t\t" + Arrays.toString(calculationFormula.getLBrackets()));
        System.out.println("R：\t\t" + Arrays.toString(calculationFormula.getRBrackets()));
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
                        System.out.println(i + ":" + j);
                        System.out.println("del L：\t\t" + Arrays.toString(calculationFormula.getLBrackets()));
                        System.out.println("del R：\t\t" + Arrays.toString(calculationFormula.getRBrackets()));
                    }
                    if (calculationFormula.getLBrackets()[i] == 0) {
                        break;
                    }
                }
            }
        }
        //去掉开头末尾
        recordSet.remove("0:" + operatorCounts);
        System.out.println("recordSet:" + recordSet);
        for (String temp : recordSet) {
            String[] index = temp.split(":");
            calculationFormula.getLBrackets()[Integer.parseInt(index[0])]++;
            calculationFormula.getRBrackets()[Integer.parseInt(index[1])]++;
            System.out.println("add L：\t\t" + Arrays.toString(calculationFormula.getLBrackets()));
            System.out.println("add R：\t\t" + Arrays.toString(calculationFormula.getRBrackets()));
        }
        System.out.println("out");
        //遍历左括号，从右端开始
/*        A:
        for (int i = calculationFormula.getLBrackets().length - 2; i >= 0; i--) {
            //忽略只有1个或没有括号的情况
            if (calculationFormula.getLBrackets()[i] <= 1) {
                continue;
            }
            int ableCounts = calculationFormula.getLBrackets()[i] - 1;
            int ignoreCounts = countBrackets(calculationFormula.getLBrackets(), i, false);

            int ignoreCountSum = 0;
            B:
            for (int j = i; j < calculationFormula.getRBrackets().length; j++) {
                //忽略的个数还没到，继续加
                if (ignoreCountSum < ignoreCounts) {//to do 考虑需要 <= 还是 <
                    ignoreCountSum += calculationFormula.getRBrackets()[j];
                    continue;
                }
                //忽略的个数到了，开始考虑减少
                //匹配单个或没有括号的情况
                if (calculationFormula.getRBrackets()[j] <= 1) {
                    ableCounts -= calculationFormula.getRBrackets()[j];
                    continue;
                }
                if (ableCounts < ignoreCountSum - ignoreCounts) {// 绝对能有留1个多的括号，因为是<
                    //允许的个数比较少
                    calculationFormula.getRBrackets()[j] -= ableCounts;
                    break;
                } else {
                    //允许的个数比较多
                    int difference = ignoreCountSum - ignoreCounts -1;

                    ignoreCountSum +;


                    continue ;
                }
            }
        }*/
        System.out.println("formula =============================== " + calculationFormula);
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
     * 计算某个方向上的括号数量
     *
     * @param index        开始的下标
     * @param bracketArray 括号数组
     * @param startInRight 是否是从右向左数
     * @return
     */
    public static int countBrackets(int[] bracketArray, int index, boolean startInRight) {
        int result = 0;
        if (startInRight) {
            //从右边开始
            for (int i = index; i >= 0; i--) {
                result += bracketArray[i];
            }
        } else {
            //从左边开始
            for (int i = index; i < bracketArray.length; i++) {
                result += bracketArray[i];
            }
        }
        return result;
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
