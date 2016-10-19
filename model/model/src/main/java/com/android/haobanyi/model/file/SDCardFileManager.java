package com.android.haobanyi.model.file;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by fWX228941 on 2016/5/30.
 *
 * @作者: 付敏
 * @创建日期：2016/05/30
 * @邮箱：466566941@qq.com
 * @当前文件描述：对外纯文件的管理，公共区域，当卸载app的时候，系统只会移除存储在getExternalFilesDir()文件夹下的文件，其他的文件不会主动移除。
 *  注意点：需根据文件类型，来有选择性地将他们保存在不同的目录下、
 *      1）共有文件： 这些文件可同时被其他的app使用，当卸载时，不希望被删除，比如下载的图片，或者使用app拍摄的照片等。
 *      2）私有文件： App的单独使用的文件，在卸载时，希望同时被删除，例如一些缓存数据。
 *
 *
 *参考文档：http://blog.csdn.net/lianchen/article/details/48015663
 */
public class SDCardFileManager {
    /*
    * 因为外存是可拆卸的，所以在访问之前必须确保其存在
    * */
    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    /*
    * 创建共有文件
    * */
    public File getAlbumStorageDir(String fileName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), fileName);//使用getExternalStoragePublicDirectory()方法来获取外设的根目录，在此根目录上创建新的文件路径。
        if (!file.mkdirs()) {
            Log.e("TAG", "Directory not created");
        }
        return file;
    }
    /*
    * 创建私有文件
    * */
    public File getAlbumStorageDir(Context context, String fileName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), fileName);//如果不需要指定类型，可以传递null到getExternalFilesDir()中，它返回的是您的应用程序的在外部存储的根目录。
        if (!file.mkdirs()) {
            Log.e("TAG", "Directory not created");
        }
        return file;
    }
    /*
    * 查询剩余空间
      如果你知道需要保存的文件的大小，你需要保证有足够的空间来存储这些文件。
      可以通过getFreeSpace() 或者 getTotalSpace() 来获取文件的大小。
    *
    *
    * */


}
