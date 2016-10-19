/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.haobanyi.core;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.haobanyi.model.bean.charge.ChargeBean;
import com.android.haobanyi.model.bean.contact.ContactBean;
import com.android.haobanyi.model.bean.foot.FootBean;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.mine.MyMessageBean;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.bean.order.OrderBean;
import com.android.haobanyi.model.bean.order.OrderParentBean;
import com.android.haobanyi.model.bean.post.AttrIds;
import com.android.haobanyi.model.bean.post.Orders;
import com.android.haobanyi.model.bean.redenvelop.RedEnvelopBean;
import com.android.haobanyi.model.bean.shopping.ShoppingCartBean;
import com.android.haobanyi.model.bean.shopping.conformorder.RedEnvelopeBean;

import com.android.haobanyi.model.bean.shopping.order.Carts;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.EvaBean;
import com.android.haobanyi.model.bean.shopping.store.ShopAcBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.bean.userpoint.UserPointBean;
import com.android.haobanyi.model.bean.voucher.VoucherBean;
import com.android.haobanyi.model.bean.shopping.store._ShopRelatedServiceBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;

/**
 * 接收app层的各种Action
 *
 * @author fumin
 * @date 16/3/24
 * @version 1.0
 */
public interface AppAction extends Parcelable {
    public void login(String loginName, String password,String ip, ActionCallbackListener<LoginResponseResult> listener);
    public void loginByPhone(String loginName, String code,String ip, ActionCallbackListener<LoginResponseResult> listener);
    /**
     * 发送验证码
     *
     * @param phoneNum 手机号
     * @param listener 回调监听器
     * @对应的接口名：GetSmsOrEmailCodeIdent
     * 返回结果：
     *  1. 101（发送成功）
     *  2. 104（发送过于频繁）
     *  3. 105（发送失败手机或邮箱格式不正确）
     *  4. 103（异常信息）
     *
     */
    public void sendSmsCode(String phoneNum, ActionCallbackListener<ResponseCode> listener);

    /**
     * 注册
     * @param phoneNum 用户名/手机号
     * @param password 登录密码
     * @param code     短信或者邮箱验证码验证码
     * @param listener 回调监听器
     * 还差一个参数：Account{strOperateIP:手机访问IP}
     *       如果我无法提供参数是不是就无法访问了？
     * 返回：
     *  1. 101（注册成功）
     *  2. 104（注册失败手机号或邮箱已存在）
     *  3. 105（注册失败手机或邮箱验证码不正确）
     *  4. 103（注册出错）
     */
    public void register(String phoneNum, String code,String password, String ip, ActionCallbackListener<ResponseCode> listener);





    /**
     * 找回密码
     *
     * @param phoneNum 手机号
     * @param code     验证码
     * @param resetPassword 重置密码
     * @param listener  回调监听器
     * 对应接口   findPwd
     *
     */
    public void findPwd(String phoneNum, String resetPassword, String code, ActionCallbackListener<ResponseCode>
            listener);

    public void modifypwd( String OldPassword, String NewPassword, ActionCallbackListener<ResponseCode>
            listener);

    public void addPlatformConsult( String content, String phone, ActionCallbackListener<ResponseCode>
            listener);

    public void receiveVouchers( String voucherscode, ActionCallbackListener<ResponseCode>
            listener);
    public void ReceiveVouchers( long tempId, ActionCallbackListener<ResponseCode>
            listener);//不带卡密
    /**
     * 券列表
     *
     * @param currentPage 当前页数
     * @param listener    回调监听器
     * 默认是返回数据的
     */
/*    public void listCoupon(int currentPage, ActionCallbackListener<List<CouponBO>> listener);*/
    /*
    * 两个参数分别是当前的页面和当前页所能加载的最大item数
    *
    * */
    public void getProductListByNormalSort(int currentPage, int PageSize, ActionCallbackTripleListener<List<SortDataBean>,String,String>
            listener);
    /*这个关联很多，还不敢乱改*/
    public void getProductListByNormalSort01(int currentPage, int PageSize, ActionCallbackListener<List
            <SortDataBean>> listener);

    public void getOrderList(Map<String, Object> params,String orderType, ActionCallbackQuadrupleListener<List<OrderParentBean>,String,String,String>
            listener);
    public void addAssessment(Map<String, Object> params,ActionCallbackListener<ResponseCode> listener);
    public void  getToken(ActionCallbackListener<LoginResponseResult> listener);
    public void  getAssessmentCount(long productId,ActionCallbackNListener<String> listener);

