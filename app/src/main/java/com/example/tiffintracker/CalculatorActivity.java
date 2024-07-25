package com.example.tiffintracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

public class CalculatorActivity extends AppCompatActivity {

    private EditText editTextTiffinsTaken;
    private EditText editTextPricePerTiffin;
    private TextView textViewTotalPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        editTextTiffinsTaken = findViewById(R.id.editTextTiffinsTaken);
        editTextPricePerTiffin = findViewById(R.id.editTextPricePerTiffin);
        textViewTotalPayment = findViewById(R.id.textViewTotalPayment);

        // Assuming you have a method to get the total number of tiffins
        int totalTiffins = getTotalTiffinsTaken();
        editTextTiffinsTaken.setText(String.valueOf(totalTiffins));

        editTextTiffinsTaken.addTextChangedListener(textWatcher);
        editTextPricePerTiffin.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            calculateTotalPayment();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void calculateTotalPayment() {
        String tiffinsTakenStr = editTextTiffinsTaken.getText().toString();
        String pricePerTiffinStr = editTextPricePerTiffin.getText().toString();

        if (!tiffinsTakenStr.isEmpty() && !pricePerTiffinStr.isEmpty()) {
            int tiffinsTaken = Integer.parseInt(tiffinsTakenStr);
            double pricePerTiffin = Double.parseDouble(pricePerTiffinStr);
            double totalPayment = tiffinsTaken * pricePerTiffin;
            textViewTotalPayment.setText(String.valueOf(totalPayment));
        } else {
            textViewTotalPayment.setText("");
        }
    }

    private int getTotalTiffinsTaken() {
        // Logic to calculate the total number of tiffins taken
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        int totalTiffins = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().contains(MainActivity.KEY_MORNING_TIFFIN_ARRIVED) || entry.getKey().contains(MainActivity.KEY_NIGHT_TIFFIN_ARRIVED)) {
                boolean tiffinArrived = (Boolean) entry.getValue();
                if (tiffinArrived) {
                    totalTiffins++;
                }
            }
        }
        return totalTiffins;
    }
}
