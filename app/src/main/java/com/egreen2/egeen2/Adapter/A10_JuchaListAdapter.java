package com.egreen2.egeen2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.egreen2.egeen2.Data.A10_JuchaData;
import com.egreen2.egeen2.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class A10_JuchaListAdapter extends RecyclerView.Adapter {
    private static final String TAG = A09_MyClassListAdapter.class.getSimpleName();

    Context context;
    ArrayList<A10_JuchaData> data;
    private JuchaClickListener mListener;

    public A10_JuchaListAdapter(Context context, ArrayList<A10_JuchaData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.a7_tab_c, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ViewHolder v = (ViewHolder) holder;

        boolean gu = data.get(position).isGu();
        if (gu) {
            v.juchaArea.setVisibility(View.GONE);
        } else {
            v.juchaArea.setVisibility(View.VISIBLE);
        }

        v.jucha.setText(data.get(position).getJucha() + "주차");

        //학습 일자
        String studyDt = data.get(position).getStudyDt();
        v.studyDate.setText("(" + studyDt + ")");

        //차시, 주제
        int chasi = data.get(position).getChasi();
        String title = data.get(position).getTitle();
        v.chasi.setText(chasi + "차시");
        v.title.setText(title);

        //학습시간/전체학습시간
        final String watchedTime = data.get(position).getWatchedTime();
        final String fullTime = data.get(position).getFullTime();
        v.time.setText(watchedTime + "/" + fullTime);

        //페이지 열람/전체 페이지수
        String readPage = data.get(position).getReadPage();
        String fullPage = data.get(position).getFullPage();
        String totalRatio = data.get(position).getTotalRatio();
        v.page.setText(readPage + "/" + fullPage);

        //출석상태, 진도율
        int _totalRatio = Integer.parseInt(totalRatio);
        if (_totalRatio == 0) {
            v.state_txt.setText("학습전");
            v.state_txt.setTextColor(ContextCompat.getColor(context, R.color.white));          //       색상 흰색으로 통일
            v.chasiArea.setBackgroundResource(R.drawable.border_gray);
            v.stateArea.setBackgroundResource(R.drawable.gray);
        } else if (_totalRatio == 100) {
            v.state_txt.setText("학습완료");
            v.state_txt.setTextColor(ContextCompat.getColor(context, R.color.white));           //       색상 흰색으로 통일
            v.chasiArea.setBackgroundResource(R.drawable.border_red);
            v.stateArea.setBackgroundResource(R.drawable.red);

        } else {
            v.state_txt.setText("학습중");
            v.state_txt.setTextColor(ContextCompat.getColor(context, R.color.white));           //       색상 흰색으로 통일
            v.chasiArea.setBackgroundResource(R.drawable.border_green);
            v.stateArea.setBackgroundResource(R.drawable.green);
        }

        v.totalRatio.setText(totalRatio + "%");

        if (mListener != null) {
            v.chasiArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int jucha = data.get(position).getJucha();
                    int chasi = data.get(position).getChasi();

                    String sCid = data.get(position).getsCid();
                    String eCid = data.get(position).geteCid();
                    String fileRoot = data.get(position).getFileRoot();

                    boolean enable = data.get(position).isEnable();

                    Log.i(TAG, "fileRoot ==> " + fileRoot);
                    mListener.onItemClicked(sCid, eCid, jucha, chasi, watchedTime, fullTime, fileRoot, enable);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnClickListener() {
        this.mListener = (JuchaClickListener) context;
    }

    public interface JuchaClickListener {
        void onItemClicked(String sCid, String eCid,
                           int jucha, int chasi, String watchedTime,
                           String fullTime, String fileRoot, boolean enable);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout juchaArea;
        LinearLayout chasiArea, stateArea;
        TextView jucha, studyDate;
        TextView chasi;
        TextView title;
        TextView time;
        TextView page;
        TextView state_txt, totalRatio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            juchaArea = itemView.findViewById(R.id.juchaArea);
            chasiArea = itemView.findViewById(R.id.chasiArea);
            stateArea = itemView.findViewById(R.id.stateArea);

            jucha = itemView.findViewById(R.id.jucha);
            studyDate = itemView.findViewById(R.id.studyDate);

            chasi = itemView.findViewById(R.id.chasi);
            title = itemView.findViewById(R.id.title);

            time = itemView.findViewById(R.id.time);
            page = itemView.findViewById(R.id.page);

            state_txt = itemView.findViewById(R.id.state_txt);
            totalRatio = itemView.findViewById(R.id.totalRatio);
        }
    }
}
