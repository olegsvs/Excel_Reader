package ru.olegsvs.excel_reader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private EditText urlEdit;
    private EditText col1Edit;
    private EditText col2Edit;
    private EditText col3Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        urlEdit = findViewById(R.id.url_edit);
        col1Edit = findViewById(R.id.col1_edit);
        col2Edit = findViewById(R.id.col2_edit);
        col3Edit = findViewById(R.id.col3_edit);

        prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        urlEdit.setText(prefs.getString("URL", "https://yadi.sk/i/GfG-WHJu3T2ZJx"));
        col1Edit.setText(prefs.getString("COL1", ""));
        col2Edit.setText(prefs.getString("COL2", ""));
        col3Edit.setText(prefs.getString("COL3", ""));
    }

    public void onSaveClick(View view) {
        prefs.edit().putString("URL", urlEdit.getText().toString()).apply();
        prefs.edit().putString("COL1", col1Edit.getText().toString()).apply();
        prefs.edit().putString("COL2", col2Edit.getText().toString()).apply();
        prefs.edit().putString("COL3", col3Edit.getText().toString()).apply();
        File file = new File("/data/data/ru.olegsvs.excel_reader/xls");
        file.delete();
        onCancelClick(null);
    }

    public void onCancelClick(View view) {
        Intent goToMain = new Intent(this, MainActivity.class);
        goToMain.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goToMain);
    }
}
