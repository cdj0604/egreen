package com.egreen2.egeen2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
    [파일명] A06_Support.java
    [설명] 학습지원
    [작성자] 장희원
    [작성일시]
 */

public class A06_Support extends AppCompatActivity {

    private static final String TAG = A06_Support.class.getSimpleName();
    private final Context context = this;
    String id, userName, loginNumber;
    StudyInfo si;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a06_support);
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");
        Toolbar toolbar = (Toolbar) findViewById(R.id.a06_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu); //메뉴 버튼 이미지 지정

        ImageView imageView = findViewById(R.id.logo);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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
//        loginNumber = si.getLoginNumber();

        ContentValues cValue = new ContentValues();
        cValue.put("userid", id);
        String url = "http://cb.egreen.co.kr/mobile_proc/login/logout_proc.asp";
        NetworkConnect networkConnect = new NetworkConnect(url, cValue);
        networkConnect.execute();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

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
                } else if (id == R.id.curriculum) {
                    moveActivity(A08_Curriculum.class);
                } else if (id == R.id.guide) {
                    moveActivity(A07_Guide.class);
                } else if (id == R.id.classroom) {
                    moveActivity(A09_Classroom.class);
                } else if (id == R.id.apply) {
                    moveActivity(A11_Apply.class);
                } else if (id == R.id.setting) {
                    moveActivity(A12_Setting.class);
                }
                return true;
            }
        });
    }

    /**
     * 인텐트 모음
     */
    public void go_A06_FAQBoard(View view) {    //FAQ 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A06_FAQ.class);
        startActivity(intent);

    }

    public void go_A06_Telephone(View view) {   //전화상담 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A06_Telephone.class);
        startActivity(intent);

    }

    public void go_A06_QuickAdvice(View view) { //빠른상담 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A06_QuickAdvice.class);
        startActivity(intent);

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
    public void onBackPressed() {   //뒤로가기 버튼을 클릭 시 드로어 레이아웃 닫기
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            Log.i(TAG, "학습지원에서 전달하는 ===> " + si.getUserId());
            Log.i(TAG, "학습지원에서 전달하는 ===> " + si.getLoginNumber());
            Log.i(TAG, "학습지원에서 전달하는 ===> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "설정에서 전달하는 =====> 없음");
        }
//        finish();

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

                }
            }
        }

    }
}
