package com.egreen2.egeen2;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/*
    [파일명] A03_PersonalInfomation.java
    [설명] 개인정보 수집 및 이용동의
    [작성자] 장희원
    [작성일시] 2021.01.11
 */

public class A03_PersonalInformation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a03_personal_information);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a03_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(false); // 메뉴 버튼 만들기
    }

    /* 인텐트 */
    private void Close_Intent() {
        finish();
    }

    /* 버튼 클릭*/
    public void close(View view) {
        Close_Intent();
    }

}
