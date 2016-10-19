package com.android.haobanyi.model.bean.shopping.product;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/6.
 *
 * @作者: 付敏
 * @创建日期：2016/07/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：由getProduct获取 服务详情
 */
public class DetailsBean {
    /**
     * code : 101
     * message : 获取成功！
     * data : {"Product":{"ProductID":23,"ProductName":"香港公司审计","ImagePath":"http://www.haobanyi
     * .com/Storage/selleradmin/1/Album/201607061501591429151.jpg","MinPrice":7800,"DiscountPrice":7800,"OrderNum":0,
     * "MobileDes":null,"IsRecommend":1,"IsHot":0,"ShopAttList":[]},"Shop":{"ShopID":1,"ShopName":"好办易事务所",
     * "ShopLogo":"http://www.haobanyi.com/Storage/selleradmin/1/Logo/201606281523061706575s.png",
     * "ShopAdd":"高新技术产业园R2-A北门203","ComprehensiveScore":0},"SSList":[],"VTList":[],"GSList":[],"CityList":[]}
     */

    private int code;
    private String message;
    /**
     * Product : {"ProductID":23,"ProductName":"香港公司审计","ImagePath":"http://www.haobanyi
     * .com/Storage/selleradmin/1/Album/201607061501591429151.jpg","MinPrice":7800,"DiscountPrice":7800,"OrderNum":0,
     * "MobileDes":null,"IsRecommend":1,"IsHot":0,"ShopAttList":[]}
     * Shop : {"ShopID":1,"ShopName":"好办易事务所","ShopLogo":"http://www.haobanyi
     * .com/Storage/selleradmin/1/Logo/201606281523061706575s.png","ShopAdd":"高新技术产业园R2-A北门203","ComprehensiveScore":0}
     * SSList : []
     * VTList : []
     * GSList : []
     * CityList : []
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * 网页测试数据：http://www.haobanyi.com/Product/Items?LproductID=23
         * ProductID : 23
         * ProductName : 香港公司审计
         * ImagePath : http://www.haobanyi.com/Storage/selleradmin/1/Album/201607061501591429151.jpg
         * MinPrice : 7800.0
         * DiscountPrice : 7800.0
         * OrderNum : 0
         * MobileDes : null
         * IsRecommend : 1
         * IsHot : 0
         * ShopAttList : []
         */

        private ProductBean Product;
        /**
         * ShopID : 1
         * ShopName : 好办易事务所
         * ShopLogo : http://www.haobanyi.com/Storage/selleradmin/1/Logo/201606281523061706575s.png
         * ShopAdd : 高新技术产业园R2-A北门203
         * ComprehensiveScore : 0.0
         */

        private ShopBean Shop;
        private List<SSBean> SSList;
        private List<VTBean> VTList;
        private List<GSBean> GSList;
        private List<CLBean> CityList;

        public ProductBean getProduct() {
            return Product;
        }

        public void setProduct(ProductBean Product) {
            this.Product = Product;
        }

        public ShopBean getShop() {
            return Shop;
        }

        public void setShop(ShopBean Shop) {
            this.Shop = Shop;
        }

        public List<SSBean> getSSList() {
            return SSList;
        }

        public void setSSList(List<SSBean> SSList) {
            this.SSList = SSList;
        }

        public List<VTBean> getVTList() {
            return VTList;
        }

        public void setVTList(List<VTBean> VTList) {
            this.VTList = VTList;
        }

        public List<GSBean> getGSList() {
            return GSList;
        }

        public void setGSList(List<GSBean> GSList) {
            this.GSList = GSList;
        }

        public List<CLBean> getCityList() {
            return CityList;
        }

        public void setCityList(List<CLBean> CityList) {
            this.CityList = CityList;
        }

        public static class ProductBean {
            private long ProductID;
            private String ProductName;
            private String ImagePath;
            private double MinPrice;
            private double DiscountPrice;
            private int OrderNum;
            private Object MobileDes;
            private int IsRecommend;
            private int IsHot;


            public String getWebDes() {
                return WebDes;
            }

            public void setWebDes(String webDes) {
                WebDes = webDes;
            }

            private String WebDes;


            public String getShareUrl() {
                return ShareUrl;
            }

            public void setShareUrl(String shareUrl) {
                ShareUrl = shareUrl;
            }
            public boolean isFav() {
                return IsFav;
            }

            public void setIsFav(boolean isFav) {
                IsFav = isFav;
            }
            /*========================getProduct01================================*/
            private String ShareUrl;//分享url”,
            private boolean IsFav;
            /*========================getProduct01================================*/

            private List<ShopAttrBean> ShopAttList;

            public long getProductID() {
                return ProductID;
            }

            public void setProductID(long ProductID) {
                this.ProductID = ProductID;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String ProductName) {
                this.ProductName = ProductName;
            }

            public String getImagePath() {
                return ImagePath;
            }

            public void setImagePath(String ImagePath) {
                this.ImagePath = ImagePath;
            }

            public double getMinPrice() {
                return MinPrice;
            }

            public void setMinPrice(double MinPrice) {
                this.MinPrice = MinPrice;
            }

            public double getDiscountPrice() {
                return DiscountPrice;
            }

            public void setDiscountPrice(double DiscountPrice) {
                this.DiscountPrice = DiscountPrice;
            }

            public int getOrderNum() {
                return OrderNum;
            }

            public void setOrderNum(int OrderNum) {
                this.OrderNum = OrderNum;
            }

            public Object getMobileDes() {
                return MobileDes;
            }

            public void setMobileDes(Object MobileDes) {
                this.MobileDes = MobileDes;
            }

            public int getIsRecommend() {
                return IsRecommend;
            }

