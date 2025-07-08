package com.group5.estoreapp.helpers;


import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    public static int getUserId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        return pref.getInt("userId", -1);
    }
}

