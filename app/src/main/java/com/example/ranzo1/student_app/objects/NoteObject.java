package com.example.ranzo1.student_app.objects;

import java.util.ArrayList;

/**
 * Created by ranzo1 on 9.5.2017..
 */

public class NoteObject extends Subject {

    private String name;
    private String shortName;
    private int color;

    private ArrayList<String> toDoList;


    public NoteObject (){

    }


    public NoteObject (String name,String shortName, int color,ArrayList<String> toDoList){
            super(name,shortName,color);
            this.toDoList=toDoList;

    }

}
