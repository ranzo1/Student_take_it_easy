package com.example.ranzo1.student_app.add_and_update;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.mainActivities.EventsActivity;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.github.sundeepk.compactcalendarview.domain.Event;

import es.dmoral.toasty.Toasty;

import static com.example.ranzo1.student_app.mainActivities.EventsActivity.compactCalendar;
import static com.example.ranzo1.student_app.mainActivities.EventsActivity.lista_datumi;

public class Calendar_updateActivity extends AppCompatActivity {

    static final int DIALOG_ID=0;
    private EditText start;
    private int hour;
    private int minute;
    private String data,midle;
    private long time;
    private int color;
    private EditText about;
    private  Event stariEvent;
    private int check=5;
    private int position;
    DatabaseHendler db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_update);
        overridePendingTransition(R.anim.bounce_fast,R.anim.fab_scale_down);

        data = getIntent().getStringExtra("Data");
        color = getIntent().getIntExtra("Color",0);
        time = getIntent().getLongExtra("Time",-1);
        check = getIntent().getIntExtra("check",check);
        position = getIntent().getIntExtra("position",0);

        stariEvent=new Event(color,time,data);
        db=DatabaseHendler.getInstance(this);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        start=(EditText)findViewById(R.id.startEditText);
        about=(EditText)findViewById(R.id.editText2);
        start.setFocusable(false);
        start.setText(data.substring(0,5));

        midle=data.substring(5,data.indexOf("\n")+1);
        about.setText(data.substring(data.indexOf("\n")+1));

        showDialogOnStartClick();

        Button update = (Button)findViewById(R.id.updateBtn);

        TextView txtV =(TextView)findViewById(R.id.txtVvv);
        txtV.setText(data.substring(5,data.indexOf(" ")-1));
        txtV.setBackgroundColor(color);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String new_data=start.getText().toString()+midle+about.getText().toString();

                db.deleteEvent(stariEvent);


                compactCalendar.removeEvent(stariEvent);
                compactCalendar.addEvent(new Event(color,time,new_data));
                lista_datumi.remove(stariEvent);
                lista_datumi.add(new Event(color,time,new_data));

                db.addEvent(new Event(color,time,new_data));

                Intent i =new Intent(getApplicationContext(),EventsActivity.class);
                i.putExtra("position",position);
                i.putExtra("Data",new_data);
                i.putExtra("Time",time);
                i.putExtra("Color",color);
                setResult(3,i);
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                Toasty.success(Calendar_updateActivity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void showDialogOnStartClick(){


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DIALOG_ID);
                InputMethodManager mgr =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(start.getWindowToken(),0);
            }
        });

    }
    @Override
    protected Dialog onCreateDialog(int id){

        if(id==DIALOG_ID)
        {
            TimePickerDialog t1=new TimePickerDialog(this,tpickerListener,hour,minute,true);


            return t1;
        }
        else return null;
    }

    private TimePickerDialog.OnTimeSetListener tpickerListener= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
            hour=hourOfDay;
            minute=minute1;
            String hourString=String.valueOf(hour);
            String minuteString=String.valueOf(minute);



            if (hour < 10)
                hourString = "0" + hour;
            else
                hourString = "" +hour;

            if (minute < 10)
                minuteString = "0" + minute;
            else
                minuteString = "" +minute;

            start.setText(hourString + ":" + minuteString);


        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
