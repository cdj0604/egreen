package com.egreen2.egeen2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egreen2.egeen2.Data.A10_ClassNotiData;
import com.egreen2.egeen2.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class A10_ClassNotiAdapter extends RecyclerView.Adapter {
    private static final String TAG = A10_ClassNotiAdapter.class.getSimpleName();

    Context context;
    ArrayList<A10_ClassNotiData> data;
    private ClassNotiListener mListener;

    public A10_ClassNotiAdapter(Context context, ArrayList<A10_ClassNotiData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.a7_home_class_noti_c, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ViewHolder v = (ViewHolder) holder;

        String title = data.get(position).getTitle();

        v.title.setText(title);
        v.title.setSingleLine();        // 1줄로만 보여주기
        v.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);     // 흐르게 하기
        v.title.setSelected(true);      // 포커스를 주지 않아도 흐르게 하기
        v.title.setMarqueeRepeatLimit(-1);

        String regDate = data.get(position).getRegDate();
        v.regDate.setText(regDate);
        v.regDate.setTextSize(12);

        v.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int notiId = data.get(position).getNotiId();

                mListener.onItemClicked_noti(notiId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnClickListener() {
        if (context instanceof ClassNotiListener) {
            mListener = (ClassNotiListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ClassNotiListener");
        }
    }

    public interface ClassNotiListener {
        void onItemClicked_noti(int notiId);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView regDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            regDate = itemView.findViewById(R.id.regDate);
        }
    }
}
