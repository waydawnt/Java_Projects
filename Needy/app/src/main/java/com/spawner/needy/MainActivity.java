package com.spawner.needy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.spawner.needy.data.DatabaseHandler;
import com.spawner.needy.model.Item;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText needyitem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private Button saveButton;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler = new DatabaseHandler(this);

        byPassActivity();

        //Check if item was saved
        List<Item> items = databaseHandler.getAllItems();
        for(Item item: items) {
            Log.d("mobile", "onCreate: "+item.getDateItemAdded());
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupDialogBuilder();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void byPassActivity() {
        if(databaseHandler.getItemCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }

    private void popupDialogBuilder() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        needyitem = view.findViewById(R.id.needyitem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemColor = view.findViewById(R.id.itemColor);
        itemSize = view.findViewById(R.id.itemSize);
        saveButton = view.findViewById(R.id.save_button);
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!needyitem.getText().toString().isEmpty()
                && !itemQuantity.getText().toString().isEmpty()
                && !itemColor.getText().toString().isEmpty()
                && !itemSize.getText().toString().isEmpty()) {
                    saveItem(view);
                }else {
                    Snackbar.make(view,"Empty fields not allowed!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void saveItem(View view) {
        //todo: save each needy item to db
        Item item = new Item();
        String newItem = needyitem.getText().toString().trim();
        int newQuantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        String newColor = itemColor.getText().toString().trim();
        int newSize = Integer.parseInt(itemSize.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemQuantity(newQuantity);
        item.setItemColor(newColor);
        item.setItemSize(newSize);

        databaseHandler.addItem(item);

        Snackbar.make(view,"Item saved", Snackbar.LENGTH_SHORT).show();

        //todo: move to next screen - details screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //code to run
                dialog.dismiss();
                //todo: move to the next screen
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        },1200); //1sec
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}