package com.egreen2.egeen2;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.egreen2.egeen2.Adapter.A08_CurriculumAdapter;
import com.egreen2.egeen2.Data.A08_CurriculumListData;
import com.egreen2.egeen2.Data.StudyInfo;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

public class A08_Curriculum extends AppCompatActivity implements NetworkAsyncTasker.AsyncResponse {
    private static final String TAG = A08_Curriculum.class.getSimpleName();
    String id, userName, loginNumber;
    StudyInfo si;
    String iniUrl = "";
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
        /* Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.a08_toolbar);
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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView imageView = findViewById(R.id.logo);

        /* 웹뷰가 모두 로딩될때까지 보여줄 Dialog */
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("잠시만 기다려주세요.");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);

        /* WebView 설정 */
        webView = findViewById(R.id.webview);//코드연결
        webView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용
        webView.loadUrl("http://mcb.egreen.co.kr/m/default2.asp");
        webView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        webView.setWebViewClient(new WebBrowserClient());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.clearCache(true);
        webView.getSettings().setTextZoom(100);  //웹뷰 폰트 크기 고정
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        /* 네트워크 연결 상태 확인 */
        netCheck = new NetworkStateCheck(this);
        if (netCheck.isConnectionNet()) {
            init();
        } else {
            Toast.makeText(A08_Curriculum.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //네비게이션 헤더에 참조할때는 아래와같이 참조해야한다.
        TextView head_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.head_name);
        TextView head_studentid = (TextView) navigationView.getHeaderView(0).findViewById(R.id.head_StudentID);

        //저장된 유저이름과 학번을 가져와서 네비게이션 헤더에 출력
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", ""); //유저이름
        String StudentID = sharedPreferences.getString("UID", ""); //유저학번
        head_name.setText(userName + "님");
        head_studentid.setText(StudentID);

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
        init();
    } //onCreate 종료

    private void init() {
        /* 교육과정 */
        netConnForGetCurriculum();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if (!(!(keyCode == KeyEvent.KEYCODE_BACK) || !webView.canGoBack())) {
            webView.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
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
     * 교육과정을 받아오는 웹 서버의 통신
     */
    private void netConnForGetCurriculum() {
        Log.i(TAG, "통신");

        String url = "http://cb.egreen.co.kr/mobile_proc/index_m2.asp";

        asyncTask = new NetworkAsyncTasker(this, url, null);
        asyncTask.execute();
    }

    /**
     * 교육과정 AsyncTask 결과
     */
    @Override
    public void processFinish(String result, String what) {
        Log.i(TAG, "=======>>" + result);

        if (result.equals("FAIL")) {
            //통신오류
            Log.i(TAG, "통신오류");
        } else {
            parsing_json(result);
            Log.i(TAG, "값을 가져옴!!");
        }
    }

    private void parsing_json(String result) {
        JSONObject jsObj = new JSONObject();
        final ArrayList<A08_CurriculumListData> curriculumListData = new ArrayList<A08_CurriculumListData>();

        try {
            JSONArray jsArray = new JSONArray(result);

            for (int i = 0; i < jsArray.length(); i++) {
                jsObj = jsArray.getJSONObject(i);

                String cClassName = jsObj.getString("cClassName");
                String cClassId = jsObj.getString("cClassId");
                String DirectoryName = jsObj.getString("DirectoryName");

                /* ListView에 data를 넣기 위한 Adapter 설정 */
                A08_CurriculumAdapter a08_curriculumAdapter = new A08_CurriculumAdapter(getApplicationContext(), R.layout.a08_curriculum_c, curriculumListData);
                a08_curriculumAdapter.notifyDataSetChanged();
                a08_curriculumList.setAdapter(a08_curriculumAdapter);
                a08_curriculumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (netCheck.isConnectionNet()) {
                            Intent intent = new Intent(A08_Curriculum.this, A08_LearningPlan.class);
                            //intent.putExtra("NOTICE", curriculumListData.get(position).getDirectoryName());
                            startActivity(intent);
                        } else {
                            Toast.makeText(A08_Curriculum.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            Log.i(TAG, "curriculum JSON Exc: " + e.getMessage());
        }
    }

    public void go_logout(View view) {
        //다이얼로그 예 눌렀을때 인텐트실행
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("로그아웃 하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
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

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    private class WebBrowserClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            Log.i(TAG, "시작 URL: " + url);
            loadingDialog.show();

            iniUrl = url;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            Log.i(TAG, "끝 URL: " + url);

            loadingDialog.dismiss();
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL", url);
            view.loadUrl(url);
            return true;
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
