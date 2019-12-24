package com.example.myfirstapp.model;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String mEmail;
    private String mUserName;
    private String mPassword;

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    public User(String email, String username, String password) {
        mEmail = email;
        mUserName = username;
        mPassword = password;
    }

    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void addUserToDB() {

        Map<String, Object> user = new HashMap<>();
        user.put("email", mEmail);
        user.put("password", mPassword);

        mDb.collection("UsersCollection").document("Users").set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /*Toast.makeText(LoginPageActivity.this,
                                "Added Successfully", Toast.LENGTH_SHORT).show();*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        /*Toast.makeText(LoginPageActivity.this, "Failure!",
                                Toast.LENGTH_SHORT).show();*/

                    }
                });
    }
}
