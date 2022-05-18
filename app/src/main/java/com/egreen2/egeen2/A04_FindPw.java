package com.egreen2.egeen2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/*
    [파일명] A04_FindPw.java
    [설명] 비밀번호 찾기
    [작성자] 장희원
    [작성일시] 2021.03.30
 */

public class A04_FindPw extends AppCompatActivity {
    private static final String TAG = A04_FindPw.class.getSimpleName();

    /* View 변수 선언 */
    public static EditText a04_findPwInputName, a04_findPwInputNumber, a04_findPwYear, a04_findPwMonth, a04_findPwDay, a04_findPwInputEmail1, a04_findPwInputEmail2; //이름, 학번, 생년월일, 이메일
    Spinner a04_findPwYearSpinner, a04_findPwMonthSpinner, a04_findPwDaySpinner, a04_findPwEmailSpinner; //Spinner
    Button a04_findPw_btn; //비밀번호 찾기 버튼
    InputMethodManager imm; //키보드 컨트롤

    //#################################### onCreate 시작 ####################################//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a04_find_pw);

        Toolbar toolbar = findViewById(R.id.a04_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //메뉴 버튼 이미지 지정

        /* View 연결 */
        a04_findPwInputName = findViewById(R.id.a04_findPwInputName); //이름 입력
        a04_findPwInputNumber = findViewById(R.id.a04_findPwInputNumber); //학번 입력
        a04_findPwYear = findViewById(R.id.a04_findPwYear); //생년월일(년도) 입력
        a04_findPwMonth = findViewById(R.id.a04_findPwMonth); //생년월일(월) 입력
        a04_findPwDay = findViewById(R.id.a04_findPwDay); //생년월일(일) 입력
        a04_findPwInputEmail1 = findViewById(R.id.a04_findPwInputEmail1); //이메일 입력
        a04_findPwInputEmail2 = findViewById(R.id.a04_findPwInputEmail2); //도메인 입력

        a04_findPwYearSpinner = findViewById(R.id.a04_findPwYearSpinner);
        a04_findPwMonthSpinner = findViewById(R.id.a04_findPwMonthSpinner);
        a04_findPwDaySpinner = findViewById(R.id.a04_findPwDaySpinner);
        a04_findPwEmailSpinner = findViewById(R.id.a04_findPwEmailSpinner);

        a04_findPw_btn = findViewById(R.id.a04_findPw_btn);

        /* 키보드 올리기/내리기 설정 */
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        /* EditText 클릭 시 키보드, 커서 안보이게 하기 */
        a04_findPwYear.setInputType(InputType.TYPE_NULL);
        a04_findPwMonth.setInputType(InputType.TYPE_NULL);
        a04_findPwDay.setInputType(InputType.TYPE_NULL);

        //로고클릭
        ImageView imageView = findViewById(R.id.logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), before_Main.class);
                startActivity(intent);
            }
        });
        /* Year Spinner */
        final String[] yearArr = getResources().getStringArray(R.array.yearArr);
        ArrayAdapter<String> y_FindSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, yearArr);
        a04_findPwYearSpinner.setAdapter(y_FindSelectAdapter);
        a04_findPwYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a04_findPwYear.setText(yearArr[position]);
                } else {
                    a04_findPwYear.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* Month Spinner */
        final String[] monthArr = getResources().getStringArray(R.array.monthArr);
        ArrayAdapter<String> m_FindSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthArr);
        a04_findPwMonthSpinner.setAdapter(m_FindSelectAdapter);
        a04_findPwMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a04_findPwMonth.setText(monthArr[position]);
                } else {
                    a04_findPwMonth.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* Day Spinner */
        final String[] dayArr = getResources().getStringArray(R.array.dayArr);
        ArrayAdapter<String> d_FindSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dayArr);
        a04_findPwDaySpinner.setAdapter(d_FindSelectAdapter);
        a04_findPwDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a04_findPwDay.setText(dayArr[position]);
                } else {
                    a04_findPwDay.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* email Spinner */
        final String[] emailArr = getResources().getStringArray(R.array.emailArr);
        ArrayAdapter<String> e_FindSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, emailArr);
        a04_findPwEmailSpinner.setAdapter(e_FindSelectAdapter);
        a04_findPwEmailSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a04_findPwInputEmail2.setText(emailArr[position]);
                } else {
                    a04_findPwInputEmail2.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    //#################################### onCreate 종료 ####################################//

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

    /**
     * 비밀번호 찾기 메소드
     */
    private void netConnForFindPw() {
        String email1 = a04_findPwInputEmail1.getText().toString().trim();
        String email2 = a04_findPwInputEmail2.getText().toString().trim();
        String emailDomain = email1 + "@" + email2; //이메일 완성

        if (checkEmail(emailDomain)) {
            String id = a04_findPwInputNumber.getText().toString().trim();
            String name = a04_findPwInputName.getText().toString().trim();
            String year = a04_findPwYear.getText().toString().trim();
            year = year.substring(0, 4);
            String month = a04_findPwMonth.getText().toString().trim();
            month = month.substring(0, 2);
            String day = a04_findPwDay.getText().toString().trim();
            day = day.substring(0, 2);
            String birth = year + "-" + month + "-" + day;
            String email = emailDomain;

            ContentValues cValue = new ContentValues();
            cValue.put("userName", name);
            cValue.put("userId", id);
            cValue.put("userBirth", birth);
            cValue.put("userEmail", email);

            String url = "http://cb.egreen.co.kr/mobile_proc/findUserInfo/find_userPw_m2.asp";

            NetworkConnect networkConnect = new NetworkConnect(url, cValue);
            networkConnect.execute();
        }
    }

    /* 비밀번호 찾기 입력 필드 체크 */
    private boolean isInputCheck() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);

        String name = a04_findPwInputName.getText().toString().trim(); //이름
        String strNum = a04_findPwInputNumber.getText().toString().trim(); //학번
        String year = a04_findPwYear.getText().toString().trim(); //년도
        String month = a04_findPwMonth.getText().toString().trim(); //월
        String day = a04_findPwDay.getText().toString().trim(); //일
        String email1 = a04_findPwInputEmail1.getText().toString().trim(); //이메일
        String email2 = a04_findPwInputEmail2.getText().toString().trim(); //도메인

        if (name.equals("")) { //이름 필드 체크
            ab.setMessage("이름을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findPwInputName.requestFocus();
                    imm.showSoftInput(a04_findPwInputName, 0);
                }
            });
            ab.show();
        } else if (strNum.equals("")) { //학번 필드 체크
            ab.setMessage("학번을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findPwInputNumber.requestFocus();
                    imm.showSoftInput(a04_findPwInputNumber, 0);
                }
            });
            ab.show();
        } else if (year.equals("")) { //년도 체크
            ab.setMessage("생년월일을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findPwYear.requestFocus();
                    imm.showSoftInput(a04_findPwYear, 0);
                }
            });
            ab.show();
        } else if (month.equals("")) { //월 체크
            ab.setMessage("생년월일을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findPwMonth.requestFocus();
                    imm.showSoftInput(a04_findPwMonth, 0);
                }
            });
            ab.show();
        } else if (day.equals("")) { //일 체크
            ab.setMessage("생년월일을 입력해주세요");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findPwDay.requestFocus();
                    imm.showSoftInput(a04_findPwDay, 0);
                }
            });
            ab.show();
        } else if (email1.equals("")) { //이메일 체크
            ab.setMessage("이메일을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findPwInputEmail1.requestFocus();
                    imm.showSoftInput(a04_findPwInputEmail1, 0);
                }
            }).show();
        } else if (email2.equals("")) { //도메인 체크
            ab.setMessage("도메인을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findPwInputEmail2.requestFocus();
                    imm.showSoftInput(a04_findPwInputEmail2, 0);
                }
            }).show();
        } else {
            return true;
        }
        return false;
    }

    /* 이메일 유효성 체크 */
    private boolean checkEmail(String email) {
        boolean emailRegular = Pattern.matches("^([\\w\\.\\~\\-]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", email.trim());
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);

        if (emailRegular == false) {
            ab.setMessage("사용할 수 없는 이메일 입니다.\n이메일을 다시 확인해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findPwInputEmail1.requestFocus();
                }
            }).show();
            return false;
        }
        return true;
    }

    /* 비밀번호 찾기 버튼을 눌렀을 때 */
    public void go_Find(View view) {
        NetworkStateCheck netCheck = new NetworkStateCheck(this); //네트워크 연결 상태 확인
        if (netCheck.isConnectionNet()) {
            if (isInputCheck()) { //각 입력 필드 확인
                netConnForFindPw();
            }
        }
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
            super.onPreExecute();

            AlertDialog.Builder ab = new AlertDialog.Builder(A04_FindPw.this);
            ab.setCancelable(false);

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customLayout = inflater.inflate(R.layout.a04_cs_find_pw_result_alert, null);

            TextView a04_name = customLayout.findViewById(R.id.a04_pw_name);
            TextView a04_init = customLayout.findViewById(R.id.a04_pw_init);
            Button a04_login = customLayout.findViewById(R.id.a04_pw_login);

            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String initData = format.format(date);
            a04_init.setText("생년월일(8자리, 예:" + initData + ")");

            String[] result = s.trim().split("\\[");
            if (result[0].equals("Success")) {
                a04_name.setText(a04_findPwInputName.getText().toString() + " 학습자님의 비밀번호가");

                a04_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(A04_FindPw.this, A02_Login.class);
                        intent.putExtra("ID", a04_findPwInputNumber.getText().toString());

                        startActivity(intent);
                        finish();
                    }
                });
                ab.setView(customLayout);
                ab.show();
            } else {
                ab.setMessage("등록된 회원이 아닙니다.")
                        .setPositiveButton("확인", null)
                        .show();
            }
        }
    }
}






























