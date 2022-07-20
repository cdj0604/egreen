package com.egreen2.egeen2;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.egreen2.egeen2.Data.StudyInfo;
import com.egreen2.egeen2.Dialog.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class StudyAgreement extends AppCompatActivity {
    private static final String TAG = StudyAgreement.class.getSimpleName();
    public EditText et_graduationDate;
    EditText et_name;
    EditText et_highSchoolName;
    String _name, _regDate, _highSchoolName, _graduationYear, _graduationMonth, _graduationDay;

    //키보드 컨트롤
    InputMethodManager imm;

    //학습에 필요한 정보
    StudyInfo si;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_agreement);

        et_highSchoolName = findViewById(R.id.et_highSchoolName);
        et_graduationDate = findViewById(R.id.et_graduationDate);

        //A6_StudyCenter 에서 전달한 학습정보를 저장
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");
        try {
            Log.i(TAG, "A6_StudyCenter 에서 전달 받은 =====> " + si.getUserId());
        } catch (Exception e) {
            Log.i(TAG, "A6_StudyCenter 에서 전달 받은 =====> 없음");
        }

        //키보드 올리기/내리기 설정
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        et_graduationDate.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imm.hideSoftInputFromWindow(et_graduationDate.getWindowToken(), 0);
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("CALL_DATEPICKER_BY_THIS", "StudyAgreement");
                    datePickerFragment.setArguments(bundle);

                    DialogFragment newPicker = datePickerFragment;
                    newPicker.show(getSupportFragmentManager(), "datePicker");
                }

                return false;
            }
        });

        if (isThereHighSchoolName()) {
            et_highSchoolName.setVisibility(View.INVISIBLE);
            et_graduationDate.setVisibility(View.INVISIBLE);
        } else {
            et_highSchoolName.setVisibility(View.VISIBLE);
            et_graduationDate.setVisibility(View.VISIBLE);
        }

        et_name = findViewById(R.id.et_name);
        et_name.setText(si.getUserName());

        TextView regDate = findViewById(R.id.regDate);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        _regDate = format.format(date);
        regDate.setText("등록일 : " + _regDate);

        Button btn_agree = findViewById(R.id.btn_agree);

        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //네트워크 통신
                //통신 성공하면,
                //결과 : A7_ClassRoom.activity 로 이동.

                if (ok_check()) {
                    netConnForUpdateStudyAgreement();
                }
            }
        });
    }

    /**
     * 동의 라디오 버튼을 체크한다.
     */
    private boolean ok_check() {
        final RadioButton a1_ok, a2_ok, a3_ok, a4_ok, a5_ok;

        a1_ok = findViewById(R.id.a1_ok);
        a2_ok = findViewById(R.id.a2_ok);
        a3_ok = findViewById(R.id.a3_ok);
        a4_ok = findViewById(R.id.a4_ok);
        a5_ok = findViewById(R.id.a5_ok);

        if (a1_ok.isChecked() && a2_ok.isChecked() && a3_ok.isChecked() && a4_ok.isChecked() && a5_ok.isChecked()) {
            return true;
        } else {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setMessage("모두 동의해주셔야 합니다.");
            ab.setPositiveButton("확인", null);
            ab.show();

            return false;
        }
    }

    /**
     * 학습동의서를 DB에 업데이트 한다.
     */
    private void netConnForUpdateStudyAgreement() {
        if (inputCheck()) {
            String url = "http://cb.egreen.co.kr/mobile_proc/mypage/new/dong_post_m2.asp";

            ContentValues cValues = new ContentValues();
            cValues.put("userId", si.getUserId());
            cValues.put("classId", si.getClassId());
            cValues.put("userName", _name);

            if (isThereHighSchoolName() == false) {
                cValues.put("highSchoolName", _highSchoolName);
                cValues.put("graduationYear", _graduationYear);
                cValues.put("graduationMonth", _graduationMonth);
                cValues.put("graduationDay", _graduationDay);
            }

            Log.i(TAG, cValues.toString());

            SetStudyAgreementNetTask setStudyAgreementNT = new SetStudyAgreementNetTask(url, cValues);
            setStudyAgreementNT.execute();
        }
    }

    /**
     * 입력필드를 체크한다.
     */
    private boolean inputCheck() {
        String _graduationDate = et_graduationDate.getText().toString().trim();
        _highSchoolName = et_highSchoolName.getText().toString().trim();
        _name = et_name.getText().toString().trim();

        AlertDialog.Builder ab = new AlertDialog.Builder(this);

        if (isThereHighSchoolName() == false) {
            //고등학교명이 DB에 없다면, 체크
            if (_highSchoolName.equals("")) {
                ab.setTitle("학교명 미입력");
                ab.setMessage("고등학교명을 입력해주세요");
                ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et_highSchoolName.setText("");
                        et_highSchoolName.requestFocus();
                    }
                });

                ab.show();

                return false;
            } else if (_highSchoolName.length() < 3) {
                ab.setTitle("학교명 입력오류");
                ab.setMessage("고등학교명이 너무 짧습니다.\n다시한번 확인해주세요.");
                ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et_highSchoolName.setText("");
                        et_highSchoolName.requestFocus();
                    }
                });

                ab.show();

                return false;
            }

            if (_graduationDate.equals("")) {
                ab.setTitle("졸업연도 미입력");
                ab.setMessage("졸업연도를 입력해주세요");
                ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et_graduationDate.setText("");
                        et_graduationDate.requestFocus();
                    }
                });

                ab.show();

                return false;
            } else {
                String[] arrGraduationDate = _graduationDate.split("-");
                //년
                _graduationYear = arrGraduationDate[0];     //'졸업일자:' 가 포함되어 있어서
                _graduationYear = _graduationYear.substring(_graduationYear.length() - 4);     //':' 다음, 5번째부터 문자열을 자른다.

                //월, 1자리(1~9)라면, 01,02,03 ... 식으로 바꿔준다.
                _graduationMonth = arrGraduationDate[1];
                if (_graduationMonth.length() == 1) {
                    _graduationMonth = "0" + _graduationMonth;
                }

                //일, 1자리(1~9)라면, 01,02,03 ... 식으로 바꿔준다.
                _graduationDay = arrGraduationDate[2];
                if (_graduationDay.length() == 1) {
                    _graduationDay = "0" + _graduationDay;
                }
            }
        }

        if (_name.equals("")) {
            ab.setMessage("이름을 입력해주세요");
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    et_name.requestFocus();
                }
            });
            ab.show();

            return false;
        }

        return true;
    }

    /**
     * 설문조사 디비 업데이트에 실패했다.
     */
    private void fail() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("학습동의서를 완료하지 못했습니다.\n본 원으로 문의주세요.");
        ab.setCancelable(false);
        ab.setPositiveButton("강의목록으로 이동", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveToA09_Classroom();
            }
        });

        ab.show();
    }

    /**
     * 설문조사 디비 업데이트에 성공했다.
     */
    private void success_proc() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("학습동의서를 완료했습니다\n감사합니다.");
        ab.setCancelable(false);
        //학습동의서 완료후 설문조사 페이지로 이동 or 설문조사 완료됐다면 강의실로 이동
        ab.setPositiveButton("사전설문조사 하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveToA09_Classroom();
            }
        });

        ab.show();
    }

    /**
     * A6_StudyCenter로 이동
     */
    //학습동의서 완료후 설문조사 페이지로 이동 or 설문조사 완료됐다면 강의실로 이동
    private void moveToA09_Classroom() {
        SharedPreferences savedCertyState = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedCertyState.edit();
        editor.putBoolean("certyState", true);
        editor.commit();

        Intent intent = new Intent(this, Survey.class);
        intent.putExtra("studyInfo", si);
        startActivity(intent);
        finish();
    }

    /**
     * 과목 ID를 가져온다.
     */
    private boolean isThereHighSchoolName() {
        /*
         * A6_MyClass에서 전달한 과목 ID 를 가져온다.
         */
        Intent intent = getIntent();
        String isThereHighSchoolName = "";
        try {
            isThereHighSchoolName = intent.getStringExtra("IS_THERE_HIGHSCHOOL_NAME");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isThereHighSchoolName.equals("False")) {
            Log.i(TAG, "isThereHighSchoolName 없다.");

            return false;
        } else {
//            Log.i(TAG, classId);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(this, Survey.class);
        intent.putExtra("studyInfo", si);
        startActivity(intent);
        finish();
    }

    /**
     * 학습동의서를 저장하기 위한 AsyncTask
     */
    public class SetStudyAgreementNetTask extends AsyncTask<Void, Void, String> {
        private final String url;
        private final ContentValues values;

        public SetStudyAgreementNetTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
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
            String result = s;

            if (result.equals("FAIL")) {
                //통신에러
            } else {
                if (result.equals("Err")) {
                    fail();
                } else {
                    success_proc();
                }
            }
        }
    }
}
