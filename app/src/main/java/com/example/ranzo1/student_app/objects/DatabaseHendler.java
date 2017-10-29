package com.example.ranzo1.student_app.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.ranzo1.student_app.adapters.GradesAdapter2;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ranzo1 on 26.1.2017..
 */

public class DatabaseHendler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=25;
    private static final String DATABASE_NAME="subjectsDatabase";

    //Subject table columns
    ////////////////////////Subject///////////////////////////////////////
    private static final String TABLE_SUBJECTS ="subjects";
    private static final String SUBJECT_ID ="id";
    private static final String SUBJECT_NAME ="NAME";
    private static final String SUBJECT_SHORT_NAME ="SHORT_NAME";
    private static final String SUBJECT_PROF_NAME ="PROF_NAME";
    private static final String SUBJECT_PROF_EMAIL ="PROF_EMAIL";
    private static final String SUBJECT_ASSIST_NAME ="ASSIST_NAME";
    private static final String SUBJECT_ASSIST_EMAIL ="ASSIST_EMAIL";
    private static final String SUBJECT_COLOR ="COLOR";
    private static final String SUBJECT_PROF_CABINET ="PROF_CABINET";
    private static final String SUBJECT_ASSIST_CABINET ="ASSIST_CABINET";
    private static final String SUBJECT_PROF_IMAGE ="PROF_IMAGE";
    private static final String SUBJECT_ASSIST_IMAGE ="ASSIST_IMAGE";

    /////////////////////////Timetable//////////////////////////////////////
    private static final String TABLE_TIMETABLE="timetable";
    private static final String TIMETABLE_ID="id";
    private static final String TIMETABLE_DAY="DAY";
    private static final String TIMETABLE_SHORT_NAME="SHORT_NAME";
    private static final String TIMETABLE_START="START";
    private static final String TIMETABLE_END="END";
    private static final String TIMETABLE_ADDITIONAL="ABOUT";
    private static final String TIMETABLE_COLOR="COLOR";
    /////////////////////////Events///////////////////////////////////////////
    private static final String TABLE_EVENTS="events";
    private static final String EVENTS_ID="ID";
    private static final String EVENTS_DATA="DATA";
    private static final String EVENTS_TIME="TIME";
    private static final String EVENTS_COLOR="COLOR";
    ///////////////////////////Notes///////////////////////////////////////////
    //0-name,1-skracen naziv,2-sadrzaj-beleske,3-naslov
    private static final String TABLE_NOTES="notes";

    private static final String NOTES_ID="id";
    private static final String NOTES_NAME="NAME";
    private static final String NOTES_SHORT_NAME="SHORT_NAME";
    private static final String NOTES_TEXT="TEXTS";
    private static final String NOTES_TITLE="TITLE";
    private static final String NOTES_DATE="DATE";
    private static final String NOTES_COLOR="COLOR";

    ///////////////////////////Grades//////////////////////////////////////////

    private static final String TABLE_GRADES="grades";

    private static final String GRADE_ID="id";
    private static final String GRADE_TYPE_OF_EXAM="TYPE";
    private static final String GRADE_POINTS="POINTS";
    private static final String GRADE_ADDITIONAL="ADDITIONAL";
    private static final String GRADE_SUM_POINTS="SUM_POINTS";
    private static final String GRADE_SHORT_NAME="SHORT_NAME";




    private static DatabaseHendler sInstance;


    //private Context context;




    public DatabaseHendler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " +
                TABLE_SUBJECTS + "("+
                SUBJECT_ID +" integer PRIMARY KEY,"+
                SUBJECT_NAME +" TEXT,"+
                SUBJECT_SHORT_NAME +" TEXT," +
                SUBJECT_PROF_NAME +" TEXT,"+
                SUBJECT_PROF_EMAIL + " TEXT,"+
                SUBJECT_ASSIST_NAME +" TEXT,"+
                SUBJECT_ASSIST_EMAIL +" TEXT,"+
                SUBJECT_COLOR + " INTEGER,"+
                SUBJECT_PROF_CABINET + " TEXT,"+
                SUBJECT_ASSIST_CABINET +" TEXT,"+
                SUBJECT_PROF_IMAGE + " TEXT,"+
                SUBJECT_ASSIST_IMAGE + " TEXT)"
        );

        db.execSQL("CREATE TABLE "+ TABLE_TIMETABLE + "(" +
                TIMETABLE_ID +" integer PRIMARY KEY," +
                TIMETABLE_START+ " TEXT," +
                TIMETABLE_END + " TEXT," +
                TIMETABLE_DAY + " TEXT," +
                TIMETABLE_COLOR + " INTEGER," +
                TIMETABLE_ADDITIONAL +" TEXT,"+
                TIMETABLE_SHORT_NAME + " TEXT)");


        db.execSQL("CREATE TABLE "+ TABLE_EVENTS + "(" +
                EVENTS_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                EVENTS_DATA +" TEXT KEY," +
                EVENTS_TIME+ " TEXT," +
                EVENTS_COLOR + " INTEGER)");

        //0-name,1-skracen naziv,2-sadrzaj-beleske,3-naslov

        db.execSQL("CREATE TABLE "+ TABLE_NOTES + "(" +
                NOTES_ID +" integer PRIMARY KEY AUTOINCREMENT," +
                NOTES_NAME + " TEXT,"+
                NOTES_SHORT_NAME + " TEXT," +
                NOTES_TEXT + " TEXT,"+
                NOTES_TITLE+ " TEXT,"+
                NOTES_DATE + " TEXT,"+
                NOTES_COLOR+ " INTEGER)");

