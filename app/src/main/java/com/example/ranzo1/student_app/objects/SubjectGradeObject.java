package com.example.ranzo1.student_app.objects;

/**
 * Created by ranzo1 on 5.6.2017..
 */

public class SubjectGradeObject {

    private String subjectName;
    private float point;
    private int grade;
    private int color;


    public SubjectGradeObject(String subjectName,int color){

        this.subjectName=subjectName;
        this.color=color;

    }
    public SubjectGradeObject(String subjectName,float point,int grade,int color){

        this.subjectName=subjectName;
        this.point=point;
        this.grade=grade;
        this.color=color;

    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
