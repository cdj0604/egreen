package com.egreen2.egeen2;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

/*
    [파일명] A07_LearningG.java
    [설명] 수강하기 가이드
            개발 완료 후 사진 경로 수정
    [작성자] 장희원
    [작성일시]
 */

public class A07_LearningG extends AppCompatActivity {
    // 배포 전 url 변경 필요!!!
    private final String[] images = new String[]{
            "http://cb.egreen.co.kr/mobile_proc/LearningG/main.png",
            "http://cb.egreen.co.kr/mobile_proc/LearningG/main2.png",
            "http://cb.egreen.co.kr/mobile_proc/LearningG/classroom.png",
            "http://cb.egreen.co.kr/mobile_proc/LearningG/classroom2.png",
            "http://cb.egreen.co.kr/mobile_proc/LearningG/classroom3.png",

    };
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a07_learning_g);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);

        sliderViewPager.setOffscreenPageLimit(1);   //n개 페이지 미리 로딩
        sliderViewPager.setAdapter(new A07_LearningGSub(this, images));

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