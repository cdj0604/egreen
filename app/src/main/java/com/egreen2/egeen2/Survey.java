package com.egreen2.egeen2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.egreen2.egeen2.Data.StudyInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Survey extends AppCompatActivity {
    private static final String TAG = Survey.class.getSimpleName();
    EditText et_comment;
    EditText et_name;
    String _regDate;
    //학습에 필요한 정보
    StudyInfo si;
    private RadioGroup survey_RG_A;
    private RadioGroup survey_RG_B;
    private RadioGroup survey_RG_C;
    private RadioGroup survey_RG_D;
    private RadioGroup survey_RG_E;
    private RadioGroup survey_RG_F;
    private RadioGroup survey_RG_G;
    private RadioGroup survey_RG_H;
    private int aValue;
    /**
     * 첫번째 질문에 대한 라디오 버튼 처리다.
     */
    RadioGroup.OnCheckedChangeListener listener_A = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.a1:
                    aValue = 1;
                    break;
                case R.id.a2:
                    aValue = 2;
                    break;
                case R.id.a3:
                    aValue = 3;
                    break;
                case R.id.a4:
                    aValue = 4;
                    break;
            }
        }
    };
    private int bValue;
    /**
     * 두번째 질문에 대한 라디오 버튼 처리다.
     */
    RadioGroup.OnCheckedChangeListener listener_B = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.b1:
                    bValue = 1;
                    break;
                case R.id.b2:
                    bValue = 2;
                    break;
                case R.id.b3:
                    bValue = 3;
                    break;
                case R.id.b4:
                    bValue = 4;
                    break;
                case R.id.b5:
                    bValue = 5;
                    break;
            }
        }
    };
    private int cValue;
    /**
     * 세번째 질문에 대한 라디오 버튼 처리다.
     */
    RadioGroup.OnCheckedChangeListener listener_C = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.c1:
                    cValue = 1;
                    break;
                case R.id.c2:
                    cValue = 2;
                    break;
                case R.id.c3:
                    cValue = 3;
                    break;
                case R.id.c4:
                    cValue = 4;
                    break;
            }
        }
    };
    private int dValue;
    /**
     * 네번째 질문에 대한 라디오 버튼 처리다.
     */
    RadioGroup.OnCheckedChangeListener listener_D = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.d1:
                    dValue = 1;
                    break;
                case R.id.d2:
                    dValue = 2;
                    break;
                case R.id.d3:
                    dValue = 3;
                    break;
                case R.id.d4:
                    dValue = 4;
                    break;
            }
        }
    };
    private int eValue;
    /**
     * 다섯번째 질문에 대한 라디오 버튼 처리다.
     */
    RadioGroup.OnCheckedChangeListener listener_E = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.e1:
                    eValue = 1;
                    break;
                case R.id.e2:
                    eValue = 2;
                    break;
                case R.id.e3:
                    eValue = 3;
                    break;
                case R.id.e4:
                    eValue = 4;
                    break;
            }
        }
    };
    private int fValue;
    /**
     * 여섯번째 질문에 대한 라디오 버튼 처리다.
     */
    RadioGroup.OnCheckedChangeListener listener_F = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.f1:
                    fValue = 1;
                    break;
                case R.id.f2:
                    fValue = 2;
                    break;
                case R.id.f3:
                    fValue = 3;
                    break;
                case R.id.f4:
                    fValue = 4;
                    break;
            }
        }
    };
    private int gValue;
    /**
     * 일곱번째 질문에 대한 라디오 버튼 처리다.
     */
    RadioGroup.OnCheckedChangeListener listener_G = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.g1:
                    gValue = 1;
                    break;
                case R.id.g2:
                    gValue = 2;
                    break;
                case R.id.g3:
                    gValue = 3;
                    break;
                case R.id.g4:
                    gValue = 4;
                    break;
                case R.id.g5:
                    gValue = 5;
                    break;
            }
        }
    };
    private int hValue;
    /**
     * 여덟번째 질문에 대한 라디오 버튼 처리다.
     */
    RadioGroup.OnCheckedChangeListener listener_H = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.h1:
                    hValue = 1;
                    break;
                case R.id.h2:
                    hValue = 2;
                    break;
                case R.id.h3:
                    hValue = 3;
                    break;
                case R.id.h4:
                    hValue = 4;
                    break;
                case R.id.h5:
                    hValue = 5;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey);

        et_comment = findViewById(R.id.comment);
        et_name = findViewById(R.id.et_name);

        TextView regDate = findViewById(R.id.regDate);
        Button btn_ok = findViewById(R.id.btn_ok);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        _regDate = format.format(date);
        regDate.setText("등록일 : " + _regDate);

        survey_RG_A = findViewById(R.id.survey_RG_A);
        survey_RG_B = findViewById(R.id.survey_RG_B);
        survey_RG_C = findViewById(R.id.survey_RG_A);
        survey_RG_D = findViewById(R.id.survey_RG_D);
        survey_RG_E = findViewById(R.id.survey_RG_E);
        survey_RG_F = findViewById(R.id.survey_RG_F);
        survey_RG_G = findViewById(R.id.survey_RG_G);
        survey_RG_H = findViewById(R.id.survey_RG_H);

        //초기화
        aValue = bValue = cValue = dValue = eValue = fValue = gValue = hValue = 1;

        survey_RG_A.setOnCheckedChangeListener(listener_A);
        survey_RG_B.setOnCheckedChangeListener(listener_B);
        survey_RG_C.setOnCheckedChangeListener(listener_C);
        survey_RG_D.setOnCheckedChangeListener(listener_D);
        survey_RG_E.setOnCheckedChangeListener(listener_E);
        survey_RG_F.setOnCheckedChangeListener(listener_F);
        survey_RG_G.setOnCheckedChangeListener(listener_G);
        survey_RG_H.setOnCheckedChangeListener(listener_H);

        //A6_StudyCenter 에서 전달한 학습정보를 저장
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");
        try {
            Log.i(TAG, "A6_StudyCenter 에서 전달 받은 =====> " + si.getUserId());
        } catch (Exception e) {
            Log.i(TAG, "A6_StudyCenter 에서 전달 받은 =====> 없음");
        }

        et_name.setText(si.getUserName());

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().equals("")) {
                    Toast.makeText(Survey.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    //네트워크 통신
                    netConnForUpdateSurveyResult();
                }
            }
        });
    }

    /**
     * 사전설문 응답 결과를 디비에 업데이트하기 위한 통신이다.
     */
    private void netConnForUpdateSurveyResult() {
        String _comment = et_comment.getText().toString().trim();
        String _name = et_name.getText().toString().trim();

        String url = "http://cb.egreen.co.kr/mobile_proc/mypage/new/survey_input_m.asp";
        String result;

        ContentValues cValues = new ContentValues();
        cValues.put("userId", si.getUserId());
        cValues.put("classId", si.getClassId());
        cValues.put("userName", _name);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        _regDate = format.format(date);
        cValues.put("regDate", _regDate);

        cValues.put("m1", aValue);
        cValues.put("m2", bValue);
        cValues.put("m3", cValue);
        cValues.put("m4", dValue);
        cValues.put("m5", eValue);
        cValues.put("m6", fValue);
        cValues.put("m7", gValue);
        cValues.put("m8", hValue);

        if (_comment.equals("")) {
            _comment = "수고하세요.";
        }
        cValues.put("comment", _comment);

        Log.i(TAG, cValues.toString());

        SetSurveyResultNetTask setSurveyResultNT = new SetSurveyResultNetTask(url, cValues);
        setSurveyResultNT.execute();
    }

    /**
     * 설문조사 디비 업데이트에 실패했다.
     */
    private void fail() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("설문조사를 완료하지 못했습니다.\n본 원으로 문의주세요.");
        ab.setCancelable(false);
        ab.setPositiveButton("강의목록으로 이동", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveToA6_StudyCenter();
            }
        });

        ab.show();
    }

    /**
     * 설문조사 디비 업데이트에 성공했다.
     */
    private void success_proc() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("설문조사를 완료했습니다\n감사합니다.");
        ab.setCancelable(false);
        ab.setPositiveButton("강의목록으로 이동", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveToA6_StudyCenter();
            }
        });

        ab.show();
    }

    /**
     * A6_StudyCenter로 이동
     */
    private void moveToA6_StudyCenter() {
        SharedPreferences savedCertyState = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedCertyState.edit();
        editor.putBoolean("certyState", true);
        editor.commit();

        Intent intent = new Intent(this, A09_Classroom.class);
        intent.putExtra("studyInfo", si);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        moveToA6_StudyCenter();
    }

    /**
     * 학습동의서를 저장하기 위한 AsyncTask
     */
    public class SetSurveyResultNetTask extends AsyncTask<Void, Void, String> {
        private final String url;
        private final ContentValues values;

        public SetSurveyResultNetTask(String url, ContentValues values) {
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
