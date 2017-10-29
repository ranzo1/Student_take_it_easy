package com.example.ranzo1.student_app.mainActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.objects.Subject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;



public class MActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private Bitmap bitmap;
        private TextView textSubjects;
        private TextView cccc;
        private List<Subject>subjects=new ArrayList<>();
        private static int TIME_OUT = 4000;

            DatabaseHendler db;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        db =DatabaseHendler.getInstance(this);
        subjects=db.getAllSubjects();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        final Button subjActivity =(Button) findViewById(R.id.idsubjects);
        final Button files=(Button) findViewById(R.id.idfiles);
        final Button timetable=(Button) findViewById(R.id.idtimetable);

        final Button events=(Button) findViewById(R.id.idevents);
        final Button notes=(Button) findViewById(R.id.idnotes);
        final Button grades=(Button) findViewById(R.id.calculator);
        //Button info=(Button) findViewById(R.id.action_about);






         bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.textura);

        LottieAnimationView lottieAnimationView = (LottieAnimationView)findViewById(R.id.animation_view);

        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
        lottieAnimationView.isAnimating();

       // textSubjects.getPaint().setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT,Shader.TileMode.REPEAT));
        //t1=(TextView) findViewById(R.id.proba);
/////////////////////animacija dugmici////////////////////////////

              /*  didTapButton(subjActivity);
                didTapButton(files);
                didTapButton(timetable);
                didTapButton(email);
                didTapButton(events);
                didTapButton(notes);*/

//////////////////animacija dugmici////////////////////////////////




        clickTapButton(subjActivity);
        clickTapButton(timetable);
        clickTapButton(events);
        clickTapButton(files);
        clickTapButton(notes);
        clickTapButton(grades);



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
                            int charat=v.getResources().getResourceName(v.getId()).indexOf("/");
                            if ( v.getResources().getResourceName(v.getId()).substring(charat+1).equals("idsubjects") ) {

                                // Your action here on button click
                                Intent intentGetMessage = new Intent(getApplicationContext(), SubjectActivity.class);
                                //didTapButton(subjActivity);
                                startActivityForResult(intentGetMessage, 1);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                // clickTapButton(subjActivity);
                            } else if( v.getResources().getResourceName(v.getId()).substring(charat+1).equals("idtimetable") ){

                                if(!subjects.isEmpty())
                                {
                                    Intent i2 = new Intent(MActivity.this, TimetableActivity.class);
                                    i2.putParcelableArrayListExtra("listaPredmeta", (ArrayList<? extends Parcelable>) subjects);
                                    startActivity(i2);
                                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                                    //clickTapButton(timetable);

                                }
                                else {
                                    Toasty.warning(MActivity.this, "Sorry you don't have any subject :(", Toast.LENGTH_SHORT).show();
                                    clickTapButtonFalse(v);
                                }
                            }else if( v.getResources().getResourceName(v.getId()).substring(charat+1).equals("idevents") ){

                                if(!subjects.isEmpty()) {

                                    Intent i4 = new Intent(MActivity.this, EventsActivity.class);
                                    i4.putParcelableArrayListExtra("listaPredmeta", (ArrayList<? extends Parcelable>) subjects);
                                    startActivity(i4);
                                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                    finish();


                                }
                                else {
                                    clickTapButtonFalse(v);
                                    Toasty.warning(MActivity.this, "Sorry you don't have any subject :(", Toast.LENGTH_SHORT).show();
                                }

                            }else if( v.getResources().getResourceName(v.getId()).substring(charat+1).equals("idfiles") ){

                                if(!subjects.isEmpty()) {
                                    Intent i1 = new Intent(MActivity.this, FilesActivity.class);
                                    startActivity(i1);
                                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                }else {
                                    Toasty.warning(MActivity.this, "Sorry you don't have any subject :(", Toast.LENGTH_SHORT).show();

                                    // Toast.makeText(MActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
                                    clickTapButtonFalse(v);
                                }

                            }else if( v.getResources().getResourceName(v.getId()).substring(charat+1).equals("idnotes") ){

                                if(!subjects.isEmpty()) {
                                    Intent i5 = new Intent(MActivity.this, NotesActivity.class);
                                    i5.putParcelableArrayListExtra("listaPredmeta", (ArrayList<? extends Parcelable>) subjects);
                                    startActivity(i5);
                                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                                }else {
                                    clickTapButtonFalse(v);
                                    Toasty.warning(MActivity.this, "Sorry you don't have any subject :(", Toast.LENGTH_SHORT).show();
                                }
                            }else if( v.getResources().getResourceName(v.getId()).substring(charat+1).equals("calculator") ){

                                if(!subjects.isEmpty()) {
                                    Intent i6 = new Intent(MActivity.this, GradesActivity.class);
                                    i6.putParcelableArrayListExtra("listaPredmeta", (ArrayList<? extends Parcelable>) subjects);
                                    startActivity(i6);
                                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                                }else {
                                    clickTapButtonFalse(v);
                                    Toasty.warning(MActivity.this, "Sorry you don't have any subject :(", Toast.LENGTH_SHORT).show();
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

    public void didTapButton(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        view.startAnimation(myAnim);




       // view.startAnimation(myAnim);
    }
    public void clickTapButtonFalse(View view) {
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);


        //animation1.setStartOffset(5000);
        animation1.setDuration(550);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }


    //funkcija za primanje podataka
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            try {
                if (data.getParcelableArrayListExtra("listaPredmeta") != null) {

                    // subjects = data.getParcelableArrayListExtra("listaPredmeta");
                    subjects=db.getAllSubjects();
                    //t1=(TextView)findViewById(R.id.proba);
                    //t1.setText(subjects.get(0).getName());
                }


            } catch (Exception e) {
                Log.i("GRESKA", e.getMessage());
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent i=new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
