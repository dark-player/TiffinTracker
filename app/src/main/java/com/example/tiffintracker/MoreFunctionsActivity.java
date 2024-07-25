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

        Button buttonViewAllData = findViewById(R.id.buttonViewAllData);
        Button buttonCalculator = findViewById(R.id.buttonCalculator);
        Button buttonReminder = findViewById(R.id.buttonReminder);

        buttonViewAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve all data and pass it to ViewDataActivity
                String allData = getAllData();
                Intent intent = new Intent(MoreFunctionsActivity.this, ViewDataActivity.class);
                intent.putExtra("ALL_DATA", allData);
                startActivity(intent);
            }
        });

        buttonCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreFunctionsActivity.this, CalculatorActivity.class);
                startActivity(intent);
            }
        });

        buttonReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreFunctionsActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getAllData() {
        Map<Long, String> allDataMap = MainActivity.getAllData(this);
        StringBuilder allDataBuilder = new StringBuilder();
        for (String data : allDataMap.values()) {
            allDataBuilder.append(data).append("\n\n");
        }
        return allDataBuilder.toString();
    }
}
