package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    private EditText mEditTimerInput;
    private TextView mTextViewCountDown;

    private ProgressBar mProgressBar;

    private Button mButtonEdit;
    private Button mButtonStart;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis = mStartTimeInMillis;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creation of navigation Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Creation of Timer views
        mTextViewCountDown = (TextView)findViewById(R.id.textViewCountdown);
        mEditTimerInput = (EditText)findViewById(R.id.editTextInput);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        mButtonEdit = (Button)(findViewById(R.id.buttonEdit));
        mButtonStart = (Button)findViewById(R.id.buttonStart);
        mButtonReset = (Button)findViewById(R.id.buttonReset);

        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mEditTimerInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MainActivity.this, "Input cannot be empty, please enter a number", Toast.LENGTH_SHORT).show();
                    return;
                }
                int turnToMinutes = 60000;
                long millisecondInput = Long.parseLong(input) * turnToMinutes;

                if (millisecondInput == 0) {
                    Toast.makeText(MainActivity.this, "Please enter a number higher than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTimer(millisecondInput);
                mEditTimerInput.setText("");
            }
        });

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mTimerRunning) {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        updateCountDownText();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setTimer(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;

                mProgressBar.setMax((int) mStartTimeInMillis / 1000);
                int progress = (int)(millisUntilFinished / 1000);
                mProgressBar.setProgress(mProgressBar.getMax() - progress);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
    }

    private void resetTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mTimerRunning = false;
        }
        mTimeLeftInMillis = mStartTimeInMillis;
        mProgressBar.setProgress(0);
        updateCountDownText();
    }
    private void updateCountDownText() {

        int hours = (int)(mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int)((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int)(mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("countDown", mTimeLeftInMillis);
        outState.putLong("startTimeInMillis", mStartTimeInMillis);
        outState.putLong("endTimer", mEndTime);
        outState.putBoolean("timerRunning", mTimerRunning);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mTimeLeftInMillis = savedInstanceState.getLong("countDown");
        mTimerRunning = savedInstanceState.getBoolean("timerRunning");
        mStartTimeInMillis = savedInstanceState.getLong("startTimeInMillis");
        updateCountDownText();

        if (mTimerRunning) {
            mEndTime = savedInstanceState.getLong("endTimer");
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            startTimer();
        }
    }
}

