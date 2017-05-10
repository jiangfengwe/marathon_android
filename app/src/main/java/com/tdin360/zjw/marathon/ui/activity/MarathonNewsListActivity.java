package com.tdin360.zjw.marathon.ui.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tdin360.zjw.marathon.R;
import com.tdin360.zjw.marathon.adapter.NewsListViewAdapter;
import com.tdin360.zjw.marathon.model.NewsModel;
import com.tdin360.zjw.marathon.utils.HttpUrlUtils;
import com.tdin360.zjw.marathon.utils.MarathonDataUtils;
import com.tdin360.zjw.marathon.utils.NetWorkUtils;
import com.tdin360.zjw.marathon.utils.db.impl.NewsServiceImpl;
import com.tdin360.zjw.marathon.weight.pullToControl.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛事新闻
 * @author zhangzhijun
 */
public class MarathonNewsListActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener{


    private TextView loadFail;
    private ListView refreshListView;
    private List<NewsModel> newsModelList=new ArrayList<>();
    private NewsListViewAdapter newsListViewAdapter;
    private int pageNumber=1;
    private int totalPages;
    private TextView not_found;
    private boolean isLoadFail;
    private PullToRefreshLayout pullToRefreshLayout;
    private NewsServiceImpl service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.service  = new NewsServiceImpl(getApplicationContext());
        setToolBarTitle("赛事新闻");
        showBackButton();
        initView();

    }

    private void initView() {


        this.loadFail = (TextView) this.findViewById(R.id.loadFail);
        this.pullToRefreshLayout = (PullToRefreshLayout) this.findViewById(R.id.pull_Layout);
        this.refreshListView  = (ListView) this.findViewById(R.id.listView);
        this.refreshListView.setOnItemClickListener(new MyListener());
        this.not_found = (TextView) this.findViewById(R.id.not_found);
        this.newsListViewAdapter  =new NewsListViewAdapter(newsModelList,this);
        this.refreshListView.setAdapter(this.newsListViewAdapter);
        this.pullToRefreshLayout.setOnRefreshListener(this);

         //加载数据
        loadData();


    }

    //加载数据(包括缓存数据和网络数据)
    private void loadData() {


        /**
         * 判断网络是否处于可用状态
         */
        if (NetWorkUtils.isNetworkAvailable(this)) {

            //加载网络数据
            pullToRefreshLayout.autoRefresh();
        } else {

            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();

            //获取缓存数据
            //如果获取得到缓存数据则加载本地数据
             newsModelList = service.getAllNews(MarathonDataUtils.init().getEventId());
             newsListViewAdapter.updateListView(newsModelList);

            //如果缓存数据不存在则需要用户打开网络设置

            if(newsModelList.size()==0) {
                loadFail.setVisibility(View.VISIBLE);
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
    private void httpRequest(final boolean isRefresh){

        RequestParams requestParams = new RequestParams(HttpUrlUtils.MARATHON_NewsOrNotice);
        requestParams.addQueryStringParameter("eventId",MarathonDataUtils.init().getEventId());
        requestParams.addQueryStringParameter("newsOrNoticeName","赛事新闻");
        requestParams.addQueryStringParameter("PageNumber",pageNumber+"");
        requestParams.addBodyParameter("appKey",HttpUrlUtils.appKey);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {

                try {
                    //删除缓存
                    service.deleteNews(MarathonDataUtils.init().getEventId());
                    if (isRefresh){

                        newsModelList.clear();

                    }

                    JSONObject json  = new JSONObject(result);


                    totalPages = json.getInt("TotalPages");
                    JSONArray eventMessageList = json.getJSONArray("eventMessageList");

                    for(int i=0;i<eventMessageList.length();i++){
                        JSONObject o = (JSONObject) eventMessageList.get(i);


                        int id = o.getInt("Id");
                        NewsModel newsModel = new NewsModel(id, o.getString("MessageName"), o.getString("MessagePictureUrl"), HttpUrlUtils.EVENT_NEWS_OR_NOTICE_DETAILS + id, o.getString("CreateTimeStr"));
                        newsModelList.add(newsModel);
                        service.addNews(newsModel);
                    }
                    //加载成功隐藏
                    loadFail.setVisibility(View.GONE);

                    if (isRefresh){

                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }else {
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    isLoadFail=true;
                    loadFail.setVisibility(View.VISIBLE);
                    if (isRefresh){

                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }else {
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                isLoadFail=true;
                loadFail.setVisibility(View.VISIBLE);
                not_found.setVisibility(View.GONE);
                if (isRefresh){

                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {


                    //判断是否有数据
                    if (newsModelList.size() > 0) {
                        loadFail.setVisibility(View.GONE);
                    //如果加载失败且没有数据
                    } else if(newsModelList.size()>0&&!isLoadFail){
                        not_found.setVisibility(View.GONE);
                    }else {

                        not_found.setVisibility(View.VISIBLE);
                    }


                newsListViewAdapter.updateListView(newsModelList);

            }
        });

    }



    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        pageNumber=1;
        httpRequest(true);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        //上拉加载更多
        if(pageNumber==totalPages){

          pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOT_MORE);

        }else if(pageNumber<totalPages){
            pageNumber++;
            httpRequest(false);

        }else {

            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOT_MORE);
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
                intent.putExtra("shareTitle",newsModel.getTitle());
                intent.putExtra("shareImageUrl",newsModel.getPicUrl());
                intent.putExtra("title", "赛事新闻");
                intent.putExtra("url", newsModel.getDetailUrl());
                startActivity(intent);


        }
    }
}
