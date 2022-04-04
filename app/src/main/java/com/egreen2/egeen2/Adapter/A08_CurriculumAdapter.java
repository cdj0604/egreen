package com.egreen2.egeen2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.egreen2.egeen2.Data.A08_CurriculumListData;
import com.egreen2.egeen2.R;

import java.util.ArrayList;

public class A08_CurriculumAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<A08_CurriculumListData> data = new ArrayList<A08_CurriculumListData>();
    LayoutInflater inflater;

    public A08_CurriculumAdapter(Context applicationContext, int a08_custum_curriculumlist, ArrayList<A08_CurriculumListData> curriculumListData) {
        this.context = applicationContext;
        this.layout = a08_custum_curriculumlist;
        this.data = curriculumListData;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getclassId();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, true);
        }

        /* Custom 한 ListView 의 View 연결 */
        TextView a08_subject_Title = convertView.findViewById(R.id.a08_subject_Title);

        /* 공지사항 작성일 View에 웹에서 받아온 data 넣기 */
        a08_subject_Title.setText(data.get(position).getclassTitle());


        return convertView;
    }

}
