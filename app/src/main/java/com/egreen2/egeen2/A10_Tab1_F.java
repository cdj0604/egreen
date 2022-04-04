package com.egreen2.egeen2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egreen2.egeen2.Adapter.A10_JuchaListAdapter;
import com.egreen2.egeen2.Data.A10_JuchaData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link A10_Tab1_F#newInstance} factory method to
 * create an instance of this fragment.
 */
public class A10_Tab1_F extends Fragment implements NetworkAsyncTasker.AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = A10_Tab1_F.class.getSimpleName();
    RecyclerView nowJuchaList;
    ShowLoading sl;
    String id, classId;
    ShowLoading loading;
    int minJucha, maxJucha;
    //AsyncTask 결과값을 받기위한 변수
    NetworkAsyncTasker asyncTask;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public A10_Tab1_F() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment A7_Tab1_F.
     */
    // TODO: Rename and change types and number of parameters
    public static A10_Tab1_F newInstance(String param1, String param2) {
        A10_Tab1_F fragment = new A10_Tab1_F();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (!checkDataNetwork()) {
            Log.d(TAG, "데이터 연결");
        } else
            Log.d(TAG, "WIFI 연결");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.a7_tab1_f, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nowJuchaList = view.findViewById(R.id.nowJuchaList);
        sl = new ShowLoading(getActivity(), "강의목록을 가져오고 있습니다.");

        id = getArguments().getString("UID");
        classId = getArguments().getString("CLASS_ID");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("re", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Log.i(TAG, id + "," + classId);
        SharedPreferences pref = getContext().getSharedPreferences("re", Context.MODE_PRIVATE);
        int re_cancel = pref.getInt("다시보지않기", 0);

        //네트워크 연결시에만 알림을 주려면 if주석 해제후 다이얼로그 생 if(!checkDataNetwork())

        if (re_cancel == 1) {
            netConnForGetListData();
        } else if (re_cancel == 2) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
            dlg.setMessage("3G/4G/5G로 수강 시 과도한 데이터 요금이 부과될 수 있습니다."); // 메시지
            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    editor.putInt("다시보지않기", 2);
                    editor.commit();
                    netConnForGetListData();
                }
            });
            dlg.setNegativeButton("다시 보지 않기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putInt("다시보지않기", 1);
                    editor.commit();
                    netConnForGetListData();
                }
            });
            dlg.show();


        } else {
            netConnForGetListData();
        }

    }

    /**
     * 현재 연결이 Data인지 확인한다.
     * Data이면 true 리턴
     */
    private boolean checkDataNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return info.isAvailable();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void netConnForGetListData() {
        sl.start();

        minJucha = getArguments().getInt("MIN_JUCHA");
        maxJucha = getArguments().getInt("MAX_JUCHA");
        int choised_jucha = getArguments().getInt("CHOISE_JUCHA");
        Log.i(TAG, "choised jucha => " + choised_jucha + ", minJucha => " + minJucha);

        String url = "http://cb.egreen.co.kr/mobile_proc/mypage/new/getCosSubjectList_Inc_m4.asp";
        ContentValues cValues = new ContentValues();
        cValues.put("userId", id);
        cValues.put("classId", classId);

        if (choised_jucha == minJucha || choised_jucha == maxJucha || choised_jucha == 0) {
            cValues.put("isAllList", "N");      //하단 '학습목차'를 터치했을때를 위함
        } else {
            cValues.put("isAllList", choised_jucha);
        }

        //목차 데이터를 가져온다.
//        GetCurrClassListNetTask getCurrClassListNT = new GetCurrClassListNetTask(url, cValues);
//        getCurrClassListNT.execute();

        asyncTask = new NetworkAsyncTasker(this, url, cValues);
        asyncTask.execute();
    }

    /**
     * 현재 출석체크 가능한 학습 목차를 가져오기 위한 AsyncTask
     */
    @Override
    public void processFinish(String result, String what) {
        if (loading != null) {
            loading.stop();
        }

        if (result.equals("FAIL")) {
            //네트워크 통신 오류
        } else {
            if (result.equals("0")) {
                Log.i(TAG, result + " is empty");
            } else {
                Log.i(TAG, "choised result => " + result);
                json_parsing(result);
            }
        }
    }

    /**
     * JSON parsing
     */
    private void json_parsing(String s) {
        A10_JuchaData juchaData;

        ArrayList<A10_JuchaData> arrCurrJuchaData = new ArrayList<>();

        /*
         * JSON 데이터를 파싱한다
         * */
        try {
            JSONObject jsObj = new JSONObject(s);
//            JSONArray jsArray = new JSONArray(s);
            JSONArray jsArray = jsObj.getJSONArray("juChaInfo");
            Log.i(TAG, "json Array: " + jsArray);


            boolean removeChasiDP = false;
            int nDay = 0;

            int jsonCount = jsObj.getInt("count");

            Log.i(TAG, jsArray.length() + "::jsArray 크기");
            for (int i = 0; i < jsArray.length(); i++) {
                jsObj = jsArray.getJSONObject(i);

                String cid = jsObj.getString("cid");
                String StrName = jsObj.getString("StrName");
                String strFileName = jsObj.getString("strFileName");
                nDay = jsObj.getInt("nDay");
                int nHour = jsObj.getInt("nHour");

                String nIs_View_Hour_Cid = jsObj.getString("nIs_View_Hour_Cid");
                String study_dt = jsObj.getString("study_dt");
                String watchedTime = jsObj.getString("nViewTime");
                String fullTime = jsObj.getString("lala_time");

                String readPage = jsObj.getString("cnt1");
                String fullPage = jsObj.getString("cnt");
                String totalRatio = jsObj.getString("totalRatio");

                //학습을 하지 않았다면, nll임 그래서 0으로 변환
                if (watchedTime.equals("null")) {
                    watchedTime = "0";
                }

                //페이지 열람을 하지 않았다면, null임 그래서 0으로 변환
                if (readPage.equals("null")) {
                    readPage = "0";
                }

                //데이터셋이 한 주차에 1, 2차시 데이터가 모두 들어가야 한다.
                //8 주차{1차시데이터}, 8주차 {2차시데이터}(X) / 8주차 {1차시데이터, 2차시데이터}
                //JSON 으로 온 데이터가 전자다.
                //후자 방식으로 다시 바꿔줘야 한다.
                if (jsonCount == 2) {
                    if (i == 1) {
                        //1개 주차일때 1번째 데이터의 차시 부분을 삭제한다.
                        removeChasiDP = true;
                    }
                } else if (jsonCount == 4) {
                    //2개 주차(4개 차시)일때 2로 나누어 떨어지는 n번째 차시 부분을 삭제한다.
                    removeChasiDP = i % 2 != 0;
                } else if (jsonCount == 6) {
                    //3개 주차(6개 차시)일때 3으로 나누어 떨어지는 n번째 차시 부분을 삭제한다.
                    removeChasiDP = i % 3 != 0;
                }

                if (nDay == 7 || nDay == 15) {
                    removeChasiDP = false;
                }

                boolean enable;
                if (nDay > maxJucha) {
                    enable = false;
                    Log.i(TAG, "maxjucha: " + maxJucha);
                } else if (nDay < minJucha) {
                    enable = false;
                    Log.i(TAG, "minjucha: " + minJucha);
                } else {
                    enable = true;
                }

                juchaData = new A10_JuchaData(cid, nIs_View_Hour_Cid, strFileName, StrName,
                        study_dt, nDay, nHour, watchedTime, fullTime, readPage,
                        fullPage, totalRatio, removeChasiDP, enable);

                arrCurrJuchaData.add(juchaData);


                Bundle args = new Bundle();
                args.putString("readPage", readPage);
                args.putString("fullPage", fullPage);


                Log.i(TAG, "열어본 페이지: " + readPage);
                Log.i(TAG, "콘텐츠 페이지: " + fullPage);
            }

            setListView(arrCurrJuchaData);
        } catch (JSONException e) {
            Log.i(TAG, "notice JSON Exc: " + e.getMessage());
        }
    }

    /**
     * 과목 목차를 그린다.
     */
    private void setListView(ArrayList<A10_JuchaData> arrCurrJuchaData) {
        LinearLayoutManager LinearLayout = new LinearLayoutManager(getActivity());
        A10_JuchaListAdapter juchaListAdapter = new A10_JuchaListAdapter(getActivity(), arrCurrJuchaData);

        nowJuchaList.setHasFixedSize(true);
        nowJuchaList.setLayoutManager(LinearLayout);
        nowJuchaList.setAdapter(juchaListAdapter);

        juchaListAdapter.setOnClickListener();
        juchaListAdapter.notifyDataSetChanged();

        if (sl != null) {
            sl.stop();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String s) {
        if (mListener != null) {
            mListener.onFragmentInteraction(s);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String s);
    }
}
