package com.clpz.mysql.monitor.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class  FileCustUtil {

    public static void appendFileLine(String path, String data) throws IOException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path, true);// 追加写入
            outputStream.write(("\r\n" + data).getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }

}
