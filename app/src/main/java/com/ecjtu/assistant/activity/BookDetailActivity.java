package com.ecjtu.assistant.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.utils.LogUtil;
import com.ecjtu.assistant.utils.StringUtils;
import com.ecjtu.assistant.utils.ToastUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BookDetailActivity extends BaseActivity {

    private String bookLink;
    private String title;
    private Document document = null;

    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_detail);

        bookLink = getIntent().getExtras().getString("bookLink");
        LogUtil.i("bookLink:" + bookLink);

        initView();
        initData();
    }
    Toolbar toolbar;
    private void initView() {

           toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        webView = (WebView) findViewById(R.id.books_detail_webView);
    }

    private void initData() {

        if (!StringUtils.isFine(bookLink)){
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    document = Jsoup.connect(bookLink).get();
                    LogUtil.i("bookLink(HTML內容):" + document.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            refreshView("");
                        }
                    });
                }
                catch(Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            ToastUtils.showShort(context,"数据异常，请稍后再试");
                        }
                    });
                }
            }
        }.start();

    }

    private void refreshView(String response) {


        try {

            title = document.title();

            Element  select4 = document. select("div.boxBd") .get(0) ;
            Elements select5 = select4. children();

            LogUtil.i("select5:" + select5.size());

            select5.get(select5.size()-1).remove();
            select5.get(select5.size()-2).remove();

            webView.loadData(select4.toString(), "text/html; charset=UTF-8", null);
            LogUtil.i("bookLink2:" + select4.toString());

            toolbar.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(context,"解析数据异常!");
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
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
