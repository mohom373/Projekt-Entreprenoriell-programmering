package com.example.myfirstapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.UserScoreAdapter;
import com.example.myfirstapp.model.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, Serializable {

    private DrawerLayout mDrawer;
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

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;

    private ListView mListView;
    private UserScoreAdapter mUserScoreAdapter;
    private ArrayList<User> mUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //**************************Creation of USERLISTVIEW***********************************//
        mListView = (ListView)findViewById(R.id.mainUserList);
        mUsersList = new ArrayList<>();
        mUsersList.add(new User("PlaceHolder@gmail.com", 3200));
        mUsersList.add(new User("PlaceHolder2@gmail.com", 2300));
        mUsersList.add(new User("PlaceHolder3@gmail.com", 1500));
        mUserScoreAdapter = new UserScoreAdapter(this, mUsersList);
        mListView.setAdapter(mUserScoreAdapter);

        //**************************************************************************************//

        // Creation of navigation Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        View navHeaderView = navigationView.getHeaderView(0);
        TextView userEmail = navHeaderView.findViewById(R.id.userEmail);

        // Get FirebaseAuth instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //************************ Get User info begin ************************************//
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (acct != null) {
            userEmail.setText(acct.getEmail());
        } else if (user != null) {
            userEmail.setText(user.getEmail());
        } else if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        userEmail.setText(object.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        //******************************* Get User info end ********************************//

        // Creation of Timer views
        mTextViewCountDown = (TextView)findViewById(R.id.textViewCountdown);
        mEditTimerInput = (EditText)findViewById(R.id.editTextInput);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        // Find all buttons from layout
        mButtonEdit = (Button)findViewById(R.id.mainEditBtn);
        mButtonStart = (Button)findViewById(R.id.mainStartBtn);
        mButtonReset = (Button)findViewById(R.id.mainResetBtn);

        // Setup Button listeners
        mButtonEdit.setOnClickListener(this);
        mButtonStart.setOnClickListener(this);
        mButtonReset.setOnClickListener(this);

        // Check if user not logged in and hide elements
        if (acct == null && accessToken == null && user == null) {
            mListView.setVisibility(View.GONE);
        }

        // Update
        updateCountDownText();
    }
    //**************************** ON CREATE ENDS *************************************//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sign_out:
                onMainSignOutNavViewClicked();
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mainEditBtn:
                onMainEditBtnClicked();
                break;
            case R.id.mainStartBtn:
                onMainStartBtnClicked();
                break;
            case R.id.mainResetBtn:
                onMainResetBtnClicked();
                break;
            default:
                break;
        }
    }

    //************************** Clickable functions start *********************************//
    private void onMainResetBtnClicked() {
        resetTimer();
    }

    private void onMainStartBtnClicked() {
        if(!mTimerRunning) {
            startTimer();
        }
    }

    public void onMainEditBtnClicked() {
        String input = mEditTimerInput.getText().toString();
        if (input.length() == 0) {
            Toast.makeText(MainActivity.this,
                    "Input cannot be empty, please enter a number",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        int turnToMinutes = 60000;
        long millisecondInput = Long.parseLong(input) * turnToMinutes;

        if (millisecondInput == 0) {
            Toast.makeText(MainActivity.this,
                    "Please enter a number higher than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        setTimer(millisecondInput);
        mEditTimerInput.setText("");
    }

    public void onMainSignOutNavViewClicked(){
        mFirebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, (task) -> {
                    Toast.makeText(this, "Sign Out Successful",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginPageActivity.class);
                    startActivity(intent);
                    finish();
                });
    }

    //************************** Clickable item functions end *********************************//
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

        String timeLeftFormatted = String.format(Locale.getDefault(),
                "%02d:%02d:%02d", hours, minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //********************************** Overrides of state **********************************//
    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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

