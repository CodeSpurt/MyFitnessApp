package com.codespurt.myfitnessapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codespurt.myfitnessapp.Prefs;
import com.codespurt.myfitnessapp.R;
import com.codespurt.myfitnessapp.SplashActivity;

public class WorkoutDetailFragment extends Fragment implements View.OnClickListener {

    // toolbar options
    private TextView tv_start, tv_stop;

    // sets
    private ImageView sets_plus, sets_minus;
    private TextView sets_counter;

    // repeat
    private ImageView repeat_plus, repeat_minus;
    private TextView repeat_counter;

    // rest timer
    private ImageView timer_plus, timer_minus;
    private TextView timer_counter;

    private Button btn_save, btn_reset;

    // default values
    private int setCounter = 0, repeatCounter = 0, timerCounter = 0;

    // shared preference
    Prefs prefs = null;

    private String TAG = "ExampleFragmentExample";
    // for toast message, sets completed
    int countr = 0;

    private int num_of_sets;
    private Handler handler;

    // time taken for one move
    private int seconds_per_move = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_detail, container, false);
        prefs = new Prefs(getContext());
        setViews(view);
        setListeners();
        setDataFromPrefs();
        handler = new Handler();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sets_plus:
                setCounter = setCounter + 4;
                sets_counter.setText(String.valueOf(setCounter));
                break;
            case R.id.sets_minus:
                if (setCounter > 0) {
                    setCounter = setCounter - 4;
                    sets_counter.setText(String.valueOf(setCounter));
                } else {
                    showToast(getResources().getString(R.string.value_cannot_be_negative));
                }
                break;
            case R.id.repeat_plus:
                repeatCounter = repeatCounter + 1;
                repeat_counter.setText(String.valueOf(repeatCounter));
                break;
            case R.id.repeat_minus:
                if (repeatCounter > 0) {
                    repeatCounter = repeatCounter - 1;
                    repeat_counter.setText(String.valueOf(repeatCounter));
                } else {
                    showToast(getResources().getString(R.string.value_cannot_be_negative));
                }
                break;
            case R.id.timer_plus:
                timerCounter = timerCounter + 5;
                timer_counter.setText(String.valueOf(timerCounter));
                break;
            case R.id.timer_minus:
                if (timerCounter > 0) {
                    timerCounter = timerCounter - 5;
                    timer_counter.setText(String.valueOf(timerCounter));
                } else {
                    showToast(getResources().getString(R.string.value_cannot_be_negative));
                }
                break;

            case R.id.tv_start:
                if (setCounter == 0 || repeatCounter == 0 || timerCounter == 0) {
                    showToast(getResources().getString(R.string.select_data_to_save));
                } else {
                    sets_plus.setVisibility(View.INVISIBLE);
                    sets_minus.setVisibility(View.INVISIBLE);
                    repeat_plus.setVisibility(View.INVISIBLE);
                    repeat_minus.setVisibility(View.INVISIBLE);
                    timer_plus.setVisibility(View.INVISIBLE);
                    timer_minus.setVisibility(View.INVISIBLE);
                    tv_start.setVisibility(View.INVISIBLE);
                    tv_stop.setVisibility(View.VISIBLE);
                    btn_save.setVisibility(View.INVISIBLE);
                    btn_reset.setVisibility(View.INVISIBLE);
                    num_of_sets = repeatCounter;
                    startTimers();
                }
                break;
            case R.id.tv_stop:
                sets_plus.setVisibility(View.VISIBLE);
                sets_minus.setVisibility(View.VISIBLE);
                repeat_plus.setVisibility(View.VISIBLE);
                repeat_minus.setVisibility(View.VISIBLE);
                timer_plus.setVisibility(View.VISIBLE);
                timer_minus.setVisibility(View.VISIBLE);
                tv_start.setVisibility(View.VISIBLE);
                tv_stop.setVisibility(View.INVISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                btn_reset.setVisibility(View.VISIBLE);
                stopTimers();
                break;
            case R.id.sets_counter:
                break;
            case R.id.repeat_counter:
                break;
            case R.id.timer_counter:
                break;

            case R.id.btn_save:
                if (setCounter == 0 || repeatCounter == 0 || timerCounter == 0) {
                    showToast(getResources().getString(R.string.select_data_to_save));
                } else {
                    prefs.setSets(String.valueOf(setCounter));
                    prefs.setRepeat(String.valueOf(repeatCounter));
                    prefs.setTimer(String.valueOf(timerCounter));
                    showToast(getResources().getString(R.string.saved_successfully));
                }
                break;
            case R.id.btn_reset:
                stopTimers();
                setCounter = 0;
                repeatCounter = 0;
                timerCounter = 0;
                sets_counter.setText(String.valueOf(setCounter));
                repeat_counter.setText(String.valueOf(repeatCounter));
                timer_counter.setText(String.valueOf(timerCounter));
                break;
        }
    }

    private void setViews(View view) {
        sets_plus = (ImageView) view.findViewById(R.id.sets_plus);
        sets_minus = (ImageView) view.findViewById(R.id.sets_minus);
        repeat_plus = (ImageView) view.findViewById(R.id.repeat_plus);
        repeat_minus = (ImageView) view.findViewById(R.id.repeat_minus);
        timer_plus = (ImageView) view.findViewById(R.id.timer_plus);
        timer_minus = (ImageView) view.findViewById(R.id.timer_minus);

        tv_start = (TextView) view.findViewById(R.id.tv_start);
        tv_stop = (TextView) view.findViewById(R.id.tv_stop);
        sets_counter = (TextView) view.findViewById(R.id.sets_counter);
        repeat_counter = (TextView) view.findViewById(R.id.repeat_counter);
        timer_counter = (TextView) view.findViewById(R.id.timer_counter);

        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_reset = (Button) view.findViewById(R.id.btn_reset);
    }

    private void setListeners() {
        sets_plus.setOnClickListener(this);
        sets_minus.setOnClickListener(this);
        repeat_plus.setOnClickListener(this);
        repeat_minus.setOnClickListener(this);
        timer_plus.setOnClickListener(this);
        timer_minus.setOnClickListener(this);

        tv_start.setOnClickListener(this);
        tv_stop.setOnClickListener(this);
        sets_counter.setOnClickListener(this);
        repeat_counter.setOnClickListener(this);
        timer_counter.setOnClickListener(this);

        btn_save.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
    }

    private void setDataFromPrefs() {
        if (prefs.getSets().equals(getResources().getString(R.string.default_start_value)) || prefs.getRepeat().equals(getResources().getString(R.string.default_start_value)) || prefs.getTimer().equals(getResources().getString(R.string.default_start_value))) {

        } else {
            setCounter = Integer.parseInt(prefs.getSets());
            repeatCounter = Integer.parseInt(prefs.getRepeat());
            timerCounter = Integer.parseInt(prefs.getTimer());
            sets_counter.setText(String.valueOf(setCounter));
            repeat_counter.setText(String.valueOf(repeatCounter));
            timer_counter.setText(String.valueOf(timerCounter));
            showToast(getResources().getString(R.string.saved_data_loaded_successfully));
        }
    }

    private void playSound() {
        try {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            MediaPlayer mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(getContext(), soundUri);
            final AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(false);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimers() {
        playSound();
        CountDownTimer countDownTimer = new CountDownTimer(setCounter * seconds_per_move * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int remaining = (int) (millisUntilFinished / 1000);
                sets_counter.setText(String.valueOf(remaining));
            }

            @Override
            public void onFinish() {
                if (countr < num_of_sets) {
                    countr++;
                }
                showToast(String.valueOf(countr) + " " + getResources().getString(R.string.set_completed));
                sets_counter.setText(String.valueOf(getResources().getString(R.string.default_start_value)));
                playSound();
                checkForRepeats();
            }
        };
        countDownTimer.start();
    }

    private void stopTimers() {
        playSound();
        Intent i = new Intent(getContext(), SplashActivity.class);
        startActivity(i);
    }

    private void checkForRepeats() {
        repeatCounter = repeatCounter - 1;
        if (repeatCounter > 0) {
            repeat_counter.setText(String.valueOf(repeatCounter));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startTimers();
                }
            }, timerCounter * 1000);
        } else {
            repeat_counter.setText(String.valueOf(getResources().getString(R.string.default_start_value)));
            showToast(getResources().getString(R.string.workout_completed));
            stopTimers();
            playSound();
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
