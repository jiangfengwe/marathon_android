package com.tdin360.zjw.marathon.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.maning.imagebrowserlibrary.MNImageBrowser;
import com.tdin360.zjw.marathon.EventBusClass;
import com.tdin360.zjw.marathon.R;
import com.tdin360.zjw.marathon.SingleClass;
import com.tdin360.zjw.marathon.WrapContentLinearLayoutManager;
import com.tdin360.zjw.marathon.adapter.RecyclerViewBaseAdapter;
import com.tdin360.zjw.marathon.model.HotelDetailBean;
import com.tdin360.zjw.marathon.model.LoginUserInfoBean;
import com.tdin360.zjw.marathon.utils.CommonUtils;
import com.tdin360.zjw.marathon.utils.HttpUrlUtils;
import com.tdin360.zjw.marathon.utils.NetWorkUtils;
import com.tdin360.zjw.marathon.utils.SharedPreferencesManager;
import com.tdin360.zjw.marathon.utils.ToastUtils;
import com.tdin360.zjw.marathon.weight.ErrorView;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 酒店详情
 */

public class HotelDetailsActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.layout_lading)
    private RelativeLayout layoutLoading;
    @ViewInject(R.id.iv_loading)
    private ImageView ivLoading;

    @ViewInject(R.id.mToolBar)
    private Toolbar toolbar;
    @ViewInject(R.id.btn_Back)
    private ImageView imageView;
    @ViewInject(R.id.line)
    private View viewline;
    @ViewInject(R.id.toolbar_title)
    private TextView titleTv;

    @ViewInject(R.id.errorView)
    private ErrorView mErrorView;

    @ViewInject(R.id.rv_hotel_detail)
    private RecyclerView recyclerView;
    private List<String> list=new ArrayList<>();
    private RecyclerViewBaseAdapter adapter,rvAdapter;
    private HotelDetailBean.ModelBean.BJHotelModelBean bjHotelModel=new HotelDetailBean.ModelBean.BJHotelModelBean();
    private List<HotelDetailBean.ModelBean.BJHotelRoomListModelBean> bjHotelRoomListModel=new ArrayList<>();
    private List<HotelDetailBean.ModelBean.BJHotelPictureListModelBean> bjHotelPictureListModel=new ArrayList<>();
    private List<HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean> bjHotelEvaluateListModel=new ArrayList<>();
    private  List<HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean.BJHotelEvaluatePictureListModelBean> bjHotelEvaluatePictureListModel=new ArrayList<>();
    private HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean bjHotelEvaluateListModelBean=new HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean();
    private HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean.EvaluationUserModelBean evaluationUserModel=new HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean.EvaluationUserModelBean();
    private ImageOptions imageOptions,imageOptionsCircle;
    private String phone1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageOptions = new ImageOptions.Builder().setFadeIn(true)//淡入效果
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
        imageOptionsCircle= new ImageOptions.Builder()
