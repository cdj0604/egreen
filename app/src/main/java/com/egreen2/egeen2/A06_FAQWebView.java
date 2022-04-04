package com.egreen2.egeen2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A06_FAQWebView.java.java
    [설명] FAQ
    [작성자] 장희원
    [작성일시]
 */

public class A06_FAQWebView extends AppCompatActivity {
    private static final String TAG = A06_FAQWebView.class.getSimpleName();
    private final Context context = this;
    ProgressDialog loadingDialog;
    /* View 변수 선언 */
    WebView a062_faqWV;   //공지사항을 뿌려주는 웹뷰
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a06_faq_webview);

        /* Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.a06_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //뒤로가기 버튼 이미지 지정

        /* View 연결 */
        a062_faqWV = findViewById(R.id.a06_faqWV); //FAQ
        ImageView imageView = findViewById(R.id.logo);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        /* 웹뷰가 모두 로딩될때까지 보여줄 Dialog */
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("잠시만 기다려주세요.");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);

        /* WebView 설정 */
        a062_faqWV.getSettings().setSupportZoom(true);    //확대/축소 사용할 수 있도록 설정
        a062_faqWV.getSettings().setBuiltInZoomControls(true);    //안드로이드에서 제공하는 줌 아이콘을 사용할 수 있도록 설정
        a062_faqWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //캐시모드를 사용하지 않고 네트워크를 통해서만 호출
        a062_faqWV.getSettings().setLoadWithOverviewMode(true);   //웹뷰 화면에 맞게 출력
        a062_faqWV.setWebViewClient(new A06_FAQWebView.WebBrowserClient());
        a062_faqWV.loadUrl("https://cb.egreen.co.kr/customer/advicecenter02.asp");
        a062_faqWV.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        a062_faqWV.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        a062_faqWV.clearCache(true);
        a062_faqWV.getSettings().setTextZoom(100);  //웹뷰 폰트 크기 고정
        a062_faqWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        a062_faqWV.getSettings().setSupportMultipleWindows(true);
        a062_faqWV.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {   // 왼쪽 상단 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class WebBrowserClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loadingDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingDialog.dismiss();
        }
    }

}
