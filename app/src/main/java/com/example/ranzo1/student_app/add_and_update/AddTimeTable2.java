package com.example.ranzo1.student_app.add_and_update;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AddTimeTable2 extends AppCompatActivity {

    private List<TimeTableObject> databaseTimetable=new ArrayList<>();
    private List<Subject> subjects= new ArrayList<>();
    private List<String> short_names=new ArrayList<>();
    private Spinner spinner_days;
    private Spinner spinner_names;
    private MaterialEditText start;
    private MaterialEditText end;
    private MaterialEditText additional;

    private int color;
    private int startClick=0;
    private int endClick=0;

    ArrayList<TimeTableObject> values=new ArrayList();

    static final int DIALOG_ID=0;
    private int hour;
    private int minute;
    private FloatingActionButton btn;
    int id=0;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_table2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        subjects = getIntent().getParcelableArrayListExtra("listaPredmeta");
        for (int i = 0; i < subjects.size(); i++) {
            short_names.add(subjects.get(i).getShortName());
        }

        databaseTimetable = getIntent().getParcelableArrayListExtra("databaseTimeTable");

        spinner_days = (Spinner) findViewById(R.id.spiner_dani);
        spinner_names = (Spinner) findViewById(R.id.spiner_predmeti);

        start = (MaterialEditText) findViewById(R.id.start);
        end = (MaterialEditText) findViewById(R.id.end);

        start.setInputType(InputType.TYPE_NULL);
        end.setInputType(InputType.TYPE_NULL);
        additional = (MaterialEditText) findViewById(R.id.sad);


        start.setFocusable(false);
        end.setFocusable(false);


        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(AddTimeTable2.this, android.R.layout.simple_list_item_1, short_names);
        myAdapter2.setDropDownViewResource((android.R.layout.simple_expandable_list_item_1));
        spinner_names.setAdapter(myAdapter2);

        showDialogOnStartClick();

        btn = (FloatingActionButton) findViewById(R.id.button6);
        didTapButton(btn);

        id = databaseTimetable.size();

        // int id=0;

        if (databaseTimetable.size() != 0) {

            for (int j = 0; j < databaseTimetable.size(); j++) {
                id = databaseTimetable.get(j).getId();


            }

            id += 1;
        }

      // clickTapButton(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                values = new ArrayList<TimeTableObject>();
                if (start.getText().toString().trim().length() > 0 && end.getText().toString().trim().length() > 0) {
                    values.add(new TimeTableObject(spinner_days.getSelectedItem().toString(), spinner_names.getSelectedItem().toString(),
                            start.getText().toString(), end.getText().toString(), additional.getText().toString(),
                            subjects.get(spinner_names.getSelectedItemPosition()).getColor(), id));


                    Intent intent = new Intent(getApplicationContext(), TimetableActivity.class);

                    intent.putParcelableArrayListExtra("listaDogadjaja", values);

                    setResult(2, intent);
                    Toasty.success(AddTimeTable2.this, "Successfully added!", Toast.LENGTH_SHORT).show();

                    finish();
                } else if(start.getText().toString().trim().length() == 0 && end.getText().toString().trim().length()==0 ) {


                    start.setError("Input time!");
                    end.setError("Input time!");
                    //  kraj.setTextColor(Color.rgb(242, 90, 90));

                    clickTapButtonFalse(btn);
                    // Toasty.error(AddTimeTable2.this, "Error!", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                    //.setAction("Action", null).show();
                }else  if(start.getText().toString().trim().length() == 0)
                {
                    start.setError("Input time!");
                    clickTapButtonFalse(btn);
                    //  Toasty.error(AddTimeTable2.this, "Error!", Toast.LENGTH_SHORT).show();
                    // Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                    //.setAction("Action", null).show();
                }else  if(end.getText().toString().trim().length()==0)
                {
                    end.setError("Input time!");
                    clickTapButtonFalse(btn);
                    //Toasty.error(AddTimeTable2.this, "Error!", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                    //.setAction("Action", null).show();


                }



            }
        });
    }



    public void clickTapButton(final View view) {

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

                            values = new ArrayList<TimeTableObject>();
                            if (start.getText().toString().trim().length() > 0 && end.getText().toString().trim().length() > 0) {
                                values.add(new TimeTableObject(spinner_days.getSelectedItem().toString(), spinner_names.getSelectedItem().toString(),
                                        start.getText().toString(), end.getText().toString(), additional.getText().toString(),
                                        subjects.get(spinner_names.getSelectedItemPosition()).getColor(), id));


                                Intent intent = new Intent(getApplicationContext(), TimetableActivity.class);

                                intent.putParcelableArrayListExtra("listaDogadjaja", values);

                                setResult(2, intent);
                                Toasty.success(AddTimeTable2.this, "Successfully added!", Toast.LENGTH_SHORT).show();

                                finish();
                            } else if(start.getText().toString().trim().length() == 0 && end.getText().toString().trim().length()==0 ) {


                                start.setError("Input time!");
                                end.setError("Input time!");
                              //  kraj.setTextColor(Color.rgb(242, 90, 90));

                                clickTapButtonFalse(view);
                               // Toasty.error(AddTimeTable2.this, "Error!", Toast.LENGTH_SHORT).show();
                                //Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                                //.setAction("Action", null).show();
                            }else  if(start.getText().toString().trim().length() == 0)
                            {
                                start.setError("Input time!");
                                clickTapButtonFalse(view);
                              //  Toasty.error(AddTimeTable2.this, "Error!", Toast.LENGTH_SHORT).show();
                                // Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                                //.setAction("Action", null).show();
                            }else  if(end.getText().toString().trim().length()==0)
                            {
                                end.setError("Input time!");
                                clickTapButtonFalse(view);
                                //Toasty.error(AddTimeTable2.this, "Error!", Toast.LENGTH_SHORT).show();
                                //Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                                //.setAction("Action", null).show();


                            }

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
            TimePickerDialog ti = new TimePickerDialog(this,tpickerListener,hour,minute,true);

            return ti;

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

    public void didTapButton(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
        animation1.setStartOffset(450);
        animation1.setDuration(400);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);



    }

    public void clickTapButtonFalse(View view) {
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        final Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);

        //animation1.setStartOffset(5000);
        animation1.setDuration(550);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
