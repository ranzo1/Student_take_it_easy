package com.example.ranzo1.student_app.add_and_update;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.mainActivities.EventsActivity;
import com.example.ranzo1.student_app.objects.Subject;
import com.example.ranzo1.student_app.objects.TimeTableObject;

import es.dmoral.toasty.Toasty;
import petrov.kristiyan.colorpicker.ColorPicker;
import java.util.ArrayList;

import static com.example.ranzo1.student_app.mainActivities.EventsActivity.lista_datumi;


public class Events_add2 extends AppCompatActivity {
    private ArrayList<Subject> subjects_for_date=new ArrayList<>();
    private Spinner s;
    private Spinner type;
    private ArrayList<String> spinner_subjects =new ArrayList<>();
    static final int DIALOG_ID=0;
    private EditText start;

    private int hour;
    private int minute;
    private ArrayList<TimeTableObject> values =new ArrayList<>();
    private int startClick=0;
    private CheckBox checkBox;

    private FloatingActionButton colorPickerbtn;
    private RelativeLayout layout1,layout2;
    private ColorPicker colorPicker;
    private int boja=0;
    private FloatingActionButton btn;
    private TextView label ;

    EditText eventsText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_add2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        subjects_for_date = getIntent().getParcelableArrayListExtra("subjects_for_date");//list of subjects
////////////////////////////findViewById///////////////////////////
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        layout1=(RelativeLayout)findViewById(R.id.Layout);
        layout2=(RelativeLayout)findViewById(R.id.Layout2);
        label=(TextView)findViewById(R.id.textView2);

        type=(Spinner)findViewById(R.id.spinnerType);
        s=(Spinner)findViewById(R.id.spinnerPredmeti);
        start=(EditText)findViewById(R.id.editStart1);

        colorPickerbtn =(FloatingActionButton) findViewById(R.id.colorBtn);
       // colorPickerbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d4d4d4")));



//------------------------------------------------------------------------------//
        //didTapButton(checkBox);
        //didTapButton(layout1);
        //didTapButton(layout2);
        // didTapButton(start);
        // didTapButton();


        start.setFocusable(false);

        eventsText=(EditText)findViewById(R.id.editEvent);
        /////////////////////////spinner_subjects//////////////////////////////////////////////////////
        for(int i =0;i<subjects_for_date.size();i++)
        {
            spinner_subjects.add(subjects_for_date.get(i).getShortName());
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Events_add2.this,android.R.layout.simple_list_item_1, spinner_subjects);
        myAdapter.setDropDownViewResource((android.R.layout.simple_expandable_list_item_1));
        s.setAdapter(myAdapter);
        //-----------------------------------------------------------------------------------------------------------------------------------//
        showDialogOnStartClick();// time picker dialog


        ////////////////////////////checkBox for subject,or not subject oriented event/////////////
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkBox.isChecked())
                {
                    layout1.setVisibility(View.INVISIBLE);
                    layout2.setVisibility(View.INVISIBLE);

                    type.setVisibility(View.INVISIBLE);
                    s.setVisibility(View.INVISIBLE);
                    colorPickerbtn.setVisibility(View.VISIBLE);




                    didTapColorpicker(colorPickerbtn);



                }else
                {

                    layout1.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.VISIBLE);

                    type.setVisibility(View.VISIBLE);
                    s.setVisibility(View.VISIBLE);
                    colorPickerbtn.setVisibility(View.INVISIBLE);



                    didTapButton(layout1);
                    didTapButton(layout2);

                }

            }
        });
//------------------------------------------------------------------------------------------------------------------------//

        ////////////////////////////////////colorPickerbtn////////////////////////////////////////////////////////////////
        colorPickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                colorPicker = new ColorPicker(Events_add2.this);
                colorPicker.setColors();
                colorPicker.setRoundColorButton(true);
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        if(color!=0) {
                            colorPickerbtn.setBackgroundTintList(ColorStateList.valueOf(color));
                            boja=color;
                            label.setBackgroundColor(color);

                        }

                    }

                    @Override
                    public void onCancel(){


                    }
                });
            }
        });
