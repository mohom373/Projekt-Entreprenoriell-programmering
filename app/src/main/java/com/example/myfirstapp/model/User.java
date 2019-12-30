package com.example.myfirstapp.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String mEmail;
    private String mUserName;
    private String mPassword;
    private int mPoints;

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    private FirebaseAuth mFirebaseAuth;

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

    public int getmPoints() {
        return mPoints;
    }

    public void setmPoints(int mPoints) {
        this.mPoints = mPoints;
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

    /*public void userLogin() {
        mFirebaseAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            Context context = ContextHelper.getContext();

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    
                } else {
                    Toast.makeText(context, "LOGIN FAIleD", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/
}
