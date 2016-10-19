package com.android.haobanyi.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.android.haobanyi.model.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fWX228941 on 2016/5/18.
 *
 * @作者: 付敏
 * @创建日期：2016/05/18
 * @邮箱：466566941@qq.com
 * @当前文件描述：管理城市列表数据库的Manager，主要用于从raw资源文件中导入数据库文件到本地数据库中
 * 步骤：涉及到数据库，向外提供三个接口
 *      01.提供构造函数，这样可以获取实体，调用系列公开的访问接口
 *      02.客户端需要调用初始化创建服务器端的数据库
 *      03.关闭数据库
 * 优化：01+01可以合并为一个接口
 */
public class CityDBManager {
    //1.参考设计：http://blog.csdn.net/li641808825/article/details/47753153 http://blog.csdn.net/wsz005/article/details/19912165
    private Context context;
    //2.预定缓存大小
    private int BUFFER_SIZE = 1024;
    //2.保存的数据库文件名
    public static final String DB_NAME = "regions.db";//数据库名不对外公开
    //3.当前应用程序所在的包名
    private String packageName;
    //4.本地数据库的路径
    public static String databasePath;
    //6.数据库对象
    private SQLiteDatabase database;
    //5.实体对象的各种属性赋值
    public CityDBManager(Context context){//初始化调用时，总是希望传递一个context进来
        this.context = context;
        this.packageName = context.getPackageName();
        //1) 对于数据库路径增加一层兼容性判断
        if (Build.VERSION.SDK_INT >= 17){
            this.databasePath = context.getApplicationInfo().dataDir+"/databases";

        } else {
            this.databasePath = "/data"+Environment.getDataDirectory().getAbsolutePath() + File.separator+packageName+"/databases";
        }
        this.database = this.initDatabase(databasePath + "/" + DB_NAME);
    }
    //7.初始化数据库，依据完整的数据库文件路径，创建所需数据库
    private SQLiteDatabase initDatabase(String dbfile) {
        // 1）先要创建文件路径，这一步必须要，不然会找不到路径
        File dFile=new File(databasePath);
        if (!dFile.exists()) {
            dFile.mkdir();
        }
        // 2) 检查数据库文件是否存在——复制assert中的数据库文件
        try {
            if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream oldDatabaseFile = this.context.getResources().openRawResource(
                        R.raw.regions); //欲导入的数据库  db文件成为流
                //方式二  this.context.getAssets().open(dbfile);
                BUFFER_SIZE = oldDatabaseFile.available();
                byte[] buffer = new byte[BUFFER_SIZE];
                FileOutputStream newDatabaseFile = new FileOutputStream(dbfile);// 写入数据库中的表中，写入一次，根据路径创建文件
                int length = 0;
                while ((length = oldDatabaseFile.read(buffer)) > 0) {
                    newDatabaseFile.write(buffer, 0, length);/*这段逻辑怎么没有用，感觉就是一个复制*/
                }
                newDatabaseFile.flush();
                newDatabaseFile.close();
                oldDatabaseFile.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

    //8.关闭数据库
    public void closeDatabase() {
        this.database.close();
    }

}
