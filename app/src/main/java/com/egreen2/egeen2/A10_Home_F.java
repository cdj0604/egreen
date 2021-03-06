package com.egreen2.egeen2;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.egreen2.egeen2.Adapter.A10_ClassNotiAdapter;
import com.egreen2.egeen2.Adapter.A10_HomeAttendAdapter;
import com.egreen2.egeen2.Data.A10_ClassNotiData;
import com.egreen2.egeen2.Data.AttendStateData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentHomeListener} interface
 * to handle interaction events.
 * Use the {@link A10_Home_F#newInstance} factory method to
 * create an instance of this fragment.
 */
public class A10_Home_F extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = A10_Home_F.class.getSimpleName();

    private static final int MYGOAL = 2;
    private static final int MYNOTE = 3;
    private static final int SELF = 4;
    private static final int AGREE = 5;
    RecyclerView notiList, attendList;
    String id, classId;
    ArrayList<A10_ClassNotiData> arrClassNoti;
    boolean isComplete = false;
    ShowLoading loading;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentHomeListener mListener;

    public A10_Home_F() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment A7_Home_F.
     */
    // TODO: Rename and change types and number of parameters
    public static A10_Home_F newInstance(String param1, String param2) {
        A10_Home_F fragment = new A10_Home_F();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.a7_home_f, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        id = getArguments().getString("UID");
        classId = getArguments().getString("CLASS_ID");

        notiList = view.findViewById(R.id.notiList);
        attendList = view.findViewById(R.id.attendList);

        TextView orientation = view.findViewById(R.id.orientation);

        RelativeLayout studyAgreeArea = view.findViewById(R.id.studyAgreeArea);
        TextView studyAgreeRegdate = view.findViewById(R.id.studyAgreeRegDate);
        TextView studyAgreeComplete = view.findViewById(R.id.studyAgreeComplete);

        RelativeLayout myGoalArea = view.findViewById(R.id.myGoalArea);
        TextView myGoalRegdate = view.findViewById(R.id.myGoalRegDate);
        TextView myGoalComplete = view.findViewById(R.id.myGoalComplete);

        RelativeLayout myNoteArea = view.findViewById(R.id.myNoteArea);
        TextView myNoteRegdate = view.findViewById(R.id.myNoteRegDate);
        TextView myNoteComplete = view.findViewById(R.id.myNoteComplete);

        RelativeLayout selfIntroduceArea = view.findViewById(R.id.selfIntroduceArea);
        TextView selfIntroduceRegdate = view.findViewById(R.id.selfIntroduceRegDate);
        TextView selfIntroduceComplete = view.findViewById(R.id.selfIntroduceComplete);

        RelativeLayout discussionArea = view.findViewById(R.id.discussionArea);
        TextView discussionRegdate = view.findViewById(R.id.discussionRegDate);
        TextView discussionComplete = view.findViewById(R.id.discussionComplete);

        loading = new ShowLoading(getActivity(), "????????? ??????????????????");

        Log.i(TAG, id + "," + classId);

        notiJson_parsing();     //????????? ????????????
        listJson_parsing();     //????????? ?????????


        String _orientation = getArguments().getString("ORIENTATION");
        String _studyAgree = getArguments().getString("STUDY_AGREE");
        String _myGoal = getArguments().getString("MY_GOAL");
        int _myNote = getArguments().getInt("MY_NOTE");
        String _selfIntroduce = getArguments().getString("SELF_INT");
        String _discussion = getArguments().getString("DISCUSSION");
        Log.i(TAG, "/?????? ==>> " + _discussion +
                "/??????????????? ==>> " + _studyAgree +
                "/???????????? ==>> " + _myGoal +
                "/????????? ?????? ==>> " + _myNote +
                "/??????????????? ==>> " + _selfIntroduce +
                "/?????????????????? ===>> " + _orientation);

        /*
            ?????????????????? ?????? ??????
         */
        orientation.setTypeface(null, Typeface.NORMAL);
        if (_orientation.equals("False")) {
            orientation.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            orientation.setBackgroundResource(R.drawable.border_gray);
            orientation.setText("?????????????????? [?????????]");
        } else {
            orientation.setTypeface(null, Typeface.BOLD);
            orientation.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            orientation.setBackgroundResource(R.drawable.border_black);
            orientation.setText("?????????????????? [????????????]");
        }

        /*
            ??????????????? ?????? ??????
         */
        studyAgreeComplete.setTypeface(null, Typeface.NORMAL);
        if (_studyAgree.equals("")) {
            studyAgreeComplete.setText("?????????");
            studyAgreeComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            studyAgreeRegdate.setVisibility(View.INVISIBLE);
        } else {
            studyAgreeComplete.setText("??????");
            studyAgreeComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            studyAgreeComplete.setTypeface(null, Typeface.BOLD);
            studyAgreeRegdate.setText(_studyAgree);
            studyAgreeRegdate.setVisibility(View.VISIBLE);
        }

        /*
            ???????????? ?????? ??????
         */
        myGoalComplete.setTypeface(null, Typeface.NORMAL);
        if (_myGoal.equals("")) {
            myGoalComplete.setText("?????????");
            myGoalComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            myGoalRegdate.setVisibility(View.INVISIBLE);
        } else {
            myGoalComplete.setText("??????");
            myGoalComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            myGoalComplete.setTypeface(null, Typeface.BOLD);
            myGoalRegdate.setText(_myGoal);
            myGoalRegdate.setVisibility(View.VISIBLE);
        }

        /*
            ????????? ????????? 4?????? ?????? ????????? ??????????????? '??????' ??? ????????????.
            ?????? ????????? ??????
         */
        myNoteComplete.setTypeface(null, Typeface.NORMAL);
        myNoteRegdate.setText(_myNote + "/4");
        if (_myNote > 0 && _myNote < 4) {
            myNoteComplete.setText("?????????");
            myNoteComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.red));

        } else if (_myNote == 4) {
            myNoteComplete.setText("??????");
            myNoteComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            myNoteComplete.setTypeface(null, Typeface.BOLD);
        } else {
            myNoteComplete.setText("?????????");
            myNoteComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
        }

        /*
            ??????????????? ?????? ??????
         */
        selfIntroduceComplete.setTypeface(null, Typeface.NORMAL);
        if (_selfIntroduce.equals("")) {
            selfIntroduceComplete.setText("?????????");
            selfIntroduceComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            selfIntroduceRegdate.setVisibility(View.INVISIBLE);
        } else {
            selfIntroduceComplete.setText("??????");
            selfIntroduceComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            selfIntroduceComplete.setTypeface(null, Typeface.BOLD);
            selfIntroduceRegdate.setText(_selfIntroduce);
            selfIntroduceRegdate.setVisibility(View.VISIBLE);
        }

        /*
            ?????? ?????? ??????
         */
        discussionComplete.setTypeface(null, Typeface.NORMAL);
        if (_discussion.equals("")) {
            discussionComplete.setText("?????????");
            discussionComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            discussionRegdate.setVisibility(View.INVISIBLE);
        } else {
            discussionComplete.setText("??????");
            discussionComplete.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            discussionComplete.setTypeface(null, Typeface.BOLD);
            discussionRegdate.setText(_discussion);
            discussionRegdate.setVisibility(View.VISIBLE);
        }

        //?????????????????? ?????? ?????? ?????????
        orientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed("ori");
            }
        });
    }

    /**
     * ????????? ?????? ??????????????? ????????????.
     */
    private void notiJson_parsing() {
//        Log.i(TAG, "???????????? =======> " + s);
        ArrayList<A10_ClassNotiData> arrJucaRatioData = (ArrayList<A10_ClassNotiData>) getArguments().get("NOTI_INFO");

        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        A10_ClassNotiAdapter a7_classNotiAdapter = new A10_ClassNotiAdapter(getActivity(), arrJucaRatioData);

        notiList.setHasFixedSize(true);
        notiList.setLayoutManager(linearLayout);
        a7_classNotiAdapter.setOnClickListener();

        notiList.setAdapter(a7_classNotiAdapter);
    }

    /**
     * ????????? ???????????? json??? ??????
     */
    private void listJson_parsing() {
        AttendStateData _attendStateData;
        ArrayList<AttendStateData> arrAttendStateData = new ArrayList<>();
        ArrayList<Double> arrJucaRatioData = (ArrayList<Double>) getArguments().get("JUCHA_RATIO");

        int minJucha = getArguments().getInt("MIN_JUCHA");
        int maxJucha = getArguments().getInt("MAX_JUCHA");
        /*
         * ???????????? ????????? ?????? ????????? ???????????? ????????????
         * */
        try {
            for (int i = 0; i < 15; i++) {
                double juchaRatio = arrJucaRatioData.get(i);

                /**
                 * ????????? ??????    2020.06.02 ?????????
                 */
                if (i == 6 || i == 14) {
                    if (juchaRatio > 0) {
                        juchaRatio = 100.0;
                    }
                }

                Log.i(TAG, "Ratio :::> " + juchaRatio);
                juchaRatio = Math.round(juchaRatio * 100) / 100.0;      //????????? 2??????????????? ?????????.
//                Log.i(TAG, "Ratio :::> " + ratio);
                _attendStateData = new AttendStateData((i + 1), minJucha, maxJucha, juchaRatio);
                /*
                if (i == 6 || i == 14){
                    if (juchaRatio == 0){
                    }
                }
                */
                arrAttendStateData.add(_attendStateData);
            }
        } catch (Exception e) {
            Log.i(TAG, "notice JSON Exc: " + e.getMessage());
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        A10_HomeAttendAdapter a7_HomeAttendAdapter = new A10_HomeAttendAdapter(getActivity(), arrAttendStateData);

        attendList.setHasFixedSize(true);
        attendList.setLayoutManager(gridLayoutManager);
        a7_HomeAttendAdapter.setOnClickListener();

        attendList.setAdapter(a7_HomeAttendAdapter);
        a7_HomeAttendAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String s) {
        if (mListener != null) {
            mListener.onFragmentHome(s);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentHomeListener) {
            mListener = (OnFragmentHomeListener) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnFragmentHomeListener");
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
    public interface OnFragmentHomeListener {
        // TODO: Update argument type and name
        void onFragmentHome(String s);
    }
}
