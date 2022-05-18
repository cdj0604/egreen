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
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
    String storeVersion = "", appVersion = "";
    String versionName;

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

        imgView = findViewById(R.id.a00_egreen_splash);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);

        //업데이트시 스토어버전 수정
        storeVersion = "2.0.0";

        if (!versionName.equals(storeVersion)) {
            android.app.AlertDialog.Builder ab1 = new android.app.AlertDialog.Builder(this);
            ab1.setMessage("*업데이트 알림*\n새로운 버전이 업데이트 되었습니다.\n업데이트 하시겠어요?");
            ab1.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goMarket(A00_SplashScreen.this, "com.egreen2.egeen2");
                }
            });
            ab1.setNegativeButton("그냥 쓸래요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                    if (status == NetworkStatus.TYPE_MOBILE) {
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
            });
            ab1.show();
        } else {
            int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
            if (status == NetworkStatus.TYPE_MOBILE) {
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
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
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

    }///oncreate

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

    }

}
