package com.egreen2.egeen2;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

/*
    [파일명] A07_ApplyG.java
    [설명] 수강신청 가이드
            개발 완료 후 사진 경로 수정
    [작성자] 장희원
    [작성일시]
 */

public class A07_ApplyG extends AppCompatActivity {
    // 배포 전 url 변경 필요!!!
    private final String[] images = new String[]{
            "https://cb.egreen.co.kr/career_upload_files/editor_images/2022_2_hakjum_1.jpg",
            "http://cb.egreen.co.kr/mobile_proc/ApplyG/main2.png",
            "http://cb.egreen.co.kr/mobile_proc/ApplyG/apply.png",
            "http://cb.egreen.co.kr/mobile_proc/ApplyG/apply2.png",
            "http://cb.egreen.co.kr/mobile_proc/ApplyG/apply3.png",

    };
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a07_apply_g);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);

        sliderViewPager.setOffscreenPageLimit(1);   //n개 페이지 미리 로딩
        sliderViewPager.setAdapter(new A07_ApplyGSub(this, images));

        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        setupIndicators(images.length);
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }
}