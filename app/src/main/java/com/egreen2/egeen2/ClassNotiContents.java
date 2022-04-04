package com.egreen2.egeen2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class ClassNotiContents extends AppCompatActivity {

    ShowLoading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_noti_contents);

        WebView wv = findViewById(R.id.wv);

        //웹뷰 설정
        WebSettings wvSet = wv.getSettings();
        wvSet.setJavaScriptEnabled(true);       //자바스크립트 사용 가능하다.
        wvSet.setUseWideViewPort(true);         //뷰포트 사용 가능하다.
        wvSet.setLoadWithOverviewMode(true);    //웹뷰 화면에 맞게 출력한다.
        wvSet.setAllowContentAccess(false);     //컨텐츠 프로바이더(CP)를 이용할때는 true로 설정, 컨텐츠 프로바이더가 제공하는 컨텐츠를 읽을 수 있다.
        wvSet.setBuiltInZoomControls(true);    //줌 기능 설정이다.
        wvSet.setDisplayZoomControls(false);    //내장 줌 기능을 사용할때, 웹뷰가 화면에 줌 컨트롤러를 표시할지 설정한다.
        wvSet.setSupportZoom(true);            //줌 컨트롤과 제스처를 사용하여 확대 여부를 결정한다.
        wvSet.setCacheMode(WebSettings.LOAD_NO_CACHE);  //캐시를 사용할지 결정한다. (배포할때 주석처리 해야함)
        wv.setWebViewClient(new WebBrowserClient());
        wv.setWebChromeClient(new WebChromeClient());

//        a6_studyRoom.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);       //자동 줄바꿈 방지, 안드로이드 3.x 이하에서는 안해도 된다.

        /*
         * A6_MyClass에서 전달한 과목 ID 를 가져온다.
         */
        Intent intent = getIntent();
        try {
            int notiId = intent.getIntExtra("NOTI_ID", 0);
            wv.loadUrl("http://cb.egreen.co.kr/mobile_proc/mypage/new/noticeContents_m.asp?notiId=" + notiId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 브라우저
     */
    private class WebBrowserClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //페이지 로딩 시작
            loading = new ShowLoading(ClassNotiContents.this, "강의를 가져오는 중입니다!");
            loading.start();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
            //컨텐츠 실행중
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //페이지 로딩 완료
            if (loading != null) {
                loading.stop();
            }
        }
    }
}
