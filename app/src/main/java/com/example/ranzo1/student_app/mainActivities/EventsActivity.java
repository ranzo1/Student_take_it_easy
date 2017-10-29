package com.example.ranzo1.student_app.mainActivities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ranzo1.student_app.add_and_update.Calendar_updateActivity;
import com.example.ranzo1.student_app.adapters.EventAdd_adapter;
import com.example.ranzo1.student_app.add_and_update.Events_add2;
import com.example.ranzo1.student_app.objects.DatabaseHendler;
import com.example.ranzo1.student_app.other.NotificationReciever;
import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.objects.Subject;
import com.example.ranzo1.student_app.objects.TimeTableObject;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class EventsActivity extends AppCompatActivity {

    public static CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    public static ArrayList<Event> lista_datumi = new ArrayList<>();
    private ArrayList<Event> listPreview = new ArrayList<>();

    private ArrayList<Event> empty = new ArrayList<>();

    private List<Subject> subjects_for_date = new ArrayList<>();
    private long pokupljeni_datum;


    private EventAdd_adapter adapterPreview;

    private ArrayList<TimeTableObject> values = new ArrayList<>();

    private int month_Value=0, today_Value=0, all_events_Value=0, empty_Value = 4;
    private ListView listViewDatumi;
    private RadioButton today, all_events, month;
    private RadioGroup groupButtons;
    ////////////////////////////////////////////////////////////////////////
    private Intent intent;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private boolean turnOf;
    private RelativeLayout rel_ley;
    DatabaseHendler db;
    private LottieAnimationView animationView;

    /////////////////////////////////////////////////////////

    private  RelativeLayout emptyState;
    private TextView emptyTitle,emptySubtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        subjects_for_date = getIntent().getParcelableArrayListExtra("listaPredmeta");

        turnOf = getIntent().getBooleanExtra("turnOf", false);




        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        db =DatabaseHendler.getInstance(this);
        subjects_for_date=db.getAllSubjects();



        lista_datumi= (ArrayList<Event>) db.getAllEvents();



        // db.deleteEvent(lista_datumi.get(0));


        listViewDatumi = (ListView) findViewById(R.id.listaDog);
        adapterPreview = new EventAdd_adapter(getApplicationContext(), listPreview);


        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendar.removeAllEvents();
        compactCalendar.addEvents(lista_datumi);
        compactCalendar.shouldSelectFirstDayOfMonthOnScroll(false);

        Calendar c= Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);


        pokupljeni_datum=c.getTimeInMillis();
