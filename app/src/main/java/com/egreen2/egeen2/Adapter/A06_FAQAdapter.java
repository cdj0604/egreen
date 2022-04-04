package com.egreen2.egeen2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.egreen2.egeen2.Data.A06_FAQListData;

import java.util.ArrayList;

public class A06_FAQAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<A06_FAQListData> data = new ArrayList<A06_FAQListData>();
    LayoutInflater inflater;

    public A06_FAQAdapter(Context applicationContext, int a06_custom_faqlist, ArrayList<A06_FAQListData> faqListData) {
        this.context = applicationContext;
        this.layout = a06_custom_faqlist;
        this.data = faqListData;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getFaqTitle();
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
        //TextView a061_faqTitle = convertView.findViewById(R.id.a061_faqTitle);

        /* 공지사항 제목 View에 웹에서 받아온 data 넣기 */
        //a061_faqTitle.setText(data.get(position).getFaqTitle());

        return convertView;
    }

}
