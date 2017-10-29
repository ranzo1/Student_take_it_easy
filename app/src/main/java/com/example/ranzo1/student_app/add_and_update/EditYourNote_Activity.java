package com.example.ranzo1.student_app.add_and_update;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.KeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.mainActivities.NotesActivity;
import com.example.ranzo1.student_app.mainActivities.SubjectActivity;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.objects.Subject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import petrov.kristiyan.colorpicker.ColorPicker;


public class EditYourNote_Activity extends AppCompatActivity  {
   // int noteId;

    private List<Subject> subjects=new ArrayList();
    private List<Subject> notes=new ArrayList();
    private List<Subject> values=new ArrayList<>();
    private ArrayList<String> valuesNotes=new ArrayList<>();
    private ArrayList<String> selectedItems= new ArrayList<>();
    private EditText naslov;
    private EditText sadrzaj;
    private List<String> lista_za_spiner=new ArrayList<>();
    private ArrayAdapter<String>  myAdapter;
    private TextView naziv;
    private Spinner s;
    private int position=-1;
    private CheckBox checkBox,checkBoxToDoList;
    private RelativeLayout layoutspiner;
    private FloatingActionButton colorpickerbtn,btn,btnShare;
    private int boja=0;
    private ListView toDoListView;
    private TextView txtAdditems,txtDeleteItems;


    private int old_color;
    private Button btnAddItem,btnDeleteItem;

    private int old_id ;
    private ActionBar actionBar;
    private RelativeLayout background;
    int colorSpinner=0;
    DatabaseHendler db;
    private ArrayList<Subject> notes_database=new ArrayList<>();
    private ArrayList<String> listOfItems=new ArrayList<>();
    private String noteOrCheck;
    private KeyListener keyListener,keyListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_your_note_);

       // getWindow().setBackgroundDrawable(new ColorDrawable(0));
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        subjects = getIntent().getParcelableArrayListExtra("listaPredmeta");

        old_id =getIntent().getIntExtra("old_id",0);

        position = getIntent().getIntExtra("position",-1);

        valuesNotes = getIntent().getStringArrayListExtra("ValuesNotes");
        old_color= getIntent().getIntExtra("color", -1);
        noteOrCheck = getIntent().getStringExtra("check");
       // boja=old_color;




        notes=getIntent().getParcelableArrayListExtra("notes");

        actionBar = getSupportActionBar();
        db=DatabaseHendler.getInstance(this);
        notes_database= (ArrayList<Subject>) db.getAllNotes();

       // colorSpinner=old_color;
       // background.setBackgroundColor(old_color);



/////////////////////////////////findViewbyID//////////////////////////////////
        layoutspiner=(RelativeLayout)findViewById(R.id.relativeLayout16);
        background=(RelativeLayout)findViewById(R.id.layoutColor);
        checkBox=(CheckBox)findViewById(R.id.checkBox2);
        final CheckBox checkBoxRead=(CheckBox)findViewById(R.id.checkBox3);



        naslov = (EditText) findViewById(R.id.naslov_beleske);
        sadrzaj = (EditText) findViewById(R.id.text_beleske);
        colorpickerbtn=(FloatingActionButton) findViewById(R.id.colorpickerbtn);

        s = (Spinner) findViewById(R.id.spinner_Naziv);
        toDoListView = (ListView)findViewById(R.id.toDoListView);
        btnAddItem = (Button)findViewById(R.id.button11);
        btnDeleteItem = (Button)findViewById(R.id.button14);
        txtDeleteItems = (TextView) findViewById(R.id.txtDeleteItems);
        txtAdditems = (TextView) findViewById(R.id.textView9);
