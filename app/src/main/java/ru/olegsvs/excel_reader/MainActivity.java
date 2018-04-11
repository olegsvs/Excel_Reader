package ru.olegsvs.excel_reader;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    HSSFWorkbook workbook;
    Row row;
    HSSFSheet sheet;
    private RecyclerView excelRecycler;
    List<ExcelItem> excelItems = new ArrayList<ExcelItem>();

    int numberOfSheets;
    String[] sheetNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        excelRecycler = findViewById(R.id.excel_book);
        excelRecycler.setNestedScrollingEnabled(false);

        if (NetworkUtils.isNetworkAvailable(this)) {
            DownloadTask dt = new DownloadTask();
            dt.execute();
        } else {
            File file = new File("/data/data/ru.olegsvs.excel_reader/xls");
            if(file.exists()) {
                Toast.makeText(this, "Проверьте свое интернет соединение!\nБудет произведена попытка загрузить локальную копию.", Toast.LENGTH_LONG).show();
                onReadClick(-1);
                loadSpinner();
            } else Toast.makeText(this, "Проверьте свое интернет соединение!", Toast.LENGTH_LONG).show();
        }
    }

    private class DownloadTask extends AsyncTask< Void, Void, Void >  {

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://getfile.dokpub.com/yandex/get/https://yadi.sk/i/GfG-WHJu3T2ZJx";
            try {
                saveUrl("/data/data/ru.olegsvs.excel_reader/xls", url);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("ExcelReader", "DownloadTask: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v)  {
            super.onPostExecute(v);
            onReadClick(-1);
            loadSpinner();
        }
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
            Log.i("ExcelReader", "onReadClick1: " + numberOfSheets);

            for (int i = 0; i < numberOfSheets; i++) {
                Log.i("ExcelReader", "onReadClick2: " + workbook.getSheetName(i));
                sheetNames[i] = workbook.getSheetName(i);
            }

            if(t==-1)
                sheet = workbook.getSheetAt(numberOfSheets-1);
            else sheet = workbook.getSheetAt(t);

            loadCells();
        } catch (Exception e) {
            /* proper exception handling to be here */
            Log.i("ExcelReader", "onReadClick3: " + e.toString());
        }
    }

    private void loadDates() {
        Log.i("IDDQD2", "loadDates: " + sheetNames[0]);
        Log.i("IDDQD2", "loadDates: " + sheetNames[sheetNames.length-1]);
    }

    private void loadCells() {
        excelItems.clear();
        for (int i = 0; i < 50; i++) {
            if(!loadRow(1, i).equals("")) {
                ExcelItem excelItem = new ExcelItem();
                excelItem.setCountry(loadRow(1, i));
                excelItem.setSended8(loadRow(2, i));
                excelItem.setSended16(loadRow(3, i));
                excelItem.setOutput(loadRow(4, i));
                excelItems.add(excelItem);
            }
        }
        (findViewById(R.id.progressBar)).setVisibility(View.GONE);

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
            value = ""+cell.getStringCellValue();
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.i("ExcelReader", "getCellAsString: " + e.toString());        }
        return value;
    }

    private void loadSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.sheets);
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
    }
}
