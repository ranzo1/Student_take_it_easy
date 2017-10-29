package com.example.ranzo1.student_app.add_and_update;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.mainActivities.TimetableActivity;
import com.example.ranzo1.student_app.objects.Subject;
import com.example.ranzo1.student_app.objects.TimeTableObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AddTimeTable extends Activity{

    public List<Subject> subjects= new ArrayList<>();
   // private List<String> short_names=new ArrayList<>();
    //private Spinner spinner_days;
    static final int DIALOG_ID=0;
    private int hour;
    private int minute;
    private Spinner spinner_names;
    private EditText start=null;
    private EditText end=null;
    private EditText additional;
    private int position=-1;
    private ArrayList<TimeTableObject> values1=new ArrayList<>();
    private ArrayList<TimeTableObject> values=new ArrayList<>();

    private int startClick=0;
    private int endClick=0;
    private ArrayList<TimeTableObject> databaseTimetable=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_add_time_table);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        values = getIntent().getParcelableArrayListExtra("Dogadjaji");
        position=getIntent().getIntExtra("position",-1);

        CardView cardView=(CardView)findViewById(R.id.cardViewtimeTable);

       // cardView.setCardBackgroundColor(values.get(0).getColor());
        TextView txtV =(TextView)findViewById(R.id.txtV);
        txtV.setText(values.get(0).getShortName());
        txtV.setBackgroundColor(values.get(0).getColor());

        //////////////////////not used in this activity,only for some testing////////////////////
        databaseTimetable=getIntent().getParcelableArrayListExtra("databaseTimeTable");



        start =(EditText) findViewById(R.id.editTextStart);
        end=(EditText) findViewById(R.id.editTextEnd);
        additional=(EditText) findViewById(R.id.sad);
        final Button edit = (Button)findViewById(R.id.Edit);

        start.setFocusable(false);
        end.setFocusable(false);
        start.setText(values.get(0).getStart());
        end.setText(values.get(0).getEnd());
        additional.setText(values.get(0).getAdditional());

        showDialogOnStartClick();
        //didTapButton(edit);

       clickTapButton(edit);


    }
    public void showDialogOnStartClick(){

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DIALOG_ID);
                InputMethodManager mgr =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(start.getWindowToken(),0);

                startClick=1;
                endClick=0;

            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DIALOG_ID);
                InputMethodManager mgr =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(start.getWindowToken(),0);

                startClick=0;
                endClick=1;

            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id){

        if(id==DIALOG_ID)
        {
            return new TimePickerDialog(this,tpickerListener,hour,minute,true);
        }
        else return null;
    }
    public void didTapButton(View view) {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce_fast);
        myAnim.setStartOffset(450);
        view.startAnimation(myAnim);


        view.startAnimation(myAnim);
    }
    public void clickTapButton(View view) {

        view.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN: {
                        //ImageButton view = (ImageButton ) v;

                        v.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {

                        Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {


                        }else {
                            values1 = new ArrayList<TimeTableObject>();
                            values1.add(new TimeTableObject(values.get(0).getDay(), values.get(0).getShortName(),
                                    start.getText().toString(), end.getText().toString(), additional.getText().toString(),
                                    values.get(0).getColor(),values.get(0).getId()));

                            if (start.getText().toString().trim().length() > 0 && end.getText().toString().trim().length() > 0) {

                                Intent intent = new Intent(getApplicationContext(), TimetableActivity.class);
                                intent.putParcelableArrayListExtra("listaDogadjaja", values1);
                                intent.putExtra("position",position);


                                setResult(2, intent);
                                Toasty.success(AddTimeTable.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                               // overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                                finish();
                                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                            } else
                                Toasty.error(AddTimeTable.this, "Insert time!", Toast.LENGTH_SHORT).show();
                          //  Snackbar.make(v, "Insert time!", Snackbar.LENGTH_LONG)
                                  //  .setAction("Action", null).show();

                        }
                    }


                    case MotionEvent.ACTION_CANCEL: {
                        //ImageButton view = (ImageButton) v;
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            // User moved outside bounds
                            v.getBackground().clearColorFilter();
                            v.invalidate();


                        }
                        return false;
                        //break;
                    }

                }
                return true;
            }

        });

    }

    private TimePickerDialog.OnTimeSetListener tpickerListener= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
            hour=hourOfDay;
            minute=minute1;
            String hourString=String.valueOf(hour);
            String minuteString=String.valueOf(minute);

            if(startClick==1)
            {
                if (hour < 10)
                    hourString = "0" + hour;
                else
                    hourString = "" +hour;


                if (minute < 10)
                    minuteString = "0" + minute;
                else
                    minuteString = "" +minute;

                start.setText(hourString + ":" + minuteString);
            }else if(endClick==1)
            {
                if (hour < 10)
                    hourString = "0" + hour;
                else
                    hourString = "" +hour;


                if (minute < 10)
                    minuteString = "0" + minute;
                else
                    minuteString = "" +minute;

                end.setText(hourString + ":" + minuteString);
            }

        }
    };

    @Override
    public void onBackPressed() {
            super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);






    }

}
