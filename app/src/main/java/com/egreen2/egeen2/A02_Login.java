package com.egreen2.egeen2;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.egreen2.egeen2.Data.StudyInfo;
import com.egreen2.egeen2.Dialog.Notify;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.Enumeration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A02_Login.java
    [설명] 로그인
    [작성자] 장희원
    [작성일시] 2021.03.25
 */

public class A02_Login extends AppCompatActivity {

    private static final String TAG = A02_Login.class.getSimpleName();
    private static final String AF_ASK_LOGOUT = "ASK_LOGOUT";
    /* 키보드 내리기 배경터치이벤트 */
    private static final String AF_NOID = "01";
    private static final String AF_NO_MATCH_PW = "02";
    private static final String AF_STOP_USER = "11";
    private static final String AF_NO_DATA = "14";
    /* View 변수 목록 */
    private static EditText a02_inputID, a02_inputPASS; //학번, 비밀번호 입력 필드
    private final int PERMISSIONSREQUEST_RESULT = 1;
    private final long backKeyPressedTime = 0;
    private final Context context = this;
    /* 공인인증서 변수 */
    private final String appKey = "OvRMXIzt8cX3lCHJukyLfg=="; //테스트 라이센스 10/31 까지 (배포전 정식 라이선스 발급 요청)
    private final String[] rtnParams = {"a", "b", "c"};
    TextView a02_version;           // 버전
    TextView a02_findID, a02_findPW;  // 학번/비번 찾기
    Button a02_login_btn, a02_join_btn; // 로그인, 회원가입
    CheckBox a02_chk_savedLoginInfo;        // 학번, 비밀번호 저장
    String id, pw, userName, loginNumber;
    String serial;
    boolean _isNeedCertyLogin, saveLoginData;
    SharedPreferences savedID_PW;
    /* 로딩을 표시하는 Dialog */
    ShowLoading loading;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;
    private DrawerLayout mDrawerLayout;

