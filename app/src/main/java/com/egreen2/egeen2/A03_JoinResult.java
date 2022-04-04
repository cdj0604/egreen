package com.egreen2.egeen2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

/* 회원가입 결과 페이지 */
/*
    [파일명] A03_JoinResult.java
    [설명] 회원가입 결과
    [작성자] 장희원
    [작성일시] 2020.12.09
*/
public class A03_JoinResult extends AppCompatActivity {
    private static final String TAG = A03_Join.class.getSimpleName();
    private final Context context = this;
    TextView a03_strName;
    TextView a03_strName2;
    TextView a03_strName3;
    TextView a03_strNumber;
    TextView a03_strNumber2;
    TextView a03_brith;
    TextView a03_phone;
    TextView a03_email;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a03_join_result);

        //회원가입시 입력한 데이터 받기
        Intent intent = getIntent();
        String strNum = intent.getExtras().getString("학번");
        String name = intent.getExtras().getString("이름");
        String birth = intent.getExtras().getString("생년월일");
        String phone = intent.getExtras().getString("휴대폰번호");
        String email = intent.getExtras().getString("이메일");

        Log.d("인텐트 값 ===> ", "학번 : " + strNum + " 이름 : " + name + " 생년월일 : " + birth + " 휴대폰 : " + phone + " 이메일 : " + email);
        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a03_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기

        a03_strName = findViewById(R.id.a03_strName);
        a03_strName2 = findViewById(R.id.a03_strName2);
        a03_strName3 = findViewById(R.id.a03_strName3);
        a03_strNumber = findViewById(R.id.a03_strNumber);
        a03_strNumber2 = findViewById(R.id.a03_strNumber2);
        a03_brith = findViewById(R.id.a03_birth);
        a03_phone = findViewById(R.id.a03_phone);
        a03_email = findViewById(R.id.a03_email);

        a03_strName.setText(name);
        a03_strName2.setText(name);
        a03_strName3.setText(name);
        a03_strNumber.setText(strNum);
        a03_strNumber2.setText(strNum);
        a03_brith.setText(birth);
        a03_phone.setText(phone);
        a03_email.setText(email);

        ImageView imageView = findViewById(R.id.logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), before_Main.class);
                startActivity(intent);
            }
        });

    }   // onCreate 종료


    /* 버튼 클릭 */
    public void go_A02_Login(View view) {
        Intent intent = new Intent(getApplicationContext(), A02_Login.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), before_Main.class);
        startActivity(intent);
    }

}
