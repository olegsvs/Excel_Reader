package ru.olegsvs.excel_reader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HSSFWorkbook workbook;
    Row row;
    HSSFSheet sheet;
    List<ExcelItem> excelItems = new ArrayList<ExcelItem>();
    int numberOfSheets;
    String[] sheetNames;
    private RecyclerView excelRecycler;
    private SharedPreferences prefs;
    private String URL, COL1, COL2, COL3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        excelRecycler = findViewById(R.id.excel_book);
        excelRecycler.setNestedScrollingEnabled(false);

        prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        URL = "https://getfile.dokpub.com/yandex/get/" + prefs.getString("URL", "");
        COL1 = prefs.getString("COL1", "");
        COL2 = prefs.getString("COL2", "");
        COL3 = prefs.getString("COL3", "");

        if (NetworkUtils.isNetworkAvailable(this)) {
            DownloadTask dt = new DownloadTask();
            dt.execute();
        } else {
            File file = new File("/data/data/ru.olegsvs.excel_reader/xls");
            if (file.exists()) {
                Toast.makeText(this, "Проверьте свое интернет соединение!\nБудет произведена попытка загрузить локальную копию.", Toast.LENGTH_LONG).show();
                onReadClick(-1);
                loadSpinner();
            } else
                Toast.makeText(this, "Проверьте свое интернет соединение или ссылку на файл!", Toast.LENGTH_LONG).show();
        }
    }

    public void onLogoClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void saveUrl(final String filename, final String urlString)
            throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(filename);

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }

    public void onReadClick(int t) {
        File excelFile = new File("/data/data/ru.olegsvs.excel_reader/xls");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(excelFile);
            workbook = new HSSFWorkbook(fis);
            numberOfSheets = workbook.getNumberOfSheets();
            sheetNames = new String[numberOfSheets];

            for (int i = 0; i < numberOfSheets; i++) {
                sheetNames[i] = workbook.getSheetName(i);
            }

            if (t == -1)
                sheet = workbook.getSheetAt(numberOfSheets - 1);
            else sheet = workbook.getSheetAt(t);

            loadCells();
        } catch (Exception e) {
            /* proper exception handling to be here */
            Log.i("ExcelReader", "onReadClick3: " + e.toString());
        }
    }

    private void loadCells() {
        excelItems.clear();
        for (int i = 0; i < 50; i++) {
            if (!loadRow(1, i).equals("")) {
                ExcelItem excelItem = new ExcelItem();
                excelItem.setCountry(loadRow(1, i));
                excelItem.setSended8(loadRow(2, i));
                excelItem.setSended16(loadRow(3, i));
                excelItem.setOutput(loadRow(4, i));

                if (COL1.isEmpty()) COL1 = loadRow(2, 1);
                if (COL2.isEmpty()) COL2 = loadRow(3, 1);
                if (COL3.isEmpty()) COL3 = loadRow(4, 1);
                excelItem.setSended8caption(COL1);
                excelItem.setSended16caption(COL2);
                excelItem.setCutCaption(COL3);
                excelItems.add(excelItem);
            }
        }
        (findViewById(R.id.progressBar)).setVisibility(View.GONE);
        (findViewById(R.id.sheets)).setVisibility(View.VISIBLE);

        ExcelBookAdapter adapter = new ExcelBookAdapter(excelItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        excelRecycler.setLayoutManager(layoutManager);
        excelRecycler.setAdapter(adapter);


    }

    private String loadRow(int i, int i1) {
        row = sheet.getRow(i);
        String value = getCellAsString(row, i1);
        return value;
    }

    protected String getCellAsString(Row row, int c) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            value = "" + cell.getStringCellValue();
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.i("IDDQD", "getCellAsString: " + e.toString());
        }
        return value;
    }

    private void loadSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.sheets);

        if (sheetNames == null) {
            Toast.makeText(this, "Проверьте ссылку на XLS", Toast.LENGTH_LONG).show();
            onLogoClick(null);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sheetNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onReadClick(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinner.setOnItemSelectedListener(itemSelectedListener);
        spinner.setSelection(sheetNames.length - 1);
        (findViewById(R.id.progressBar)).setVisibility(View.GONE);
        (findViewById(R.id.sheets)).setVisibility(View.VISIBLE);
    }

    private class DownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                saveUrl("/data/data/ru.olegsvs.excel_reader/xls", URL);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("ExcelReader", "DownloadTask: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            onReadClick(-1);
            loadSpinner();
        }
    }
}
