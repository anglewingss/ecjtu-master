package com.ecjtu.assistant.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ecjtu.assistant.R;

import java.util.ArrayList;
import java.util.List;

public class LifeInfoActivity extends BaseActivity {

    //    private int flag;
    private ProgressBar progressBar;
    private WebView webView;

    private String newsLink;
    private String readNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_info);
        setTitle("新闻详情");
//        flag = getIntent().getExtras().getInt("flag");
        findViewById(R.id.image_sure_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(newsLink);
            }
        });
        newsLink = getIntent().getExtras().getString("newsLink");
        Log.e("gaom newsLink=", newsLink);
        readNumber = getIntent().getExtras().getString("readNumber");
        initView();
    }

    private void share(String content) {
        try {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain"); // 查询所有可以分享的Activity
            List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (!resInfo.isEmpty()) {
                List<Intent> targetedShareIntents = new ArrayList<Intent>();
                for (ResolveInfo info : resInfo) {
                    Intent targeted = new Intent(Intent.ACTION_SEND);
                    targeted.setType("text/plain");
                    ActivityInfo activityInfo = info.activityInfo;
                    Log.v("logcat", "packageName=" + activityInfo.packageName + "Name=" + activityInfo.name); // 分享出去的内容

                    targeted.putExtra(Intent.EXTRA_TEXT, content); // 分享出去的标题
                    targeted.putExtra(Intent.EXTRA_SUBJECT, "主题");
                    targeted.setPackage(activityInfo.packageName);
                    targeted.setClassName(activityInfo.packageName, info.activityInfo.name);
                    PackageManager pm = getApplication().getPackageManager();
                    // 微信有2个怎么区分-。- 朋友圈还有微信
                    if (info.activityInfo.applicationInfo.loadLabel(pm).toString().equals("微信")) {
                        targetedShareIntents.add(targeted);
                    }
                } // 选择分享时的标题
                if (targetedShareIntents.size() == 0) {
                    Toast.makeText(this, "找不到可以分享的应用!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "选择分享");
                if (chooserIntent == null) {
                    return;
                }
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);

            }
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "找不到可以分享的应用!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        //微信里面的文章加载是通过js来实现的，所以我们需要设置WebView去设置javaScript可用
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webView.loadUrl(newsLink);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {//
//                意思就是网页在不停的加载,本方法就在不停的进行,newProgress就是实际得到的
//                进度,可以根据此回调函数来完成进度条的控制.
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