//////////////////////////////////////////////

        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_GRADES + "(" +
                GRADE_ID +" integer PRIMARY KEY AUTOINCREMENT," +
                GRADE_SHORT_NAME + " TEXT,"+
                GRADE_TYPE_OF_EXAM + " TEXT," +
                GRADE_POINTS + " REAL,"+
                GRADE_ADDITIONAL + " TEXT,"+
                GRADE_SUM_POINTS + " REAL)");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);

            onCreate(db);
        }
    }


    public static synchronized DatabaseHendler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHendler(context.getApplicationContext());
        }
        return sInstance;
    }


    public void addGrade(GradeObject g){
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).


            ContentValues contentValues = new ContentValues();
            contentValues.put(GRADE_ID,g.getId());
            contentValues.put(GRADE_SHORT_NAME, g.getShortName());
            contentValues.put(GRADE_TYPE_OF_EXAM, g.getTypeOfExam());
            contentValues.put(GRADE_POINTS, g.getPoints());
            contentValues.put(GRADE_ADDITIONAL, g.getAdditional());
            contentValues.put(GRADE_SUM_POINTS, g.getSumPoints());



            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_GRADES, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }

    }


    //SQLiteDatabase db = this.getWritableDatabase();


    public void addTimetable(TimeTableObject t){
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).


            ContentValues contentValues = new ContentValues();
            contentValues.put(TIMETABLE_ID,t.getId());
            contentValues.put(TIMETABLE_START, t.getStart());
            contentValues.put(TIMETABLE_END, t.getEnd());
            contentValues.put(TIMETABLE_DAY, t.getDay());
            contentValues.put(TIMETABLE_COLOR, t.getColor());
            contentValues.put(TIMETABLE_ADDITIONAL, t.getAdditional());
            contentValues.put(TIMETABLE_SHORT_NAME,t.getShortName());



            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_TIMETABLE, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }

    }

    public void addEvent(Event event){
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            String strTime = Long.toString(event.getTimeInMillis());

            ContentValues contentValues = new ContentValues();
            contentValues.put(EVENTS_DATA, (String) event.getData());
            contentValues.put(EVENTS_TIME,strTime);
            contentValues.put(EVENTS_COLOR,event.getColor());




            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_EVENTS, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }

    }




    public void addSubject(Subject s) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).


            ContentValues contentValues = new ContentValues();
            contentValues.put(SUBJECT_ID,s.getId());
            contentValues.put(SUBJECT_NAME, s.getName());
            contentValues.put(SUBJECT_SHORT_NAME, s.getShortName());
            contentValues.put(SUBJECT_PROF_NAME, s.getProfessor());
            contentValues.put(SUBJECT_PROF_EMAIL, s.getProfEmail());
            contentValues.put(SUBJECT_ASSIST_NAME, s.getAsisstent());
            contentValues.put(SUBJECT_ASSIST_EMAIL, s.getAsistEmail());
            contentValues.put(SUBJECT_COLOR, s.getColor());
            contentValues.put(SUBJECT_PROF_CABINET, s.getProfCabinet());
            contentValues.put(SUBJECT_ASSIST_CABINET, s.getAsistCabinet());
            contentValues.put(SUBJECT_PROF_IMAGE, s.getProfImage());
            contentValues.put(SUBJECT_ASSIST_IMAGE, s.getAssistImage());


            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_SUBJECTS, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public void addNote(Subject n){
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            //0-name,1-skracen naziv,2-sadrzaj-beleske,3-naslov,4 -datum

            ContentValues contentValues = new ContentValues();

            contentValues.put(NOTES_ID,n.getId());
            contentValues.put(NOTES_NAME,n.getName());
            contentValues.put(NOTES_SHORT_NAME,n.getShortName());
            contentValues.put(NOTES_TEXT,n.getProfessor());
            contentValues.put(NOTES_TITLE,n.getProfEmail());
            contentValues.put(NOTES_DATE,n.getAsisstent());
            contentValues.put(NOTES_COLOR,n.getColor());




            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_NOTES, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }

    }

    public List <GradeObject> getAllGrades(){
        List<GradeObject> gradesList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GRADES;

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    /*
                    ContentValues contentValues = new ContentValues();
                 contentValues.put(GRADE_ID,g.getId());
                contentValues.put(GRADE_SHORT_NAME, g.getShortName());
                contentValues.put(GRADE_TYPE_OF_EXAM, g.getTypeOfExam());
                contentValues.put(GRADE_POINTS, g.getPoints());
                contentValues.put(GRADE_ADDITIONAL, g.getAdditional());
                contentValues.put(GRADE_SUM_POINTS, g.getSumPoints());
                     */
                    GradeObject g=new GradeObject();
                    g.setId(Integer.parseInt((cursor.getString(0))));
                    g.setShortName(cursor.getString(1));
                    g.setTypeOfExam(cursor.getString(2));
                    g.setPoints(Float.valueOf(cursor.getString(3)));
                    g.setAdditional((cursor.getString(4)));
                    g.setSumPoints(Float.valueOf(cursor.getString(5)));



                    addGrade(g);
                    gradesList.add(g);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return gradesList;

    }


    public List <TimeTableObject> getAllTimeTable(){
        List<TimeTableObject> timeTableList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_TIMETABLE;

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    TimeTableObject t=new TimeTableObject();
                    t.setId(Integer.parseInt((cursor.getString(0))));
                    t.setStart(cursor.getString(1));
                    t.setEnd(cursor.getString(2));
                    t.setDay(cursor.getString(3));
                    t.setColor(Integer.parseInt(cursor.getString(4)));
                    t.setAdditional(cursor.getString(5));
                    t.setShortName(cursor.getString(6));

                    addTimetable(t);
                    timeTableList.add(t);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return timeTableList;

    }
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public List <Subject> getAllNotes(){
        List<Subject> notesList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NOTES;

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                     /*
            contentValues.put(NOTES_ID,n.getId());0
            contentValues.put(NOTES_NAME,n.getName());1
            contentValues.put(NOTES_SHORT_NAME,n.getShortName());2
            contentValues.put(NOTES_TEXT,n.getProfessor());3
            contentValues.put(NOTES_TITLE,n.getProfEmail());4
            contentValues.put(NOTES_DATE,n.getAsisstent());5
            contentValues.put(NOTES_COLOR,n.getColor());6*/


                    Subject n= new Subject();
                    n.setId(Integer.parseInt((cursor.getString(0))));
                    n.setName(cursor.getString(1));
                    n.setShortName(cursor.getString(2));
                    n.setProfessor(cursor.getString(3));
                    n.setProfEmail(cursor.getString(4));
                    n.setAsisstent(cursor.getString(5));
                    n.setColor(Integer.parseInt(cursor.getString(6)));



                    addNote(n);
                    notesList.add(n);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return notesList;

    }



    public List<Subject> getAllSubjects() {
        List<Subject> subjectList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " +TABLE_SUBJECTS;

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Subject subject=new Subject();
                    subject.setId(Integer.parseInt((cursor.getString(0))));
                    subject.setName(cursor.getString(1));
                    subject.setShortName(cursor.getString(2));
                    subject.setProfessor(cursor.getString(3));
                    subject.setProfEmail(cursor.getString(4));
                    subject.setAsisstent(cursor.getString(5));
                    subject.setAsistEmail(cursor.getString(6));
                    subject.setColor(Integer.parseInt(cursor.getString(7)));
                    subject.setProfCabinet(cursor.getString(8));
                    subject.setAsistCabinet(cursor.getString(9));
                    subject.setProfImage(cursor.getString((10)));
                    subject.setAssistImage(cursor.getString((11)));


                    addSubject(subject);
                    subjectList.add(subject);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return subjectList;
    }

    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " +TABLE_EVENTS;

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    Event e =new Event(Integer.valueOf(cursor.getString(3)),Long.valueOf(cursor.getString(2)),cursor.getString(1));

                    eventList.add(e);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return eventList;
    }

    public void deleteGrade(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GRADES, GRADE_ID + " =?",new String[]  {String.valueOf(id)});
        db.close();

    }

    public void deleteEvent(Event event){

        SQLiteDatabase db = this.getWritableDatabase();

       // String selectQuery = "SELECT ID FROM " + TABLE_EVENTS + " WHERE "+;


        db.delete(TABLE_EVENTS, EVENTS_DATA + " =?",new String[] {(String) event.getData()});
        db.close();

    }
    public  void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, NOTES_ID + " =?",new String[] {String.valueOf(id)});
        db.close();
    }

    public void deleteTimeTableObject(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIMETABLE, TIMETABLE_ID + " =?",new String[] {String.valueOf(id)});
        db.close();

    }

    public  void deleteSubject(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBJECTS, SUBJECT_ID + " =?",new String[] {String.valueOf(id)});
        db.close();
    }




    ///////////////////////////function for update database,not used/////////////////////////////////////////////
/*
    public long addOrUpdateUser(User user) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_NAME, user.userName);
            values.put(KEY_USER_PROFILE_PICTURE_URL, user.profilePictureUrl);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER_NAME + "= ?", new String[]{user.userName});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.userName)});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }*/

//------------------------------------------------------------------------------------------------------------------------------//






}
