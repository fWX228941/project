package com.android.haobanyi.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

import java.util.Objects;

/**
 * Created by fWX228941 on 2016/4/15.
 *
 * @作者: 付敏
 * @创建日期：2016/04/15
 * @邮箱：466566941@qq.com
 * @当前文件描述：广告栏轮播的网络图片加载视图
 */
public class NetworkImageHolderView implements Holder<Integer>{
    /*
    * 客户端：也就是把适配器的两个工作分流到了holder中
    * 1.先创建视图，return返回得到视图
    * 2.再加载视图跟新UI
    * */
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        /*
        * 创建视图也是两种方法：一种是通过Layout 静态
        * 一种是动态创建LayoutInflater layout = LayoutInflater.from(context);
        * 因为这里仅仅展示的是图片，所以直接代码创建
        * 客户端是这样调用的：view = holder.createView(container.getContext());
        * */
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return  imageView;
    }



    /*这里的数据类型参数也是设置为了object 超泛型类，可以修改参数类型，参数是可以向下转型的,为了与泛型参数同步*/
    @Override
   public void UpdateUI(Context context, int position, Integer data) {
        //网络图片加载框架来加载图片 ，其中data 就是第二个参数
        //客户端是这样调用的：holder.UpdateUI(container.getContext(), position, mDatas.get(position)); integer  数据往往是资源ID
        /*以ImageLoader为例子
        * imageView.setImageResource(R.drawable.ic_default_adimage); 当没有网络图片时加载
           ImageLoader.getInstance().displayImage(data,imageView);
        *
        * */
       imageView.setImageResource(data);
    }
}
