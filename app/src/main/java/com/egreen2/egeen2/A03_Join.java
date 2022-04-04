package com.egreen2.egeen2;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A03_Join.java
    [설명] 회원가입 입력 폼
    [작성자] 장희원
    [작성일시] 2020.12.09
*/

public class A03_Join extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = A03_Join.class.getSimpleName();
    /* View 변수 선언 */
    //이름, 년, 월, 일, 휴대폰 중간, 휴대폰 마지막, 이메일, 도메인
    public static EditText a03_inputName, a03_inputYear, a03_inputMonth, a03_inputDay, a03_inputPhone2, a03_inputPhone3, a03_inputEmail1, a03_inputEmail2, a03_inputPw, a03_reinputPw;
    private final Context context = this;
    //Spinner 휴대폰 앞자리, 년, 월, 일, 도메인
    Spinner a03_inputPhone1, a03_inputyearSpinner, a03_inputmonthSpinner, a03_inputdaySpinner, a03_emailSpinner;
    //Button 남성, 여성, 가입하기, 결과 페이지의 로그인 버튼
    Button btn_male, btn_female, a03_joinBtn, a03_login;
    /* Custom Alert 변수 선언 */
    // 결과 페이지의 학번, 이름, 년, 월, 일 이메일,도메인, 핸드폰 번호
    TextView a03_strNum, a03_name, a03_year, a03_month, a03_day, a03_birth, a03_email, a03_email2, a03_phone;
    String birthData, emailFull, selectFirstPhoneNum;
    // '-'이 포함된 휴대폰 번호 저장
    String phoneNumberWithHyphen;
    //성별선택
    String yourSex;
    ArrayList<Button> btnSexArr = new ArrayList<>();
    //키보드 컨트롤
    InputMethodManager imm;
    private DrawerLayout mDrawerLayout;

    //#################################### onCreate 시작 ####################################//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a03_join);

        /* View 연결 */
        btn_male = findViewById(R.id.a03_btn_male);
        btn_female = findViewById(R.id.a03_btn_female);
        a03_year = findViewById(R.id.a03_year);
        a03_month = findViewById(R.id.a03_month);
        a03_day = findViewById(R.id.a03_day);
        a03_email = findViewById(R.id.a03_inputEmail1);
        a03_email2 = findViewById(R.id.a03_inputEmail2);
        ImageView imageView = findViewById(R.id.logo);


        a03_inputName = findViewById(R.id.a03_inputName);    //이름 입력
        a03_inputYear = findViewById(R.id.a03_year);         //년 입력
        a03_inputMonth = findViewById(R.id.a03_month);        //월 입력
        a03_inputDay = findViewById(R.id.a03_day);          //일 입력
        a03_inputyearSpinner = findViewById(R.id.a03_yearSpinner);  //년 선택
        a03_inputmonthSpinner = findViewById(R.id.a03_monthSpinner); //월 선택
        a03_inputdaySpinner = findViewById(R.id.a03_daySpinner);   //일 선택

        a03_inputPhone1 = findViewById(R.id.a03_inputPhone1);  //휴대폰 앞자리
        a03_inputPhone2 = findViewById(R.id.a03_inputPhone2);  //휴대폰 중간
        a03_inputPhone3 = findViewById(R.id.a03_inputPhone3);  //휴대폰 마지막

        a03_inputEmail1 = findViewById(R.id.a03_inputEmail1); //이메일 입력
        a03_inputEmail2 = findViewById(R.id.a03_inputEmail2); //도메인 입력
        a03_emailSpinner = findViewById(R.id.a03_emailSpinner);//도메인 선택

        a03_inputPw = findViewById(R.id.a03_inputPw);    //비밀번호 입력
        a03_reinputPw = findViewById(R.id.a03_reinputPw);  //비밀번호 재입력
        a03_joinBtn = findViewById(R.id.a03_join_btn);     //회원가입 버튼

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a03_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //메뉴 버튼 이미지 지정

        mDrawerLayout = findViewById(R.id.drawer_layout);

        /* 성별 초기 설정*/
        btn_male.setOnClickListener(this);
        btn_female.setOnClickListener(this);
        btnSexArr.add(btn_male);
        btnSexArr.add(btn_female);
        yourSex = "남";
        setSexChoise(yourSex);

        a03_joinBtn.setOnClickListener(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), before_Main.class);
                startActivity(intent);
            }
        });
        /* 년도를 선택하는 Spinner 호출 */
        final String[] yearArr = getResources().getStringArray(R.array.yearArr);
        final ArrayAdapter<String> a03_yearSelectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, yearArr);
        a03_inputyearSpinner.setAdapter(a03_yearSelectAdapter);
        a03_inputyearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a03_inputYear.setText(yearArr[position]);
                } else {
                    a03_inputYear.setText("년");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* 월을 선택하는 Spinner 호출 */
        final String[] monthArr = getResources().getStringArray(R.array.monthArr);
        final ArrayAdapter<String> a03_monthSelectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthArr);
        a03_inputmonthSpinner.setAdapter(a03_monthSelectAdapter);
        a03_inputmonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a03_inputMonth.setText(monthArr[position]);
                } else {
                    a03_inputMonth.setText("월");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        /* 일을 선택하는 Spinner 호출 */
        final String[] dayArr = getResources().getStringArray(R.array.dayArr);
        final ArrayAdapter<String> a03_daySelectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dayArr);
        a03_inputdaySpinner.setAdapter(a03_daySelectAdapter);
        a03_inputdaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a03_inputDay.setText(dayArr[position]);
                } else {
                    a03_inputDay.setText("일");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* 휴대폰 앞자리 선택하는 Spinner 호출 */
        final String[] phoneArr = getResources().getStringArray(R.array.phoneArr);
        ArrayAdapter<String> a03_PhoneSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, phoneArr);
        a03_inputPhone1.setAdapter(a03_PhoneSelectAdapter);
        a03_inputPhone1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectFirstPhoneNum = phoneArr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* 도메인 선택하는 Spinner 호출 */
        final String[] emailArr = getResources().getStringArray(R.array.emailArr);
        final ArrayAdapter<String> a03_EmailSelectAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, emailArr);
        a03_emailSpinner.setAdapter(a03_EmailSelectAdapter);
        a03_emailSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    a03_inputEmail2.setText(emailArr[position]);
                } else {
                    a03_inputEmail2.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a03_btn_male:
                yourSex = "남";
                setSexChoise(yourSex);  //성별 클릭 시 하이라이트
                break;
            case R.id.a03_btn_female:
                yourSex = "여";
                setSexChoise(yourSex);  //성별 클릭 시 하이라이트

                break;
            case R.id.a03_join_btn:
                NetworkStateCheck netCheck = new NetworkStateCheck(this);   //네트워크 연결 상태 확인
                if (netCheck.isConnectionNet()) {
                    if (isInputCheck()) {   //각 입력 필드 확인
                        netConnForJoin();
                    }
                }
                break;
        }
    }

    //#################################### onCreate 종료 ####################################//

    /**
     * 회원가입
     */
    private void netConnForJoin() {
        String phone2 = a03_inputPhone2.getText().toString();
        String phone3 = a03_inputPhone3.getText().toString();
        phoneNumberWithHyphen = selectFirstPhoneNum + "-" + phone2 + "-" + phone3;

        if (passwordMatch()) {
            String email1 = a03_inputEmail1.getText().toString();
            String email2 = a03_inputEmail2.getText().toString();
            emailFull = email1 + "@" + email2;

            if (checkEmail(emailFull)) {
                String name = a03_inputName.getText().toString().trim();
                String year = a03_year.getText().toString().trim();
                String month = a03_month.getText().toString().trim();
                String day = a03_day.getText().toString().trim();
                year = year.substring(0, 4);
                month = month.substring(0, 2);
                day = day.substring(0, 2);
                String birth = year + "-" + month + "-" + day;
                String password = a03_inputPw.getText().toString();
                String phoneNumber = phoneNumberWithHyphen;
                String email = emailFull;
                String permission = "10";

                ContentValues cValue = new ContentValues();
                cValue.put("strName", name);
                cValue.put("birth", birth);

                String gender;
                if (yourSex.equals("남")) {
                    gender = "1";
                } else {
                    gender = "2";
                }
                cValue.put("sex", gender);
                cValue.put("pass", password);
                cValue.put("phone", phoneNumber);
                cValue.put("email", email);
                cValue.put("permission", permission);

                String url = "http://cb.egreen.co.kr/mobile_proc/join/join_proc_m2.asp";
                Log.i(TAG, name + "/" + birth + "/" + gender + "/" + phoneNumber + "/" + email);

                NetworkConnect networkConnect = new NetworkConnect(url, cValue);
                networkConnect.execute();
            }
        }
    }

    /**
     * 비밀번호 입력 상태 확인 - 두번째 입력과 일치하는가
     */
    private boolean passwordMatch() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);

        String pw = a03_inputPw.getText().toString();   //비밀번호 입력
        String rPw = a03_reinputPw.getText().toString();    //비밀번호 재입력

        final Boolean isMatch;

        if (!rPw.equals(pw)) {
            isMatch = false;

            ab.setMessage("비밀번호가 일치하지 않습니다.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_reinputPw.setText("");
                    a03_reinputPw.requestFocus();
                    // imm.showSoftInput(a03_reinputPw, 0);
                }
            }).show();
        } else {
            isMatch = true;
        }
        return isMatch;
    }

    /**
     * 성별 클릭 하이라이트
     */
    private void setSexChoise(String _sex) {
        for (int i = 0; i < btnSexArr.size(); i++) {
            if (btnSexArr.get(i).getText().toString().equals(_sex)) {
                btnSexArr.get(i).setTextColor(Color.WHITE);
                btnSexArr.get(i).setBackgroundResource(R.drawable.btn_back_color);
            } else {
                btnSexArr.get(i).setTextColor(Color.BLACK);
                btnSexArr.get(i).setBackgroundResource(R.drawable.btn_back_color_white);
            }
        }
    }

    /**
     * 회원가입 입력 체크
     */
    private boolean isInputCheck() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);
        String name = a03_inputName.getText().toString().trim();    //이름
        String year = a03_year.getText().toString().trim();
        String month = a03_month.getText().toString().trim();
        String day = a03_day.getText().toString().trim();
        String birthday = year + "-" + month + "-" + day;

        String pw = a03_inputPw.getText().toString().trim();    //비밀번호
        String rPw = a03_reinputPw.getText().toString().trim(); //비밀번호 재입력
        String phone2 = a03_inputPhone2.getText().toString().trim(); //휴대폰 가운데
        String phone3 = a03_inputPhone3.getText().toString().trim(); //휴대폰 마지막
        String email1 = a03_inputEmail1.getText().toString().trim(); //이메일
        String email2 = a03_inputEmail2.getText().toString().trim(); //도메인

        //휴대폰 번호 정규식
        phoneNumberWithHyphen = selectFirstPhoneNum + "-" + phone2 + "-" + phone3;
        String pattern = "^\\d{3}-\\d{3,4}-\\d{4}$";
        //비밀번호 정규식 (숫자, 문자, 특수문자 포함 8~15자리 이내)
        String pattern2 = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
        //이름 정규식 (한글만)
        String pattern3 = "^[가-힣]*$";
        //생년월일 정규식
        String pattern4 = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";


        //생년월일 값 subString 할때 값이 없으면 오류발생 방지
        /*if (!year.equals("") && !day.equals("") && !month.equals("")) {
            year = year.substring(0, 4);
            month = month.substring(0, 2);
            day = day.substring(0, 2);
        }

        else*/
        if (name.equals("")) {  //이름 체크
            ab.setMessage("이름을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputName.setText("");
                    a03_inputName.requestFocus();
//                    imm.showSoftInput(a03_inputName, 0);
                }
            });
            ab.show();
        } else if (year.equals("년") || year.equals("")) { //년도 체크
            ab.setMessage("생년월일을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_year.setText("년");
                    a03_year.requestFocus();
                    //                  imm.showSoftInput(a03_year, 0);
                }
            });
            ab.show();
        } else if (month.equals("월") || month.equals("")) { //월 체크
            ab.setMessage("생년월일을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_month.setText("");
                    a03_month.requestFocus();
                    //  imm.showSoftInput(a03_month, 0);
                }
            });
            ab.show();
        } else if (day.equals("일") || day.equals("")) { //일 체크
            ab.setMessage("생년월일을 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_day.setText("");
                    a03_day.requestFocus();
                    //     imm.showSoftInput(a03_day, 0);
                }
            });
            ab.show();
        } else if (pw.equals("")) { //비밀번호 체크
            ab.setMessage("비밀번호를 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputPw.setText("");
                    a03_inputPw.requestFocus();
//                    imm.showSoftInput(a03_inputPw, 0);
                }
            });
            ab.show();
        } else if (rPw.equals("")) { //비밀번호 재확인 체크
            ab.setMessage("비밀번호를 한번 더 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_reinputPw.setText("");
                    a03_reinputPw.requestFocus();
                    //                imm.showSoftInput(a03_reinputPw, 0);
                }
            });
            ab.show();
        } else if (phone2.equals("")) { //핸드폰 가운데 체크
            ab.setMessage("휴대폰번호를 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputPhone2.setText("");
                    a03_inputPhone2.requestFocus();
                    //                 imm.showSoftInput(a03_inputPhone2, 0);
                }
            });
            ab.show();
        } else if (phone3.equals("")) { //핸드폰 마지막 체크
            ab.setMessage("휴대폰번호를 입력해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputPhone3.setText("");
                    a03_inputPhone3.requestFocus();
                    //           imm.showSoftInput(a03_inputPhone3, 0);
                }
            });
            ab.show();
        } else if (email1.equals("")) { //이메일 체크
            ab.setMessage("이메일을 확인해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputEmail1.setText("");
                    a03_inputEmail1.requestFocus();
                    //      imm.showSoftInput(a03_inputEmail1, 0);
                }
            });
            ab.show();
        } else if (email2.equals("")) { //이메일 체크
            ab.setMessage("이메일을 확인해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputEmail2.setText("");
                    a03_inputEmail2.requestFocus();
                    //         imm.showSoftInput(a03_inputEmail2, 0);
                }
            });
            ab.show();
        }
        //------------정규식 체크---------
        /*
        else if (!Pattern.matches(pattern3, name)) {
            ab.setMessage("이름은 한글만 입력 가능합니다. ");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputName.setText("");
                    a03_inputName.requestFocus();
                }
            }).show();
        }
        */
        else if (!Pattern.matches(pattern2, pw)) {
            ab.setMessage("비밀번호는 숫자,문자,특수문자 포함 8~20자리 이내로 설정해주세요. ");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputPw.setText("");
                    a03_reinputPw.setText("");
                    a03_inputPw.requestFocus();
                }
            }).show();
        } else if (!Pattern.matches(pattern, phoneNumberWithHyphen)) {
            ab.setMessage("올바른 전화번호 형식이 아닙니다. ");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputPhone2.setText("");
                    a03_inputPhone3.setText("");
                    a03_inputPhone2.requestFocus();
                }
            }).show();
        }

        /*
        else if (!Pattern.matches(pattern4, birthday)) {
            ab.setMessage("생년월일을 다시 입력해주세요. ");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputYear.setText("");
                    a03_inputDay.setText("");
                    a03_inputMonth.setText("");
                    a03_inputYear.requestFocus();
                }
            }).show();
        }

         */
        else {
            return true;
        }
        return false;
    }

    /**
     * 이메일 유효성 체크
     */
    private boolean checkEmail(String email) {
        boolean emailRegular = Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", email.trim());
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);

        if (emailRegular == false) {
            ab.setMessage("사용할 수 없는 이메일입니다.\n이메일을 다시 확인해주세요.");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a03_inputEmail1.requestFocus();
                }
            }).show();
            return false;
        }
        return true;
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

    /**
     * 키보드 내리기 배경터치이벤트
     */
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

            AlertDialog.Builder ab = new AlertDialog.Builder(A03_Join.this);
            ab.setCancelable(false);


            String[] result = (s.trim().split("\\<"));

            if (result[0].equals("already account")) {
                ab.setMessage("이미 등록된 회원입니다.");
                ab.setPositiveButton("확인", null).show();

            } else if (result[0].equals("already email")) {
                ab.setMessage("해당 이메일로 가입된 이력이 있습니다.");
                ab.setPositiveButton("확인", null).show();
            } else if (result[0].equals("empty")) {
                ab.setMessage("회원가입중 오류가 발생했습니다.\n본 원으로 문의해주세요.");
                ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
            } else { //가입 성공시 결과 페이지로 넘어감
/*
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customLayout = inflater.inflate(R.layout.a03_join_result, null);*/

                final String strNum = result[0];
                String name = a03_inputName.getText().toString();
                String year = a03_year.getText().toString();
                String month = a03_month.getText().toString();
                String day = a03_day.getText().toString();
                String birth = year + month + day;
                String phone = phoneNumberWithHyphen;
                String email = emailFull;
                Log.d("학번", strNum);

                Intent intent = new Intent(A03_Join.this, A03_JoinResult.class);
                intent.putExtra("학번", strNum);
                intent.putExtra("이름", name);
                intent.putExtra("생년월일", birth);
                intent.putExtra("휴대폰번호", phone);
                intent.putExtra("이메일", email);
                startActivity(intent);



                    /* a03_strNum = customLayout.findViewById(R.id.a03_strNumber);
                     a03_name = customLayout.findViewById(R.id.a03_name);
                     a03_birth = customLayout.findViewById(R.id.a03_birth);
                     a03_phone = customLayout.findViewById(R.id.a03_phone);
                     a03_email = customLayout.findViewById(R.id.a03_email);
                     a03_login = customLayout.findViewById(R.id.a03_loginBtn);*/

                a03_strNum.setText(strNum);
                a03_name.setText(name);
                a03_birth.setText(birth);
                a03_phone.setText(phone);
                a03_email.setText(email);

                /*    ab.setView(customLayout);*/

                final AlertDialog dismissAb;
                dismissAb = ab.create();
                dismissAb.setCanceledOnTouchOutside(false);

                dismissAb.show();

                a03_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(A03_Join.this, A02_Login.class);
                        intent.putExtra("ID", strNum);

                        dismissAb.dismiss();

                        startActivity(intent);
                        finish();
                    }

                });

            }

        }
    }

}
