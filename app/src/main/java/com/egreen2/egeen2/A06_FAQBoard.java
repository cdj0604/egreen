package com.egreen2.egeen2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.egreen2.egeen2.Adapter.A06_FAQAdapter;
import com.egreen2.egeen2.Data.A06_FAQListData;
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
    [파일명] A06_FAQBoard.java.java
    [설명] FAQ 게시판
    [작성자] 장희원
    [작성일시]
 */

public class A06_FAQBoard extends AppCompatActivity implements NetworkAsyncTasker.AsyncResponse {
    private static final String TAG = A06_FAQBoard.class.getSimpleName();
    private final Context context = this;
    /* View 변수 선언 */
    String id;
    String userName;
    String loginNumber;
    StudyInfo si;
    Spinner a061_FAQSelect;
    ListView a061_FAQList;
    /* FAQ 카테고리를 담는 변수 */
    String selectFAQ = "";
    //AsyncTask 결과값을 받기 위한 변수
    NetworkAsyncTasker asyncTask;
    //네트워크 상태 체크
    NetworkStateCheck netCheck;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a06_faq_board);

        /* Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.a06_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //메뉴 버튼 이미지 지정

        /* View 연결 */
        ImageView imageView = findViewById(R.id.logo);
        a061_FAQList = findViewById(R.id.a06_faqList); //공지사항
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        a061_FAQSelect = findViewById(R.id.a06_FAQSelect);

        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");

        /* 네트워크 연결 상태 확인 */
        netCheck = new NetworkStateCheck(this);
        if (netCheck.isConnectionNet()) {
            init();
        } else {
            Toast.makeText(A06_FAQBoard.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
        }

        /* FAQ 카테고리 선택 Spinner 설정 */
        final String[] faqArr = getResources().getStringArray(R.array.faqArr);
        ArrayAdapter<String> a061_faqSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, faqArr);
        a061_FAQSelect.setAdapter(a061_faqSelectAdapter);
        a061_FAQSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectFAQ = faqArr[position];
                } else {
                    selectFAQ = "";
                }
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                /* 드로어 레이아웃 메뉴 */
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
                } else if (id == R.id.setting) {
                    moveActivity(A12_Setting.class);
                }
                return true;
            }
        });
        init();
    }   // onCreate 종료

    private void init() {

        /* FAQ */
        netConnForGetFAQ();
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
            Log.i(TAG, "FAQ에서 " + si.getUserId());
            Log.i(TAG, "FAQ에서 " + si.getLoginNumber());
            Log.i(TAG, "FAQ에서 " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "설정에서 전달하는 =====> 없음");
        }
//        finish();

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
     * FAQ를 받아오는 웹 서버의 통신
     */
    private void netConnForGetFAQ() {
        Log.i(TAG, "통신");

        String url = "http://cb.egreen.co.kr/mobile_proc/index_m2.asp";

        asyncTask = new NetworkAsyncTasker(this, url, null);
        asyncTask.execute();
    }

    /**
     * FAQ AsyncTask 결과
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

    private void parsing_json(String result) {
        JSONObject jsObj = new JSONObject();
        final ArrayList<A06_FAQListData> faqListData = new ArrayList<A06_FAQListData>();

        try {
            JSONArray jsArray = new JSONArray(result);

            for (int i = 0; i < jsArray.length(); i++) {
                jsObj = jsArray.getJSONObject(i);

                String faqTitle = jsObj.getString("strTitle");
                String faqCid = jsObj.getString("cId");


                /* ListView에 data를 넣는 작업 */
                A06_FAQListData data = new A06_FAQListData(faqCid, faqTitle);
                faqListData.add(data);
            }

            /* ListView에 data를 넣기 위한 Adapter 설정 */
            A06_FAQAdapter a06_faqAdapter = new A06_FAQAdapter(getApplicationContext(), R.layout.a06_cs_faq_list, faqListData);
            a06_faqAdapter.notifyDataSetChanged();
            a061_FAQList.setAdapter(a06_faqAdapter);
            a061_FAQList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (netCheck.isConnectionNet()) {
                        Intent intent = new Intent(A06_FAQBoard.this, A06_FAQ.class);
                        intent.putExtra("FAQ", faqListData.get(position).getFaqCid());
                        startActivity(intent);
                    } else {
                        Toast.makeText(A06_FAQBoard.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            Log.i(TAG, "notice JSON Exc: " + e.getMessage());
        }
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

    }             //로그인 페이지로 이동

/*
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
*/

    /**
     * 기존 코드 주석후 왼쪽 상단 클릭시 뒤로가기
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //왼쪽 상단 버튼 눌렀을 때
                finish();
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
}
