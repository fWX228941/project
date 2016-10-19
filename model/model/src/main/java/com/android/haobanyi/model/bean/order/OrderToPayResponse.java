package com.android.haobanyi.model.bean.order;

import com.android.haobanyi.model.bean.BaseResponse;

import java.util.List;

/**
 * Created by fWX228941 on 2016/9/23.
 *
 * @作者: 付敏
 * @创建日期：2016/09/23
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */

public class OrderToPayResponse extends BaseResponse {
    /*================SubmitOrder========================*/
    //{"code":101,"message":"提交成功！","data":[30339]}
    private List<Integer> data;
    public List<Integer> getData() {
        return data;
    }
    public void setData(List<Integer> data) {
        this.data = data;
    }
    /*================SubmitOrder========================*/

}
