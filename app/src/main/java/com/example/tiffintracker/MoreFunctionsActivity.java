package com.example.tiffintracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Map;

public class MoreFunctionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_functions);

        // Initialize buttons
        Button buttonViewAllData = findViewById(R.id.buttonViewAllData);
        Button buttonCalculator = findViewById(R.id.buttonCalculator);

        // Set onClick listener for buttonViewAllData
        buttonViewAllData.setOnClickListener(v -> {
            String allData = getAllData();
            Intent intent = new Intent(MoreFunctionsActivity.this, ViewDataActivity.class);
            intent.putExtra("ALL_DATA", allData);
            startActivity(intent);
        });

        // Set onClick listener for buttonCalculator
        buttonCalculator.setOnClickListener(v -> {
            Intent intent = new Intent(MoreFunctionsActivity.this, CalculatorActivity.class);
            startActivity(intent);
        });
    }

    // Method to retrieve all data
    private String getAllData() {
        Map<Long, String> allDataMap = MainActivity.getAllData(this);
        if (allDataMap == null || allDataMap.isEmpty()) {
            return "No data available.";
        }
        StringBuilder allDataBuilder = new StringBuilder();
        for (String data : allDataMap.values()) {
            allDataBuilder.append(data).append("\n\n");
        }
        return allDataBuilder.toString();
    }
}
