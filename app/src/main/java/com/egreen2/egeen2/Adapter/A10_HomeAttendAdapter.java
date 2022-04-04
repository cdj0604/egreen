package com.egreen2.egeen2.Adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egreen2.egeen2.Data.AttendStateData;
import com.egreen2.egeen2.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class A10_HomeAttendAdapter extends RecyclerView.Adapter {
    private static final String TAG = A10_HomeAttendAdapter.class.getSimpleName();

    Context context;
    ArrayList<AttendStateData> data;
    private AttendStateClickListener mListener;

    public A10_HomeAttendAdapter(Context context, ArrayList<AttendStateData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.a7_home_attend_state_c, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder v = (ViewHolder) holder;

        final int jucha = data.get(position).getjucha();

        if (jucha == 7) {
            v.jucha.setText("중간고사");
            v.jucha.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        } else if (jucha == 15) {
            v.jucha.setText("기말고사");
            v.jucha.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        } else {
            v.jucha.setText(jucha + "주차");
        }

        double isComplete = data.get(position).getAttenRatio();
        int minDate = data.get(position).getMinJucha();
        int maxDate = data.get(position).getMaxJucha();

        if (jucha > maxDate) {
            //아직 열리지 않은 강의
            v.jucha.setTextColor(ContextCompat.getColor(context, R.color.gray));
            v.isComplete.setText(isComplete + "%");
            v.isComplete.setTextColor(ContextCompat.getColor(context, R.color.gray));
        } else if (jucha < minDate) {
            //학습기간이 지난 강의
            v.isComplete.setText(isComplete + "%");
            v.area_stateBox.setBackgroundResource(R.drawable.ed);

            if (isComplete == 100.0) {
                //중에 100% 달성했을때
                v.area_stateBox.setBackgroundResource(R.drawable.ed100);
            }
        }

        if (jucha == minDate || jucha == maxDate) {
            //지금 출석 인정 가능한 강의
            v.isComplete.setText(isComplete + "%");
            v.area_stateBox.setBackgroundResource(R.drawable.ing);

            if (isComplete == 100.0) {
                v.area_stateBox.setBackgroundResource(R.drawable.ing100);
            }
        }

        v.area_stateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked_home(jucha);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnClickListener() {
        if (context instanceof AttendStateClickListener) {
            mListener = (AttendStateClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AttendStateClickListener");
        }
    }

    public interface AttendStateClickListener {
        void onItemClicked_home(int jucha);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView jucha, isComplete;
        LinearLayout area_stateBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jucha = itemView.findViewById(R.id.jucha);
            isComplete = itemView.findViewById(R.id.isComplete);
            area_stateBox = itemView.findViewById(R.id.area_stateBox);
        }
    }
}
