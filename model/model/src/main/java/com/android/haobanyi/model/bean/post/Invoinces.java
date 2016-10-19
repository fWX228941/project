package com.android.haobanyi.model.bean.post;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/5.
 *
 * @作者: 付敏
 * @创建日期：2016/08/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class Invoinces {
    List<Invoincesbean> Invoinces;// 添加到购物车

    public List<Invoincesbean> getInvoinces() {
        return Invoinces;
    }

    public void setInvoinces(List<Invoincesbean> invoinces) {
        Invoinces = invoinces;
    }

    public static class Invoincesbean {
        String Title;
        boolean IsSelected;

        public Invoincesbean(String title, boolean isSelected) {
            Title = title;
            IsSelected = isSelected;
        }


        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public boolean isSelected() {
            return IsSelected;
        }

        public void setIsSelected(boolean isSelected) {
            IsSelected = isSelected;
        }
    }
}
