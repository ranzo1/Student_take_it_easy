package com.example.ranzo1.student_app.mainActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.adapters.GradesAdapter;
import com.example.ranzo1.student_app.add_and_update.AddGradeActivity;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.objects.GradeObject;
import com.example.ranzo1.student_app.objects.Subject;
import com.example.ranzo1.student_app.objects.SubjectGradeObject;

import java.util.ArrayList;
import java.util.List;

public class GradesActivity extends AppCompatActivity {

    private List<Subject> subjects= new ArrayList<>();
    private ListView listViewGrades;
    private GradesAdapter adapterGrades;
    private List<SubjectGradeObject> subjectsGrades=new ArrayList<>();
    private ArrayList<GradeObject> gradesSpecific=new ArrayList<>();
    private List<GradeObject> grades = new ArrayList<>();
    private TextView textAvarageGrade,textPoints;
    DatabaseHendler db;

   // public final static String STRING_VALUES_KEY = "values";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);


        subjects=getIntent().getParcelableArrayListExtra("listaPredmeta");


        for (int i=0;i<subjects.size();i++){

            SubjectGradeObject g=new SubjectGradeObject(subjects.get(i).getShortName(),subjects.get(i).getColor());
            subjectsGrades.add(g);

        }

        db =DatabaseHendler.getInstance(this);

        grades=db.getAllGrades();

        setSumPoints();



       /* for(int i=0;i<grades.size();i++){

            db.deleteGrade(grades.get(i).getId());

        }*/

        textPoints=(TextView)findViewById(R.id.points);
        textAvarageGrade = (TextView) findViewById(R.id.textView15);
        listViewGrades = (ListView) findViewById(R.id.listViewGrades);
        adapterGrades = new GradesAdapter(getApplicationContext(), subjectsGrades);
        listViewGrades.setAdapter(adapterGrades);

        setGradeFinal();



        listViewGrades.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(),AddGradeActivity.class);
                //ArrayList values = new ArrayList<>();

                i.putExtra("name",subjects.get(position).getShortName());
                i.putExtra("color",subjects.get(position).getColor());
                i.putExtra("position",position);

                startActivityForResult(i,1);


            }

        });


        textAvarageGrade.setText(String.valueOf(avarageGrade()));

    }

    //function for calculating points of all exams in subject
    public void setSumPoints(){

        for (int i =0;i<subjectsGrades.size();i++) {

           // gradeFinal(subjectsGrades.get(i));

            for (int j = 0; j < grades.size(); j++) {

                if (grades.get(j).getShortName().equals(subjectsGrades.get(i).getSubjectName())) {

                    subjectsGrades.get(i).setPoint(grades.get(j).getSumPoints());
                }

            }
        }


    }

    public void setGradeFinal(){

        for(int i=0;i<subjectsGrades.size();i++){

            gradeFinal(subjectsGrades.get(i));


        }


    }

    //function for final grade of subject
    public void gradeFinal(SubjectGradeObject s){

        if(s.getPoint()<55){
            s.setGrade(5);

        }else if(s.getPoint()>54 && s.getPoint()<65){
            s.setGrade(6);

        }else if(s.getPoint()>64 && s.getPoint()<75) {
            s.setGrade(7);

        }else if(s.getPoint()>74 && s.getPoint()<85){

            s.setGrade(8);

        }else if(s.getPoint()>84 && s.getPoint()<95){

            s.setGrade(9);

        }else if(s.getPoint()>94){

            s.setGrade(10);
        }



    }
   //function for calculating avarage grade of grades of all subjects
    public float avarageGrade(){

        float nullSum=0;
        float sum=0;
        ArrayList<SubjectGradeObject> subjGradesNew=new ArrayList<>();

        for(int i=0;i<subjectsGrades.size();i++){

            if(subjectsGrades.get(i).getGrade()!=0 && (subjectsGrades.get(i).getGrade())!=5){

                subjGradesNew.add(subjectsGrades.get(i));
                sum+=subjectsGrades.get(i).getGrade();

            }


        }

        if(sum!=nullSum) {
            return sum / subjGradesNew.size();
        }else return nullSum;
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //db =DatabaseHendler.getInstance(this);

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {

            if(resultCode==2){


                if (data.getParcelableArrayListExtra("values") != null) {


                    try {

                        int position = data.getIntExtra("position", -1);
                        subjectsGrades.get(position).setPoint(0);

                        grades = db.getAllGrades();
                        setGradeFinal();
                        textAvarageGrade.setText(String.valueOf(avarageGrade()));

                        listViewGrades.setAdapter(adapterGrades);
                    }

                    catch (Exception e) {
                    Log.i("GRESKA", e.getMessage());
                }

                }

            }

            try {


                if (data.getParcelableArrayListExtra("values") != null) {


                    gradesSpecific=data.getParcelableArrayListExtra("values");
                    int position=data.getIntExtra("position",-1);




                           subjectsGrades.get(position).setPoint(gradesSpecific.get(0).getSumPoints());
                          // Toast.makeText(this, String.valueOf(grades.get(position).getSumPoints()), Toast.LENGTH_SHORT).show();
                           grades=db.getAllGrades();
                           setGradeFinal();
                           textAvarageGrade.setText(String.valueOf(avarageGrade()));

                    //setSumPoints();

                    listViewGrades.setAdapter(adapterGrades);



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