////////////////////////////////////////////////////////////////////////////////////////////
        int colorst=manipulateColor(old_color,1.2f);
        background.setBackgroundColor(colorst);

        colorpickerbtn.setVisibility(View.INVISIBLE);


        btnAddItem.setVisibility(View.INVISIBLE);
        btnDeleteItem.setVisibility(View.INVISIBLE);
        txtAdditems.setVisibility(View.INVISIBLE);
        txtDeleteItems.setVisibility(View.INVISIBLE);
        toDoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        keyListener=naslov.getKeyListener();
        keyListener2=sadrzaj.getKeyListener();

        //String[] items={"English","Chinese","French","German","Italian","Khmer"};

        final ArrayAdapter<String> aa=new ArrayAdapter<String>(this,R.layout.checkable_list_layout,R.id.txt_title,listOfItems);
        toDoListView.setAdapter(aa);

        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                String selectedItem = ((TextView) view).getText().toString();
                if(selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                else
                    selectedItems.add(selectedItem); //add selected item to the list of selected items


            }

        });

        toDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditYourNote_Activity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_item_layout, null);
                final EditText editTxtItem = (EditText) mView.findViewById(R.id.typeItem);
                final Button btnOk = (Button) mView.findViewById(R.id.button13);
                final Button btnNext = (Button)mView.findViewById(R.id.btnNext);
                final Button btnDismiss = (Button)mView.findViewById(R.id.button15);



                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                dialog.show();

                String substringUpdate = listOfItems.get(position);
                substringUpdate = substringUpdate.substring(3);


                editTxtItem.setText(substringUpdate);

                dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);



                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!editTxtItem.getText().toString().isEmpty()){

                            Toasty.success(EditYourNote_Activity.this, "Item updated!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            String stringItem = String.valueOf(position+1) + ". " + editTxtItem.getText().toString();
                            listOfItems.remove(position);
                            listOfItems.add(position,stringItem);
                            toDoListView.setAdapter(aa);

                        }else{
                            Toasty.error(EditYourNote_Activity.this, "Edit text is empty!", Toast.LENGTH_SHORT).show();
                            // editTxtItem.setHintTextColor(Color.rgb(242, 90, 90));

                        }

                    }
                });

                btnNext.setVisibility(View.INVISIBLE);

                btnDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                return true;
            }
        });


        checkPosition(position);
        if(position==-1)
        {
            actionBar.setTitle("Add note");
        }else {
            actionBar.setTitle("Update note");
            //int colorOfSubject=valuesNotes.get(position)

            colorpickerbtn.setBackgroundTintList(ColorStateList.valueOf(old_color));
                //int old_color2=old_color;
            boja=old_color;
            checkBoxRead.performClick();
            naslov.setKeyListener(null);
            sadrzaj.setKeyListener(null);
            //background.setBackgroundColor(manipulateColor(old_color2,1.2f));


        }

        for(int i =0;i<subjects.size();i++)
        {

            lista_za_spiner.add(subjects.get(i).getName());

        }

      //  colorpickerbtn.setBackgroundColor(old_color);


        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(EditYourNote_Activity.this,android.R.layout.simple_list_item_1,lista_za_spiner);
        myAdapter2.setDropDownViewResource((android.R.layout.simple_expandable_list_item_1));
        s.setAdapter(myAdapter2);



        if(valuesNotes!=null)
        {
            naslov.setText(valuesNotes.get(3).toString());

            sadrzaj.setText(valuesNotes.get(2).toString());



            for(int i=0;i<lista_za_spiner.size();i++)
            {
                if(valuesNotes.get(0).equals(lista_za_spiner.get(i)))
                {
                    s.setSelection(i);

                }
            }
        }





        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(!checkBox.isChecked()){
                 colorSpinner = subjects.get(position).getColor();
                    int color2= colorSpinner;
                    background.setBackgroundColor(manipulateColor(color2,1.2f));

                }else{

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        checkBoxRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkBoxRead.isChecked()){

                    naslov.setKeyListener(null);
                    sadrzaj.setKeyListener(null);


                }else if(!checkBoxRead.isChecked()){

                    naslov.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
                    sadrzaj.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
                }
            }
        });
