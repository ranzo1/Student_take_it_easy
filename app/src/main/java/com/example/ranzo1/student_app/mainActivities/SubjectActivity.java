package com.example.ranzo1.student_app.mainActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranzo1.student_app.add_and_update.AddSubjectActivity;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.objects.GradeObject;
import com.example.ranzo1.student_app.objects.Subject;
import com.example.ranzo1.student_app.adapters.SubjectAdapter;
import com.example.ranzo1.student_app.objects.TimeTableObject;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class SubjectActivity extends AppCompatActivity implements SubjectAdapter.ItemClickListener,SubjectAdapter.OnItemLongClickListener {



    private RecyclerView lvSubjects;
    private SubjectAdapter adapter;
    private ArrayList<Subject> subjects=new ArrayList<>();
    private FloatingActionButton btn;
    DatabaseHendler db;
    ActionBar actionBar;
    private List<TimeTableObject> timeTableObject_database=new ArrayList<>();
    private List<Event>event_database=new ArrayList<>();
    private List<Subject>notes_database=new ArrayList<>();
    private List<GradeObject>grades_database=new ArrayList<>();
    public final static String STRING_VALUES_KEY = "values";

    private RelativeLayout emptyState;
    private TextView emptyTitle,emptySubtitle;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        db =DatabaseHendler.getInstance(this);
        subjects= (ArrayList<Subject>) db.getAllSubjects();
        timeTableObject_database=db.getAllTimeTable();
        event_database=db.getAllEvents();
        notes_database=db.getAllNotes();
        grades_database=db.getAllGrades();

        btn=(FloatingActionButton) findViewById(R.id.button4);

        emptyState =(RelativeLayout)findViewById(R.id.relativeLayout7);
        emptyTitle=(TextView)findViewById(R.id.textView3) ;
        emptySubtitle=(TextView)findViewById(R.id.textView4);

        didTapButton(btn);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout123);
        ////disable drawer//////
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        if(subjects.size()==0)
        {
            actionBar.setTitle("You don't have any subjects :(");
        }
        else if(subjects.size()==1) {
            actionBar.setTitle(String.valueOf("You have "+subjects.size()) + " subject!");
        }else{
            actionBar.setTitle(String.valueOf("You have "+subjects.size()) + " subjects!");

        }

        if(!subjects.isEmpty()){

            emptyState.setVisibility(View.INVISIBLE);
            emptyTitle.setVisibility(View.INVISIBLE);
            emptySubtitle.setVisibility(View.INVISIBLE);


        }


        // set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.ListViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new SubjectAdapter(this, subjects);
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);

        recyclerView.setAdapter(adapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGetMessage = new Intent(getApplicationContext(), AddSubjectActivity.class);
                intentGetMessage.putParcelableArrayListExtra("subjectDatabase",subjects);
                startActivityForResult(intentGetMessage, 2);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }
        });





    }








    public void clickTapButtonFalse(View view) {
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        final Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);

        //animation1.setStartOffset(5000);
        animation1.setDuration(550);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }


    public void didTapButton(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
        animation1.setStartOffset(450);
        animation1.setDuration(400);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);



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

                            Intent intentGetMessage = new Intent(getApplicationContext(), AddSubjectActivity.class);
                            intentGetMessage.putParcelableArrayListExtra("subjectDatabase",subjects);
                            startActivityForResult(intentGetMessage, 2);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        didTapButton(btn);
        //db =DatabaseHendler.getInstance(this);

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {

            try {

                if (data.getStringArrayListExtra(STRING_VALUES_KEY) != null) {

                    ArrayList<Subject> values = data.getParcelableArrayListExtra(STRING_VALUES_KEY);


                    Subject s=values.get(0);

                    db.addSubject(s);

                    subjects.add(s);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new SubjectAdapter(this, subjects);
                    adapter.setClickListener(this);
                    adapter.setLongClickListener(this);
                    recyclerView.setAdapter(adapter);




                    if(subjects.size()==0)
                    {
                        actionBar.setTitle("You don't have any subjects :(");
                    }
                    else if(subjects.size()==1) {
                        actionBar.setTitle(String.valueOf("You have "+subjects.size()) + " subject!");
                    }else{
                        actionBar.setTitle(String.valueOf("You have "+subjects.size()) + " subjects!");

                    }

                    if(!subjects.isEmpty()) {

                        emptyState.setVisibility(View.INVISIBLE);
                        emptyTitle.setVisibility(View.INVISIBLE);
                        emptySubtitle.setVisibility(View.INVISIBLE);

                    }

                }


            } catch (Exception e) {
                Log.i("GRESKA", e.getMessage());
            }
        }else if(requestCode==4){

            try {

                if(data.getParcelableArrayListExtra("editSubject")!=null) {


                    ArrayList<Subject> values = data.getParcelableArrayListExtra("editSubject");
                    int position = data.getIntExtra("position", -1);

                    Subject s=values.get(0);

                    db.deleteSubject(s.getId());
                    db.addSubject(s);

                    List<Subject>subject_database=db.getAllSubjects();


                    /////////updating subjects in odher activities/////////////////////
                    updateTimetable(subject_database,timeTableObject_database);
                    updateEvents(subject_database,event_database);
                    updateNotes(subject_database,notes_database);
                    updateGrades(subject_database,grades_database);


                    subjects.remove(subjects.get(position));
                    subjects.add(position,s);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new SubjectAdapter(this, subjects);
                    adapter.setClickListener(this);
                    adapter.setLongClickListener(this);
                    recyclerView.setAdapter(adapter);

                }

            }catch (Exception e) {
                Log.i("GRESKA", e.getMessage());
            }





        }
    }

    public void deleteUpdateTimetable(List<TimeTableObject> timeTableObject_database,int position)
    {
        for (int i =0;i<timeTableObject_database.size();i++)
        {

            if(subjects.get(position).getShortName().equals(timeTableObject_database.get(i).getShortName()))
            {

                db.deleteTimeTableObject(timeTableObject_database.get(i).getId());


            }

        }
    }

    public void deleteUpdateEvent(List<Event>event_database,int position){

        for (int i =0;i<event_database.size();i++)
        {
            String comparation= (String) event_database.get(i).getData();

            String subComparation=comparation.substring(5,comparation.indexOf(" ")-1);


            if(subjects.get(position).getShortName().equals(subComparation))
            {

                db.deleteEvent(event_database.get(i));


            }

        }

    }

    public void deleteUpdateNote(List<Subject>notes_database,int position){

        for (int i =0;i<notes_database.size();i++)
        {

            if(subjects.get(position).getShortName().equals(notes_database.get(i).getShortName()))
            {

                db.deleteNote(notes_database.get(i).getId());


            }

        }
    }

    public void deleteUpdateGrades(List <GradeObject> grades_database,int position){

        for (int i =0;i<grades_database.size();i++)
        {

            if(subjects.get(position).getShortName().equals(grades_database.get(i).getShortName()))
            {

                db.deleteGrade(grades_database.get(i).getId());


            }

        }

    }

    public void updateTimetable(List<Subject> subject_database ,List<TimeTableObject> timeTableObject_database){

        for(int i =0;i<timeTableObject_database.size();i++)
        {


            for(int j = 0; j<subject_database.size(); j++)
            {
                if(timeTableObject_database.get(i).getShortName().equals(subjects.get(j).getShortName())){

                    TimeTableObject t =timeTableObject_database.get(i);
                    db.deleteTimeTableObject(t.getId());
                    t.setColor(subject_database.get(j).getColor());
                    t.setShortName(subject_database.get(j).getShortName());
                    db.addTimetable(t);



                }
            }

        }
    }

    public void updateEvents(List<Subject> subject_database ,List<Event> event_database){

        for (int i =0;i<event_database.size();i++)
        {

            for (int j =0;j<subject_database.size();j++)
            {

               // String subComparation=null;

                String comparation= (String) event_database.get(i).getData();


                String subComparation=comparation.substring(5,comparation.indexOf(" ")-1);


                    if(subComparation.equals(subjects.get(j).getShortName())){

                        Event e=event_database.get(i);
                        db.deleteEvent(e);
                        String oldData=(String)e.getData();
                        String newData=oldData.replaceAll(subComparation,subject_database.get(j).getShortName());
                        Event e2=new Event(subject_database.get(j).getColor(),e.getTimeInMillis(),newData);
                        db.addEvent(e2);

                    }

            }

        }


    }

    public void updateNotes(List<Subject>subject_database,List<Subject>notes_database)
    {

        for(int i =0;i<notes_database.size();i++)
        {


            for(int j = 0; j<subject_database.size(); j++)
            {
                if(notes_database.get(i).getShortName().equals(subjects.get(j).getShortName())){

                    Subject note =notes_database.get(i);
                    db.deleteNote(note.getId());
                    note.setColor(subject_database.get(j).getColor());
                    note.setShortName(subject_database.get(j).getShortName());
                    note.setName(subject_database.get(j).getName());
                    db.addNote(note);



                }
            }

        }

    }
    public void updateGrades(List<Subject>subject_database,List<GradeObject>grades_database){

        for(int i =0;i<grades_database.size();i++)
        {


            for(int j = 0; j<subject_database.size(); j++)
            {
                if(grades_database.get(i).getShortName().equals(subjects.get(j).getShortName())){

                    GradeObject grade =grades_database.get(i);
                    db.deleteGrade(grade.getId());
                    //grade.setColor(subject_database.get(j).getColor());
                    grade.setShortName(subject_database.get(j).getShortName());

                    db.addGrade(grade);



                }
            }

        }

    }





    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SubjectActivity.this,MActivity.class);
        intent.putParcelableArrayListExtra("listaPredmeta",  subjects);
        setResult(1,intent);

        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

