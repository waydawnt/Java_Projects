package com.spawner.firestoreintro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText enterTitle;
    private EditText enterThoughts;
    private Button enterButton;
    private Button showButton;
    private Button updateButton;
    private Button deleteButton;
    private TextView showTitle;
    private TextView showThoughts;

    //Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHT = "thought";
    //Connect to Firebase Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference journalRef = db.collection("Journal")
            .document("First Thoughts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterTitle = findViewById(R.id.enterTitle);
        enterThoughts = findViewById(R.id.enterThoughts);
        enterButton = findViewById(R.id.enterButton);
        showButton = findViewById(R.id.showButton);
        showTitle = findViewById(R.id.showTitle);
        showThoughts = findViewById(R.id.showThoughts);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                journalRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()) {
                                    String title = documentSnapshot.getString(KEY_TITLE);
                                    String thoughts = documentSnapshot.getString(KEY_THOUGHT);

                                    showTitle.setText(title);
                                    showThoughts.setText(thoughts);
                                } else {
                                    Toast.makeText(MainActivity.this, "No data exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = enterTitle.getText().toString().trim();
                String thought = enterThoughts.getText().toString().trim();

                Map<String, Object> data = new HashMap<>();
                data.put(KEY_TITLE, title);
                data.put(KEY_THOUGHT, thought);

                journalRef.set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Successfully Added",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        journalRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String thoughts = documentSnapshot.getString(KEY_THOUGHT);

                    showTitle.setText(title);
                    showThoughts.setText(thoughts);
                } else {
                    showTitle.setText("");
                    showThoughts.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateButton:
                //update data
                UpdateMyData();
                break;
            case R.id.deleteButton:
                //delete data
                DeleteData();
                break;
        }
    }

    private void DeleteData() {
        journalRef.delete();
//        journalRef.update(KEY_TITLE, FieldValue.delete());
//        journalRef.update(KEY_THOUGHT, FieldValue.delete());
    }

    private void UpdateMyData() {
        String title = enterTitle.getText().toString().trim();
        String thought = enterThoughts.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_TITLE, title);
        data.put(KEY_THOUGHT, thought);

        journalRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}