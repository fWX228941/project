package com.android.haobanyi.activity.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.haobanyi.R;
import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.activity.searching.KeywordSearchActivity;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;


/**
 * Created by fWX228941 on 2016/5/10.
 *
 * @作者: 付敏
 * @创建日期：2016/05/10
 * @邮箱：466566941@qq.com
 * @当前文件描述：搜索界面 包括两个部分，头部是一个SearchView 尾部是热门标签和搜索历史，后期会陆续添加
 * 参考设计：https://github.com/yetwish/CustomSearchView
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.id_textView_history_search)
    TextView idTextViewHistorySearch;
    @BindView(R.id.id_textView_tip)
    TextView idTextViewTip;
    @BindView(R.id.id_textView_hot_search)
    TextView idTextViewHotSearch;
    @BindView(R.id.id_textView_tip_02)
    TextView idTextViewTip02;
    @BindView(R.id.id_textView_back)
    ImageView idTextViewBack;
    @BindView(R.id.id_editText_search)
    ClearEditText idEditTextSearch;
    @BindView(R.id.id_textView_search)
    TextView idTextViewSearch;
    /**
     * 用户输入的数据源，少量就封装到Bundle中，并且传递给activity
     */
    Bundle userSearchData = null;

    /**
     * 热门搜索
     */
    @BindView(R.id.id_gridview_hotsearch)
    TagContainerLayout idGridviewHotsearch;

    /*
    *
    * 历史记录
    * */
    @BindView(R.id.id_gridview_historySearch)
    TagContainerLayout idGridviewHistorySearch;

    String user_input; //用户输入
    @Override
    protected int setLayout() {
        return R.layout.activity_search;

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        initHistoryData();
        initHotData();
    }

    /*
    * 业务逻辑：
    * 01.当用户没有搜索的时候显示热门搜索和历史搜索
    * 02.当用户搜索时，点击搜索，找到相关服务时才会显示分类以及list
    *
    * */
    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {

    }

    //初始化搜索关键字
    private void initHistoryData() {


        PreferenceUtil complexPreferences= PreferenceUtil.getSharedPreference(context, "preferences");
        List<String> user_input_list = new ArrayList<String>();
        user_input_list =complexPreferences.getListObject(Constants.USER_SEARCH_LIST,String.class);
        if (user_input_list.isEmpty()){
            idGridviewHistorySearch.setVisibility(View.GONE);
            idTextViewTip.setVisibility(View.VISIBLE);

        }else {
            idGridviewHistorySearch.setVisibility(View.VISIBLE);
            idTextViewTip.setVisibility(View.GONE);
            //String[] historyWords = (String[]) user_input_list.toArray(); Caused by: java.lang.ClassCastException: java.lang.Object[] cannot be cast to java.lang.String[]
            //转化错误这样会导致程序崩溃
            idGridviewHistorySearch.setTags(user_input_list);
            idGridviewHistorySearch.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text) {
                    //调转到搜索界面
                    user_input = text;// 这个逻辑需要修改下
                    intoSearch(user_input);
                }
                @Override
                public void onTagLongClick(final int position, String text) {
                    AlertDialog dialog = new AlertDialog.Builder(SearchActivity.this)
                            .setTitle("删除标签")
                            .setMessage("确定是否删除该标签!")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    idGridviewHistorySearch.removeTag(position);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
            });
        }

    }

    /*这样写又简洁，又漂亮*/
    private void initHotData() {
        String[] hotWords = getResources().getStringArray(R.array.array_list_history);
        for (String his:hotWords) {
            Log.d("SearchActivity", his);
        }
        idGridviewHotsearch.setTags(hotWords);
        idGridviewHotsearch.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                //调转到搜索界面
                user_input = text;// 这个逻辑需要修改下
                intoSearch(user_input);
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                AlertDialog dialog = new AlertDialog.Builder(SearchActivity.this)
                        .setTitle("删除标签")
                        .setMessage("确定是否删除该标签!")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                idGridviewHotsearch.removeTag(position);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });

    }

    @Override
    protected void registerEventListener() {

        //03.监听用户输入，并保存，点击搜索按钮时，把用户输入传递到搜索框中
        registerSearch();

    }
    //监听用户输入
    private void registerSearch() {
        idEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    idEditTextSearch.setText(str1);

                    idEditTextSearch.setSelection(start);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    @Override
    protected void registerBroadCastReceiver() {

    }

    @Override
    protected void saveState(Bundle outState) {

    }

    //以下是监听回调接口，当搜索框 文本改变时 触发的回调 ,更新自动补全数据，涉及到补全搜索算法


    /*
    * 1.补全搜索算法：简单的匹配算法，其匹配过程如下，
    *       不支持多词搜索，在数据库中匹配每个bean的【productName属性是否包含搜索词，包含则表示匹配
    *       否则不匹配，然后将所有匹配的bean显示到list列表中，只取结果集的前10项，并按距离排序
    *       更多添加一个上拉刷新或者下拉刷新机制
    * 2.更新数据   adapter.notifyDataSetChanged()
    *
    * 3.逻辑如下：
    *  01.从数据库中获取匹配数据  随时做好判空准备，效率更好，更加健硕性
    *  02.获取一定数量的数据后
    *  03.显示数据，更新数据
    *
    * */
    private void getAutoCompleteData(String text) {


    }


    /*存在一个刷新操作*/
    @OnClick(R.id.id_textView_tip_02)
    public void onClick() {
        /*01.这里存在一热门搜索的接口*/
    }


    @OnClick({R.id.id_textView_back, R.id.id_textView_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_textView_back:
                finish();
                SearchActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
                break;
            case R.id.id_textView_search:
                //携带用户数据的深入搜索
                user_input = idEditTextSearch.getText().toString();// 这个逻辑需要修改下
                intoSearch(user_input);
                break;
        }
    }

    private void intoSearch(String user_input) {
        //只有当用户完成按下搜索键时，用户输入的数据源才完成确定，这个时候就需要利用各种缓存机制把数据源给缓存下来。
        if (!TextUtils.isEmpty(user_input)){
            Bundle bundle = new Bundle();
            bundle.putString(Constants.USER_SEARCH, user_input);
            IntentUtil.gotoActivityWithData(SearchActivity.this, KeywordSearchActivity.class, bundle,false);  //false 保存页面，true不保存
        }

    }
}
