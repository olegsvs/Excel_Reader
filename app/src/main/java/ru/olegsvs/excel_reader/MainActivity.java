package ru.olegsvs.excel_reader;

<<<<<<< HEAD
import android.content.Intent;
=======
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
>>>>>>> newbranch
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
<<<<<<< HEAD
    FormulaEvaluator formulaEvaluator;

    TextView tvIzhevsk8, tvIzhevsk16, tvIzhevskMinus;

    TextView tvAgr8, tvAgr16, tvAgrMinus;

    TextView tvChy8, tvChy16, tvChyMinus;

    TextView tvPerm8, tvPerm16, tvPermMinus;

    TextView tvNeft8, tvNeft16, tvNeftMinus;

    TextView tvTotal8, tvTotal16, tvTotalMinus;

    TextView tvSBM8, tvSBM16, tvSBMMinus;

    TextView tvSarapul8, tvSarapul16, tvSarapulMinus;
=======
    private RecyclerView excelRecycler;
    List<ExcelItem> excelItems = new ArrayList<ExcelItem>();
    private SharedPreferences prefs;
    private String URL;
>>>>>>> newbranch

    int numberOfSheets;
    String[] sheetNames;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        (findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        findViews();
=======
        excelRecycler = findViewById(R.id.excel_book);
        excelRecycler.setNestedScrollingEnabled(false);

        prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        URL ="https://getfile.dokpub.com/yandex/get/" + prefs.getString("URL", "");

>>>>>>> newbranch
        if (NetworkUtils.isNetworkAvailable(this)) {
            DownloadTask dt = new DownloadTask();
            dt.execute();
        } else {
            File file = new File("/data/data/ru.olegsvs.excel_reader/xls");
            if (file.exists()) {
                Toast.makeText(this, "Проверьте свое интернет соединение!\nБудет произведена попытка загрузить локальную копию.", Toast.LENGTH_LONG).show();
                onReadClick(-1);
                loadSpinner();
<<<<<<< HEAD
            } else
                Toast.makeText(this, "Проверьте свое интернет соединение!", Toast.LENGTH_LONG).show();
        }
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
=======
            } else Toast.makeText(this, "Проверьте свое интернет соединение или ссылку на файл!", Toast.LENGTH_LONG).show();
        }
    }

    public void onLogoClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
>>>>>>> newbranch
    }

    private class DownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                saveUrl("/data/data/ru.olegsvs.excel_reader/xls", URL);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("ExcelReader", "DownloadTask: " + e.toString());
                Toast.makeText(MainActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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

<<<<<<< HEAD
            if (t == -1)
                sheet = workbook.getSheetAt(numberOfSheets - 1);
=======
            if(t==-1)
                sheet = workbook.getSheetAt(numberOfSheets-1);
>>>>>>> newbranch
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

<<<<<<< HEAD
        //Izhevsk
        tvIzhevsk8.setText(loadRow(2, 3));
        tvIzhevsk16.setText(loadRow(3, 3));
        tvIzhevskMinus.setText(loadRow(4, 3));

        //SBM
        tvSBM8.setText(loadRow(2, 4));
        tvSBM16.setText(loadRow(3, 4));
        tvSBMMinus.setText(loadRow(4, 4));

        //Agriz
        tvAgr8.setText(loadRow(2, 5));
        tvAgr16.setText(loadRow(3, 5));
        tvAgrMinus.setText(loadRow(4, 5));

        //Chaykovskiy
        tvChy8.setText(loadRow(2, 6));
        tvChy16.setText(loadRow(3, 6));
        tvChyMinus.setText(loadRow(4, 6));

        //Perm
        tvPerm8.setText(loadRow(2, 7));
        tvPerm16.setText(loadRow(3, 7));
        tvPermMinus.setText(loadRow(4, 7));

        //Neftekamsk
        tvNeft8.setText(loadRow(2, 8));
        tvNeft16.setText(loadRow(3, 8));
        tvNeftMinus.setText(loadRow(4, 8));

        //Sarapul
        tvSarapul8.setText(loadRow(2, 9));
        tvSarapul16.setText(loadRow(3, 9));
        tvSarapulMinus.setText(loadRow(4, 9));

        //Total
        tvTotal8.setText(loadRow(2, 10));
        tvTotal16.setText(loadRow(3, 10));
        tvTotalMinus.setText(loadRow(4, 10));
=======
>>>>>>> newbranch

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
<<<<<<< HEAD
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        String formattedDouble = String.format("%.2f", numericValue);
                        value = formattedDouble;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.i("ExcelReader", "getCellAsString: " + e.toString());
        }
        return value;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        menu.add("Сменить пароль");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.putExtra("type", 1);
        startActivity(loginIntent);
        return super.onOptionsItemSelected(item);
=======
            cell.setCellType(Cell.CELL_TYPE_STRING);
            value = ""+cell.getStringCellValue();
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.i("IDDQD", "getCellAsString: " + e.toString());        }
        return value;
    }

    private void loadSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.sheets);

        if(sheetNames == null) {
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
>>>>>>> newbranch
    }
}
