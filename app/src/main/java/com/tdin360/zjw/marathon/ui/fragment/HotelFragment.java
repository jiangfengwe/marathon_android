package com.tdin360.zjw.marathon.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.tdin360.zjw.marathon.R;
import com.tdin360.zjw.marathon.SingleClass;
import com.tdin360.zjw.marathon.WrapContentLinearLayoutManager;
import com.tdin360.zjw.marathon.adapter.RecyclerViewBaseAdapter;
import com.tdin360.zjw.marathon.model.HH;
import com.tdin360.zjw.marathon.model.HotelListBean;
import com.tdin360.zjw.marathon.ui.activity.HotelDetailsActivity;
import com.tdin360.zjw.marathon.utils.HttpUrlUtils;
import com.tdin360.zjw.marathon.utils.NetWorkUtils;
import com.tdin360.zjw.marathon.utils.ToastUtils;
import com.tdin360.zjw.marathon.weight.ErrorView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 酒店预订
 * Created by admin on 17/7/26.
 */


public class HotelFragment extends BaseFragment {
    @ViewInject(R.id.layout_lading)
    private RelativeLayout layoutLoading;
    @ViewInject(R.id.iv_loading)
    private ImageView ivLoading;

    @ViewInject(R.id.cb_hotel_check)
    private CheckBox checkBoxHot;
    @ViewInject(R.id.rv_hotel)
    private RecyclerView rvHotel;
    private List<String> list=new ArrayList<>();
    private RecyclerViewBaseAdapter adapter;

    private int totalPage;
    private int pageIndex=1;
    private int pageSize=10;
    private boolean flag=false;
    @ViewInject(R.id.springView)
    private SpringView springView;
    @ViewInject(R.id.errorView)
    private ErrorView mErrorView;

    private int lowPrice;
    private int highPrice;
    private int index;

    private List<HotelListBean.ModelBean.BJHotelStyleListModelBean> bjHotelStyleListModel=new ArrayList<>();
    private List<HotelListBean.ModelBean.BJHotelListModelBean> bjHotelListModel=new ArrayList<>();
   /* private List<HH.ModelBean.BJHotelStyleListModelBean> bjHotelStyleListModel=new ArrayList<>();
    private List<HH.ModelBean.BJHotelListModelBean> bjHotelListModel=new ArrayList<>();*/
    ImageOptions imageOptions;

    public static HotelFragment newInstance(){

        return new HotelFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_hotel_list,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageOptions= new ImageOptions.Builder().setFadeIn(true)//淡入效果
                //ImageOptions.Builder()的一些其他属性：
                //.setCircular(true) //设置图片显示为圆形
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //.setSquare(true) //设置图片显示为正方形
                //.setCrop(true).setSize(130,130) //设置大小
                //.setAnimation(animation) //设置动画
                .setFailureDrawableId(R.drawable.event_bg) //设置加载失败的动画
                // .setFailureDrawableId(int failureDrawable) //以资源id设置加载失败的动画
                //.setLoadingDrawable(Drawable loadingDrawable) //设置加载中的动画
                .setLoadingDrawableId(R.drawable.event_bg) //以资源id设置加载中的动画
                .setIgnoreGif(false) //忽略Gif图片
                //.setRadius(10)
                .setUseMemCache(true).build();
         layoutLoading.setVisibility(View.VISIBLE);
        ivLoading.setBackgroundResource(R.drawable.loading_before);
        AnimationDrawable background =(AnimationDrawable) ivLoading.getBackground();
        background.start();
        initNet();
        initView();
        initTop();

    }

