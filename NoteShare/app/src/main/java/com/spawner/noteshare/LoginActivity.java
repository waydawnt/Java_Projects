package com.spawner.noteshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import util.NoteShareApi;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText loginEmail, loginPassword;
    private Button loginButton, forgetButton, registerButton;
    private ImageView logoImage;
    private TextView welcomeText, signinText;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        forgetButton = findViewById(R.id.forget_button);
        registerButton = findViewById(R.id.new_user_button);
        logoImage = findViewById(R.id.logoImage_2);
        welcomeText = findViewById(R.id.welcome_text);
        signinText = findViewById(R.id.sigin_text);
        logoImage = findViewById(R.id.logoImage_2);
        progressBar = findViewById(R.id.login_progressbar);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                    startActivity(intent);
                    finish();
                }else {

                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getEditableText().toString().trim();
                String password = loginPassword.getEditableText().toString().trim();

                if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)) {
                    //send user to next activity
                    SignInAccount(email, password);
                }else {
                    //show snackbar
                    Snackbar.make(findViewById(R.id.login_layout), "Empty fields not allowed!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(logoImage, "logo");
                pairs[1] = new Pair<View, String>(welcomeText, "t_1");
                pairs[2] = new Pair<View, String>(signinText, "t_2");
                pairs[3] = new Pair<View, String>(loginEmail, "t_3");
                pairs[4] = new Pair<View, String>(loginPassword, "t_4");
                pairs[5] = new Pair<View, String>(loginButton, "t_5");
                pairs[6] = new Pair<View, String>(registerButton, "t_6");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }

    private void SignInAccount(String email, String password) {

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressBar.setVisibility(View.INVISIBLE);

                            Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}