//adapter on click
    @Override
    public void onItemClick(View view, int position) {

        Subject s=new Subject(subjects.get(position).getName(),subjects.get(position).getShortName(),subjects.get(position).getProfessor(),subjects.get(position).getProfEmail(),
                subjects.get(position).getAsisstent(),subjects.get(position).getAsistEmail(),subjects.get(position).getColor(),subjects.get(position).getProfCabinet(),subjects.get(position).getAsistCabinet(),
                subjects.get(position).getProfImage(),subjects.get(position).getAssistImage(),subjects.get(position).getId());
        Intent i=new Intent(getApplicationContext(),AddSubjectActivity.class);

        ArrayList<Subject> subj =new ArrayList<Subject>();
        subj.add(s);
        i.putParcelableArrayListExtra("editSubject",subj);
        i.putExtra("edit",1);
        i.putExtra("position",position);


        startActivityForResult(i,4);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public boolean onItemLongClicked(View view, final int position) {

        new AlertDialog.Builder(SubjectActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this subject?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        db.deleteSubject(subjects.get(position).getId());
                        deleteUpdateTimetable(timeTableObject_database,position);
                        deleteUpdateNote(notes_database,position);
                        deleteUpdateEvent(event_database,position);
                        deleteUpdateGrades(grades_database,position);

                        //List<Subject>subject_database=db.getAllSubjects();

                        subjects.remove(position);
                        adapter.notifyDataSetChanged();

                        // subjects= (ArrayList<Subject>) db.getAllSubjects();
                        Toasty.success(SubjectActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                        if(subjects.size()==0)
                        {
                            actionBar.setTitle("You don't have any subjects :(");
                        }
                        else if(subjects.size()==1) {
                            actionBar.setTitle(String.valueOf("You have "+subjects.size()) + " subject!");
                        }else{
                            actionBar.setTitle(String.valueOf("You have "+subjects.size()) + " subjects!");

                        }

                        if(subjects.isEmpty()){

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
}




