package com.example.tiffintracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "TiffinPrefs";
    public static final String KEY_MORNING_TIFFIN_ARRIVED = "morningTiffinArrived";
    public static final String KEY_MORNING_NOTES = "morningNotes";
    public static final String KEY_MORNING_DATE = "morningDate";
    public static final String KEY_NIGHT_TIFFIN_ARRIVED = "nightTiffinArrived";
    public static final String KEY_NIGHT_NOTES = "nightNotes";
    public static final String KEY_NIGHT_DATE = "nightDate";

    private boolean morningTiffinArrived;
    private boolean nightTiffinArrived;
    private EditText editTextMorningNotes;
    private EditText editTextNightNotes;
    private TextView textViewMorningData;
    private TextView textViewNightData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView premiumLogo = findViewById(R.id.premiumLogo);
        Button buttonMorningArrived = findViewById(R.id.buttonMorningArrived);
        Button buttonMorningNotArrived = findViewById(R.id.buttonMorningNotArrived);
        Button buttonNightArrived = findViewById(R.id.buttonNightArrived);
        Button buttonNightNotArrived = findViewById(R.id.buttonNightNotArrived);
        Button buttonSaveData = findViewById(R.id.buttonSaveData);
        Button buttonMoreFunctions = findViewById(R.id.buttonMoreFunctions);

        editTextMorningNotes = findViewById(R.id.editTextMorningNotes);
        editTextNightNotes = findViewById(R.id.editTextNightNotes);
        textViewMorningData = findViewById(R.id.textViewMorningData);
        textViewNightData = findViewById(R.id.textViewNightData);

        loadData();

        buttonMorningArrived.setOnClickListener(v -> {
            morningTiffinArrived = true;
            buttonMorningArrived.setBackgroundColor(getResources().getColor(R.color.button_morning_arrived_pressed));
            buttonMorningNotArrived.setBackgroundColor(getResources().getColor(R.color.button_inactive));
        });

        buttonMorningNotArrived.setOnClickListener(v -> {
            morningTiffinArrived = false;
            buttonMorningNotArrived.setBackgroundColor(getResources().getColor(R.color.button_morning_not_arrived_pressed));
            buttonMorningArrived.setBackgroundColor(getResources().getColor(R.color.button_inactive));
        });

        buttonNightArrived.setOnClickListener(v -> {
            nightTiffinArrived = true;
            buttonNightArrived.setBackgroundColor(getResources().getColor(R.color.button_night_arrived_pressed));
            buttonNightNotArrived.setBackgroundColor(getResources().getColor(R.color.button_inactive));
        });

        buttonNightNotArrived.setOnClickListener(v -> {
            nightTiffinArrived = false;
            buttonNightNotArrived.setBackgroundColor(getResources().getColor(R.color.button_night_not_arrived_pressed));
            buttonNightArrived.setBackgroundColor(getResources().getColor(R.color.button_inactive));
        });

        buttonSaveData.setOnClickListener(v -> {
            if (!isDataForCurrentDateSaved()) {
                saveMorningData();
                saveNightData();
                editTextMorningNotes.setText("");
                editTextNightNotes.setText("");
            } else {
                showToast("Data for today is already saved");
            }
        });

        buttonMoreFunctions.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MoreFunctionsActivity.class);
            startActivity(intent);
        });
    }

    private boolean isDataForCurrentDateSaved() {
        String currentDate = getCurrentDate();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().contains(KEY_MORNING_DATE) || entry.getKey().contains(KEY_NIGHT_DATE)) {
                if (currentDate.equals(entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void saveMorningData() {
        String currentDate = getCurrentDate();
        long timestamp = System.currentTimeMillis();
        saveData(KEY_MORNING_TIFFIN_ARRIVED + "_" + timestamp, morningTiffinArrived, KEY_MORNING_NOTES + "_" + timestamp, editTextMorningNotes.getText().toString(), KEY_MORNING_DATE + "_" + timestamp, currentDate);
        showToast("Morning data saved");
    }

    private void saveNightData() {
        String currentDate = getCurrentDate();
        long timestamp = System.currentTimeMillis();
        saveData(KEY_NIGHT_TIFFIN_ARRIVED + "_" + timestamp, nightTiffinArrived, KEY_NIGHT_NOTES + "_" + timestamp, editTextNightNotes.getText().toString(), KEY_NIGHT_DATE + "_" + timestamp, currentDate);
        showToast("Night data saved");
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        TreeMap<Long, String> sortedMorningData = new TreeMap<>();
        TreeMap<Long, String> sortedNightData = new TreeMap<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().contains(KEY_MORNING_TIFFIN_ARRIVED) || entry.getKey().contains(KEY_NIGHT_TIFFIN_ARRIVED)) {
                String[] keyParts = entry.getKey().split("_");
                if (keyParts.length >= 2) {
                    long timestamp = Long.parseLong(keyParts[1]);
                    boolean arrived = (Boolean) entry.getValue();
                    String arrivedText = arrived ? "Arrived" : "Not Arrived";

                    String notesKey = entry.getKey().contains(KEY_MORNING_TIFFIN_ARRIVED) ? KEY_MORNING_NOTES + "_" + timestamp : KEY_NIGHT_NOTES + "_" + timestamp;
                    String dateKey = entry.getKey().contains(KEY_MORNING_TIFFIN_ARRIVED) ? KEY_MORNING_DATE + "_" + timestamp : KEY_NIGHT_DATE + "_" + timestamp;

                    String notes = prefs.getString(notesKey, "");
                    String date = prefs.getString(dateKey, "");

                    String dataText = (entry.getKey().contains(KEY_MORNING_TIFFIN_ARRIVED) ? "Morning" : "Night") + " Data:\nDate: " + date + "\nTiffin Status: " + arrivedText + "\nNotes: " + notes;

                    if (entry.getKey().contains(KEY_MORNING_TIFFIN_ARRIVED)) {
                        sortedMorningData.put(timestamp, dataText);
                    } else {
                        sortedNightData.put(timestamp, dataText);
                    }
                }
            }
        }

        textViewMorningData.setText("");
        for (Map.Entry<Long, String> entry : sortedMorningData.entrySet()) {
            textViewMorningData.append(entry.getValue() + "\n\n");
        }

        textViewNightData.setText("");
        for (Map.Entry<Long, String> entry : sortedNightData.entrySet()) {
            textViewNightData.append(entry.getValue() + "\n\n");
        }
    }

    private void saveData(String keyArrived, boolean tiffinArrived, String keyNotes, String notes, String keyDate, String date) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(keyArrived, tiffinArrived);
        editor.putString(keyNotes, notes);
        editor.putString(keyDate, date);
        editor.apply();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Method to retrieve all data for MoreFunctionsActivity
    public static Map<Long, String> getAllData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        TreeMap<Long, String> allData = new TreeMap<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().contains(KEY_MORNING_TIFFIN_ARRIVED) || entry.getKey().contains(KEY_NIGHT_TIFFIN_ARRIVED)) {
                String[] keyParts = entry.getKey().split("_");
                if (keyParts.length >= 2) {
                    long timestamp = Long.parseLong(keyParts[1]);
                    boolean arrived = (Boolean) entry.getValue();
                    String arrivedText = arrived ? "Arrived" : "Not Arrived";

                    String notesKey = entry.getKey().contains(KEY_MORNING_TIFFIN_ARRIVED) ? KEY_MORNING_NOTES + "_" + timestamp : KEY_NIGHT_NOTES + "_" + timestamp;
                    String dateKey = entry.getKey().contains(KEY_MORNING_TIFFIN_ARRIVED) ? KEY_MORNING_DATE + "_" + timestamp : KEY_NIGHT_DATE + "_" + timestamp;

                    String notes = prefs.getString(notesKey, "");
                    String date = prefs.getString(dateKey, "");

                    String dataText = (entry.getKey().contains(KEY_MORNING_TIFFIN_ARRIVED) ? "Morning" : "Night") + " Data:\nDate: " + date + "\nTiffin Status: " + arrivedText + "\nNotes: " + notes;

                    allData.put(timestamp, dataText);
                }
            }
        }
        return allData;
    }
}
