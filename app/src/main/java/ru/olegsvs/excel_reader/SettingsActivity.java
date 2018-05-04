package ru.olegsvs.excel_reader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private EditText urlEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        urlEdit = findViewById(R.id.url_edit);

        prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        urlEdit.setText(prefs.getString("URL", "https://yadi.sk/i/GfG-WHJu3T2ZJx"));
    }

    public void onSaveClick(View view) {
        prefs.edit().putString("URL", urlEdit.getText().toString()).apply();
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