/////////////////////////////////////////VISIBILITY////////////////////////////////////////////////////
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkBox.isChecked()) {
                    layoutspiner.setVisibility(View.INVISIBLE);
                    colorpickerbtn.setVisibility(View.VISIBLE);

                    didTapColorpicker(colorpickerbtn);

                   // ColorDrawable buttonColor = (ColorDrawable) colorpickerbtn.getBackground();

                    if(boja!=0) {
                        int color = boja;
                        background.setBackgroundColor(manipulateColor(color, 1.2f));
                    }else
                    {
                        int color=old_color;
                        background.setBackgroundColor(manipulateColor(color,1.2f));
                    }

                }else {
                    layoutspiner.setVisibility(View.VISIBLE);
                    colorpickerbtn.setVisibility(View.INVISIBLE);

                    didTapColorpicker(layoutspiner);

                    if(colorSpinner!=0) {

                        int color = colorSpinner;
                        background.setBackgroundColor(manipulateColor(color, 1.2f));
                    }else
                    {
                        int color = subjects.get(0).getColor();
                        background.setBackgroundColor(manipulateColor(color,1.2f));

                    }

                }

            }
        });



                if(noteOrCheck.equals("checkList"))
                {
                    toDoListView.setVisibility(View.VISIBLE);
                    sadrzaj.setVisibility(View.INVISIBLE);
                    btnAddItem.setVisibility(View.VISIBLE);
                    btnDeleteItem.setVisibility(View.VISIBLE);
                    txtAdditems.setVisibility(View.VISIBLE);
                    txtDeleteItems.setVisibility(View.VISIBLE);

                    didTapColorpicker(btnAddItem);
                    didTapColorpicker(btnDeleteItem);
                    didTapButton(txtAdditems);
                    didTapButton(txtDeleteItems);



                }else if(noteOrCheck.equals("note")){

                    toDoListView.setVisibility(View.INVISIBLE);
                    sadrzaj.setVisibility(View.VISIBLE);
                    btnAddItem.setVisibility(View.INVISIBLE);
                    btnDeleteItem.setVisibility(View.INVISIBLE);
                    txtAdditems.setVisibility(View.INVISIBLE);
                    txtDeleteItems.setVisibility(View.INVISIBLE);

                }


