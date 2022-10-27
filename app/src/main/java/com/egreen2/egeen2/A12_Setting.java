package com.egreen2.egeen2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.egreen2.egeen2.Data.StudyInfo;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A12_Setting.java
    [설명] 설정화면
    [작성자] 채동주
    [작성일시] 2021.07.20
 */

public class A12_Setting extends AppCompatActivity {
    private static final String TAG = A12_Setting.class.getSimpleName();
    private static final String MAIN_LIST = "mainList";
    private static final String OVERLAP = "overlap";
    private static final String LOGOUT = "logout";

    PackageInfo packageInfo = null;
    TextView name, number, nowAppVersion, LatestAppVersion,result_text;
    StudyInfo si;
    Button button, updatebtn, li_button;
    String versionName;
    int versionCode;
    String id;
    String userName;
    String loginNumber;
    NetworkStateCheck netCheck;
    NetworkAsyncTasker asyncTask;

    private DrawerLayout mDrawerLayout;


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

    /**
     * 플레이스토어로 이동~~~
     */
    public static void goMarket(Activity caller, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a12_setting);


        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");
        ImageView imageView = findViewById(R.id.logo);
        // Button go_info = (Button) findViewById(R.id.go_info);

        final Switch sw = findViewById(R.id.switch2);
        final Switch sw1 = findViewById(R.id.switch1);
        //     final Switch sw3 = findViewById(R.id.switch3);
        name = findViewById(R.id.a12_name);
        number = findViewById(R.id.a12_number);
        button = findViewById(R.id.button2);
        updatebtn = findViewById(R.id.button3);
        nowAppVersion = findViewById(R.id.nowAppVersion);
        LatestAppVersion = findViewById(R.id.LatestVersion);
        //공인인증서 라이센스 확인용 선언
      /*  result_text=(TextView)findViewById(R.id.result_text);
        li_button = (Button)findViewById(R.id.button);*/