//////////////////////////////////Animation//////////////////////////////////////////////////
       /* animationView = (LottieAnimationView) findViewById(R.id.animation_view);
       // animationView.setAnimation("pencil_write.json");
        animationView.useExperimentalHardwareAcceleration(true);
        animationView.enableMergePathsForKitKatAndAbove(true);
        animationView.playAnimation();*/

        today = (RadioButton) findViewById(R.id.today);
        month = (RadioButton) findViewById(R.id.month);
        all_events = (RadioButton) findViewById(R.id.all_events);

        emptyState =(RelativeLayout)findViewById(R.id.relativeLayout7);
        emptyTitle=(TextView)findViewById(R.id.textView3) ;
        emptySubtitle=(TextView)findViewById(R.id.textView4);

        didTapButton(today);
        didTapButton(month);
        didTapButton(all_events);

        groupButtons = (RadioGroup) findViewById(R.id.radioGroup);


        String str = dateFormatMonth.format(Calendar.getInstance().getTime());
        str=str.replace("-"," ");
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);

        actionBar.setTitle(cap);
        listEventTimeSort(lista_datumi);




        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar c= Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY,0);
                c.set(Calendar.MINUTE,0);
                c.set(Calendar.SECOND,0);
                c.set(Calendar.MILLISECOND,0);

                listPreview = (ArrayList<Event>) compactCalendar.getEvents(c.getTimeInMillis());
                adapterPreview = new EventAdd_adapter(getApplicationContext(), listPreview);
                listViewDatumi.setAdapter(adapterPreview);


                today_Value = 1;
                month_Value = 0;
                all_events_Value = 0;
                empty_Value = 0;
                listViewDatumi.setVisibility(View.VISIBLE);

                if(!listPreview.isEmpty()){

                    emptyState.setVisibility(View.INVISIBLE);
                    emptyTitle.setVisibility(View.INVISIBLE);
                    emptySubtitle.setVisibility(View.INVISIBLE);


                }else{

                    emptyState.setVisibility(View.VISIBLE);
                    emptyTitle.setVisibility(View.VISIBLE);
                    emptySubtitle.setVisibility(View.VISIBLE);

                    emptyTitle.setText("No events for today!");

                }


                listEventTimeSort(lista_datumi);
                listEventTimeSort(listPreview);
            }
        });

        today.performClick();


        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPreview = (ArrayList<Event>) compactCalendar.getEventsForMonth(Calendar.getInstance().getTime());
                adapterPreview = new EventAdd_adapter(getApplicationContext(), listPreview);
                listViewDatumi.setAdapter(adapterPreview);

                today_Value = 0;
                month_Value = 2;
                all_events_Value = 0;
                empty_Value = 0;
                listViewDatumi.setVisibility(View.VISIBLE);


                listEventTimeSort(lista_datumi);
                listEventTimeSort(listPreview);

                if(!listPreview.isEmpty()){

                    emptyState.setVisibility(View.INVISIBLE);
                    emptyTitle.setVisibility(View.INVISIBLE);
                    emptySubtitle.setVisibility(View.INVISIBLE);


                }else{

                    emptyState.setVisibility(View.VISIBLE);
                    emptyTitle.setVisibility(View.VISIBLE);
                    emptySubtitle.setVisibility(View.VISIBLE);

                    emptyTitle.setText("No events for this month!");

                }
            }
        });
        all_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listPreview = (ArrayList<Event>) lista_datumi.clone();
                adapterPreview = new EventAdd_adapter(getApplicationContext(), listPreview);
                listViewDatumi.setAdapter(adapterPreview);

                today_Value = 0;
                month_Value = 0;
                all_events_Value = 3;
                empty_Value = 0;
                listViewDatumi.setVisibility(View.VISIBLE);

                if(!listPreview.isEmpty()){

                    emptyState.setVisibility(View.INVISIBLE);
                    emptyTitle.setVisibility(View.INVISIBLE);
                    emptySubtitle.setVisibility(View.INVISIBLE);


                }else{

                    emptyState.setVisibility(View.VISIBLE);
                    emptyTitle.setVisibility(View.VISIBLE);
                    emptySubtitle.setVisibility(View.VISIBLE);

                    emptyTitle.setText("No events in calendar!");

                }

                Collections.sort(listPreview, new Comparator<Event>() {
                    public int compare(Event o1, Event o2) {
                        if (getDate(o1.getTimeInMillis(), "MM/dd/yyyy HH:mm:ss") == null || getDate(o2.getTimeInMillis(), "MM/dd/yyyy HH:mm:ss") == null)
                            return 0;
                        //else if()
                        return getDate(o1.getTimeInMillis(), "MM/dd/yyyy HH:mm:ss").compareTo(getDate(o2.getTimeInMillis(), "MM/dd/yyyy HH:mm:ss"));

                    }


                });

                listEventTimeSort(lista_datumi);
                listEventTimeSort(listPreview);


            }
        });

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                pokupljeni_datum = dateClicked.getTime();

                Calendar c= Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY,0);
                c.set(Calendar.MINUTE,0);
                c.set(Calendar.SECOND,0);
                c.set(Calendar.MILLISECOND,0);

               /* if(pokupljeni_datum==c.getTimeInMillis()){

                    //today.performClick();
                    today.setChecked(true);
                }*/



               listPreview = new ArrayList<Event>();

                for(int j=0;j<lista_datumi.size();j++){

                    if(pokupljeni_datum==lista_datumi.get(j).getTimeInMillis())
                    {

                        listPreview.add(lista_datumi.get(j));

                    }


                }


                adapterPreview = new EventAdd_adapter(getApplicationContext(), listPreview);
                listViewDatumi.setAdapter(adapterPreview);

                if(!listPreview.isEmpty()){

                    emptyState.setVisibility(View.INVISIBLE);
                    emptyTitle.setVisibility(View.INVISIBLE);
                    emptySubtitle.setVisibility(View.INVISIBLE);


                }else{

                    emptyState.setVisibility(View.VISIBLE);
                    emptyTitle.setVisibility(View.VISIBLE);
                    emptySubtitle.setVisibility(View.VISIBLE);

                    emptyTitle.setText("No events for this date!");

                }

               /* Intent i = new Intent(getApplicationContext(), Events_add.class);
                i.putExtra("pokupljeni_datum", pokupljeni_datum);
                i.putParcelableArrayListExtra("subjects_for_date", (ArrayList<? extends Parcelable>) subjects_for_date);
                */


                groupButtons.clearCheck();
                empty_Value = 4;
                today_Value = 0;
                month_Value = 0;
                all_events_Value = 0;
                //listViewDatumi.setVisibility(View.INVISIBLE);


               // startActivityForResult(i, 2);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                compactCalendar.removeAllEvents();
                compactCalendar.addEvents(lista_datumi);

                Collections.sort(lista_datumi, new Comparator<Event>() {
                    public int compare(Event o1, Event o2) {
                        if (getDate(o1.getTimeInMillis(), "MM/dd/yyyy HH:mm:ss") == null || getDate(o2.getTimeInMillis(), "MM/dd/yyyy HH:mm:ss") == null)
                            return 0;
                        //else if()
                        return getDate(o1.getTimeInMillis(), "MM/dd/yyyy HH:mm:ss").compareTo(getDate(o2.getTimeInMillis(), "MM/dd/yyyy HH:mm:ss"));

                    }

                });

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                String str = dateFormatMonth.format(firstDayOfNewMonth);
                str=str.replace("-"," ");
                String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                actionBar.setTitle(cap);
            }
        });





        listViewDatumi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(EventsActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this event?")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db.deleteEvent(listPreview.get(position));
                                lista_datumi.remove(listPreview.get(position));
                                listPreview.remove(position);

                                compactCalendar.removeAllEvents();
                                compactCalendar.addEvents(lista_datumi);
                                adapterPreview.notifyDataSetChanged();

                                Toasty.success(EventsActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT, true).show();

                                if(listPreview.isEmpty()){



                                        emptyState.setVisibility(View.VISIBLE);
                                        emptyTitle.setVisibility(View.VISIBLE);
                                        emptySubtitle.setVisibility(View.VISIBLE);

                                    if(today_Value==1){

                                        emptyTitle.setText("No events for today!");
                                    }else if(month_Value==2){

                                        emptyTitle.setText("No events for this month");

                                    }else if(all_events_Value==3){

                                        emptyTitle.setText("No events in calendar!");

                                    }else if(empty_Value==4){

                                        emptyTitle.setText("No events for this date!");

                                    }


                                }

                            }
                        })
                        .setPositiveButton("No", null)
                        .show();

                return true;
            }
        });
        listViewDatumi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Event e = listPreview.get(position);
                Intent i = new Intent(getApplicationContext(), Calendar_updateActivity.class);
                i.putExtra("Data", e.getData().toString());
                i.putExtra("Time", e.getTimeInMillis());
                i.putExtra("Color", e.getColor());


                startActivityForResult(i, 3);



              /*  Event e =trenutni_datumi.get(position);
                Intent i =new Intent(getApplicationContext(),Calendar_updateActivity.class);
                i.putExtra("Data",e.getData().toString());
                i.putExtra("Time",e.getTimeInMillis());
                i.putExtra("Color",e.getColor());
                i.putExtra("check",3);
                // i.putExtra("position",position);

                position1=position;

                startActivityForResult(i,3);
*/

            }
        });
