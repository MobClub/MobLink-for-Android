package com.mob.moblink.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mob.moblink.Scene;
import com.mob.moblink.SceneRestorable;

public class BrowserActivity extends Activity implements SceneRestorable {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        webView = findViewById(R.id.agentweb);

        /**支持Js**/
        webView.getSettings().setJavaScriptEnabled(true);
        /**设置自适应屏幕，两者合用**/
        //将图片调整到适合webview的大小
        webView.getSettings().setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webView.getSettings().setLoadWithOverviewMode(true);
        /**缩放操作**/
        // 是否支持画面缩放，默认不支持
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        // 是否显示缩放图标，默认显示
        webView.getSettings().setDisplayZoomControls(false);
        // 设置网页内容自适应屏幕大小
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        /**设置允许JS弹窗**/
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);

        /**关闭webview中缓存**/
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        /**设置可以访问文件 **/
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webView.loadUrl("http://f.moblink.mob.com/pro/scene/news/?id=0&mobid=7z2eyi");
    }

    @Override
    public void onReturnSceneData(Scene scene) {
        if (scene != null && scene.params != null) {
            final String url = (String) scene.params.get("startPage");
            Log.d("Moblink", "url==" + url);


           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(url);
                }
            }, 3000);*/
        }
    }
}
