package com.egreen2.egeen2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A05_NoticeWebView.java.java
    [설명] 공지사항
    [작성자] 장희원
    [작성일시]
 */

/* 공지사항 페이지 */
public class A05_NoticeWebView extends AppCompatActivity {
    private static final String TAG = A05_NoticeWebView.class.getSimpleName();
    /* View 변수 선언 */
    WebView a05_noticeWV;   //공지사항을 뿌려주는 웹뷰
    ProgressDialog loading;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a05_notice_webview);

        /* 약관을 보여줄때까지 로딩 Dialog 설정 */
        loading = new ProgressDialog(A05_NoticeWebView.this);
        loading.setMessage("공지사항을 불러오는 중입니다.");
        loading.setIndeterminate(true);
        loading.setCancelable(true);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a05_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //뒤로가기 버튼 이미지 지정

        /* View 연결 */
        a05_noticeWV = findViewById(R.id.a05_noticeWV); //공지사항

        /* WebView 설정 */
        a05_noticeWV.getSettings().setSupportZoom(true);    //확대/축소 사용할 수 있도록 설정
        a05_noticeWV.getSettings().setBuiltInZoomControls(true);    //안드로이드에서 제공하는 줌 아이콘을 사용할 수 있도록 설정
        a05_noticeWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //캐시모드를 사용하지 않고 네트워크를 통해서만 호출
        a05_noticeWV.getSettings().setLoadWithOverviewMode(true);   //웹뷰 화면에 맞게 출력

        /* A05_NoticeBoard에서 전달받은 각 URL을 WebView에 뿌려줌 */
        Intent intent = getIntent();
        if (intent.getStringExtra("NOTICE") != null) {
            Log.i(TAG, "NOTICE: " + intent.getStringExtra("NOTICE"));

            a05_noticeWV.setWebViewClient(new WebBrowserClient());
            a05_noticeWV.loadUrl("http://cb.egreen.co.kr/mobile_proc/cs_notice_view.asp?cCid=" + intent.getStringExtra("NOTICE"));
        }
    }   //onCreate 종료

    /* 로그인 버튼 */
    public void go_A02_Login(View view) {

        //다이얼로그 예 눌렀을때 인텐트실행
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("로그아웃 하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), A02_Login.class);
                startActivity(intent);

            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }             //로그인 페이지로 이동

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   // 왼쪽 상단 버튼 눌렀을 때
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class WebBrowserClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loading.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loading.dismiss();
        }
    }

}
