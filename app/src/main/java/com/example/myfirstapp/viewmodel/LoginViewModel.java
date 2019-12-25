package com.example.myfirstapp.viewmodel;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.myfirstapp.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends BaseObservable {
    private User mUser;

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

    public LoginViewModel() {
        mUser = new User("","");
    }

    public void onLoginClicked(){
        if (isInputDataValid()) {
            //mUser.addUserToDB();
            setToastMessage(successMessage);
        } else {
            setToastMessage(errorMessage);
        }
    }

    public String getToastMessage() {
        return toastMessage;
    }

}
