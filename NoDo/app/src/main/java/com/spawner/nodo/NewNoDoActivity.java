package com.spawner.nodo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NewNoDoActivity extends AppCompatActivity {
    private static final String EXTRA_REPLY = "com.spawner.android.reply";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_no_do);

        editText = findViewById(R.id.edit_nodo);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editText.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                }else {
                    String nodoString = editText.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, nodoString);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

    }
}