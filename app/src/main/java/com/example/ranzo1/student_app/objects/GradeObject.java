package com.example.ranzo1.student_app.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ranzo1 on 2.6.2017..
 */

public class GradeObject implements Parcelable {

    private String shortName;
   // private ArrayList<String>exams;
    private String typeOfExam;
    private float points,sumPoints;
    private int id;
    private String additional;

    public GradeObject(){

    }

    public GradeObject(String shortName,String typeOfExam ,float points,String additional,float sumPoints,int id){

        this.shortName=shortName;
        this.typeOfExam=typeOfExam;
        this.id=id;
        this.points=points;
        this.additional=additional;
        this.sumPoints=sumPoints;

    }

    public GradeObject(String shortName,String typeOfExam ,float points,float sumPoints,int id){

        this.shortName=shortName;
        this.typeOfExam=typeOfExam;
        this.id=id;
        this.points=points;
        this.sumPoints=sumPoints;

    }

    public GradeObject(Parcel parcel){


        this.shortName=parcel.readString();
        this.typeOfExam=parcel.readString();
        this.points=parcel.readFloat();
        this.additional=parcel.readString();
        this.sumPoints=parcel.readFloat();
        this.id=parcel.readInt();


    }
    /*public GradeObject(String shortName,String typeOfExam ,double points,String additional){

        this.shortName=shortName;
        this.typeOfExam=typeOfExam;
        this.id=id;
        this.points=points;
        this.additional=additional;

    }*/

    public static final Creator<GradeObject> CREATOR = new Creator<GradeObject>() {
        @Override
        public GradeObject createFromParcel(Parcel in) {
            return new GradeObject(in);
        }

        @Override
        public GradeObject[] newArray(int size) {
            return new GradeObject[size];
        }
    };

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getTypeOfExam() {
        return typeOfExam;
    }

    public void setTypeOfExam(String exams) {
        this.typeOfExam = exams;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdditional() {
        return additional;
    }


    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public float getSumPoints() {
        return sumPoints;
    }

    public void setSumPoints(float sumPoints) {
        this.sumPoints = sumPoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shortName);
        dest.writeString(typeOfExam);
        dest.writeFloat(points);
        dest.writeString(additional);
        dest.writeFloat(sumPoints);
        dest.writeInt(id);


    }
}
