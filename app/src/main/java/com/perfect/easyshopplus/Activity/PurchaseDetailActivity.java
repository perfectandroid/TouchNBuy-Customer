package com.perfect.easyshopplus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.perfect.easyshopplus.R;

public class PurchaseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView im;
    TextView tv_header;
    WebView webView;
    String url ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail);


        initiateViews();
        setRegViews();

        Intent in = getIntent();
        url = in.getStringExtra("voucherUrl");

       //url = "http://www.tutorialspoint.com";
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);



    }

    private void initiateViews() {

        im = (ImageView)findViewById(R.id.im);
        tv_header = (TextView)findViewById(R.id.tv_header);

        webView = (WebView) findViewById(R.id.webview);
    }

    private void setRegViews() {

        im.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.im:
                onBackPressed();
                break;

        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}