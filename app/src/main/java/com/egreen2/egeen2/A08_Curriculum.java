package com.egreen2.egeen2;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.egreen2.egeen2.Data.StudyInfo;
import com.google.android.material.navigation.NavigationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

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
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            textView.setText(bundle.getString("list1"));                      //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
            textView2.setText(bundle.getString("list"));                      //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
            textView3.setText(bundle.getString("list2"));                      //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
        }
    };
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a08_curriculum);
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        final Bundle bundle = new Bundle();

        new Thread() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    //과목명
                    doc = Jsoup.connect("https://cb.egreen.co.kr/Contents/test/list.html").get();
                    Elements contents = doc.select("#tab01 > table > tbody > tr:nth-child(1) > td.t_l_i.f_b > a");
                    list1 = contents.text() + "\n";

                    for (int i = 2; i < 69; i++) {
                        contents = doc.select("#tab01 > table > tbody > tr:nth-child(" + i + ") > td.t_l_i.f_b > a");
                        list1 += contents.text() + "\n";
                    }

                    //이수구분
                    Elements contents1 = doc.select("#tab01 > table > tbody > tr:nth-child(1) > td:nth-child(1)");
                    list = contents1.text() + "\n";
                    for (int i = 2; i < 69; i++) {
                        contents = doc.select("#tab01 > table > tbody > tr:nth-child( " + i + " ) > td:nth-child(1)");
                        list += contents.text() + "\n";
                    }

                    //교수명
                    Elements contents2 = doc.select("#tab01 > table > tbody > tr:nth-child(1) > td:nth-child(6)");
                    list2 = contents2.text() + "\n";
                    for (int i = 2; i < 69; i++) {
                        contents = doc.select("#tab01 > table > tbody > tr:nth-child( " + i + " ) > td:nth-child(6)");
                        list2 += contents.text() + "\n";
                    }

                    bundle.putString("list1", list1);                               //핸들러를 이용해서 Thread()에서 가져온 데이터를 메인 쓰레드에 보내준다.
                    bundle.putString("list2", list2);
                    bundle.putString("list", list);
                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

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
