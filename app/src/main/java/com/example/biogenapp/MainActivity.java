package com.example.biogenapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<ExperimentItem> fields = new ArrayList<ExperimentItem>();
    private RecyclerView recyclerView;
    public static ExperimentListAdapter mainAdapter;

    public static final String APP_PREFERENCES = "mysettings";


    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fields = loadSharedPreferencesLogList(this);
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        mainAdapter = new ExperimentListAdapter(this);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveSharedPreferencesLogList(this);
    }

    public static ArrayList<ExperimentItem> loadSharedPreferencesLogList(Context context) {
        ArrayList<ExperimentItem> callLog = new ArrayList<ExperimentItem>();
        SharedPreferences mPrefs = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);
        Gson gson = new Gson(); String json = mPrefs.getString("myJson", "");
        if (!json.isEmpty()){
            Type type = new TypeToken<List<ExperimentItem>>() {}.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    public static void saveSharedPreferencesLogList(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(fields);
        prefsEditor.putString("myJson", json);
        prefsEditor.commit();
    }


    public void addListener(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View window = inflater.inflate(R.layout.name_dialogue_layout, null);
        AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(this);
        DialogBuilder.setView(window);

        EditText editName = (EditText) window.findViewById(R.id.experiment_name);
        editName.setText("новый эксперимент");
        DialogBuilder.setPositiveButton("ОК",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fields.add(0, new ExperimentItem(editName.getText().toString()));
                        mainAdapter.notifyDataSetChanged();
                        Intent intent = new Intent(MainActivity.this, ExperimentActivity.class);
                        intent.putExtra("position", 0);
                        startActivity(intent);
                    }
                });
        DialogBuilder.setNegativeButton("отмена",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        DialogBuilder.create().show();
    }

    public void itemListener(View view) {
        Intent intent = new Intent(MainActivity.this, ExperimentActivity.class);
        startActivity(intent);
        mainAdapter.notifyDataSetChanged();
    }
}