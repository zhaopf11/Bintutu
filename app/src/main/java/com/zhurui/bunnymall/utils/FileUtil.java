package com.zhurui.bunnymall.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * Created by zhaopf on 2017/9/9 0009.
 */

public class FileUtil {
    /**
     * 创建临时文件夹
     * @param pathName
     * @return
     */
    public static File tempFile(String pathName){
        File file = null;
        String path = "";
        String filePath = "";
        if(isSDCardEnable()){
            path = Environment.getExternalStorageDirectory().getPath() + File.separator + "bintuImage";
            File file1 = new File(path);
            if(!file1.mkdirs()){
                file1.mkdirs();
            }
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "bintuImage"+ File.separator + pathName;
            file = new File(filePath);
            if(!file.mkdirs()){
                file.mkdirs();
            }
        }
        return file;
    }

    /**
     * 判断SDCard是否可用
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * 复制文件
     * @param oldPath
     * @param newPathFile
     */
    @SuppressWarnings("unused")
    public static void copyFile(String oldPath,  File newPathFile) {
        try {
            int bytesum = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPathFile.getPath());
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inStream.read(buffer)) != -1) {
                    bytesum += length; // 字节数 文件大小
                    fs.write(buffer, 0, length);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            // System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 获取指定文件大小
     * @param file
     * @return
     * @throws Exception 　　
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 转换文件大小
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
