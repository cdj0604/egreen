package com.egreen2.egeen2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class A12_Info extends AppCompatActivity {

    TextView a12_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a12_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.a07_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //메뉴 버튼 이미지 지정

        //view
        a12_name = findViewById(R.id.a12_name);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        a12_name.setText(name);

    }

    /**
     * 왼쪽 상단 버튼 눌렀을 때
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //뒤로가기 버튼을 클릭 시 드로어 레이아웃 닫기
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
