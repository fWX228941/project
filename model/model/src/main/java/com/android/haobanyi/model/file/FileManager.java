package com.android.haobanyi.model.file;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * Created by fWX228941 on 2016/5/30.
 *
 * @作者: 付敏
 * @创建日期：2016/05/30
 * @邮箱：466566941@qq.com
 * @当前文件描述：对文件进行增删改查，这里仅仅考虑内部文件的存储。默认情况下存储在内部设备上的文件只能被本应用的app单独访问
 * 适合保存json数据和app相关的私密信息
 *
 *
 *  更多参考：
 *  http://blog.csdn.net/hester_hester/article/details/51335133
 *  http://blog.csdn.net/FishSeeker/article/details/51232259
 *
 *  更强大的功能：文件夹管理器1.0：实现了增删改查，排序，复制和粘贴操作.
 *  https://github.com/jiangzhengnan/PumpkinFileManager
 *  http://blog.csdn.net/qq_22770457/article/details/51536263
 *
 */
public class FileManager {
    /*
        首先都是需要去判断，不然就会出现文件不存在等各种问题
    *  根据文件路径名创建文件路径
    *  比如：filePath = /data"+Environment.getDataDirectory().getAbsolutePath() + File.separator+packageName+"/databases
    *  data/data/databases
    *
    *
    * */
    public static void makedir(String filePath){
        File dFile=new File(filePath);
        if (!dFile.exists()) {
            dFile.mkdir();
        }
    }
    /*
    *  根据完整的路径名+文件名,raw文件
    *  比如：databasePath + "/" + DB_NAME
    *  data/data/databases/regions.db
    *  @param :int id 文件id 比如：R.raw.regions
    *  @param ：需要把参数一的流文件写入到文件中
    *
    * */
    public static void copyRawToFile(Context context,int rawid, String fileAllPath){
        try {
            if (!(new File(fileAllPath).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream oldDatabaseFile = context.getResources().openRawResource(
                        rawid); //欲导入的数据库  db文件成为流
                //方式二  this.context.getAssets().open(dbfile);
                int BUFFER_SIZE = 1024;
                BUFFER_SIZE = oldDatabaseFile.available();
                byte[] buffer = new byte[BUFFER_SIZE];
                FileOutputStream newDatabaseFile = new FileOutputStream(fileAllPath);// 写入数据库中的表中，写入一次，根据路径创建文件
                int length = 0;
                while ((length = oldDatabaseFile.read(buffer)) > 0) {
                    newDatabaseFile.write(buffer, 0, length);/*这段逻辑怎么没有用，感觉就是一个复制*/
                }
                newDatabaseFile.flush();
                newDatabaseFile.close();
                oldDatabaseFile.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }

    }
    /*
    * 参考文档：http://blog.csdn.net/eyishion/article/details/51502849
    * 存储到内存文件中:
    *       调用context.openfileOutput() 存储
    *       context.openfileInput() 读取
    *  默认存储位置：默认是存储到data/data/files/目录下面
    *  一般使用它最便捷
    *
    * */
    public static void saveDataToSystemFile(Context context,String fileName,String data ) {
        BufferedWriter bw = null;
        try {
            FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);//传入文件名称
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(data);
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /*
        读取文件中的数据,客户端是主  法一：
    *
    * String fileName = data.txt
    *
    * */
    public static String restoreDataFromSystemFile(Context context, String fileName) {
        byte[] buffer = new byte[1024];
        String data = "";
        try {
            FileInputStream fis = context.openFileInput(fileName);//参数是文件名

            int in = -1;
            while ((in = fis.read(buffer)) != -1) {
                String s = new String(buffer, 0, in);
                data += s;
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

    }
    /* 法二：打开文件读取内容*/
    public static String read(Context context,String fileName){
        String line;
        StringBuilder sb=new StringBuilder();
        BufferedReader br=null;
        try {
            FileInputStream fis=context.openFileInput(fileName);
            br=new BufferedReader(new InputStreamReader(fis));
            while((line=br.readLine())!=null){
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(br!=null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /*
    * 从通过url获取文件名，然后保存在缓存目录下：
    * getFilesDir()     会返回你的app的内部存储空间的路径
    * getCacheDir()     返回你的app的内部存储空间的缓存目录， 如果缓存文件不太需要的话，请及时清理已保证缓存目录的以保证缓存文件不会占用太大空间，
    * */
    public static File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /*综合判断*/
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

}