    public void  getUserInfo(ActionCallbackListener<UserBean.DataBean> listener);
    public void  GetUserCapital(ActionCallbackDoubleListener<List<ChargeBean>,String> listener);
    public void  getUserPoint(ActionCallbackDoubleListener<List<UserPointBean>,String> listener);
    public void  getOrderCount(String orderType,Integer PageNo);
    public void  getFavoriteProductList(int currentPage,ActionCallbackTripleListener<List<SortDataBean>,String,String>
            listener); //这个还是看下，免得有问题, int PageSize
    public void  getFavoriteProductCount(int PageNo,ActionCallbackListener<String> listener);
    public void  getUserMessageList(String messageType, int PageNo, int PageSize,ActionCallbackListener<List<MyMessageBean>> listener);
    public void  getFavoriteShopList(int currentPage,ActionCallbackTripleListener<List<ShopBean>,String,String>
            listener);
    public void  getFavoriteShopCount(int PageNo,ActionCallbackListener<String> listener);
    public void  submitPayment(String  OrderIds,ActionCallbackListener<LoginResponseResult.DataBean> listener);
    public void  getProduct(long productId,ActionCallbackListener<ProductDetailsBean>
    listener);/*暂时设计的用户无关*/
    public void  getProduct01(long productId,ActionCallbackFivefoldListener<ProductDetailsBean,List<SatisfySendBean>,List<VouchersTemplateBean>,List<ShopAttrBean>, List<DetailsBean.DataBean.CLBean>>
            listener); //进化版，用于传递多个参数
    /*long和Long的差别在于一个是基本类型，一个是对象类型*/
    public void  getProductSatisfySend(long productId,ActionCallbackListener<List<SatisfySendBean>>
            listener);
    public void  getVouchersTemplate(long productId,ActionCallbackListener<List<VouchersTemplateBean>>
            listener);
    public void  getShopAttr(long productId,ActionCallbackListener<List<ShopAttrBean>>
            listener);
    public void getAssessmentList(long productID,int assessType,int pageNo,int pageSize, ActionCallbackTripleListener<List<EvaBean>,Integer,Integer>
            listener);//分页的
    public void getChargeList(int pageNo,int pageSize, ActionCallbackTripleListener<List<ChargeBean>,Integer,Integer>
            listener);//分页的
    public void getRedEnvelopeList(int pageNo,int pageSize, ActionCallbackTripleListener<List<RedEnvelopBean>,Integer,Integer>
            listener);//分页的


    public void getMyFootList(int pageNo,int pageSize, ActionCallbackTripleListener<List<FootBean>,Integer,Integer>
            listener);//分页的

    public void getRedEnvelopeTempList(int pageNo,int pageSize, ActionCallbackTripleListener<List<RedEnvelopBean>,Integer,Integer>
            listener);//分页的




    public void getVoucherList(int pageNo,int pageSize,ActionCallbackListener<List<VoucherBean>> listener);//分页的
    public void getProductListByHotSort(int currentPage, int PageSize);
    public void getShop(long shopId);
    //非用户相关，参考这个
    public void getAreaList();
    public void deleteCartSelectedItem(long ProductId, long GroupSellID,ActionCallbackListener<String> listener);
    public void saveGeneralInvoice(String title,boolean isSelected ,ActionCallbackListener<ResponseCode> listener);
    public void saveVatInvoice( Map<String, String> params ,ActionCallbackListener<ResponseCode> listener);
    public void bindEmail( Map<String, String> params ,ActionCallbackListener<ResponseCode> listener);
    public void bindMobile( Map<String, String> params ,ActionCallbackListener<ResponseCode> listener);
    public void applyOrderRefund( Map<String, Object> params ,ActionCallbackListener<ResponseCode> listener);
    public void uploadUserPhoto(String path,ActionCallbackListener<String> listener);
    public void setPayPwd(String AccountPwd,String PayPwd,ActionCallbackListener<ResponseCode> listener);
    public void modifyPayPwd( String AccountPwd,String ValidateCode,String PayPwd ,ActionCallbackListener<ResponseCode> listener);




    public void addCartItem(long ShopId,long ProductId,long GroupSellID,int Quantity,long districtID, List<Long> AttributeIdList, ActionCallbackListener<ResponseCode> listener);
    public void selectCartOtherService(long productId,long groupSellId,List< AttrIds.AttrIdsbean > list , ActionCallbackListener<ResponseCode> listener);


    public void addCartItemOfBuyNow(long ShopId,long ProductId,long GroupSellID,int Quantity,long districtID, List<Long> AttributeIdList, ActionCallbackListener<ResponseCode> listener);


