package com.moxiaoxiao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
            int i = 1;
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


    public static List<String> readFileToList(String filePath) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        List<String> resultList = new ArrayList<>();
        try {
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                resultList.add(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return resultList;
    }

    public static boolean writeStringToFile(String value, String filePath) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
