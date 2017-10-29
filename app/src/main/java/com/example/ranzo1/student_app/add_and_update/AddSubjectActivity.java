package com.example.ranzo1.student_app.add_and_update;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.cocosw.bottomsheet.BottomSheet;
import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.mainActivities.SubjectActivity;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.objects.Subject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;


import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import petrov.kristiyan.colorpicker.ColorPicker;

import static com.example.ranzo1.student_app.R.layout.activity_add_subject;

public class AddSubjectActivity extends AppCompatActivity {


    public final static String STRING_VALUES_KEY = "values";
    private ColorPicker colorPicker;
    private int boja,old_id;
    private ArrayList<Subject> values=new ArrayList();
    private int editCheck=0;
    private ArrayList<Subject> editSubject=new ArrayList<>();
    private TextView subject,profesor,asistent;
    int position;
    private MaterialEditText subject_name,subject_short_name,professor_name,professor_email,assistent_name,assistent_email,professor_cabinet,assistent_cabinet;
    private List<Subject> subject_database=new ArrayList<>();
    DatabaseHendler db;
    private ActionBar actionBar;
    private SimpleDraweeView simpleDraweeView,simpleDraweeView2;
    private FloatingActionButton colorPickerbtn;
    private String professor_image_uri, assistent_image_uri;
    private RoundingParams roundingParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(activity_add_subject);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        FloatingActionButton done = (FloatingActionButton) findViewById(R.id.idDone);

        db=DatabaseHendler.getInstance(this);

        init();//findviewbyid


        roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        roundingParams.setBorder(Color.parseColor("#507C5C"),20f);
        simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
        simpleDraweeView2.getHierarchy().setRoundingParams(roundingParams);

        editCheck=getIntent().getIntExtra("edit",0);
        editSubject=getIntent().getParcelableArrayListExtra("editSubject");
        position=getIntent().getIntExtra("position",-1);

        subject_database=db.getAllSubjects();

       // stara_boja=getIntent().getIntExtra("color",-1);
        //old_id=getIntent().getIntExtra("id",0);

        didTapButton(done);
        didTapButton(simpleDraweeView);
        didTapButton(simpleDraweeView2);
        didTapButton(colorPickerbtn);


        if(editCheck==1)
        {
            subject_name.setText(editSubject.get(0).getName());
            subject_short_name.setText(editSubject.get(0).getShortName());
            professor_name.setText(editSubject.get(0).getProfessor());
            professor_email.setText(editSubject.get(0).getProfEmail());
            assistent_name.setText(editSubject.get(0).getAsisstent());
            assistent_email.setText(editSubject.get(0).getAsistEmail());
            professor_cabinet.setText((editSubject.get(0).getProfCabinet()));
            assistent_cabinet.setText(editSubject.get(0).getAsistCabinet());
            boja=editSubject.get(0).getColor();
            old_id=editSubject.get(0).getId();
            colorPickerbtn.setBackgroundTintList(ColorStateList.valueOf((editSubject.get(0).getColor())));

            professor_image_uri=editSubject.get(0).getProfImage();
            assistent_image_uri=editSubject.get(0).getAssistImage();

            simpleDraweeView.setImageURI(professor_image_uri);
            simpleDraweeView2.setImageURI(assistent_image_uri);


            roundingParams.setBorderColor(boja);
            simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
            simpleDraweeView2.getHierarchy().setRoundingParams(roundingParams);
            subject.setBackgroundColor(boja);
            profesor.setBackgroundColor(boja);
            asistent.setBackgroundColor(boja);
            actionBar.setBackgroundDrawable(new ColorDrawable(boja));
            actionBar.setTitle("Update subject");

        }


       // clickTapButton(done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject_string_name = subject_name.getText().toString();
                String subject_string_short_name = subject_short_name.getText().toString();
                String professor_string_name = professor_name.getText().toString();
                String professor_string_email = professor_email.getText().toString();
                String assistent_string_name = assistent_name.getText().toString();
                String assistent_string_email = assistent_email.getText().toString();
                String professor_string_cabinet=professor_cabinet.getText().toString();
                String assistent_string_cabinet=assistent_cabinet.getText().toString();


