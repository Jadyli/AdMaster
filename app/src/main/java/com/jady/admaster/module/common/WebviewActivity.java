package com.jady.admaster.module.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jady.admaster.R;

public class WebviewActivity extends AppCompatActivity {

    protected WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        webview = (WebView) findViewById(R.id.webview);

        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            webview.loadUrl(url);
        }
        //设置可自由缩放网页
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
    }
}
