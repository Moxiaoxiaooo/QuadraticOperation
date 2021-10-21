package com.moxiaoxiao;

import static com.moxiaoxiao.Generation.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AppTest {


    @Test
    public void testGenerateQuestion() {
        /*
        0×((4×0/3))÷3
         */
        List<CalculationFormula> calculationFormulaList = Generation.generateQuestionList(100, 10, 5);
        for (CalculationFormula formula : calculationFormulaList) {
            System.out.println("formula =============================== " + formula);
        }
        System.out.println("Hello");
    }

    @Test
    public void test() {
        int a[] = {1, 2, 3, 4, 5};
        int b[] = Arrays.copyOf(a, 8);
        System.out.println(b);
    }

    @Test
    public void testGenerateBrackets() {
        int operatorCounts = 5;
        int upperLimit = 10;
        int size = 100;
        for (int T = 0; T < size; T++) {
            System.out.println("in");

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
            int lBracketCounts = generateRandomInt(operatorCounts + 1);
            while (lBracketCounts > 0) {
                int lIndex = generateRandomInt(operatorCounts);
                int generateLCounts = generateRandomInt(lIndex) + 1;
                //有可能随机到同样的index，所以用 += 而不能直接 =
                calculationFormula.getLBrackets()[lIndex] += generateLCounts;
                lBracketCounts -= generateLCounts;

                System.out.println("左括号:\t\t" + Arrays.toString(calculationFormula.getLBrackets()));
                int rBracketCounts = generateLCounts;
                //生成左括号后生成对应个数的右括号
                while (rBracketCounts > 0) {
                    int generateRCounts = generateRandomInt(rBracketCounts) + 1;
                    //生成的位置必定在左括号下标的右边
                    int rIndex = generateRandomInt(operatorCounts - lIndex) + 1 + lIndex;
                    calculationFormula.getRBrackets()[rIndex] += generateRCounts;
                    rBracketCounts -= generateRCounts;
                    System.out.println("右括号：\t\t" + Arrays.toString(calculationFormula.getRBrackets()));
                    //除去重复生成的括号
                    /*
                    todo
                         in
                            左括号:		[0, 0, 2, 0, 0, 0]
                            右括号：		[0, 0, 0, 0, 1, 0]
                            右括号：		[0, 0, 0, 0, 1, 1]
                            左括号:		[0, 0, 2, 0, 1, 0]
                            右括号：		[0, 0, 0, 0, 1, 2]
                        out
                     */
                    if (calculationFormula.getRBrackets()[rIndex] > 1 && calculationFormula.getLBrackets()[lIndex] > 1) {
                        int num = Math.max(calculationFormula.getRBrackets()[rIndex] - 1, 1);
                        calculationFormula.getLBrackets()[lIndex] -= num;
                        calculationFormula.getRBrackets()[rIndex] -= num;
                        System.out.println("去重后的左：\t" + Arrays.toString(calculationFormula.getLBrackets()));
                        System.out.println("去重后的右：\t" + Arrays.toString(calculationFormula.getRBrackets()));
                    }
                }
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
                int bracketCounts = calculationFormula.getLBrackets()[i];
                if (bracketCounts >= calculationFormula.getRBrackets()[i]) {
                    int num = Math.min(bracketCounts, calculationFormula.getRBrackets()[i]);
                    calculationFormula.getLBrackets()[i] -= num;
                    calculationFormula.getRBrackets()[i] -= num;
                }
            }
            System.out.println("out");
        }
    }

    @Test
    public void testList() {
        Set<String> ww = new TreeSet<>();
        ww.add("123");
        System.out.println(ww);
        ww.add("12");
        System.out.println(ww);

    }
}
