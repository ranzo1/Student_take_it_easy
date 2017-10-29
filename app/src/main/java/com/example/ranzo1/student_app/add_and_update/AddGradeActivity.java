package com.example.ranzo1.student_app.add_and_update;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.adapters.GradesAdapter2;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.objects.GradeObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AddGradeActivity extends AppCompatActivity {

    private ListView listViewGrades;
    private GradesAdapter2 adapterGrades;
    private ArrayList<GradeObject> grades=new ArrayList<>();
    private ArrayList<GradeObject> gradesSpecific=new ArrayList<>();
    private EditText typeDialog,pointsDialog,additionalDialog;
    private Button btnDialog;
    private GradeObject g=new GradeObject();
    private int position;
    private String shortName;
    DatabaseHendler db;
    ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_grade);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }



        setTheme(R.style.AppTheme);



        //getWindow().setBackgroundDrawable(new ColorDrawable(0));
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.addGrade);

        final RelativeLayout emptyState =(RelativeLayout)findViewById(R.id.relativeLayout7);
        final TextView emptyTitle=(TextView)findViewById(R.id.textView3) ;
        final TextView emptySubtitle=(TextView)findViewById(R.id.textView4);

        didTapButton(btnAdd);


        shortName = getIntent().getStringExtra("name");
        int color = getIntent().getIntExtra("color", -1);
        position = getIntent().getIntExtra("position", -1);

        // GradeObject g = new GradeObject(shortName,"exam",33.3,0);
        // grades.add(g);
        db = DatabaseHendler.getInstance(this);
        grades = (ArrayList<GradeObject>) db.getAllGrades();


        //Setting list of gradesObject that are same short name as member of list view that we are clicked on in grades activity
        if(grades.size()!=0) {
            for (int i = 0; i < grades.size(); i++) {

                if (grades.get(i).getShortName().equals(shortName)) {

                    gradesSpecific.add(grades.get(i));
                }

            }
        }


        listViewGrades = (ListView) findViewById(R.id.listViewGradess);
        adapterGrades = new GradesAdapter2(getApplicationContext(), gradesSpecific);
        listViewGrades.setAdapter(adapterGrades);

        //for action bar title setup
        setTitle();


        if(!gradesSpecific.isEmpty()){

            emptyState.setVisibility(View.INVISIBLE);
            emptyTitle.setVisibility(View.INVISIBLE);
            emptySubtitle.setVisibility(View.INVISIBLE);


        }








      //  clickTapButton(btnAdd);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddGradeActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_grade_layout, null);

                typeDialog=(EditText) mView.findViewById(R.id.editTextType);
                pointsDialog=(EditText)mView.findViewById(R.id.editTextPoints);
                btnDialog=(Button)mView.findViewById(R.id.button8);
                additionalDialog=(EditText)mView.findViewById(R.id.editTexAdditional);


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                dialog.show();
                //dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                btnDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(typeDialog.getText().toString().isEmpty()) {

                           // Toasty.error(AddGradeActivity.this, "Type field is empty!", Toast.LENGTH_SHORT).show();
                            typeDialog.setHintTextColor(Color.rgb(242, 90, 90));

                        }if(pointsDialog.getText().toString().isEmpty()){

                           // Toasty.error(AddGradeActivity.this, "Points field is empty!", Toast.LENGTH_SHORT).show();
                            pointsDialog.setHintTextColor(Color.rgb(242, 90, 90));
                        }

                        else if(additionalDialog.getText().toString().isEmpty()){

                            int id=0;

                            if(grades.size()!=0) {

                                for (int j = 0; j < grades.size(); j++) {
                                    id = grades.get(j).getId();


                                }
                                id += 1;
                            }



                            g =new GradeObject(shortName,typeDialog.getText().toString(),
                                    Float.valueOf(pointsDialog.getText().toString()),"",0,id);
                            grades.add(g);
                            grades.get(grades.size()-1).setSumPoints(gradesSum());
                            gradesSpecific.add(g);
                            gradesSpecific.get(0).setSumPoints(gradesSum());
                            g.setSumPoints(gradesSum());
                            db.addGrade(g);
                            setTitle();
                            listViewGrades.setAdapter(adapterGrades);
                            Toasty.success(AddGradeActivity.this, "Successfully added!", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();


                            if(!gradesSpecific.isEmpty()){

                                emptyState.setVisibility(View.INVISIBLE);
                                emptyTitle.setVisibility(View.INVISIBLE);
                                emptySubtitle.setVisibility(View.INVISIBLE);


                            }

                        }else{


                            int id=0;

                            if(grades.size()!=0) {

                                for (int j = 0; j < grades.size(); j++) {
                                    id = grades.get(j).getId();


                                }
                                id += 1;
                            }



                            g =new GradeObject(shortName,typeDialog.getText().toString(),
                                    Float.valueOf(pointsDialog.getText().toString()),additionalDialog.getText().toString(),0,id);
                            grades.add(g);
                            grades.get(grades.size()-1).setSumPoints(gradesSum());
                            gradesSpecific.add(g);
                            gradesSpecific.get(0).setSumPoints(gradesSum());
                            g.setSumPoints(gradesSum());
                            db.addGrade(g);
                            setTitle();
                            listViewGrades.setAdapter(adapterGrades);
                            Toasty.success(AddGradeActivity.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            if(!gradesSpecific.isEmpty()){

                                emptyState.setVisibility(View.INVISIBLE);
                                emptyTitle.setVisibility(View.INVISIBLE);
                                emptySubtitle.setVisibility(View.INVISIBLE);


                            }

                        }


                    }
                });





            }
        });

        listViewGrades.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(AddGradeActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this test?")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                db.deleteGrade(gradesSpecific.get(position).getId());



                                //List<Subject>subject_database=db.getAllSubjects();

                                gradesSpecific.remove(position);
                                if(!gradesSpecific.isEmpty()){

                                    for (int i=0;i<gradesSpecific.size();i++){


                                        db.deleteGrade(gradesSpecific.get(i).getId());
                                        gradesSpecific.get(i).setSumPoints(gradesSum());
                                       db.addGrade(gradesSpecific.get(i));
                                    }

                                }
                                adapterGrades.notifyDataSetChanged();


                                Toasty.success(AddGradeActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                                //for action bar title setup
                                setTitle();

                                if(gradesSpecific.isEmpty()){

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

        listViewGrades.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddGradeActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_grade_layout, null);

                typeDialog = (EditText) mView.findViewById(R.id.editTextType);
                pointsDialog = (EditText) mView.findViewById(R.id.editTextPoints);
                btnDialog = (Button) mView.findViewById(R.id.button8);
                additionalDialog = (EditText) mView.findViewById(R.id.editTexAdditional);


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


                typeDialog.setText(gradesSpecific.get(position).getTypeOfExam());
                pointsDialog.setText(String.valueOf(gradesSpecific.get(position).getPoints()));
                additionalDialog.setText(gradesSpecific.get(position).getAdditional());


                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                dialog.show();
                //dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                btnDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (typeDialog.getText().toString().isEmpty()) {

                           // Toast.makeText(AddGradeActivity.this, "Type field is empty!", Toast.LENGTH_SHORT).show();
                            typeDialog.setHintTextColor(Color.rgb(242, 90, 90));

                        }if (pointsDialog.getText().toString().isEmpty()) {

                           // Toast.makeText(AddGradeActivity.this, "Points field is empty!", Toast.LENGTH_SHORT).show();
                            pointsDialog.setHintTextColor(Color.rgb(242, 90, 90));
                        } else if (additionalDialog.getText().toString().isEmpty()) {


                            g = new GradeObject(shortName, typeDialog.getText().toString(),//
                                    Float.valueOf(pointsDialog.getText().toString()), "", 0, gradesSpecific.get(position).getId());
                            //  grades.remove(position);
                            //  grades.add(position,g);
                            //  grades.get(grades.size()-1).setSumPoints(gradesSum());
                            gradesSpecific.remove(position);
                            gradesSpecific.add(position, g);
                            gradesSpecific.get(0).setSumPoints(gradesSum());
                            db.deleteGrade(gradesSpecific.get(position).getId());
                            g.setSumPoints(gradesSum());
                            db.addGrade(g);
                            if(!gradesSpecific.isEmpty()){

                                for (int i=0;i<gradesSpecific.size();i++){


                                    db.deleteGrade(gradesSpecific.get(i).getId());
                                    gradesSpecific.get(i).setSumPoints(gradesSum());
                                    db.addGrade(gradesSpecific.get(i));
                                }

                            }

                            //for action bar title setup
                            setTitle();
                            listViewGrades.setAdapter(adapterGrades);
                            Toasty.success(AddGradeActivity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();




                        } else if (!additionalDialog.getText().toString().isEmpty()) {

                            g = new GradeObject(shortName, typeDialog.getText().toString(),//
                                    Float.valueOf(pointsDialog.getText().toString()), additionalDialog.getText().toString(), 0, gradesSpecific.get(position).getId());
                            //grades.remove(position);
                            // grades.add(position,g);
                            // grades.get(grades.size()-1).setSumPoints(gradesSum());
                            gradesSpecific.remove(position);
                            gradesSpecific.add(position, g);
                            gradesSpecific.get(0).setSumPoints(gradesSum());
                            db.deleteGrade(gradesSpecific.get(position).getId());
                            g.setSumPoints(gradesSum());
                            db.addGrade(g);

                            if(!gradesSpecific.isEmpty()){

                                for (int i=0;i<gradesSpecific.size();i++){


                                    db.deleteGrade(gradesSpecific.get(i).getId());
                                    gradesSpecific.get(i).setSumPoints(gradesSum());
                                    db.addGrade(gradesSpecific.get(i));
                                }

                            }
                            //for action bar title setup
                            setTitle();
                            listViewGrades.setAdapter(adapterGrades);
                            Toasty.success(AddGradeActivity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();




                        }


                    }
                });


            }


        });



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

                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddGradeActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.dialog_grade_layout, null);

                            typeDialog=(EditText) mView.findViewById(R.id.editTextType);
                            pointsDialog=(EditText)mView.findViewById(R.id.editTextPoints);
                            btnDialog=(Button)mView.findViewById(R.id.button8);
                            additionalDialog=(EditText)mView.findViewById(R.id.editTexAdditional);





                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();

                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                            dialog.show();
                            //dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            btnDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(typeDialog.getText().toString().isEmpty()) {

                                        Toasty.error(AddGradeActivity.this, "Type field is empty!", Toast.LENGTH_SHORT).show();
                                        typeDialog.setHintTextColor(Color.rgb(242, 90, 90));

                                    }else if(pointsDialog.getText().toString().isEmpty()){

                                        Toasty.error(AddGradeActivity.this, "Points field is empty!", Toast.LENGTH_SHORT).show();
                                        pointsDialog.setHintTextColor(Color.rgb(242, 90, 90));
                                    }

                                    else if(additionalDialog.getText().toString().isEmpty()){

                                        int id=0;

                                        if(grades.size()!=0) {

                                            for (int j = 0; j < grades.size(); j++) {
                                                id = grades.get(j).getId();


                                            }
                                            id += 1;
                                        }



                                        g =new GradeObject(shortName,typeDialog.getText().toString(),
                                                Float.valueOf(pointsDialog.getText().toString()),"",0,id);
                                        grades.add(g);
                                        grades.get(grades.size()-1).setSumPoints(gradesSum());
                                        gradesSpecific.add(g);
                                        gradesSpecific.get(0).setSumPoints(gradesSum());
                                        g.setSumPoints(gradesSum());
                                        db.addGrade(g);
                                        setTitle();
                                        listViewGrades.setAdapter(adapterGrades);
                                        Toasty.success(AddGradeActivity.this, "Successfully added!", Toast.LENGTH_SHORT).show();

                                        dialog.dismiss();
                                    }else{


                                        int id=0;

                                        if(grades.size()!=0) {

                                            for (int j = 0; j < grades.size(); j++) {
                                                id = grades.get(j).getId();


                                            }
                                            id += 1;
                                        }



                                        g =new GradeObject(shortName,typeDialog.getText().toString(),
                                                Float.valueOf(pointsDialog.getText().toString()),additionalDialog.getText().toString(),0,id);
                                        grades.add(g);
                                        grades.get(grades.size()-1).setSumPoints(gradesSum());
                                        gradesSpecific.add(g);
                                        gradesSpecific.get(0).setSumPoints(gradesSum());
                                        g.setSumPoints(gradesSum());
                                        db.addGrade(g);
                                        setTitle();
                                        listViewGrades.setAdapter(adapterGrades);
                                        Toasty.success(AddGradeActivity.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }


                                }
                            });





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

    public void didTapButton(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
        animation1.setStartOffset(450);
        animation1.setDuration(400);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);



    }

    public float gradesSum(){

        float sum=0;

        for(int i=0;i<gradesSpecific.size();i++){


            sum+=gradesSpecific.get(i).getPoints();

        }

        return sum;
    }

    public void setTitle(){

        //for action bar title setup
        if (gradesSpecific.size() == 0) {
            actionBar.setTitle("You don't have any tests in"+" "+shortName+" "+ ":(");
        } else if (gradesSpecific.size() == 1) {
            gradesSpecific.get(0).setSumPoints(gradesSum());
            actionBar.setTitle(String.valueOf("You have " + gradesSpecific.size()) + " test in "+" "+shortName+" !");
        } else {
            gradesSpecific.get(0).setSumPoints(gradesSum());
            actionBar.setTitle(String.valueOf("You have " + gradesSpecific.size()) + " tests in "+" "+shortName+" !");

        }


    }




    @Override
    public void onBackPressed() {

            if(!gradesSpecific.isEmpty()) {
                Intent i = new Intent(getApplicationContext(), AddGradeActivity.class);

                i.putParcelableArrayListExtra("values", gradesSpecific);
                i.putExtra("position", position);
                setResult(1, i);
            }else if(gradesSpecific.isEmpty()){

                Intent i = new Intent(getApplicationContext(), AddGradeActivity.class);
                i.putParcelableArrayListExtra("values", gradesSpecific);
                i.putExtra("position", position);
                setResult(2,i);

            }






        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

    }
}
