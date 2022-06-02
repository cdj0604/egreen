package com.egreen2.egeen2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class A06_FAQ extends AppCompatActivity {

    TextView a1, a2, a3, a4, a5, a6, a7, a8, a9, a10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a06_faq);




        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a06_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //메뉴 버튼 이미지 지정
        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);
        a5 = findViewById(R.id.a5);
        a6 = findViewById(R.id.a6);
        a7 = findViewById(R.id.a7);
        a8 = findViewById(R.id.a8);
        a9 = findViewById(R.id.a9);
        a10 = findViewById(R.id.a10);


    }

    //문의 클릭시 문의답변 보여주기
    public void q1(View view) {
        if (a1.getVisibility() == View.GONE) {
            a1.setVisibility(View.VISIBLE);
        } else {
            a1.setVisibility(View.GONE);
        }

    }

    public void q2(View view) {
        if (a2.getVisibility() == View.GONE) {
            a2.setVisibility(View.VISIBLE);
        } else {
            a2.setVisibility(View.GONE);
        }

    }

    public void q3(View view) {
        if (a3.getVisibility() == View.GONE) {
            a3.setVisibility(View.VISIBLE);
        } else {
            a3.setVisibility(View.GONE);
        }

    }

    public void q4(View view) {
        if (a4.getVisibility() == View.GONE) {
            a4.setVisibility(View.VISIBLE);
        } else {
            a4.setVisibility(View.GONE);
        }

    }

    public void q5(View view) {
        if (a5.getVisibility() == View.GONE) {
            a5.setVisibility(View.VISIBLE);
        } else {
            a5.setVisibility(View.GONE);
        }

    }

    public void q6(View view) {
        if (a6.getVisibility() == View.GONE) {
            a6.setVisibility(View.VISIBLE);
        } else {
            a6.setVisibility(View.GONE);
        }

    }

    public void q7(View view) {
        if (a7.getVisibility() == View.GONE) {
            a7.setVisibility(View.VISIBLE);
        } else {
            a7.setVisibility(View.GONE);
        }

    }

    public void q8(View view) {
        if (a8.getVisibility() == View.GONE) {
            a8.setVisibility(View.VISIBLE);
        } else {
            a8.setVisibility(View.GONE);
        }

    }

    public void q9(View view) {
        if (a9.getVisibility() == View.GONE) {
            a9.setVisibility(View.VISIBLE);
        } else {
            a9.setVisibility(View.GONE);
        }

    }

    public void q10(View view) {
        if (a10.getVisibility() == View.GONE) {
            a10.setVisibility(View.VISIBLE);
        } else {
            a10.setVisibility(View.GONE);
        }

    }

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}



