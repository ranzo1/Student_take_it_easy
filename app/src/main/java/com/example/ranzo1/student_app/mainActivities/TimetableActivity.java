package com.example.ranzo1.student_app.mainActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranzo1.student_app.add_and_update.AddTimeTable;
import com.example.ranzo1.student_app.add_and_update.AddTimeTable2;
import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.objects.Subject;
import com.example.ranzo1.student_app.objects.TimeTableObject;
import com.example.ranzo1.student_app.adapters.Timetable_adapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class TimetableActivity extends AppCompatActivity {

    private List<Subject> subjects = new ArrayList<>();
    private int position1 = -1;
    private ListView monday, tuesday, wednesday, thursday, friday;

    private List<TimeTableObject> timeTableObjects = new ArrayList<>();
    private List<TimeTableObject> databaseTimeTable=new ArrayList<>();
    private List<TimeTableObject> eventsMonday = new ArrayList<>();
    private List<TimeTableObject> eventsTuesday = new ArrayList<>();
    private List<TimeTableObject> eventsWednesday = new ArrayList<>();
    private List<TimeTableObject> eventsThursday = new ArrayList<>();
    private List<TimeTableObject> eventsFriday = new ArrayList<>();


    private Timetable_adapter Madapter;
    private TextView textView;
    private Timetable_adapter TUadapter;
    private Timetable_adapter Wadapter;
    private Timetable_adapter Thadapter;
    private Timetable_adapter Fadapter;
    private FloatingActionButton btn;
    DatabaseHendler db;

    private RelativeLayout emptyState;
    private TextView emptyTitle,emptySubtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //////////////////////////list of subject///////////////////////////////
        subjects = getIntent().getParcelableArrayListExtra("listaPredmeta");
        //-------------------------------------------------------------------//


        db=DatabaseHendler.getInstance(this);




        /////////////////////////findview by id//////////////////////////////
        monday = (ListView) findViewById(R.id.monday_listView);
        tuesday = (ListView) findViewById(R.id.tuesday_listView);
        wednesday = (ListView) findViewById(R.id.wednesday_listView);
        thursday = (ListView) findViewById(R.id.thursday_listView);
        friday = (ListView) findViewById(R.id.friday_listView);

        emptyState =(RelativeLayout)findViewById(R.id.relativeLayout7);
        emptyTitle=(TextView)findViewById(R.id.textView3) ;
        emptySubtitle=(TextView)findViewById(R.id.textView4);
        //-------------------------------------------------------------------//
        /////////////////////////adappters for list////////////////////////////
        Madapter = new Timetable_adapter(getApplicationContext(), eventsMonday);
        monday.setAdapter(Madapter);

        TUadapter = new Timetable_adapter(getApplicationContext(), eventsTuesday);
        tuesday.setAdapter(TUadapter);

        Wadapter = new Timetable_adapter(getApplicationContext(), eventsWednesday);
        wednesday.setAdapter(Wadapter);

        Thadapter = new Timetable_adapter(getApplicationContext(), eventsThursday);
        thursday.setAdapter(Thadapter);

        Fadapter = new Timetable_adapter(getApplicationContext(), eventsFriday);
        friday.setAdapter(Fadapter);



        //db.addTimetable(new TimeTableObject("","add","as","faf","adsad",123,0));
        databaseTimeTable=db.getAllTimeTable();
        // db.deleteTimeTableObject(0);

        int i= databaseTimeTable.size();


        if(!databaseTimeTable.isEmpty())
        {
            organizeTimeTable();
            emptyState.setVisibility(View.INVISIBLE);
            emptyTitle.setVisibility(View.INVISIBLE);
            emptySubtitle.setVisibility(View.INVISIBLE);

        }else{

                emptyState.setVisibility(View.VISIBLE);
                emptyTitle.setVisibility(View.VISIBLE);
                emptySubtitle.setVisibility(View.VISIBLE);



        }

        //------------------------------------------------------------------------//

        /////////////update from list//////////////////
        onClickList(monday,eventsMonday);
        onClickList(tuesday,eventsTuesday);
        onClickList(wednesday,eventsWednesday);
        onClickList(thursday,eventsThursday);
        onClickList(friday,eventsFriday);
        //-------------------------------------------------------------------//

        //////////delete from list/////////////////////////
        onLongClickList(monday,eventsMonday,Madapter);
        onLongClickList(tuesday,eventsTuesday,TUadapter);
        onLongClickList(wednesday,eventsWednesday,Wadapter);
        onLongClickList(thursday,eventsThursday,Thadapter);
        onLongClickList(friday,eventsFriday,Fadapter);
        //-------------------------------------------------------------------//



        btn=(FloatingActionButton) findViewById(R.id.button5) ;
        didTapButton(btn);
        ////////////////////////open activity Add TimeTable,send data////////////////////////////

        //------------------------------------------------------------------------------------------------------//
        //clickTapButton(btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddTimeTable2.class);
                intent.putParcelableArrayListExtra("listaPredmeta", (ArrayList<? extends Parcelable>) subjects);
                intent.putParcelableArrayListExtra("databaseTimeTable", (ArrayList<? extends Parcelable>) databaseTimeTable);
                startActivityForResult(intent, 2);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
    }
    //////////////////function for animation//////////////////////////////
    public void didTapButton(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
        animation1.setStartOffset(450);
        animation1.setDuration(400);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);



    }

    /////////////////////////function for deliting objects from timetable//////////////////////////////////
    public void onLongClickList(final ListView monday, final List<TimeTableObject> eventsMonday, final Timetable_adapter Madapter){
        monday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(TimetableActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this event?")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                for (int i = 0; i < databaseTimeTable.size(); i++)
                                {
                                    if (databaseTimeTable.get(i).getId() == eventsMonday.get(position).getId())
                                    {
                                        db.deleteTimeTableObject(databaseTimeTable.get(i).getId());
                                        databaseTimeTable.remove(i);

                                    }
                                }



                                eventsMonday.remove(position);
                                Madapter.notifyDataSetChanged();
                                Toasty.success(TimetableActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();

                                if(databaseTimeTable.isEmpty()){


                                    emptyState.setVisibility(View.VISIBLE);
                                    emptyTitle.setVisibility(View.VISIBLE);
                                    emptySubtitle.setVisibility(View.VISIBLE);

                                }

                            }
                        })
                        .setPositiveButton("No", null)
                        .show();

                return true;
            }
        });
        //---------------------------------------------------------------------------------------------------------------------//
    }
    /////////////////////////open edit activity AddTimeTable/////////////////////////////////////
    public void onClickList(ListView monday, final List<TimeTableObject> eventsMonday){

        monday.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), AddTimeTable.class);
                List<TimeTableObject> values = new ArrayList<>();


                TimeTableObject o = new TimeTableObject(eventsMonday.get(position).getDay(), eventsMonday.get(position).getShortName(),
                        eventsMonday.get(position).getStart(), eventsMonday.get(position).getEnd(),
                        eventsMonday.get(position).getAdditional(), eventsMonday.get(position).getColor(),eventsMonday.get(position).getId());

                values.add(o);

                i.putParcelableArrayListExtra("Dogadjaji", (ArrayList<? extends Parcelable>) values);
                i.putExtra("position", position);
                i.putParcelableArrayListExtra("databaseTimeTable", (ArrayList<? extends Parcelable>) databaseTimeTable);
                startActivityForResult(i, 2);

            }

        });

    }

    //----------------------------------------------------------------------------------------------------------------------//

    //////////////////////function for sorting subjects in timetable//////////////////////////////
    public void listSort(List<TimeTableObject> raspored) {


        if(raspored.size()>1) {

            for (int i = 0; i < raspored.size(); i++) {

                for (int j = i + 1; j < raspored.size(); j++) {
                    if ((Integer.valueOf(raspored.get(i).getStart().substring(0, 2)) > Integer.valueOf(raspored.get(j).getStart().substring(0, 2))))
                    {
                        TimeTableObject temp = raspored.get(i);
                        raspored.set(i, raspored.get(j));
                        raspored.set(j, temp);

                    }
                    else if ((Integer.valueOf(raspored.get(i).getStart().substring(0, 2)) == Integer.valueOf(raspored.get(j).getStart().substring(0, 2)))) {
                        if ((Integer.valueOf(raspored.get(i).getEnd().substring(0, 2)) > Integer.valueOf(raspored.get(j).getEnd().substring(0, 2)))) {
                            TimeTableObject temp = raspored.get(i);
                            raspored.set(i, raspored.get(j));
                            raspored.set(j, temp);

                        }
                    }
                }

            }
        }





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

                            Intent intent = new Intent(getApplicationContext(), AddTimeTable2.class);
                            intent.putParcelableArrayListExtra("listaPredmeta", (ArrayList<? extends Parcelable>) subjects);
                            intent.putParcelableArrayListExtra("databaseTimeTable", (ArrayList<? extends Parcelable>) databaseTimeTable);
                            startActivityForResult(intent, 2);
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

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

    //funkcija za primanje podataka
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        didTapButton(btn);


        if (requestCode == 2) {

            try {
                if (data.getParcelableArrayListExtra("listaDogadjaja") != null) {

                    timeTableObjects = data.getParcelableArrayListExtra("listaDogadjaja");
                    position1 = data.getIntExtra("position", -1);



                    if (position1 == -1) {
                        db.addTimetable(timeTableObjects.get(0));
                        databaseTimeTable.add(timeTableObjects.get(0));

                        if (timeTableObjects.get(0).getDay().equals("Monday")) {
                            eventsMonday.add(timeTableObjects.get(0));
                            listSort(eventsMonday);
                            monday.setAdapter(Madapter);

                        } else if (timeTableObjects.get(0).getDay().equals("Tuesday")) {
                            eventsTuesday.add(timeTableObjects.get(0));
                            listSort(eventsTuesday);
                            tuesday.setAdapter(TUadapter);

                        } else if (timeTableObjects.get(0).getDay().equals("Wednesday")) {
                            eventsWednesday.add(timeTableObjects.get(0));
                            listSort(eventsWednesday);
                            wednesday.setAdapter(Wadapter);

                        } else if (timeTableObjects.get(0).getDay().equals("Thursday")) {
                            eventsThursday.add(timeTableObjects.get(0));
                            listSort(eventsThursday);
                            thursday.setAdapter(Thadapter);

                        } else if (timeTableObjects.get(0).getDay().equals("Friday")) {
                            eventsFriday.add(timeTableObjects.get(0));
                            listSort(eventsFriday);
                            friday.setAdapter(Fadapter);
                        }

                        if(!databaseTimeTable.isEmpty()){

                            emptyState.setVisibility(View.INVISIBLE);
                            emptyTitle.setVisibility(View.INVISIBLE);
                            emptySubtitle.setVisibility(View.INVISIBLE);

                        }


                    } else {

                        db.deleteTimeTableObject(timeTableObjects.get(0).getId());
                        db.addTimetable(timeTableObjects.get(0));

                        databaseTimeTable.remove(position1);
                        databaseTimeTable.add(position1,timeTableObjects.get(0));


                        if (timeTableObjects.get(0).getDay().equals("Monday")) {

                            eventsMonday.remove(position1);
                            eventsMonday.add(position1, timeTableObjects.get(0));
                            listSort(eventsMonday);
                            monday.setAdapter(Madapter);

                        } else if (timeTableObjects.get(0).getDay().equals("Tuesday")) {

                            eventsTuesday.remove(position1);
                            eventsTuesday.add(position1, timeTableObjects.get(0));
                            listSort(eventsTuesday);
                            tuesday.setAdapter(TUadapter);

                        } else if (timeTableObjects.get(0).getDay().equals("Wednesday")) {
                            eventsWednesday.remove(position1);
                            eventsWednesday.add(position1, timeTableObjects.get(0));
                            listSort(eventsWednesday);
                            wednesday.setAdapter(Wadapter);

                        } else if (timeTableObjects.get(0).getDay().equals("Thursday")) {
                            eventsThursday.remove(position1);
                            eventsThursday.add(position1, timeTableObjects.get(0));
                            listSort(eventsThursday);
                            thursday.setAdapter(Thadapter);

                        } else if (timeTableObjects.get(0).getDay().equals("Friday")) {
                            eventsFriday.remove(position1);
                            eventsFriday.add(position1, timeTableObjects.get(0));
                            listSort(eventsFriday);
                            friday.setAdapter(Fadapter);
                        }
                    }

                }





            } catch (Exception e) {
                Log.i("GRESKA", e.getMessage());
            }
        }
    }
    public void organizeTimeTable()
    {

        for(int i=0;i<databaseTimeTable.size();i++)
        {

            if (databaseTimeTable.get(i).getDay().equals("Monday")) {
                eventsMonday.add(databaseTimeTable.get(i));
                listSort(eventsMonday);
                monday.setAdapter(Madapter);

            } else if (databaseTimeTable.get(i).getDay().equals("Tuesday")) {
                eventsTuesday.add(databaseTimeTable.get(i));
                listSort(eventsTuesday);
                tuesday.setAdapter(TUadapter);

            } else if (databaseTimeTable.get(i).getDay().equals("Wednesday")) {
                eventsWednesday.add(databaseTimeTable.get(i));
                listSort(eventsWednesday);
                wednesday.setAdapter(Wadapter);

            } else if (databaseTimeTable.get(i).getDay().equals("Thursday")) {
                eventsThursday.add(databaseTimeTable.get(i));
                listSort(eventsThursday);
                thursday.setAdapter(Thadapter);

            } else if (databaseTimeTable.get(i).getDay().equals("Friday")) {
                eventsFriday.add(databaseTimeTable.get(i));
                listSort(eventsFriday);
                friday.setAdapter(Fadapter);
            }

        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}