//----------------------------------------------------------------------------------------------------------------------//
        colorpickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorPicker colorPicker = new ColorPicker(EditYourNote_Activity.this);
                colorPicker.setRoundColorButton(true);
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        if(color!=0) {
                            colorpickerbtn.setBackgroundTintList(ColorStateList.valueOf(color));
                            boja=color;

                            background.setBackgroundColor(manipulateColor(color,1.2f));

                            //stara_boja=boja;

                        }

                    }

                    @Override
                    public void onCancel(){


                    }
                });
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditYourNote_Activity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_item_layout, null);
                final EditText editTxtItem = (EditText) mView.findViewById(R.id.typeItem);
                final Button btnOk = (Button) mView.findViewById(R.id.button13);
                final Button btnNext = (Button)mView.findViewById(R.id.btnNext);
                final Button btnDismiss = (Button)mView.findViewById(R.id.button15);



                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                dialog.show();

                dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);



                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!editTxtItem.getText().toString().isEmpty()){
                            Toasty.success(EditYourNote_Activity.this, "Item added!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            String stringItem = String.valueOf(listOfItems.size()+1) + ". " + editTxtItem.getText().toString();




                           /* for(int i =0;i<selectedItems.size();i++){

                                //if(selectedItems.get(i).substring(2).equals(stringItem.substring(2))){

                                    //selectedItems.add(stringItem);
                                    //selectedItems.remove(selectedItems.get(i));
                                    toDoListView.setItem


                               //}
                            }*/


                            listOfItems.add(stringItem);


                            /*for(int i =0;i<listOfItems.size();i++){

                                for(int j=0;j<selectedItems.size();j++){

                                    if(listOfItems.get(i).equals(selectedItems.get(j))){

                                        toDoListView.setItemChecked(i,true);

                                    }
                                }

                            }*/

                            toDoListView.setAdapter(aa);

                        }else{
                            Toasty.error(EditYourNote_Activity.this, "Edit text is empty!", Toast.LENGTH_SHORT).show();
                           // editTxtItem.setHintTextColor(Color.rgb(242, 90, 90));

                        }

                    }
                });

                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       // dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        btnOk.performClick();
                        dialog.dismiss();
                        btnAddItem.performClick();

                    }
                });

                btnDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });



            }
        });

        btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listOfItems.size()==0){

                    Toasty.error(EditYourNote_Activity.this, "You do not have items in to do list!", Toast.LENGTH_SHORT).show();

                }else if(selectedItems.size()==0){

                    Toasty.error(EditYourNote_Activity.this, "You do not have selected items in to do list!", Toast.LENGTH_SHORT).show();

                }else {

                    new AlertDialog.Builder(EditYourNote_Activity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this subject?")
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                            ArrayList<String>oldListOfItems = listOfItems;
                                            listOfItems.removeAll(selectedItems);
                                            selectedItems.clear();
                                            toDoListView.setAdapter(aa);



                                            for(int i =0;i<listOfItems.size();i++)
                                            {
                                                String substring=listOfItems.get(i).substring(0,listOfItems.get(i).indexOf("."));

                                                String stringItem = listOfItems.get(i).replaceAll(substring,String.valueOf(i+1));
                                                listOfItems.remove(i);
                                                listOfItems.add(i,stringItem);


                                            }

                                    //Toast.makeText(EditYourNote_Activity.this, String.valueOf(selectedItems.size()), Toast.LENGTH_SHORT).show();
                                    Toasty.success(EditYourNote_Activity.this, "Selected items deleted!", Toast.LENGTH_SHORT).show();
                                    toDoListView.setAdapter(aa);
                                    // toDoListView.setAdapter(aa);


                                }
                            })
                            .setPositiveButton("No", null)
                            .show();
                }
            }
        });

        // EditText editText = (EditText) findViewById(R.old_id.editText);

        btn=(FloatingActionButton)findViewById(R.id.button9);
        btnShare=(FloatingActionButton)findViewById(R.id.button10);

        //didTapButton(btnShare);
        didTapButton(btn);
        String datum=new SimpleDateFormat("dd-MM-yyyy").format(new Date());


       //clickTapButton(btn);


      /*  btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File file = new File(Environment.getDataDirectory().toString() + "/" + "abc.pdf");
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
                startActivity(Intent.createChooser(sharingIntent, "share file with"));



            }
        });*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id=0;

                if(position==-1) {

                    if (notes_database.size() != 0) {

                        for (int j = 0; j < notes_database.size(); j++) {
                            id = notes_database.get(j).getId();


                        }

                        id += 1;
                    }

                }else if(position!=-1) {

                    id = old_id;
                }




                String datum = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date());




                if(checkBox.isChecked()) {

                    //ColorDrawable buttonColor = (ColorDrawable) colorpickerbtn.getBackground();

                    if(boja!=0) {


                        values.add(new Subject("123", "OTHER", sadrzaj.getText().toString(), naslov.getText().toString(), datum, "tttt", boja,id));

                        Intent i = new Intent(getApplicationContext(), NotesActivity.class);

                        i.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) values);
                        i.putExtra("position",position);

                        setResult(2, i);


                        finish();
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                        if(position==-1) {
                            Toasty.success(EditYourNote_Activity.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toasty.success(EditYourNote_Activity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        colorpickerbtn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(242, 90, 90)));
                        clickTapButtonFalse(v);
                        // Toasty.error(EditYourNote_Activity.this, "Choose color!", Toast.LENGTH_SHORT).show();
                        //Snackbar.make(v, "Choose colorSpinner!", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                    }


                }else
                {
                    values.add(new Subject(subjects.get(s.getSelectedItemPosition()).getName(), subjects.get(s.getSelectedItemPosition()).getShortName(),
                            sadrzaj.getText().toString(), naslov.getText().toString(), datum, "tttt", subjects.get(s.getSelectedItemPosition()).getColor(),id));

                    Intent i = new Intent(getApplicationContext(), NotesActivity.class);

                    i.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) values);
                    i.putExtra("position",position);

                    setResult(2, i);

                    // colorTextView.setTextColor(getResources().getColor(R.colorSpinner.colorAccent));


                    finish();
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    if(position==-1) {
                        Toasty.success(EditYourNote_Activity.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toasty.success(EditYourNote_Activity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                    }

                }

                // values.add(new Subject(subjects.get(s.getSelectedItemPosition()).getName(), subjects.get(s.getSelectedItemPosition()).getShortName(),
                //    sadrzaj.getText().toString(), naslov.getText().toString(), "4567", "tttt", subjects.get(s.getSelectedItemPosition()).getColor()));

                //0-name,1-skracen naziv,2-sadrzaj-beleske,3-naslov


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




                                int id=0;

                                if(position==-1) {

                                    if (notes_database.size() != 0) {

                                        for (int j = 0; j < notes_database.size(); j++) {
                                            id = notes_database.get(j).getId();


                                        }

                                        id += 1;
                                    }

                                }else if(position!=-1) {

                                    id = old_id;
                                }




                                String datum = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date());




                                if(checkBox.isChecked()) {

                                    //ColorDrawable buttonColor = (ColorDrawable) colorpickerbtn.getBackground();

                                    if(boja!=0) {


                                        values.add(new Subject("123", "OTHER", sadrzaj.getText().toString(), naslov.getText().toString(), datum, "tttt", boja,id));

                                        Intent i = new Intent(getApplicationContext(), NotesActivity.class);

                                        i.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) values);
                                        i.putExtra("position",position);

                                        setResult(2, i);


                                        finish();
                                        if(position==-1) {
                                            Toasty.success(EditYourNote_Activity.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toasty.success(EditYourNote_Activity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                                        }
                                    }else
                                    {
                                        colorpickerbtn.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(242, 90, 90)));
                                        clickTapButtonFalse(v);
                                       // Toasty.error(EditYourNote_Activity.this, "Choose color!", Toast.LENGTH_SHORT).show();
                                        //Snackbar.make(v, "Choose colorSpinner!", Snackbar.LENGTH_LONG)
                                        //.setAction("Action", null).show();

                                    }


                                }else
                                {
                                    values.add(new Subject(subjects.get(s.getSelectedItemPosition()).getName(), subjects.get(s.getSelectedItemPosition()).getShortName(),
                                            sadrzaj.getText().toString(), naslov.getText().toString(), datum, "tttt", subjects.get(s.getSelectedItemPosition()).getColor(),id));

                                    Intent i = new Intent(getApplicationContext(), NotesActivity.class);

                                    i.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) values);
                                    i.putExtra("position",position);

                                    setResult(2, i);

                                    // colorTextView.setTextColor(getResources().getColor(R.colorSpinner.colorAccent));


                                    finish();
                                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                                    if(position==-1) {
                                        Toasty.success(EditYourNote_Activity.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toasty.success(EditYourNote_Activity.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                // values.add(new Subject(subjects.get(s.getSelectedItemPosition()).getName(), subjects.get(s.getSelectedItemPosition()).getShortName(),
                                //    sadrzaj.getText().toString(), naslov.getText().toString(), "4567", "tttt", subjects.get(s.getSelectedItemPosition()).getColor()));

                                //0-name,1-skracen naziv,2-sadrzaj-beleske,3-naslov




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
        //animation1.setFillAfter(true);
        view.startAnimation(animation1);



    }

   /* public void selectionItems()
    {

    }*/

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
    public void didTapColorpicker(View view) {

        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);
        //animation1.setStartOffset(450);
        animation1.setDuration(400);
        //animation1.setFillAfter(true);
        view.startAnimation(animation1);


        // view.startAnimation(myAnim);
    }
    public void clickTapButtonFalse(View view) {
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        final Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_scale_up);

        //animation1.setStartOffset(5000);
        animation1.setDuration(550);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }
    public void checkPosition(int position){

        if(position!=-1) {

            if (valuesNotes.get(1).equals("OTHER")) {

                checkBox.performClick();


                layoutspiner.setVisibility(View.INVISIBLE);
                colorpickerbtn.setVisibility(View.VISIBLE);


               // boja=old_color;
                //colorpickerbtn.setBackgroundColor(old_color);
                int color=old_color;
                background.setBackgroundColor(manipulateColor(color,1.2f));

            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public ListView getToDoListView() {
        return toDoListView;
    }
}