            public void setIsRecommend(int IsRecommend) {
                this.IsRecommend = IsRecommend;
            }

            public int getIsHot() {
                return IsHot;
            }

            public void setIsHot(int IsHot) {
                this.IsHot = IsHot;
            }

            public List<ShopAttrBean> getShopAttList() {
                return ShopAttList;
            }

            public void setShopAttList(List<ShopAttrBean> ShopAttList) {
                this.ShopAttList = ShopAttList;
            }
            public static class ShopAttrBean {


                /**
                 * ShopAttrID : 1
                 * Name : 其他服务
                 * Price : 1
                 */

                private long ShopAttrID;
                private String AttrName;
                private String Price;

                public long getShopAttrID() {
                    return ShopAttrID;
                }

                public void setShopAttrID(long ShopAttrID) {
                    this.ShopAttrID = ShopAttrID;
                }

                public String getName() {
                    return AttrName;
                }

                public void setName(String Name) {
                    this.AttrName = Name;
                }

                public String getPrice() {
                    return Price;
                }

                public void setPrice(String Price) {
                    this.Price = Price;
                }
            }
        }

        public static class ShopBean {
            private long ShopID;
            private String ShopName;
            private String ShopLogo;
            private String ShopAdd;
            private double ComprehensiveScore;

            public long getShopID() {
                return ShopID;
            }

            public void setShopID(long ShopID) {
                this.ShopID = ShopID;
            }

            public String getShopName() {
                return ShopName;
            }

            public void setShopName(String ShopName) {
                this.ShopName = ShopName;
            }

            public String getShopLogo() {
                return ShopLogo;
            }

            public void setShopLogo(String ShopLogo) {
                this.ShopLogo = ShopLogo;
            }

            public String getShopAdd() {
                return ShopAdd;
            }

            public void setShopAdd(String ShopAdd) {
                this.ShopAdd = ShopAdd;
            }

            public double getComprehensiveScore() {
                return ComprehensiveScore;
            }

            public void setComprehensiveScore(double ComprehensiveScore) {
                this.ComprehensiveScore = ComprehensiveScore;
            }
        }

        public static class SSBean {
                private long SSendRuleID;
                private String Price;
                private String Money;

                public long getSSendRuleID() {
                    return SSendRuleID;
                }

                public void setSSendRuleID(long SSendRuleID) {
                    this.SSendRuleID = SSendRuleID;
                }

                public String getPrice() {
                    return Price;
                }

                public void setPrice(String Price) {
                    this.Price = Price;
                }

                public String getMoney() {
                    return Money;
                }

                public void setMoney(String Money) {
                    this.Money = Money;
                }

        }
        /*一个GS 是红包，一个CL 是城市列表*/
        public static class VTBean {
            private long VouchersTemplateID;
            private String Price;
            private String StartTime;
            private String EndTime;
            private String Limit;
            private boolean IsExist;

            public long getVouchersTemplateID() {
                return VouchersTemplateID;
            }

            public void setVouchersTemplateID(long VouchersTemplateID) {
                this.VouchersTemplateID = VouchersTemplateID;
            }


            public String getPrice() {
                return Price;
            }

            public void setPrice(String Price) {
                this.Price = Price;
            }

            public String getStartTime() {
                return StartTime;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public String getEndTime() {
                return EndTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public String getLimit() {
                return Limit;
            }

            public void setLimit(String Limit) {
                this.Limit = Limit;
            }

            public boolean isIsExist() {
                return IsExist;
            }

            public void setIsExist(boolean IsExist) {
                this.IsExist = IsExist;
            }
        }

        public static class GSBean {
            private long GroupSellID;
            private String Name; //这个真是麻烦
        }

        public static class CLBean {

                private String ProvinceID;
                private String Name;
                private List<CityListBean> CityList;

                public String getProvinceID() {
                    return ProvinceID;
                }

                public void setProvinceID(String ProvinceID) {
                    this.ProvinceID = ProvinceID;
                }

                public String getName() {
                    return Name;
                }

                public void setName(String Name) {
                    this.Name = Name;
                }

                public List<CityListBean> getCityList() {
                    return CityList;
                }

                public void setCityList(List<CityListBean> CityList) {
                    this.CityList = CityList;
                }

                public static class CityListBean {
                    private String CityID;
                    private String Name;
                    private String ProvinceID;
                    private List<DistrictListBean> DistrictList;

                    public String getCityID() {
                        return CityID;
                    }

                    public void setCityID(String CityID) {
                        this.CityID = CityID;
                    }

                    public String getName() {
                        return Name;
                    }

                    public void setName(String Name) {
                        this.Name = Name;
                    }

                    public String getProvinceID() {
                        return ProvinceID;
                    }

                    public void setProvinceID(String ProvinceID) {
                        this.ProvinceID = ProvinceID;
                    }

                    public List<DistrictListBean> getDistrictList() {
                        return DistrictList;
                    }

                    public void setDistrictList(List<DistrictListBean> DistrictList) {
                        this.DistrictList = DistrictList;
                    }

                    public static class DistrictListBean {
                        private String DistrictID;
                        private String Name;
                        private String CityID;

                        public String getDistrictID() {
                            return DistrictID;
                        }

                        public void setDistrictID(String DistrictID) {
                            this.DistrictID = DistrictID;
                        }

                        public String getName() {
                            return Name;
                        }

                        public void setName(String Name) {
                            this.Name = Name;
                        }

                        public String getCityID() {
                            return CityID;
                        }

                        public void setCityID(String CityID) {
                            this.CityID = CityID;
                        }
                    }
                }
        }
    }
    /*非0为套餐服务*/


}
