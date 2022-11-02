package com.egreen2.egeen2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/*
    [파일명] A03_JoinTerms.java
    [설명] 약관동의 페이지
    [작성자] 장희원
    [작성일시] 2021.01.12
 */

public class A03_JoinTerms extends AppCompatActivity {

    private static final String TAG = A03_JoinTerms.class.getSimpleName();
    // Data를 관리해주는 Adapter
    private final A03_JoinTerms.CustomAdapter mCustomAdapter = null;
    // 제네릭(String)을 사용한 ArrayList
    private final ArrayList<String> mArrayList = new ArrayList<String>();
    private final Context context = this;
    // ListView 안에 Item을 클릭시에 호출되는 Listener
    private final AdapterView.OnItemClickListener mItemClickListener = new
            AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {

                    mCustomAdapter.setChecked(position);
                    // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                    mCustomAdapter.notifyDataSetChanged();

                }
            };
    private final ListView mListView = null;
    private final CheckBox mAllCheckBox = null;
    /* 변수 모음 */
    Button next_btn;
    CheckBox chk_all, check_box, checkBox1, checkBox2, checkBox3, all_check; //약관전체동의 체크
    ShowLoading loading;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a03_join_term);

        /* View 연결 */
        next_btn = findViewById(R.id.a03_next_btn);
        check_box = findViewById(R.id.join_check_box);
        all_check = findViewById(R.id.all_check);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);

        ImageView imageView = findViewById(R.id.logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), before_Main.class);
                startActivity(intent);
            }
        });

        //약관 모두 동의하
        all_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);

                if (all_check.isChecked()) {
                    checkBox1.setChecked(true);
                    checkBox2.setChecked(true);
                    checkBox3.setChecked(true);
                }

            }
        });

        //약관 모두 동의시에만 넘어가기
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), A03_Join.class);
                    startActivity(intent);
                } else
                    showAlert();
            }
        });

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.a03_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.back); //메뉴 버튼 이미지 지정

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                /* 드로어 레이아웃 메뉴 */
                if (id == R.id.home) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.noticeboard) {
                    Intent intent = new Intent(getApplicationContext(), A05_NoticeBoard.class);
                    startActivity(intent);
                } else if (id == R.id.curriculum) {
                    Intent intent = new Intent(getApplicationContext(), A08_Curriculum.class);
                    startActivity(intent);
                } else if (id == R.id.guide) {
                    Intent intent = new Intent(getApplicationContext(), A07_Guide.class);
                    startActivity(intent);
                } else if (id == R.id.classroom) {
                    Intent intent = new Intent(getApplicationContext(), A09_Classroom.class);
                    startActivity(intent);
                } else if (id == R.id.apply) {
                    Intent intent = new Intent(getApplicationContext(), A11_Apply.class);
                    startActivity(intent);
                } else if (id == R.id.support) {
                    Intent intent = new Intent(getApplicationContext(), A06_Support.class);
                    startActivity(intent);
                }

                return true;
            }
        });


    }   //onCrate 종료

    /* 버튼 클릭 */
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

    }         //로그인 페이지로 이동

    public void go_A03_ClickWrap(View view) {
        Intent intent = new Intent(getApplicationContext(), A03_ClickWrap.class);
        startActivity(intent);
    }       //회원 약관동의 페이지로 이동

    /*
     * Layout
     */

    public void go_A03_PersonalInformation(View view) {
        Intent intent = new Intent(getApplicationContext(), A03_PersonalInformation.class);
        startActivity(intent);
    }       //개인정보 수집 및 이용동의 페이지로 이동

    public void go_A03_Trust(View view) {
        Intent intent = new Intent(getApplicationContext(), A03_Trust.class);
        startActivity(intent);
    }       //개인정보와 공유 및 취급 위탁에 대한 동의 페이지로 이동

    public void go_A03_Join(View view) {
        Intent intent = new Intent(getApplicationContext(), A03_Join.class);
        startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (loading != null) {
            loading.stop();
        }

    }

    private void showAlert() {
        AlertDialog.Builder ab = new androidx.appcompat.app.AlertDialog.Builder(this);
        ab.setMessage("약관에 모두 동의해주셔야 회원가입이 가능합니다.");
        ab.setPositiveButton("확인", null);
        ab.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Custom Adapter
    class CustomAdapter extends BaseAdapter {

        private final boolean[] isCheckedConfrim;
        private A03_JoinTerms.ViewHolder viewHolder = null;
        // 뷰를 새로 만들기 위한 Inflater
        private LayoutInflater inflater = null;
        private ArrayList<String> sArrayList = new ArrayList<String>();

        public CustomAdapter(Context c, ArrayList<String> mList) {
            inflater = LayoutInflater.from(c);
            this.sArrayList = mList;
            // ArrayList Size 만큼의 boolean 배열을 만든다.
            // CheckBox의 true/false를 구별 하기 위해
            this.isCheckedConfrim = new boolean[sArrayList.size()];
        }


        // CheckBox를 모두 선택하는 메서드
        public void setAllChecked(boolean ischecked) {
            int tempSize = isCheckedConfrim.length;
            for (int a = 0; a < tempSize; a++) {
                isCheckedConfrim[a] = ischecked;
            }

        }

        public ArrayList<Integer> getChecked() {
            int tempSize = isCheckedConfrim.length;
            ArrayList<Integer> mArrayList = new ArrayList<Integer>();
            for (int b = 0; b < tempSize; b++) {
                if (isCheckedConfrim[b]) {
                    mArrayList.add(b);
                }
            }
            return mArrayList;
        }

        public void setChecked(int position) {
            isCheckedConfrim[position] = !isCheckedConfrim[position];
        }

        @Override
        public int getCount() {
            return sArrayList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // ConvertView가 null 일 경우
            View v = convertView;

            if (v == null) {
                viewHolder = new A03_JoinTerms.ViewHolder();
                // View를 inflater 시켜준다.
                v = inflater.inflate(R.layout.row, null);
                viewHolder.cBox = v.findViewById(R.id.join_check_box);
                v.setTag(viewHolder);
            } else {
                viewHolder = (A03_JoinTerms.ViewHolder) v.getTag();
            }

            // CheckBox는 기본적으로 이벤트를 가지고 있기 때문에 ListView의 아이템
            // 클릭리즈너를 사용하기 위해서는 CheckBox의 이벤트를 없애 주어야 한다.
            viewHolder.cBox.setClickable(false);
            viewHolder.cBox.setFocusable(false);

            viewHolder.cBox.setText(sArrayList.get(position));
            // isCheckedConfrim 배열은 초기화시 모두 false로 초기화 되기때문에
            // 기본적으로 false로 초기화 시킬 수 있다.
            viewHolder.cBox.setChecked(isCheckedConfrim[position]);

            return v;
        }

    }

    class ViewHolder {
        // 새로운 Row에 들어갈 CheckBox
        private CheckBox cBox = null;
    }
}