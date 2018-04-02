package com.shaheryarbhatti.polaroidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.preferences.LocalStoragePreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "LoginActivity";
    /*private final int FB_REQUEST_CODE = 64206;
    private final int TWITTER_REQUEST_CODE = 140;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;*/
    private AppCompatEditText emailEdt, passwordEdt;
    private Button loginButton, fbSigninButton, twitterSigninButton, logoutButton;
    private TextView signupBtn;
    //    private CallbackManager callbackManager;
    //   FB REQUEST_CODE: 64206
//    private TwitterAuthClient mTwitterAuthClient = new TwitterAuthClient();
    private boolean isLoggedIn;
    private LocalStoragePreferences preferences;


    @Override
    protected void onStart() {
        super.onStart();

/*        currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
        Log.d(TAG, "onStart: login: " + (currentUser != null));
        }*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        mAuth = FirebaseAuth.getInstance();


        updateUI(currentUser);
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        isLoggedIn = session != null;

        callbackManager = CallbackManager.Factory.create();
        isLoggedIn = AccessToken.getCurrentAccessToken() != null;
        if (isLoggedIn) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
        }*/
       /* LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        handleFacebookAccessToken(loginResult.getAccessToken());


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });*/
        setContentView(R.layout.activity_login);
        preferences = new LocalStoragePreferences(this);
        if (preferences.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        fbSigninButton = findViewById(R.id.fb_sign_in_button);
        twitterSigninButton = findViewById(R.id.twitter_sign_in_button);
        loginButton = findViewById(R.id.email_sign_in_button);
        signupBtn = findViewById(R.id.signupBtn);
//        logoutButton = (Button) findViewById(R.id.logout_button);
        loginButton.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
//        fbSigninButton.setOnClickListener(this);
//        twitterSigninButton.setOnClickListener(this);
//        logoutButton.setOnClickListener(this);

    }

   /* private void fbSignIn() {
        if (!isLoggedIn)
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }*/

    /*private void twitterSignIn() {
        mTwitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Log.d(TAG, "success: ");
                handleTwitterSession(twitterSessionResult.data);
                // Success
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }*/

    /*private void logoutTwitter() {
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }

    private void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }*/


//  TODO onClick

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            if (validateUi()) {
                String pushToken = FirebaseInstanceId.getInstance().getToken();
                handleEmailAndPasswordLogin(emailEdt.getText().toString(), passwordEdt.getText().toString(), pushToken);
            }
            /*handleEmailAndPasswordLogin(emailEdt.getText().toString(), passwordEdt.getText().toString());*/
        }
        if (v == signupBtn) {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        }
        /*if (v == fbSigninButton) {
            fbSignIn();

        }
        if (v == twitterSigninButton) {
            twitterSignIn();
        }*/
      /*  if (v == logoutButton) {
//            mAuth.signOut();
//            logoutTwitter();
//            logoutFacebook();
        }*/

    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateUi() {
        if (emailEdt.getText().toString().isEmpty()) {
            emailEdt.setError("Email Required");
            return false;
        }
        if (passwordEdt.getText().toString().isEmpty()) {
            emailEdt.setError("Password Required");
            return false;
        }
        if (!isEmailValid(emailEdt.getText().toString())) {
            emailEdt.setError("Inavlid Email");
            return false;
        }
        return true;
    }

    private void handleEmailAndPasswordLogin(String email, String password, final String pushToken) {
        String loginUrl = getResources().getString(R.string.login_test_url);
        Log.d(TAG, "handleEmailAndPasswordLogin: for debugging");
        AndroidNetworking.post(loginUrl)
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .addBodyParameter("push_token", pushToken)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "onResponse: " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSuccess = jsonObject.getBoolean("success");

                            if (isSuccess) {
                                JSONObject dataObj = jsonObject.getJSONObject("data");
                                JSONObject userObj = dataObj.getJSONObject("user");
                                preferences.setIsLoggedIn(true);
                                preferences.setUserId(userObj.getString("id"));
                                preferences.setToken(dataObj.getString("token"));
                                preferences.setName(userObj.getString("name"));
                                preferences.setEmail(userObj.getString("email"));
                                preferences.setDOB(userObj.getString("dob"));
                                preferences.setPushToken(pushToken);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                    }
                });

    }

    /*mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });*/


//    TODO handleFacebookAccessToken

//    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//        /*AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            currentUser = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });*/
//    }

//    TODO handleTwitterSession

  /*  private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }*/

//    TODO onActivityResult

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FB_REQUEST_CODE:
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                    break;
                case TWITTER_REQUEST_CODE:
                    Log.d(TAG, "onActivityResult: twitter login");
                    mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
//        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: result code: " + resultCode + " request code: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}
