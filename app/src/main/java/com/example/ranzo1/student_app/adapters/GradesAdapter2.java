package com.example.ranzo1.student_app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.objects.GradeObject;
import com.example.ranzo1.student_app.objects.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranzo1 on 2.6.2017..
 */

public class GradesAdapter2 extends BaseAdapter {

    private Context mContext;
    private List<GradeObject> mGrades;
    private TextView type,points,additional;


    public GradesAdapter2(Context mContext, List<GradeObject> mGrades){

        this.mContext=mContext;
        this.mGrades = mGrades;

    }
    @Override
    public int getCount() {
        return mGrades.size();
    }

    @Override
    public Object getItem(int position) {
        return mGrades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.grades_adapter2,null);


        points=(TextView)v.findViewById(R.id.textView25);
        type =(TextView) v.findViewById(R.id.textView24);
        additional=(TextView)v.findViewById(R.id.textView26);



        //RelativeLayout relativeLayoutColor=(RelativeLayout)v.findViewById(R.id.colorGrades2);
       // relativeLayoutColor.setBackgroundColor(mGrades.get(position).getColor());
        points.setText(String.valueOf(mGrades.get(position).getPoints()));
        type.setText(String.valueOf(mGrades.get(position).getTypeOfExam()));
        additional.setText(String.valueOf(mGrades.get(position).getAdditional()));


        //note.setText(String.valueOf("nisam radio"));
        //setting subject name to type of exam
       // type.setTextColor(mGrades.get(position).getColor());


       // v.setTag(mGrades.get(position).getName());

        return v;
    }
}
