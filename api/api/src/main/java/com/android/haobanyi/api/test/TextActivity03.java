package com.android.haobanyi.api.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ListView;


import com.android.haobanyi.api.R;


/**
 * Created by fWX228941 on 2016/5/16.
 *
 * @作者: 付敏
 * @创建日期：2016/05/16
 * @邮箱：466566941@qq.com
 * @当前文件描述：测试上新下拉机制
 */
public class TextActivity03 extends Activity {
    /*
    *  预期鲜果：
    *  1.对于刷新强迫症的患者而言，刷新一次，发起调用一次，有变化的就即使更新，没有的就使用原来的图片，最主要的是心理作用
    *  2.下拉是加载更多机制，调用一次接口，
    *
    *
    * */
     Button btn_retrofit_simple_contributors;
    ListView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_03);
        btn_retrofit_simple_contributors =(Button)findViewById(R.id.test_button);




    }

}
