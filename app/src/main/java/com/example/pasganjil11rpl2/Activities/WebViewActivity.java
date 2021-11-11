package com.example.pasganjil11rpl2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pasganjil11rpl2.Model.HistoryModel;
import com.example.pasganjil11rpl2.R;
import com.example.pasganjil11rpl2.Realm.HistoryHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WebViewActivity extends AppCompatActivity {
    String url, title, name, currentTime, urlToImage;
    WebView webView;
    ProgressBar pb_webview;
    Realm realm;
    HistoryHelper historyHelper;
    HistoryModel historyModel;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        name = intent.getStringExtra("name");
        urlToImage = intent.getStringExtra("urlToImage");
        currentTime = currentTimes();

        pb_webview = findViewById(R.id.pb_webview);
        webView = findViewById(R.id.wv_news);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //init
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);

        historyModel = new HistoryModel(title, urlToImage, name, currentTime, url);
        historyHelper = new HistoryHelper(realm);
        historyHelper.save(historyModel);

        pb_webview.setVisibility(View.GONE);
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
            case R.id.nav_refresh:
                webView.reload();
//                WebViewActivity.this.webView.loadUrl(url);
                Toast.makeText(this, "Reloading now!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_webview, menu);
        return true;
    }

    private String currentTimes() {
        String currentTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        return currentTime + ", " + currentDate;
    }
}