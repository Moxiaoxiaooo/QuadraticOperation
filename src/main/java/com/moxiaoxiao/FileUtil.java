package com.moxiaoxiao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author 墨小小
 */
public class FileUtil {


    /**
     * 将表达式与答案写入对应的文件
     *
     * @param formulaFilePath   表达式的输出文件路径
     * @param ansFilePath       答案文件的输出路径
     * @param formulaAndAnsList 表达式与对应答案 对象的list
     * @return true
     */
    public static boolean writeFormulaAndAns(String formulaFilePath, String ansFilePath, List<FormulaAndAns> formulaAndAnsList) {
        FileWriter formulaFileWriter = null;
        FileWriter ansFileWriter = null;
        try {
            int i = 0;
            formulaFileWriter = new FileWriter(formulaFilePath);
            ansFileWriter = new FileWriter(ansFilePath);
            for (FormulaAndAns temp : formulaAndAnsList) {
                formulaFileWriter.write(i + ".  " + temp.getFormula().toString() + '\n');
                ansFileWriter.write(i + ".  " + temp.getAns().toString() + '\n');
                i++;
            }
            formulaFileWriter.flush();
            ansFileWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (formulaFileWriter != null) {
                try {
                    formulaFileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ansFileWriter != null) {
                try {
                    ansFileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    public static List<String> readFileToList(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        System.out.println(bufferedReader.readLine());
        System.out.println(bufferedReader.readLine());
        System.out.println(bufferedReader.readLine());
        return null;
    }

    public static boolean writeListToFile(String filePath, List list) {


        return false;
    }
}
