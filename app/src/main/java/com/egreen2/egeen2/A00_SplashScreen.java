package com.egreen2.egeen2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/*
    [파일명] A00_SplashScreen.java
    [설명] 앱 실행 화면
    [작성자] 장희원
    [작성일시] 2020.12.09
*/

public class A00_SplashScreen extends AppCompatActivity {


    Thread splashThread;
    Animation anim;
    ImageView imgView;
    String versionName;
    String version;//


    public static void goMarket(Activity caller, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a00_splash_screen);
        getAppVersionName();

        final Bundle bundle = new Bundle();

        new Thread() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://cb.egreen.co.kr/Contents/test/list.html").get();
                    Elements contents = doc.select("#tab01 > table > tbody > tr:nth-child(1) > td:nth-child(1)");
                    version = contents.text();

                    bundle.putString("version", version);
                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //공인인증서 상태 변경
        SharedPreferences savedCertyState = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedCertyState.edit();
        editor.putBoolean("certyState", false);
        editor.commit();


        SharedPreferences login = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = login.edit();
        editor1.putInt("aaa", 2);
        editor1.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("autologin", MODE_PRIVATE);
        int a = sharedPreferences.getInt("login", 0);

        Log.d("switch", String.valueOf(a));

        imgView =

                findViewById(R.id.a00_egreen_splash);

        anim = AnimationUtils.loadAnimation(

                getApplicationContext(), R.anim.alpha);

        //업데이트시 스토어버전 수정
        //스토어에 올릴 버전 직접 수정 (gradle 버전과 맞추기)
        // storeVersion = "2.0.0";

        //강제업데이트를 위해서 로딩화면에선 스토어버전을 배포된 마켓링크로 가져와야함, 그 외 단순 텍스트로 보여지는것은 string값 변경
        String storeVersion = getString(R.string.store);
        Log.d("storeVersion", storeVersion);


        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatus.TYPE_MOBILE) {
            /**
             * a가 1일때 = 자동로그인 스위치on상태
             * a가 1이 아닐때 = 자동로그인 스위치off상태 or 앱 설치후 첫 로그인시
             */

            //자동 로그인 체크상태로, 로그인 완료 된 메인화면으로 간다
            if (a == 1) {
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(A00_SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

            //자동 로그인 체크아닌상태로, 로그인 전 화면으로 간다
            else {
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(A00_SplashScreen.this, before_Main.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }
        } else if (status == NetworkStatus.TYPE_WIFI) {
            /**
             * a가 1일때 = 자동로그인 스위치on상태
             * a가 1이 아닐때 = 자동로그인 스위치off상태 or 앱 설치후 첫 로그인시
             */

            //자동 로그인 체크상태로, 바로 메인화면으로 간다
            if (a == 1) {
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(A00_SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

            //자동 로그인 체크아닌상태로, 로그인 전 화면으로 간다
            else {
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(A00_SplashScreen.this, before_Main.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }
        } else {

            android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(getApplicationContext());
            ab.setCancelable(false);
            ab.setMessage("네트워크 연결상태를 확인해주세요.");
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
        imgView.startAnimation(anim);
    }

    ///oncreate


    //앱버전 명
    public void getAppVersionName() {
        PackageInfo packageInfo = null;         //패키지에 대한 전반적인 정보

        //PackageInfo 초기화
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }

        versionName = packageInfo.versionName;
        Log.d("versionName ====> ", versionName);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            version = bundle.getString("version");
            Log.d("서버등록버전", bundle.getString("version"));

            SharedPreferences savedCertyState = getSharedPreferences("UPDATE_VERSION", MODE_PRIVATE);
            SharedPreferences.Editor editor = savedCertyState.edit();
            editor.putString("StoreVersion",version);
            editor.commit();

        }

    };
}
