package com.moxiaoxiao;

import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        //数值范围
        int r = 0;
        //表达式个数
        int n = 0;
        //要读取的计算式文件
        String e = null;
        //要读取的计算式答案文件
        String a = null;
        for (int i = 0; i < args.length; i++) {
            if ("-r".equals(args[i])) {
                //数值范围
                r = Integer.parseInt(args[i + 1]);
            }
            if ("-n".equals(args[i])) {
                //表达式个数
                n = Integer.parseInt(args[i + 1]);
            }
            if ("-e".equals(args[i])) {
                //计算式文件
                e = args[i + 1];
            }
            if ("-a".equals(args[i])) {
                //计算式答案文件
                a = args[i + 1];
            }
        }
        if (n == 0 || r == 0) {
            System.out.println("请确保参数列表正确，通过 -r [整数数值]  指定数值范围， 通过 -n [整数数值]  指定表达式个数");
        } else if (a != null && e != null) {
            List<String> formulaList = FileUtil.readFileToList(e);
            List<String> ansList = FileUtil.readFileToList(a);
            String result = Calculation.countFormulaCorrectCounts(formulaList, ansList);
            FileUtil.writeStringToFile(result, "Grade.txt");

        } else {
            //写入文件
            List<FormulaAndAns> resultList = Generation.generateQuestionList(n, r, 3);
            FileUtil.writeFormulaAndAns("Exercises.txt", "Answers.txt", resultList);
        }


    }


}
