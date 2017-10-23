package ru.olegsvs.excel_reader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    HSSFWorkbook workbook;
    Row row;
    HSSFSheet sheet;
    FormulaEvaluator formulaEvaluator;

    TextView tvIzhevsk8,tvIzhevsk16,tvIzhevskMinus;

    TextView tvAgr8,tvAgr16,tvAgrMinus;

    TextView tvChy8,tvChy16,tvChyMinus;

    TextView tvPerm8,tvPerm16,tvPermMinus;

    TextView tvNeft8,tvNeft16,tvNeftMinus;

    TextView tvTotal8,tvTotal16,tvTotalMinus;

    TextView tvSBM8,tvSBM16,tvSBMMinus;

    TextView tvSarapul8,tvSarapul16,tvSarapulMinus;

    int numberOfSheets;
    String[] sheetNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
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
        spinner.setSelection(sheetNames.length-1);
    }

    private class DownloadTask extends AsyncTask< Void, Void, Void >  {

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "";
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

    private void findViews() {
        tvIzhevsk8 = (TextView) findViewById(R.id.Izhevsk8);
        tvIzhevsk16 = (TextView) findViewById(R.id.Izhevsk16);
        tvIzhevskMinus = (TextView) findViewById(R.id.IzhevskMinus);

        tvSBM8 = (TextView) findViewById(R.id.SBM8);
        tvSBM16 = (TextView) findViewById(R.id.SBM16);
        tvSBMMinus = (TextView) findViewById(R.id.SBMMinus);

        tvAgr8 = (TextView) findViewById(R.id.Agr8);
        tvAgr16 = (TextView) findViewById(R.id.Agr16);
        tvAgrMinus = (TextView) findViewById(R.id.AgrMinus);

        tvChy8 = (TextView) findViewById(R.id.Chy8);
        tvChy16 = (TextView) findViewById(R.id.Chy16);
        tvChyMinus = (TextView) findViewById(R.id.ChyMinus);

        tvPerm8 = (TextView) findViewById(R.id.Perm8);
        tvPerm16 = (TextView) findViewById(R.id.Perm16);
        tvPermMinus = (TextView) findViewById(R.id.PermMinus);

        tvNeft8 = (TextView) findViewById(R.id.Neft8);
        tvNeft16 = (TextView) findViewById(R.id.Neft16);
        tvNeftMinus = (TextView) findViewById(R.id.NeftMinus);

        tvTotal8 = (TextView) findViewById(R.id.Total8);
        tvTotal16 = (TextView) findViewById(R.id.Total16);
        tvTotalMinus = (TextView) findViewById(R.id.TotalMinus);

        tvSarapul8 = (TextView) findViewById(R.id.Sarapul8);
        tvSarapul16 = (TextView) findViewById(R.id.Sarapul16);
        tvSarapulMinus = (TextView) findViewById(R.id.SarapulMinus);

    }

    public void onReadClick(int t) {
        File excelFile = new File("/data/data/ru.olegsvs.excel_reader/xls");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(excelFile);
            workbook = new HSSFWorkbook(fis);
            numberOfSheets = workbook.getNumberOfSheets();
            sheetNames = new String[numberOfSheets];
            Log.i("ExcelReader", "onReadClick: " + numberOfSheets);

            for (int i = 0; i < numberOfSheets; i++) {
                Log.i("ExcelReader", "onReadClick: " + workbook.getSheetName(i));
                sheetNames[i] = workbook.getSheetName(i);
            }

            if(t==-1)
            sheet = workbook.getSheetAt(numberOfSheets-1);
            else sheet = workbook.getSheetAt(t);
            formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            loadCells();
        } catch (Exception e) {
            /* proper exception handling to be here */
            Log.i("ExcelReader", "onReadClick: " + e.toString());
        }
    }

    private void loadCells() {

        //Izhevsk
        tvIzhevsk8.setText(loadRow(2,3));
        tvIzhevsk16.setText(loadRow(3,3));
        tvIzhevskMinus.setText(loadRow(4,3));

        //SBM
        tvSBM8.setText(loadRow(2,4));
        tvSBM16.setText(loadRow(3,4));
        tvSBMMinus.setText(loadRow(4,4));

        //Agriz
        tvAgr8.setText(loadRow(2,5));
        tvAgr16.setText(loadRow(3,5));
        tvAgrMinus.setText(loadRow(4,5));

        //Chaykovskiy
        tvChy8.setText(loadRow(2,6));
        tvChy16.setText(loadRow(3,6));
        tvChyMinus.setText(loadRow(4,6));

        //Perm
        tvPerm8.setText(loadRow(2,7));
        tvPerm16.setText(loadRow(3,7));
        tvPermMinus.setText(loadRow(4,7));

        //Neftekamsk
        tvNeft8.setText(loadRow(2,8));
        tvNeft16.setText(loadRow(3,8));
        tvNeftMinus.setText(loadRow(4,8));

        //Sarapul
        tvSarapul8.setText(loadRow(2,9));
        tvSarapul16.setText(loadRow(3,9));
        tvSarapulMinus.setText(loadRow(4,9));

        //Total
        tvTotal8.setText(loadRow(2,10));
        tvTotal16.setText(loadRow(3,10));
        tvTotalMinus.setText(loadRow(4,10));

    }

    private String loadRow(int i, int i1) {
        row = sheet.getRow(i);
        String value = getCellAsString(row, i1, formulaEvaluator);
        if (value.equals("")) value = "0";
        return value;
    }

    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
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
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.i("ExcelReader", "getCellAsString: " + e.toString());        }
        return value;
    }
}