    private void initTop() {
        checkBoxHot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    final View popupView = getActivity().getLayoutInflater().inflate(R.layout.item_hotel_mydialog, null);
                    final PopupWindow window = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout layout = (LinearLayout) popupView.findViewById(R.id.layout_choose_room);
                    TextView tvSure = (TextView) popupView.findViewById(R.id.tv_room_sure);
                    final RadioGroup rgPrice = (RadioGroup) popupView.findViewById(R.id.rg_price_level);
                    rgPrice.check(index);
                    rgPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                            switch (checkedId){
                                case R.id.rg_room_one:
                                    lowPrice = 0;
                                    highPrice = 400;
                                    index=checkedId;
                                    //Log.d("hotelprice", "initData: "+lowPrice+"price"+ highPrice);
                                    break;
                                case R.id.rg_room_two:
                                    lowPrice=400;
                                    highPrice=800;
                                    index=checkedId;
                                    break;
                                case R.id.rg_room_three:
                                    lowPrice=800;
                                    highPrice=1000;
                                    index=checkedId;
                                    break;
                                case R.id.rg_room_four:
                                    lowPrice=1000;
                                    highPrice=2000;
                                    index=checkedId;
                                    break;
                            }
                            Log.d("hotelprice", "initData: "+lowPrice+"price"+ highPrice);
                        }

                    });
                    tvSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            window.dismiss();
                            initData(1);
                            checkBoxHot.setChecked(false);
                        }
                    });

                    TextView tvClass = new TextView(getActivity());
                    layout.removeAllViews();
                    Log.d("bjHotelStyleListModel", "onCheckedChanged: "+bjHotelStyleListModel.size());
                    for (int i = 0; i < bjHotelStyleListModel.size(); i++) {
                        String name = bjHotelStyleListModel.get(i).getName();
                        tvClass.setBackgroundResource(R.drawable.hotel_text_bg_selector);
                        tvClass.setText(name);
                        tvClass.setTextColor(Color.RED);
                        tvClass.setGravity(Gravity.CENTER);
                        tvClass.setTextSize(14);
                        layout.addView(tvClass);
                    }

                    // TODO: 2016/5/17 设置动画
                    // window.setAnimationStyle(R.style.popup_window_anim);
                    // TODO: 2016/5/17 设置背景颜色
                    window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
                    // TODO: 2016/5/17 设置可以获取焦点
                    // window.setFocusable(true);
                    // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
                    window.setOutsideTouchable(true);
                    //popupWindow消失是下拉图片变化
                    window.setTouchInterceptor(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            //点击PopupWindow以外区域时PopupWindow消失
                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                // checkBoxHot.setChecked(false);
                            }
                            return false;
                        }
                    });
                    // TODO：更新popupwindow的状态
                    window.update();
                    // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
                    window.showAsDropDown(checkBoxHot, 0, 0);
                }else{
                    // checkBoxHot.setClickable(true);
                   /* viewBg.setVisibility(View.GONE);
                    imageViewBack.setImageResource(R.drawable.back);
                    toolbarBack.setBackgroundColor(Color.parseColor("#ff621a"));
                    tvTitle.setTextColor(Color.WHITE);*/
                }
            }
        });
    }

    private void initNet() {
        //加载失败点击重试
        mErrorView.setErrorListener(new ErrorView.ErrorOnClickListener() {
            @Override
            public void onErrorClick(ErrorView.ViewShowMode mode) {
                switch (mode){
                    case NOT_NETWORK:
                        initData(0);
                        break;

                }
            }
        });
        //判断网络是否处于可用状态
        if(NetWorkUtils.isNetworkAvailable(getActivity())){
            //加载网络数据
            initData(0);
        }else {
            layoutLoading.setVisibility(View.GONE);
            //如果缓存数据不存在则需要用户打开网络设置
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("网络不可用，是否打开网络设置");
            alert.setCancelable(false);
            alert.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //打开网络设置

                    startActivity(new Intent( android.provider.Settings.ACTION_SETTINGS));

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
    private void initView() {
        for (int i = 0; i <9 ; i++) {
            list.add(""+i);
        }
        adapter=new RecyclerViewBaseAdapter<HotelListBean.ModelBean.BJHotelListModelBean>(getActivity(),bjHotelListModel,R.layout.item_hotel_rv) {
            @Override
            protected void onBindNormalViewHolder(NormalViewHolder holder, HotelListBean.ModelBean.BJHotelListModelBean model) {
                ImageView hotelPic = (ImageView) holder.getViewById(R.id.iv_hotel_pic);
                holder.setText(R.id.tv_hotel_name,model.getName());
                holder.setText(R.id.tv_hotel_level,model.getHotelStyle());
                holder.setText(R.id.tv_hotel_price,model.getLowestPrice()+"");
                holder.setText(R.id.tv_hotel_score,model.getScoring()+"分");
                x.image().bind(hotelPic,model.getPictureUrl(),imageOptions);
            }
        };
        rvHotel.setAdapter(adapter);
        rvHotel.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        adapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HotelListBean.ModelBean.BJHotelListModelBean bjHotelListModelBean = bjHotelListModel.get(position);
                String hotelId = bjHotelListModelBean.getId() + "";
                Intent intent=new Intent(getActivity(),HotelDetailsActivity.class);
                intent.putExtra("hotelId",hotelId);
                SingleClass.getInstance().setHotelId(hotelId);
                startActivity(intent);
            }
        });
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springView.onFinishFreshAndLoad();
                bjHotelListModel.clear();
                pageIndex=1;
                initData(0);
            }

            @Override
            public void onLoadmore() {
                springView.onFinishFreshAndLoad();
                if(totalPage<=pageIndex){
                    return;
                }
                if(totalPage>pageIndex){
                    pageIndex++;
                    initData(0);
                }
            }
        });
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
    }
   /* private void initToolbar() {
        //showBack(toolbarBack,imageViewBack);
        //checkBoxHot.setClickable(true);
        checkBoxHot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkBoxHot.setClickable(false);
                if(isChecked){
                    //viewBg.setVisibility(View.VISIBLE);
                    //rvHotel.setClickable(false);
                    //imageViewBack.setImageResource(R.drawable.back_black);
                    //toolbarBack.setBackgroundColor(Color.WHITE);
                    //tvTitle.setTextColor(Color.BLACK);
                    final View popupView = getActivity().getLayoutInflater().inflate(R.layout.item_hotel_mydialog, null);
                    final PopupWindow window = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout layout = (LinearLayout) popupView.findViewById(R.id.layout_choose_room);
                    TextView tvSure = (TextView) popupView.findViewById(R.id.tv_room_sure);
                    final RadioGroup rgPrice = (RadioGroup) popupView.findViewById(R.id.rg_price_level);
                    rgPrice.check(index);
                    rgPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                            switch (checkedId){
                                case R.id.rg_room_one:
                                    lowPrice = 0;
                                    highPrice = 400;
                                    index=checkedId;
                                    //Log.d("hotelprice", "initData: "+lowPrice+"price"+ highPrice);
                                    break;
                                case R.id.rg_room_two:
                                    lowPrice=400;
                                    highPrice=800;
                                    index=checkedId;
                                    break;
                                case R.id.rg_room_three:
                                    lowPrice=800;
                                    highPrice=1000;
                                    index=checkedId;
                                    break;
                                case R.id.rg_room_four:
                                    lowPrice=1000;
                                    highPrice=2000;
                                    index=checkedId;
                                    break;
                            }
                            Log.d("hotelprice", "initData: "+lowPrice+"price"+ highPrice);
                        }

                    });
                    tvSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            window.dismiss();
                            initData(1);
                            //checkBoxHot.setClickable(true);
                            checkBoxHot.setChecked(false);
                            viewBg.setVisibility(View.GONE);
                            imageViewBack.setImageResource(R.drawable.back);
                            toolbarBack.setBackgroundColor(Color.parseColor("#ff621a"));
                            tvTitle.setTextColor(Color.WHITE);
                        }
                    });

                    TextView tvClass = new TextView(getApplicationContext());
                    layout.removeAllViews();
                    Log.d("bjHotelStyleListModel", "onCheckedChanged: "+bjHotelStyleListModel.size());
                    for (int i = 0; i < bjHotelStyleListModel.size(); i++) {
                        String name = bjHotelStyleListModel.get(i).getName();
                        tvClass.setBackgroundResource(R.drawable.hotel_text_bg_selector);
                        tvClass.setText(name);
                        tvClass.setTextColor(Color.RED);
                        tvClass.setGravity(Gravity.CENTER);
                        tvClass.setTextSize(14);
                        layout.addView(tvClass);
                        // addView(layout, tvClass, i);
                    }

                    // TODO: 2016/5/17 设置动画
                    // window.setAnimationStyle(R.style.popup_window_anim);
                    // TODO: 2016/5/17 设置背景颜色
                    window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
               *//* if(window.isShowing()){
                    checkBoxHot.setChecked(false);
                    checkBoxHot.setClickable(false);
                }else{
                    checkBoxHot.setChecked(true);


                }*//*
                    // TODO: 2016/5/17 设置可以获取焦点
                    // window.setFocusable(true);
                    // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
                    window.setOutsideTouchable(true);
                    //popupWindow消失是下拉图片变化
                    window.setTouchInterceptor(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            //点击PopupWindow以外区域时PopupWindow消失
                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                // checkBoxHot.setChecked(false);
                            }
                            return false;
                        }
                    });
                    // TODO：更新popupwindow的状态
                    window.update();
                    // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
                    window.showAsDropDown(checkBoxHot, 0, 20);
                }else{
                    // checkBoxHot.setClickable(true);
                    viewBg.setVisibility(View.GONE);
                    imageViewBack.setImageResource(R.drawable.back);
                    toolbarBack.setBackgroundColor(Color.parseColor("#ff621a"));
                    tvTitle.setTextColor(Color.WHITE);
                }
            }
        });
    }*/

    private void initData(int i) {
        // Log.d("hotelprice", "initData: "+lowPrice+"price"+ highPrice);
        String eventId = SingleClass.getInstance().getEventId();
        if(i==1){
            bjHotelListModel.clear();
        }
        RequestParams params=new RequestParams(HttpUrlUtils.HOTEL);
        params.addBodyParameter("appKey",HttpUrlUtils.appKey);
        params.addBodyParameter("pageSize",""+pageSize);
        params.addBodyParameter("pageIndex",""+pageIndex);
        params.addBodyParameter("eventId",eventId);
        params.addBodyParameter("styleId","");
        params.addBodyParameter("lowPrice",lowPrice+"");
        params.addBodyParameter("highPrice",highPrice+"");
        params.setConnectTimeout(5000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("hotel", "onSuccess: " + result);
                Gson gson = new Gson();
                HotelListBean hotelListBean = gson.fromJson(result, HotelListBean.class);
                //HH hotelListBean = gson.fromJson(result, HH.class);
                boolean state = hotelListBean.isState();
                if(state){
                    HotelListBean.ModelBean model = hotelListBean.getModel();
                    totalPage=model.getTotalPages();
                    bjHotelListModel.addAll(model.getBJHotelListModel());
                    bjHotelStyleListModel= model.getBJHotelStyleListModel();
                    if(bjHotelListModel.size()<=0){
                        mErrorView.show(rvHotel,"暂时没有数据",ErrorView.ViewShowMode.NOT_DATA);
                    }else {
                        mErrorView.hideErrorView(rvHotel);
                    }
                }else{
                    ToastUtils.showCenter(getActivity(),hotelListBean.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mErrorView.show(rvHotel, "加载失败,点击重试", ErrorView.ViewShowMode.NOT_NETWORK);
                ToastUtils.showCenter(getActivity(), "网络不给力,连接服务器异常!");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                adapter.update(bjHotelListModel);
                layoutLoading.setVisibility(View.GONE);
                // hud.dismiss();

            }
        });
    }
}
