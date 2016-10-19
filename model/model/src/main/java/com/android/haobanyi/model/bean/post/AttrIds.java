package com.android.haobanyi.model.bean.post;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/23.
 *
 * @作者: 付敏
 * @创建日期：2016/08/23
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class AttrIds {

    public List<AttrIdsbean> getAttrIds() {
        return AttrIds;
    }

    public void setAttrIds(List<AttrIdsbean> attrIds) {
        AttrIds = attrIds;
    }

    List<AttrIdsbean> AttrIds;// 添加到购物车


    public static class AttrIdsbean {
        public long getShopAttrID() {
            return ShopAttrID;
        }

        public void setShopAttrID(long shopAttrID) {
            ShopAttrID = shopAttrID;
        }

        public AttrIdsbean(long shopAttrID) {
            ShopAttrID = shopAttrID;
        }

        long ShopAttrID;
    }

}
