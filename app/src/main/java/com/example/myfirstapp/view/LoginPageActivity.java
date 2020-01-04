package com.example.myfirstapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.FacebookSdk;

public class LoginPageActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";

    private TextView mSignUpTextView;

    private EditText mLoginEmail;
    private EditText mLoginPassword;

    private Button  mLoginBtn;
    private LoginButton mFacebookSignInBtn;

    private FirebaseAuth mFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mLoginEmail = (EditText)findViewById(R.id.loginEmail);
        mLoginPassword = (EditText)findViewById(R.id.loginPassword);
        mLoginBtn = (Button)findViewById(R.id.loginBtn);
        mSignUpTextView = (TextView)findViewById(R.id.loginTextViewSignUp);

        // Set listener for button
        mLoginBtn.setOnClickListener(this);

        // Set listener for textview
        mSignUpTextView.setOnClickListener(this);

        // Configure Facebook sign in
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookSignInBtn = (LoginButton) findViewById(R.id.facebookSignInBtn);
        mFacebookSignInBtn.setReadPermissions("email", "public_profile");
        mFacebookSignInBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCANCEL: Canceling request" );

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onERROR: " + error);
            }
        });

        // Configure Google Sign in
        findViewById(R.id.googleSignInBtn).setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // Initialize Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    @Override
    public void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        if (currentUser != null || account != null || accessToken != null) {
            startActivity(new Intent(this, MainActivity.class));
        }

        super.onStart();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.googleSignInBtn:
                googleSignIn();
                break;
            case R.id.loginBtn:
                userLogin();
                break;
            case R.id.loginTextViewSignUp:
                onTextViewSignUpClicked();
                break;
            default:
                break;
        }
    }

    private void userLogin() {
        mFirebaseAuth = FirebaseAuth.getInstance();

        String email = mLoginEmail.getText().toString().trim();
        String password = mLoginPassword.getText().toString().trim();
        if(isInputDataValid(email, password)) {
            mFirebaseAuth.signInWithEmailAndPassword(email,
                    password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        setToastMessage("User login successful");
                        Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        setToastMessage("User login failed");
                    }
                }
            });
        } else {
            if (TextUtils.isEmpty(email)) {
                setToastMessage("Please fill in your email");
            } else if (TextUtils.isEmpty(password))
            {
                setToastMessage("Please fill in you password");
            } else {
                setToastMessage("User email or password incorrect");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            setToastMessage("User login successful");
                            Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(LoginPageActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            setToastMessage("User login failed");
                        }
                    }
                });
    }

    private void firebaseAuthWithFacebook(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);
        final AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            setToastMessage("User login Successful");
                            Intent intent = new Intent(LoginPageActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            setToastMessage("Authentication failed");
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
    }

    private boolean isInputDataValid(String email, String password) {
        return !TextUtils.isEmpty(email) &&
                (password.length()) > 5 && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void onTextViewSignUpClicked () {
        setToastMessage("Going to sign up page");
        Intent intent = new Intent(LoginPageActivity.this, SignUpPageActivity.class);
        startActivity(intent);
    }

    private void setToastMessage(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }
}
