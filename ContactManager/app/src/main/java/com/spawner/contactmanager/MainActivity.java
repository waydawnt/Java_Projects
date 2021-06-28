package com.spawner.contactmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spawner.contactmanager.adapter.RecyclerViewAdapter;
import com.spawner.contactmanager.data.DatabaseHandler;
import com.spawner.contactmanager.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Contact> contactArrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactArrayList = new ArrayList<>();
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);

        List<Contact> contactList = db.getAllContacts();

        for(Contact contact: contactList) {
            Log.d("allContacts", "onCreate: " +contact.getName());
            contactArrayList.add(contact);
        }

        //setup an adapter
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, contactArrayList);

        recyclerView.setAdapter(recyclerViewAdapter);

//        db.addContact(new Contact("Vedant","8080734155"));
//        db.addContact(new Contact("Rhinocarl","7219250935"));
//        db.addContact(new Contact("Barbie","6546489654"));
//        db.addContact(new Contact("Sofia","824686844"));
//        db.addContact(new Contact("Princess","65426544654"));
//        db.addContact(new Contact("Sanket","516465465841"));
//        db.addContact(new Contact("Chaitanya","5464416816"));
//        db.addContact(new Contact("Bhushan","5165646654"));
//        db.addContact(new Contact("Ruben","3646484685"));
//        db.addContact(new Contact("Ritik","78979465654"));
//
//        db.addContact(new Contact("Ashna","65464646151"));
//        db.addContact(new Contact("Mansi","56464686564"));
//        db.addContact(new Contact("Akash","65145648486"));
//        db.addContact(new Contact("Jayesh","54541131165"));
//        db.addContact(new Contact("Harshvardhan","6165489818"));
//        db.addContact(new Contact("Somesh","1654984988"));
//        db.addContact(new Contact("Siddhant","6198484984"));
//        db.addContact(new Contact("John","65198419988"));
//        db.addContact(new Contact("Omkar","6516951916516"));
//        db.addContact(new Contact("Kaustubh","6516511916"));

    }
}