package com.example.ranzo1.student_app.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ranzo1 on 13.12.2016..
 */

public class TimeTableObject implements Parcelable {


    private String day;
    private String shortName;
    private String start;
    private String end;
    private String additional;
    private int color,id;

    public TimeTableObject(){

    }


    public TimeTableObject(String day, String shortName, String start, String end, String additional, int color,int id) {
        this.shortName = shortName;
        this.day = day;
        this.start = start;
        this.end = end;
        this.additional = additional;
        this.color=color;
        this.id=id;
    }

    public TimeTableObject(String day, String shortName, String start, String end, int color,int id) {
        this.shortName = shortName;
        this.day = day;
        this.start = start;
        this.end = end;
        this.color=color;
        this.id=id;

    }



    public TimeTableObject(Parcel parcel){

        this.day=parcel.readString();
        this.shortName=parcel.readString();
        this.start=parcel.readString();
        this.end=parcel.readString();
        this.additional=parcel.readString();
        this.color=parcel.readInt();
        this.id=parcel.readInt();


    }

    public static final Creator<TimeTableObject> CREATOR=new Creator<TimeTableObject>() {
        @Override
        public TimeTableObject createFromParcel(Parcel parcel) {
            return new TimeTableObject(parcel);
        }

        @Override
        public TimeTableObject[] newArray(int i) {
            return new TimeTableObject[i];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.day);
        parcel.writeString(this.shortName);
        parcel.writeString(this.start);
        parcel.writeString(this.end);
        parcel.writeString(this.additional);
        parcel.writeInt(this.color);
        parcel.writeInt(this.id);


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public static Creator<TimeTableObject> getCREATOR() {
        return CREATOR;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}