    /**
     * 접속 중인 IP 가져오기
     */
    public static String getConnectIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof InetAddress) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()");
    }

    // ####################################onCreate#########################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a02_login);

        /* View 연결 */
        a02_version = findViewById(R.id.a02_version); // 버전
        a02_inputID = findViewById(R.id.a02_inputID); //학번 입력 필드
        a02_inputPASS = findViewById(R.id.a02_inputPW); //비밀번호 입력 필드
        a02_findID = findViewById(R.id.a02_findId); // 학번 찾기
        a02_findPW = findViewById(R.id.a02_findPw); // 비밀번호 찾기
        a02_login_btn = findViewById(R.id.a02_login_btn); // 로그인 버튼
        a02_chk_savedLoginInfo = findViewById(R.id.a02_chk_savedLoginInfo); // 학번, 비밀번호 저장 체크박스

        //로고클릭
        ImageView imageView = findViewById(R.id.logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), before_Main.class);
                startActivity(intent);
            }
        });

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a02_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        //  actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu); //메뉴 버튼 이미지 지정

        mDrawerLayout = findViewById(R.id.drawer_layout);

        /* 저장된 학번과 비밀번호를 가져온다. */
        String _id = "", _pw = "";
        savedID_PW = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        try {
            _id = savedID_PW.getString("UID", "");
            _pw = savedID_PW.getString("UPW", "");
            saveLoginData = savedID_PW.getBoolean("SAVE_LOGIN_DATA", false);
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }

        if (_id.equals("")) {
            a02_inputID.setText("");
        } else {
            a02_inputID.setText(_id);
        }

        if (_pw.equals("")) {
            a02_inputPASS.setText("");
        } else {
            a02_inputPASS.setText(_pw);
        }
        //체크박스 상태 저장
        a02_chk_savedLoginInfo.setChecked(saveLoginData);


        /* 앱 버전을 가져온다. */
        try {
            a02_version.setText("V " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "앱 버전 가져오기 오류: " + e.getMessage());
        }


        SharedPreferences s = getSharedPreferences("APPVERSION", MODE_PRIVATE);//유저이름 저장
        SharedPreferences.Editor editor = s.edit();
        editor.putString("NOW_APPVERSION", String.valueOf(a02_version));
        editor.commit();
        /**
         * 안드로이드10 (API29,Q)부턴 권한체크 사용불가
         * 10이후 사용할 수 있는 대체 고유 식별자의 종류로 변경해야한다.
         */
        checkPermissions(); //권한 체크

    } // ####################################onCreate#########################################

    /**
     * 권한 체크
     * Pie 버전 이상에서 기기 일련번호를 가져오기 위해서는 원한 요청을 하고 승인을 받아야 함
     * 사용자에세 직접 승인 받아야 하는 요청은 인터넷으로 확인
     * CALL은 권한 필요 DIAL은 권한 필요없음. DIAL 사용중이기 때문에 권한설정 안해도 됨.
     */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONSREQUEST_RESULT);
                return;
            } else {
                serial = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.i(TAG, "serial: " + serial);
            }
        }
    }

    /**
     * 권한 요청 승인 결과 처리
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONSREQUEST_RESULT:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        //하나라도 거부한다면
                        new AlertDialog.Builder(this).setTitle("알림")
                                .setMessage("권한을 허용해 주셔야 앱을 이용할 수 있습니다.\n권한 체크가 안나올시, 설정 - 애플리케이션 - e그린앱 에서 전화권한 '허용함' 으로 변경해주세요.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .setCancelable(false).show();

                    } else {
                        Toast.makeText(this, "승인되었습니다.", Toast.LENGTH_SHORT).show();
                        checkPermissions();
                    }
                }
                break;
        }
    }

    /**
     * 입력필드의 값이 비어있는지 확인
     */
    private boolean isEmpty() {
        id = a02_inputID.getText().toString().trim();
        pw = a02_inputPASS.getText().toString().trim();

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);

        if (id.equals("")) {
            ab.setMessage("학번을 입력해주세요.");
            a02_inputID.setText("");
            a02_inputID.requestFocus();
        } else if (pw.equals("")) {
            ab.setMessage("비밀번호를 입력해주세요.");
            a02_inputPASS.requestFocus();
        } else {
            return true;
        }

        ab.setPositiveButton("확인", null);
        ab.show();

        return false;
    }

    /**
     * 로그인 네트워크 통신
     * <순서>
     * 1. 로그인이 되어있는 중인지 확인
     * 2. 로그인 중이라면 로그아웃 할 것인가
     * 3. 로그인
     */
    private void doLogin() {
        /*순서
            1.로그인이 되어있는 중인지 확인
            2.로그인 중이라면 로그아웃 할 것인지 물어본 후,
            3.로그인
        */
        String encodingPw;
        try {
            encodingPw = URLEncoder.encode(pw, "UTF-8");

            encodingPw = URLEncoder.encode(pw, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            encodingPw = pw;
        }

        //Log.d("encoding",encodingPw);

        String url = "http://cb.egreen.co.kr/mobile_proc/login/new/login_proc_m2.asp";
        ContentValues cValue = new ContentValues();
        cValue.put("userId", id);
        cValue.put("userPw", encodingPw);
        cValue.put("userIp", getConnectIpAddress());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            /*String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            cValue.put("uMobileKey", android_id);*/
            cValue.put("uMobileKey", Build.SERIAL);
        } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.P)) {
            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            cValue.put("uMobileKey", android_id);
        } else {
            cValue.put("uMobileKey", serial);
        }
        cValue.put("uMobileBrand", Build.BRAND + "::" + Build.MODEL);

        NetworkStateCheck netCheck = new NetworkStateCheck(this);
        if (netCheck.isConnectionNet()) {
            DoLoginNetTask doLoginNT = new DoLoginNetTask(url, cValue);
            doLoginNT.execute();

            loading = new ShowLoading(this, "로그인 중입니다.");
            loading.start();
        } else {
            Toast.makeText(A02_Login.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 로그인 조회 결과를 처리한다.
     * 인증서 유예자 처리.
     */
    private void loginResult_proc(String result) {
        String[] loginData = result.split(",");
        if (loginData[0].equals("Err")) {
            //오류 처리~
            error_proc(loginData[1]);
        } else if (loginData[0].equals("server_check")) {
            //혹시나... 서버에 문제가 있을때..
            showAlert(AF_ASK_LOGOUT, "죄송합니다.\n서버점검으로 인해 앱 수강이 어렵습니다.\n홈페이지를 참고해주세요");
        } else {
            //OK, 성공 로직~
            userName = loginData[1];     //학습자 이름
            boolean isNeedCertyLogin = Boolean.parseBoolean(loginData[2]);      //인증서 로그인 필요유무
            String postponementTerm = "";

            try {
                postponementTerm = loginData[3];     //인증서 유예 기간
            } catch (Exception e) {
                e.printStackTrace();
            }

            _isNeedCertyLogin = isNeedCertyLogin;
            if (isNeedCertyLogin) {
                //공인인증서 유예자라면,
                android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(this);
                ab.setTitle("공인인증서 유예 로그인");
                ab.setMessage(userName + "님은 공인인증서 유예자 입니다.\n유예기간은 " + postponementTerm + "까지 입니다.\n범용공인인증서를 준비해주세요.");
                ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //필독 안내 사항Dialog를 띄운다.
                        SharedPreferences sharedNotify = getSharedPreferences("NOTIFY", MODE_PRIVATE);
                        if (sharedNotify.getBoolean("CANCEL_CHECK", false)) {
                            success_proc();
                        } else {
                            //notify dialog 를 띄워 공지를 보여준다.
                            //'다시보지않기' 를 선택하지 않는 동안 계속 실행된다.
                            show_notify();
                        }
                    }
                });
                ab.show();

            } else {
                success_proc();
            }
        }
    }

    /**
     * 최종 로그인 완료, 학번/비번 저장하기 선택에 따른 학번/비번 저장,
     * MainActivity 로 이동한다.
     */
    private void success_proc() {
        loading = new ShowLoading(this, "로그인 중입니다");
        loading.start();

        String url = "http://cb.egreen.co.kr/mobile_proc/login/new/login_insert_m2.asp";
        ContentValues cValues = new ContentValues();
        cValues.put("userId", id);
        cValues.put("uMobileBrand", Build.BRAND + "::" + Build.MODEL);

        LoginSuccessNetTask loginSuccessNT = new LoginSuccessNetTask(url, cValues);
        loginSuccessNT.execute();
    }

    /**
     * Activity 를 이동한다.
     */
    private void moveActivity(Class activity) {
        StudyInfo si = new StudyInfo();
        si.setLoginNumber(loginNumber);
        si.setUserId(id);
        si.setUserName(userName);

        Intent intent = new Intent(this, activity);
        intent.putExtra("studyInfo", si);
        startActivity(intent);
        finish();

        try {
            Log.i(TAG, "MainActivity로 전달하는 =====> " + si.getUserId());
            Log.i(TAG, "MainActivity로 전달하는 =====> " + si.getLoginNumber());
            Log.i(TAG, "MainActivity로 전달하는 =====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "MainActivity로 전달하는 =====> 없음");
        }
    }

    /**
     * 로그인 오류 처리
     */
    private void error_proc(String errCode) {
        if (errCode.equals(AF_NOID)) {         //학번 불일치
            showAlert(AF_NOID, "입력한 학번이 존재하지 않습니다.\n다시 한번 확인해주세요."); //2021.03.24 수정 배포X
        } else if (errCode.equals(AF_NO_MATCH_PW)) {         //비밀번호 불일치
            showAlert(AF_NOID, "입력한 학번과 비밀번호가 일치하지 않습니다.\n다시 한번 확인해주세요."); //2021.03.24 수정 배포X
        } else if (errCode.equals(AF_STOP_USER)) {
            showAlert(AF_STOP_USER, "일시정지된 회원은 로그인할 수 없습니다.\n본원에 문의주세요!");
        } else if (errCode.equals(AF_NO_DATA)) {
            showAlert(AF_NO_DATA, "없는 학번입니다.\n회원가입을 해주세요.");
        } else {
            showAlert("", "예상치 못한 오류가 발생했습니다.\n본 원으로 오류메세지를 알려주세요!\n==오류 내용==\n" + errCode);
        }
    }

    private void show_notify() {
        final Notify notify = new Notify(A02_Login.this, R.layout.notify_dig);
        notify.callResultDialog();
        notify.setCsDialogListener(new Notify.CsDialogListener() {
            @Override
            public void onPositiveClicked(String s) {
                Log.i(TAG, "공지: " + s);

                if (s.equals("CANCEL")) { //'다시보지않기'선택 시 상태를 저장
                    SharedPreferences notify = getSharedPreferences("NOTIFY", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = notify.edit();

                    editor.putBoolean("CANCEL_CHECK", true);
                    editor.commit();
                }
                success_proc();
            }
        });
    }

    /**
     * 상황에 맞는 Alert 보여줌
     */
    private void showAlert(final String flag, String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(A02_Login.this);
        ab.setCancelable(false);
        ab.setMessage(msg);
        ab.setPositiveButton("확인", null);
        ab.show();
    }

    public void go_login(View view) {     //빠른 학습상담 페이지로 이동
        if (isEmpty()) {
            doLogin();

        }
    }

    public void go_A06_QuickAdvice(View view) {     //1:1문의 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A06_QuickAdvice.class);
        intent.putExtra("loginstate", 1);
        startActivity(intent);
    }

    public void go_A06_Telephone(View view) {       //전화상담 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A06_Telephone.class);
        intent.putExtra("loginstate", 1);
        startActivity(intent);
    }

    public void go_A03_JoinTerm(View view) {        //회원가입 약관동의 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A03_JoinTerms.class);
        startActivity(intent);
    }

    public void go_A04_FindID(View view) {          //학번 찾기 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A04_FindId.class);
        startActivity(intent);
    }

    public void go_A04_FindPW(View view) {          //비밀번호 찾기 페이지로 이동
        Intent intent = new Intent(getApplicationContext(), A04_FindPw.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), before_Main.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause()");

        if (loading != null) {
            loading.stop();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()");
    }

    /**
     * 학생정보를 조회하기 위한 AsyncTask
     */
    private class DoLoginNetTask extends AsyncTask<Void, Void, String> {
        private final String url;
        private final ContentValues values;

        public DoLoginNetTask(String url, ContentValues values) {
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

            if (loading != null) {
                loading.stop();
            }

            if (result.equals("FAIL")) {
                //네트워크 통신 오류
            } else {
                //로그인 학번 조회 결과를 처리한다.
                loginResult_proc(result);
            }
        }
    }

    /**
     * 로그인 기록을 남기기 위한 AsyncTask
     */
    private class LoginSuccessNetTask extends AsyncTask<Void, Void, String> {
        private final String url;
        private final ContentValues values;

        public LoginSuccessNetTask(String url, ContentValues values) {
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
                //네트워크 통신 오류
            } else {
                String[] arrResult = new String[0];

                if (result != "") {
                    arrResult = result.split(",");
                    Log.i(TAG, "arrResult =>" + arrResult[0] + ", arrResult[1] =>" + arrResult[1]);

                    if (result.equals("Err")) {
                        android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(A02_Login.this);
                        ab.setMessage("죄송합니다.\n로그인 중 예상치 않은 오류가 발생했습니다\n본 원으로 오류메세지를 알려주세요!\n==오류 내용==\n");
                        ab.setPositiveButton("확인", null);
                        ab.show();
                    } else {
                        loginNumber = arrResult[1];
                        SharedPreferences.Editor editor = savedID_PW.edit();

                        //로그인 시에 1 값을 저장하고 앱 재실행시에 2를 저장한다 -> 1일때 로그인상태로 인식 2일때 비로그인 상태로 인식
                        SharedPreferences login = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = login.edit();
                        editor1.putInt("aaa", 1);
                        editor1.apply();
                        editor.putString("UID", id);
                        editor.putBoolean("isNeedCerty", _isNeedCertyLogin);
                        editor.putBoolean("certyState", false);
                        editor.putString("userName", userName);

                        if (a02_chk_savedLoginInfo.isChecked()) {
                            Log.i(TAG, "학번: " + id);
                            Log.i(TAG, "비번: " + pw);
                            editor.putString("UPW", pw);  //비밀번호 저장
                            editor.putBoolean("SAVE_LOGIN_DATA", a02_chk_savedLoginInfo.isChecked());

                        } else {
                            a02_inputPASS.setText("");
                            editor.putString("UPW", "");
                            editor.remove("SAVE_LOGIN_DATA");
                        }
                        editor.commit();

                        moveActivity(MainActivity.class);
                    }
                } else {
                    Log.i(TAG, "전달 받은 값 없음");
                }
            }
        }
    }


}