    public void getShopwithoutId(long shopId,ActionCallbackDoubleListener<ShopBean,List<SortDataBean>>
            listener);//包含热门服务和店铺信息
    public void getShopProductListByNormalSort(long shopId,int currentPage, int PageSize, ActionCallbackTripleListener<List<_ShopRelatedServiceBean>,String,String>
            listener);
    public void getShopProductListByHotSort(long shopId,int currentPage, int PageSize,ActionCallbackListener<List<_ShopRelatedServiceBean>> listener);
    public void getShopActivity(long shopId,ActionCallbackListener<List<ShopAcBean>> listener);
    public void getShopVouchersList(long shopId,ActionCallbackListener<List<VoucherBean>> listener);
    public void searchProductByKeyword(String keyword,int currentPage, int PageSize,int sortType,boolean sortMode, ActionCallbackTripleListener<List<SortDataBean>,String,String>
            listener);
    public void searchShopByKeyword(String keyword,int currentPage, int PageSize, ActionCallbackTripleListener<List<ShopBean>,String,String>
            listener);
    public void getShopListByNormalSort(int currentPage, int PageSize, ActionCallbackTripleListener<List<ShopBean>,String,String>
            listener);
    public void getShopListByHotSort(int currentPage, int PageSize, ActionCallbackTripleListener<List<ShopBean>,String,String>
            listener);
    public void getOrder(long orderId,ActionCallbackListener<OrderBean> listener);
    public void confirmOrder(long orderId,ActionCallbackListener<ResponseCode> listener);
    public void closeOrder(long orderId,ActionCallbackListener<ResponseCode> listener);
    public void getShoppingCart(ActionCallbackFivefoldListener <List<Map<String,Object>> , List<List<Map<String, Object>>>,String,String,Boolean>  listener);

    public void receiveRedEnvelope(long tempId,ActionCallbackListener<ResponseCode> listener);

    public void getContactList(ActionCallbackListener<List<ContactBean>> listener);
    public void delContact(long contactManagerId ,ActionCallbackListener<ResponseCode> listener);
    public void setDefaultContact(long contactManagerId ,ActionCallbackListener<ResponseCode> listener);

    public void getVerifyShoppingCart(ActionCallbackSixfoldListener <List<Map<String,Object>> , List<List<Map<String, Object>>>,ContactBean,List<RedEnvelopeBean>,ShoppingCartBean.DataBean,Orders.OrdersBean>  listener);//获取确认后的购物车列表
    public void selectCartItem(long productId,ActionCallbackListener<String> listener);
    public void cancelCartItem(long productId,ActionCallbackListener<String> listener);
    /*public void deleteCartItem(Long productId);*/

    public void selectCartGroupItem(long groupSellId,ActionCallbackListener<String> listener);
    public void cancelCartGroupItem(long groupSellId,ActionCallbackListener<String> listener);
    public void selectCartShopItem(long shopId,ActionCallbackListener<String> listener);
    public void cancelCartShopItem(long shopId,ActionCallbackListener<String> listener);

    /*接下来的接口，一切都仿照这个来进行，失败了就重新写一遍*/
    public void selectCartAllItem(ActionCallbackListener<String> listener);
    public void cancelCartAllItem(ActionCallbackListener<String> listener);
    public void updateCartQuantity(long productId,int quantity,ActionCallbackListener<String> listener);
    public void updateCartGroupSellQuantity(long groupSellId,int quantity,ActionCallbackListener<String> listener);
    public void saveCartFavoriteProduct(List<Carts.Cartbean03 > list,ActionCallbackListener<String> listener);
    public void submitOrder(String redId,Orders.OrdersBean params,ActionCallbackListener<List<Integer>> listener);
    public void payByCapital(String orderPayId, String payPwd,ActionCallbackListener<ResponseCode> listener);
    /*List<Carts.Cartbean01 > list01,List<Carts.Cartbean02 > list02,*/
    public void deleteAllCartSelectedItem(List<Carts.Cartbean01 > list01,List<Carts.Cartbean02 > list02,ActionCallbackListener<String> listener);
    public void deleteFavProduct(long productId ,ActionCallbackListener<ResponseCode> listener);
    public void favProduct(long productId ,ActionCallbackListener<ResponseCode> listener);
    public void deleteFavShop(long shopId,ActionCallbackListener<ResponseCode> listener);
    public void favShop(long shopId,ActionCallbackListener<ResponseCode> listener);
    /*业务说白了无非就是增删改查，确实也没有什么难的，无非就是尽可能把各种主流的场景给考虑到，数据格式进行转化，异常情况友好处理就是了，并且保证一个数据流，一个逻辑流正确，至于性能的优化，代码模式的优化，方案的优化，这个
    * 就是要靠多思考，多总结，多积累，nothing is impossible ,最不怕的就是逻辑和悟性了，哈哈。*/

    public void addContact( Map<String, String> params ,ActionCallbackListener<ResponseCode> listener);//获取列表饿才是才会有的
    public void editContact( Map<String, String> params ,ActionCallbackListener<ResponseCode> listener);//获取列表饿才是才会有的

    public void authLogin( Map<String, String> params ,ActionCallbackListener<UserBean.DataBean> listener);
    public void bindUser( Map<String, String> params ,ActionCallbackListener<UserBean.DataBean> listener);

    public void cancelOrder( long orderId ,String reason,ActionCallbackListener<ResponseCode> listener);//获取列表饿才是才会有的


    public void EditContact( Map<String, String> params ,ActionCallbackListener<ResponseCode> listener);
    public void qrCodeLogin(String logintoken,String nsts,ActionCallbackListener<ResponseCode> listener);//分页的





    @Override
    int describeContents();

    @Override
    void writeToParcel(Parcel dest, int flags);


}