//                     .setSize(DensityUtil.dip2px(80), DensityUtil.dip2px(80))//图片大小
                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setRadius(DensityUtil.dip2px(80))
                .setLoadingDrawableId(R.drawable.my_portrait)//加载中默认显示图片
                .setUseMemCache(true)//设置使用缓存
                .setFailureDrawableId(R.drawable.my_portrait)//加载失败后默认显示图片
                .build();
        initToolbar();
        //initData();
        initNet();
        initRecyclerView();
    }
    private void initNet() {
        //加载失败点击重试
        mErrorView.setErrorListener(new ErrorView.ErrorOnClickListener() {
            @Override
            public void onErrorClick(ErrorView.ViewShowMode mode) {
                switch (mode){
                    case NOT_NETWORK:
                        initData();
                        break;

                }
            }
        });
        //判断网络是否处于可用状态
        if(NetWorkUtils.isNetworkAvailable(this)){
            //加载网络数据
            initData();
        }else {
            layoutLoading.setVisibility(View.GONE);
            //如果缓存数据不存在则需要用户打开网络设置
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
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
    private void initData() {
        bjHotelRoomListModel.clear();
        layoutLoading.setVisibility(View.VISIBLE);
        ivLoading.setBackgroundResource(R.drawable.loading_before);
        AnimationDrawable background =(AnimationDrawable) ivLoading.getBackground();
        background.start();
        String eventId= SingleClass.getInstance().getEventId();
        String hotelId = getIntent().getStringExtra("hotelId");
        RequestParams params=new RequestParams(HttpUrlUtils.HOTEL_DETAIL);
        params.addBodyParameter("appKey",HttpUrlUtils.appKey);
        params.addBodyParameter("eventId",eventId);
        params.addBodyParameter("hotelId",hotelId);
        params.setConnectTimeout(5000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("hoteldetail", "onSuccess: "+result);
               Gson gson=new Gson();
                 HotelDetailBean hotelDetailBean = gson.fromJson(result, HotelDetailBean.class);
                boolean state = hotelDetailBean.isState();
                if(state){
                   // ToastUtils.showCenter(getApplicationContext(),hotelDetailBean.getMessage());
                    HotelDetailBean.ModelBean model = hotelDetailBean.getModel();
                    bjHotelEvaluateListModel= model.getBJHotelEvaluateListModel();
                    bjHotelModel= model.getBJHotelModel();
                    phone1 = bjHotelModel.getPhone1();
                    bjHotelPictureListModel = model.getBJHotelPictureListModel();
                    SingleClass.getInstance().setBjHotelPictureListModel(bjHotelPictureListModel);
                    bjHotelRoomListModel= model.getBJHotelRoomListModel();
                    bjHotelEvaluateListModelBean = bjHotelEvaluateListModel.get(0);
                   evaluationUserModel= bjHotelEvaluateListModelBean.getEvaluationUserModel();
                    /* if(bjHotelRoomListModel.size()<=0){
                        mErrorView.show(recyclerView,"暂时没有数据",ErrorView.ViewShowMode.NOT_DATA);
                    }else {
                        mErrorView.hideErrorView(recyclerView);
                    }*/

                }else {
                    ToastUtils.showCenter(getApplicationContext(),hotelDetailBean.getMessage());
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mErrorView.show(recyclerView,"加载失败,点击重试",ErrorView.ViewShowMode.NOT_NETWORK);
                ToastUtils.showCenter(HotelDetailsActivity.this,"网络不给力,连接服务器异常!");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                adapter.update(bjHotelRoomListModel);
                layoutLoading.setVisibility(View.GONE);
                //hud.dismiss();

            }
        });
    }
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        adapter=new RecyclerViewBaseAdapter<HotelDetailBean.ModelBean.BJHotelRoomListModelBean>(getApplicationContext(),
                bjHotelRoomListModel,R.layout.item_hotel_detail_rv) {
            @Override
            protected void onBindNormalViewHolder(NormalViewHolder holder, final HotelDetailBean.ModelBean.BJHotelRoomListModelBean model) {
                ImageView roomPic = (ImageView) holder.getViewById(R.id.iv_room_pic);
                x.image().bind(roomPic,model.getPictureUrl(),imageOptions);

                TextView tvOrder = (TextView) holder.getViewById(R.id.tv_room_order);
                holder.setText(R.id.tv_room_name,model.getName());
                holder.setText(R.id.tv_room_area,model.getArea()+"㎡");
                holder.setText(R.id.tv_room_free,model.getWindow());
                holder.setText(R.id.tv_room_price,model.getPrice()+"");

                tvOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginUserInfoBean.UserBean loginInfo = SharedPreferencesManager.getLoginInfo(getApplicationContext());
                        String customerId = loginInfo.getId();
                        if(TextUtils.isEmpty(customerId)){
                            Intent intent=new Intent(HotelDetailsActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }else{
                        Intent intent=new Intent(HotelDetailsActivity.this,HotelRoomInActivity.class);
                        String hotelRoomId = model.getId() + "";
                        intent.putExtra("hotelRoomId",hotelRoomId);
                        intent.putExtra("hotelprice",model.getPrice());
                        startActivity(intent);
                        }
                    }
                });


            }

            @Override
            public void onBindHeaderViewHolder(HeaderViewHolder holder) {
                super.onBindHeaderViewHolder(holder);
                ImageView headPic = (ImageView) holder.getViewById(R.id.iv_hotel_head_pic);
                x.image().bind(headPic,bjHotelModel.getPictureUrl(),imageOptions);
                ImageView headPhone = (ImageView) holder.getViewById(R.id.iv_hotel_head_phone);
                holder.setText(R.id.hotel_pic_count,bjHotelPictureListModel.size()+"");
                holder.setText(R.id.tv_hotel_head_name,bjHotelModel.getName());
                holder.setText(R.id.tv_hotel_head_address,bjHotelModel.getAddress());
                holder.setText(R.id.tv_hotel_head_info,bjHotelModel.getDescription());

                LinearLayout layout = (LinearLayout) holder.getViewById(R.id.layout_hotel_detail_pic);
                //拨打电话
                headPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ContextCompat.checkSelfPermission(HotelDetailsActivity.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE},3);
                            //ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
                        }else{
                            showTelDialog();
                        }
                    }
                });
                //查看图片
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(HotelDetailsActivity.this,PictureActivity.class);
                        intent.putExtra("picture","hotelPic");
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onBindFooterViewHolder(FooterViewHolder holder) {
                super.onBindFooterViewHolder(holder);
                holder.setText(R.id.tv_travel_comment_level,bjHotelModel.getScoring()+"");
                holder.setText(R.id.tv_travel_comment_count,"一共有"+bjHotelModel.getEvaluationCount()+"条评论");
                TextView tvMore = (TextView) holder.getViewById(R.id.tv_check_more_comment);
                Log.d("si", "onBindFooterViewHolder: "+bjHotelRoomListModel.size());
                LinearLayout layout = (LinearLayout) holder.getViewById(R.id.layout_hotel_detail);
                if(bjHotelEvaluateListModel.size()<=0){
                    layout.setVisibility(View.GONE);
                    return;
                }else{
                    layout.setVisibility(View.VISIBLE);
                    HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean bjHotelEvaluateListModelBean = bjHotelEvaluateListModel.get(0);
                    HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean.EvaluationUserModelBean evaluationUserModel =
                            bjHotelEvaluateListModelBean.getEvaluationUserModel();
                    ImageView ivHeadPic = (ImageView) holder.getViewById(R.id.iv_comment_head_pic);
                    x.image().bind(ivHeadPic,evaluationUserModel.getHeadImg(),imageOptionsCircle);
                    holder.setText(R.id.tv_comment_name,evaluationUserModel.getNickName());
                    holder.setText(R.id.tv_comment_content,bjHotelEvaluateListModelBean.getEvaluateContent());
                    holder.setText(R.id.tv_comment_time,bjHotelEvaluateListModelBean.getEvaluateTimeStr());
                    //图片展示
                    List<HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean.BJHotelEvaluatePictureListModelBean> bjHotelEvaluatePictureListModel =
                            bjHotelEvaluateListModelBean.getBJHotelEvaluatePictureListModel();
                    final ArrayList<String> image= new ArrayList<>();
                    for(int i = 0; i< bjHotelEvaluatePictureListModel.size(); i++){
                        image.add(bjHotelEvaluatePictureListModel.get(i).getPictureUrl());
                    }
                    RecyclerView rvDetail = (RecyclerView) holder.getViewById(R.id.rv_hotel_detail_foot);
                    rvAdapter=new RecyclerViewBaseAdapter<HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean.BJHotelEvaluatePictureListModelBean>(getApplicationContext(),
                           bjHotelEvaluatePictureListModel,R.layout.item_hotel_detail_pic) {
                        @Override
                        protected void onBindNormalViewHolder(NormalViewHolder holder, HotelDetailBean.ModelBean.BJHotelEvaluateListModelBean.BJHotelEvaluatePictureListModelBean model) {
                            ImageView imageView = (ImageView) holder.getViewById(R.id.iv_comment_pic);
                            x.image().bind(imageView,model.getThumbPictureUrl(),imageOptions);
                        }
                    };
                    rvDetail.setAdapter(rvAdapter);
                    rvDetail.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    rvAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ImageView imageView1=new ImageView(HotelDetailsActivity.this);
                            MNImageBrowser.showImageBrowser(HotelDetailsActivity.this,imageView1,position, image);
                        }
                    });
                }
                //查看更多评价
                tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hotelId = getIntent().getStringExtra("hotelId");
                        Intent intent=new Intent(HotelDetailsActivity.this,HotelMoreCommentActivity.class);
                        intent.putExtra("hotelId",hotelId);
                        startActivity(intent);

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.addHeaderView(R.layout.item_hotel_detail_head);
        adapter.addFooterView(R.layout.item_hotel_detail_foot);
        adapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HotelDetailBean.ModelBean.BJHotelRoomListModelBean bjHotelRoomListModelBean = bjHotelRoomListModel.get(position);
                SingleClass.getInstance().setBjHotelRoomListModelBean(bjHotelRoomListModelBean);
                Intent intent=new Intent(HotelDetailsActivity.this,HotelRoomActivity.class);
                intent.putExtra("name",bjHotelModel.getName());
                startActivity(intent);
            }
        });

        
    }

    private void showTelDialog() {
        android.support.v7.app.AlertDialog.Builder normalDialog =new android.support.v7.app.AlertDialog.Builder(HotelDetailsActivity.this);
        normalDialog.setMessage("是否拨打"+phone1);
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //String phone = textViewhot.getText().toString();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" +phone1);
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
        normalDialog.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }
    private void initToolbar() {
        toolbar.setBackgroundResource(R.color.home_tab_title_color_check);
        viewline.setBackgroundResource(R.color.home_tab_title_color_check);
        imageView.setImageResource(R.drawable.back);
        titleTv.setText(R.string.hotel_detail);
        titleTv.setTextColor(Color.WHITE);
        showBack(toolbar,imageView);

    }

    @Override
    public int getLayout() {
        return R.layout.activity_hotel_detials;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Back:
                showBack(toolbar,imageView);
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 3:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    showTelDialog();
                    //用户授权成功
                }else {
                    //用户没有授权
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                    alert.setTitle("提示");
                    alert.setMessage("您需要设置允许存储权限才能使用该功能");
                    alert.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            CommonUtils.getAppDetailSettingIntent(getApplicationContext());
                        }
                    });
                    alert.show();

                }
                break;
        }

    }

 /*   class MyAdapter extends RecyclerViewBaseAdapter<HotelItem>{


        public MyAdapter(Context context, List<HotelItem> list, int layoutId) {
            super(context, list, layoutId);
        }


        @Override
        public void onBindHeaderViewHolder(final HeaderViewHolder holder) {


              ViewPager viewPager = (ViewPager) holder.getViewById(R.id.galleryView);
               galleryAdapter = new GalleryAdapter(pics);
               viewPager.setAdapter(galleryAdapter);
               holder.setText(R.id.textCountTip,"1/"+pics.size());
               viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                 @Override
                 public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                 }

                 @Override
                 public void onPageSelected(int position) {

                     holder.setText(R.id.title,picTitles.get(position));
                     holder.setText(R.id.textCountTip,(position+1)+"/"+pics.size());

                 }

                 @Override
                 public void onPageScrollStateChanged(int state) {

                 }
             });

            if(model==null)return;
             holder.setText(R.id.hotel_name,model.getName());
             holder.setText(R.id.hotel_price,"¥"+model.getLowestPrice()+"起");
             holder.setText(R.id.hotel_Description,model.getDescription());
             String p2 = model.getPhone2()==null||model.getPhone2().equals("null")?"":","+model.getPhone2();
             holder.setText(R.id.hotel_tel,model.getPhone1()+p2);
             holder.setText(R.id.hotel_address,model.getAddress());

            }
        @Override
        protected void onBindNormalViewHolder(NormalViewHolder holder, final HotelItem model) {


            ImageView view = (ImageView) holder.getViewById(R.id.room_imageView);
            x.image().bind(view,model.getPictureUrl());
            holder.setText(R.id.room_name,model.getTitle());
              holder.setText(R.id.room_bad_size,model.getType());
              holder.setText(R.id.room_area,model.getArea()+"平米");
              holder.setText(R.id.room_price,model.getPrice()+"元");
             holder.getViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                    Intent intent = new Intent(HotelDetailsActivity.this,HotelOrderSubmitActivity.class);


                     intent.putExtra("id",model.getHotelId());
                     intent.putExtra("title",title+"-"+model.getTitle());
                     intent.putExtra("price",model.getPrice());
                     startActivity(intent);
                 }
             });
            holder.setText(R.id.room_intro,"");

        }
    }


    *//**
     * 详情页顶部图片适配器
     *//*
    class GalleryAdapter extends PagerAdapter{


        private List<View>list;

        public GalleryAdapter(List<View>list){


            this.list=list;

        }
        public void update(List<View> list){

            this.list=list;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return list==null? 0:list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = list.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView(list.get(position));

        }
    }*/
}
