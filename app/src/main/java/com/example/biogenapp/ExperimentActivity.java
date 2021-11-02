package com.example.biogenapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.example.biogenapp.MainActivity.fields;
import static com.example.biogenapp.MainActivity.mainAdapter;
import static com.example.biogenapp.MainActivity.saveSharedPreferencesLogList;

public class ExperimentActivity extends AppCompatActivity {

    private ParameterListAdapter adapter;
    private int pos = 0;
    private double value = 0;
    private String key = "", date = "", time = "";
    private Chronometer timer;
    private ImageButton imageButton, modeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.experiment_layout);
        Bundle arguments = getIntent().getExtras();
        pos = (int) arguments.get("position");

        TextView nameField = findViewById(R.id.name_field);
        TextView dateField = findViewById(R.id.date_field);
        nameField.setText(fields.get(pos).getName());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (fields.get(pos).isStarted()) dateField.setText(dateFormat.format(fields.get(pos).getStartTime()));
        else dateField.setText(getResources().getString(R.string.not_started));

        RecyclerView recyclerView = findViewById(R.id.parameters_recycler);
        adapter = new ParameterListAdapter(this, pos);
        recyclerView.setAdapter(adapter);

        long base = SystemClock.elapsedRealtime();
        timer = findViewById(R.id.chronometer);
        imageButton = findViewById(R.id.pause_button);

        modeButton = findViewById(R.id.mode_button);
        if (fields.get(pos).isCompMode()) modeButton.setImageResource(R.drawable.mode_2);
        else modeButton.setImageResource(R.drawable.mode_1);

        if (!fields.get(pos).isStarted()) {
            imageButton.setImageResource(R.drawable.play_button);
        }
        else{
            if (fields.get(pos).isPause()){
                imageButton.setImageResource(R.drawable.play_button);
                base += fields.get(pos).getAllStopTime();
                timer.setBase(base);
            }
            else{
                imageButton.setImageResource(R.drawable.pause_button);
                base -= (System.currentTimeMillis() - fields.get(pos).getAllStopTime());
                timer.setBase(base);
                timer.start();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveSharedPreferencesLogList(this);
    }

    public void moveListener(View view){
        if (!fields.get(pos).isStarted()){
            fields.get(pos).Start(System.currentTimeMillis());
            imageButton.setImageResource(R.drawable.pause_button);
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
            TextView dateField = findViewById(R.id.date_field);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            dateField.setText(dateFormat.format(fields.get(pos).getStartTime()));
        }
        else if (fields.get(pos).isPause()){
            imageButton.setImageResource(R.drawable.pause_button);
            timer.setBase(SystemClock.elapsedRealtime() + fields.get(pos).getAllStopTime());
            fields.get(pos).addToStopTime(System.currentTimeMillis());
            timer.start();
        }
        else{
            imageButton.setImageResource(R.drawable.play_button);
            fields.get(pos).setLastPause(System.currentTimeMillis());
            timer.stop();
        }
        fields.get(pos).switchPause();
    }

    public void modeListener(View view){
        fields.get(pos).switchMode();
        adapter.notifyDataSetChanged();
        if (fields.get(pos).isCompMode()) modeButton.setImageResource(R.drawable.mode_2);
        else modeButton.setImageResource(R.drawable.mode_1);
    }

    public void backListener(View view){
        finish();
        mainAdapter.notifyDataSetChanged();
    }

    public void addParameterListener(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View window = inflater.inflate(R.layout.parameter_dialogue_layout, null);
        AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
        DialogBuilder.setView(window);
        Spinner spinner = window.findViewById(R.id.spinner);
        ArrayAdapter<?> SpinAdapter = ArrayAdapter.createFromResource(this, R.array.parameters_list, android.R.layout.simple_spinner_item);
        SpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(SpinAdapter);

        EditText editTime = window.findViewById(R.id.edit_time);
        EditText editDate = window.findViewById(R.id.edit_date);
        Date currentDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        editDate.setText(dateFormat.format(currentDate));
        editTime.setText(timeFormat.format(currentDate));

        DialogBuilder.setPositiveButton("OK",
                (dialog, id) -> {
                    key = spinner.getSelectedItem().toString();
                    EditText EditValue = window.findViewById(R.id.value_input);
                    value = Double.parseDouble(EditValue.getText().toString());
                    date = editDate.getText().toString();
                    time = editTime.getText().toString();
                    fields.get(pos).add(key, value, date, time);
                    adapter.notifyDataSetChanged();
                });
        DialogBuilder.setNegativeButton("cancel",
                (dialog, id) -> dialog.cancel());
        DialogBuilder.create().show();
    }
}
