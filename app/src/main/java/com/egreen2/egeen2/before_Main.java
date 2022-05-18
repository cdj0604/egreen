package com.egreen2.egeen2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.egreen2.egeen2.Data.StudyInfo;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] MainActivity.java => 파일명 A01_MAin으로 수정 필요
    [설명] 메인
    [작성자] 장희원
    [작성일시] 2021.03.26
 */

public class before_Main extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final Context context = this;
    // ** View 변수 선언 **
    ViewFlipper a1_bannerFlipper;   // 배너
    ImageView a1_bannerImg1, a1_bannerImg2, a1_bannerImg3;  // Flipper에 들어가는 이미지
    PackageInfo packageInfo = null;
    String id;
    String userName;
    String loginNumber;
    StudyInfo si;
    private long backKeyPressedTime = 0;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_main);


        SharedPreferences sharedPreferences1 = getSharedPreferences("login", MODE_PRIVATE);
        int a = sharedPreferences1.getInt("aaa", 0);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a01_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu); //메뉴 버튼 이미지 지정

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);


        //저장된 유저이름과 학번을 가져와서 네비게이션 헤더에 출력
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", ""); //유저이름
        String StudentID = sharedPreferences.getString("UID", ""); //유저학번
        id = StudentID;

        /**
         * 스토어버전 , 현재버전 가져오기 -> 네비게이션 헤더에 뿌리기
         */
        String storeVersion = "2.0.0"; //storeVersion 은 업데이트시 수기로 수정
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
        //


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
                    A05_Intent();
                } else if (id == R.id.curriculum) {
                    A08_Intent();
                } else if (id == R.id.guide) {
                    A07_Intent();
                } else if (id == R.id.classroom) {
                    A09_Intent();
                } else if (id == R.id.apply) {
                    A11_Intent();
                } else if (id == R.id.support) {
                    A06_Intent();
                } else if (id == R.id.setting) {
                    A12_setting();
                }

                return true;
            }
        });
        // ** View 연결 **
        a1_bannerFlipper = findViewById(R.id.a01_bannerFlipper);     // 배너 Flipper

        // ** 배너 이미지 자동으로 넘김 **
        //   a1_bannerFlipper.setFlipInterval(3000);
        //  a1_bannerFlipper.startFlipping();

        init();


        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");

        try {
            Log.i(TAG, "A2_Login 에서 전달 받은 =====> " + si.getUserId());
            Log.i(TAG, "A2_Login 에서 전달 받은 =====> " + si.getLoginNumber());
            Log.i(TAG, "A2_Login 에서 전달 받은 =====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "A2_Login 에서 전달 받은 =====> 없음");
        }


    }


    private void init() {
        // ** 배너 **
        showBanner();
    }


    /**
     * 인텐트 모음
     */
    private void A05_Intent() {     //공지사항 페이지로 이동
        loginAlert();
        //Intent intent = new Intent(getApplicationContext(), A05_NoticeBoard.class);
        //startActivity(intent);

    }

    private void A08_Intent() {     //전체교육과정 페이지로 이동

        loginAlert();
        //Intent intent = new Intent(getApplicationContext(), A08_Curriculum.class);
        //startActivity(intent);

    }

    private void A07_Intent() {     //사용자 가이드 페이지로 이동
        loginAlert();
        //Intent intent = new Intent(getApplicationContext(), A07_Guide.class);
        //startActivity(intent);

    }

    private void A09_Intent() {     //나의 강의실 페이지로 이동

        loginAlert();
        //  Intent intent = new Intent(getApplicationContext(), A09_Classroom.class);
        //  startActivity(intent);

    }

    private void A11_Intent() {     //수강신청 페이지로 이동
        loginAlert();

    }

    private void A06_Intent() {     //학습지원 페이지로 이동
        loginAlert();
        //Intent intent = new Intent(getApplicationContext(), A06_Support.class);
        //startActivity(intent);

    }

    private void A06T_Intent() {    //전화상담 페이지로 이동
        // loginAlert();
        Intent intent = new Intent(getApplicationContext(), A06_Telephone.class);
        intent.putExtra("loginstate", 1);
        startActivity(intent);

    }

    private void A06Q_Intent() {    //빠른상담 페이지로 이동
        // loginAlert();
        Intent intent = new Intent(getApplicationContext(), A06_QuickAdvice.class);
        intent.putExtra("loginstate", 1);
        startActivity(intent);

    }

    private void A02_Intent() {     //로그인 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A02_Login.class);
        startActivity(intent);

    }

    private void A12_setting() {
        loginAlert();
        //Intent intent = new Intent(getApplicationContext(), A12_Setting.class);
        //startActivity(intent);

    }

    /**
     * 배너 이미지를 보여줌
     */
    private void showBanner() {
        // ** 배너 ImageView를 ArrayList에 추가
        a1_bannerImg1 = findViewById(R.id.a1_banner_Img1);  // 배너 이미지1
        a1_bannerImg2 = findViewById(R.id.a1_banner_Img2);  // 배너 이미지2
        a1_bannerImg3 = findViewById(R.id.a1_banner_Img3);  // 배너 이미지3

        Glide.with(this)
                .load("http://cb.egreen.co.kr/banner/images/image01.jpg")
                .skipMemoryCache(true)                              //메모리 캐싱 끄기
                .diskCacheStrategy(DiskCacheStrategy.NONE)      //변형된 이미지만 캐싱
                .into(a1_bannerImg1);


        //배너 클릭시 모바일 홈페이지로 이동

        a1_bannerImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("http://mcb.egreen.co.kr/m/default.asp");
                intent.setData(uri);
                startActivity(intent);

            }
        });
    }

    /**
     * 버튼 클릭
     */
    public void go_A05_NoticeBoard(View view) {
        A05_Intent();
    }   //공지사항 버튼 클릭

    public void go_A08_Curriculum(View view) {
        A08_Intent();
    }    //전체교육과정 버튼 클릭

    public void go_A07_Guide(View view) {
        A07_Intent();
    }         //사용자 가이드 버튼 클릭

    public void go_A09_Classroom(View view) {
        A09_Intent();
    }     //나의 강의실 버튼 클릭

    public void go_A11_Apply(View view) {
        A11_Intent();
    }         //수강신청 버튼 클릭

    public void go_A06_Support(View view) {
        A06_Intent();
    }       //학습지원 버튼 클릭

    public void go_A06_Telephone(View view) {
        A06T_Intent();
    }    //전화상담 버튼 클릭

    public void go_A06_QuickAdvice(View view) {
        A06Q_Intent();
    }  //빠른상담 버튼 클릭

    public void go_A02_Login(View view) {
        A02_Intent();
    }         //로그인 버튼 클릭


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void loginAlert() {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        //  ab.setTitle("오리엔테이션");
        ab.setMessage("로그인 후 이용해 주세요");
        ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                A02_Intent();
            }
        });
        ab.setNegativeButton("아니오", null);
        ab.show();
    }

    @Override
    public void onBackPressed() {

        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast toast = Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ActivityCompat.finishAffinity(this);
            System.exit(0); // 앱 프로세스 종료
        }
    }
}

