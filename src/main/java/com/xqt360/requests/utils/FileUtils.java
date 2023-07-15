package com.xqt360.requests.utils;

import java.io.*;

public class FileUtils {
    public static void saveFile(byte[] bytes, String path) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write(bytes);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void saveFile(InputStream inputStream, String path) {
        File dest = new File(path);
        //确保是最新的文件，如果存在那么就删除
        if (dest.exists()) {
            dest.delete();
        }

        dest.getParentFile().mkdir();
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(path);
            byte[] buffer = new byte[1204];
            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                fs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();

        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
