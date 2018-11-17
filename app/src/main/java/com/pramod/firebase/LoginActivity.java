package com.pramod.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pramod.firebase.services.ClipboardMonitorService;

/**
 * @author Pramod Nanduri
 * <p>
 * Resources:
 * Firebase Login setup:
 * https://firebase.google.com/docs/android/setup
 * <p>
 * Facebook login:
 * https://firebase.google.com/docs/auth/android/facebook-login
 * <p>
 * Realtime DB Vs Cloudstore.
 * https://firebase.google.com/docs/database/rtdb-vs-firestore?authuser=0
 */
public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button loginBtn;

    //Firebase Auth handler
    FirebaseAuth firebaseAuth;

    //For FB
    CallbackManager callbackManager = CallbackManager.Factory.create();
    private static final String TAG = "PmdLogTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Ui Components
        setupElements();

        //Start Services
        startServices();

        //Firebase init settings.
        setupFireBase();

        //Ignore this method call if FB login not needed!
        facebookLogin();

    }

    /**
     * Setting up all UI elements for this activity.
     */

    void setupElements() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmailPassword(email.getText().toString(), password.getText().toString());
            }
        });

    }

    void startServices() {
        startService(new Intent(this, ClipboardMonitorService.class));
    }

    void setupFireBase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * The handler for Facebook Login.
     * Before this all the steps in the below link needs to be followed .
     */

    void facebookLogin() {

        LoginButton loginButton = findViewById(R.id.facebookLoginButton);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
                error.printStackTrace();
            }
        });
    }


    /**
     * Once we get valid Auth token from Facebook login,
     * we authenticate firebase login with the fb credentials of the current user.
     */
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        handleLoginResult(task);
                    }
                });
    }

    /**
     * Needed for FB Login.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Common method to handle signin result.
    void handleLoginResult(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithEmail:success");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            navigateHomePage();
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.getException());
            Toast.makeText(getApplicationContext(), "Invalid Credentials!", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Basic email password authentication
     */

    void loginEmailPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                handleLoginResult(task);
            }
        });
    }

    /**
     * Method to navigate to Home Page after successful login.
     */
    void navigateHomePage() {
        Intent intent = new Intent(getApplicationContext(), GlobalHomeActivity.class);
        startActivity(intent);
    }

}
