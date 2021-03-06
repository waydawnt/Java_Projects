package com.spawner.contactmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private TextView detsName;
    private TextView detsPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        detsName = findViewById(R.id.dets_name);
        detsPhone = findViewById(R.id.dets_phone);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            String name = bundle.getString("name");
            String number = bundle.getString("number");

            detsName.setText(name);
            detsPhone.setText(number);
        }
    }
}