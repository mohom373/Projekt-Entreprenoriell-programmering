package com.example.myfirstapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpPageActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail;
    private EditText mPassword;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        findViewById(R.id.signUpBtn).setOnClickListener(this);
        findViewById(R.id.signUpTextViewLogin).setOnClickListener(this);

        mEmail = findViewById(R.id.signUpEmail);
        mPassword = findViewById(R.id.signUpPassword);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpBtn:
                signUpUser();
                break;
            case R.id.signUpTextViewLogin:
                setToastMessage("Going back to login screen");
                Intent intent = new Intent(this, LoginPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
                break;
        }
    }

    private void signUpUser() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "User_SignUp");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "SignUp_Button");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if(isInputDataValid(email, password)) {
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                setToastMessage("User registered successfully");
                                Intent intent = new Intent(SignUpPageActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                if (task.getException()
                                        instanceof FirebaseAuthUserCollisionException) {
                                    setToastMessage("Email already used for registration");
                                }
                            }
                        }
                    });
        } else {
            setToastMessage("Sign up failed");
        }
    }

    private boolean isInputDataValid(String email, String password) {
        return !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length() > 5;
    }


    private void setToastMessage(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }
}
