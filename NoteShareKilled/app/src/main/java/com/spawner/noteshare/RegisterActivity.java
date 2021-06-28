package com.spawner.noteshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private ImageView logoImage3;
    private Button registerButton;
    private TextView logoText2, alreadyAccount;
    private TextInputLayout registerFirstname, registerLastname, registerUsername, registerEmail, registerPassword;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firebase connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerFirstname = findViewById(R.id.register_firstname);
        registerLastname = findViewById(R.id.register_lastname);
        registerUsername = findViewById(R.id.register_username);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        logoImage3 = findViewById(R.id.logoimage3);
        logoText2 = findViewById(R.id.logo_textview2);
        alreadyAccount = findViewById(R.id.already_account);
        registerButton = findViewById(R.id.register_button);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    //user is already logged in
                }else {
                    //no user yet
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(registerFirstname.getEditText().toString())
                && !TextUtils.isEmpty(registerLastname.getEditText().toString())
                && !TextUtils.isEmpty(registerUsername.getEditText().toString())
                && !TextUtils.isEmpty(registerEmail.getEditText().toString())
                && !TextUtils.isEmpty(registerPassword.getEditText().toString())) {

                    String firstname = registerFirstname.getEditText().toString().trim();
                    String lastname = registerLastname.getEditText().toString().trim();
                    String username = registerUsername.getEditText().toString().trim();
                    String email = registerEmail.getEditText().toString().trim();
                    String password = registerPassword.getEditText().toString().trim();

                    createUserEmailAccount(firstname, lastname, username, email, password);
                }else {
                    //create a toast
                    Toast.makeText(RegisterActivity.this, "Empty fields not allowed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send user to login Activity
                loginActivity();
            }
        });
    }

    private void createUserEmailAccount(String firstname,
                                        String lastname,
                                        String username,
                                        String email,
                                        String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //we take user to AccountActivity
                            currentUser = firebaseAuth.getCurrentUser();
                            String currentUserId = currentUser.getUid();

                            //Create a user map
                            Map<String, String> userObj = new HashMap<>();
                            userObj.put("userId", currentUserId);
                            userObj.put("fullname", firstname+" "+lastname);
                            userObj.put("username", username);

//                            DocumentReference documentReference = db.collection("Users").document(firstname+" "+lastname);
//
//                            documentReference.set(userObj)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//
//                                }
//                            });

                            collectionReference.add(userObj)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            documentReference.get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.getResult().exists()) {
                                                                Intent intent = new Intent(RegisterActivity.this, AccountActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }else {

                                                            }
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        } else {
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

    private void loginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(logoImage3, "logo");
        pairs[1] = new Pair<View, String>(logoText2, "logo_text");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this, pairs);
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}