package com.balakrishnan.poorna.absenseimitationbhel;

import android.graphics.drawable.Drawable;

public class Contacts {
    Drawable id;
    String name;

    Contacts(){};
    Contacts(String name,Drawable id){
        this.id=id;//R.id.button5;
        this.name=name;

    }



    public Drawable getId() {
        return this.id;
    }


    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }
}

