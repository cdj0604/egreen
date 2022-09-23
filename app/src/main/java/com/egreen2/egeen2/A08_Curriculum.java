package com.egreen2.egeen2;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.egreen2.egeen2.Data.StudyInfo;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A08_Curriculum.java
    [설명] 전체 교육과정
    [작성자] 장희원
    [작성일시]
 */

public class A08_Curriculum extends AppCompatActivity {
    private static final String TAG = A08_Curriculum.class.getSimpleName();
    String id, userName, loginNumber;
    StudyInfo si;
    String iniUrl = "";
    String list, list1, list2;
    TextView textView, textView2, textView3;
    ProgressDialog loadingDialog;
    ListView a08_curriculumList;    //교육과정 리스트
    //AsyncTask 결과 값을 받기 위한 변수
    NetworkAsyncTasker asyncTask;
    //네트워크 상태 체크
    NetworkStateCheck netCheck;
    WebView webView;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a08_curriculum);
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");
        webView = findViewById(R.id.webview);


        /* 약관을 보여줄때까지 로딩 Dialog 설정 */
        loadingDialog = new ProgressDialog(A08_Curriculum.this);
        loadingDialog.setMessage("교육과정을 불러오는 중입니다.");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
        /* WebView 설정 */
        webView.setWebViewClient(new WebBrowserClient());
        webView.getSettings().setJavaScriptEnabled(true);
        //  webView.setWebViewClient(new WebViewClientClass());
        webView.loadUrl("https://mcb.egreen.co.kr/m/default_m.asp");


        webView.getSettings().setSupportZoom(true);    //확대/축소 사용할 수 있도록 설정
        webView.getSettings().setBuiltInZoomControls(true);    //안드로이드에서 제공하는 줌 아이콘을 사용할 수 있도록 설정
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //캐시모드를 사용하지 않고 네트워크를 통해서만 호출
        webView.getSettings().setLoadWithOverviewMode(true);   //웹뷰 화면에 맞게 출력
        webView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.clearCache(true);
        webView.getSettings().setLoadWithOverviewMode(true);   //웹뷰 화면에 맞게 출력
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setTextZoom(100);  //웹뷰 폰트 크기 고정
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);



        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a08_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu); //메뉴 버튼 이미지 지정

        try {
            Log.i(TAG, "받은값 ====> " + si.getUserId());
            Log.i(TAG, "받은값 ====> " + si.getLoginNumber());
            Log.i(TAG, "받은값 ====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "받은값 ====> 없음");
        }

        /* View 연결 */
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ImageView imageView = findViewById(R.id.logo);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        //네비게이션 헤더에 참조할때는 아래와같이 참조해야한다.
        TextView head_name = navigationView.getHeaderView(0).findViewById(R.id.head_name);
        TextView head_studentid = navigationView.getHeaderView(0).findViewById(R.id.head_StudentID);

        //저장된 유저이름과 학번을 가져와서 네비게이션 헤더에 출력
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", ""); //유저이름
        String StudentID = sharedPreferences.getString("UID", ""); //유저학번
        head_name.setText(userName + "님");
        head_studentid.setText(StudentID);

        /**
         * 스토어버전 , 현재버전 가져오기 -> 네비게이션 헤더에 뿌리기
         */
        String storeVersion = getString(R.string.store); //storeVersion 은 업데이트시 수기로 수정
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = packageInfo.versionName; //현재 버전 저장
        TextView AppVersion = navigationView.getHeaderView(0).findViewById(R.id.AppVersion);
        TextView StroeVersion = navigationView.getHeaderView(0).findViewById(R.id.StroeVersion);
        AppVersion.setText(versionName);
        StroeVersion.setText(storeVersion);

        id = StudentID;
        loginNumber = si.getLoginNumber();

        ContentValues cValue = new ContentValues();
        cValue.put("userid", id);
        String url = "http://cb.egreen.co.kr/mobile_proc/login/logout_proc.asp";
        NetworkConnect networkConnect = new NetworkConnect(url, cValue);
        networkConnect.execute();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                /**
                 * 드로어 레이아웃 메뉴
                 */
                if (id == R.id.home) {
                    moveActivity(MainActivity.class);
                } else if (id == R.id.noticeboard) {
                    moveActivity(A05_NoticeBoard.class);
                } else if (id == R.id.setting) {
                    moveActivity(A12_Setting.class);
                } else if (id == R.id.guide) {
                    moveActivity(A07_Guide.class);
                } else if (id == R.id.classroom) {
                    moveActivity(A09_Classroom.class);
                } else if (id == R.id.apply) {
                    moveActivity(A11_Apply.class);
                } else if (id == R.id.support) {
                    moveActivity(A06_Support.class);
                }
                return true;
            }
        });

    } //onCreate 종료

    public void go_logout(View view) {
        //다이얼로그 예 눌렀을때 인텐트실행
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("로그아웃 하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences sharedPreferences = getSharedPreferences("autologin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("login", 2);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), before_Main.class);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void moveActivity(Class activity) {
        StudyInfo si = new StudyInfo();
        si.setUserId(id);
        si.setUserName(userName);
        si.setLoginNumber(loginNumber);
        Intent intent = new Intent(this, activity);
        intent.putExtra("studyInfo", si);
        startActivity(intent);

        try {
            Log.i(TAG, "전체교육과정 에서 전달하는 =====> " + si.getUserId());
            Log.i(TAG, "전체교육과정 에서 전달하는 =====> " + si.getLoginNumber());
            Log.i(TAG, "전체교육과정 에서 전달하는 =====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "전체교육과정 에서 전달하는 =====> 없음");
        }
//        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   // 왼쪽 상단 버튼 눌렀을 때
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // Back 키를 눌러서 뒤로가기 Start
    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        if ((KeyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return super.onKeyDown(KeyCode, event);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    public class WebViewClientClass extends WebViewClient {

    }

    public class WebBrowserClient extends WebViewClient {

        //웹뷰 내 페이지 이동 가능
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //웹뷰 로딩 완료 전 까지 다이얼로그 띄움
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


    private class NetworkConnect extends AsyncTask<Void, Void, String> {
        private final String url;
        private final ContentValues values;

        public NetworkConnect(String url, ContentValues value) {
            this.url = url;
            this.values = value;
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

            if (result.equals("FAIL")) {
                //네트워크 통신 오류
            } else {
                String[] arrResult = new String[0];

                if (result != "") {
                    arrResult = result.split(",");
//                    Log.i(TAG, "arrResult =>" + arrResult[0] + ", arrResult[1] =>" + arrResult[1]);
//                        loginNumber = arrResult[1];


                } else {
                    Log.i(TAG, "전달 받은 값 없음");
                }
            }
        }


    }
}