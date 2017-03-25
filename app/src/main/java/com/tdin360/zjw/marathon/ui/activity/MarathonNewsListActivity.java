package com.tdin360.zjw.marathon.ui.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.tdin360.zjw.marathon.R;
import com.tdin360.zjw.marathon.adapter.NewsListViewAdapter;
import com.tdin360.zjw.marathon.model.NewsModel;
import com.tdin360.zjw.marathon.utils.HttpUrlUtils;
import com.tdin360.zjw.marathon.utils.MarathonDataUtils;
import com.tdin360.zjw.marathon.utils.NetWorkUtils;
import com.tdin360.zjw.marathon.weight.RefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛事新闻
 * @author zhangzhijun
 */
public class MarathonNewsListActivity extends BaseActivity implements RefreshListView.OnRefreshListener{


    private TextView loadFail;
    private RefreshListView refreshListView;
    private List<NewsModel> newsModelList=new ArrayList<>();
    private NewsListViewAdapter newsListViewAdapter;
    private int pageNumber=1;
    private int totalPages;
    private TextView not_found;
    private KProgressHUD hud;
    private boolean isLoadFail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBarTitle("赛事新闻");
        showBackButton();
        initView();

    }

    private void initView() {


        this.loadFail = (TextView) this.findViewById(R.id.loadFail);
        this.refreshListView  = (RefreshListView) this.findViewById(R.id.listView);
        this.refreshListView.setOnRefreshListener(this);
        this.refreshListView.setOnItemClickListener(new MyListener());
        this.not_found = (TextView) this.findViewById(R.id.not_found);
        this.newsListViewAdapter  =new NewsListViewAdapter(newsModelList,this);
        this.refreshListView.setAdapter(this.newsListViewAdapter);
         initHUD();
         //加载数据
        loadData();


    }
    /**
     * 初始化提示框
     */
    private void initHUD(){

        //显示提示框
        this.hud = KProgressHUD.create(this);
        hud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        hud.setCancellable(true);
        hud.setAnimationSpeed(1);
        hud.setDimAmount(0.5f);

    }
    //加载数据(包括缓存数据和网络数据)
    private void loadData() {

        hud.show();

        /**
         * 判断网络是否处于可用状态
         */
        if (NetWorkUtils.isNetworkAvailable(this)) {

            //加载网络数据
            httpRequest();
        } else {

            hud.dismiss();
            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
            loadFail.setVisibility(View.VISIBLE);
            //获取缓存数据
            //如果获取得到缓存数据则加载本地数据


            //如果缓存数据不存在则需要用户打开网络设置

            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setMessage("网络不可用，是否打开网络设置");
            alert.setCancelable(false);
            alert.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //打开网络设置

                    startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));

                }
            });
            alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            alert.show();
        }
    }
    @Override
    public int getLayout() {
        return R.layout.activity_marathon_news;
    }


    /**
     * 请求网络数据
     *
     * 获取新闻数据列表
     */
    private void httpRequest(){

        RequestParams requestParams = new RequestParams(HttpUrlUtils.MARATHON_NewsOrNotice);
        requestParams.addQueryStringParameter("eventId","1");
        requestParams.addQueryStringParameter("newsOrNoticeName","赛事新闻");
        requestParams.addQueryStringParameter("PageNumber",pageNumber+"");
        requestParams.addBodyParameter("appKey",HttpUrlUtils.appKey);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {

                try {

                    JSONObject json  = new JSONObject(result);


                    totalPages = json.getInt("TotalPages");
                    JSONArray eventMessageList = json.getJSONArray("eventMessageList");

                    for(int i=0;i<eventMessageList.length();i++){
                        JSONObject o = (JSONObject) eventMessageList.get(i);


                        int id = o.getInt("Id");

                        newsModelList.add(new NewsModel(id, o.getString("MessageName"), o.getString("MessagePictureUrl"),HttpUrlUtils.EVENT_NEWS_OR_NOTICE_DETAILS+id, o.getString("CreateTimeStr")));

                    }
                    //加载成功隐藏
                    loadFail.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();

                    isLoadFail=true;
                    loadFail.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                isLoadFail=true;
                loadFail.setVisibility(View.VISIBLE);
                not_found.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

                hud.dismiss();
                if(!isLoadFail) {
                    //判断是否有数据
                    if (newsModelList.size() > 0) {

                        not_found.setVisibility(View.GONE);
                    } else {
                        not_found.setVisibility(View.VISIBLE);
                    }

                }
                refreshListView.hideHeaderView();
                refreshListView.hideFooterView();
                newsListViewAdapter.updateListView(newsModelList);

            }
        });

    }

    @Override
    public void onDownPullRefresh() {
        //刷新
        newsModelList.clear();
        pageNumber=1;
        httpRequest();

    }

    @Override
    public void onLoadingMore() {
        //下拉加载更多
        if(pageNumber<totalPages){

            pageNumber++;
            httpRequest();
        }else {

            refreshListView.hideFooterView();
        }
    }

    /**
     * 新闻列表点击事件
     */
    private class MyListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewsModel newsModel = (NewsModel) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MarathonNewsListActivity.this, ShowHtmlActivity.class);
                intent.putExtra("title", "赛事新闻");
                intent.putExtra("url", newsModel.getDetailUrl());
                startActivity(intent);


        }
    }
}