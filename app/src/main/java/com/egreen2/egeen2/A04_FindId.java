package com.egreen2.egeen2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/*
    [파일명] A04_FindId.java
    [설명] 학번 찾기
    [작성자] 장희원
    [작성일시] 2021.03.26
 */

public class A04_FindId extends AppCompatActivity {

    private static final String TAG = A04_FindId.class.getSimpleName();

    /* View 변수 선언 */
    public EditText a04_findIdInputName, a04_findIdYear, a04_findIdMonth, a04_findIdDay, a04_findIdInputEmail1, a04_findIdInputEmail2; //이름, 생년월일, 이메일
    Spinner a04_findIdYearSpinner, a04_findIdMonthSpinner, a04_findIdDaySpinner, a04_findIdEmailSpinner; //Spinner
    Button a04_findId_btn; //학번 찾기 버튼
    InputMethodManager imm; //키보드 컨트롤
    String birthData;

    //#################################### onCreate 시작 ####################################//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a04_find_id);
        /* Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.a04_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //메뉴 버튼 이미지 지정
        /* View 연결 */
        a04_findIdInputName = findViewById(R.id.a04_findIdInputName); //이름 입력
        a04_findIdYear = findViewById(R.id.a04_findIdYear); //생년월일(년도) 입력
        a04_findIdMonth = findViewById(R.id.a04_findIdMonth); //생년월일(월) 입력
        a04_findIdDay = findViewById(R.id.a04_findIdDay); //생년월일(일) 입력
        a04_findIdInputEmail1 = findViewById(R.id.a04_findIdInputEmail1); //이메일 입력
        a04_findIdInputEmail2 = findViewById(R.id.a04_findIdInputEmail2); //도메인 입력

        a04_findIdYearSpinner = findViewById(R.id.a04_findIdYearSpinner); //생년월일 (년도) 선택
        a04_findIdMonthSpinner = findViewById(R.id.a04_findIdMonthSpinner); //생년월일 (월) 선택
        a04_findIdDaySpinner = findViewById(R.id.a04_findIdDaySpinner); //생년월일 (일) 선택
        a04_findIdEmailSpinner = findViewById(R.id.a04_findIdEmailSpinner); //도메인 선택

        a04_findId_btn = findViewById(R.id.a04_findId_btn); //학번 찾기 버튼

