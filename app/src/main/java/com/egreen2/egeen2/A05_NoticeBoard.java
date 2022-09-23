package com.egreen2.egeen2;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.egreen2.egeen2.Adapter.A05_NoticeAdapter;
import com.egreen2.egeen2.Data.A05_NoticeListData;
import com.egreen2.egeen2.Data.StudyInfo;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A05_NoticeBoard.java.java
    [설명] 공지사항 게시판
    [작성자] 장희원
    [작성일시]
 */

public class A05_NoticeBoard extends AppCompatActivity implements NetworkAsyncTasker.AsyncResponse {
    private static final String TAG = A05_NoticeBoard.class.getSimpleName();

    ListView a05_noticeList;      //공지사항 리스트
    //AsyncTask 결과값을 받기 위한 변수
    NetworkAsyncTasker asyncTask;
    //네트워크 상태 체크
    NetworkStateCheck netCheck;
    String id, userName, loginNumber;
    StudyInfo si;
    ProgressDialog loadingDialog;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a05_notice_board);



        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a05_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu); //메뉴 버튼 이미지 지정

        /* View 연결 */
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        a05_noticeList = findViewById(R.id.a05_noticeList); //공지사항
        NavigationView navigationView = findViewById(R.id.nav_view);
        ImageView imageView = findViewById(R.id.logo);
        //네비게이션 헤더에 참조할때는 아래와같이 참조해야한다.

        TextView head_name = navigationView.getHeaderView(0).findViewById(R.id.head_name);
        TextView head_studentid = navigationView.getHeaderView(0).findViewById(R.id.head_StudentID);

        SharedPreferences sharedPreferences1 = getSharedPreferences("login", MODE_PRIVATE);
        int a = sharedPreferences1.getInt("aaa", 0);

        //저장된 유저이름과 학번을 가져와서 네비게이션 헤더에 출력
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", ""); //유저이름
        String StudentID = sharedPreferences.getString("UID", ""); //유저학번   id = StudentID;

        id = StudentID;
        loginNumber = si.getLoginNumber();

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
       /* ContentValues cValue = new ContentValues();
        cValue.put("userid", id);
        String url = "http://cb.egreen.co.kr/mobilee_proc/join/join_proc_m2.asp";
        NetworkConnect networkConnect = new NetworkConnect(url, cValue);
        networkConnect.execute();*/

        /* 네트워크 연결 상태 확인 */
        netCheck = new NetworkStateCheck(this);
        if (netCheck.isConnectionNet()) {
            init();
        } else {
            Alert();
            Toast.makeText(A05_NoticeBoard.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
        }

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
                } else if (id == R.id.curriculum) {
                    moveActivity(A08_Curriculum.class);
                } else if (id == R.id.guide) {
                    moveActivity(A07_Guide.class);
                } else if (id == R.id.classroom) {
                    moveActivity(A09_Classroom.class);
                } else if (id == R.id.apply) {
                    moveActivity(A11_Apply.class);
                } else if (id == R.id.support) {
                    moveActivity(A06_Support.class);
                } else if (id == R.id.setting) {
                    moveActivity(A12_Setting.class);
                }

                return true;
            }
        });
        init();
        StudyInfo si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");
        try {
            Log.i(TAG, "MainActivity 에서 전달 받은 =====> " + si.getLoginNumber());
            Log.i(TAG, "MainActivity 에서 전달 받은 =====> " + si.getUserId());
            Log.i(TAG, "MainActivity 에서 전달 받은 =====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "MainActivity 에서 전달 받은 =====> 없음");
        }
    }   //onCreate 종료


    private void init() {

        /* 공지사항 */
        netConnForGetNotify();
    }

    //
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 공지사항을 받아오는 웹 서버의 통신
     */
    private void netConnForGetNotify() {
        Log.i(TAG, "통신");

        String url = "http://cb.egreen.co.kr/mobile_proc/index_m2.asp";

        asyncTask = new NetworkAsyncTasker(this, url, null);
        asyncTask.execute();
    }

    /**
     * 공지사항 AsyncTask 결과
     */
    @Override
    public void processFinish(String result, String what) {
        Log.i(TAG, "====>>>" + result);

        if (result.equals("FAIL")) {
            //통신오류
            Log.i(TAG, "통신오류!!");
        } else {
            parsing_json(result);
            Log.i(TAG, "값을 가져옴!!");

        }
    }

    private void loginAlert() {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        //  ab.setTitle("오리엔테이션");
        ab.setMessage("로그인 후 이용해 주세요");
        ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), A02_Login.class);
                startActivity(intent);

            }
        });
        ab.setNegativeButton("아니오", null);
        ab.show();
    }

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


    private void moveActivity(Class activity) {
        StudyInfo si = new StudyInfo();
        si.setUserId(id);
        si.setUserName(userName);
        si.setLoginNumber(loginNumber);
        Intent intent = new Intent(this, activity);
        intent.putExtra("studyInfo", si);
        startActivity(intent);

        try {
            Log.i(TAG, "공지사항에서 전달하는 =====> " + si.getUserId());
            Log.i(TAG, "공지사항에서 전달하는 =====> " + si.getLoginNumber());
            Log.i(TAG, "공지사항에서 전달하는 =====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "공지사항에서 전달하는 =====> 없음");
        }
//        finish();

    }

    private void Alert() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);
        ab.setMessage("네트워크 연결이 끊켰습니다. 네트워크 연결상태를 확인해주세요.");
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                moveTaskToBack(true);
                finishAndRemoveTask();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }).show();
    }

    private void parsing_json(String result) {
        JSONObject jsObj = new JSONObject();
        final ArrayList<A05_NoticeListData> noticeListData = new ArrayList<A05_NoticeListData>();

        try {
            JSONArray jsArray = new JSONArray(result);

            for (int i = 0; i < jsArray.length(); i++) {
                jsObj = jsArray.getJSONObject(i);

                String flag = jsObj.getString("Flag");
                String noticeTitle = jsObj.getString("strTitle");
                String noticeCid = jsObj.getString("cId");
                String noticeDate = jsObj.getString("dateRegist");

                int noticeFlag = Integer.parseInt(flag);
                int noticeimportance = 0;

                if (noticeFlag != 0) {
                    noticeimportance = 0;
                } else {
                    noticeimportance = 1;
                }
                /* ListView에 data를 넣는 작업 */
                A05_NoticeListData data = new A05_NoticeListData(noticeimportance, noticeCid, noticeTitle, noticeDate);
                noticeListData.add(data);
            }

            /* ListView에 data를 넣기 위한 Adapter 설정 */
            A05_NoticeAdapter a05_noticeAdapter = new A05_NoticeAdapter(getApplicationContext(), R.layout.a05_cs_notice_list, noticeListData);
            a05_noticeAdapter.notifyDataSetChanged();
            a05_noticeList.setAdapter(a05_noticeAdapter);
            a05_noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG, "공지사항 URL: " + noticeListData.get(position).getNoticeCid());
                    if (netCheck.isConnectionNet()) {
                        Intent intent = new Intent(A05_NoticeBoard.this, A05_NoticeWebView.class);
                        intent.putExtra("NOTICE", noticeListData.get(position).getNoticeCid());
                        startActivity(intent);
                    } else {
                        Toast.makeText(A05_NoticeBoard.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            Log.i(TAG, "notice JSON Exc: " + e.getMessage());
        }
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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

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
