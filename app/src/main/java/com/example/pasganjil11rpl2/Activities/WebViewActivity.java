package com.example.pasganjil11rpl2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pasganjil11rpl2.Model.HistoryModel;
import com.example.pasganjil11rpl2.R;
import com.example.pasganjil11rpl2.Realm.HistoryHelper;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WebViewActivity extends AppCompatActivity {
    String url, title, name, currentTime, urlToImage;
    WebView webView;
    LinearProgressIndicator pb_webview;
    Realm realm;
    HistoryHelper historyHelper;
    HistoryModel historyModel;
    SwipeRefreshLayout refreshLayout_webview;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
//        title = intent.getStringExtra("title");
//        name = intent.getStringExtra("name");
//        urlToImage = intent.getStringExtra("urlToImage");
//        currentTime = currentTimes();

        refreshLayout_webview = findViewById(R.id.srl_refreshLayout_webview);
        pb_webview = findViewById(R.id.pb_webview);
        webView = findViewById(R.id.wv_news);
//        webView.setWebViewClient(new myWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                setTitle("Loading...");
                setProgress(progress * 100);
                pb_webview.setVisibility(View.VISIBLE);
//                pb_webview.setProgress(progress);
                pb_webview.setProgressCompat(progress, true);

                if(progress == 100) {
                    setTitle("");
                    pb_webview.setVisibility(View.GONE);
                }
            }
        });
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //init
//        Realm.init(this);
//        RealmConfiguration configuration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
//        realm = Realm.getInstance(configuration);
//
//        historyModel = new HistoryModel(title, urlToImage, name, currentTime, url);
//        historyHelper = new HistoryHelper(realm);
//        historyHelper.save(historyModel);

        refreshLayout_webview.setProgressBackgroundColorSchemeResource(R.color.def_header);
        refreshLayout_webview.setColorSchemeResources(
                android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout_webview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pb_webview.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.reload();
                        pb_webview.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Reloading now!", Toast.LENGTH_SHORT).show();
                        refreshLayout_webview.setRefreshing(false);
                    }
                }, 4000);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
            this.finish();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
//            case R.id.nav_refresh:
//                webView.reload();
////                WebViewActivity.this.webView.loadUrl(url);
//                Toast.makeText(this, "Reloading now!", Toast.LENGTH_SHORT).show();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.header_webview, menu);
//        return true;
//    }

//    private class myWebViewClient extends WebViewClient {
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            pb_webview.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            pb_webview.setVisibility(View.GONE);
//        }
//    }
}