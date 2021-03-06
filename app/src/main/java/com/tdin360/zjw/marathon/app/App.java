package com.tdin360.zjw.marathon.app;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.MainThread;
import android.support.multidex.MultiDexApplication;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lzy.ninegrid.NineGridView;
import com.tdin360.zjw.marathon.CrashHander;
import com.tdin360.zjw.marathon.R;
import com.tdin360.zjw.marathon.utils.db.DbManager;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import com.umeng.socialize.utils.Log;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by Administrator on 2016/8/10.
 */
public class App extends MultiDexApplication {

    //    分享平台配置
    {

        PlatformConfig.setWeixin("wx56d6dadff22bbf5e","47211ce7c72da9b01ace9b47ba5d9dfa");
        PlatformConfig.setQQZone("1105925323","q4m5hraExG9wAfe7");
        PlatformConfig.setSinaWeibo("2316733117", "a4bdff71157545c0366244049d37d218","http://sns.whalecloud.com");

    }


    public static ImageOptions xUtilsOptions = new ImageOptions.Builder()//
            .setIgnoreGif(false)                                //是否忽略GIF格式的图片
            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)  //缩放模式
            .setLoadingDrawableId(R.drawable.event_bg)  //下载中显示的图片
            .setFailureDrawableId(R.drawable.event_bg)  //下载失败显示的图片
            .build();
    // 在application的onCreate中初始化
    /** XUtils 加载 */
    private class XUtilsImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            x.image().bind(imageView, url, App.xUtilsOptions);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config=ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this,config);
        //全局异常捕捉
        //CrashHander crashHandler= CrashHander.getInstance();
       // crashHandler.init(getApplicationContext());
        //社交图片
        NineGridView.setImageLoader(new XUtilsImageLoader());
        // 初始化
        x.Ext.init(this);
        // 设置是否输出debug
        x.Ext.setDebug(false);
/*
        String currentProcessName = getCurrentProcessName();
        if (getPackageName().equals(currentProcessName)) {

        }*/
        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //友盟分享
        UMShareAPI.get(this);
        Log.LOG=false;
//        分享时无客户端自动跳转到下载界面
        Config.isJumptoAppStore = true;
//        友盟统计
        MobclickAgent.setDebugMode(false);

        //创建数据库
        DbManager.init(this);
        //初始化X5内核,WebView
        if(QbSdk.canLoadX5(this)) {
            //加载❤x5内核
            QbSdk.initX5Environment(this, null);
        }
    }
    //获取进程名字
    private String getCurrentProcessName() {
        String currentProcName = "";
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                currentProcName = processInfo.processName;
                break;
            }
        }
        return currentProcName;
    }




}