        /* 키보드 올리기/내리기 설정 */
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        /* EditText 클릭 시 키보드, 커서 안보이게 하기 */
        a04_findIdYear.setInputType(InputType.TYPE_NULL);
        a04_findIdMonth.setInputType(InputType.TYPE_NULL);
        a04_findIdDay.setInputType(InputType.TYPE_NULL);
        //로고클릭
        ImageView imageView = findViewById(R.id.logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), before_Main.class);
                startActivity(intent);
            }
        });

        /* 년도를 선택하는 Spinner 호출 */
        final String[] yearArr = getResources().getStringArray(R.array.yearArr);
        final ArrayAdapter<String> y_FindSelectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, yearArr);
        a04_findIdYearSpinner.setAdapter(y_FindSelectAdapter);
        a04_findIdYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a04_findIdYear.setText(yearArr[position]);
                } else {
                    a04_findIdYear.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* 월을 선택하는 Spinner 호출 */
        final String[] monthArr = getResources().getStringArray(R.array.monthArr);
        final ArrayAdapter<String> m_FindSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthArr);
        a04_findIdMonthSpinner.setAdapter(m_FindSelectAdapter);
        a04_findIdMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a04_findIdMonth.setText(monthArr[position]);
                } else {
                    a04_findIdMonth.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* 일을 선택하는 Spinner 호출 */
        final String[] dayArr = getResources().getStringArray(R.array.dayArr);
        final ArrayAdapter<String> d_FindSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dayArr);
        a04_findIdDaySpinner.setAdapter(d_FindSelectAdapter);
        a04_findIdDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a04_findIdDay.setText(dayArr[position]);
                } else {
                    a04_findIdDay.setText("");
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
        a04_findIdEmailSpinner.setAdapter(e_FindSelectAdapter);
        a04_findIdEmailSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a04_findIdInputEmail2.setText(emailArr[position]);
                } else {
                    a04_findIdInputEmail2.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }//#################################### onCreate 종료 ####################################//

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
     * 학번 찾기 메소드
     */
    private void netConnForFindId() {
        String email1 = a04_findIdInputEmail1.getText().toString().trim();
        String email2 = a04_findIdInputEmail2.getText().toString().trim();
        String emailDomain = email1 + "@" + email2; //이메일 완성

        if (checkEmail(emailDomain)) {
            String name = a04_findIdInputName.getText().toString().trim();
            String year = a04_findIdYear.getText().toString().trim();
            year = year.substring(0, 4);
            String month = a04_findIdMonth.getText().toString().trim();
            month = month.substring(0, 2);
            String day = a04_findIdDay.getText().toString().trim();
            day = day.substring(0, 2);
            String birth = year + "-" + month + "-" + day;
            String email = emailDomain;

            ContentValues cValue = new ContentValues();
            cValue.put("userName", name);
            cValue.put("userBirth", birth);
            cValue.put("userEmail", email);

            String url = "http://cb.egreen.co.kr/mobile_proc/findUserInfo/find_userId_m2.asp";
            //      String url = "http://cb.egreen.co.kr/mobile_proc/test/findUserInfo/find_userId_m2.asp";

            NetworkConnect networkConnect = new NetworkConnect(url, cValue);
            networkConnect.execute();
        }
    }

    /* 학번 찾기 입력 필드 체크 */
    private boolean isInputCheck() {
        androidx.appcompat.app.AlertDialog.Builder ab = new androidx.appcompat.app.AlertDialog.Builder(this);
        ab.setCancelable(false);

        String name = a04_findIdInputName.getText().toString().trim(); //이름
        String year = a04_findIdYear.getText().toString().trim(); //년도
        String month = a04_findIdMonth.getText().toString().trim(); //월
        String day = a04_findIdDay.getText().toString().trim(); //일
        String email1 = a04_findIdInputEmail1.getText().toString().trim(); //사용자 이메일
        String email2 = a04_findIdInputEmail2.getText().toString().trim(); //이메일 도메인

        if (name.equals("")) { //이름 필드 체크
            ab.setMessage("이름을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findIdInputName.requestFocus();
                    a04_findIdInputName.setText("");
                    imm.showSoftInput(a04_findIdInputName, 0);
                }
            });
            ab.show();
        } else if (year.equals("")) { //년도 필드 체크
            ab.setMessage("생년월일을 선택해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findIdYear.requestFocus();
                }
            });
            ab.show();
        } else if (month.equals("")) { //월 필드 체크
            ab.setMessage("생년월일을 선택해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findIdMonth.requestFocus();
                }
            });
            ab.show();
        } else if (day.equals("")) { //일 필드 체크
            ab.setMessage("생년월일을 선택해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findIdDay.requestFocus();
                }
            });
            ab.show();
        } else if (email1.equals("")) {
            ab.setMessage("이메일을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findIdInputEmail1.requestFocus();
                    a04_findIdInputEmail1.setText("");
                    imm.showSoftInput(a04_findIdInputEmail1, 0);
                }
            }).show();
        } else if (email2.equals("")) {
            ab.setMessage("도메인을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findIdInputEmail2.requestFocus();
                    a04_findIdInputEmail2.setText("");
                    imm.showSoftInput(a04_findIdInputEmail2, 0);
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
        AlertDialog.Builder ab = new androidx.appcompat.app.AlertDialog.Builder(this);
        ab.setCancelable(false);

        if (emailRegular == false) {
            ab.setMessage("사용할 수 없는 이메일입니다.\n다시 확인해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a04_findIdInputEmail1.requestFocus();
                }
            }).show();
            return false;
        }
        return true;
    }

    /* 학번 찾기 버튼을 눌렀을 때 */
    public void go_Find(View view) {
        NetworkStateCheck netCheck = new NetworkStateCheck(this); //네트워크 연결 상태 확인
        if (netCheck.isConnectionNet()) {
            if (isInputCheck()) { //각 입력 필드 확인
                netConnForFindId();
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
            super.onPostExecute(s);

            androidx.appcompat.app.AlertDialog.Builder ab = new androidx.appcompat.app.AlertDialog.Builder(A04_FindId.this);
            ab.setCancelable(false);

            String[] result = s.trim().split("\\<");
            if (!result[0].equals("[]")) {
                //Custom Alert를 띄우기 위해 Custom된 layout을 불러옴
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customLayout = inflater.inflate(R.layout.a04_cs_find_id_result_alert, null);

                String id = "";
                String name = "";

                TextView a04_id = customLayout.findViewById(R.id.a04_id_strNum);
                TextView a04_name = customLayout.findViewById(R.id.a04_id_name);
                Button a04_login = customLayout.findViewById(R.id.a04_id_login);
                Button a04_findpw = customLayout.findViewById(R.id.a04_id_findPw);

                JSONObject jsObj = new JSONObject();

                try {
                    JSONArray jsArr = new JSONArray(s);

                    for (int i = 0; i < jsArr.length(); i++) {
                        jsObj = jsArr.getJSONObject(i);

                        id = jsObj.getString("cId").trim();
                        name = jsObj.getString("strName");
                    }

                    a04_id.setText(id);
                    a04_name.setText(name);

                    final String tName = name;
                    final String tId = id;
                    final String tYearData = a04_findIdYear.getText().toString();
                    final String tMonthData = a04_findIdMonth.getText().toString();
                    final String tDayData = a04_findIdDay.getText().toString();
                    final String tEmail1 = a04_findIdInputEmail1.getText().toString();
                    final String tEmail2 = a04_findIdInputEmail2.getText().toString();

                    ab.setView(customLayout);
                    final androidx.appcompat.app.AlertDialog dismissAb;
                    dismissAb = ab.create();
                    dismissAb.show();

                    /* 학번을 찾고 '로그인하기' 버튼을 눌렀을 때, A02_Login Activity로 이동. ID 전달 */
                    a04_login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(A04_FindId.this, A02_Login.class);
                            intent.putExtra("ID", tId);

                            dismissAb.dismiss();

                            startActivity(intent);
                            finish();
                        }
                    });

                    /* 학번을 찾고 '비밀번호 찾기' 버튼을 눌렀을 때, A04_FindPw Activity로 이동. ID 전달 */
                    a04_findpw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(A04_FindId.this, A04_FindPw.class);
                            intent.putExtra("NAME", tName);
                            intent.putExtra("ID", tId);
                            intent.putExtra("YEAR", tYearData);
                            intent.putExtra("MONTH", tMonthData);
                            intent.putExtra("DAY", tDayData);
                            intent.putExtra("EMAIL1", tEmail1);
                            intent.putExtra("EMAIL2", tEmail2);

                            dismissAb.dismiss();

                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    Log.i(TAG, "JSONArray Exc: " + e.getMessage());
                }
            } else {
                ab.setMessage("등록된 회원이 아닙니다.")
                        .setPositiveButton("확인", null)
                        .show();
            }
        }
    }
}