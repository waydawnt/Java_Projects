package com.spawner.noteshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import util.NoteShareApi;

public class RegisterActivity extends AppCompatActivity {
    private ImageView logoImage;
    private TextView welcomeText, signupText, loginTextView;
    private TextInputEditText registerFullname, registerUsername, registerEmail, registerPassword;
    private Button registerButton;
    private ProgressBar progressBar;

    //Firebase Firestore
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //get firestore instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        logoImage = findViewById(R.id.logoImage_3);
        welcomeText = findViewById(R.id.welcome_text_1);
        signupText = findViewById(R.id.sigin_text_1);
        registerFullname = findViewById(R.id.register_full_name);
        registerUsername = findViewById(R.id.register_username);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register_button);
        loginTextView = findViewById(R.id.already_account_textview);
        progressBar = findViewById(R.id.register_progress_bar);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null) {
                    //user is already logged in...
                }else {
                    //no user yet
                }
            }
        };

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user and store data in firestore database
                String fullname = registerFullname.getEditableText().toString().trim();
                String username = registerUsername.getEditableText().toString().trim();
                String email = registerEmail.getEditableText().toString().trim();
                String password = registerPassword.getEditableText().toString().trim();

                if (!TextUtils.isEmpty(fullname)
                && !TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)) {

                    //Create a user account with email and password
                    CreateNewUser(fullname, username, email, password);

                }else {
                    Snackbar.make(findViewById(R.id.register_activity), "Empty fields not allowed!", Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(RegisterActivity.this, "Empty fields not allowed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CreateNewUser(String fullname, String username, String email, String password) {

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //take user to next activity
                            currentUser = firebaseAuth.getCurrentUser();
                            String currentUserId = currentUser.getUid();

                            //create user Map to add in user collection
                            Map<String, String> userObj = new HashMap<>();
                            userObj.put("userId", currentUserId);
                            userObj.put("username", username);
                            userObj.put("email", email);

                            //save to firestore database
                            DocumentReference documentReference = db.collection("Users").document(fullname);

                            documentReference.set(userObj)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            documentReference.get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.getResult().exists()) {
                                                                progressBar.setVisibility(View.INVISIBLE);

                                                                NoteShareApi noteShareApi = NoteShareApi.getInstance();
                                                                noteShareApi.setFullname(fullname);
                                                                noteShareApi.setUsername(task.getResult().getString("email"));
                                                                noteShareApi.setUserId(currentUserId);

                                                                Intent intent = new Intent(RegisterActivity.this, AccountActivity.class);
                                                                startActivity(intent);
                                                                finish();

                                                            }else {

                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }else {
                            //something went wrong
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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