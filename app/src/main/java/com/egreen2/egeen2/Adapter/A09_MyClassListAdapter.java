package com.egreen2.egeen2.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.egreen2.egeen2.Data.A09_MyClassListData;
import com.egreen2.egeen2.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class A09_MyClassListAdapter extends RecyclerView.Adapter {
    private static final String TAG = A09_MyClassListAdapter.class.getSimpleName();

    Context context;
    ArrayList<A09_MyClassListData> data;
    private MyClassListClickListener mListener;

    public A09_MyClassListAdapter(Context context, ArrayList<A09_MyClassListData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.a09_classroom_c, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ViewHolder v = (ViewHolder) holder;
        String abc = data.get(position).getStudyDate();
        /* 색 변경 */
        final boolean isEnable = data.get(position).isEnable();
        if (isEnable) {
            //모바일에서 수강 가능한 과목이라면,
            double a = Double.parseDouble(data.get(position).getAttendRatio());
            v.topLayout.setBackgroundResource(R.drawable.round_border2);
            v.classTitle.setTextColor(ContextCompat.getColor(context, R.color.black));
            v.studyDate.setTextColor(ContextCompat.getColor(context, R.color.black));
            v.attendRatio.setTextColor(ContextCompat.getColor(context, R.color.black));


        } else {
            v.topLayout.setBackgroundResource(R.drawable.round_border2);
            v.classTitle.setTextColor(ContextCompat.getColor(context, R.color.gray));
            v.studyDate.setTextColor(ContextCompat.getColor(context, R.color.gray));
            v.attendRatio.setTextColor(ContextCompat.getColor(context, R.color.gray));

        }

        v.classTitle.setText(data.get(position).getClassTitle());
        //밑줄긋기
//        v.classTitle.setPaintFlags(v.classTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String studyDate_txt;
        String studyDate_txt1;
        if (isEnable) {
            if (data.get(position).getStudyDate().equals("0")) {
                studyDate_txt = "출석기간이 아닙니다. 접속시 오류가 발생합니다.";
                v.studyDate.setText(studyDate_txt);
                v.studyDate.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else {
                studyDate_txt =  data.get(position).getStudyDate() + " 진행중";
                v.studyDate.setText(studyDate_txt);
                v.studyDate.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        }

        //진도율
        String jindo = data.get(position).getAttendRatio();
        //프로그레스바의 레이어목록을 나눔
        LayerDrawable progressBarDrawable = (LayerDrawable) v.progressBar.getProgressDrawable();
        //프로그레스바를 나눈후 배경을 회색으로 고정
        Drawable backgroundDrawable = progressBarDrawable.getDrawable(0);
        Drawable progressDrawableDrawable = progressBarDrawable.getDrawable(1);

        if (jindo.equals("0")) {
            v.state_study.setText("학습전");
            v.state_study.setTextColor(ContextCompat.getColor(context, R.color.gray));
        } else if (jindo.equals("100")) {
            v.state_study.setText("학습완료");
            v.state_study.setTextColor(ContextCompat.getColor(context, R.color.red));
            v.progressBar.getProgressDrawable().setColorFilter(Color.rgb(220, 45, 45), PorterDuff.Mode.MULTIPLY);
            // v.progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.b_red), PorterDuff.Mode.SRC_IN); //배경 회색 설정
        } else {
            v.state_study.setText("학습중");
            v.state_study.setTextColor(ContextCompat.getColor(context, R.color.green));
            v.progressBar.getProgressDrawable().setColorFilter(Color.rgb(46, 146, 94), PorterDuff.Mode.MULTIPLY);
            backgroundDrawable.setColorFilter(ContextCompat.getColor(context, R.color.b_green), PorterDuff.Mode.SRC_IN); //배경 회색 설정
            //  progressDrawableDrawable.setColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.SRC_IN);
        }
        double a = Double.parseDouble(data.get(position).getAttendRatio());
        v.progressBar.setProgress((int) Math.round(a));
        v.attendRatio.setText(+Math.round(a) + "%"); // 학습률% 반올림
        if (a == 0) {
            v.attendRatio.setTextColor(ContextCompat.getColor(context, R.color.gray));
        } else
            v.attendRatio.setTextColor(ContextCompat.getColor(context, R.color.black));


        //A09classroom 에 텍스트뷰 생성후 진도율 값에 따라 학습완료,학습중 표 state_study
        Log.i(TAG, "주차표시" + data.get(position).getStudyDate());
        Log.i(TAG, "진도율" + data.get(position).getAttendRatio());
        Log.i(TAG, "타입" + data.get(position).getAttendRatio().getClass().getName());


        if (mListener != null) {
            v.topLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String classId = data.get(position).getClassId();
                    String classTitle = data.get(position).getClassTitle();
                    String directoryName = data.get(position).getDirectoryName();
                    String isReadOrientation = data.get(position).getIsReadOrientation();
                    String isSurvey = data.get(position).getIsSurvey();
                    String isThereHighSchoolName = data.get(position).getIsThereHighSchoolName();
                    String isDongPost = data.get(position).isDongPost();
                    String myGoal = data.get(position).getMyGoal();
                    String myNote = data.get(position).getMyNote();
                    String studyDate = data.get(position).getStudyDate();
                    String selfInt = data.get(position).getSelfInt();
                    String discussion = data.get(position).getDiscussion();
//                    Log.i(TAG, "Clicked" + classId);

                    mListener.onItemClicked(isEnable, classId, classTitle, directoryName, isReadOrientation,
                            isSurvey, isThereHighSchoolName, isDongPost, myGoal, myNote, studyDate, selfInt, discussion);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "데이터가 얼마나 있니??? " + data.size());
        return data.size();
    }

    public void setOnClickListener(MyClassListClickListener listener) {
        this.mListener = listener;
    }

    public interface MyClassListClickListener {
        void onItemClicked(boolean isEnable, String classId, String classTitle, String strUrl, String isReadOrientation,
                           String isSurvey, String isThereHighSchoolName, String isDongPost, String myGoal, String myNote,
                           String studyDate, String selfInt, String discussion);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout topLayout;
        TextView classTitle;
        TextView studyDate;
        TextView attendRatio;
        TextView state_study;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            topLayout = itemView.findViewById(R.id.topLayout);
            classTitle = itemView.findViewById(R.id.classTitle);
            studyDate = itemView.findViewById(R.id.studyDate);
            attendRatio = itemView.findViewById(R.id.attendRatio);
            state_study = itemView.findViewById(R.id.state_study);
            progressBar = itemView.findViewById(R.id.progressBar2);
        }
    }
}
