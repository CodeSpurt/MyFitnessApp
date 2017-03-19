package com.codespurt.myfitnessapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String SETS = "sets";
    private String REPEAT = "repeat";
    private String REST_TIMER = "rest_timer";

    public Prefs(Context context) {
        this.context = context;
        setFile();
    }

    private void setFile() {
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    // sets
    public void setSets(String sets) {
        editor = sharedPreferences.edit();
        editor.putString(SETS, sets);
        editor.apply();
    }

    public String getSets() {
        return sharedPreferences.getString(SETS, "0");
    }

    // repeat
    public void setRepeat(String repeat) {
        editor = sharedPreferences.edit();
        editor.putString(REPEAT, repeat);
        editor.apply();
    }

    public String getRepeat() {
        return sharedPreferences.getString(REPEAT, "0");
    }

    // timer
    public void setTimer(String timer) {
        editor = sharedPreferences.edit();
        editor.putString(REST_TIMER, timer);
        editor.apply();
    }

    public String getTimer() {
        return sharedPreferences.getString(REST_TIMER, "0");
    }
}
