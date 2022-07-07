package com.egreen2.egeen2;


import android.app.PictureInPictureParams;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Rational;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.egreen2.egeen2.Data.StudyInfo;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


public class A13_Learning extends AppCompatActivity implements NetworkAsyncTasker.AsyncResponse {
    public static final String WIFI_STATE = "WIFE";
    public static final String MOBILE_STATE = "MOBILE";
    public static final String NONE_STATE = "NONE";
    private static final String TAG = A13_Learning.class.getSimpleName();
    private static A13_Learning instanse;
    WebView wv;
    int jucha = 0, chasi = 0;
    String contentsPage;
    String eCid = "", fileRoot = "";
    String watchedTime = "", fullTime = "";
    Timer jindoTimer = new Timer();
    TimerTask jindoTimerTask = null;
    int viewTime, maxTime, minutes, second;
    int alert_viewTime;
    ShowLoading loading;
    String loadingTxt;
    ViewPager pager;
    //AsyncTask 결과값을 받기위한 변수
    NetworkAsyncTasker asyncTask;
    NetworkStateCheck netCheck;
    Handler loginOverHelper;
    //A7_StudyCenter >> A7_ClassRoom 으로 전달 받은 intent 정보
    StudyInfo si;
    private boolean enable;

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    //#################################### onCreate 종료 ####################################//

    //#################################### onCreate 시작 ####################################//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.a13_learning);

        //pip
    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder pipBuilder = new PictureInPictureParams.Builder();
            enterPictureInPictureMode(pipBuilder.build());}*/

/*
        // 현재 기기의 SDK버전이 안드로이드11 보다 크거나 같다면
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN )
        }*/

        /* 2021.04.01 앱 캐시 지우기 코드 시작 */
        instanse = this;
        clearApplicationData();
        /* 앱 캐시 지우기 코드 끝 */

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     //화면 가로로 고정 2020.06.01 장희원
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);        //화면 회전
//        if (Build.VERSION.SDK_INT <= 23) {
//            Log.i(TAG, "This is API Level 23");
        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;
//        }

        wv = findViewById(R.id.wv);

        //웹뷰 설정
        WebSettings wvSet = wv.getSettings();
        wvSet.setTextZoom(100);                 //웹뷰 폰트크기 고정 2020.07.09 장희원
        wvSet.setJavaScriptEnabled(true);       //자바스크립트 사용 가능하다.
        wvSet.setUseWideViewPort(true);         //뷰포트 사용 가능하다.
        wvSet.setLoadWithOverviewMode(true);    //웹뷰 화면에 맞게 출력한다.
        wvSet.setAllowContentAccess(false);     //컨텐츠 프로바이더(CP)를 이용할때는 true로 설정, 컨텐츠 프로바이더가 제공하는 컨텐츠를 읽을 수 있다.
        wvSet.setBuiltInZoomControls(false);    //줌 기능 설정이다.
        wvSet.setDisplayZoomControls(false);    //내장 줌 기능을 사용할때, 웹뷰가 화면에 줌 컨트롤러를 표시할지 설정한다.
        wvSet.setSupportZoom(false);            //줌 컨트롤과 제스처를 사용하여 확대 여부를 결정한다.
        //wvSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //캐시를 사용할지 결정한다. (배포할때 주석처리 해야함) : LOAD_DEFAULT => 기본으로 캐시를 사용하고 만료된 경우 네트워크를 사용.
        wvSet.setAppCacheEnabled(false);     //앱 내부 캐시 사용 여부 설정
        wvSet.setLoadsImagesAutomatically(true);        //웹뷰가 앱에 등록되어 있는 이미지 리소를 자동으로 로드하도록 설정

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            wvSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }

        wv.addJavascriptInterface(new JSCall(), "jscall");      //웹 -> 앱 함수를 호출한다.
        wv.setWebViewClient(new WebBrowserClient());
        wv.setWebChromeClient(new WebChromeClient());

