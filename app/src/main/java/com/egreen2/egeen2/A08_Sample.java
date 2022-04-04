package com.egreen2.egeen2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.egreen2.egeen2.Data.StudyInfo;

import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/*
    [파일명] A08_Sample.java
    [설명] 전체 교육과정 샘플영상 1주차 1차시 영상 재생
    [작성자] 장희원
    [작성일시] 2021.03.24
 */

public class A08_Sample extends AppCompatActivity implements NetworkAsyncTasker.AsyncResponse {
    private static final String TAG = A08_Sample.class.getSimpleName();

    WebView wv;

    String eCid = "";
    String loadingTxT;

    ShowLoading loading;

    StudyInfo si;

    /* AsyncTask 결과 값을 받기 위한 변수 */
    NetworkAsyncTasker asyncTask;
    NetworkStateCheck netCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a08_sample);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //화면 가로로 고정한다.
        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;

        wv = findViewById(R.id.sample_wv);

        /* 웹뷰 설정 */
        WebSettings wvSet = wv.getSettings();
        wvSet.setTextZoom(100); //웹뷰 폰트 크기 고정한다.
        wvSet.setJavaScriptEnabled(true); //자바스크립트 사용 가능하다.
        wvSet.setUseWideViewPort(true); //뷰포트 사용 가능하다.
        wvSet.setLoadWithOverviewMode(true); //웹뷰 화면에 맞게 출력한다.
        wvSet.setAllowContentAccess(false); //콘텐츠 프로바이더(CP)를 이용할 때는 true, 콘텐츠 프로바이더가 제공하는 콘텐츠를 읽을 수 있다.
        wvSet.setBuiltInZoomControls(false); //줌 기능 설정이다.
        wvSet.setDisplayZoomControls(false); //내장 줌 기능을 사용할 때 웹뷰가 화면에 줌 컨트롤러를 표시할지 설정한다.
        wvSet.setSupportZoom(false); //줌 컨트롤과 제스처를 사용하여 확대 여부를 결정한다.
        wvSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //캐시를 사용할지 결정한다. (배포할 때 주석처리 해야함) : LOAD_DEFALT => 기본으로 캐시를 사용하고 만료된 경우 네트워크를 사용
        wvSet.setAppCacheEnabled(false);  //앱 내부 캐시 사용 여부 설정
        wvSet.setLoadsImagesAutomatically(true); //웹뷰가 앱에 등록되어 있는 이미지 리소스를 자동으로 로드하도록 설정한다.

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            wvSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //콘텐츠 사이즈 맞추기(킷켓 이상부터 지원하지 않음)
        }

        wv.setWebViewClient(new WebBrowserClient());
        wv.setWebChromeClient(new WebChromeClient());

        /**
         * A08_Curriculum에서 전달한 과목 ID를 가져온다.
         */
        Intent intent = getIntent();
        try {
            eCid = intent.getStringExtra("CID");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //잘 가져왔는지 체크
        checkGetIntent();

        //강의를 웹뷰에 띄운다.
        String contentsUrl;

        String domain = "";
        domain = "http://cb.egreen.co.kr/contents_m/android/" + si.getDirectoryName();

        loadingTxT = "샘플강의를 불러오는 중입니다!";
        contentsUrl = domain + "/01/01.html";

        netCheck = new NetworkStateCheck(this);
        if (netCheck.isConnectionNet()) {
            wv.loadUrl(contentsUrl);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 중복 로그인 체크
     */
    public String netConnForOverlapCheck() {
        String url = "http://cb.egreen.co.kr/mobile_proc/loginOverlap_check_m2.asp";
        ContentValues cValues = new ContentValues();
        cValues.put("userId", si.getUserId());
        cValues.put("ulNum", si.getLoginNumber());

        //네트워크 통신으로 데이터를 가져온다.
        asyncTask = new NetworkAsyncTasker(this, url, cValues);
        try {
            return asyncTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Async 결과 전달 받음
     */
    @Override
    public void processFinish(String result, String what) {
    }

    /**
     * intent로 잘 넘어왔나 체크
     */
    private void checkGetIntent() {
        try {
            if (eCid.equals("")) {
                Log.i(TAG, "cId가 없다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 뒤로가기 버튼 클릭 시 정말 종료할 것인지를 묻는다.
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //final Handler handler = new Handler();

        String alertTxt = "샘플강의를 종료합니다.";
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage(alertTxt);
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveToA08_Curriculum();
            }
        });
        ab.setNegativeButton("취소", null);
        ab.show();
    }

    /**
     * A08_Curriculum으로 이동
     */
    public void moveToA08_Curriculum() {
        Intent intent = new Intent(this, A08_Curriculum.class);
        startActivity(intent);
        finish();
    }

    /**
     * 중복 로그인 알림
     */
    private void logoutByLoginOverlap(String result) {
        String[] arrResult = result.split(",");
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("중복 로그인이 감지되어 로그아웃 되었습니다\n\n" +
                "접속기간: " + arrResult[1] + "\n" +
                "접속 IP: " + arrResult[2] + "\n" +
                "접속 OS: " + arrResult[3] + "\n" +
                "접속 Browser: " + arrResult[4]);
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), A02_Login.class);
                startActivity(intent);

            }
        }).show();
    }

    /**
     * A02_Login으로 이동
     */
    private void moveToA02_Login() {
        SharedPreferences saveCertyState = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = saveCertyState.edit();
        editor.putBoolean("certyState", false);
        editor.commit();

        startActivity(new Intent(this, A02_Login.class));
        finish();
    }

    /**
     * 강의 화면을 벗어나게 될 때 동영상 재생 중이었다면 멈추도록 한다.
     * 전화, 카톡, 홈 버튼 등
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (netCheck.isConnectionNet()) {
            //동영상 플레이어를 일시정지한다
            if (wv != null) {
                wv.loadUrl("javascript:background_pause();");
            }
        }
        if (loading != null) {
            loading.stop();
        }
    }

    @Override
    protected void onDestroy() {
        //액티비티 종료될 때 반드시 제거
        super.onDestroy();
    }

    /**
     * 브라우저
     */
    private class WebBrowserClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //페이지 로딩 시작
            Log.i(TAG, "onPageStarted()");

            if (loading == null) {
                loading = new ShowLoading(A08_Sample.this, loadingTxT);
                loading.start();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
            //콘텐츠 실행 중
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.i(TAG, "onPageFinished()");

            //페이지 로딩 완료
            if (loading != null) {
                Log.i(TAG, "loading.stop()");
                loading.stop();
                loading = null;
            }
        }
    }
}