        //앱 라이센스 정보를 얻기위한 코드 추후 삭제or주석처리
     /*   li_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLicense();
                result_text.setText("");
            }
        });*/


        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a12_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu); //메뉴 버튼 이미지 지정

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        //네비게이션 헤더에 참조할때는 아래와같이 참조해야한다.

        TextView head_name = navigationView.getHeaderView(0).findViewById(R.id.head_name);
        TextView head_studentid = navigationView.getHeaderView(0).findViewById(R.id.head_StudentID);

        /**
         * 스토어버전 , 현재버전 가져오기 -> 네비게이션 헤더에 뿌리기
         */
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences5 = getSharedPreferences("UPDATE_VERSION", MODE_PRIVATE);
        String storeVersion = sharedPreferences5.getString("StoreVersion", "");
        String versionName = packageInfo.versionName; //현재 버전 저장
        TextView AppVersion = navigationView.getHeaderView(0).findViewById(R.id.AppVersion);
        TextView StroeVersion = navigationView.getHeaderView(0).findViewById(R.id.StroeVersion);
        AppVersion.setText(versionName);
        StroeVersion.setText(storeVersion);

        //최신 앱 버전은 배포후 스토어 링크를 통해 가져와야함 ( 업데이트시에 추가 )

        nowAppVersion.setText("현재 앱 버전 : " + versionName);
        LatestAppVersion.setText("최신 앱 버전 : " + storeVersion);


        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences("re", MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences("push", MODE_PRIVATE);

        String userName = sharedPreferences.getString("userName", ""); //유저이름저장
        String StudentID = sharedPreferences.getString("UID", ""); //유저학번저장
        id = StudentID;
        loginNumber = si.getLoginNumber();


        head_name.setText(userName + "님");
        head_studentid.setText(StudentID);

        number.setText(StudentID);
        name.setText(userName);

        sharedPreferences = getSharedPreferences("autologin", 0); //자동 로그인 설정 값 저장 (스위치 체크여부)
        sharedPreferences1 = getSharedPreferences("re", 0); //다시 보지 않기 설정 값 저장 (스위치 체크여부)
        sharedPreferences2 = getSharedPreferences("push", 0); //다시 보지 않기 설정 값 저장 (스위치 체크여부)

        //저장된 값 가져오기
        int a = sharedPreferences.getInt("login", 0);
        int a1 = sharedPreferences1.getInt("다시보지않기", 0);
        int a2 = sharedPreferences2.getInt("push", 0);


        /*ContentValues cValue = new ContentValues();
        cValue.put("userid", id);
        String url = "http://cb.egreen.co.kr/mobilee_proc/join/join_proc_m2.asp";
        NetworkConnect networkConnect = new NetworkConnect(url, cValue);
        networkConnect.execute();*/

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (versionName.equals(storeVersion)) {
                    //최신버전일때
                    AlertDialog.Builder builder = new AlertDialog.Builder(A12_Setting.this);

                    builder.setMessage("현재 버전은 최신버전입니다.");

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    builder.show();
                } else {
                    //최신버전이 아닐때
                    AlertDialog.Builder builder = new AlertDialog.Builder(A12_Setting.this);

                    builder.setMessage("*업데이트 알림*\n새로운 버전이 업데이트 되었습니다.");

                    builder.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goMarket(A12_Setting.this, "com.egreen2.egeen2");
                        }
                    });

                    builder.show();
                }
            }

        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(A12_Setting.this);

                builder.setMessage("앱 캐시 및 데이터를 삭제하시겠습니까?");

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        clearApplicationData();
                        Toast.makeText(A12_Setting.this, "완료되었습니다.", Toast.LENGTH_SHORT).show();
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
        });

       /* go_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkStateCheck netCheck = new NetworkStateCheck(A12_Setting.this);
                if (netCheck.isConnectionNet()){
                    SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
                    String name = sharedPreferences.getString("userName", "");
                    ContentValues cValue = new ContentValues();
                    cValue.put("userName", name);
                    String url = "http://cb.egreen.co.kr/mobile_proc/findUserInfo/find_userId_m2.asp";
                    NetworkConnect networkConnect = new NetworkConnect(url, cValue);
                    networkConnect.execute();
                }



               // Intent intent = new Intent(getApplicationContext(),A12_Info.class);
               // intent.putExtra("이름",userName);
               // startActivity(intent);
            }
        });*/

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
                } else if (id == R.id.support) {
                    moveActivity(A06_Support.class);
                }


                return true;


            }
        });


        /**
         * 스위치 on/off에 따른 자동 로그인
         * 스위치 on일때 1의값 저장 off일때 2의값 저장후 앱실행시 1이면 메인화면 2이면 로그인화면
         */
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("autologin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putInt("login", 1);
                    editor.apply();
                    sw.setChecked(true);
                } else {
                    editor.putInt("login", 2);
                    editor.apply();
                    sw.setChecked(false);
                }
            }
        });
        /**
         * 스위치 on/off에 따른 이동통신망 알림 설정
         * 스위치 on일때 2의값 저장 off일때 1의값 저장후 2이면 다시보기 1이면 다보지않기
         */
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("re", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putInt("다시보지않기", 2);
                    editor.apply();
                    sw1.setChecked(true);
                } else {
                    editor.putInt("다시보지않기", 1);
                    editor.apply();
                    sw1.setChecked(false);
                }
            }
        });

        /**
         * 스위치 on/off에 따른 자동 로그인
         * 스위치 on일때 1의값 저장 off일때 2의값 저장
         */
       /* sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("push", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putInt("push", 1);
                    editor.commit();
                    sw3.setChecked(true);
                } else {
                    editor.putInt("push", 2);
                    editor.commit();
                    sw3.setChecked(false);
                }
            }
        });*/


        if (a == 1) {
            sw.setChecked(true);
        } else if (a == 2) {
            sw.setChecked(false);
        }

        /*if (a2 == 1){
            sw3.setChecked(true);
        } else if (a2 == 2) {
            sw3.setChecked(false);
        }*/
        sw1.setChecked(a1 == 2);


    }   //onCreate 종료

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

    }          //로그인 페이지로 이동


    // 기존코드 : 왼쪽 상단 클릭시 메뉴 네비게이션 나오
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /* 2021.04.01 앱 캐시 지우기 코드 시작 */
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (s.equals("shared_prefs"))
                    continue; //shared_prefs 폴더의 내용은 지우지 않는다. 자동로그인 / 다지보지않기 / 학번저장 등의 정보 를 제외한 강의 캐시데이터를 지움
                else if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "***************** File /data/data/APP_PACKAGE/" + s + "DELETE *****************");
                }
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    /*
     * 라이선스 등록을 위해 꼭 실행하여야 함.
     */
    private void doLicense() {
        //라이선스 정보 추출
        Intent intent = new Intent();
        intent.setData(Uri.parse("crosscert://licenseinfo"));
        intent.putExtra("requestCode", 2);
        startActivityForResult(intent, 2);

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
            Log.i(TAG, "설정에서 전달하는 " + si.getUserId());
            Log.i(TAG, "설정에서 전달하는 " + si.getLoginNumber());
            Log.i(TAG, "설정에서 전달하는 " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "설정에서 전달하는 =====> 없음");
        }
//        finish();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
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
            // String id = "";
            // String name = "";
            // Button go_info = (Button) findViewById(R.id.go_info);

            // JSONObject jsObj = new JSONObject();

            // try {
            //     JSONArray jsArr = new JSONArray(s);

            //    for (int i = 0; i < jsArr.length(); i++) {
            //      jsObj = jsArr.getJSONObject(i);

            //     id = jsObj.getString("cId").trim();
            //   name = jsObj.getString("strName");
            //   }


            //   final String tName = name;
            // final String tId = id;


            // } catch (JSONException e) {
            //       Log.i(TAG, "JSONArray Exc: " + e.getMessage());
            //  }


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
