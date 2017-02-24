package com.tdin360.zjw.marathon.app;

import android.app.Application;


import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/8/10.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化
        x.Ext.init(this);
        // 设置是否输出debug
        x.Ext.setDebug(false);
        //极光推送
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

        Config.DEBUG=false;
        UMShareAPI.get(this);


    }

//    分享平台配置
    {

        PlatformConfig.setWeixin("wx56d6dadff22bbf5e","47211ce7c72da9b01ace9b47ba5d9dfa");
        PlatformConfig.setQQZone("1105925323","q4m5hraExG9wAfe7");
        PlatformConfig.setSinaWeibo("2316733117", "a4bdff71157545c0366244049d37d218","https://api.weibo.com/oauth2/default.html");

    }
}
