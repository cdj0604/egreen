package com.egreen2.egeen2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final Context context = this;
    // ** View 변수 선언 **
    String storeVersion = "2.0.0", appVersion = ""; //storeVersion 은 업데이트시 수기로 수정
    ViewFlipper a1_bannerFlipper;   // 배너
    ImageView a1_bannerImg1, a1_bannerImg2, a1_bannerImg3;  // Flipper에 들어가는 이미지
    PackageInfo packageInfo = null;
    String id;
    String userName;
    String loginNumber;
    StudyInfo si;
    boolean _isNeedCertyLogin, saveLoginData;
    NetworkStateCheck netCheck;
    private long backKeyPressedTime = 0;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //비정상종료 테스트
        //파이어베이스 stop 프로젝트에 오류보고서 확인. 필요시 버튼생성후 주석제거
       /* Button btn_stop = (Button)findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new IllegalStateException("Firebase Crash Test");
            }
        });*/


        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a01_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu); //메뉴 버튼 이미지 지정

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView head_name = navigationView.getHeaderView(0).findViewById(R.id.head_name);
        TextView head_studentid = navigationView.getHeaderView(0).findViewById(R.id.head_StudentID);
        TextView AppVersion = navigationView.getHeaderView(0).findViewById(R.id.AppVersion);
        TextView StroeVersion = navigationView.getHeaderView(0).findViewById(R.id.StroeVersion);

        //저장된 유저이름과 학번을 가져와서 네비게이션 헤더에 출력
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", ""); //유저이름
        String StudentID = sharedPreferences.getString("UID", ""); //유저학번

        Log.i(TAG, "인증서 로그인 성공 상태를 유지 중인가?" + sharedPreferences.getBoolean("certyState", false));
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");

        id = StudentID;
        //loginNumber = si.getLoginNumber();


        head_name.setText(userName + "님");
        head_studentid.setText(StudentID);


        //데이터 삭제
        //clearApplicationData();

        //나의강의실에서 강의듣기로 가기위해선 아래코드 필요 , 강의목록 뜨는거랑은 상관없음
        String url = "http://cb.egreen.co.kr/mobile_proc/login/new/login_insert_m2.asp";
        ContentValues cValues = new ContentValues();
        cValues.put("userId", id);
        cValues.put("uMobileBrand", Build.BRAND + "::" + Build.MODEL);
        MainActivity.LoginSuccessNetTask loginSuccessNT = new LoginSuccessNetTask(url, cValues);
        loginSuccessNT.execute();


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
                if (id == R.id.noticeboard) {
                    moveActivity(A05_NoticeBoard.class);
                } else if (id == R.id.curriculum) {
                    moveActivity(A08_Curriculum.class);
                } else if (id == R.id.home) {
                    moveActivity(MainActivity.class);
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
        // ** View 연결 **
        a1_bannerFlipper = findViewById(R.id.a01_bannerFlipper);     // 배너 Flipper

        // ** 배너 이미지 자동으로 넘김 **
        //   a1_bannerFlipper.setFlipInterval(3000);
        //  a1_bannerFlipper.startFlipping();

        /* 네트워크 연결 상태 확인 */
        netCheck = new NetworkStateCheck(this);
        if (netCheck.isConnectionNet()) {
            init();
        } else {
            Alert();
            //Toast.makeText(MainActivity.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
        }

        /**
         * 1.현재 설치된 앱버전, 배포된앱버전 가져오기
         * 2.가져온 현재버전 네비게이션 헤더에 뿌려주기
         * 최초 출시이후 다음업데이트시에 출시된 앱버전도 가져와서 뿌려주고, 자동업데이트 구현필요
         */

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = packageInfo.versionName; //현재 버전 저장
        int versionCode = packageInfo.versionCode;
        Log.d("versionName", versionName);
        Log.d("versionCode", String.valueOf(versionCode));
        AppVersion.setText(versionName);
        StroeVersion.setText(storeVersion);


       /* try {
            Log.i(TAG, "A2_Login 에서 전달 받은 =====> " + si.getUserId());
            Log.i(TAG, "A2_Login 에서 전달 받은 =====> " + si.getLoginNumber());
            Log.i(TAG, "A2_Login 에서 전달 받은 =====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "A2_Login 에서 전달 받은 =====> 없음");
        }*/

        SharedPreferences sharedPreferences1 = getSharedPreferences("login", MODE_PRIVATE);
        int a = sharedPreferences1.getInt("aaa", 0);

        Log.i("aaa는", String.valueOf(a));

    }
    //oncreate 종료

    private void init() {
        // ** 배너 **
        showBanner();
    }


    /**
     * Activity 를 이동한다.
     */
    private void moveActivity(Class activity) {
        StudyInfo si = new StudyInfo();
        si.setUserId(id);
        si.setUserName(userName);
        si.setLoginNumber(loginNumber);
        Intent intent = new Intent(this, activity);
        intent.putExtra("studyInfo", si);
        startActivity(intent);

        try {
            Log.i(TAG, "MainAcivity에서  전달하는 =====> " + si.getUserId());
            Log.i(TAG, "MainAcivity에서  전달하는 =====> " + si.getLoginNumber());
            Log.i(TAG, "MainAcivity에서  전달하는 =====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "MainAcivity에서  =====> 없음");
        }
//        finish();

    }

    /**
     * 인텐트 모음
     */
    private void A05_Intent() {     //공지사항 페이지로 이동
        moveActivity(A05_NoticeBoard.class);
        //Intent intent = new Intent(getApplicationContext(), A05_NoticeBoard.class);
        //startActivity(intent);
        //
    }

    private void A08_Intent() {     //전체교육과정 페이지로 이동
        moveActivity(A08_Curriculum.class);

    }

    private void A07_Intent() {     //사용자 가이드 페이지로 이동
        moveActivity(A07_Guide.class);

    }

    private void A09_Intent() {     //나의 강의실 페이지로 이동
        moveActivity(A09_Classroom.class);
        //  Intent intent = new Intent(getApplicationContext(), A09_Classroom.class);
        //  startActivity(intent);
        //
    }

    private void A11_Intent() {     //수강신청 페이지로 이동
        moveActivity(A11_Apply.class);

    }

    private void A06_Intent() {     //학습지원 페이지로 이동
        moveActivity(A06_Support.class);

    }

    private void A06T_Intent() {    //전화상담 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A06_Telephone.class);
        startActivity(intent);

    }

    private void A06Q_Intent() {    //빠른상담 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A06_QuickAdvice.class);
        startActivity(intent);

    }

    private void A02_Intent() {     //로그인 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A02_Login.class);
        startActivity(intent);

    }

    private void A12_setting() {
        moveActivity(A12_Setting.class);

    }

    private void go_logout() {
        Intent intent = new Intent(getApplicationContext(), before_Main.class);
        startActivity(intent);

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

    public void go_logout(View view) {
        go_logout();
    }

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

    /**
     * 로그인 기록을 남기기 위한 AsyncTask
     */
    private class LoginSuccessNetTask extends AsyncTask<Void, Void, String> {
        private final String url;
        private final ContentValues values;

        public LoginSuccessNetTask(String url, ContentValues values) {
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
            try {


                if (result.equals("FAIL")) {
                    //네트워크 통신 오류
                    Alert();
                } else {
                    String[] arrResult = new String[0];

                    if (result != "") {
                        arrResult = result.split(",");
                        Log.i(TAG, "arrResult =>" + arrResult[0] + ", arrResult[1] =>" + arrResult[1]);

                        if (result.equals("Err")) {
                            android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(MainActivity.this);
                            ab.setMessage("죄송합니다.\n로그인 중 예상치 않은 오류가 발생했습니다\n본 원으로 오류메세지를 알려주세요!\n==오류 내용==\n");
                            ab.setPositiveButton("확인", null);
                            ab.show();
                        } else {
                            loginNumber = arrResult[1];


                        }
                    } else {
                        Log.i(TAG, "전달 받은 값 없음");
                    }
                }
            } catch (NullPointerException e) {

            }
        }
    }


}

