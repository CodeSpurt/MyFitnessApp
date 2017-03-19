package com.codespurt.myfitnessapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.codespurt.myfitnessapp.fragments.WorkoutDetailFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        WorkoutDetailFragment workoutDetailFragment = new WorkoutDetailFragment();
        fragmentTransaction.add(R.id.fragment_container, workoutDetailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getResources().getString(R.string.not_allowed), Toast.LENGTH_SHORT).show();
    }
}
