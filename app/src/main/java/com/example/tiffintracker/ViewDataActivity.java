package com.example.tiffintracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ViewDataActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private TextView textViewData;
    private Button buttonExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        editTextSearch = findViewById(R.id.editTextSearch);
        textViewData = findViewById(R.id.textViewData);
        buttonExport = findViewById(R.id.buttonExport);

        Intent intent = getIntent();
        String allData = intent.getStringExtra("ALL_DATA");
        textViewData.setText(allData);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonExport.setOnClickListener(v -> exportData(allData));
    }

    private void filterData(String query) {
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        StringBuilder filteredData = new StringBuilder();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().contains(MainActivity.KEY_MORNING_DATE) || entry.getKey().contains(MainActivity.KEY_NIGHT_DATE)) {
                String date = (String) entry.getValue();
                if (date.contains(query)) {
                    boolean morningArrived = prefs.getBoolean(MainActivity.KEY_MORNING_TIFFIN_ARRIVED, false);
                    String morningNotes = prefs.getString(MainActivity.KEY_MORNING_NOTES, "");
                    boolean nightArrived = prefs.getBoolean(MainActivity.KEY_NIGHT_TIFFIN_ARRIVED, false);
                    String nightNotes = prefs.getString(MainActivity.KEY_NIGHT_NOTES, "");

                    String arrivedTextMorning = morningArrived ? "Arrived" : "Not Arrived";
                    String arrivedTextNight = nightArrived ? "Arrived" : "Not Arrived";

                    filteredData.append("Date: ").append(date)
                            .append("\nMorning Tiffin Status: ").append(arrivedTextMorning)
                            .append("\nMorning Notes: ").append(morningNotes)
                            .append("\nNight Tiffin Status: ").append(arrivedTextNight)
                            .append("\nNight Notes: ").append(nightNotes)
                            .append("\n\n");
                }
            }
        }

        textViewData.setText(filteredData.toString());
    }

    private void exportData(String data) {
        // Get the external storage directory
        File externalStorageDir = Environment.getExternalStorageDirectory();

        // Create a directory named "TiffinData" in the external storage
        File tiffinDataDir = new File(externalStorageDir, "TiffinData");
        if (!tiffinDataDir.exists()) {
            tiffinDataDir.mkdirs();
        }

        // Get the current date to use as the file name
        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        File file = new File(tiffinDataDir, "TiffinData_" + currentDate + ".txt");

        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(data.getBytes());
            Toast.makeText(this, "Data exported successfully to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error exporting data", Toast.LENGTH_SHORT).show();
        }
    }
}
