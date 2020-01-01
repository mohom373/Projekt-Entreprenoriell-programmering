package com.example.myfirstapp.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.myfirstapp.model.User;
import com.example.myfirstapp.view.MainActivity;
import com.example.myfirstapp.view.SignUpPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends BaseObservable {
    private User mUser;
    private Context mContext;

    private FirebaseAuth mFirebaseAuth;

    private String successMessage = "Login was successful";
    private String errorMessage = "Email or Password not valid";

    @Bindable
    private String toastMessage = null;

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    private boolean isInputDataValid() {
        return !TextUtils.isEmpty(getUserEmail()) &&
                Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches() &&
                getUserPassword().length() > 5;
    }

    /*
    PUBLIC METHODS START HERE
     */
    public void setUserEmail(String email) {
        mUser.setmEmail(email);
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserEmail() {
        return mUser.getmEmail();
    }

    @Bindable
    public String getUserPassword() {
        return mUser.getmPassword();
    }

    public void setUserPassword(String password) {
        mUser.setmPassword(password);
        notifyPropertyChanged(BR.userPassword);
    }

    public LoginViewModel(Context context) {
        mUser = new User("", "");
        mContext = context;
    }

    public void onLoginClicked() {
        if (isInputDataValid()) {
            //mUser.addUserToDB();
            this.userLogin();
        } else {
            setToastMessage(errorMessage);
        }
    }

    private void userLogin() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signInWithEmailAndPassword(getUserEmail(),
                getUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setToastMessage(successMessage);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext, "LOGIN FAIleD", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onTextViewSignUpClicked () {
        setToastMessage("TRYING TO CLICK THIS DAMN TING");
        Intent intent = new Intent(mContext, SignUpPageActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public String getToastMessage() {
        return toastMessage;
    }
}