//-----------------------------------------------------------------------------------------------------------------------------//

        ///////////////////////////////button for add event//////////////////////////////////////////////////////////////////////////
        btn=(FloatingActionButton) findViewById(R.id.button7);
        didTapButton(btn);

       //clickTapButton(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox.isChecked()) {

                    if (start.getText().toString().trim().length() > 0 && boja != 0) {
                        String vreme = start.getText().toString();
                        TimeTableObject event = new TimeTableObject("Other", "",
                                vreme, eventsText.getText().toString(), boja, lista_datumi.size());

                        values.add(event);

                        Intent i = new Intent(getApplicationContext(), EventsActivity.class);
                        i.putParcelableArrayListExtra("event lista", values);
                        setResult(2, i);
                        //setResult(2,i2);
                        // finish The activity
                        finish();
                        //listEventTimeSort(lista_datumi);
                        Toasty.success(Events_add2.this, "Successfully added!", Toast.LENGTH_SHORT).show();

                    } else if (start.getText().toString().isEmpty() && boja == 0) {

                        clickTapButtonFalse(btn);
                        start.setError("Input time");
                        // Toasty.error(Events_add2.this, "Insert time and color!", Toast.LENGTH_SHORT).show();
                        //Snackbar.make(view, "Insert time and colorSpinner!", Snackbar.LENGTH_LONG)
                        // .setAction("Action", null).show();
                    } else if (start.getText().toString().isEmpty()) {

                        start.setError("Input time");
                        clickTapButtonFalse(btn);
                        // Toasty.error(Events_add2.this, "Insert time!", Toast.LENGTH_SHORT).show();


                        //Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                    } if (boja == 0) {

                        colorPickerbtn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(242, 90, 90)));
                        clickTapButtonFalse(btn);
                        // Toasty.error(Events_add2.this, "Insert color!", Toast.LENGTH_SHORT).show();
                        //Snackbar.make(view, "Insert colorSpinner!", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                    }


                } else {


                    if (start.getText().toString().trim().length() > 0) {

                        String vreme = start.getText().toString();
                        TimeTableObject event = new TimeTableObject(s.getSelectedItem().toString(), type.getSelectedItem().toString(),
                                vreme, eventsText.getText().toString(), subjects_for_date.get(s.getSelectedItemPosition()).getColor(), lista_datumi.size());
                        values.add(event);
                        Intent i = new Intent(getApplicationContext(), EventsActivity.class);
                        i.putParcelableArrayListExtra("event lista", values);
                        setResult(2, i);
                        //setResult(2,i2);
                        // finish The activity
                        finish();
                        Toasty.success(Events_add2.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                    } else {

                        start.setError("Input time");
                        clickTapButtonFalse(btn);
                        // Toasty.error(Events_add2.this, "Insert time!", Toast.LENGTH_SHORT).show();


                        //Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                        //      .setAction("Action", null).show();
                    }

                }
            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------//
    }

    public void clickTapButton(final View view) {

        view.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event1) {
                switch (event1.getAction()) {


                    case MotionEvent.ACTION_DOWN: {
                        //ImageButton view = (ImageButton ) v;

                        v.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {

                        Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        if (!rect.contains(v.getLeft() + (int) event1.getX(), v.getTop() + (int) event1.getY())) {


                        } else {

                            if (checkBox.isChecked()) {

                                if (start.getText().toString().trim().length() > 0 && boja != 0) {
                                    String vreme = start.getText().toString();
                                    TimeTableObject event = new TimeTableObject("Other", "",
                                            vreme, eventsText.getText().toString(), boja, lista_datumi.size());

                                    values.add(event);

                                    Intent i = new Intent(getApplicationContext(), EventsActivity.class);
                                    i.putParcelableArrayListExtra("event lista", values);
                                    setResult(2, i);
                                    //setResult(2,i2);
                                    // finish The activity
                                    finish();
                                    //listEventTimeSort(lista_datumi);
                                    Toasty.success(Events_add2.this, "Successfully added!", Toast.LENGTH_SHORT).show();

                                } else if (start.getText().toString().isEmpty() && boja == 0) {

                                    clickTapButtonFalse(view);
                                    start.setError("Input time");
                                   // Toasty.error(Events_add2.this, "Insert time and color!", Toast.LENGTH_SHORT).show();
                                    //Snackbar.make(view, "Insert time and colorSpinner!", Snackbar.LENGTH_LONG)
                                    // .setAction("Action", null).show();
                                } else if (start.getText().toString().isEmpty()) {

                                    start.setError("Input time");
                                    clickTapButtonFalse(view);
                                   // Toasty.error(Events_add2.this, "Insert time!", Toast.LENGTH_SHORT).show();


                                    //Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                                    //.setAction("Action", null).show();

                                } if (boja == 0) {

                                    colorPickerbtn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(242, 90, 90)));
                                    clickTapButtonFalse(view);
                                   // Toasty.error(Events_add2.this, "Insert color!", Toast.LENGTH_SHORT).show();
                                    //Snackbar.make(view, "Insert colorSpinner!", Snackbar.LENGTH_LONG)
                                    //.setAction("Action", null).show();
                                }


                            } else {


                                if (start.getText().toString().trim().length() > 0) {

                                    String vreme = start.getText().toString();
                                    TimeTableObject event = new TimeTableObject(s.getSelectedItem().toString(), type.getSelectedItem().toString(),
                                            vreme, eventsText.getText().toString(), subjects_for_date.get(s.getSelectedItemPosition()).getColor(), lista_datumi.size());
                                    values.add(event);
                                    Intent i = new Intent(getApplicationContext(), EventsActivity.class);
                                    i.putParcelableArrayListExtra("event lista", values);
                                    setResult(2, i);
                                    //setResult(2,i2);
                                    // finish The activity
                                    finish();
                                    Toasty.success(Events_add2.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                                } else {

                                    start.setError("Input time");
                                    clickTapButtonFalse(view);
                                   // Toasty.error(Events_add2.this, "Insert time!", Toast.LENGTH_SHORT).show();


                                    //Snackbar.make(view, "Insert time!", Snackbar.LENGTH_LONG)
                                    //      .setAction("Action", null).show();
                                }

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
                        if (!rect.contains(v.getLeft() + (int) event1.getX(), v.getTop() + (int) event1.getY())) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void showDialogOnStartClick(){


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DIALOG_ID);
                InputMethodManager mgr =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(start.getWindowToken(),0);
                startClick=1;

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
    /////////////////////////////////////////function for animation of button///////////////////////////////////////
    public void didTapButton(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
        animation1.setStartOffset(450);
        animation1.setDuration(400);
       // animation1.setFillAfter(true);
        view.startAnimation(animation1);



    }
    public void didTapColorpicker(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
      // animation1.setStartOffset(450);
        animation1.setDuration(400);
       // animation1.setFillAfter(true);
        view.startAnimation(animation1);



    }
    public void clickTapButtonFalse(View view) {
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
       // final Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
        //animation1.setStartOffset(5000);
        animation1.setDuration(550);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }

    //-----------------------------------------------------------------------------------------------------------//
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
            }

        }
    };


}
