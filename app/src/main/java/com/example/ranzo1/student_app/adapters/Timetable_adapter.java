package com.example.ranzo1.student_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ranzo1.student_app.objects.TimeTableObject;
import com.example.ranzo1.student_app.R;

import java.util.List;

/**
 * Created by ranzo1 on 13.12.2016..
 */

public class Timetable_adapter extends BaseAdapter {

    private Context mContext;
    private ImageView o1,o2,o3,o4;
    private TextView subjectShortName;
    private TextView time;
    private TextView time2;
    private Activity activity;
    private RelativeLayout background;



    ImageView boja;
    // private LayoutInflater layoutInflater;
    private List<TimeTableObject> mSubjects;


    public Timetable_adapter(Context mContext,List<TimeTableObject> mSubjects ){

        this.mContext=mContext;
        this.mSubjects=mSubjects;

    }
    @Override
    public int getCount() {
        return mSubjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mSubjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.view_timetable,null);
        subjectShortName = (TextView)v.findViewById (R.id.short_nameS);
        time=(TextView)v.findViewById(R.id.start);
        time2=(TextView)v.findViewById(R.id.end);
        //background=(RelativeLayout)v.findViewById(R.id.background);
        //background.setBackgroundColor(mSubjects.get(position).getColor());






        subjectShortName.setText(String.valueOf(mSubjects.get(position).getShortName()));
        subjectShortName.setBackgroundColor(mSubjects.get(position).getColor());
        time.setText(String.valueOf(mSubjects.get(position).getStart()));
        time2.setText(String.valueOf(mSubjects.get(position).getEnd()));






        return v;
    }
}
