package com.egreen2.egeen2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A06_QuickAdvice.java
    [설명] 빠른 학습상담
    [작성자] 장희원
    [작성일시]
 */

public class A06_QuickAdvice extends AppCompatActivity implements View.OnClickListener {
    /* View 변수 선언 */
    EditText a06_QAInputName, a06_QAInputPhone2, a06_QAInputPhone3, a06_QAInputAdvice;    // 이름, 핸드폰 번호, 문의사항 입력 필드
    Spinner a06_QAInputPhone1, a06_QAClassSelect, a06_QATimeSelect;    // 핸드폰 번호 앞자리, 희망 전공, 희망 시간 선택
    Button a06_QABt;   // 문의하기 버튼
    /* 선택한 희망전공과 희망시간을 담는 변수 */
    String selectClass = "", selectTime = "", selectFirstPhoneNum = "";
    /* '-'이 포함된 휴대폰 번호를 저장 */
    String phoneNumberWithHyphen;
    private DrawerLayout mDrawerLayout;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a06_quick_advice);

        /* Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.a06_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //뒤로가기 버튼 이미지 지정

        ImageView imageView = findViewById(R.id.logo);

        /* EditText View 연결 */
        a06_QAInputName = findViewById(R.id.a06_QAInputName);
        a06_QAInputPhone2 = findViewById(R.id.a06_QAInputPhone2);
        a06_QAInputPhone3 = findViewById(R.id.a06_QAInputPhone3);
        a06_QAInputAdvice = findViewById(R.id.a06_QAInputAdvice);
        /* Spinner View 연결 */
        a06_QAInputPhone1 = findViewById(R.id.a06_QAInputPhone1);
        a06_QAClassSelect = findViewById(R.id.a06_QAClassSelect);
        a06_QATimeSelect = findViewById(R.id.a06_QATimeSelect);
        /* Button View 연결 */
        a06_QABt = findViewById(R.id.a06_QABt);

        /* View.setOnClickListener */
        a06_QABt.setOnClickListener(this);

        /**
         * 로그인 전 화면에서 넘어왔을시 홈화면 로고 클릭시 로그인 전 화면으로 이동
         */
        Intent intent1 = getIntent();
        int loginstate = intent1.getIntExtra("loginstate", 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginstate == 1) {
                    Intent sendintent = new Intent(getApplicationContext(), before_Main.class);
                    startActivity(sendintent);
                } else {
                    Intent sendintent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(sendintent);
                }

            }
        });



        /* 희망과정 선택 Spinner 설정 */
        final String[] classArr = getResources().getStringArray(R.array.classArr);
        ArrayAdapter<String> a064_QAClassSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, classArr);
        a06_QAClassSelect.setAdapter(a064_QAClassSelectAdapter);
        a06_QAClassSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectClass = classArr[position];
                } else {
                    selectClass = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* 희망상담시간 선택 Spinner 설정 */
        final String[] timeArr = getResources().getStringArray(R.array.timeArr);
        ArrayAdapter<String> a064_QATimeSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timeArr);
        a06_QATimeSelect.setAdapter(a064_QATimeSelectAdapter);
        a06_QATimeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectTime = timeArr[position];
                } else {
                    selectTime = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* 휴대폰 번호 앞자리 선택 Spinner 설정 */
        final String[] phoneArr = getResources().getStringArray(R.array.phoneArr);
        ArrayAdapter<String> a064_QAPhoneSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, phoneArr);
        a06_QAInputPhone1.setAdapter(a064_QAPhoneSelectAdapter);
        a06_QAInputPhone1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectFirstPhoneNum = phoneArr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            a06_QAInputName.setText(intent.getStringExtra("USER_NAME"));
        }
    }//onCreate 종료

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a06_QABt:
                NetworkStateCheck netCheck = new NetworkStateCheck(this);   // 네트워크 연결 상태 확인
                if (netCheck.isConnectionNet()) {
                    if (inputCheck()) {
                        netConnForQuickAdvice();
                    }
                }
                break;
        }

    }

    /**
     * 빠른상담 남기기 메소드
     */
    private void netConnForQuickAdvice() {
        String phone2 = a06_QAInputPhone2.getText().toString().trim();
        String phone3 = a06_QAInputPhone3.getText().toString().trim();

        phoneNumberWithHyphen = selectFirstPhoneNum + "-" + phone2 + "-" + phone3;

        String name = a06_QAInputName.getText().toString().trim();
        String phoneNumber = phoneNumberWithHyphen;
        String wantClass = selectClass;
        String wantTime = selectTime;
        String question = a06_QAInputAdvice.getText().toString();

        ContentValues cValue = new ContentValues();
        cValue.put("uname", name);
        cValue.put("uHakwi", wantClass);
        cValue.put("uphone", phoneNumber);
        cValue.put("utime", wantTime);
        cValue.put("uInquiry", question);

        String url = "http://cb.egreen.co.kr/mobile_proc/quickCounsel_m2.asp";

        NetworkConnect networkConnect = new NetworkConnect(url, cValue);
        networkConnect.execute();
    }

    /**
     * 비어있는 필드가 있는지 체크
     */
    private boolean inputCheck() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);

        String name = a06_QAInputName.getText().toString().trim();
        String phone2 = a06_QAInputPhone2.getText().toString().trim();
        String phone3 = a06_QAInputPhone3.getText().toString().trim();
        String phone = "";
        String advice = a06_QAInputAdvice.getText().toString().trim();

        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); // 키보드 올리기

        if (name.equals("")) {  /* 이름 필드 체크 */
            ab.setMessage("이름을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a06_QAInputName.requestFocus();
                    imm.showSoftInput(a06_QAInputName, 0);
                }
            });
            ab.show();
        } else if (phone2.equals("")) {  /* 휴대폰 번호 가운데 자리 필드 체크 */
            ab.setMessage("전화번호를 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a06_QAInputPhone2.requestFocus();
                    imm.showSoftInput(a06_QAInputPhone2, 0);
                }
            });
            ab.show();
        } else if (phone3.equals("")) {    /* 휴대폰 번호 마지막 자리 필드 체크 */
            ab.setMessage("전화번호를 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a06_QAInputPhone3.requestFocus();
                    imm.showSoftInput(a06_QAInputPhone3, 0);
                }
            })
                    .show();
        } else if (selectClass.equals("")) {
            ab.setMessage("상담희망전공을 선택해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        } else if (selectTime.equals("")) {
            ab.setMessage("상담희망시간을 선택해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        } else if (advice.equals("")) {
            ab.setMessage("입력된 내용이 없으면 기본 메세지로 전송됩니다.\n\n[기본메세지]\n\"상담 부탁드립니다. 감사합니다.\"\n\n문의 하시겠습니까?");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    netConnForQuickAdvice();
                    a06_QAInputAdvice.setText("상담 부탁드립니다.\n감사합니다.");
                }
            });
            ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        } else {
            return true;
        }
        return false;
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

    }            //로그인 페이지로 이동

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {   // 왼쪽 상단 버튼 눌렀을 때
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

    //키보드 내리기 터치 이벤트
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
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

            String[] result = s.split("\\[");
            android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(A06_QuickAdvice.this);

            if (result[0].equals("success")) {
                ab.setMessage("등록되었습니다.\n상담희망시간에 연락드리겠습니다.\n감사합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(A06_QuickAdvice.this, A06_Support.class));
                                finish();
                            }
                        }).show();
            } else {
                ab.setMessage("오류가 발생하여 등록에 실패했습니다.\n잠시 후 다시 시도해주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  startActivity(new Intent(A06_QuickAdvice.this, A06_Support.class));
                                // finish();
                            }
                        }).show();
            }
        }
    }
}

