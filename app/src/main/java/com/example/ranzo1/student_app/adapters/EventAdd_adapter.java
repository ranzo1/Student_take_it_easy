package com.example.ranzo1.student_app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ranzo1.student_app.objects.Subject;
import com.example.ranzo1.student_app.R;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ranzo1 on 28.12.2016..
 */

public class EventAdd_adapter extends BaseAdapter {

    private TextView text;
    private Context mContext;
    private TextView time, timeTextView,monthTextView,monthNumTextView;
    private List<Event> mSubjects;





    public EventAdd_adapter(){}




    public EventAdd_adapter(Context mContext, List<Event> mSubjects ){

        this.mContext=mContext;
        this.mSubjects=mSubjects;

    }


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

        View v = View.inflate(mContext, R.layout.custom_eventadd_row,null);
        timeTextView =(TextView)v.findViewById (R.id.vreme);
        text = (TextView)v.findViewById (R.id.textGornjideo);
        time = (TextView)v.findViewById(R.id.timeEventAdd);

        monthTextView = (TextView)v.findViewById(R.id.textViewMonth);
        monthNumTextView =(TextView) v.findViewById(R.id.textViewMontNumber);
        TextView label=(TextView)v.findViewById(R.id.imageView2222);


        if(mSubjects.get(position).getData().toString().substring(5,8).equals("0:0"))
        {
            text.setText(String.valueOf(mSubjects.get(position).getData()).substring(9));

        }else{
            text.setText(String.valueOf(mSubjects.get(position).getData()).substring(5,13)+
                    String.valueOf(mSubjects.get(position).getData()).substring(13));


        }

              //  txtV.setText(data.substring(5,data.indexOf(" ")-1));

        label.setBackgroundColor(mSubjects.get(position).getColor());
       // String pomocni=String.valueOf(mSubjects.get(position).getData());
        //Treba lepse napisati,!
       // label.setText(String.valueOf(mSubjects.get(position).getData()).substring(5,pomocni.indexOf(" ")-1));


        //month.setText((getDate(mSubjects.get(position).getTimeInMillis(),"dd/MM/yyyy hh:mm")).substring(0,2));
        String timeString=String.valueOf(mSubjects.get(position).getData()).substring(0,5);
        timeTextView.setText(timeString);



        time.setText((getDate(mSubjects.get(position).getTimeInMillis(),"EEE, d MMM yyyy")));

        monthNumTextView.setText((getDate(mSubjects.get(position).getTimeInMillis(),"dd/MM/yyyy hh:mm")).substring(0,2));

        monthTextView.setText((getDate(mSubjects.get(position).getTimeInMillis(),"dd/MMM/yyyy hh:mm")).substring(3,6));






        //time.setText(String.valueOF(( mSubjects.get(position).getTimeInMillis())))


        return v;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    /**
     * Created by ranzo1 on 13.12.2016..
     */



    public static class Notes_Adapter extends BaseAdapter {

        public Notes_Adapter(){}

        private Context mContext;
        private ImageView prvi,drugi,treci,cetvrti;
        private TextView subjectShortName,text;
        private TextView title;
        private RelativeLayout layoutNote;
        private TextView datumst;

        public TextView getDatumst() {
            return datumst;
        }

        public void setDatumst(TextView datumst) {
            this.datumst = datumst;
        }





        ImageView boja;

        private List<Subject> mSubjects;


        public Notes_Adapter(Context mContext, ArrayList<Subject> mSubjects ){

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

            View v = View.inflate(mContext,R.layout.notes_view,null);



            title=(TextView)v.findViewById(R.id.title_notes);
            text=(TextView)v.findViewById(R.id.textView10);
            TextView sn=(TextView)v.findViewById(R.id.shortNameeee);
            datumst=(TextView)v.findViewById(R.id.datum);
            layoutNote=(RelativeLayout)v.findViewById(R.id.relativeLayout13);
            CardView cardView=(CardView) v.findViewById(R.id.cardViewNote);


           // prvi =(ImageView)v.findViewById(R.id.prvi);
            drugi =(ImageView)v.findViewById(R.id.drugi);
            //treci =(ImageView)v.findViewById(R.id.treci);
            //cetvrti =(ImageView)v.findViewById(R.id.cetvrti);

            //time2=(TextView)v.findViewById(R.id.end);
            //v.setBackgroundColor(mSubjects.get(position).getColor());

           // cardView.setCardBackgroundColor(mSubjects.get(position).getColor());

           // prvi.setBackgroundColor(mSubjects.get(position).getColor());
            drugi.setBackgroundColor(mSubjects.get(position).getColor());
           // treci.setBackgroundColor(mSubjects.get(position).getColor());
           // cetvrti.setBackgroundColor(mSubjects.get(position).getColor());


            // String datum=new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            //datumst.setText(datum);

            int color=mSubjects.get(position).getColor();

            color=manipulateColor(color,1.2f);

           // layoutNote.setBackgroundColor(color);

            //sn.setText(String.valueOf(mSubjects.get(position).getShortName()));
            sn.setText(String.valueOf(mSubjects.get(position).getShortName()));
            title.setText(String.valueOf(mSubjects.get(position).getProfEmail()));
            text.setText(String.valueOf(mSubjects.get(position).getProfessor()));
            datumst.setText(String.valueOf(mSubjects.get(position).getAsisstent()));

            //0-name,1-skracen naziv,2-sadrzaj-beleske,3-naslov





            return v;
        }

        public static int manipulateColor(int color, float factor) {
            int a = Color.alpha(color);
            int r = Math.round(Color.red(color) * factor);
            int g = Math.round(Color.green(color) * factor);
            int b = Math.round(Color.blue(color) * factor);
            return Color.argb(a,
                    Math.min(r,255),
                    Math.min(g,255),
                    Math.min(b,255));
        }
    }
}
