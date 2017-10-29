package com.example.ranzo1.student_app.mainActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranzo1.student_app.adapters.EventAdd_adapter;
import com.example.ranzo1.student_app.add_and_update.AddTimeTable2;
import com.example.ranzo1.student_app.add_and_update.EditYourNote_Activity;
import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.objects.Subject;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class NotesActivity extends AppCompatActivity {

    private List<Subject> notes = new ArrayList<>();
    private List<Subject> values = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();

    private List<Subject>notesPreview=new ArrayList<>();

    private Spinner s;
    private CardView card;
    private EventAdd_adapter.Notes_Adapter adapter5;
    private GridView listViewNotes;
    private int position=-1;
    private BoomMenuButton bmb;

    private ArrayList<String> selectionListSpinner=new ArrayList<>();





    private View mBackgroundContainer;

    private  RelativeLayout emptyState;
    private TextView emptyTitle,emptySubtitle;
    DatabaseHendler db;



    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        db=DatabaseHendler.getInstance(this);

        notes=db.getAllNotes();
        subjects = getIntent().getParcelableArrayListExtra("listaPredmeta");
        subjects.size();

        listViewNotes = (GridView) findViewById(R.id.notes_activity);
        adapter5 = new EventAdd_adapter.Notes_Adapter(getApplicationContext(), (ArrayList<Subject>) notes);
        listViewNotes.setAdapter(adapter5);

        actionBar=getSupportActionBar();


         emptyState =(RelativeLayout)findViewById(R.id.relativeLayout7);
         emptyTitle=(TextView)findViewById(R.id.textView3) ;
         emptySubtitle=(TextView)findViewById(R.id.textView4);
         s=(Spinner)findViewById(R.id.spinner);
        card=(CardView)findViewById(R.id.card);




        if(notes.size()==0)
        {
            actionBar.setTitle("You don't have any notes :(");
        }
        else if(notes.size()==1) {
            actionBar.setTitle(String.valueOf("You have "+notes.size()) + " note!");
        }else{
            actionBar.setTitle(String.valueOf("You have "+notes.size()) + " notes!");

        }
        //mBackgroundContainer = findViewById(R.id.content_notes);


        if(!notes.isEmpty()){

            emptyState.setVisibility(View.INVISIBLE);
            emptyTitle.setVisibility(View.INVISIBLE);
            emptySubtitle.setVisibility(View.INVISIBLE);


        }

        selectionListSpinner.add("ALL NOTES");
        selectionListSpinner.add("OTHER");

        subjects = getIntent().getParcelableArrayListExtra("listaPredmeta");
        for (int i = 0; i < subjects.size(); i++) {
            selectionListSpinner.add(subjects.get(i).getShortName());
        }

        bmb=(BoomMenuButton)findViewById(R.id.bmb1);
        didTapButton(bmb);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(NotesActivity.this, android.R.layout.simple_list_item_1, selectionListSpinner);
        myAdapter2.setDropDownViewResource((android.R.layout.simple_expandable_list_item_1));
        s.setAdapter(myAdapter2);



        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(s.getSelectedItem().toString().equals("ALL NOTES")){



                    notes=db.getAllNotes();

                    //adapter5 = new EventAdd_adapter.Notes_Adapter(getApplicationContext(), (ArrayList<Subject>) notes);
                    //listViewNotes.setAdapter(adapter5);

                    if(notes.isEmpty()){

                        emptyState.setVisibility(View.VISIBLE);
                        emptyTitle.setVisibility(View.VISIBLE);
                        emptySubtitle.setVisibility(View.VISIBLE);


                    }else{
                        emptyState.setVisibility(View.INVISIBLE);
                        emptyTitle.setVisibility(View.INVISIBLE);
                        emptySubtitle.setVisibility(View.INVISIBLE);

                    }

                    adapter5 = new EventAdd_adapter.Notes_Adapter(getApplicationContext(), (ArrayList<Subject>) notes);
                    listViewNotes.setAdapter(adapter5);


                }else if(s.getSelectedItem().toString().equals("OTHER")){

                    notes=db.getAllNotes();

                    ArrayList<Subject> notesTemp=new ArrayList<Subject>();


                    int size=notes.size();

                    for(int i=0;i<size;i++){

                        if(notes.get(i).getShortName().equals("OTHER")){

                            notesTemp.add(notes.get(i));

                        }



                    }

                    notes=notesTemp;

                    if(notes.isEmpty()){

                        emptyState.setVisibility(View.VISIBLE);
                        emptyTitle.setVisibility(View.VISIBLE);
                        emptySubtitle.setVisibility(View.VISIBLE);


                    }else{
                        emptyState.setVisibility(View.INVISIBLE);
                        emptyTitle.setVisibility(View.INVISIBLE);
                        emptySubtitle.setVisibility(View.INVISIBLE);

                    }

                    adapter5 = new EventAdd_adapter.Notes_Adapter(getApplicationContext(), (ArrayList<Subject>) notes);
                    listViewNotes.setAdapter(adapter5);

                }else {

                    notes=db.getAllNotes();

                    int size=notes.size();

                    ArrayList<Subject> notesTemp=new ArrayList<Subject>();

                    for (int i=0;i<size;i++){

                        if(notes.get(i).getShortName().equals(s.getSelectedItem().toString())){

                            notesTemp.add(notes.get(i));



                        }


                    }

                    notes=notesTemp;



                    if(notes.isEmpty()){

                        emptyState.setVisibility(View.VISIBLE);
                        emptyTitle.setVisibility(View.VISIBLE);
                        emptySubtitle.setVisibility(View.VISIBLE);


                    }else{
                        emptyState.setVisibility(View.INVISIBLE);
                        emptyTitle.setVisibility(View.INVISIBLE);
                        emptySubtitle.setVisibility(View.INVISIBLE);

                    }

                    adapter5 = new EventAdd_adapter.Notes_Adapter(getApplicationContext(), (ArrayList<Subject>) notes);
                    listViewNotes.setAdapter(adapter5);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Intent i = new Intent(getApplicationContext(),EditYourNote_Activity.class);
                ArrayList<String> valuesNotes = new ArrayList<>();

                valuesNotes.add( notes.get(position).getName());
                valuesNotes.add( notes.get(position).getShortName());
                valuesNotes.add( notes.get(position).getProfessor());
                valuesNotes.add( notes.get(position).getProfEmail());


                //0-name,1-skracen naziv,2-sadrzaj-beleske,3-naslov

                i.putStringArrayListExtra("ValuesNotes", valuesNotes);
                i.putParcelableArrayListExtra("listaPredmeta", (ArrayList<? extends Parcelable>) subjects);
                i.putExtra("position",position);
                i.putExtra("other",valuesNotes.get(1));
                i.putExtra("color",notes.get(position).getColor());
                i.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) notes);
                i.putExtra("old_id",notes.get(position).getId());
                i.putExtra("check","note");


                startActivityForResult(i, 2);

            }



        });






        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            if (i == 0) {
                TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                        .normalImageRes(R.drawable.ic_post_it)
                        .shadowEffect(true)
                        .imagePadding(new Rect(50, 50, 50, 50))
                        .normalTextColor(Color.BLACK)
                        .highlightedTextColor(Color.BLACK)
                        .normalColor(Color.parseColor("#a8c282"))
                        .highlightedColor(Color.WHITE)
                        .normalText("Create new note")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        String note = "note";
                                        Intent i = new Intent(getApplicationContext(), EditYourNote_Activity.class);
                                        i.putParcelableArrayListExtra("listaPredmeta", (ArrayList<? extends Parcelable>) subjects);
                                        i.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) notes);
                                        i.putExtra("check", note);
                                        startActivityForResult(i, 2);
                                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                                    }
                                }, 500);

                            }
                        });
                bmb.addBuilder(builder);
            } else if (i == 1) {
                TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                        .normalImageRes(R.drawable.ic_checklist)
                        .shadowEffect(true)
                        .imagePadding(new Rect(55, 55, 55, 55))
                        .normalTextColor(Color.BLACK)
                        .highlightedTextColor(Color.BLACK)
                        .normalColor(Color.parseColor("#a8c282"))
                        .highlightedColor(Color.WHITE)
                        .normalText("Create new checklist")
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {



                                     /*String checkList= "checkList";
                                     Intent i = new Intent(getApplicationContext(),EditYourNote_Activity.class);
                                  i.putParcelableArrayListExtra("listaPredmeta", (ArrayList<? extends Parcelable>) subjects);
                                  i.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) notes);
                                  i.putExtra("check",checkList);
                                 startActivityForResult(i, 2);
                                 overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/
                                        Toasty.info(NotesActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();


                            }
                        });
                bmb.addBuilder(builder);

            }
        }







        listViewNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(NotesActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                db.deleteNote(notes.get(position).getId());
                                notes.remove(position);

                                adapter5.notifyDataSetChanged();
                                Toasty.success(NotesActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();

                                if(notes.size()==0)
                                {
                                    actionBar.setTitle("You don't have any notes :(");
                                }
                                else if(notes.size()==1) {
                                    actionBar.setTitle(String.valueOf("You have "+notes.size()) + " note!");
                                }else{
                                    actionBar.setTitle(String.valueOf("You have "+notes.size()) + " notes!");

                                }


                                if(notes.isEmpty()){

                                    emptyState.setVisibility(View.VISIBLE);
                                    emptyTitle.setVisibility(View.VISIBLE);
                                    emptySubtitle.setVisibility(View.VISIBLE);


                                }

                            }
                        })
                        .setPositiveButton("No", null)
                        .show();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                return true;
            }
        });




        //clickTapButton(btn);

    }


    public void didTapButton(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
        animation1.setStartOffset(450);
        animation1.setDuration(400);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {

            try {

                if (data.getParcelableArrayListExtra("notes") != null) {

                    values = data.getParcelableArrayListExtra("notes");
                    position=data.getIntExtra("position",-1);

                    if(position==-1)
                    {
                        notes=db.getAllNotes();
                        db.addNote(values.get(0));
                        notes=db.getAllNotes();
                       // notes.add(values.get(0));
                        s.setSelection(0);
                        adapter5 = new EventAdd_adapter.Notes_Adapter(getApplicationContext(), (ArrayList<Subject>) notes);
                        listViewNotes.setAdapter(adapter5);

                        if(notes.size()==0)
                        {
                            actionBar.setTitle("You don't have any notes :(");
                        }
                        else if(notes.size()==1) {
                            actionBar.setTitle(String.valueOf("You have "+notes.size()) + " note!");

                            emptyState.setVisibility(View.INVISIBLE);
                            emptyTitle.setVisibility(View.INVISIBLE);
                            emptySubtitle.setVisibility(View.INVISIBLE);
                        }else{
                            actionBar.setTitle(String.valueOf("You have "+notes.size()) + " notes!");



                                emptyState.setVisibility(View.INVISIBLE);
                                emptyTitle.setVisibility(View.INVISIBLE);
                                emptySubtitle.setVisibility(View.INVISIBLE);




                        }
                    }
                    else
                    {
                        notes=db.getAllNotes();
                        db.deleteNote(notes.get(position).getId());
                        db.addNote(values.get(0));
                        notes=db.getAllNotes();

                        s.setSelection(0);

                        adapter5 = new EventAdd_adapter.Notes_Adapter(getApplicationContext(), (ArrayList<Subject>) notes);

                       // notes.remove(position);
                        //notes.add(position,values.get(0));
                        listViewNotes.setAdapter(adapter5);
                    }



                    // notes.add(values.get(0));

                    //listViewNotes.setAdapter(adapter5);

                }


            } catch (Exception e) {
                Log.i("GRESKA", e.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}




















