package com.egreen2.egeen2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.egreen2.egeen2.Data.StudyInfo;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A06_Telephone.java
    [설명] 전화상담
    [작성자] 장희원
    [작성일시]
 */

public class A06_Telephone extends AppCompatActivity {

    private static final String TAG = A06_Telephone.class.getSimpleName();
    private final Context context = this;
    NetworkAsyncTasker asyncTask;
    StudyInfo si;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a06_telephone);
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");
        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a06_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //메뉴 버튼 이미지 지정

        ImageView imageView = findViewById(R.id.logo);
        mDrawerLayout = findViewById(R.id.drawer_layout);

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
         * 로그인 전 화면에서 넘어왔을시 홈화면 로고 클릭시 로그인 전 화면으로 이동
         */
        Intent intent1 = getIntent();
        int loginstate = intent1.getIntExtra("loginstate", 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginstate == 1) {
                    Intent sendintent = new Intent(getApplicationContext(), before_Main.class);
                    startActivity(sendintent);
                } else {
                    Intent sendintent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(sendintent);
                }

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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.noticeboard) {
                    Intent intent = new Intent(getApplicationContext(), A05_NoticeBoard.class);
                    startActivity(intent);
                } else if (id == R.id.curriculum) {
                    Intent intent = new Intent(getApplicationContext(), A08_Curriculum.class);
                    startActivity(intent);
                } else if (id == R.id.guide) {
                    Intent intent = new Intent(getApplicationContext(), A07_Guide.class);
                    startActivity(intent);
                } else if (id == R.id.classroom) {
                    Intent intent = new Intent(getApplicationContext(), A09_Classroom.class);
                    startActivity(intent);
                } else if (id == R.id.apply) {
                    Intent intent = new Intent(getApplicationContext(), A11_Apply.class);
                    startActivity(intent);
                } else if (id == R.id.support) {
                    Intent intent = new Intent(getApplicationContext(), A06_Support.class);
                    startActivity(intent);
                } else if (id == R.id.setting) {
                    Intent intent = new Intent(getApplicationContext(), A12_Setting.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    public void go_A06_consultingT(View view) {     //학습상담 및 설계 전화
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1544-8463")));
    }

    public void go_A06_operationT(View view) {      //학사운영지원 전화
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02-540-7802")));
    }

    public void go_A06_PCT(View view) {             //시스템 오류 및 설계 PC 전화
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02-540-7805")));
    }

    public void go_A06_mobileT(View view) {         //시스템 오류 및 설계 모바일 전화
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02-2088-8407")));
    }

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

    }          //로그인 페이지로 이동

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

    public void aaa() {
        String result;
        result = netConnForOverlapCheck();
        Log.i(TAG, "중복 로그인 결과 ===> " + result);
    }

    public String netConnForOverlapCheck() {
        String url = "http://cb.egreen.co.kr/mobile_proc/loginOverlap_check_m2.asp";
        ContentValues cValues = new ContentValues();
        cValues.put("userId", si.getUserId());
        cValues.put("ulNum", si.getLoginNumber());

        Log.i(TAG, "전달하는 값 ====> " + cValues);

        //네트워크 통신으로 데이터를 가져온다.
        asyncTask = new NetworkAsyncTasker((NetworkAsyncTasker.AsyncResponse) this, url, cValues);
        try {
            return asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public void onBackPressed() {   //뒤로가기 버튼을 클릭 시 드로어 레이아웃 닫기
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
