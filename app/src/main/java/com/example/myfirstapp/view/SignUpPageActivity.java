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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPageActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEmail;
    EditText mPassword;

    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        findViewById(R.id.signUpBtn).setOnClickListener(this);

        mEmail = findViewById(R.id.signUpEmail);
        mPassword = findViewById(R.id.signUpPassword);

        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpBtn:
                signUpUser();
                break;
            case R.id.signUpTextViewLogin:
                startActivity(new Intent(SignUpPageActivity.this, LoginPageActivity.class));
                break;
        }
    }

    private void signUpUser() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if(isInputDataValid(email, password)) {
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SignUpPageActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(SignUpPageActivity.this, "Sign Up failed", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isInputDataValid(String email, String password) {
        return !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length() > 5;
    }
}
