package com.wangjf.myutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wangjf on 18-1-11.
 */

public class Utf8FileUtils {

    //将字符串输出到UTF-8文件
    public static void WriteUTF8File(String fileName, String data) {
        try {
            File file = new File(fileName);
            file.createNewFile();
            FileOutputStream fOS = new FileOutputStream(file);
            byte[] byteData = data.getBytes("UTF-8");
            fOS.write(byteData);
            fOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String ReadUTF8File(String fileName) {

        File file = new File(fileName);
        int fileLen = (int) file.length();
        byte[] byteData = new byte[fileLen];

        if(fileLen == 0) {
            return null;
        }
        try {
            FileInputStream fIS = new FileInputStream(file);
            fIS.read(byteData);
            fIS.close();
            return new String(byteData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(byteData);
    }

}
