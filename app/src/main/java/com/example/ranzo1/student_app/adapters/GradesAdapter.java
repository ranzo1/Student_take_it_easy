package com.example.ranzo1.student_app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.objects.Subject;
import com.example.ranzo1.student_app.objects.SubjectGradeObject;

import java.util.List;

/**
 * Created by ranzo1 on 2.6.2017..
 */

public class GradesAdapter extends BaseAdapter {

    private Context mContext;
    private List<SubjectGradeObject> mSubjectGrades;
    private TextView subjectName,points,grade;

    public GradesAdapter(Context mContext, List<SubjectGradeObject> mSubjectGrades){

        this.mContext=mContext;
        this.mSubjectGrades = mSubjectGrades;

    }
    @Override
    public int getCount() {
        return mSubjectGrades.size();
    }

    @Override
    public Object getItem(int position) {
        return mSubjectGrades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.grades_adapter,null);

        //imageView=(ImageView)v.findViewById(R.id.imageView2);

        // imageView.setImageResource(position);


        subjectName=(TextView) v.findViewById(R.id.subjName);
        points=(TextView) v.findViewById(R.id.points);
        grade=(TextView) v.findViewById(R.id.grade);
        RelativeLayout relativeLayoutColor = (RelativeLayout)v.findViewById(R.id.colorGrades);

        subjectName.setText(String.valueOf(mSubjectGrades.get(position).getSubjectName()));
       // subjectName.setTextColor(mSubjectGrades.get(position).getColor());
        relativeLayoutColor.setBackgroundColor(mSubjectGrades.get(position).getColor());

        points.setText(String.valueOf(mSubjectGrades.get(position).getPoint()));

        if(mSubjectGrades.get(position).getGrade()==0){

            grade.setText(String.valueOf(5));

        }else {
            grade.setText(String.valueOf(mSubjectGrades.get(position).getGrade()));
        }

            if (mSubjectGrades.get(position).getPoint() > 54) {
                int color = Color.parseColor("#84da5d");
                grade.setTextColor(color);
            }











        return v;
    }
}
