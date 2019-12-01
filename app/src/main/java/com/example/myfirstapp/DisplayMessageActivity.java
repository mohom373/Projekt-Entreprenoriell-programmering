package com.example.myfirstapp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DisplayMessageActivity extends AppCompatActivity {

    private static final String KEY_USER_NAME = "username";
    private static final String KEY_PASSWORD = "password";

    private EditText editTextUserName;
    private EditText editTextPassword;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        editTextUserName = (EditText) findViewById(R.id.edit_text_user_name);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);

    }

    public void addUser(View view){
        String userName = editTextUserName.getText().toString();
        String password = editTextPassword.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_USER_NAME, userName);
        user.put(KEY_PASSWORD, password);

        db.collection("UsersCollection").document("Users").set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DisplayMessageActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DisplayMessageActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
