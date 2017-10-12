package ru.olegsvs.excel_reader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        onReadClick(null);
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
    }

    public void onReadClick(View view) {
        InputStream stream = getResources().openRawResource(R.raw.test1);
        try {
            workbook = new HSSFWorkbook(stream);
            sheet = workbook.getSheetAt(4);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            loadCells();
        } catch (Exception e) {
            /* proper exception handling to be here */
//            printlnToUser(e.toString());
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

        //Total
        tvTotal8.setText(loadRow(2,9));
        tvTotal16.setText(loadRow(3,9));
        tvTotalMinus.setText(loadRow(4,9));
    }

    private String loadRow(int i, int i1) {
        row = sheet.getRow(i);
        String value = getCellAsString(row, i1, formulaEvaluator);
        if (value.equals("")) value = "0";
        return value;
    }

//    public void onWriteClick(View view) {
//        printlnToUser("writing xlsx file");
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("mysheet"));
//        for (int i=0;i<10;i++) {
//            Row row = sheet.createRow(i);
//            Cell cell = row.createCell(0);
//            cell.setCellValue(i);
//        }
//        String outFileName = "filetoshare.xlsx";
//        try {
//            printlnToUser("writing file " + outFileName);
//            File cacheDir = getCacheDir();
//            File outFile = new File(cacheDir, outFileName);
//            OutputStream outputStream = new FileOutputStream(outFile.getAbsolutePath());
//            workbook.write(outputStream);
//            outputStream.flush();
//            outputStream.close();
//            printlnToUser("sharing file...");
//            share(outFileName, getApplicationContext());
//        } catch (Exception e) {
//            /* proper exception handling to be here */
//            printlnToUser(e.toString());
//        }
//    }

    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            Log.i("DribbbleApp", "getCellAsString: cell" + cellValue);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    Log.i("DribbbleApp", "getCellAsString: numeric" + numericValue);
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
//            printlnToUser(e.toString());
        }
        return value;
    }

//    /**
//     * print line to the output TextView
//     * @param str
//     */
//    private void printlnToUser(String str) {
//        final String string = str;
//        if (output.length()>8000) {
//            CharSequence fullOutput = output.getText();
//            fullOutput = fullOutput.subSequence(5000,fullOutput.length());
//            output.setText(fullOutput);
//            output.setSelection(fullOutput.length());
//        }
//        output.append(string+"\n");
//    }


}
