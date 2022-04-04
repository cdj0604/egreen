package com.egreen2.egeen2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.egreen2.egeen2.Data.A05_NoticeListData;
import com.egreen2.egeen2.R;

import java.util.ArrayList;

public class A05_NoticeAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<A05_NoticeListData> data = new ArrayList<A05_NoticeListData>();
    LayoutInflater inflater;

    public A05_NoticeAdapter(Context applicationContext, int a05_custum_noticelist, ArrayList<A05_NoticeListData> noticeListData) {
        this.context = applicationContext;
        this.layout = a05_custum_noticelist;
        this.data = noticeListData;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getNoticeTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        /* Custom 한 ListView 의 View 연결 */
        TextView a05_noticeImportance = convertView.findViewById(R.id.a05_noticeImportance);
        TextView a05_noticeTitle = convertView.findViewById(R.id.a05_noticeTitle);
        TextView a05_noticeDate = convertView.findViewById(R.id.a05_noticeDate);

        /* 공지사항 제목 View에 웹에서 받아온 data 넣기 */
        a05_noticeTitle.setText(data.get(position).getNoticeTitle());

        /* 공지사항 작성일 View에 웹에서 받아온 data 넣기 */
        a05_noticeDate.setText(data.get(position).getNoticeDate());

        /* 웹에서 받아온 flag 값에 따라 '중요', '공지' 를 표시 */
        if (data.get(position).getNoticeimportance() == 1) {
            a05_noticeImportance.setBackgroundResource(R.drawable.a05_star);
        } else {
            a05_noticeImportance.setBackgroundResource(R.drawable.a05_notice);
        }

        return convertView;
    }
}
