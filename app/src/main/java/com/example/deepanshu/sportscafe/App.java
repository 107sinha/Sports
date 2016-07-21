package com.example.deepanshu.sportscafe;

import android.app.Application;

/**
 * Created by deepanshu on 21/7/16.
 */

public class App extends Application {

    public static final String STUDENTLIST = "STUDENTLISTS";
    public static final String ID = "id";
    public static final String NAME = "student";
    public static final String ADDR = "address";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String IMAGE = "image";


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
