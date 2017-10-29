package com.example.ranzo1.student_app.objects;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ranzo1 on 21.11.2016..
 */

public class Subject implements Parcelable {


    private String name;
    private String shortName;
    private int color;
    private String professor;
    private String asisstent;
    private String profEmail;
    private String asistEmail;
    private String profCabinet;
    private String asistCabinet;
    private String profImage;
    private String assistImage;
    private int id;


/*public Subject(String shortName){
        super();
    }*/

    public Subject(){

    }

    public Subject(String name,String shortName,int color)
    {
        this.name=name;
        this.shortName=shortName;
        this.color=color;
    }

    public Subject(Parcel parcel){

        this.name=parcel.readString();
        this.shortName=parcel.readString();
        this.professor=parcel.readString();
        this.profEmail=parcel.readString();
        this.asisstent=parcel.readString();
        this.asistEmail=parcel.readString();
        this.color=parcel.readInt();
        this.profCabinet=parcel.readString();
        this.asistCabinet=parcel.readString();
        this.profImage=parcel.readString();
        this.assistImage=parcel.readString();
        this.id=parcel.readInt();


    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.name);
        parcel.writeString(this.shortName);
        parcel.writeString(this.professor);
        parcel.writeString(this.profEmail);
        parcel.writeString(this.asisstent);
        parcel.writeString(this.asistEmail);
        parcel.writeInt(this.color);
        parcel.writeString(this.profCabinet);
        parcel.writeString(this.asistCabinet);
        parcel.writeString(this.profImage);
        parcel.writeString(this.assistImage);
        parcel.writeInt(this.id);

    }

    public static final Creator<Subject> CREATOR=new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel parcel) {
            return new Subject(parcel);
        }

        @Override
        public Subject[] newArray(int i) {
            return new Subject[i];
        }
    };

    public Subject(String name, String shortName, String professor, String profEmail, String asisstent, String asistEmail,int color,int id)
    {
        this.name = name;
        this.shortName = shortName;
        this.color = color;
        this.professor = professor;
        this.asisstent = asisstent;
        this.profEmail = profEmail;
        this.asistEmail = asistEmail;
        this.id = id;

    }
    public Subject(String name, String shortName, String professor, String profEmail, String asisstent, String asistEmail,int color,
                   String profCabinet,String asistCabinet)
    {
        this.name=name;
        this.shortName=shortName;
        this.color=color;
        this.professor=professor;
        this.asisstent=asisstent;
        this.profEmail=profEmail;
        this.asistEmail=asistEmail;
        this.profCabinet=profCabinet;
        this.asistCabinet=asistCabinet;

    }
    public Subject(String name, String shortName, String professor, String profEmail, String asisstent, String asistEmail,int color,
                   String profCabinet,String asistCabinet,int id)
    {
        this.name=name;
        this.shortName=shortName;
        this.color=color;
        this.professor=professor;
        this.asisstent=asisstent;
        this.profEmail=profEmail;
        this.asistEmail=asistEmail;
        this.profCabinet=profCabinet;
        this.asistCabinet=asistCabinet;
        this.id=id;

    }

    public Subject(String name, String shortName, String professor, String profEmail, String asisstent, String asistEmail,int color,
                   String profCabinet,String asistCabinet,String profImage,String assistImage,int id)
    {
        this.name=name;
        this.shortName=shortName;
        this.color=color;
        this.professor=professor;
        this.asisstent=asisstent;
        this.profEmail=profEmail;
        this.asistEmail=asistEmail;
        this.profCabinet=profCabinet;
        this.asistCabinet=asistCabinet;
        this.profImage=profImage;
        this.assistImage=assistImage;
        this.id=id;

    }

    public Subject(String name,String shortName,String professor,String profEmail,String asisstent,String asistEmail)
    {

        this.asistEmail=asistEmail;
        this.asisstent=asisstent;
        this.name=name;
        this.shortName=shortName;
        this.color=color;
        this.professor=professor;
        this.profEmail=profEmail;

    }


    public String getProfImage() {
        return profImage;
    }

    public void setProfImage(String profImage) {
        this.profImage = profImage;
    }

    public String getAssistImage() {
        return assistImage;
    }

    public void setAssistImage(String assistImage) {
        this.assistImage = assistImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getAsisstent() {
        return asisstent;
    }

    public void setAsisstent(String asisstent) {
        this.asisstent = asisstent;
    }

    public String getProfEmail() {
        return profEmail;
    }

    public void setProfEmail(String profEmail) {
        this.profEmail = profEmail;
    }

    public String getAsistEmail() {
        return asistEmail;
    }

    public void setAsistEmail(String asistEmail) {
        this.asistEmail = asistEmail;
    }

    public String getProfCabinet() {
        return profCabinet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAsistCabinet() {
        return asistCabinet;
    }

    public void setProfCabinet(String profCabinet) {
        this.profCabinet = profCabinet;
    }

    public void setAsistCabinet(String asistCabinet) {
        this.asistCabinet = asistCabinet;
    }
}