//        a6_studyRoom.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);       //자동 줄바꿈 방지, 안드로이드 3.x 이하에서는 안해도 된다.

        //A7_StudyCenter에서 전달한 학습정보를 저장
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");

        /*
         * A6_MyClass에서 전달한 과목 ID 를 가져온다.
         */
        Intent intent = getIntent();
        try {
            jucha = intent.getIntExtra("JUCHA", 0);     //주차
            chasi = intent.getIntExtra("CHASI", 0);     //차시
            eCid = intent.getStringExtra("CID");            //cid
            watchedTime = intent.getStringExtra("WATCHED_TIME");    //학습 누적시간
            fullTime = intent.getStringExtra("FULL_TIME");      //컨텐츠 전체시간
            fileRoot = intent.getStringExtra("FILE_ROOT");      //컨텐츠 파일 경로 ex)01/01.html
            enable = intent.getBooleanExtra("ENABLE", false);  //출석체크가 가능한 과목인지 확인
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Test, 15200002
//        if (si.getUserId().equals("15200002")) {
//            //2019 평가인정 Test
//            jucha = 1;     //주차
//            chasi = 0;     //차시
//            eCid = intent.getStringExtra("CID");            //cid
//            watchedTime = "40";    //학습 누적시간
//            fullTime = "40";      //컨텐츠 전체시간
//            fileRoot = "17/01.html";      //컨텐츠 파일 경로 ex)01/01.html
//            enable = false;
//            si.setDirectoryName("2019_08mp");
//        }

        //위 값들을 잘 가져왔는지 그냥.. 체크한다. 잘 가져왔겠지만.
        checkGetIntent();

        //강의를 열었을때, 마지막으로 본 페이지로 이동한다.(예정)
//        SharedPreferences savedContents = getSharedPreferences("CONTENTS", MODE_PRIVATE);
//        SharedPreferences.Editor editor = savedContents.edit();
//        editor.putString("D_NAME", directoryName);
//        editor.putInt("CHASI", chasi);
//        editor.commit();

        //학습시간을 초기화한다.
        viewTime = Integer.parseInt(watchedTime);
        maxTime = Integer.parseInt(fullTime);
        int _viewTime = 0;
        if (viewTime < maxTime) {
            //학습한 시간이 총 학습시간보다 작을때,
            //남은 시간에서부터 - 하기 위해,
            //총 학습시간에서 학습누적시간을 - 해준다.
            _viewTime = maxTime;
            _viewTime -= viewTime;
            alert_viewTime = _viewTime;
            Log.i(TAG, "_viewTime: " + _viewTime);  //2020.05.14 장희원
        } else {  //2020.05.14 장희원
            Log.i(TAG, "_viewTime: " + _viewTime);
        }
        /**
         * enable=true로 유지/ 계속해서 수강기록 가져오기 2020.05.14 장희원
         */
        //else if (viewTime >= maxTime) {
        //    enable = false;
        //}
        //else {
        //    //총 학습시간보다 크거나 같을때,
        //    _viewTime = maxTime;
        //}

        if (enable == false) {
            Log.i(TAG, "찾았다~~~~~~~~~~~~~ : ");
        }

        minutes = _viewTime;
        second = 60;

        Log.i(TAG, "onCreate :: " + viewTime + ", " + maxTime + ", " + minutes + ", " + second);

//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {

        //마지막으로 본 화면을 불러온다.
//                SharedPreferences savedPage = getSharedPreferences("CONTENTS", MODE_PRIVATE);
//                String page = savedPage.getString("SAVED_PAGE", "");
//
//                String[] splitRoot, splitRoot2;

        //저장된 페이지가 있는지 확인, 마지막 페이지인지 확인(마지막 페이지는 09.html)
//                if (!page.equals("")) {
//                    splitRoot = page.split("/");
//                    splitRoot2 = splitRoot[2].split(".");
//
//                    if (!splitRoot2[0].equals("09")) {
//                        fileRoot = page;
//                    }
//                }

        //중복 로그인 객체
        loginOverHelper = new Handler();


        //강의를 웹뷰에 띄운다.
        String contentsUrl;

        String domain = "";
        domain = "http://cb.egreen.co.kr/contents_m/android/" + si.getDirectoryName();

        if (jucha == 0) {
            loadingTxt = "오리엔테이션을 불러오는 중입니다.";
            contentsUrl = domain + "/00/orientation.html";
        } else {
            loadingTxt = "강의를 불러오는 중입니다!";
            contentsUrl = domain + "/" + fileRoot;
        }

        netCheck = new NetworkStateCheck(this);
        if (netCheck.isConnectionNet()) {

            wv.loadUrl(contentsUrl);
        }
    }

    /**
     * 현재 연결이 Data인지 확인한다.
     * Data이면 true 리턴
     */
    private boolean checkDataNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return info.isAvailable();
    }

    /**
     * 현재 연결이 WIFI인지 확인한다.
     * WIFI이면 true 리턴
     */
    private boolean checkWifiNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info.isAvailable();
    }

    /* 2021.04.01 앱 캐시 지우기 코드 시작 */
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (s.equals("shared_prefs"))
                    continue; //shared_prefs 폴더의 내용은 지우지 않는다. 자동로그인 / 다지보지않기 / 학번저장 등의 정보
                else if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "***************** File /data/data/APP_PACKAGE/" + s + "DELETE *****************");
                }
            }
        }
    }
    /* 앱 캐시 지우기 코드 끝 */

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "onStart :: " + viewTime + ", " + maxTime + ", " + minutes + ", " + second);
        try {
            if (enable) {
                if (netCheck.isConnectionNet()) {
                    Log.i(TAG, "시작!" + minutes + ", " + second);
                    jindoCheck_proc(1);
                    startTimerTask();
                } else {
                    Toast.makeText(A13_Learning.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                    //네트워크 상태가 불안할 때, 처리
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

//        Log.i(TAG, "전달하는 값 ====> " + cValues.toString());

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
     * 2분마다 체크, 진도처리를 한다.
     */
    private void startTimerTask() {
        stopTimerTask();

        jindoTimerTask = new TimerTask() {
            @Override
            public void run() {
                second--;
                if (second == -1) {
                    second = 59;
                    minutes--;
                }

                Log.i(TAG, "돈다!!" + minutes + ", " + second);

                //1분 마다 로그인 중복 체크, 5초에 한번씩
                if (second % 5 == 0) {
                    //여기서 중복로그인 체크!
                    //중복 로그인 체크
                    final String result;
                    result = netConnForOverlapCheck();
                    Log.i(TAG, "중복 로그인 조회 결과 ===> " + result);

                    if (result != "") {
                        Log.i(TAG, "진도 스레드 자원 정리");
                        Log.i(TAG, "진도 스레드 종료");

                        stopTimerTask();

                        loginOverHelper.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                logoutByLoginOverlap(result);
                            }
                        }, 0);
                    }
                }

                if ((minutes % 2 == 0) && (second == 0)) {
                    if (minutes > 0) {
                        try {
                            //여기서 '처리'
                            if (netCheck.isConnectionNet()) {
                                Log.i(TAG, "처리!" + minutes + ", " + second);
                                jindoCheck_proc(2);
                            } else {
                                Toast.makeText(A13_Learning.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                                //네트워크 불안할때 처리
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                if ((minutes == 0) && (second == 0)) {
                    //여기서 '충족'
                    try {
                        if (netCheck.isConnectionNet()) {
                            Log.i(TAG, "충족!" + minutes + ", " + second);
                            jindoCheck_proc(5);
                        } else {
                            Toast.makeText(A13_Learning.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                            //네트워크 불안할 때 처리
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (minutes > maxTime) {
                    Log.i(TAG, "진도 스레드 자원 정리");
                    Log.i(TAG, "진도 스레드 종료");

                    stopTimerTask();
                }
            }
        };

        jindoTimer.schedule(jindoTimerTask, 0, 1000);
    }

    /**
     * 진도체크 쓰레드를 종료한다.
     */
    private void stopTimerTask() {
        if (jindoTimerTask != null) {
            jindoTimerTask.cancel();
            jindoTimerTask = null;
        }
    }

    /**
     * 진도처리를 한다.
     */
    public void jindoCheck_proc(int what) {
        String jindoDomain;

        ContentValues cValues = new ContentValues();
        cValues.put("cUserid", si.getUserId());
        cValues.put("cClassid", si.getClassId());

        if (what == 5) {    //2020.05.14 장희원
            //if (what == 4) {
            //페이지 체크
            Log.i(TAG, "페이지 체크");
            jindoDomain = "http://cb.egreen.co.kr/mobile_proc/mypage/course/studyPageSave_Proc2.asp";

            cValues.put("strUrl", contentsPage);
        } else {
            //시간 체크
            Log.i(TAG, "시간 체크");
            jindoDomain = "http://cb.egreen.co.kr/mobile_proc/mypage/course/studyTimeSave_Proc2.asp";

            cValues.put("nIs_Start", what);
            cValues.put("nDay", jucha);
            cValues.put("nHour", chasi);
            cValues.put("nViewCid", eCid);
        }
        try {
            if (netCheck.isConnectionNet()) {
                SetJindoNetTask setJindoNT = new SetJindoNetTask(jindoDomain, cValues);
                setJindoNT.execute();
            } else {
                Toast.makeText(A13_Learning.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "네트워크 연결이 불안불안~");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 그냥 intent 로 잘 넘어왔나 체크...
     */
    private void checkGetIntent() {
        try {
            if (jucha == 0) {
                Log.i(TAG, "주차가 없다.");
            }

            if (chasi == 0) {
                Log.i(TAG, "차시가 없다.");
            }

            if (eCid.equals("")) {
                Log.i(TAG, "cId가 없다.");
            }

            if (watchedTime.equals("")) {
                Log.i(TAG, "watchedTime가 없다.");
            }

            if (fullTime.equals("")) {
                Log.i(TAG, "fullTime가 없다.");
            }

            if (fileRoot.equals("")) {
                Log.i(TAG, "파일경로가 없다.");
            } else {
                Log.i(TAG, fileRoot + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 뒤로가기 버튼 클릭시, 정말 종료할것인지를 묻는다.
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        final Handler handler = new Handler();

        String alertTxt = "";

        if (jucha == 0) {
            alertTxt = "오리엔테이션을 종료합니다.";
        } else {
            if (minutes <= 0) {
                alertTxt = "강의시간이 충족되었습니다.\n강의를 종료합니다.";
            } else
                alertTxt = jucha + "주차 " + chasi + "차시 학습시간 " + minutes + "분 남았습니다.\n강의를 종료하시겠습니까?";   //2020.06.12 장희원
        }

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage(alertTxt);
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                if (enable) {
                    loading = null;
                    loading = new ShowLoading(A13_Learning.this, "학습시간을 저장하고있습니다.\n잠시만 기다려주세요.");
                    loading.start();

                    new Thread(new Runnable() {
                        int count = 0;

                        @Override
                        public void run() {
                            while (count < 2) {
                                ++count;

                                if (count == 2) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            moveToA7_ClassRoom();
                                        }
                                    });
                                }

                                try {
                                    Thread.sleep(750);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else {
                    moveToA7_ClassRoom();
                }
            }
        });
        ab.setNegativeButton("취소", null);
        ab.show();
    }

    /**
     * A7_ClassRoom으로 이동
     */
    public void moveToA7_ClassRoom() {
        Intent intent = new Intent(this, A10_ClassRoom.class);
        intent.putExtra("studyInfo", si);
        startActivity(intent);
        finish();
    }

    private void Alert() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);
        ab.setMessage("네트워크 연결이 끊켰습니다. 다시 로그인 해주세요.");
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveToA2_Login();

            }
        }).show();
    }

   /* public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig){
        if(isInPictureInPictureMode) {
            Toast.makeText(this, "PIP Mode",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "not PIP Mode",Toast.LENGTH_SHORT).show();
        }
    }*/
    /**
     * 중복 로그인 알림
     */
    private void logoutByLoginOverlap(String result) {
        String[] arrResult = result.split(",");
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);
        ab.setMessage("중복 로그인이 감지되어 로그아웃 되었습니다.\n\n" +
                "접 속 시 간 : " + arrResult[1] + "\n" +
                "접 속 IP : " + arrResult[2] + "\n" +
                "접 속 OS : " + arrResult[3] + "\n" +
                "접속Browser : " + arrResult[4]);
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), A02_Login.class);
                startActivity(intent);

            }
        }).show();
    }

    /**
     * A2_Login으로 이동
     */
    public void moveToA2_Login() {
        SharedPreferences savedCertyState = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedCertyState.edit();
        editor.putBoolean("certyState", false);
        editor.commit();

        startActivity(new Intent(this, A02_Login.class));
        finish();
    }

    /**
     * 이 화면을 벗어나게 될때 강의가 동영상 재생중이었다면, 멈추도록 한다.
     * 전화 왔을때, 카톡할때 등등
     */
    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "onPause :: " + viewTime + ", " + maxTime + ", " + minutes + ", " + second);

        if (netCheck.isConnectionNet()) {
            // 동영상 플레이어를 일시정지 한다.
            if (wv != null) {
                wv.loadUrl("javascript:background_pause();");
            }

            if (enable) {
                //진도체크 - 종료
                jindoCheck_proc(3);
                stopTimerTask();
            }
        }

        if (loading != null) {
            loading.stop();
        }
    }

    @Override
    protected void onDestroy() {
        //액티비티 종료될때 반드시 제거
        jindoTimer.cancel();
        Log.i(TAG, "onDestroy()");

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
                loading = new ShowLoading(A13_Learning.this, loadingTxt);
                loading.start();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
            //컨텐츠 실행중
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.i(TAG, "onPageFinished()");

            //페이지 로딩 완료
            if (loading != null) {
                Log.i(TAG, "loading.stop();");
                loading.stop();
                loading = null;
            }
        }
    }

    /**
     * 웹 -> 안드로이드 함수 호출
     */
    private class JSCall {
        @JavascriptInterface
        public void toAndroid(final String msg) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "JS 메세지: " + msg);

                    //페이지 이어보기... 음
//                    SharedPreferences savedPage = getSharedPreferences("CONTENTS", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = savedPage.edit();
//                    editor.putString("SAVED_PAGE", msg);
//                    editor.commit();

                    contentsPage = msg;
                    if (enable) {
                        if (netCheck.isConnectionNet()) {
                            //페이지 열람 체크
                            //jindoCheck_proc(4);
                            jindoCheck_proc(5);     //2020.05.14 장희원
                        } else {
                            Toast.makeText(A13_Learning.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                            //네트워크 연결 불안할때 처리
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 과목 리스트를 가져오기 위한 AsyncTask
     */
    public class SetJindoNetTask extends AsyncTask<Void, Void, String> {
        private final String url;
        private final ContentValues values;

        public SetJindoNetTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result;

            NetworkConnection nc = new NetworkConnection();
            result = nc.request(url, values);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result = s;

            Log.i(TAG, "결과 :" + result);

            //DB에 insert하는 일이라 오류 처리만 하면 된다.
            if (result.equals("FAIL")) {
                //네트워크 통신 오류
                Log.i(TAG, "진도처리 통신 오류 발생!!");
            }
        }
    }

    //TODO PIP 모드로 전환 실시
    public void setPipMode(){
        /** [pip 모드 설명]
         *  1. pip 는 안드로이드 8.0 오레오 이상에서 활동을 수행할 수 있습니다
         *  2. pip 는 특수한 유형의 멀티 윈도우 모드입니다
         *  3. pip 모드 예로는 유튜브 창모드 전환 등이 있습니다
         * */
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("---","---");
                Log.w("//===========//","================================================");
                Log.d("","\n"+"[MainActivity > setPipMode() PIP 모드 실행 실시]");
                Log.d("","\n"+"[결과 : "+String.valueOf("오레오 버전 이상 > 전환 수행")+"]");
                Log.w("//===========//","================================================");
                Log.d("---","---");

                /** [기본 방법]
                 PictureInPictureParams.Builder pipBuilder = new PictureInPictureParams.Builder();
                 enterPictureInPictureMode(pipBuilder.build());
                 */

                /** [화면 사이즈 조절 방법]*/
                Rational aspectRatio = new Rational(16, 9);
                PictureInPictureParams params = new PictureInPictureParams.Builder()
                        .setAspectRatio(aspectRatio).build();
                enterPictureInPictureMode(params);

            }
            else{
                Log.d("---","---");
                Log.e("//===========//","================================================");
                Log.d("","\n"+"[MainActivity > setPipMode() PIP 모드 실행 실시]");
                Log.d("","\n"+"[결과 : "+String.valueOf("오레오 버전 미만 > 전환 실패")+"]");
                Log.e("//===========//","================================================");
                Log.d("---","---");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //홈버튼 클릭이벤트
    @Override
    protected void onUserLeaveHint() {
        setPipMode();

    }
}
