package com.spawner.swingtradingcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText entryPoint, stopLoss, lossAmount;
    private TextView resultView;
    private double Entry, StopLoss, LossAmount;
    private MaterialButton calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entryPoint = findViewById(R.id.entry_point);
        stopLoss = findViewById(R.id.stop_loss);
        lossAmount = findViewById(R.id.loss_amount);
        resultView = findViewById(R.id.result_view);
        calculateButton = findViewById(R.id.calculate_button);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Entry = Double.parseDouble(Objects.requireNonNull(entryPoint.getText()).toString().trim());
                StopLoss = Double.parseDouble(Objects.requireNonNull(stopLoss.getText()).toString().trim());
                LossAmount = Double.parseDouble(Objects.requireNonNull(lossAmount.getText()).toString().trim());

                double difference, target, targetDifference, quantity, total_cost;
                difference = Entry - StopLoss;
                targetDifference = difference * 2;
                target = Entry + targetDifference;
                quantity = LossAmount / difference;
                total_cost = Entry * quantity;

                resultView.setText("Result :\n" +
                        "Entry Point :" +Entry+ "\n" +
                        "Target : " +target+ "\n" +
                        "StopLoss : " +StopLoss+ "\n" +
                        "StopLoss Difference : " +difference+ "\n" +
                        "Target Difference : " +targetDifference+ "\n" +
                        "Max Loss Amount : " +LossAmount+ "\n" +
                        "Shares Quantity : " +quantity+ "\n" +
                        "Total Cost : " +total_cost+ "\n" +
                        "Target Hit Profit : " +(target * quantity)+ "\n" +
                        "Stoploss Hit Loss : " +(StopLoss * quantity));
            }
        });
    }
}