///////////////////////////////notification ////////////////////////////////////////////////////////////////////////////


        /**stoping notifications
         Intent intent = new Intent(this, Notification_reciever.class);
         PendingIntent sender = PendingIntent.getBroadcast(context, YOUR_ID, intent, INTENT_FLAGS);
         AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
         */



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {

                Intent i = new Intent(getApplicationContext(), Events_add2.class);
                i.putExtra("pokupljeni_datum", pokupljeni_datum);

                i.putParcelableArrayListExtra("subjects_for_date", (ArrayList<? extends Parcelable>) subjects_for_date);

            //listViewDatumi.setVisibility(View.INVISIBLE);


              startActivityForResult(i, 2);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);



           /* Intent i = new Intent(getApplicationContext(), Events_add2.class);
            i.putParcelableArrayListExtra("subjects_for_date", (ArrayList<? extends Parcelable>) subjects_for_date);
            listEventTimeSort(trenutni_datumi);

            startActivityForResult(i, 2);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

        }
        return super.onOptionsItemSelected(item);
    }

    public void listEventTimeSort(List<Event> events) {


        if (events.size() > 1) {

            for (int i = 0; i < events.size(); i++) {

                for (int j = i + 1; j < events.size(); j++) {


                    if (getDate(events.get(i).getTimeInMillis(), "MM/dd/yyyy HH:mm:ss").toString().equals(getDate(events.get(j).getTimeInMillis(), "MM/dd/yyyy HH:mm:ss").toString())) {
                        if ((Integer.valueOf(events.get(i).getData().toString().substring(0, 2)) > Integer.valueOf(events.get(j).getData().toString().substring(0, 2)))) {
                            Event temp = events.get(i);
                            events.set(i, events.get(j));
                            events.set(j, temp);

                        } else if (((Integer.valueOf(events.get(i).getData().toString().substring(0, 2)) == Integer.valueOf(events.get(j).getData().toString().substring(0, 2))))) {
                            if ((Integer.valueOf(events.get(i).getData().toString().substring(3, 5)) > Integer.valueOf(events.get(j).getData().toString().substring(3, 5)))) {

                                Event temp = events.get(i);
                                events.set(i, events.get(j));
                                events.set(j, temp);
                            }
                        }
                    }
                }
            }

        }
    }

    //  }
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 2) {

            try {

                if (data.getParcelableArrayListExtra("event lista") != null) {

                    String eventText;
                    values = data.getParcelableArrayListExtra("event lista");


                    //TimeTableObject(String day, String shortName, String start, String end, int colorSpinner,int id)

                    if (values.get(0).getEnd() != null) {
                        eventText = values.get(0).getStart() + values.get(0).getDay() + ":" +" "+ values.get(0).getShortName() + "\n" + values.get(0).getEnd();
                    } else {
                        eventText = values.get(0).getStart() + values.get(0).getDay() + ":" +" "+ values.get(0).getShortName();

                    }

                    Event ev1 = new Event(values.get(0).getColor(),pokupljeni_datum, eventText);
                    db.addEvent(ev1);
                    lista_datumi.add(ev1);
                    listPreview.add(ev1);
                    compactCalendar.removeAllEvents();
                    compactCalendar.addEvents(lista_datumi);
                    listViewDatumi.setAdapter(adapterPreview);


                    if(!listPreview.isEmpty()){

                        emptyState.setVisibility(View.INVISIBLE);
                        emptyTitle.setVisibility(View.INVISIBLE);
                        emptySubtitle.setVisibility(View.INVISIBLE);


                    }




                    Collections.sort(lista_datumi, new Comparator<Event>() {
                        public int compare(Event o1, Event o2) {
                            if (getDate(o1.getTimeInMillis(),"MM/dd/yyyy HH:mm:ss")== null ||getDate(o2.getTimeInMillis(),"MM/dd/yyyy HH:mm:ss") == null)
                                return 0;
                            //else if()
                            return getDate(o1.getTimeInMillis(),"MM/dd/yyyy HH:mm:ss").compareTo(getDate(o2.getTimeInMillis(),"MM/dd/yyyy HH:mm:ss"));

                        }




                    });
                    listEventTimeSort(lista_datumi);
                    listEventTimeSort(listPreview);
                }

            } catch (Exception e) {
                Log.i("ERROR", e.getMessage());

            }
        }
////////////////////////////Update event///////////////////////////////////////////


        else if (requestCode == 3) {

            try {

                if (data.getLongExtra("Time", -1) != -1) {


                    int position1=getIntent().getIntExtra("position",0);
                    String data_new = data.getStringExtra("Data");
                    long time = data.getLongExtra("Time", 0);
                    int color = data.getIntExtra("Color", 0);
                    listEventTimeSort(lista_datumi);

                    Event e = new Event(color, time, data_new);




                    //Visak koda

                   // db.deleteEvent(listPreview.get(position1));

                   // db.addEvent(e);

                  /*  listPreview.remove(position1);
                    //adapter.notifyDataSetChanged();
                    listPreview.add(position1, e);
                    listViewDatumi.setAdapter(adapterPreview);
                    listEventTimeSort(listPreview);*/
                    //adapter.notifyDataSetChanged();


                    if (today_Value == 1) {

                        today.performClick();


                    } else if (month_Value == 2)

                    {

                        month.performClick();


                    } else if (all_events_Value == 3) {

                        all_events.performClick();


                    } else if (empty_Value == 4) {

                       // listViewDatumi.setVisibility(View.INVISIBLE);
                        groupButtons.clearCheck();
                        today.setSelected(false);
                        month.setSelected(false);
                        all_events.setSelected(false);
                    ////////////////////////////////////////////
                        listPreview = new ArrayList<Event>();
                        lista_datumi= (ArrayList<Event>) db.getAllEvents();

                        for(int j=0;j<lista_datumi.size();j++){

                            if(pokupljeni_datum==lista_datumi.get(j).getTimeInMillis())
                            {

                                listPreview.add(lista_datumi.get(j));

                            }


                        }


                        adapterPreview = new EventAdd_adapter(getApplicationContext(), listPreview);
                        listViewDatumi.setAdapter(adapterPreview);
                        listEventTimeSort(listPreview);


                    }





                }



            } catch (Exception e) {
                Log.i("ERROR", e.getMessage());

            }
        }

    }
    public void didTapButton(View view) {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce_fast);
        myAnim.setStartOffset(450);
        view.startAnimation(myAnim);


        view.startAnimation(myAnim);
    }

    @Override
    public void onBackPressed() {
       /* if (!turnOf) {

            //calendar.add(Calendar.DAY_OF_YEAR, 1);
            //Date tomorrow = calendar.getTime();


            //calendar1.set(Calendar.HOUR_OF_DAY,2);
            //calendar1.set(Calendar.MINUTE,40);
            intent = new Intent(getApplicationContext(), NotificationReciever.class);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


            //  String tomorrowAsString = dateFormat.format(tomorrow);


            if (!lista_datumi.isEmpty()) {

                Calendar calendar=Calendar.getInstance();
                ArrayList<Event> notificationList=new ArrayList<>(lista_datumi);

                for(int i=0;i<notificationList.size();i++){


                    String eventDate=getDate(notificationList.get(i).getTimeInMillis(),"dd/MM/yyyy");
                    String todayEvent=getDate(calendar.getTimeInMillis(),"dd/MM/yyyy");

                    if(System.currentTimeMillis()>notificationList.get(i).getTimeInMillis())
                    {
                        notificationList.remove(notificationList.get(i));
                    }

                }

                if(!notificationList.isEmpty()) {

                    Calendar calendar1 = Calendar.getInstance();


                    calendar1.setTimeInMillis(notificationList.get(0).getTimeInMillis());

                    calendar1.set(Calendar.DAY_OF_YEAR, calendar1.get(Calendar.DAY_OF_YEAR)-1);
                    //calendar1.set(Calendar.MONTH,calendar1.get(Calendar.MONTH)+1);
                    calendar1.set(Calendar.HOUR_OF_DAY, 13);
                    calendar1.set(Calendar.MINUTE, 55);
                    //calendar1.set(Calendar.MINUTE, 8);


                    // DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    //String stringBeforeEvent = dateFormat.format(dateBeforeEvent);
                    Calendar calendarCheck=  Calendar.getInstance();



                    //if(calendar1.HOUR_OF_DAY <= calendarCheck.HOUR_OF_DAY &&calendarCheck.MINUTE<=calendar1.MINUTE) {

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), alarmManager.INTERVAL_DAY, pendingIntent);
                   /// }else{


                   // }

                }
            }
        }

        */
        Intent i=new Intent(getApplicationContext(),MActivity.class);
        startActivity(i);
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

    }
}