                if( subject_name.getText().toString().trim().length()== 0|
                        subject_short_name.getText().toString().trim().length()== 0|
                        subject_short_name.getText().toString().trim().length()> 7|
                        boja==0) {

                    if(subject_name.getText().toString().trim().length()==0 ){

                        subject_name.setError("Input name!");
                        // Toasty.error(AddSubjectActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        clickTapButtonFalse(v);

                        // Toasty.error(AddSubjectActivity.this, "Fill fields with red letters and choose color!", Toast.LENGTH_SHORT).show();

                    }

                    if(subject_short_name.getText().toString().trim().length()==0)
                    {   subject_short_name.setError("Input short name!");

                        //Toasty.error(AddSubjectActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        clickTapButtonFalse(v);



                    }if(boja==0)
                    {
                        colorPickerbtn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(242, 90, 90)));
                        //Toasty.error(AddSubjectActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        clickTapButtonFalse(v);


                    }


                }else {

                    if (editCheck==0)
                    {
                        int id=0;

                        if(subject_database.size()!=0) {

                            for (int j = 0; j < subject_database.size(); j++) {
                                id = subject_database.get(j).getId();


                            }

                            id += 1;
                        }


                        Subject s=new Subject(subject_string_name, subject_string_short_name, professor_string_name,
                                professor_string_email, assistent_string_name, assistent_string_email, boja,
                                professor_string_cabinet, assistent_string_cabinet,professor_image_uri,assistent_image_uri,id);
                        values.add(s);

                        // db.addSubject(s);

                        ///////////check if subject name or short name exist in list////////////
                        boolean exist=false;



                        for(int k=0;k<subject_database.size();k++)
                        {

                            if(subject_database.get(k).getName().equals(subject_string_name))
                            {
                                exist=true;

                            }else if(subject_database.get(k).getShortName().equals(subject_string_short_name))
                            {
                                exist=true;
                            }

                        }

                        if(exist==false) {
                            Intent i = new Intent(getApplicationContext(), SubjectActivity.class);

                            i.putParcelableArrayListExtra(STRING_VALUES_KEY, values);
                                       /*i.putExtra("byteProf",byte_prof_image);
                                        i.putExtra("byteAssist",byte_assist_image);*/
                            setResult(2, i);
                            Toasty.success(AddSubjectActivity.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                        }else if(exist==true)
                        {
                            Toasty.error(AddSubjectActivity.this, "Subject name or short name already exist!", Toast.LENGTH_SHORT).show();
                        }

                    }else
                    {
                        //update

                        Intent i = new Intent(getApplicationContext(), SubjectActivity.class);
                        values.add(new Subject(subject_string_name, subject_string_short_name, professor_string_name,
                                professor_string_email, assistent_string_name, assistent_string_email, boja,
                                professor_string_cabinet, assistent_string_cabinet,professor_image_uri,assistent_image_uri,old_id));
                        i.putParcelableArrayListExtra(STRING_VALUES_KEY, values);


                        i.putParcelableArrayListExtra("editSubject", values);
                        i.putExtra("check",1);
                        i.putExtra("position",position);



                        i.putExtra("boja",boja);
                        setResult(4,i);

                        Toasty.success(AddSubjectActivity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();

                        finish();
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }

            }
        });



        colorPickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                colorPicker = new ColorPicker(AddSubjectActivity.this);
                colorPicker.setRoundColorButton(true);
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        if(color!=0) {
                            colorPickerbtn.setBackgroundTintList((ColorStateList.valueOf(color)));
                            boja=color;

                            roundingParams.setBorderColor(boja);
                            simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
                            simpleDraweeView2.getHierarchy().setRoundingParams(roundingParams);

                            subject.setBackgroundColor(boja);
                            profesor.setBackgroundColor(boja);
                            asistent.setBackgroundColor(boja);

                            actionBar.setBackgroundDrawable(new ColorDrawable(boja));
                        }

                    }
                    @Override
                    public void onCancel(){

                    }
                });
            }
        });

        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new BottomSheet.Builder(AddSubjectActivity.this).sheet(R.menu.menu).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.sendEmail:
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{professor_email.getText().toString()});

                                email.setType("message/rfc822");

                                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                break;
                            case R.id.pickImage:
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent,1);

                        }
                    }
                }).show();



            }
        });

        simpleDraweeView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new BottomSheet.Builder(AddSubjectActivity.this).sheet(R.menu.menu).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.sendEmail:
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL, new String[] { assistent_email.getText().toString()});

                                email.setType("message/rfc822");

                                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                break;
                            case R.id.pickImage:
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent,2);

                        }
                    }
                }).show();

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

                            String subject_string_name = subject_name.getText().toString();
                            String subject_string_short_name = subject_short_name.getText().toString();
                            String professor_string_name = professor_name.getText().toString();
                            String professor_string_email = professor_email.getText().toString();
                            String assistent_string_name = assistent_name.getText().toString();
                            String assistent_string_email = assistent_email.getText().toString();
                            String professor_string_cabinet=professor_cabinet.getText().toString();
                            String assistent_string_cabinet=assistent_cabinet.getText().toString();


                            if( subject_name.getText().toString().trim().length()== 0|
                                    subject_short_name.getText().toString().trim().length()== 0|
                                    subject_short_name.getText().toString().trim().length()> 7|
                                    boja==0) {

                                if(subject_name.getText().toString().trim().length()==0 ){

                                    subject_name.setError("Input name!");
                                   // Toasty.error(AddSubjectActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                    clickTapButtonFalse(v);

                                   // Toasty.error(AddSubjectActivity.this, "Fill fields with red letters and choose color!", Toast.LENGTH_SHORT).show();

                                }

                                 if(subject_short_name.getText().toString().trim().length()==0)
                                {   subject_short_name.setError("Input short name!");

                                    //Toasty.error(AddSubjectActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                    clickTapButtonFalse(v);



                                }if(boja==0)
                                {
                                    colorPickerbtn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(242, 90, 90)));
                                    //Toasty.error(AddSubjectActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                    clickTapButtonFalse(v);


                                }


                            }else {

                                if (editCheck==0)
                                {
                                    int id=0;

                                    if(subject_database.size()!=0) {

                                        for (int j = 0; j < subject_database.size(); j++) {
                                            id = subject_database.get(j).getId();


                                        }

                                        id += 1;
                                    }


                                    Subject s=new Subject(subject_string_name, subject_string_short_name, professor_string_name,
                                            professor_string_email, assistent_string_name, assistent_string_email, boja,
                                            professor_string_cabinet, assistent_string_cabinet,professor_image_uri,assistent_image_uri,id);
                                    values.add(s);

                                   // db.addSubject(s);

                                    ///////////check if subject name or short name exist in list////////////
                                    boolean exist=false;



                                    for(int k=0;k<subject_database.size();k++)
                                    {

                                        if(subject_database.get(k).getName().equals(subject_string_name))
                                        {
                                            exist=true;

                                        }else if(subject_database.get(k).getShortName().equals(subject_string_short_name))
                                        {
                                            exist=true;
                                        }

                                    }

                                    if(exist==false) {
                                        Intent i = new Intent(getApplicationContext(), SubjectActivity.class);

                                        i.putParcelableArrayListExtra(STRING_VALUES_KEY, values);
                                       /*i.putExtra("byteProf",byte_prof_image);
                                        i.putExtra("byteAssist",byte_assist_image);*/
                                        setResult(2, i);
                                        Toasty.success(AddSubjectActivity.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                                        finish();
                                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                                    }else if(exist==true)
                                    {
                                        Toasty.error(AddSubjectActivity.this, "Subject name or short name already exist!", Toast.LENGTH_SHORT).show();
                                    }

                                }else
                                {
                                    //update

                                    Intent i = new Intent(getApplicationContext(), SubjectActivity.class);
                                    values.add(new Subject(subject_string_name, subject_string_short_name, professor_string_name,
                                            professor_string_email, assistent_string_name, assistent_string_email, boja,
                                            professor_string_cabinet, assistent_string_cabinet,professor_image_uri,assistent_image_uri,old_id));
                                    i.putParcelableArrayListExtra(STRING_VALUES_KEY, values);


                                    i.putParcelableArrayListExtra("editSubject", values);
                                    i.putExtra("check",1);
                                    i.putExtra("position",position);



                                    i.putExtra("boja",boja);
                                    setResult(4,i);

                                    Toasty.success(AddSubjectActivity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();

                                    finish();
                                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
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

    public void init(){

        simpleDraweeView=(SimpleDraweeView)findViewById(R.id.fresco_view);
        simpleDraweeView2=(SimpleDraweeView)findViewById(R.id.fresco_view2);

        colorPickerbtn= (FloatingActionButton) findViewById(R.id.colorPicker2);
        subject_name = (MaterialEditText) findViewById(R.id.ideditSubjectName);
        subject_short_name = (MaterialEditText) findViewById(R.id.idShortName);
        professor_name = (MaterialEditText) findViewById(R.id.idNameProffesor);
        professor_email = (MaterialEditText) findViewById(R.id.idEmailProfessor);
        assistent_name = (MaterialEditText) findViewById(R.id.idNameAssistent);
        assistent_email = (MaterialEditText) findViewById(R.id.idEmailAssistent);
        professor_cabinet=(MaterialEditText)findViewById(R.id.profCabinet);
        assistent_cabinet=(MaterialEditText)findViewById(R.id.asistCabinet);
        subject=(TextView)findViewById(R.id.idTxtSubject);
        profesor=(TextView)findViewById(R.id.idTxtProfessor);
        asistent=(TextView)findViewById(R.id.idTxtAssistent);


        actionBar = getSupportActionBar();




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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //code 1 for professor
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

                professor_image_uri = String.valueOf(data.getData());
                simpleDraweeView = (SimpleDraweeView) findViewById(R.id.fresco_view);
                simpleDraweeView.setImageURI(professor_image_uri);

            //code for assistent
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

                assistent_image_uri =String.valueOf(data.getData());
                simpleDraweeView2 = (SimpleDraweeView) findViewById(R.id.fresco_view2);
                simpleDraweeView2.setImageURI(assistent_image_uri);


        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

}