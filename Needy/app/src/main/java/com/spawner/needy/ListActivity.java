package com.spawner.needy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.spawner.needy.data.DatabaseHandler;
import com.spawner.needy.model.Item;
import com.spawner.needy.ui.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private static final String TAG = "ListActivity";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText needyitem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private Button saveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerview);

        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        //get items from db
        itemList = databaseHandler.getAllItems();

        for (Item item: itemList) {
            Log.d(TAG, "onCreate: "+item.getItemColor());
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopDialog();
            }
        });

    }

    private void createPopDialog() {

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
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        },1200); //1sec
    }
}