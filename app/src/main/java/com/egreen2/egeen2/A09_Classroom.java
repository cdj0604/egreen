package com.egreen2.egeen2;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.egreen2.egeen2.Adapter.A09_MyClassListAdapter;
import com.egreen2.egeen2.Data.A09_MyClassListData;
import com.egreen2.egeen2.Data.StudyInfo;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/*
    [파일명] A09_Classroom.java
    [설명] 과목명 강의실
    [작성자] 채동주


    [작성일시] 2021.07.20
 */

public class A09_Classroom extends AppCompatActivity implements View.OnClickListener,
        A09_MyClassListAdapter.MyClassListClickListener,
        NetworkAsyncTasker.AsyncResponse {

    private static final String TAG = A09_Classroom.class.getSimpleName();
    private static final String MAIN_LIST = "mainList";
    private static final String OVERLAP = "overlap";
    private static final String LOGOUT = "logout";
    /* 공인인증서 변수 */
    private final String appKey = "trqfA6ksqY7JpZhVJW0VzA==";     //테스트 라이센스 (배포전 정식 라이선스 발급 요청)
    private final String[] rtnParams = {"a", "b", "c"};
    ArrayList<A09_MyClassListData> data;
    RecyclerView myClassList;
    SwipeRefreshLayout swipeRefresh;
    ShowLoading loading;
    A09_MyClassListAdapter adapter;
    //AsyncTask 결과값을 받기위함
    NetworkAsyncTasker asyncTask;
    //학습에 필요한 정보
    StudyInfo si;
    String id;
    String userName;
    String loginNumber;
    SharedPreferences savedCertyState;
    boolean isEnable;
    String isThereHighSchoolName, isSurvey;
    //네트워크 체크
    NetworkStateCheck netCheck;
    //커스텀 다이얼로그
    Dialog dialog;
    TextView a;
    private DrawerLayout mDrawerLayout;

    // private final String appKey = "hToVAEsOMbTR80ZJqJrQIuGBBDCl7bEiw9fi+9qaka8=";     //2.0 버전의 app 라이선스
    // w7bZQyyZVmN82xdvTdD5P8L/qwPJm0ffOpRuqABiaXY= 기존 버전 앱의 라이선스
    private TextView result_text;

    //한국전자인증 공인인증센터 App 설치
    public static void searchMarket(Activity caller, String packagename) {
        Uri uri = Uri.parse("market://details?id=" + packagename);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        caller.startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onReStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a09_classroom);
        //  doLicense();
        /* Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.a09_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu); //메뉴 버튼 이미지 지정


        // result_text=(TextView)findViewById(R.id.result_text);
        TextView username = (TextView) findViewById(R.id.username);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        myClassList = findViewById(R.id.myClassList);
        ImageView imageView = findViewById(R.id.logo);


        //다이얼로그 선언은 메소드에서가 아닌 oncreate에서 해야 오류안뜸
        dialog = new Dialog(A09_Classroom.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.class_dig);

        // Button button = (Button)findViewById(R.id.button);

        /*앱 라이센스 정보를 얻기위한 코드 추후 삭제or주석처리
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLicense();
                result_text.setText("");
            }
        });

*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //네비게이션 헤더에 참조할때는 아래와같이 참조해야한다.
        TextView head_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.head_name);
        TextView head_studentid = (TextView) navigationView.getHeaderView(0).findViewById(R.id.head_StudentID);

        //저장된 유저이름과 학번을 가져와서 네비게이션 헤더에 출력
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", ""); //유저이름
        String StudentID = sharedPreferences.getString("UID", ""); //유저학번
        head_name.setText(userName + "님");
        head_studentid.setText(StudentID);
//        username.setText(userName + "님 강의실");
        si = (StudyInfo) getIntent().getSerializableExtra("studyInfo");
        id = StudentID;
        loginNumber = si.getLoginNumber();
        // Intent intentt = getIntent();
        // loginNumber = intentt.getStringExtra("studyinfo");


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                /**
                 * 드로어 레이아웃 메뉴
                 */
                if (id == R.id.home) {
                    moveActivity(MainActivity.class);
                } else if (id == R.id.noticeboard) {
                    moveActivity(A05_NoticeBoard.class);
                } else if (id == R.id.curriculum) {
                    moveActivity(A08_Curriculum.class);
                } else if (id == R.id.guide) {
                    moveActivity(A07_Guide.class);
                } else if (id == R.id.apply) {
                    moveActivity(A11_Apply.class);
                } else if (id == R.id.support) {
                    moveActivity(A06_Support.class);
                } else if (id == R.id.setting) {
                    moveActivity(A12_Setting.class);
                }

                return true;
            }
        });

        savedCertyState = getSharedPreferences("LOGIN_INFO", MODE_PRIVATE);


        //A7_StudyCenter 또는 A8_Learning 에서 전달한 학습정보를 저장


        try {
            Log.i(TAG, "받은값 =====> " + si.getLoginNumber());
            Log.i(TAG, "받은값 =====> " + si.getUserId());
            Log.i(TAG, "받은값 =====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "받은값tMessage(\"로그아웃 하시겠습니까?\");\n =====> 없음");
        }


        netCheck = new NetworkStateCheck(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netConnForGetMyClassListData();
            }
        });
        netConnForGetMyClassListData();

    }   //onCreate 종료

    /**
     * 과정정보를 가져오는 네트워크 통신
     */
    private void netConnForGetMyClassListData() {
        String url = "http://cb.egreen.co.kr/mobile_proc/mypage/new/main_m2.asp";
        ContentValues cValues = new ContentValues();
        cValues.put("userId", si.getUserId());

        Log.i(TAG, "전달하는 값1 ====> " + cValues);
        Log.d("확인", String.valueOf(cValues));

        //과목 가져오기
        loading = new ShowLoading(A09_Classroom.this, "과목정보를 가져오는 중입니다.");
        if (netCheck.isConnectionNet()) {
            loading.start();

            asyncTask = new NetworkAsyncTasker(this, url, cValues, MAIN_LIST);
            asyncTask.execute();
        } else {
            Toast.makeText(A09_Classroom.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 로그아웃을 위한 통신이다.
     */
    private void netConnForLogout() {
        String url = "http://cb.egreen.co.kr/mobile_proc/login/new/logout_proc_m2.asp";
        ContentValues cValues = new ContentValues();
        cValues.put("userId", si.getUserId());
        cValues.put("ulNum", si.getLoginNumber());

        Log.i(TAG, "전달하는 값2 ====> " + cValues);

        if (netCheck.isConnectionNet()) {
            asyncTask = new NetworkAsyncTasker(this, url, cValues, LOGOUT);
            asyncTask.execute();
            Intent intent = new Intent(getApplicationContext(), before_Main.class);
            startActivity(intent);

        } else {
            Toast.makeText(A09_Classroom.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 중복 로그인 체크
     */
    public String netConnForOverlapCheck() {
        String url = "http://cb.egreen.co.kr/mobile_proc/loginOverlap_check_m2.asp";
        ContentValues cValues = new ContentValues();
        cValues.put("userId", si.getUserId());
        cValues.put("ulNum", si.getLoginNumber());

        Log.i(TAG, "전달하는 값3 ====> " + cValues);

        loading = new ShowLoading(A09_Classroom.this, "잠시만 기다려주세요.");
        if (netCheck.isConnectionNet()) {
            loading.start();

            asyncTask = new NetworkAsyncTasker(this, url, cValues, OVERLAP);
            asyncTask.execute();
        } else {
            Toast.makeText(A09_Classroom.this, "네트워크 연결 실패", Toast.LENGTH_SHORT).show();
        }

        return "";
    }

    /**
     * 과목 리스트를 가져는 AsyncTask 결과 처리
     */
    @Override
    public void processFinish(String result, String what) {
        if (loading != null) {
            loading.stop();
        }

        if (result.equals("FAIL")) {
            //네트워크 통신 오류
        } else if (what.equals(MAIN_LIST)) {
            json_parsing(result);
        } else if (what.equals(OVERLAP)) {
            //과목명에 따른 을/를 처리
            Log.i(TAG, "종성" + KoreanUtil.getComleteWordByJongsung(si.getClassTitle(), "을", "를"));
            TextView textView = dialog.findViewById(R.id.a);
            textView.setText(KoreanUtil.getComleteWordByJongsung(si.getClassTitle(), "을", "를") + " 학습하시겠습니까?");
            // 아니오 버튼
            Button noBtn = dialog.findViewById(R.id.noBtn);
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 원하는 기능 구현
                    dialog.dismiss(); // 다이얼로그 닫기

                }
            });
            // 네 버튼
            dialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss(); // 다이얼로그 닫기
                 //   overlap_proc(result); //앱 출시 시에는 이코드
                    checkDong_Servey(); //공인인증서 무시하고 강의실 진입
                }
            });
            dialog.show();

        } else if (what.equals(LOGOUT)) {
            finish();
        }
    }

    /**
     * 터치 액션시, 중복 로그인 체크
     * 중복 로그인이 아니면 학습동의서, 사전설문, 나의 강의실로 이동
     */
    private void overlap_proc(String result) {
        if (result != "") {
            //중복 로그인
            String[] arrResult = result.split(",");

            AlertDialog.Builder ab = new AlertDialog.Builder(A09_Classroom.this);
            ab.setCancelable(false);
            ab.setMessage("중복 로그인이 감지되어 로그아웃 되었습니다.\n\n" +
                    "접 속 시 간 : " + arrResult[1] + "\n" +
                    "접 속 IP : " + arrResult[2] + "\n" +
                    "접 속 OS : " + arrResult[3] + "\n" +
                    "접속Browser : " + arrResult[4]);
            ab.setPositiveButton("알겠습니다", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), A02_Login.class);
                    startActivity(intent);

                }
            }).show();
        } else {
            Log.i(TAG, "공인인증서 유예자 인가?" + getIsNeedCerty());
            Log.i(TAG, "인증서 로그인 성공 상태를 유지 중인가?" + savedCertyState.getBoolean("certyState", false));

            if (isEnable) {
                if (getIsNeedCerty() == false && savedCertyState.getBoolean("certyState", false) == false) {
                    isLoginCerty();
                } else {
                    checkDong_Servey();
                }
            } else {
                //모바일 수강 불가능한 과목이면 Alert를 띄운다.
                AlertDialog.Builder ab = new AlertDialog.Builder(A09_Classroom.this);
                ab.setTitle("모바일 수강불가 과목");
                ab.setMessage("죄송합니다.\n[" + si.getClassTitle().trim() + "]은 모바일 강의를 지원하지 않습니다.\nPC를 이용해주세요.");
                ab.setPositiveButton("알겠습니다", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        netConnForGetMyClassListData();
                    }
                });
                ab.show();
            }
        }
    }

    /**
     * JSON parsing
     */
    private void json_parsing(String s) {
        A09_MyClassListData a9_myClassListData;
        ArrayList<A09_MyClassListData> myClassListData = new ArrayList<>();

        /*
         * JSON 데이터를 파싱한다
         * */
        try {
            JSONObject jsObj = new JSONObject(s);
//            JSONArray jsArray = new JSONArray(s);
            JSONArray jsArray = jsObj.getJSONArray("classInfo");

            for (int i = 0; i < jsArray.length(); i++) {
//            for (int i = 0; i < 8; i++) {
                jsObj = jsArray.getJSONObject(i);

                String classId = jsObj.getString("classId");
                String classTitle = jsObj.getString("classTitle");
                String currJucha = jsObj.getString("currJucha");
                String attendRatio = jsObj.getString("attendRatio");
                String isReadOrientation = jsObj.getString("isReadOrientation");
                String isEnable = jsObj.getString("isEnable");
                String isSurvey = jsObj.getString("isSurvey");
                String isThereHighSchoolName = jsObj.getString("isThereHighSchoolName");
                String isDongPost = jsObj.getString("isDongPost");
                String directoryName = jsObj.getString("strUrl");     //디렉토리 명
                String myGoal = jsObj.getString("myGoal");
                String myNote = jsObj.getString("myNote");
                String selfInt = jsObj.getString("selfIntroduce");
                String discussion = jsObj.getString("discussion");


                Boolean _isEnable;
                _isEnable = isEnable.equals("Y");

                a9_myClassListData = new A09_MyClassListData(_isEnable, classId, classTitle, currJucha, attendRatio,
                        isReadOrientation, isSurvey, isThereHighSchoolName, isDongPost, directoryName,
                        myGoal, myNote, selfInt, discussion);
                myClassListData.add(a9_myClassListData);

            }
        } catch (JSONException e) {
            Log.i(TAG, "notice JSON Exc: " + e.getMessage());
        }

        setListView(myClassListData);
    }

    /**
     * NetConnForgetMyClassList() 로 받아온 데이터를 그린다.
     */
    private void setListView(ArrayList<A09_MyClassListData> myClassListData) {
        /*
         * 각 Item 들이 RecyclerView 의 전체크기를 변경하지 않는다면,
         * setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있다.
         * 전체크기가 변경될 가능성이 있다면 false, 아니면 true
         * */
        LinearLayoutManager listViewLayout = new LinearLayoutManager(A09_Classroom.this);
        listViewLayout.setOrientation(LinearLayoutManager.VERTICAL);
        A09_MyClassListAdapter myClassListAdapter = new A09_MyClassListAdapter(A09_Classroom.this, myClassListData);

        myClassList.setHasFixedSize(true);
        myClassList.setLayoutManager(listViewLayout);
        myClassListAdapter.setOnClickListener(A09_Classroom.this);

        myClassList.setAdapter(myClassListAdapter);
        myClassListAdapter.notifyDataSetChanged();


        if (loading != null) {
            loading.stop();
        }

        swipeRefresh.setRefreshing(false);      //해주지 않으면 리스레쉬 아이콘이 사라지지 않는다.
        Toast.makeText(this, "과목불러오기 완료", Toast.LENGTH_SHORT).show();
    }

    /**
     * A6_MyClassListAdapter -> A7_StudyCenter
     */
    @Override
    public void onItemClicked(boolean isEnable, String classId, String classTitle, String strUrl, String isReadOrientation,
                              String isSurvey, String isThereHighSchoolName, String isDongPost, String myGoal, String myNote,
                              String studyDate, String selfIntro, String discussion) {

        //체크순서
        //1. 모바일 수강 가능한지.
        //2. 출석 인증 기간인지.
        //3. 학습동의서를 완료 했는지.
        //4. 설문조사는 완료 했는지.
        si.setClassId(classId);
        si.setStudyDate(studyDate);
        si.setClassTitle(classTitle);
        si.setDirectoryName(strUrl);

        si.setDongPost(isDongPost);
        si.setMyGoal(myGoal);
        si.setMyNote(myNote);
        si.setSelfIntro(selfIntro);
        si.setOrientation(isReadOrientation);
        si.setDiscussion(discussion);

        this.isEnable = isEnable;
        this.isThereHighSchoolName = isThereHighSchoolName;
        this.isSurvey = isSurvey;

        //여기서 중복로그인 체크!
        //중복 로그인 체크

        netConnForOverlapCheck();
    }

    /**
     * 학습동의서 및 사전설문조사 확인, 화면 이동
     */
    private void checkDong_Servey() {
        Intent intent;
        if (si.getStudyDate().equals("0")) {
            //showDoNotStudyDate(si.getStudyDate());
            //테스트
            goClassRoom();
            //테스트
        } else if (si.getDongPost().equals("")) {
            //학습동의서 미완료시,
            //학습동의서 StudyAgreement Activity 로 이동한다.
            intent = new Intent(this, StudyAgreement.class);
            intent.putExtra("CLASS_ID", si.getClassId());
            intent.putExtra("IS_THERE_HIGHSCHOOL_NAME", isThereHighSchoolName);
            intent.putExtra("studyInfo", si);
            startActivity(intent);
            finish();
        } else if (isSurvey.equals("False")) {
            //설문조사 미완료시,
            //설문조사 Survey Activity 로 이동한다.
            intent = new Intent(this, Survey.class);
            intent.putExtra("CLASS_ID", si.getClassId());
            intent.putExtra("studyInfo", si);
            startActivity(intent);
            finish();
        } else {
            //학습동의서와 설문조사 완료시,
            //과목 목차 리스트 Activity로 이동한다.
            SharedPreferences.Editor editor = savedCertyState.edit();
            editor.putBoolean("certyState", true);
            editor.commit();

            goClassRoom();
        }
    }

    /**
     * A10_ClassRoom 으로 이동
     */
    private void goClassRoom() {
        Intent intent = new Intent(this, A10_ClassRoom.class);
        intent.putExtra("studyInfo", si);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
       // finish();
    }

    /**
     * 학습이 시작되기 전이거나 종료했을때, 알럿을 띄워 보여준다.
     */
    private void showDoNotStudyDate(String _studyDate) {
        try {
            if (_studyDate.equals("0")) {
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setMessage("출석인증 기간이 아닙니다.");
                ab.setPositiveButton("확인", null);
                ab.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    }

    ////////////////////// ----------------------- 공인인증서 -------------------------------///////////////////
    /*
     * 라이선스 등록을 위해 꼭 실행하여야 함.
     */
    private void doLicense() {
        //라이선스 정보 추출
        Intent intent = new Intent();
        intent.setData(Uri.parse("crosscert://licenseinfo"));
        intent.putExtra("requestCode", 2);
        startActivityForResult(intent, 2);

    }

    //공인인증서 로그인 유무 확인
    protected void isLoginCerty() {
        if (isCertyApp() == false) {
            android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(this);
            ab.setCancelable(false);

            ab.setTitle("공인인증서 로그인")
                    .setMessage("범용공인인증서 로그인을 위해서는 한국전자인증 앱이 필요합니다.\n설치 화면으로 이동 하시겠어요?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            searchMarket(A09_Classroom.this, "com.crosscert.android");
                        }
                    })
                    .setNegativeButton("아니오", null)
                    .show();
        } else {

            // 전자인증서
            // doEsign("abcd", 1, null);
            String inputData = "abcd";
            String inputbase64 = null;
            try {
                inputbase64 = Base64Coder.encodeString(inputData);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            // 전자서명
            doEsign(inputbase64, 1, null);

        }
    }

    //인증서 설치유무 확인
    protected boolean isCertyApp() {
        //한국전자인증 공인인증센터 App 설치 유무 확인
        PackageManager pm = getPackageManager();
        Boolean isCrosscertInstalled = false;

        //flags Additional option flags.  Currently should always be 0.
        List<ApplicationInfo> appList = pm.getInstalledApplications(0);
        Iterator<ApplicationInfo> i = appList.iterator();

        while (i.hasNext()) {
            ApplicationInfo app = i.next();
            if (!(app.packageName.equals("com.crosscert.android"))) {
                isCrosscertInstalled = false;
            } else {
                isCrosscertInstalled = true;
                break;
            }
        }

        return isCrosscertInstalled;
    }

    /**
     * 액티비티 결과 : 하위 액티비티가 리턴하는 결과 처리하기
     * 하위 액티비티가 종료되면, 해당 액티비티를 시작시킨
     * 호출 액티비티의 onActivityResult 이벤트 핸들러가 호출된다.
     * 하위 액티비티가 리턴하는 결과를 처리하려면 이 메서드를 재정의 해야한다.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.i(TAG, "requestCode : " + requestCode);
//        Log.i(TAG, "resultCode : " + resultCode);

        if (resultCode == RESULT_OK) {
            Log.i(TAG, "어디서 돌아오셨나요? " + requestCode);


            int resultState = 1;
            if (requestCode == 2) {
                resultState = data.getIntExtra("resultstate", resultState);
                String resultData = data.getStringExtra("result");
                Log.i(TAG, "LicenseInfo::" + resultData);
                if (resultState == 0) {
                    Log.i(TAG, "LicenseInfo::" + resultData);
                } else {
                    Log.i(TAG, "error: " + "(" + resultData + ")");
                    Log.i(TAG, "LicenseInfo::" + resultData);
                }
            } else if (requestCode == 8) {    //전자서명
                resultState = data.getIntExtra("resultstate", resultState);
                String resultData = data.getStringExtra("result");
                String[] rtnParamsData = data.getStringArrayExtra("rtnParam");
                Log.i(TAG, "LicenseInfo::" + resultData);
//                Log.v("clientApp", "resultState : " + resultState);

                String result = "";
                if (resultState == 0) {
                    if (resultData != null) {
                        String sidCheck = data.getStringExtra("sidCheck");
                        String sidResult = data.getStringExtra("sidResult");
                        if (sidCheck != null && sidResult != null) {
//                            resultData = resultData + "\nsidcheck : " + sidCheck;
//                            resultData = resultData + "\nsidResult : " + sidResult;
                        }
                        String certiResultData = resultData;
//                        alertDialog("result", resultData, this);
//                        Log  .i(TAG, "공인인증서 서명 :" + tResultData);
                        Log.i(TAG, "LicenseInfo::" + resultData);
                        // 공인인증서 로그인, cb2 로 고정
                        String url = "http://cb2.egreen.co.kr/mobile_proc/login/new/new_verify_m.asp";

                        ContentValues cValue = new ContentValues();
                        cValue.put("cuserid", si.getUserId());
                        cValue.put("uMobileBrand", Build.BRAND + "::" + Build.MODEL);
                        cValue.put("result", certiResultData);

                        VerifyNetTask verifyNT = new VerifyNetTask(url, cValue);
                        verifyNT.execute();
                    }
                } else {
                    Log.i(TAG, "error: " + "(" + resultData + ")");
                    Log.i(TAG, "LicenseInfo::" + resultData);
                }
            }
        }
    }

    private void loginCertyResult_proc(String result) {
        android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(A09_Classroom.this);
        ab.setCancelable(false);

        if (result.equals("OK")) {
            checkDong_Servey();
        } else {
            if (result.equals("FAIL")) {
                //DB에 저장된 인증서와 다름
                ab.setMessage("e그린원격평생교육원 홈페이지에서 사용하는 인증서와 동일한 인증서를 사용해주세요.");
                ab.setPositiveButton("확인", null);
                ab.show();
            } else if (result.equals("Err")) {
                ab.setMessage("공인인증서 조회에 실패했습니다\n본 원으로 연락주세요!");
                ab.setPositiveButton("확인", null);
                ab.show();
            } else if (result.equals("1358954498")) {
                ab.setMessage("범용 공인인증서가 아닙니다.\n인증서를 다시한번 확인해주세요.");
                ab.setPositiveButton("확인", null);
                ab.show();
            } else if (result.equals("1342177281")) {
                ab.setMessage("본인 확인에 실패했습니다.\n인증서를 다시한번 확인해주세요.");
                ab.setPositiveButton("확인", null);
                ab.show();
            } else if (result.equals("23068686")) {
                ab.setMessage("전자서명 형식이 유효하지 않습니다.\n인증서를 다시한번 확인해주세요.");
                ab.setPositiveButton("확인", null);
                ab.show();
            } else {
                //위 사항 모두 해당 없을때
                ab.setMessage("예상치 못한 오류가 발생했습니다.\n오류내용을 본 교육원에 알려주세요\n오류내용 : " + result);
                ab.setPositiveButton("확인", null);
                ab.show();
            }
        }
    }

    /**
     * 전자서명 (PKCS#7 SignedData)
     *
     * @param inputbase64 base64 인코딩된 평문 데이터
     */
    private void doEsign(String inputbase64, int sidOption, String sidValue) {
        // 앱에서 한국전자인증의 공인인증센터 전자서명 모듈 링크 걸기
        Intent intent = new Intent();

        intent.setData(Uri.parse("crosscert://esign"));
        intent.putExtra("requestCode", 8);
        intent.putExtra("data", inputbase64); // base64 인코딩된 평문 데이터
        intent.putExtra("appkey", appKey);  //App 서비스 라이선스
        intent.putExtra("rtnParam", rtnParams);

        // verify sid
        if (sidOption == 1) {
            intent.putExtra("sidOption", sidOption);
        } else if (sidOption == 2) {
            intent.putExtra("sidOption", sidOption);
            intent.putExtra("sidValue", sidValue);
        }

        startActivityForResult(intent, 8);
    }


    /**
     * 로그아웃 Alert을 띄운다.
     */
    public void alertLogout() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("로그아웃 하시겠어요?");
        ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                netConnForLogout();
            }
        });
        ab.setNegativeButton("아니오", null);
        ab.show();
    }
    ////////////////////// ----------------------- 공인인증서 -------------------------------///////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 회원 LoginNumber를 가져온다.
     */
    private boolean getIsNeedCerty() {
        /*
         * 저장되어 있는 회원 ID를 가져온다.
         */
        boolean isNeedCerty = false;
        try {
            isNeedCerty = savedCertyState.getBoolean("isNeedCerty", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isNeedCerty;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (loading != null) {
            loading.stop();
        }

        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy()");
    }

    public void go_logout(View view) {
        //다이얼로그 예 눌렀을때 인텐트실행
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("로그아웃 하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                netConnForLogout();
                //  Intent intent = new Intent(getApplicationContext(), before_Main.class);
                //  startActivity(intent);

            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    /**
     * Activity 를 이동한다.
     */
    private void moveActivity(Class activity) {
        StudyInfo si = new StudyInfo();
        si.setUserId(id);
        si.setUserName(userName);
        si.setLoginNumber(loginNumber);
        Intent intent = new Intent(this, activity);
        intent.putExtra("studyInfo", si);
        startActivity(intent);

        try {
            Log.i(TAG, "A09_Classroom 에서 전달하는 =====> " + si.getUserId());
            Log.i(TAG, "A09_Classroom 에서 전달하는 =====> " + si.getLoginNumber());
            Log.i(TAG, "A09_Classroom 에서 전달하는 =====> " + si.getUserName());
        } catch (Exception e) {
            Log.i(TAG, "A09_Classroom 으로 전달하는 =====> 없음");
        }
//        finish();

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 마지막 글자 유니코드로 추출 후 을/를 나타내기
     */
    public static class KoreanUtil {
        public static final String getComleteWordByJongsung(String name, String firstValue, String secondValue) {
            char lastName = name.charAt(name.length() - 1);

            // 한글의 제일 처음과 끝의 범위밖일 경우는 오류
            if (lastName < 0xAC00 || lastName > 0xD7A3) {
                return name;
            }
            String seletedValue = (lastName - 0xAC00) % 28 > 0 ? firstValue : secondValue;
            return name + seletedValue;
        }
    }

    /**
     * 공인인증서 로그인 결과처리 AsyncTask
     */
    private class VerifyNetTask extends AsyncTask<Void, Void, String> {
        private final String url;
        private final ContentValues values;

        public VerifyNetTask(String url, ContentValues values) {
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
//                    Log.i(TAG, "arrResult =>" + arrResult[0] + ", arrResult[1] =>" + arrResult[1]);
//                        loginNumber = arrResult[1];


                } else {
                    Log.i(TAG, "전달 받은 값 없음");
                }
                Log.i(TAG, "인증서 결과 : " + result);
                loginCertyResult_proc(result);
            }
        }


    }

}
