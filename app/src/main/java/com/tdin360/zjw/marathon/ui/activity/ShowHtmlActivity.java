package com.tdin360.zjw.marathon.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tdin360.zjw.marathon.R;
import com.tdin360.zjw.marathon.model.ShareInfo;
import com.tdin360.zjw.marathon.utils.NetWorkUtils;
import com.tdin360.zjw.marathon.utils.ShareInfoManager;
import com.umeng.socialize.UMShareAPI;

/**
 * 用于显示网页的界面
 * @author zhangzhijun
 */
public class ShowHtmlActivity extends BaseActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private LinearLayout loading;
    private TextView loadFail;
    private Button signUpBtn;
    private LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.webView = (WebView) this.findViewById(R.id.webView);
        this.signUpBtn = (Button) this.findViewById(R.id.signBtn);
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.loading= (LinearLayout) this.findViewById(R.id.loading);
        this.loadFail = (TextView) this.findViewById(R.id.loadFail);
        this.main= (LinearLayout) this.findViewById(R.id.main);

        showBackButton();
        //处理其他界面传过来的数据
        Intent intent = this.getIntent();

        if (intent!=null){

            boolean isSign = intent.getBooleanExtra("isSign", false);
            if(isSign){
                this.signUpBtn.setVisibility(View.VISIBLE);
                this.signUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShowHtmlActivity.this,SignUpActivity.class);
                        startActivity(intent);
                    }
                });
            }
            String title = intent.getStringExtra("title");
            setToolBarTitle(title);
            final String url = intent.getStringExtra("url");
            /**
             * 构建分享内容
             */
          ShareInfoManager manager = new ShareInfoManager(this);
            manager.buildShareWebLink(title,"http://www.baidu.com","来自网页详情的分享", BitmapFactory.decodeResource(getResources(),R.mipmap.logo));
            showShareButton(manager);

            //加载失败点击重新加载
            this.loadFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading(url);
                }
            });

           loading(url);

        }


    }


    /**
     * 分享需要配置确保回调成功
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_show_html;
    }

    private void loading(String url){

        if(!NetWorkUtils.isNetworkAvailable(this)){
            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
            loadFail.setText("检查网络后重新打开!");
            loadFail.setVisibility(View.VISIBLE);
            return;
        }

         this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
         this.webView.getSettings().setJavaScriptEnabled(true);
         this.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
         this.webView.getSettings().setAllowFileAccess(true);
         this.webView.setWebChromeClient(new MyWebViewChromeClient());
         this.webView.setWebViewClient(new MyWebViewClient());
         this.webView.loadUrl(url);
     }


    private class MyWebViewChromeClient extends WebChromeClient{



        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
        }
    }
    private class MyWebViewClient extends WebViewClient {




        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            main.setVisibility(View.INVISIBLE);

        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return  true;


        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            loadFail.setText("加载失败了,点击重新加载!");
            loadFail.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);



        }
    }

    @Override
    public void onBackPressed() {

          finish();
    }

}
